package com.fzh.controller.weixin;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fzh.common.LoginValidate;
import com.fzh.controller.PlatformController;
import com.fzh.entity.ImageTextInfo;
import com.fzh.entity.User;
import com.fzh.service.ImageTextInfoService;
import com.fzh.service.ImageTextMoreService;
import com.fzh.util.VeDate;
import com.fzh.wx.util.StringUtil;

/**
 * 单图文操作Controller
 * @author fzh
 *
 */
@Controller
@RequestMapping("/imagetextinfo")
public class ImageTextInfoController extends PlatformController {
	@Autowired
	private ImageTextInfoService imageTextInfoService;
	@Autowired
	private ImageTextMoreService imageTextMoreService;
	
	/**
	 * 新增单图文页面
	 * @return
	 */
	@RequestMapping("/addUI")
	public ModelAndView addImageTextInfoUI() {
		ModelAndView mav = new ModelAndView(getBasePath() + "article_detail");
		mav.addObject("currDate", VeDate.getDateYMD(new Date()));
		return mav;
	}
	
	/**
	 * 修改单图文页面
	 * @param rid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateUI/{imageTextNo}")
	public ModelAndView updateImageTextInfoUI(@PathVariable("imageTextNo") String imageTextNo) throws Exception {
		ModelAndView mav = new ModelAndView(getBasePath() + "article_detail_edit");
		mav.addObject("imageTextInfo", imageTextInfoService.findByPK(imageTextNo));
		return mav;
	}
	
	/**
	 * 保存或修改单图文
	 * @param request
	 * @param imageTextInfo
	 * @return
	 */
	@RequestMapping("/saveOrUpdate")
	public @ResponseBody boolean saveOrUpdateImageTextInfo(HttpServletRequest request, ImageTextInfo imageTextInfo) {
		String flag = request.getParameter("flag");
		String picUrl = request.getParameter("picUrl");
		if(flag == null || imageTextInfo == null) return false;
		if(picUrl != null) imageTextInfo.setImageUrl(picUrl);
		if("add".equals(flag)){
			User user = getSessionUser(request);
			imageTextInfo.setImageTextType("1");
			imageTextInfo.setCreateTime(new Date());
			imageTextInfo.setOperator(user.getUsername());
			imageTextInfo.setWeixinPublicNo(user.getWxpublicno());
			int suc = imageTextInfoService.save(imageTextInfo);
			if(suc > 0) return true;
		}
		if("update".equals(flag)){
			int suc = imageTextInfoService.update(imageTextInfo);
			if(suc > 0) return true;
		}
		return false;
	}
	
	/**
	 * 删除图文
	 * @param imageTextNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/remove/{imageTextNo}")
	public @ResponseBody boolean removeImageText(@PathVariable("imageTextNo") String imageTextNo) throws Exception {
		ImageTextInfo imageTextInfo = imageTextInfoService.findByPK(imageTextNo);
		if(imageTextInfo != null){
			imageTextInfoService.delete(imageTextNo);
			return true;
		}
		return false;
	}

	/**
	 * 图文分页
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ModelAndView imageTextList(HttpServletRequest request) throws Exception {
		int page = getPage(request.getParameter(PAGE));
		String wxno = getWeiXinPublicNo(request);
		ModelAndView mav = new ModelAndView(getBasePath() + "article");
		mav.addObject(PAGE, page);
		mav.addObject("totalCount", imageTextInfoService.findImageTextCount(wxno));
		mav.addObject(LIST, imageTextInfoService.findImageTextList(getPageStart(page), PAGE_SIZE, wxno));
		return mav;
	}
	
	/**
	 * 查看详细页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@LoginValidate(value=false)
	@RequestMapping("/detail")
	public ModelAndView findDetailArticle(HttpServletRequest request) throws Exception {
		String id = request.getParameter("id");
		String type = request.getParameter("type");
		ModelAndView mav = new ModelAndView(getBasePath() + "detail");
		if(!StringUtil.isBlank(id) && !StringUtil.isBlank(type)){
			if("1".equals(type)){
				mav.addObject("imageText", imageTextInfoService.findByPK(id));
			} else {
				mav.addObject("imageText", imageTextMoreService.findByPK(id));
			}
		}
		return mav;
	}
	
	@Override
	protected String getBasePath() {
		return "/material/imagetextinfo/";
	}

}
