package com.fzh.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.ImageTextInfoDAO;
import com.fzh.dao.ImageTextMoreDAO;
import com.fzh.entity.ImageTextInfo;

@Service
public class ImageTextInfoService {
	@Autowired
	private ImageTextInfoDAO imageTextInfoDAO;
	@Autowired
	private ImageTextMoreDAO imageTextMoreDAO;
	
	public int save(ImageTextInfo imageTextInfo){
		return imageTextInfoDAO.save(imageTextInfo);
	}
	
	public void delete(String imageTextNo){
		imageTextInfoDAO.delete(imageTextNo);
	}
	
	public ImageTextInfo findByPK(String imageTextNo){
		return imageTextInfoDAO.findByPK(imageTextNo);
	}
	
	public ImageTextInfo findImageText(String imageTextNo) throws Exception {
		ImageTextInfo mainVo = imageTextInfoDAO.findByPK(imageTextNo);
		if(mainVo == null) return null;
		if("2".equals(mainVo.getImageTextType())) {
			mainVo.setSubVoList(imageTextMoreDAO.findImageTextMoreList(mainVo.getImageTextNo()));
		}
		return mainVo;
	}
	
	public List<ImageTextInfo> findImageTextList(int start, int length, String wxPublicNo) throws Exception {
		List<ImageTextInfo> imageTextList = imageTextInfoDAO.findImageTextList(start, length, wxPublicNo);
		for(ImageTextInfo imgtxtInfo : imageTextList){
			// 多图文
			if("2".equals(imgtxtInfo.getImageTextType())){
				imgtxtInfo.setSubVoList(imageTextMoreDAO.findSubImageText(imgtxtInfo.getImageTextNo()));
			}
		}
		return imageTextList;
	}
	
	public int findImageTextCount(String wxPublicNo) throws Exception {
		return imageTextInfoDAO.findImageTextCount(wxPublicNo);
	}
	
	public List<Map<String, Object>> doFindImageText4Select(Map<String, Object> params) throws Exception {
		return imageTextInfoDAO.doFindImageText4Select(params);
	}

	public int update(ImageTextInfo imageTextInfo) {
		if(findByPK(imageTextInfo.getImageTextNo().toString()) != null) {
			return imageTextInfoDAO.update(imageTextInfo);
		}
		return 0;
	}
}
