package com.example.demo.bysj.dao;

import com.example.demo.bysj.domain.School;
import com.example.demo.util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
//	static{
//		schools = new TreeSet<School>();
//		School school = new School(1,"土木工程","01","");
//		schools.add(school);
//		schools.add(new School(2,"管理工程","02","最好的学院"));
//		schools.add(new School(3,"市政工程","03",""));
//		schools.add(new School(4,"艺术","04",""));
//	}
	
	private SchoolDao(){}
	
	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Collection<School> findAll() throws SQLException {
		TreeSet<School> schools = new TreeSet<School>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from school");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			schools.add(new School(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks")));
		}
		//执行预编译语句
		JdbcHelper.close(resultSet,stmt,connection);
		return schools;
	}
	
	public School find(Integer id) throws SQLException{
		School school = null;
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM school WHERE id=?");
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks,school值为参数，创建对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			school = new School(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return school;
	}

	public boolean update(School school) throws ClassNotFoundException,SQLException{
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateSchool_sql = " update school set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateSchool_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setString(2,school.getNo());
		preparedStatement.setString(3,school.getRemarks());
		preparedStatement.setInt(4,school.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("更新了" + affectedRows + "条记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	
	public boolean add(School school) throws SQLException{
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//添加预编译语句
		PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO school (no,description,remarks) VALUES (?,?,?)");
		preparedStatement.setString(1,school.getNo());
		preparedStatement.setString(2,school.getDescription());
		preparedStatement.setString(3,school.getRemarks());
		//执行预编译对象的executeUpdate()方法，获得删除行数
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("添加了"+ affectedRowNum + "条记录");
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Integer id)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(
				"DELETE FROM school WHERE id=?");
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("删除了"+ affectedRows + "条记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
}
