package com.yann.demo.framework.handler;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.yann.demo.framework.beans.MethodInfo;
import com.yann.demo.framework.beans.ServerInfo;
import com.yann.demo.framework.interfaces.RestHandler;

import reactor.core.publisher.Mono;

public class WebClientRestHandler implements RestHandler {

	private WebClient client;
	
	@Override
	public void init(ServerInfo serviceInfo) {
		this.client = WebClient.create(serviceInfo.getUrl());

	}

	@Override
	public Object invokeRest(MethodInfo methodInfo) {
		Object result = null;
		RequestBodySpec req = this.client
			//请求方法
			.method(methodInfo.getMethod())
			//请求url 和 参数
			.uri(methodInfo.getUrl(),methodInfo.getParmas())
			.accept(MediaType.APPLICATION_JSON);
		ResponseSpec retrieve=null;
		if(methodInfo.getBody()!=null) {
			//设置请求体和请求体的实际类型
			retrieve = req.body(methodInfo.getBody(), methodInfo.getBodyElementType()).retrieve();
		}else {
			retrieve = req.retrieve();
		}
		
		//处理异常
		retrieve.onStatus(status-> status.value()==404, response->Mono.just(new RuntimeException("Not Found")));
		
		if(methodInfo.isReturnFlux()) {
			result = retrieve.bodyToFlux(methodInfo.getReturnElementType());
		}else {
			result = retrieve.bodyToMono(methodInfo.getReturnElementType());
		}
		return result;
	}

}
