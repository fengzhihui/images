package ueditor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.math.RandomUtils;

import com.fzh.util.VeDate;

public class OmFileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 上传文件的保存路径，相对于应用的根目录
	private static final String UPLOAD_PIC_PATH = "images/upload/";
	// 文件允许格式
	private String[] allowFiles = { ".gif", ".png", ".jpg", ".jpeg", ".bmp" };

	byte[] imgBufTemp = new byte[102401];

	private ServletContext servletContext;

	public void init(ServletConfig config) throws ServletException {
//		super.init(config);
		this.servletContext = config.getServletContext();
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 获取客户端回调函数名
		response.setContentType("text/html;charset=UTF-8");
		// 默认上传到项目
		defaultProcessFileUpload(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 生成保存上传文件的磁盘路径
	 * 
	 * @param fileName
	 * @return
	 */
	public String getSavePath(String fileName) {
		String realPath = servletContext.getRealPath("/");
		return realPath + UPLOAD_PIC_PATH + fileName;
	}

	/**
	 * 生成访问上传成功后的文件的URL地址
	 * 
	 * @param fileName
	 * @return
	 */
	public String getFileUrl(String fileName) {
		return UPLOAD_PIC_PATH + fileName;
	}

	/**
	 * 默认上传到本地项目
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void defaultProcessFileUpload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ServletFileUpload upload = new ServletFileUpload();
		upload.setHeaderEncoding("UTF-8");
		InputStream stream = null;
		BufferedOutputStream bos = null;
		String fileUrl = "", savePath = null;
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				FileItemIterator iter = upload.getItemIterator(request);
				int i = 0;
				String fileName = "";
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					stream = item.openStream();
					if (!item.isFormField()) {
						String prefix = VeDate.getDateYMDHMS(new Date()) + "-" + RandomUtils.nextInt();
						// 得到文件的扩展名(无扩展名时将得到全名)
						String ext = getFileExt(item.getName());
						if(!checkFileType(ext)){
							response.getWriter().write("{\"error\":\"fileType\"}");
							return;
						}
						fileName = prefix + ext;
						savePath = getSavePath(fileName);
						Logger.getAnonymousLogger().info("getSavePath: " + savePath);
						if (i > 0) {
							fileUrl += ",";
						}
						fileUrl += getFileUrl(fileName);
						// 用文件流把图片写入保存路径中
						bos = new BufferedOutputStream(new FileOutputStream(new File(savePath)));
						int length;
						while ((length = stream.read(imgBufTemp)) != -1) {
							bos.write(imgBufTemp, 0, length);
						}
						i++;
					}
				}
				String json = getJsonResult(request, fileUrl, fileName);
				response.getWriter().write(json);
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	private String getJsonResult(HttpServletRequest request, String fileUrl, String fileName) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"");
		json.append("fileUrl");
		json.append("\":\"");
		// 缩略图显示路径
		json.append(fileUrl);
		json.append("\"");
		// 加个fileName
		json.append(",");
		json.append("\"");
		json.append("fileName");
		json.append("\":\"");
		json.append(fileName);
		json.append("\"");
		//
		Enumeration<String> pNames = request.getParameterNames();
		String pName;
		while (pNames.hasMoreElements()) {
			json.append(",");
			pName = (String) pNames.nextElement();
			json.append("\"");
			json.append(pName);
			json.append("\":\"");
			json.append(request.getParameter(pName));
			json.append("\"");
		}
		json.append("}");
		return json.toString();
	}
	
	/**
	 * 文件类型判断
	 * @param fileName
	 * @return
	 */
	private boolean checkFileType(String fileName) {
		Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
		while (type.hasNext()) {
			String ext = type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取文件扩展名
	 * @return string
	 */
	private String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

}
