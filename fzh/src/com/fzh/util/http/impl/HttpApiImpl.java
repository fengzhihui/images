package com.fzh.util.http.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.fzh.util.http.HttpApi;

public class HttpApiImpl implements HttpApi {
//	private static final String DEFAULT_CLIENT_VERSION = "com.tjrclient";
//	private static final String CLIENT_VERSION_HEADER = "User-Agent";
	private static final int TIMEOUT = 8; // 设置连接超时及读取超时
	private final DefaultHttpClient mHttpClient;
	
	public HttpApiImpl(DefaultHttpClient httpClient) {
		mHttpClient = httpClient;
	}
	
	@SuppressWarnings("deprecation")
	public String doHttpGet(String url, NameValuePair... nameValuePairs) throws Exception {
		HttpGet httpGet = createHttpGet(url, nameValuePairs);
		HttpResponse response = executeHttpRequest(httpGet);
		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw e;
			}
		default:
			response.getEntity().consumeContent();
			throw new Exception(response.getStatusLine().toString());
		}
	}
	
	@SuppressWarnings("deprecation")
	public String doHttpGet(String url) throws Exception {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = executeHttpRequest(httpGet);
		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw e;
			}
		default:
			response.getEntity().consumeContent();
			throw new Exception(response.getStatusLine().toString());
		}
	}

	@SuppressWarnings("deprecation")
	public String doHttpPost(String url, NameValuePair... nameValuePairs) throws Exception {
		HttpPost httpPost = createHttpPost(url, nameValuePairs);
		HttpResponse response = executeHttpRequest(httpPost);
		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw e;
			}
		default:
			response.getEntity().consumeContent();
			throw new Exception(response.getStatusLine().toString());
		}
	}

	public String doHttpURLConnectionGet(String specUrl, NameValuePair... nameValuePairs) throws Exception {
		String query = URLEncodedUtils.format(stripNulls(nameValuePairs), "UTF-8");
		URL url = new URL(specUrl + "?" + query);
		// 设置请求头
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setUseCaches(false);
		// 设置连接超时时间
		conn.setConnectTimeout(TIMEOUT * 1000);
		// 读取超时
		conn.setReadTimeout(TIMEOUT * 1000);
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Content-Type", "text/html");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Accept-Charset", "utf-8");
		InputStreamReader in = new InputStreamReader(conn.getInputStream());
		BufferedReader reader = new BufferedReader(in);// 读字符串用的。
		String inputLine = null;
		StringBuffer result = new StringBuffer();
		// 使用循环来读取获得的数据，把数据都村到result中了
		while (((inputLine = reader.readLine()) != null)) {
			result.append(inputLine);
		}
		reader.close();// 关闭输入流
		// 关闭http连接
		conn.disconnect();
		return result.toString();
	}


	private HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		String query = URLEncodedUtils.format(stripNulls(nameValuePairs), "UTF-8");
		HttpGet httpGet = new HttpGet(url + "?" + query);
		return httpGet;
	}

	private HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
		HttpPost httpPost = new HttpPost(url);
		// httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(stripNulls(nameValuePairs), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException("参数设置不正确");
		}
		return httpPost;
	}
	
	/**
	 * execute() an httpRequest catching exceptions and returning null instead.
	 * 
	 * @param httpRequest
	 * @return
	 * @throws IOException
	 */
	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
		try {
			//Close expired connections
			mHttpClient.getConnectionManager().closeExpiredConnections();
			//Optionally, close connections
            // that have been idle longer than 30 sec
			mHttpClient.getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);
			return mHttpClient.execute(httpRequest);
		} catch (IOException e) {
			httpRequest.abort();
			throw e;
		}
	}

	private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < nameValuePairs.length; i++) {
			NameValuePair param = nameValuePairs[i];
			if (param.getValue() != null) {
				params.add(param);
			}
		}
		return params;
	}
	
	/**
	 * Create a thread-safe client. This client does not do redirecting, to
	 * allow us to capture correct "error" codes.
	 * @return HttpClient
	 */
	public static final DefaultHttpClient createHttpClient() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
		         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(
		         new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		// Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
		final HttpParams httpParams = createHttpParams();
		HttpClientParams.setRedirecting(httpParams, true);
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		//Increase max total connection to 200
		cm.setMaxTotal(1024);
		//Increase default max connection per route to 20
		cm.setDefaultMaxPerRoute(200);
		//Increase max connections for localhost:80 to 50
		HttpHost localhost = new HttpHost("localhost", 80);
		cm.setMaxPerRoute(new HttpRoute(localhost), 50);
        DefaultHttpClient httpClient = new DefaultHttpClient(cm);
        httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {

            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName(); 
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch(NumberFormatException ignore) {
                        }
                    }
                }
                HttpHost target = (HttpHost) context.getAttribute(
                        ExecutionContext.HTTP_TARGET_HOST);
                if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
                    // Keep alive for 5 seconds only
                    return 5 * 1000;
                } else {
                    // otherwise keep alive for 30 seconds
                    return 30 * 1000;
                }
            }
        });
		return httpClient;
	}
	
	/**
	 * Create the default HTTP protocol parameters.
	 */
	private static final HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUseExpectContinue(params, true);
		return params;
	}
}
