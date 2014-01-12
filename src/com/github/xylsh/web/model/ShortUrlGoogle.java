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

public class ShortUrlGoogle {
    
    private static final String CREATE_SHORT_URL = "https://www.googleapis.com/urlshortener/v1/url";
    
    public static ShortUrlResult getShortUrl(String longurl) throws IOException{
        ShortUrlResult shortUrlResult = new ShortUrlResult();
        shortUrlResult.setLongurl(longurl);
        
        if( !(longurl.startsWith("http://") || longurl.startsWith("https://")) ){
            longurl = "http://" + longurl;
        }
        
        URL url = new URL(CREATE_SHORT_URL);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        
        urlConnection.setDoOutput(true);         //打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true
        urlConnection.setDoInput(true);          //打算使用 URL 连接进行输入，则将 DoIutput 标志设置为 true
        urlConnection.setRequestMethod("POST");  //设置 URL 请求的方法
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.connect();
        
        JSONObject requestJson = new JSONObject();
        requestJson.put("longUrl", longurl);
        
        OutputStream os = urlConnection.getOutputStream();
        DataOutputStream out = new DataOutputStream(os);
        out.writeBytes(requestJson.toString());
        out.flush();
        out.close();
        
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
        BufferedReader in = new BufferedReader(inputStreamReader);
        
        StringBuffer jsonStr = new StringBuffer();
        String line;
        while( (line = in.readLine()) != null ){
            System.out.println(line);
            jsonStr.append(line);
        }
        in.close();
        urlConnection.disconnect();
        
        JSONObject jsonObject = new JSONObject(jsonStr.toString());
        String tinyurl = jsonObject.getString("id");
        shortUrlResult.setTinyurl(tinyurl);
        
        return shortUrlResult;
    }
    
    public static ShortUrlResult getLongUrl(String tinyurl) throws IOException{
        ShortUrlResult shortUrlResult = new ShortUrlResult();
        
        if( !(tinyurl.startsWith("http://") || tinyurl.startsWith("https://")) ){
            tinyurl = "http://" + tinyurl;
        }
        shortUrlResult.setTinyurl(tinyurl);
        
        String queryUrl = CREATE_SHORT_URL + "?shortUrl=" + tinyurl;
        URL url = new URL(queryUrl);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
        BufferedReader in = new BufferedReader(inputStreamReader);
        
        StringBuilder jsonStr = new StringBuilder();
        String line;
        while( (line = in.readLine()) != null){
            System.out.println(line);
            jsonStr.append(line);
        }
        in.close();
        urlConnection.disconnect();
        
        JSONObject jsonObject = new JSONObject(jsonStr.toString());
        String longurl = jsonObject.getString("longUrl");
        shortUrlResult.setLongurl(longurl);
        
        return shortUrlResult;
    }
}

/*
class Test{
    public static void main(String[] args){
        String longurl = "http://www.google.com/";
        String longurl2 = "...";
        try{
            ShortUrlGoogle.getShortUrl(longurl2);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}*/
