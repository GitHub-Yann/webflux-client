package com.yann.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yann.demo.apiservice.IUserApi;

@RestController
public class TestController {

	@Autowired
	private IUserApi userApi;
	
	@GetMapping("/")
	public void test() {
//		Flux<User> users = userApi.getAllUser();
//		users.subscribe(u->{
//			System.out.println("id"+u.getId()+", name="+u.getName()+", age="+u.getAge());
//		});
		
		userApi.getUserById("001").subscribe(u1->{
			System.out.println("id="+u1.getId()+", name="+u1.getName()+", age="+u1.getAge());
		},e->{
			System.err.println("找不到用户");
		});
		
		userApi.getUserById("002").subscribe(u1->{
			System.out.println("id="+u1.getId()+", name="+u1.getName()+", age="+u1.getAge());
		},e->{
			System.err.println("找不到用户");
		});
		
//		User nu = new User();
//		nu.setAge(11);
//		nu.setId("u0001");
//		nu.setName("asdfasdfasf");
//		Mono<User> user = userApi.createUser(Mono.just(nu));
//		user.subscribe(u->{
//			System.out.println("id="+u.getId()+", name="+u.getName()+", age="+u.getAge());
//		});
		
//		userApi.deleteUserById("ssssssss").subscribe();
		
//		Mono<User> user1 = WebClient.create("http://localhost:8080/user").method(HttpMethod.POST).uri("/").accept(MediaType.APPLICATION_JSON)
//			.body(Mono.just(nu),User.class).retrieve().bodyToMono(User.class);
//		user1.subscribe(System.out::println);
	}
}
