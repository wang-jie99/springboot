package com.example.demo.bysj.dao;

import com.example.demo.bysj.domain.User;
import com.example.demo.bysj.service.TeacherService;
import com.example.demo.util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
//	private static Collection<User> users;
//	static{
//		TeacherDao teacherDao = TeacherDao.getInstance();
//		users = new TreeSet<User>();
//		User user = new User(1,"st","st",new Date(),teacherDao.find(1));
//		users.add(user);
//		users.add(new User(2,"lx","lx",new Date(),teacherDao.find(2)));
//		users.add(new User(3,"wx","wx",new Date(),teacherDao.find(3)));
//		users.add(new User(4,"lf","lf",new Date(),teacherDao.find(4)));
//	}
	public Collection<User> findAll()throws SQLException {
		TreeSet<User> users = new TreeSet<>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from user");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			users.add(new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))));
		}
		JdbcHelper.close(resultSet,stmt,connection);
		return users;
	}
	
	public User find(Integer id)throws SQLException{
		User desiredUser = null;
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id=?");
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,name,teacher值为参数，创建对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			desiredUser = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return desiredUser;
	}
	//定义一个方法使可以按用户名查询findByUsername
	public User findByUsername(String username) throws SQLException{
		User findByUsername = null;
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT * FROM user WHERE username=?");
		preparedStatement.setString(1,username);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()){
			findByUsername = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return findByUsername;
	}
	//修改用户名，密码
	public boolean update(User user)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(
				"UPDATE user SET username=?,password=? WHERE id=?");
		//为预编译参数赋值
		preparedStatement.setString(1,user.getUsername());
		preparedStatement.setString(2,user.getPassword());
		preparedStatement.setInt(3,user.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改了"+ affectedRowNum + "条记录");
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}
	
	public boolean add(User user,Connection connection)throws SQLException{
		//添加预编译语句
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO user (username,password,teacher_id) VALUES (?,?,?)");
		preparedStatement.setString(1,user.getUsername());
		preparedStatement.setString(2,user.getPassword());
		preparedStatement.setInt(3,user.getTeacher().getId());
		//执行预编译对象的executeUpdate()方法，获得删除行数
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("添加了"+ affectedRowNum + "条用户记录");
//		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Integer teacher_id,Connection connection)throws SQLException{
		//Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteUser_sql = "DELETE FROM user WHERE teacher_id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteUser_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,teacher_id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("删除了" + affectedRows + "条用户记录");
		//关闭资源
//		JdbcHelper.close(preparedStatement,connection);
			return affectedRows > 0;
	}

	public User login(String username, String password)throws SQLException{
		User user = null;
 		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String loginUser_sql = "select * from user where username=? and password=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(loginUser_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,username);
		preparedStatement.setString(2,password);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()){
			user = new User(resultSet.getInt("id"),resultSet.getString("username"),
					resultSet.getString("password"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return user;
	}
	
//	public boolean delete(User user){
//		return users.remove(user);
//	}

//	public static void main(String[] args)throws SQLException{
//		User dao = new User(3,"dye","dye",new Date(), TeacherDao.getInstance().find(3));
//		System.out.println(dao);
//		//UserDao.getInstance().add(dao);
//	}

//	private static void display(Collection<User> users) {
//		for (User user : users) {
//			System.out.println(user);
//		}
//	}
}
