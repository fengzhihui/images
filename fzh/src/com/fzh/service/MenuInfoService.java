package com.fzh.service;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import com.fzh.dao.MenuInfoDAO;
import com.fzh.dao.MenuKeySetDAO;
import com.fzh.dao.WeiXinPubNoListDAO;
import com.fzh.entity.MenuInfo;
import com.fzh.entity.MenuKeyset;
import com.fzh.entity.WeixinPubnoList;
import com.fzh.wx.pojo.menupojo.AccessToken;
import com.fzh.wx.pojo.menupojo.ZTreeBean;
import com.fzh.wx.util.WeixinUtil;

@Service
public class MenuInfoService {
	private static final Logger logger = LoggerFactory.getLogger(MenuInfoService.class);
	@Autowired
	private MenuInfoDAO menuInfoDAO;
	@Autowired
	private MenuKeySetDAO menuKeySetDAO;
	@Autowired
	private WeiXinPubNoListDAO weiXinPubNoListDAO;
	
	/**
	 * 发布菜单
	 * @param wxNo
	 * @param jsondata
	 * @return
	 */
	public String createMenu(String wxNo, String jsondata) {
		WeixinPubnoList wxpListVO = weiXinPubNoListDAO.findByWxNo(wxNo);
		if(wxpListVO != null && jsondata != null){
			if(wxpListVO.getAppid()==null || wxpListVO.getAppsecret()==null){
				return "un_authorize";
			}
			//获取access_token
			AccessToken accessToken = WeixinUtil.getAccessToken(wxpListVO.getAppid(), wxpListVO.getAppsecret());
			int result = -1;
			if(accessToken != null){
				jsondata = jsondata.replaceAll("},]", "}]");
				//System.out.println("jsondata = "+jsondata);
				result = WeixinUtil.createMenu(jsondata, accessToken.getToken());
				if(result == 0){
					logger.info("菜单创建成功! " + accessToken.getToken());
					return "yes";
				}
			}
		}
		return "no";
	}
	
	/**
	 * 操作菜单状态
	 * @param wxno
	 * @param treedata
	 * @return
	 * @throws Exception
	 */
	public boolean operateMenuStatus(String wxno, String treedata) throws Exception {
		MenuInfo menuInfoVO = menuInfoDAO.doFindByWxNo(wxno);
		if(menuInfoVO != null){
			//有修改
			if(!menuInfoVO.getVchar1().equals(treedata)){
				menuKeySetDAO.deleteByWxNo(wxno);
				return true;
			}
			//全部删除
			if(treedata.length()==53 && treedata.contains("id:0")){
				menuInfoDAO.deleteByWxNo(wxno);
				menuKeySetDAO.deleteByWxNo(wxno);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 保存菜单
	 * @param wxno
	 * @param username
	 * @param treedata
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public void saveMenu(String wxno, String username, String treedata, String str) throws Exception {
		boolean status = operateMenuStatus(wxno, treedata);
		if(!status) return;
		//修改菜单
		String[] pidsArr = new String[3];
		List<ZTreeBean> treeList = setPidsAndTreeList(treedata, pidsArr);//提取菜单父级id
		
		String duiying = str;
		String[] strArr = null;
		if(duiying != null && !"".equals(duiying)){
			strArr = duiying.split("[$]");
		}
		long[] orderNoArr = { 0, 5, 10 };// { idx1, idx2, idx3 }
		for (ZTreeBean zree : treeList) {
			if(zree == null || "0".equals(zree.getId())) continue;
			//保存或更新菜单
			saveOrUpdateMenuInfo(zree, wxno, treedata, strArr, pidsArr, orderNoArr);
			//保存菜单回复内容
			saveMenuKeySet(zree, wxno, username);
		}
	}
	
	/**
	 * 提取菜单父级id
	 * @param treedata
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ZTreeBean> setPidsAndTreeList(String treedata, String[] pids){
		treedata = "[" + treedata.replaceAll("open:true,", "").replaceAll("\r\n", "\\\\r\\\\n") + "]";
		JSONArray jsonArray = JSONArray.fromObject(treedata);
		List<ZTreeBean> treeList = JSONArray.toList(jsonArray, ZTreeBean.class);
		int j = 0;
		for(ZTreeBean zree : treeList){
			if(zree == null) continue;
			if(!"0".equals(zree.getId()) && "0".equals(zree.getPid())){
				pids[j] = zree.getId();
				j ++;
			}
		}
		return treeList;
	}
	
	/**
	 * 保存菜单回复内容
	 * @param zree
	 * @param wxno
	 * @param username
	 */
	public void saveMenuKeySet(ZTreeBean zree,String wxno,String username){
		//菜单回复内容
		MenuKeyset keySetVO = new MenuKeyset();
		keySetVO.setCreateTime(new Date());
		keySetVO.setKeyWord(zree.getId());
		keySetVO.setReType(zree.getType());
		keySetVO.setCreateUser(username);
		keySetVO.setMenuId(Long.parseLong(zree.getId()));//据此查询菜单点击事件回复内容
		keySetVO.setWeixinPublicNo(wxno);
		if("1".equals(zree.getType())){
			keySetVO.setRefText(zree.getContent());
		}else if("2".equals(zree.getType()) && !StringUtil.isBlank(zree.getContent())){
			//zree.getContent()为图文id
			keySetVO.setRefImageTextId(zree.getContent());//据此查询菜单点击跳转图文链接
		}
		menuKeySetDAO.save(keySetVO);
	}
	
	/**
	 * 保存或更新菜单
	 * @param zree
	 * @param wxno
	 * @param treedata
	 * @param strArr
	 * @param pidsArr
	 * @param orderNoArr
	 * @throws Exception
	 */
	public void saveOrUpdateMenuInfo(ZTreeBean zree, String wxno, String treedata,
			String[] strArr, String[] pidsArr, long[] orderNoArr) throws Exception {
		//菜单信息
		MenuInfo mInfoVO = new MenuInfo();
		MenuInfo vo = menuInfoDAO.doFindByMenuId(zree.getId(), wxno);
		if(vo != null){
			mInfoVO = vo;
		}
		
		if(strArr != null && strArr.length > 0){
			for (int i = 0; i < strArr.length; i++) {
				if(StringUtil.isBlank(zree.getId()) || StringUtil.isBlank(strArr[i])) continue;
				if("2".equals(zree.getType()) && zree.getId().equals(strArr[i])){
					mInfoVO.setVchar3(strArr[i+1].trim());
				}
				i++;
			}
		}
		//根据父级菜单序号记录子菜单序号
		if(!"0".equals(zree.getPid())){
			String temp = zree.getPid();
			if(pidsArr[0] !=null && temp.equals(pidsArr[0])){
				orderNoArr[0] ++;
				mInfoVO.setOrderNo(orderNoArr[0]);
			}else if(pidsArr[1] !=null && temp.equals(pidsArr[1])){
				orderNoArr[1] ++;
				mInfoVO.setOrderNo(orderNoArr[1]);
			}else if(pidsArr[2] !=null && temp.equals(pidsArr[2])){
				orderNoArr[2] ++;
				mInfoVO.setOrderNo(orderNoArr[2]);
			}
		}
		mInfoVO.setUrl(zree.getUrl());
		mInfoVO.setOpTime(new Date());
		mInfoVO.setWeixinPublicNo(wxno);
		mInfoVO.setMenuName(zree.getName());
		mInfoVO.setMenuType(zree.getType());//1为文本、2为图文、3为链接
		mInfoVO.setParentMenuId(zree.getPid()!=null?Long.parseLong(zree.getPid()):null);
		mInfoVO.setVchar1("[" + treedata.replaceAll("pid:0", "pid:0,open:true") + "]");//zTreeNodes字串
		
		//图文修改成其他
		if(!"2".equals(zree.getType()) 
				&& mInfoVO.getVchar3() != null 
				&& !"".equals(mInfoVO.getVchar3())){
			mInfoVO.setVchar3("");
		}
		//链接修改成其他
		if(!"3".equals(zree.getType()) 
				&& mInfoVO.getUrl() != null 
				&& !"".equals(mInfoVO.getUrl())){
			mInfoVO.setUrl("");
		}
		
		if(vo != null){
			mInfoVO.setMenuId(vo.getMenuId());
			this.update(mInfoVO);
		} else {
			mInfoVO.setMenuId(Integer.parseInt(zree.getId()));
			this.save(mInfoVO);
		}
	}
	
	public void save(MenuInfo menuInfo) throws Exception{
		menuInfoDAO.save(menuInfo);
	}
	
	public MenuInfo findByPk(String pk) throws Exception{
		return menuInfoDAO.findByPK(pk);
	}
	
	public void update(MenuInfo menuInfo){
		menuInfoDAO.update(menuInfo, "id");
	}
	
	public void delete(int id){
		menuInfoDAO.deleteMenuInfo(id);
	}
	
	public void deleteByWxNo(String wxno){
		menuInfoDAO.deleteByWxNo(wxno);
	}
	
	public List<MenuInfo> doFindMenuByWxNo(String wxNo){
		return menuInfoDAO.doFindMenuByWxNo(wxNo);
	}
	
	public MenuInfo doFindByMenuId(String menuId,String wxno) {
		return menuInfoDAO.doFindByMenuId(menuId, wxno);
	}
	
	public MenuInfo doFindByWxNo(String wxNo) throws Exception {
		return menuInfoDAO.doFindByWxNo(wxNo);
	}
	
	public List<MenuInfo> findByWxNo(String wxNo){
		return menuInfoDAO.findByWxNo(wxNo);
	}
	
	public String doFindByUrl(String wxno,String url){
		return menuInfoDAO.doFindByUrl(wxno, url);
	}
	
	public List<String> findParentMenuList(String wxno){
		return menuInfoDAO.findParentMenuList(wxno);
	}
	
}
