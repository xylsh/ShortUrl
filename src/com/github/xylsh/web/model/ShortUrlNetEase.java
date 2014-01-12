package com.github.xylsh.web.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.github.xylsh.web.util.ShortUrlResult;

//API Key:4f73afcbd05e4f8d91637f1802af5176

/**
 * 网易短网址类<br />
 * 网易短网址<a href="http://126.com">http://126.am/</a>
 */
public class ShortUrlNetEase {
	
	//网易生成短网址请求地址
	private static final String CREATE_SHORT_URL = "http://126.am/api!shorten.action";
	//网易生成自定义短网址请求地址
	private static final String CREATE_CUSTOM_SHORT_URL = "http://126.am/api!alias.action";
	//网易还原短网址请求地址
	private static final String QUERY_SHORT_URL = "http://126.am/api!expand.action";
	
	/**
	 * 生成短网址
	 * @param longurl 长网址
	 * @param key 开发者申请的API Key
	 * @return ShortUrlResult封装了结果
	 */
	public static ShortUrlResult getShortUrl(String longurl,String key) throws IOException{
		ShortUrlResult shortUrlResult = new ShortUrlResult();
		
		//必须以"http://"或"https://"开头
		if( !(longurl.startsWith("http://") || longurl.startsWith("https://")) ){
		    longurl = "http://"+longurl;
		}
		shortUrlResult.setLongurl(longurl);
		
		URL url = new URL(CREATE_SHORT_URL);
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		
		//配置URLConnection对象，必须在URLConnection.connect()之前配置
		urlConnection.setDoOutput(true);         //打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true
		urlConnection.setDoInput(true);          //打算使用 URL 连接进行输入，则将 DoIutput 标志设置为 true
		urlConnection.setRequestMethod("POST");  //设置 URL 请求的方法
		urlConnection.setUseCaches(false);
		//urlConnection.setInstanceFollowRedirects(true);    //自动执行 HTTP 重定向（响应代码为 3xx 的请求）
		urlConnection.connect();    //打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）
		
		OutputStream os = urlConnection.getOutputStream();
		DataOutputStream out = new DataOutputStream(os);
		String content = "longUrl=" + longurl + "&key=" + key;
		out.writeBytes(content);
		out.flush();  //清空此数据输出流。这迫使所有缓冲的输出字节被写出到流中。
		out.close();
		
		InputStream inputStream = urlConnection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(inputStreamReader);
		
		String jsonStr = in.readLine();
		in.close();
		urlConnection.disconnect();
		
		System.out.println( "NetEase:" + jsonStr );
		JSONObject jsonObject = new JSONObject(jsonStr);
		
		int status = jsonObject.getInt("status_code");
		shortUrlResult.setStatus(status);
		shortUrlResult.setStatustxt(jsonObject.getString("status_txt"));
		
		//System.out.println("url:"+jsonObject.getString("url"));
		if( status==200 ){       //status==200表示成功
		    shortUrlResult.setIsSuccess(true);
			shortUrlResult.setTinyurl("http://"+jsonObject.getString("url"));     //获得短网址
		}else{
		    shortUrlResult.setIsSuccess(false);
		    //只在失败时添加errmsg
		    shortUrlResult.setErrmsg(ShortUrlNetEase.getDescription(shortUrlResult));
		}
		
		return shortUrlResult;
	}
	
	/**
	 * 获得原网址
	 * @param tinyurl 短地址
	 * @param key 开发者申请的API Key
	 * @return ShortUrlResult封装了结果
	 * @throws IOException
	 */
	public static ShortUrlResult getLongUrl(String tinyurl,String key) throws IOException{
		ShortUrlResult shortUrlResult = new ShortUrlResult();
		
		//必须以"http://"开头
		if( !tinyurl.startsWith("http://") ){
		    tinyurl = "http://"+tinyurl;
		}
		shortUrlResult.setTinyurl(tinyurl);
		
		URL url = new URL(QUERY_SHORT_URL);
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		
		//配置URLConnection对象，必须在URLConnection.connect()之前配置
		urlConnection.setDoOutput(true);         //打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true
		urlConnection.setDoInput(true);          //打算使用 URL 连接进行输入，则将 DoIutput 标志设置为 true
		urlConnection.setRequestMethod("POST");  //设置 URL 请求的方法
		urlConnection.setUseCaches(false);
		//urlConnection.setInstanceFollowRedirects(true);    //自动执行 HTTP 重定向（响应代码为 3xx 的请求）
		urlConnection.connect();    //打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）
		
		OutputStream os = urlConnection.getOutputStream();
		DataOutputStream out = new DataOutputStream(os);
		String content = "shortUrl=" + tinyurl + "&key=" + key;
		out.writeBytes(content);
		out.flush();  //清空此数据输出流。这迫使所有缓冲的输出字节被写出到流中。
		out.close();
		
		InputStream inputStream = urlConnection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(inputStreamReader);
		
		String jsonStr = in.readLine();
		in.close();
		urlConnection.disconnect();
		
		System.out.println("NetEase:"+jsonStr);
		JSONObject jsonObject = new JSONObject(jsonStr);
		
		int status = jsonObject.getInt("status_code");
		shortUrlResult.setStatus(status);
		shortUrlResult.setStatustxt(jsonObject.getString("status_txt"));
		
		//System.out.println("url:"+jsonObject.getString("url"));
		if( status==200 ){       //status==200表示成功
		    shortUrlResult.setIsSuccess(true);
			shortUrlResult.setLongurl(jsonObject.getString("url"));     //获得长网址
		}else{
		    //只在失败时添加errmsg
		    shortUrlResult.setIsSuccess(false);
            shortUrlResult.setErrmsg(ShortUrlNetEase.getDescription(shortUrlResult));
        }
		
		return shortUrlResult;
	}
	
	/**
	 * 自定义短网址
	 * @param longurl 长网址
	 * @param alias 用户自定义的短地址,只限于5~31个英文字母或数字
	 * @param key 开发者申请的API Key
	 * @return ShortUrlResult封装了结果
	 * @throws IOException
	 */
	public static ShortUrlResult getCustomShortUrl(String longurl,String alias,String key) throws IOException{
		ShortUrlResult shortUrlResult = new ShortUrlResult();
		shortUrlResult.setLongurl(longurl);
		shortUrlResult.setAlias(alias);
		
		URL url = new URL(CREATE_CUSTOM_SHORT_URL);
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		
		//配置URLConnection对象，必须在URLConnection.connect()之前配置
		urlConnection.setDoOutput(true);         //打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true
		urlConnection.setDoInput(true);          //打算使用 URL 连接进行输入，则将 DoIutput 标志设置为 true
		urlConnection.setRequestMethod("POST");  //设置 URL 请求的方法
		urlConnection.setUseCaches(false);
		//urlConnection.setInstanceFollowRedirects(true);    //自动执行 HTTP 重定向（响应代码为 3xx 的请求）
		urlConnection.connect();    //打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）
		
		OutputStream os = urlConnection.getOutputStream();
		DataOutputStream out = new DataOutputStream(os);
		String content = "longUrl=" + longurl + "&userShort=" + alias + "&key=" + key;
		out.writeBytes(content);
		out.flush();  //清空此数据输出流。这迫使所有缓冲的输出字节被写出到流中。
		out.close();
		
		InputStream inputStream = urlConnection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader in = new BufferedReader(inputStreamReader);
		
		String jsonStr = in.readLine();
		in.close();
		urlConnection.disconnect();
		
		System.out.println(jsonStr);
		JSONObject jsonObject = new JSONObject(jsonStr);
		
		int status = jsonObject.getInt("status_code");
		shortUrlResult.setStatus(status);
		shortUrlResult.setStatustxt(jsonObject.getString("status_txt"));
		
		//System.out.println("url:"+jsonObject.getString("url"));
		if( status==200 ){       //status==200表示成功
		    shortUrlResult.setIsSuccess(true);
			shortUrlResult.setTinyurl(jsonObject.getString("userShort"));
		}else{                   //如果失败
		    shortUrlResult.setIsSuccess(false);
		}
		
		return shortUrlResult;
	}
	
	/**
	 * 判断ShortUrlResult是否成功
	 * @param shortUrlResult 要判断的shortUrlResult
	 * @return 成功返回true,失败返回false
	 */
	public static boolean isSuccess(ShortUrlResult shortUrlResult){
		if( shortUrlResult.getStatus()==200 ){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获得状态码对应的中文说明
	 * @param shortUrlResult 封装了结果的ShortUrlResult对象
	 * @return
	 */
	public static String getDescription(ShortUrlResult shortUrlResult){
		int status = shortUrlResult.getStatus();
		
		switch (status){
		case 200: return "成功";
		case 201: return "Key不可用";
		case 202: return "未知错误";
		case 203: return "系统错误";
		case 204: return "短地址不合法";
		case 205: return "短地址已存在";
		case 206: return "长地址不存在";
		case 207: return "长地址不合法";
		case 208: return "Key和短地址不匹配，无法还原";
		case 209: return "请求频率限制";
		default:  return "未知状态码";
		}
	}
}

/*
class TestShortUrlNetEase{
	public static void main(String[] args){
		String longurl = "http://ent.qq.com/a/20130830/010458.htm";
		String longurl2 = "http://5";
		String tinyurl = "http://126.am/wvXoM2";
		String key = "4f73afcbd05e4f8d91637f1802af5176";
		try{
			//ShortUrlNetEase.getShortUrl(longurl2, key).getErrmsg();
			//System.out.println(ShortUrlNetEase.getLongUrl(tinyurl, key).getLongurl());
			ShortUrlNetEase.getCustomShortUrl(longurl, "oe393Wi32", key);
		}catch(Exception e){
			
		}
		
	}
}*/



