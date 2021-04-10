package com.yann.demo.framework.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.yann.demo.framework.annotation.ApiServer;
import com.yann.demo.framework.beans.MethodInfo;
import com.yann.demo.framework.beans.ServerInfo;
import com.yann.demo.framework.handler.WebClientRestHandler;
import com.yann.demo.framework.interfaces.ProxyCreator;
import com.yann.demo.framework.interfaces.RestHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 使用jdk动态代理实现代理类
 * @author Administrator
 *
 */
public class JDKProxyCreator implements ProxyCreator {
	
	@Override
	public Object createProxy(Class<?> type) {
		// 根据接口得到API服务器信息
		ServerInfo serviceInfo = extractServerInfo(type);
		// 给每一个代理类一个实现
		RestHandler handler = new WebClientRestHandler();
		// 初始化服务器信息
		handler.init(serviceInfo);
		System.out.println("serviceInfo--->"+serviceInfo);
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {type}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				//根据方法和参数得到调用信息
				MethodInfo methodInfo = extractMethodInfo(method,args);
				System.out.println(methodInfo);
				//调用rest
				return handler.invokeRest(methodInfo);
			}

			private MethodInfo extractMethodInfo(Method method, Object[] args) {
				MethodInfo methodInfo = new MethodInfo();
				//得到方法上面的url和请求方法
				Annotation[] annos = method.getAnnotations();
				for(Annotation anno : annos) {
					if(anno instanceof GetMapping) {
						GetMapping a = (GetMapping)anno;
						methodInfo.setUrl(a.value()[0]);
						methodInfo.setMethod(HttpMethod.GET);
					}else if(anno instanceof PostMapping) {
						PostMapping a = (PostMapping)anno;
						methodInfo.setUrl(a.value()[0]);
						methodInfo.setMethod(HttpMethod.POST);
					}else if(anno instanceof DeleteMapping) {
						DeleteMapping a = (DeleteMapping)anno;
						methodInfo.setUrl(a.value()[0]);
						methodInfo.setMethod(HttpMethod.DELETE);
					}
				}
				//得到调用方法的参数和body
				Parameter[] params = method.getParameters();
				Map<String,Object> pMap = new LinkedHashMap<>();
				methodInfo.setParmas(pMap);
				for(int i=0;i<params.length;i++) {//循环方法的参数定义
					//是否带有@PathVariable
					PathVariable annoPath = params[i].getAnnotation(PathVariable.class);
					if(annoPath!=null) {
						pMap.put(annoPath.value(), args[i]);
					}
					//是否带有@RequestBody
					RequestBody annoReqBody = params[i].getAnnotation(RequestBody.class);
					if(annoReqBody!=null) {
						//设置body
						methodInfo.setBody((Mono<?>) args[i]);
						//设置body的实际类型
						Type[] typex = ((ParameterizedType)params[i].getParameterizedType()).getActualTypeArguments();
						methodInfo.setBodyElementType((Class<?>) typex[0]);
					}
				}
				//得到调用方法的返回类型
				//method.getReturnType() 这个方法不会带泛型信息
				//isAssignableFrom判断类型是否是某个的子类
				//instanceof判断实例是否是某个的子类
				methodInfo.setReturnFlux(method.getReturnType().isAssignableFrom(Flux.class));
				Type[] type = ((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments();
				methodInfo.setReturnElementType((Class<?>) type[0]);
				return methodInfo;
			}
		});
	}

	private ServerInfo extractServerInfo(Class<?> type) {
		ServerInfo serverInfo = new ServerInfo();
		ApiServer apiServerAnno = type.getAnnotation(ApiServer.class);
		serverInfo.setUrl(apiServerAnno.value());
		return serverInfo;
	}

}
