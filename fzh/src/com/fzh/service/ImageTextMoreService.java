package com.fzh.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.ImageTextInfoDAO;
import com.fzh.dao.ImageTextMoreDAO;
import com.fzh.entity.ImageTextInfo;
import com.fzh.entity.ImageTextMore;
import com.fzh.wx.util.StringUtil;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

@Service
public class ImageTextMoreService {
	private static final Logger logger = LoggerFactory.getLogger(ImageTextMoreService.class);
	@Autowired
	private ImageTextMoreDAO imageTextMoreDAO;
	@Autowired
	private ImageTextInfoDAO imageTextInfoDAO;
	
	public int saveImageTextMore(ImageTextMore imageTextMore){
		return imageTextMoreDAO.save(imageTextMore);
	}
	
	public ImageTextMore findByPK(String moreImageTextNo){
		return imageTextMoreDAO.findByPK(moreImageTextNo);
	}
	
	public List<ImageTextMore> findSubImageText(Long imageTextNo) {
		return imageTextMoreDAO.findSubImageText(imageTextNo);
	}
	
	/**保存多图文
	 * @param jsonData
	 * @param username
	 * @param wxno
	 * @return
	 * @throws Exception
	 */
	public boolean saveImageText(String jsonData, String username, String wxno) throws Exception{
		ImageTextInfo mainVo = new ImageTextInfo();
		List<ImageTextMore> subVoList = new ArrayList<ImageTextMore>();
		buildSaveImageText(mainVo, subVoList, jsonData, username, wxno);
		return createImageText(mainVo, subVoList);
	}
	
	/**
	 * 解析JSON，封装保存图文参数
	 * @param mainVo
	 * @param subVoList
	 * @param jsonData
	 * @param username
	 * @param wxno
	 */
	public void buildSaveImageText(ImageTextInfo mainVo, List<ImageTextMore> subVoList, String jsonData, String username, String wxno){
		Date now = new Date();
		try {
			JSONArray array = JSONArray.fromObject(jsonData);
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if (i == 0) { // 主图文
					mainVo.setCreateTime(now);
					mainVo.setImageTextType("2");
					mainVo.setOperator(username);
					mainVo.setWeixinPublicNo(wxno);
					mainVo.setTitle(obj.getString("title"));
					mainVo.setImageUrl(obj.getString("imageUrl"));
					mainVo.setMainText(obj.getString("mainText"));
					mainVo.setSourceUrl(obj.getString("sourceUrl"));
					mainVo.setClickOutUrl(obj.getString("clickOutUrl"));
				} else {      // 子图文
					ImageTextMore subVo = new ImageTextMore();
					subVo.setCreateTime(now);
					subVo.setWeixinPublicNo(wxno);
					subVo.setTitle(obj.getString("title"));
					subVo.setImageUrl(obj.getString("imageUrl"));
					subVo.setMainText(obj.getString("mainText"));
					subVo.setSourceUrl(obj.getString("sourceUrl"));
					subVo.setClickOutUrl(obj.getString("clickOutUrl"));
					subVoList.add(subVo);
				}
			}
		} catch (JSONException e) {
			try {
				e.printStackTrace();
			} catch (JSONException e2) {
				logger.info("非法的JSON字符串");
			}
		}
	}
	
	/**
	 * 创建多图文，先创建主图文再创建子图文
	 * @param mainVo
	 * @param subVoList
	 * @throws Exception
	 */
	private boolean createImageText(ImageTextInfo mainVo,List<ImageTextMore> subVoList) throws Exception {
		Long imageTextNo = imageTextInfoDAO.saveAndGetId(mainVo);
		if(imageTextNo == 0) return false;
		for(ImageTextMore subVo : subVoList) {
			subVo.setImageTextNo(imageTextNo);
			int suc = saveImageTextMore(subVo);
			if(suc <= 0) return false;
		}
		return true;
	}
	
	/**
	 * 解析JSON，封装修改图文参数
	 * @param mainVo
	 * @param subVoList
	 * @param jsonData
	 * @param username
	 * @param wxno
	 */
	public void buildeUpdateImageText(ImageTextInfo mainVo, List<ImageTextMore> subVoList, String jsonData, String username, String wxno){
		Date now = new Date();
		String imageTextNo = null;
		try {
			JSONArray array = JSONArray.fromObject(jsonData);
			for (int i = 0; array !=null && i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if(obj == null) continue;
				if (i == 0) { // 主图文
					imageTextNo = obj.getString("rid");
					if(!StringUtil.isBlank(imageTextNo)) {
						ImageTextInfo imageTextInfo = imageTextInfoDAO.findByPK(imageTextNo);
						if(imageTextInfo == null) return;
					}
					//修改
					mainVo.setCreateTime(now);
					mainVo.setImageTextType("2");
					mainVo.setImageTextNo(Long.parseLong(imageTextNo));
					mainVo.setTitle(obj.getString("title"));
					mainVo.setImageUrl(obj.getString("imageUrl"));
					mainVo.setMainText(obj.getString("mainText"));
					mainVo.setSourceUrl(obj.getString("sourceUrl"));
					mainVo.setClickOutUrl(obj.getString("clickOutUrl"));
				} else {      // 子图文
					String rid = obj.getString("rid");
					ImageTextMore subVo = new ImageTextMore();
					if(!StringUtil.isBlank(rid)) {
						subVo = findByPK(rid);
					} else {
						subVo.setOperator(username);
						subVo.setWeixinPublicNo(wxno);
						subVo.setImageTextNo(Long.parseLong(imageTextNo));
					}
					//修改或新增
					subVo.setCreateTime(now);
					subVo.setTitle(obj.getString("title"));
					subVo.setImageUrl(obj.getString("imageUrl"));
					subVo.setMainText(obj.getString("mainText"));
					subVo.setSourceUrl(obj.getString("sourceUrl"));
					subVo.setClickOutUrl(obj.getString("clickOutUrl"));
					subVoList.add(subVo);
				}
			}
		} catch (JSONException e) {
			try {
				e.printStackTrace();
			} catch (JSONException e2) {
				logger.info("非法的JSON字符串");
			}
		}
	}
	
	/**
	 * 删除多图文
	 * @param jsonData
	 * @param delResId
	 * @param username
	 * @param wxno
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean updateImageText(String jsonData, String delResId, String username, String wxno) throws Exception {
		ImageTextInfo mainVo = new ImageTextInfo();
		List<ImageTextMore> subVoList = new ArrayList<ImageTextMore>();
		buildeUpdateImageText(mainVo, subVoList, jsonData, username, wxno);
		List<String> delVoList = new ArrayList<String>();
		JSONArray delArray = JSONArray.fromObject(delResId);
		delVoList = JSONArray.toList(delArray);
		boolean bool = updateAndDelete(mainVo, subVoList, delVoList);
		return bool;
	}
	
	public boolean updateAndDelete(ImageTextInfo mainVo, List<ImageTextMore> subVoList, List<String> delVoList){
		if(mainVo.getImageTextNo() == null) return false;
		try {
			//更新主图文
			imageTextInfoDAO.update(mainVo);
			for(ImageTextMore subVo : subVoList) {
				if(subVo.getMoreImageTextNo() != null) {
					imageTextMoreDAO.update(subVo);//更新子图文
				} else {
					imageTextMoreDAO.save(subVo);  //新增子图文
				}
			}
			for (String rid : delVoList) {
				if(!StringUtil.isBlank(rid)) {
					//删除子图文
					imageTextMoreDAO.delete(Long.parseLong(rid));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
