package com.example.demo.bysj.dao;

import com.example.demo.bysj.domain.Department;
import com.example.demo.bysj.service.SchoolService;
import com.example.demo.util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class DepartmentDao {
	private static DepartmentDao departmentDao=new DepartmentDao();
	private DepartmentDao(){}

	public static DepartmentDao getInstance(){
		return departmentDao;
	}


	public Collection<Department> findAll() throws SQLException {
		TreeSet<Department> departments = new TreeSet<Department>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from department");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			departments.add(new Department(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"),
					SchoolDao.getInstance().find(resultSet.getInt("school_id"))));
		}
		//执行预编译语句
		JdbcHelper.close(resultSet,stmt,connection);
		return departments;
	}

    //定义一个方法用来返回schoolId指定的系
    public Collection<Department> findAllBySchool(Integer schoolId) throws SQLException{
        TreeSet<Department> departments = new TreeSet<Department>();
        Connection connection = JdbcHelper.getConn();
        PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM department WHERE school_id=?");
        preparedStatement.setInt(1,schoolId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            departments.add(new Department(resultSet.getInt("id"),resultSet.getString("description"),
                    resultSet.getString("no"),resultSet.getString("remarks"),
                    SchoolService.getInstance().find(schoolId)));
        }
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return departments;
    }

	public Department find(Integer id) throws SQLException{
		Department department = null;
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT * FROM department WHERE id=?");
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks,school值为参数，创建对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			department = new Department(resultSet.getInt("id"),resultSet.getString("description"),
					resultSet.getString("no"),resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id")));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return department;
	}

	public boolean update(Department department) throws ClassNotFoundException,SQLException{
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateSchool_sql = " update department set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateSchool_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,department.getDescription());
		preparedStatement.setString(2,department.getNo());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("更新了" + affectedRows + "条记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public boolean add(Department department) throws SQLException{
        //获取数据库连接对象
        Connection connection = JdbcHelper.getConn();
        //添加预编译语句
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO department (no,description,remarks,school_id) VALUES (?,?,?,?)");
        preparedStatement.setString(1,department.getNo());
        preparedStatement.setString(2,department.getDescription());
        preparedStatement.setString(3,department.getRemarks());
        preparedStatement.setInt(4,department.getSchool().getId());
        //执行预编译对象的executeUpdate()方法，获得删除行数
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("添加了"+ affectedRowNum + "条记录");
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(
				"DELETE FROM department WHERE id=?");
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

