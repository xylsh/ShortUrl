package com.github.xylsh.web.util;

public class ShortUrlResult {
	private int status;          //结果状态码
	private String statustxt;    //状态说明
	private String longurl;
	private String tinyurl;      //短网址
	private String errmsg;
	private String alias;        //自定义短网址
	private boolean isSuccess;  //缩短或还原网址是否成功
	
	public ShortUrlResult(){
	    status = -1;
	    statustxt = null;
	    longurl = null;
	    tinyurl = null;
	    errmsg = null;
	    alias = null;
	    isSuccess = false;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatustxt() {
		return statustxt;
	}
	public void setStatustxt(String statustxt) {
		this.statustxt = statustxt;
	}
	public String getLongurl() {
		return longurl;
	}
	public void setLongurl(String longurl) {
		this.longurl = longurl;
	}
	public String getTinyurl() {
		return tinyurl;
	}
	public void setTinyurl(String tinyurl) {
		this.tinyurl = tinyurl;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
    public boolean getIsSuccess() {
        return isSuccess;
    }
    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
