package com.fzh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.MenuKeySetDAO;
import com.fzh.entity.MenuKeyset;

@Service
public class MenuKeySetService {
	@Autowired
	private MenuKeySetDAO menuKeySetDAO;
	
	public void save(MenuKeyset menuKeySet){
		menuKeySetDAO.save(menuKeySet);
	}
	
	public void update(MenuKeyset menuKeySet){
		menuKeySetDAO.update(menuKeySet);
	}
	
	public void delete(String menuKeySetNo){
		menuKeySetDAO.delete(menuKeySetNo);
	}
	
	public void deleteByWxNo(String wxno){
		menuKeySetDAO.deleteByWxNo(wxno);
	}
	
	public MenuKeyset findByPk(String pk){
		return menuKeySetDAO.findByPK(pk);
	}
	
	public List<MenuKeyset> findByWxNo(String weixinPublicNo){
		return menuKeySetDAO.findByWxNo(weixinPublicNo);
	}
	
}
