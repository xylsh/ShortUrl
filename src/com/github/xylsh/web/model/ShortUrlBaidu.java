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


/**
 *百度短网址类<br />
 *百度短网址:<a href="http://dwz.cn/">http://dwz.cn/</a>
 */
public class ShortUrlBaidu {
	
	//百度生成短网址请求地址
	private static final String CREATE_SHORT_URL = "http://dwz.cn/create.php";
	//百度还原短网址请求地址
	private static final String QUERY_SHORT_URL = "http://dwz.cn/query.php";
	
	/**
	 * 生成短网址
	 * @param longurl 长网址
	 * @return ShortUrlResult封装了结果
	 * @throws IOException
	 */
	public static ShortUrlResult getShortUrl(String longurl) throws IOException{
		ShortUrlResult shortUrlResult = new ShortUrlResult();
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
		String content = "url="+longurl;
		out.writeBytes(content);
		out.flush();  //清空此数据输出流。这迫使所有缓冲的输出字节被写出到流中。
		out.close();
		
		InputStream inputStream = urlConnection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
		BufferedReader in = new BufferedReader(inputStreamReader);
		
		String jsonStr = in.readLine();   //百度的create.php返回值只有1行(以JSON格式)
		in.close();
		urlConnection.disconnect();
		
		//System.out.println(jsonStr);
		JSONObject jsonObject = new JSONObject(jsonStr);
		
		int status = jsonObject.getInt("status");
		shortUrlResult.setStatus(status);
		if( status==0 ){    //status==0表示成功
		    shortUrlResult.setIsSuccess(true);
			shortUrlResult.setTinyurl(jsonObject.getString("tinyurl"));
		}else{              //如果失败
		    shortUrlResult.setIsSuccess(false);
			shortUrlResult.setErrmsg(jsonObject.getString("err_msg"));
		}

		return shortUrlResult;
	}
	
	/**
	 * 获得原网址
	 * @param tinyurl 短网址
	 * @return ShortUrlResult封装了结果
	 * @throws IOException
	 */
	public static ShortUrlResult getLongUrl(String tinyurl) throws IOException{
		ShortUrlResult shortUrlResult = new ShortUrlResult();
		shortUrlResult.setTinyurl(tinyurl);
		
		URL url = new URL(QUERY_SHORT_URL);
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		
		//配置URLConnection对象，必须在URLConnection.connect()之前配置
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setRequestMethod("POST");
		urlConnection.setUseCaches(false);
		//urlConnection.setInstanceFollowRedirects(true);    //自动执行 HTTP 重定向（响应代码为 3xx 的请求）
		urlConnection.connect();
		
		OutputStream os = urlConnection.getOutputStream();
		DataOutputStream out = new DataOutputStream(os);
		String content = "tinyurl="+tinyurl;
		out.writeBytes(content);
		out.flush();
		out.close();
		
		InputStream inputStream = urlConnection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
		BufferedReader in = new BufferedReader(inputStreamReader);
		
		String jsonStr = in.readLine();
		in.close();
		urlConnection.disconnect();
		
		JSONObject jsonObject = new JSONObject(jsonStr);
		//System.out.println(jsonStr);
		
		int status = jsonObject.getInt("status");
		shortUrlResult.setStatus(status);
		if( status==0 ){    //status为0表示成功
		    shortUrlResult.setIsSuccess(true);
			shortUrlResult.setLongurl(jsonObject.getString("longurl"));
		}else{
		    shortUrlResult.setIsSuccess(false);
			shortUrlResult.setErrmsg(jsonObject.getString("err_msg"));
		}
		
		return shortUrlResult;
	}
	
	/**
	 * 自定义短网址
	 * @param longurl
	 * @param alias 自定义的短网址,可接受字母、数字、破折号,不能超过20个字符。要输入"http://dwz.cn/xxx"中的"xxx"，不含"http://dwz.cn/"。
	 * @return ShortUrlResult封装了结果
	 * @throws IOException
	 */
	public static ShortUrlResult getCustomShortUrl(String longurl,String alias) throws IOException{
		ShortUrlResult shortUrlResult = new ShortUrlResult();
		shortUrlResult.setLongurl(longurl);
		shortUrlResult.setAlias(alias);
		
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
		String content = "url=" + longurl + "&alias=" + alias;
		out.writeBytes(content);
		out.flush();  //清空此数据输出流。这迫使所有缓冲的输出字节被写出到流中。
		out.close();
		
		InputStream inputStream = urlConnection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
		BufferedReader in = new BufferedReader(inputStreamReader);
		
		String jsonStr = in.readLine();   //百度的create.php返回值只有1行(以JSON格式)
		in.close();
		urlConnection.disconnect();
		
		System.out.println(jsonStr);
		JSONObject jsonObject = new JSONObject(jsonStr);
		
		int status = jsonObject.getInt("status");
		shortUrlResult.setStatus(status);
		if( status==0 ){    //如果成功
		    shortUrlResult.setIsSuccess(true);
			shortUrlResult.setTinyurl(jsonObject.getString("tinyurl"));
		}else{              //如果失败
		    shortUrlResult.setIsSuccess(false);
			shortUrlResult.setErrmsg(jsonObject.getString("err_msg"));
		}
		
		return shortUrlResult;
	}
	
	/**
	 * 判断ShortUrlResult是否成功
	 * @param shortUrlResult 要判断的shortUrlResult
	 * @return 成功返回true,失败返回false
	 */
	public static boolean isSuccess(ShortUrlResult shortUrlResult){
		if( shortUrlResult.getStatus()==0 ){
			return true;
		}else{
			return false;
		}
	}
	
}

class TestShortUrlBaidu{
	public static void main(String[] args){
		try{
			String longurl = "http://ent.qq.com/a/20130830/010458.htm";
			/*ShortUrlResult shortUrlResult = ShortUrlBaidu.getShortUrl("http://ent.qq.com/a/20130830/010458.htm");
			System.out.println(shortUrlResult.getTinyurl());
			ShortUrlResult shortUrlResult2 = ShortUrlBaidu.getLongUrl(shortUrlResult.getTinyurl());
			System.out.println(shortUrlResult2.getLongurl());
			System.out.println(shortUrlResult2.getErrmsg());*/
			
			ShortUrlResult shortUrlResult3 = ShortUrlBaidu.getCustomShortUrl(longurl,"lwqet34f");
			System.out.println(shortUrlResult3.getTinyurl());
			System.out.println(shortUrlResult3.getErrmsg());
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
}
