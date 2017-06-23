package com.fzh.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fzh.entity.User;
import com.fzh.service.KeysetService;
import com.fzh.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:WebContent/WEB-INF/spring-servlet.xml")
public class JunitTest {
	@Autowired
	private UserService userService;
	@Autowired
	private KeysetService keysetService;
	
	@Test
	public void checkUserId() {
		try {
			User user = new User();
			user.setUsername("冯志辉");
			user.setLevel(6);
			System.out.println(userService.findByUsername("fzh"));
			System.out.println(keysetService.findKeywords("gh_7672614010ac"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
