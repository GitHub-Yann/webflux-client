package com.yann.demo.framework.interfaces;

import com.yann.demo.framework.beans.MethodInfo;
import com.yann.demo.framework.beans.ServerInfo;

public interface RestHandler {

	void init(ServerInfo serviceInfo);//初始化服务器信息

	Object invokeRest(MethodInfo methodInfo);//调用rest请求，返回接口
	
}
