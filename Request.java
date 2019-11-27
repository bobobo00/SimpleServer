package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装请求协议: 封装请求参数为Map
 * 
 * @author dell
 *
 */
public class Request {
	//协议信息
	private String requestInfo;
	//请求方式
	private String method;
	//请求url
	private String URL;
	//请求参数
	private String queryInfo;
	private final String  CRLF = "\r\n";
	//存储参数
	private Map<String,List<String>> parameter;
	public Request(Socket client) throws IOException{
		this(client.getInputStream());
	}
	public Request(InputStream is){
		parameter=new HashMap<String,List<String>>();
		byte[] datas=new byte[1024*1024*1024];
		int len=0;
		try {
			len = is.read(datas);
			requestInfo=new String(datas,0,len);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//分解字符串
		parseRequestInfo();
	}
	private void parseRequestInfo() {
		method=this.requestInfo.substring(0,this.requestInfo.indexOf("/")).trim();
		System.out.println("method:"+this.method);
		URL=this.requestInfo.substring(this.requestInfo.indexOf("/")+1,this.requestInfo.indexOf("HTTP/")).trim();
		if(this.URL.indexOf("?")!=-1) {
			String[] datas=this.URL.split("\\?");
			this.URL=datas[0];
			this.queryInfo=datas[1].trim();
		}
		System.out.println("URL:"+this.URL.trim());
		if(this.method.equals("POST")) {
			String str=this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
			if(null==this.queryInfo) {
				this.queryInfo=str;
			}else {
				this.queryInfo+="&"+str;
			}
		}
		if(null==this.queryInfo) {
			this.queryInfo="";
		}
		this.queryInfo=decode(this.queryInfo,"gbk");
		System.out.println("queryInfo:"+this.queryInfo);
		CreateParameterMap();
	}
	//处理请求参数为Map
	private void CreateParameterMap() {
		//1、分割字符串 &
		String[] parameter=this.queryInfo.split("&");
		for(String str:parameter) {
			//2、再次分割字符串  =
			String[]  kv=str.split("=");
			kv=Arrays.copyOf(kv, 2);
			//获取key和value
			String key = kv[0];
			String value = kv[1]==null?null:decode( kv[1],"gbk");
			//存储到map中
			if(!this.parameter.containsKey(key)) { //第一次
				this.parameter.put(key, new ArrayList<>());
			}
			this.parameter.get(key).add(value);
		}
	}
	/**
	 * 处理中文
	 * @return
	 */
	private String decode(String value,String charSet) {
		try {
			return java.net.URLDecoder.decode(value, charSet);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过name获取对应的多个值
	 * @param key
	 * @return
	 */
	public String[] getParameterValues(String key) {
		List<String> values = this.parameter.get(key);
		if(null==values || values.size()<1) {
			return null;
		}
		return values.toArray(new String[0]);
	}
	/**
	 * 通过name获取对应的一个值
	 * @param key
	 * @return
	 */
	public String getParameterValue(String key) {
		String []  values =getParameterValues(key);
		return values ==null?null:values[0];
	}
	public String getMethod() {
		return method;
	}
	
	public String getUrl() {
		return URL;
	}
	
	public String getQueryStr() {
		return queryInfo;
	}
}
