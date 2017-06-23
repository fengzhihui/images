package com.fzh.wx.intf;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

/**
 * 利用httpclient操作人人网 比如登陆，发状态，模拟你访问任何人主页等等
 * @author: http://50vip.com
 * 
 */
public class RenRen {
    private String userName = "";
    private String password = "";
     
    private static String redirectURL = "http://www.renren.com/home";
    private static String renRenLoginURL = "http://www.renren.com/PLogin.do";
    private static String talkUrl = "http://photo.renren.com/photo/simsimiWebPagerChat";
    private HttpResponse response;
    private DefaultHttpClient httpclient = null;
    private String requestToken = null;
    private String _rtk = null;
     
    public RenRen(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
     
    public static void main(String[] args) {
        RenRen rr = null;
        rr = new RenRen("495300897@qq.com", "fzh789456");
        rr.login();
        System.out.println(rr.talk("毛"));
//        while(true){
//        	Scanner sc = new Scanner(System.in);
//        	String msg =  sc.next();
//        	System.out.println(rr.talk(msg));
//        	sc.reset();
//        }
    }
    
    /**
     * 登陆
     * @author: http://50vip.com
     * @return
     */
	public boolean login() {
        if (httpclient != null) {
            return true;
        }
        httpclient = null;
        httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(renRenLoginURL);
        // All the parameters post to the web site
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("origURL", redirectURL));
        nvps.add(new BasicNameValuePair("domain", "renren.com"));
        nvps.add(new BasicNameValuePair("autoLogin", "true"));
        nvps.add(new BasicNameValuePair("formName", ""));
        nvps.add(new BasicNameValuePair("method", ""));
        nvps.add(new BasicNameValuePair("submit", "登录"));
        nvps.add(new BasicNameValuePair("email", userName));
        nvps.add(new BasicNameValuePair("password", password));
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = httpclient.execute(httpost);
            //System.out.println(response);
            } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            httpost.abort();
        }
         
        String redirectLocation = getRedirectLocation();
        if (redirectLocation != null) {
            // 跳到首页，登录完成
            String indexHtml=getText(redirectLocation);
             
            //获取requestToken get_check:'-2062261917'
            Pattern pattern1 = Pattern.compile("get_check:'(.*)',get_check_x");
            Matcher m1 = pattern1.matcher(indexHtml);
            if (m1.find()) {
            requestToken=m1.group(1);
            } else {
            System.out.println("获取requestToken失败！");
            }
             
            //"获取_rtk失败！"get_check_x:'50d55fbd'
            Pattern pattern2 = Pattern.compile("get_check_x:'(.*)',env:");
            Matcher m2 = pattern2.matcher(indexHtml);
            if (m2.find()) {
                _rtk=m2.group(1);
            } else {
                System.out.println("获取_rtk失败！");
            }
        }
        return true;
    }
     
    /**
     * 和人人的小黄鸡交谈
     * @author: http://50vip.com
     * @param msg
     * @return
     */
	public String talk(String msg) {
        String repMsg="";//小黄的回复
         
        HttpPost httpost = new HttpPost(talkUrl);
        // All the parameters post to the web site
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("_rtk", _rtk));
        nvps.add(new BasicNameValuePair("requestToken", requestToken));
        nvps.add(new BasicNameValuePair("message", msg));
         
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            repMsg = httpclient.execute(httpost, responseHandler);
            
            JSONObject json = JSONObject.fromObject(repMsg);
	   		if(json !=null && json.containsKey("answer")){
	   			repMsg = json.get("answer").toString();
	   		}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpost.abort();
        }
        return new String(repMsg);
    }
     
    /**
     * 此处人人网会302跳转
     * @author: http://50vip.com
     * @return
     */
    private String getRedirectLocation() {
        Header locationHeader = response.getFirstHeader("Location");
        if (locationHeader == null) {
            return null;
        }
        return locationHeader.getValue();
    }
     
    /**
     * 读取首页内容
     * @author: http://50vip.com
     * @param redirectLocation
     * @return
     */
    private String getText(String redirectLocation) {
        HttpGet httpget = new HttpGet(redirectLocation);
        // Create a response handler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String responseBody = "";
        try {
            responseBody = httpclient.execute(httpget, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
            responseBody = null;
        } finally {
            httpget.abort();
        }
        return responseBody;
    }
    
}