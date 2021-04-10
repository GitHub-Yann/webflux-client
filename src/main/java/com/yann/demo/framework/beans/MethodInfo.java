package com.yann.demo.framework.beans;

import java.util.Map;

import org.springframework.http.HttpMethod;

import reactor.core.publisher.Mono;

public class MethodInfo {
	private String url;//请求url
	private HttpMethod method;//请求方法
	private Map<String,Object> parmas;//请求参数（url）
	private Mono<?> body;//请求体
	private Class<?> bodyElementType;//请求体类型
	private boolean returnFlux;//是flux还是mono
	private Class<?> returnElementType;//返回类型
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public HttpMethod getMethod() {
		return method;
	}
	public void setMethod(HttpMethod method) {
		this.method = method;
	}
	public Map<String, Object> getParmas() {
		return parmas;
	}
	public void setParmas(Map<String, Object> parmas) {
		this.parmas = parmas;
	}
	public Mono<?> getBody() {
		return body;
	}
	public void setBody(Mono<?> body) {
		this.body = body;
	}
	public boolean isReturnFlux() {
		return returnFlux;
	}
	public void setReturnFlux(boolean returnFlux) {
		this.returnFlux = returnFlux;
	}
	public Class<?> getReturnElementType() {
		return returnElementType;
	}
	public void setReturnElementType(Class<?> returnElementType) {
		this.returnElementType = returnElementType;
	}
	public Class<?> getBodyElementType() {
		return bodyElementType;
	}
	public void setBodyElementType(Class<?> bodyElementType) {
		this.bodyElementType = bodyElementType;
	}
	@Override
	public String toString() {
		return "MethodInfo [url=" + url + ", method=" + method + ", parmas=" + parmas + ", body=" + body
				+ ", bodyElementType=" + bodyElementType + ", returnFlux=" + returnFlux + ", returnElementType="
				+ returnElementType + "]";
	}
}
