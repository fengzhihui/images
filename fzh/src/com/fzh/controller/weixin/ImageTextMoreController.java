package com.fzh.controller.weixin;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.fzh.controller.PlatformController;
import com.fzh.entity.ImageTextInfo;
import com.fzh.entity.User;
import com.fzh.service.ImageTextInfoService;
import com.fzh.service.ImageTextMoreService;
import com.fzh.util.VeDate;
import com.fzh.wx.util.StringUtil;

/**
 * 多图文操作Controller
 * @author fzh
 *
 */
@Controller
@RequestMapping("/imagetextmore")
public class ImageTextMoreController extends PlatformController {
	@Autowired
	private ImageTextMoreService imageTextMoreService;
	@Autowired
	private ImageTextInfoService imageTextInfoService;
	
	/**
	 * 新增多图文页面
	 * @return
	 */
	@RequestMapping("/addUI")
	public ModelAndView addImageTextMoreUI() {
		ModelAndView mav = new ModelAndView(getBasePath() + "article_mul_detail");
		mav.addObject("currDate", VeDate.getDateYMD(new Date()));
		return mav;
	}
	
	/**
	 * 修改多图文页面
	 * @param rid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateUI/{imageTextNo}")
	public ModelAndView updateUI(@PathVariable("imageTextNo") String imageTextNo) throws Exception {
		ModelAndView mav = new ModelAndView(getBasePath() + "article_mul_detail_edit");
		mav.addObject("imageText", imageTextInfoService.findImageText(imageTextNo));
		return mav;
	}
	
	/**
	 * 创建或修改多图文
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveOrUpdate")
	public @ResponseBody boolean saveOrUpdateImageText(HttpServletRequest request) throws Exception {
		String action = request.getParameter("action");
		String jsonData = request.getParameter("jsonData");
		if(StringUtil.isBlank(jsonData) || StringUtil.isBlank(action)) return false;
		User user = getSessionUser(request);
		if("add".equals(action)) {
			return imageTextMoreService.saveImageText(jsonData, user.getUsername(), user.getWxpublicno());
		}
		String delResId = request.getParameter("delResId");
		return imageTextMoreService.updateImageText(jsonData, delResId, user.getUsername(), user.getWxpublicno());
	}
	
	/**
	 * 图文预览
	 * @throws Exception
	 */
	@RequestMapping("/imgPreview/{id}")
	public @ResponseBody JSONObject ImageTextPreView(@PathVariable("id") String id) throws Exception {
		JSONObject json = new JSONObject();
		json.put("success", false);
		if(!StringUtil.isBlank(id)){
			ImageTextInfo imageTextInfo = imageTextInfoService.findImageText(id);
			if(imageTextInfo == null) return json;
			json.put("success", true);
			json.put("imageTextInfo", imageTextInfo);
			json.put("createTime", VeDate.DateToString(imageTextInfo.getCreateTime()));
		}
		return json;
	}
	
	/**
	 * 选择图文列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectList")
	public ModelAndView selectImageTextList(HttpServletRequest request) throws Exception {
		int page = getPage(request.getParameter(PAGE));
		String wxno = getWeiXinPublicNo(request);
		ModelAndView mav = new ModelAndView("/menu/menuinfo/article");
		mav.addObject(PAGE, page);
		mav.addObject("totalCount", imageTextInfoService.findImageTextCount(wxno));
		mav.addObject(LIST, imageTextInfoService.findImageTextList(getPageStart(page), PAGE_SIZE, wxno));
		return mav;
	}

	@Override
	protected String getBasePath() {
		return "/material/imagetextmore/";
	}
	
}
