package com.fzh.storage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.math.RandomUtils;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;

public class BAEStorage {

	/**
	 * 百度云存储         http://bcs.duapp.com/bcs-api/bcs-sdk-javadoc_1.4.5/index.html
	 * @param fileUrl
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String uploadByBAE(String fileUrl, String fileName) throws IOException {
		String AK = "ZyVtGOXRtqg3F6CRujv1AQkC";
		String SK = "S53zFM7NKkZolfsrgrqG1zNMGEyDsHwA";
		String HOST = "bcs.duapp.com";
		BaiduBCS BAIDU_BCS = new BaiduBCS(new BCSCredentials(AK, SK), HOST);
		String BUCKET = "newbkt";
		fileName = "/imagespkg/" + fileName;
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentEncoding("utf-8");
		objectMetadata.setContentLength(1024);
		PutObjectRequest request = null;
		request = new PutObjectRequest(BUCKET, fileName, new File("d:/icon.jpg"));     //磁盘路径保存BAEStorage
		
		/*
		 * 通过临时路径读取流存入BAEStorage
		 */
		/*URL url = new URL(fileUrl);
	    URLConnection con = url.openConnection();
	    if(con != null){
	    	InputStream is = con.getInputStream();
	    	if(is != null)request = new PutObjectRequest(BUCKET, fileName, is, objectMetadata);
	    }*/
	    /*End*/
	    
		BAIDU_BCS.putObject(request);
		GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET, BUCKET, fileName);
		fileUrl = BAIDU_BCS.generateUrl(generateUrlRequest);
		System.out.println(fileUrl);
		return fileUrl;
	}
	
	// 上传文件的保存路径，相对于应用的根目录
	private static final String UPLOAD_PIC_PATH = "images/upload/";
	byte[] imgBufTemp = new byte[102401];
	private ServletContext servletContext;
	
	/**
	 * 将上传到百度云的临时图片再上传到百度云存储
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private void baeFileUpload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ServletFileUpload upload = new ServletFileUpload();
		upload.setHeaderEncoding("UTF-8");
		InputStream stream = null;
		BufferedOutputStream bos = null;
		String fileUrl = "", savePath = "";
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				FileItemIterator iter = upload.getItemIterator(request);
				int i = 0;
				String fileName = "";
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					stream = item.openStream();
					if (!item.isFormField()) {
						String prefix = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-" + RandomUtils.nextInt();
						// 得到文件的扩展名(无扩展名时将得到全名)
						String ext = item.getName().substring(item.getName().lastIndexOf(".") + 1);
						fileName = prefix + "." + ext;
//						savePath = getSavePath(fileName);
						if (i > 0) {
							fileUrl += ",";
						}
						fileUrl += getFileUrl(fileName);
						// 用文件流把图片写入保存路径（临时）中
						bos = new BufferedOutputStream(new FileOutputStream(
								new File(savePath)));
						int length;
						while ((length = stream.read(imgBufTemp)) != -1) {
							bos.write(imgBufTemp, 0, length);
						}
						i++;
					}
				}
				System.out.println("BAE  tepm-savePath: " + savePath);
				// 文件最终路径保存在BAEStorage
				fileUrl = uploadByBAE(savePath, fileName);
				
				StringBuilder json = new StringBuilder();
				json.append("{");
				json.append("'");
				json.append("fileUrl");
				json.append("':'");
				// 缩略图显示路径
				json.append(fileUrl.toString());
				json.append("'");
				// 加个fileName
				json.append(",");
				json.append("'");
				json.append("fileName");
				json.append("':'");
				json.append(fileName);
				json.append("'");
				//
				@SuppressWarnings("unchecked")
				Enumeration<String> pNames = request.getParameterNames();
				String pName;
				while (pNames.hasMoreElements()) {
					json.append(",");
					pName = (String) pNames.nextElement();
					json.append("'");
					json.append(pName);
					json.append("':'");
					json.append(request.getParameter(pName));
					json.append("'");
				}
				json.append("}");
				response.getWriter().write(json.toString());
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
	
	public static void main(String[] args) throws IOException {
		uploadByBAE("d:/icon.jpg", "icon");
	}
}
