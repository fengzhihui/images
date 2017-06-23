package com.fzh.wx.intf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 利用httpclient操作 比如登陆，发状态，模拟你访问任何人主页等等
 * 
 */
public class PaiPaiDai {
    private String userName = "";
    private String passWord = "";
    private HttpResponse response;
    private DefaultHttpClient httpclient = null;
    private static String redirectURL = "http://loan.ppdai.com/account/borrow";
    private static String loginURL = "https://ac.ppdai.com/User/Login";
    private static String codeUrl = "https://ac.ppdai.com/ValidateCode/Image?tmp=0.04517268643851868";
    private static String code = "6750";
    private static String cookie = "uniqueid=a3ca9bbe-b9d2-46d6-8e02-331a04f46747;";
    public PaiPaiDai(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }
     
    public static void main(String[] args) throws IOException {
    	getData();
    	
//        PaiPaiDai rr = null;
//        rr = new PaiPaiDai("13570346253", "12345678");
//        cookie = rr.imgCookie();
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("请输入下载下来的验证码中显示的数字...");
//        code = br.readLine();
//        rr.login();
    }
    
    /**
     * 登陆
     * @return
     * @throws IOException 
     */
	public boolean login() throws IOException {
        if (httpclient != null) return true;
        httpclient = null;
        httpclient = new DefaultHttpClient();
        
        HttpPost httpost = new HttpPost(loginURL);
        httpost.setHeader("Cookie", cookie);
        httpost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpost.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        httpost.setHeader("Cache-Control", "max-age=0");
        httpost.setHeader("Connection", "keep-alive");
        httpost.setHeader("Upgrade-Insecure-Requests", "1");
        httpost.setHeader("Host", "ac.ppdai.com");
        httpost.setHeader("X-Requested-With", "XMLHttpRequest");
        httpost.setHeader("Referer", "http://loan.ppdai.com/borrow/createlist/1");
        httpost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("IsAsync", "true"));
        nvps.add(new BasicNameValuePair("RememberMe", "false"));
        nvps.add(new BasicNameValuePair("UserName", userName));
        nvps.add(new BasicNameValuePair("PassWord", passWord));
        nvps.add(new BasicNameValuePair("ValidateCode", code));
        nvps.add(new BasicNameValuePair("Redirect", ""));
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = httpclient.execute(httpost);
            HttpEntity result = response.getEntity();
            String content = EntityUtils.toString(result);
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            httpost.abort();
        }
         
        String redirectLocation = getRedirectLocation();
        if (redirectLocation != null) {
            // 跳到首页，登录完成
            String indexHtml = getText(redirectLocation);
            System.out.println(indexHtml);
        }
        return true;
    }
     
    /**
     * 此处302跳转
     * @return
     */
    private String getRedirectLocation() {
        Header locationHeader = response.getFirstHeader("Location");
        if (locationHeader == null) return null;
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
    
    private String imgCookie() {
		BufferedReader in = null;
		try {
			httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			HttpGet httpGet = new HttpGet(codeUrl);
			httpGet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			HttpResponse response = httpclient.execute(httpGet);
			download(response.getEntity().getContent(), "d://vcode.png");
			List<Cookie> cookies = httpclient.getCookieStore().getCookies();
			httpGet.abort();
			StringBuilder cookiesSB = new StringBuilder();
			System.out.println("第一次cookie");
			if (cookies.isEmpty()) {
				System.out.println("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					// System.out.println("- " + cookies.get(i).toString());
					cookiesSB.append(cookies.get(i).getName()).append("=").append(cookies.get(i).getValue())
							.append(";");
				}
			}
			return cookiesSB.toString();
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
    
    public static boolean download(InputStream in, String path) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			byte b[] = new byte[1024];
			int j = 0;
			while ((j = in.read(b)) != -1) {
				out.write(b, 0, j);
			}
			out.flush();
			File file = new File(path);
			if (file.exists() && file.length() == 0) return false;
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if ("FileNotFoundException".equals(e.getClass().getSimpleName()))
				System.err.println("download FileNotFoundException");
			if ("SocketTimeoutException".equals(e.getClass().getSimpleName()))
				System.err.println("download SocketTimeoutException");
			else
				e.printStackTrace();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return false;
	}
    
    
    public static String getData() throws IOException {
		File in = new File("d://test.html");
		Document doc = Jsoup.parse(in, "UTF-8", "");
		Elements elements = doc.select("div[class=main] a");
		for (Element element : elements) {
			System.out.println(element.select("span[class=left]").text());
			System.out.println(element.select("span[class=right]").text());
		}
		return null;
	}
    
    
}