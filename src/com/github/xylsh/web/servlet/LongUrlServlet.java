package com.github.xylsh.web.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.xylsh.web.model.ShortUrlBaidu;
import com.github.xylsh.web.model.ShortUrlGoogle;
import com.github.xylsh.web.model.ShortUrlNetEase;
import com.github.xylsh.web.util.ShortUrlResult;

//@WebServlet("/LongUrlServlet")
public class LongUrlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LongUrlServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String tinyurl = request.getParameter("tinyurl");
	    
	    ShortUrlResult baiduShortUrlResult = null;
	    String key = "4f73afcbd05e4f8d91637f1802af5176";
        ShortUrlResult netEaseShortUrlResult = null;
        ShortUrlResult googleShortUrlResult = null;
	    
        try{
            if( !(tinyurl == null || tinyurl.equals("")) ){
                
                if( tinyurl.startsWith("http://dwz.cn") || tinyurl.startsWith("dwz.cn") ){
                    
                    baiduShortUrlResult = ShortUrlBaidu.getLongUrl(tinyurl);
                    request.setAttribute("baiduShortUrlResult", baiduShortUrlResult);
                    
                }else if( tinyurl.startsWith("http://126.am") || tinyurl.startsWith("126.am") ){
                    
                    netEaseShortUrlResult = ShortUrlNetEase.getLongUrl(tinyurl,key);
                    request.setAttribute("netEaseShortUrlResult", netEaseShortUrlResult);
                    
                }else if( tinyurl.startsWith("http://goo.gl") || tinyurl.startsWith("goo.gl") ){
                    
                    googleShortUrlResult = ShortUrlGoogle.getLongUrl(tinyurl);
                    request.setAttribute("googleShortUrlResult", googleShortUrlResult);
                    
                }
            }
        }catch(Exception e){
            
        }finally{
            RequestDispatcher view = request.getRequestDispatcher("./longurl.jsp");
            view.forward(request,response);
        }
        
	}
	
}
