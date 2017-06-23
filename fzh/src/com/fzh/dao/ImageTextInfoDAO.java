package com.fzh.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fzh.entity.ImageTextInfo;
import com.fzh.jdbctemplate.JDBCUtil;

@Repository
public class ImageTextInfoDAO extends JDBCUtil {
    
	public int save(ImageTextInfo imageTextInfo) {
		return this.insert(imageTextInfo);
	}

	/**
	 * 保存对象并返回主键
	 * @param imageTextInfo
	 * @return
	 * @throws Exception
	 */
	public Long saveAndGetId(ImageTextInfo imageTextInfo) throws Exception {
		return this.insertAndGetId(imageTextInfo);
	}

	public int update(ImageTextInfo imageTextInfo) {
		return this.update(imageTextInfo, "image_text_no");
	}
	
	public int delete(String imageTextNo) {
		String sql = "delete from image_text_info where image_text_no=?";
		return this.jdbcTemplate.update(sql, new Object[]{ imageTextNo });
	}

	public ImageTextInfo findByPK(String imageTextNo) {
		String sql = "select * from image_text_info where image_text_no = ?";
		return this.getObject(ImageTextInfo.class, sql, new Object[]{ imageTextNo });
	}

	/**
	 * 图文分页
	 * @param page
	 * @param length
	 * @param wxPublicNo
	 * @return
	 */
	public List<ImageTextInfo> findImageTextList(int start, int length, String wxPublicNo){
		String sql = "select * from image_text_info i where i.weixin_public_no = ? order by i.create_time desc limit ?, ?";
		return this.getListObject(ImageTextInfo.class, sql, new Object[]{ wxPublicNo, start, length});
	}
	
	public int findImageTextCount(String wxPublicNo) {
		String sql = "select count(image_text_no) as count from image_text_info where weixin_public_no = ?";
		return this.getCounts(sql, new Object[]{ wxPublicNo });
	}

	public ImageTextInfo findByPKNoContent(String imageTextNo) {
		String sql = "select * from imagetextinfo i where i.imagetextno = ?";
		return this.getObject(ImageTextInfo.class, sql, new Object[]{ imageTextNo });
	}

	public List<Map<String, Object>> doFindImageText4Select(Map<String, Object> params) throws Exception {
    	String wxPublicNo = (String) params.get("wxPublicNo");
    	List<Map<String, Object>> imageTextList = new ArrayList<Map<String, Object>>();
    	List<ImageTextInfo> list = findByWxPublicNo(wxPublicNo);
    	for(ImageTextInfo info : list){
    		Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("rid", info.getImageTextNo());
			tempMap.put("title", info.getTitle());
			imageTextList.add(tempMap);
    	}
    	return imageTextList;
    }
	
	public List<ImageTextInfo> findByWxPublicNo(String wxPublicNo) {
		String sql = "select * from image_text_info i where i.weixin_public_no = ? order by i.create_time desc limit 1";
		return this.getListObject(ImageTextInfo.class, sql, new Object[]{ wxPublicNo });
	}
	
}
