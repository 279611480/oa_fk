package org.yun.identity;

import org.yun.identity.domain.User;

public class UserHolder {
	/***
	 * 因为   security模块并不能以依赖前面用户模块的User（见第一天的图片，会死循环），
	 * 而安全控制模块有需要登录用户后才能看到信息，所以写方法拿。
	 * 
	 * 用户模块提供方法拿到线程的user（里面是，线程user的信息）
	 * 
	 * 此工具的目的是把User（用户）存储在当前线程里面，方便在其它的模块使用 
	 * */
	//1.创建一个线程
	private static final ThreadLocal<User> THREAD_LOCAL = new ThreadLocal<>();
	//01.拿到 线程里面的用户信息
	public static User get() {
		return THREAD_LOCAL.get();
	}
	//02.将用户设置到线程里面去
	public static void set(User user) {
		THREAD_LOCAL.set(user);
	}
	//03.每次拿完，都要清理现场
	public static void remove() {
		THREAD_LOCAL.remove();
	}
	
	
}
