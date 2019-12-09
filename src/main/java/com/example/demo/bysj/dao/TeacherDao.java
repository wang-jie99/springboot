package com.example.demo.bysj.dao;

import com.example.demo.bysj.domain.Teacher;
import com.example.demo.bysj.domain.User;
import com.example.demo.bysj.service.DegreeService;
import com.example.demo.bysj.service.DepartmentService;
import com.example.demo.bysj.service.ProfTitleService;
import com.example.demo.util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao = new TeacherDao();

	private TeacherDao() {
	}

	public static TeacherDao getInstance() {
		return teacherDao;
	}
//	static{
//		ProfTitle assProf = ProfTitleDao.getInstance().find(2);
//		ProfTitle lecture = ProfTitleDao.getInstance().find(3);
//
//		Degree phd = DegreeDao.getInstance().find(1);
//		Degree master = DegreeDao.getInstance().find(2);
//
//
//		Department misDept = DepartmentService.getInstance().find(2);
//
//		teachers = new TreeSet<Teacher>();
//		Teacher teacher = new Teacher(1,"苏同",assProf,phd,misDept);
//		teachers.add(teacher);
//		teachers.add(new Teacher(2,"刘霞",lecture,phd,misDept));
//		teachers.add(new Teacher(3,"王方",assProf,phd,misDept));
//		teachers.add(new Teacher(4,"刘峰",assProf,master,misDept));
//	}

	public Collection<Teacher> findAll() throws SQLException {
		TreeSet<Teacher> teachers = new TreeSet<>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()) {
			teachers.add(new Teacher(resultSet.getInt("id"), resultSet.getString("name"),
					resultSet.getString("no"),
					ProfTitleService.getInstance().find(resultSet.getInt("title")),
					DegreeService.getInstance().find(resultSet.getInt("degree")),
					DepartmentService.getInstance().find(resultSet.getInt("department"))));
		}
		//执行预编译语句
		JdbcHelper.close(resultSet, stmt, connection);
		return teachers;
	}

	public Teacher find(Integer id) throws SQLException {
		Teacher teacher = null;
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM teacher WHERE id=?");
		//为预编译参数赋值
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,name,title,department,degree值为参数，创建对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()) {
			teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("name"),
					resultSet.getString("no"),
					ProfTitleService.getInstance().find(resultSet.getInt("title")),
					DegreeService.getInstance().find(resultSet.getInt("degree")),
					DepartmentService.getInstance().find(resultSet.getInt("department")));
		}
		//关闭资源
		JdbcHelper.close(resultSet, preparedStatement, connection);
		return teacher;
	}

	public boolean update(Teacher teacher) throws SQLException, ClassNotFoundException {
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(
				"UPDATE teacher SET name=?,title=?,degree=?,department=? WHERE id=?");
		//为预编译参数赋值
		preparedStatement.setString(1, teacher.getName());
		preparedStatement.setInt(2, teacher.getTitle().getId());
		preparedStatement.setInt(3, teacher.getDegree().getId());
		preparedStatement.setInt(4, teacher.getDepartment().getId());
		preparedStatement.setInt(5, teacher.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改了" + affectedRowNum + "条记录");
		JdbcHelper.close(preparedStatement, connection);
		return affectedRowNum > 0;
	}

	public void add(Teacher teacher) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			//获取数据库连接对象
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			//添加预编译语句
			preparedStatement = connection.prepareStatement(
					"INSERT INTO teacher (name,no,title,degree,department) VALUES (?,?,?,?,?)");
			preparedStatement.setString(1, teacher.getName());
			preparedStatement.setString(2, teacher.getNo());
			preparedStatement.setInt(3, teacher.getTitle().getId());
			preparedStatement.setInt(4, teacher.getDegree().getId());
			preparedStatement.setInt(5, teacher.getDepartment().getId());
			//执行预编译对象的executeUpdate()方法，获得删除行数
			int affectedRowNum = preparedStatement.executeUpdate();
			System.out.println("添加了" + affectedRowNum + "条教师记录");
			preparedStatement = connection.prepareStatement("SELECT * FROM teacher WHERE id=(select MAX(id) from teacher)");
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Teacher teacher1 = new Teacher(resultSet.getInt("id"), resultSet.getString("name"),
						resultSet.getString("no"),
						ProfTitleService.getInstance().find(resultSet.getInt("title")),
						DegreeService.getInstance().find(resultSet.getInt("degree")),
						DepartmentService.getInstance().find(resultSet.getInt("department")));
				User user = new User(teacher1.getNo(), teacher1.getNo(), new Date(), teacher1);
				UserDao.getInstance().add(user, connection);
			}
			connection.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "\n errorCode=" + e.getErrorCode());
			try {
				//回滚当前连接所做的操作
				if (connection != null) {
					//事务以回滚结束
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				//恢复自动提交
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JdbcHelper.close(preparedStatement, connection);
		}
	}

	public void delete(Integer id) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);
			UserDao.getInstance().delete(id, connection);
			//在该连接上创建预编译语句对象
			preparedStatement = connection.prepareStatement("DELETE FROM teacher WHERE id=?");
			//为预编译参数赋值
			preparedStatement.setInt(1, id);
			//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
			int affectedRows = preparedStatement.executeUpdate();
			System.out.println("删除了" + affectedRows + "条教师记录");
			connection.commit();
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "\n errorCode=" + e.getErrorCode());
			try {
				//回滚当前连接所做的操作
				if (connection != null) {
					//事务以回滚结束
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				//恢复自动提交
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(preparedStatement, connection);
		}
	}
}
