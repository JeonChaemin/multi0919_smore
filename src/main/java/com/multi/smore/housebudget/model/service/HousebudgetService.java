package com.multi.smore.housebudget.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.smore.housebudget.model.mapper.HousebudgetMapper;
import com.multi.smore.housebudget.model.vo.Housebudget;

@Service
public class HousebudgetService {
	
	@Autowired
	private HousebudgetMapper mapper;
	
	public int selectHbCount(int memNo) {
		return mapper.selectHbCount(memNo);
	}
	
	public List<Housebudget> selectAllHb(Map<String, Object> searchMap) {
		return mapper.selectAllHb(searchMap);
	}
	
	public List<String> selectHbByRange(Map<String, Object> searchMap) {
		return mapper.selectHbByRange(searchMap);
	}
	
	@Transactional
	public int insertHb(Housebudget hb) {
		return mapper.insertHb(hb);
	}

	@Transactional
	public int updateHb(Map<String, Object> updateMap) {
		return mapper.updateHb(updateMap);
	}
	
	@Transactional
	public int deleteHb(Map<String, Object> updateMap) {
		return mapper.deleteHb(updateMap);
	}
	
}
