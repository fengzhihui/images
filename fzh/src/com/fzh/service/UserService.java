package com.fzh.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.UserDAO;
import com.fzh.entity.User;
import com.fzh.util.encrypt.TjrAES;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;

	public User findByUsernameAndPwd(String username,String password) throws Exception{
		return userDAO.findByUsernameAndPwd(username, TjrAES.encrypt(password));
	}
	
	public int saveUser(User user) throws Exception {
		user.setCreateTime(new Date());
		user.setPassword(TjrAES.encrypt(user.getPassword()));
		return userDAO.insert(user);
	}
	
	public int updateUser(User user) throws Exception {
		return userDAO.update(user, "id");
	}
	
	public User findByUsername(String username) {
		return userDAO.findByUsername(username);
	}
	
	
}
