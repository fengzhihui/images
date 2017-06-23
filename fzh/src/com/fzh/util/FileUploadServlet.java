package com.fzh.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static String AK = "ZyVtGOXRtqg3F6CRujv1AQkC";
	private final static String SK = "S53zFM7NKkZolfsrgrqG1zNMGEyDsHwA";
	private final static String HOST = "bcs.duapp.com";
	private final static BaiduBCS BAIDU_BCS = new BaiduBCS(new BCSCredentials(AK,SK),HOST);
	private final static String BUCKET = "fzhbkt";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String AK = "ZyVtGOXRtqg3F6CRujv1AQkC";
		String SK = "S53zFM7NKkZolfsrgrqG1zNMGEyDsHwA";
		String HOST = "bcs.duapp.com";
		BaiduBCS BAIDU_BCS = new BaiduBCS(new BCSCredentials(AK, SK), HOST);
		String BUCKET = "fzhbkt";
		String filePath = "http://s0.hao123img.com/res/img/logo/logonew.png";
		String fileName = "/imagespkg/aa.jpg";
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentEncoding("utf-8");
		
//		PutObjectRequest request = new PutObjectRequest(BUCKET, fileName, new File(filePath));
		
		URL url = new URL(filePath);
	    // 打开连接
	    URLConnection con = url.openConnection();
	    // 输入流
	    InputStream is = con.getInputStream();
	    PutObjectRequest req = new PutObjectRequest(BUCKET, fileName, is, objectMetadata);
		
		boolean isExist = BAIDU_BCS.doesObjectExist(BUCKET, fileName);
		
		if(isExist){
			System.out.println("文件已经存在!");
			return;
		}
		BAIDU_BCS.putObject(req);
		GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET, BUCKET, fileName);
		System.out.println(BAIDU_BCS.generateUrl(generateUrlRequest));
	
	}
	
	public static void uploadImage(String filePath) throws IOException {
		filePath = "";//"D:\\tq.png"
		String fileName = "/imagespkg/share.jpg";
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentEncoding("utf-8");
		PutObjectRequest request = new PutObjectRequest(BUCKET, fileName, new File(filePath));
		
		boolean isExist = BAIDU_BCS.doesObjectExist(BUCKET, fileName);
		if(isExist){
			System.out.println("文件已经存在!");
			return;
		}
		BAIDU_BCS.putObject(request);
		GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET, BUCKET, fileName);
		System.out.println(BAIDU_BCS.generateUrl(generateUrlRequest));
	}

}
