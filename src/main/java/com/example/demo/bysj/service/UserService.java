package com.example.demo.bysj.service;

import com.example.demo.bysj.dao.UserDao;
import com.example.demo.bysj.domain.User;

import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	
	public UserService() {
	}
	
	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> findAll()throws SQLException {
		return userDao.findAll();
	}
	
	public User find(Integer id)throws SQLException{
		return userDao.find(id);
	}
	
	public boolean updateUser(User user)throws SQLException{
		return userDao.update(user);
	}

	public User findByUsername(String username) throws SQLException{
		return userDao.findByUsername(username);
	}

	public User login(String username, String password)throws SQLException{
		return userDao.login(username,password);
	}	
}
