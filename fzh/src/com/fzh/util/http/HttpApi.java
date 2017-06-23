package com.fzh.util.http;

import org.apache.http.NameValuePair;

public interface HttpApi {
	public String doHttpURLConnectionGet(String specUrl, NameValuePair... nameValuePairs) throws Exception;
	public String doHttpGet(String url, NameValuePair... nameValuePairs) throws Exception;
	public String doHttpGet(String url) throws Exception;
	public String doHttpPost(String url, NameValuePair... nameValuePairs) throws Exception;
}
