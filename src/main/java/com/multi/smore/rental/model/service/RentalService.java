package com.multi.smore.rental.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.rental.model.mapper.RentalMapper;
import com.multi.smore.rental.model.vo.Rental;

@Service
public class RentalService {

	@Autowired
	private RentalMapper mapper;
	
	@Transactional(rollbackFor = Exception.class)
	public int getRentalCount(Map<String, Object> param) {
		return mapper.selectRentalCount(param);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public List<Rental> getRentalList(PageInfo pageInfo, Map<String, Object> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() - 1));
		return mapper.selectRentalList(param);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public List<Rental> getRentalListHome(Map<String, String> paramMap){
		return mapper.selectRentalListHome(paramMap);
	}	
		
	@Transactional(rollbackFor = Exception.class)
	public int clipRental(int memNo, int rentalNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("rentalNo", ""+rentalNo);
		return mapper.clipRental(map);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int unClipRental(int memNo, int rentalNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("rentalNo", ""+rentalNo);
		return mapper.unClipRental(map);
	}
	@Transactional(rollbackFor = Exception.class)
	// 실시간 스크랩 수
	public int clipCount(int rentalNo) {
		return mapper.clipCount(rentalNo);
	}
}
