package com.example.demo.bysj.service;

import com.example.demo.bysj.dao.ProfTitleDao;
import com.example.demo.bysj.domain.ProfTitle;

import java.sql.SQLException;
import java.util.Collection;

public final class ProfTitleService {
	private static ProfTitleDao profTitleDao= ProfTitleDao.getInstance();
	private static ProfTitleService profTitleService=new ProfTitleService();
	private ProfTitleService(){}

	public static ProfTitleService getInstance(){
		return profTitleService;
	}

	public Collection<ProfTitle> getAll()throws SQLException {
		return profTitleDao.findAll();
	}

	public ProfTitle find(Integer id)throws SQLException{
		return profTitleDao.find(id);
	}

	public boolean update(ProfTitle profTitle)throws SQLException,ClassNotFoundException{
		return profTitleDao.update(profTitle);
	}

	public boolean add(ProfTitle profTitle)throws SQLException{
		return profTitleDao.add(profTitle);
	}

	public boolean delete(Integer id)throws SQLException{
		return profTitleDao.delete(id);
	}

//	public boolean delete(ProfTitle profTitle){
//		return delete(profTitle);
//	}
}

