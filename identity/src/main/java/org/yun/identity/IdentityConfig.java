package org.yun.identity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;



/*
 * 针对Identity模块的配置
 * */

@SpringBootApplication
@ComponentScan("org.yun")
@EnableJpaRepositories//激活JPA的自动Dao扫描
public class IdentityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		//第一种加密方式
		//PasswordEncoder encode = new BCryptPasswordEncoder();
		//第二种
		//PasswordEncoder encode = new SCryptPasswordEncoder();
		//第三种
		Map<String, PasswordEncoder> map = new HashMap<>();
		map.put("B", new BCryptPasswordEncoder());
		map.put("S", new SCryptPasswordEncoder());
		
		//第一个参数表示默认的编码器
		DelegatingPasswordEncoder encode = new DelegatingPasswordEncoder("S",map);
		//在没有找到密码加密器的时候，使用BCryptPasswordEncoder
		encode.setDefaultPasswordEncoderForMatches(map.get("B"));
		return encode;
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(IdentityConfig.class, args);
	}
	
}
