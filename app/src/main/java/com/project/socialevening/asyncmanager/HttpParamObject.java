package com.project.socialevening.asyncmanager;


import com.project.socialevening.Network.ClientURLConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpParamObject {

    private String url = "";
    private List<NameValuePair> postParams = new ArrayList<NameValuePair>();
    private HashMap<String, String> headers = new HashMap<String, String>();
    private Class classType;
    private String method = ClientURLConnection.GET_METHOD;
    private String json = "";
    private String contentType = "application/x-www-form-urlencoded;charset=UTF-8";

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<NameValuePair> getDiscvrParams() {
        return discvrParams;
    }

    public void setDiscvrParams(List<NameValuePair> discvrParams) {
        this.discvrParams = discvrParams;
    }

    private List<NameValuePair> discvrParams = new ArrayList<NameValuePair>();

    public String getMethod() {
        return method;
    }

    public void setPutMethod() {
        this.method = ClientURLConnection.PUT_METHOD;
    }

    public void setPostMethod() {
        this.method = ClientURLConnection.POST_METHOD;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<NameValuePair> getPostParams() {
        return postParams;
    }

    public void setPostParams(List<NameValuePair> postParams) {
        this.postParams = postParams;
    }

    public Class getClassType() {
        return classType;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }

    public void addNameValuePair(NameValuePair pair) {
        postParams.add(pair);
    }

    public void addParameter(String name, String value) {
        NameValuePair pair = new BasicNameValuePair(name, value);
        postParams.add(pair);
    }

    public void addDiscvrParam(String name, String value) {
        NameValuePair pair = new BasicNameValuePair(name, value);
        discvrParams.add(pair);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public static HttpParamObject createURLWithSlashes(HttpParamObject obj, String pageURL) {
        StringBuilder builder = new StringBuilder(obj.url);
        builder.append(pageURL);
        for (NameValuePair pair : obj.discvrParams) {
            builder.append("/");
            builder.append(pair.getName());
            builder.append("/");
            builder.append(pair.getValue());
        }
//        builder.deleteCharAt(builder.length() - 1);
        obj.setUrl(builder.toString());
        return obj;
    }

    public HttpParamObject createURLWithSlash(String pageURL) {
        StringBuilder builder = new StringBuilder(this.url);
        builder.append(pageURL);
        for (NameValuePair pair : this.discvrParams) {
            builder.append("/");
            builder.append(pair.getName());
            builder.append("/");
            builder.append(pair.getValue());
        }
//        builder.deleteCharAt(builder.length() - 1);
        this.setUrl(builder.toString());
        return this;
    }

}
