package com.example.demo.bysj.dao;

import com.example.demo.bysj.domain.ProfTitle;
import com.example.demo.util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	//private static Collection<ProfTitle> profTitles;

	public Collection<ProfTitle> findAll() throws SQLException {
		Set<ProfTitle> profTitles = new HashSet<>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from profTitle");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			profTitles.add(new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks")));
		}
		//执行预编译语句
		JdbcHelper.close(resultSet,stmt,connection);
		return profTitles;
	}

	public ProfTitle find(Integer id)throws SQLException{
		ProfTitle profTitle = null;
		Connection connection = JdbcHelper.getConn();
		String deleteDegree_sql = "SELECT * FROM profTitle WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDegree_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return profTitle;
	}

	public boolean update(ProfTitle profTitle) throws SQLException,ClassNotFoundException{
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateDegree_sql = " update profTitle set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		preparedStatement.setInt(4,profTitle.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public boolean add(ProfTitle profTitle) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		String addDegree_sql = "INSERT INTO profTitle (description,no,remarks) VALUES"+" (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(addDegree_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		//执行预编译语句，获取添加记录行数并赋值给affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}


		public boolean delete(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteDegree_sql = "DELETE FROM profTitle WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDegree_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

//	public boolean delete(ProfTitle profTitle){
//		return profTitles.remove(profTitle);
//	}
}

