package com.fzh.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.model.BucketSummary;
import com.baidu.inf.iis.bcs.model.ObjectListing;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.model.ObjectSummary;
import com.baidu.inf.iis.bcs.model.Resource;
import com.baidu.inf.iis.bcs.model.X_BS_ACL;
import com.baidu.inf.iis.bcs.request.CreateBucketRequest;
import com.baidu.inf.iis.bcs.request.GetObjectRequest;
import com.baidu.inf.iis.bcs.request.ListBucketRequest;
import com.baidu.inf.iis.bcs.request.ListObjectRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
 
/**
 * 百度云存储工具类<br>
 * 1.创建bucket<br>
 * 2.删除bucket<br>
 * 3.列出bucket<br>
 * 4.存入object<br>
 * 5.获取object<br>
 * 6.复制object<br>
 * 7.删除object<br>
 * 8.列出object<br>
 * 9.获取ObjectMetadata<br>
 * 10.设置获取ObjectMetadata<br>
 */
public class BCSUtil {
     
    /**
     * 初始化baiduBCS
     */
    private static BaiduBCS baiduBCS = null;
    static{
        String accessKey = "";
        String secretKey = "";
        String host = "bcs.duapp.com";
        baiduBCS = new BaiduBCS(new BCSCredentials(accessKey, secretKey), host);
        baiduBCS.setDefaultEncoding("UTF-8");
    }
     
    /**
     * 创建一个bucket，公开读取
     * @param bucket
     */
    public static void createBucket(String bucket){
        createBucket(bucket, X_BS_ACL.PublicRead);
    }
     
    /**
     * 创建一个bucket
     * @param bucket
     * @param type
     */
    public static void createBucket(String bucket, X_BS_ACL type){
        baiduBCS.createBucket(new CreateBucketRequest(bucket, type));
    }
     
    /**
     * 删除一个bucket
     * @param bucket
     */
    public static void deleteBucket(String bucket){
        baiduBCS.deleteBucket(bucket);
    }
     
    /**
     * 列出bucket
     * @return
     */
    public static List<BucketSummary> listBucket(){
        List<BucketSummary> buckets = new ArrayList<BucketSummary>();
         
        BaiduBCSResponse<List<BucketSummary>> response = baiduBCS.listBucket(new ListBucketRequest());
        for (BucketSummary bucket : response.getResult()) {
            buckets.add(bucket);
        }
        return buckets;
    }
     
    /**
     * 存入object，文件方式，公开
     * @param bucket
     * @param object
     * @param file
     * @return
     */
    public static String putObjectByFilePublic(String bucket, String object, File file){
        return putObjectByFile(bucket, object, file, X_BS_ACL.PublicRead);
    }
     
    /**
     * 存入object，文件方式，私有
     * @param bucket
     * @param object
     * @param file
     * @return
     */
    public static String putObjectByFilePrivate(String bucket, String object, File file){
        return putObjectByFile(bucket, object, file, X_BS_ACL.Private);
    }
     
    /**
     * 存入object，文件方式，type
     * @param bucket
     * @param object
     * @param file
     * @param type
     * @return
     */
    public static String putObjectByFile(String bucket, String object, File file, X_BS_ACL type){
        PutObjectRequest request = new PutObjectRequest(bucket, object, file);
        request.setMetadata(new ObjectMetadata());
        request.setAcl(type);
        return baiduBCS.putObject(request).getResult().toString();
    }
 
    /**
     * 存入object，InputStream方式
     * @param bucket
     * @param object
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String putObjectByInputStream(String bucket, String object, File file) throws FileNotFoundException{
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("text/html");
        objectMetadata.setContentLength(file.length());
 
        PutObjectRequest request = new PutObjectRequest(bucket, object, new FileInputStream(file), objectMetadata);
         
        return baiduBCS.putObject(request).getResult().toString();
    }
     
    /**
     * 获取object，保存到目标文件
     * @param bucket
     * @param object
     * @param destFile
     */
    public static void getObjectToDestFile(String bucket, String object, File destFile){
        baiduBCS.getObject(new GetObjectRequest(bucket, object), destFile);
    }
     
    /**
     * 复制object
     * @param sourceBucket
     * @param sourceObject
     * @param destBucket
     * @param destObject
     */
    public static void copyObject(String sourceBucket, String sourceObject, String destBucket, String destObject){
        baiduBCS.copyObject(new Resource(sourceBucket, sourceObject), new Resource(destBucket, destObject));
    }
 
    /**
     * 复制object，可设置对象格式
     * @param sourceBucket
     * @param sourceObject
     * @param destBucket
     * @param destObject
     * @param objectMetadata
     */
    public static void copyObject(String sourceBucket, String sourceObject, String destBucket, String destObject, ObjectMetadata objectMetadata){
        baiduBCS.copyObject(new Resource(sourceBucket, sourceObject), new Resource(destBucket, destObject), objectMetadata);
    }
     
    /**
     * 删除object
     * @param bucket
     * @param object
     * @return
     */
    public static String deleteObject(String bucket, String object){
        return baiduBCS.deleteObject(bucket, object).getResult().toString();
    }
     
    /**
     * 列出object
     * @param bucket
     * @param start
     * @param limit
     * @return
     */
    public static List<ObjectSummary> listObject(String bucket, int start, int limit){
        List<ObjectSummary> objects = new ArrayList<ObjectSummary>();
         
        ListObjectRequest listObjectRequest = new ListObjectRequest(bucket);
        listObjectRequest.setStart(start);
        listObjectRequest.setLimit(limit);
        BaiduBCSResponse<ObjectListing> response = baiduBCS.listObject(listObjectRequest);
         
        for (ObjectSummary os : response.getResult().getObjectSummaries()) {
            objects.add(os);
        }
        return objects;
    }
     
    /**
     * 获取ObjectMetadata
     * @param bucket
     * @param object
     * @return
     */
    public static ObjectMetadata getObjectMetadata(String bucket, String object){
        return baiduBCS.getObjectMetadata(bucket, object).getResult();
    }
     
    /**
     * 设置ObjectMetadata
     * @param bucket
     * @param object
     * @param objectMetadata
     */
    public static void setObjectMetadata(String bucket, String object, ObjectMetadata objectMetadata){
        baiduBCS.setObjectMetadata(bucket, object, objectMetadata);
    }
    
}
     
