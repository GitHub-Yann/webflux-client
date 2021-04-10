package com.yann.demo.framework.interfaces;

/**
 * 创建代理类接口
 * @author Administrator
 *
 */
public interface ProxyCreator {
	Object createProxy(Class<?> type);
}
