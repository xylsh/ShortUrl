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

//@WebServlet("/ShortUrlServlet")
public class ShortUrlServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ShortUrlServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String longurl = request.getParameter("longurl");

        ShortUrlResult baiduShortUrlResult = null;
        String key = "4f73afcbd05e4f8d91637f1802af5176";
        ShortUrlResult netEaseShortUrlResult = null;
        ShortUrlResult googleShortUrlResult = null;

        try{
            if ( !(longurl == null || longurl.equals("")) ) {
                baiduShortUrlResult = ShortUrlBaidu.getShortUrl(longurl);
                netEaseShortUrlResult = ShortUrlNetEase.getShortUrl(longurl, key);
                googleShortUrlResult = ShortUrlGoogle.getShortUrl(longurl);
                request.setAttribute("baiduShortUrlResult", baiduShortUrlResult);
                request.setAttribute("netEaseShortUrlResult", netEaseShortUrlResult);
                request.setAttribute("googleShortUrlResult", googleShortUrlResult);
            }
        }catch(Exception e){
            //非法网址有可能导致异常，不用处理，重新回到输入页面即可
        }finally{
            RequestDispatcher view = request.getRequestDispatcher("./index.jsp");
            view.forward(request, response);
        }

    }

}
