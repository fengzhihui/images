package com.fzh.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.PutObjectResponse;

public class CloudStorage {
	private final static String AK = "ZyVtGOXRtqg3F6CRujv1AQkC";
	private final static String SK = "S53zFM7NKkZolfsrgrqG1zNMGEyDsHwA";
	private final static String HOST = "bcs.duapp.com";
	
	private final static BaiduBCS BAIDU_BCS = new BaiduBCS(new BCSCredentials(AK,SK),HOST);

	private final static String BUCKET = "fengzhihui";
	
	public static void upload(String fileName, byte[] contents) throws IOException {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentEncoding("utf-8");
		objectMetadata.setContentLength(contents.length);
		PutObjectRequest request = new PutObjectRequest(BUCKET, fileName, new ByteArrayInputStream(contents), objectMetadata);
		
		boolean isExist = BAIDU_BCS.doesObjectExist(BUCKET, fileName);
		if(isExist){
			System.out.println("文件已经存在!");
			return;
		}
		BAIDU_BCS.putObject(request);
	}
	
	public static void uploadImage() throws IOException {
		BaiduBCS BAIDU_BCS = new BaiduBCS(new BCSCredentials(AK, SK), HOST);
//		String filePath = "http://fzh2014.duapp.com/fzh/wx/uploadimages/20141021115433-463011476.png";
		String filePath = "http://s0.hao123img.com/res/img/logo/logonew.png";
		String fileName = "/imagespkg/hb.jpg";
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(1024);
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
	
	public static void main(String[] args) throws IOException {
		// 初始化一个BosClient
	    BosClientConfiguration config = new BosClientConfiguration();
	    config.setCredentials(new DefaultBceCredentials(AK, SK));
	    BosClient client = new BosClient(config);
		PutObject(client, BUCKET, "mytest", null, "");
		//http://blog.csdn.net/u011124373/article/details/12949073
		/*String fileName = "/imagespkg/fzh文档";//"imagespkg"是文件夹名字，"fzh文档"是文件名字
		for(int i = 0; i < 2;i++){
			upload(fileName+"_"+i+".txt","测试一下中文1111".getBytes());
		}*/	
	}
	
	/**
	 * BOS一共支持四种形式的Object上传
	 * @param client
	 * @param bucketName
	 * @param objectKey
	 * @param byte1
	 * @param string1
	 * @throws FileNotFoundException 
	 */
	public static void PutObject(BosClient client, String bucketName, String objectKey, byte[] byte1, String string1) throws FileNotFoundException{
	    // 获取指定文件
	    File file = new File("D://chb.png");
	    // 获取数据流
	    //InputStream inputStream = new FileInputStream("/path/to/test.zip");

	    // 以文件形式上传Object
	    PutObjectResponse putObjectFromFileResponse = client.putObject(bucketName, objectKey, file);
	    /*// 以数据流形式上传Object
	    PutObjectResponse putObjectResponseFromInputStream = client.putObject(bucketName, objectKey, inputStream);
	    // 以二进制串上传Object
	    PutObjectResponse putObjectResponseFromByte = client.putObject(bucketName, objectKey, byte1);
	    // 以字符串上传Object
	    PutObjectResponse putObjectResponseFromString = client.putObject(bucketName, objectKey, string1);*/

	    // 打印ETag
	    System.out.println(putObjectFromFileResponse.getETag());
	}

}
