package com.fzh.storage;

import java.io.File;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.auth.BCSSignCondition;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;

public class FileUploadUtils {

	private static FileUploadUtils instance;

	// ----------------------------------------//
	private static String host = "bcs.duapp.com";
	private static String accessKey = "";
	private static String secretKey = "";
	private static String bucket = "";
	// ---------------------------------------//

	// ---------------------------------------//
	private static BCSCredentials credentials = null;
	private static BaiduBCS baiduBCS = null;
	// ---------------------------------------//

	//static String object = &quot;/images/first-object2&quot;;
	private static final String IMAGE_DIR = "";
	private static final String FILE_DIR = "";

	private FileUploadUtils() {
		credentials = new BCSCredentials(accessKey, secretKey);
		baiduBCS = new BaiduBCS(credentials, host);
		baiduBCS.setDefaultEncoding("UTF-8");
	}

	public static FileUploadUtils getInstance() {
		if (instance == null) {
			instance = new FileUploadUtils();
		}
		return instance;
	}

	/**
	 * 上传到百度云
	 * @param image
	 * @return
	 */
	public String uploadImage(File image) {
		String object = IMAGE_DIR + image.getName();
		putObjectByFile(image, object);
		return generateUrl(object);
	}

	public static void uploadFile(File file) {
		String object = FILE_DIR + file.getName();
		putObjectByFile(file, object);
	}
	
	private static void putObjectByFile(File file, String object) {
		PutObjectRequest request = new PutObjectRequest(bucket, object, file);
		ObjectMetadata metadata = new ObjectMetadata();
		// metadata.setContentType("text/html");
		request.setMetadata(metadata);
		BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);
		ObjectMetadata objectMetadata = response.getResult();
	}
	
	public String generateUrl(String object) {
		GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET, bucket, object);
		generateUrlRequest.setBcsSignCondition(new BCSSignCondition());
		//generateUrlRequest.getBcsSignCondition().setIp(&quot;115.156.249.55&quot;);
		//generateUrlRequest.getBcsSignCondition().setTime(123455777777L);
		//generateUrlRequest.getBcsSignCondition().setSize(123455L);
		return baiduBCS.generateUrl(generateUrlRequest);//上传百度云的核心</span></em></strong>
	}
	
	
	private static File createSampleFile() {
		//Writer writer = new OutputStreamWriter(new FileOutputStream(file));
		//URL url = Sample.class.getClassLoader().getResource(&quot;20140317172637.jpg&quot;);
		//File file = new File(url.toString());
		File file = new File("D:\\work\\BAEtest\\WebRoot\\WEB-INF\\classes\\20140317172637.jpg");
		//file.deleteOnExit();
		return file;
	}
}
