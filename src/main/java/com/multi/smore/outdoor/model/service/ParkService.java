package com.multi.smore.outdoor.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.outdoor.model.mapper.ParkMapper;
import com.multi.smore.outdoor.model.vo.Park;
import com.multi.smore.outdoor.model.vo.ParkClip;
import com.multi.smore.outdoor.model.vo.ParkReply;

@Service
public class ParkService {

	@Autowired
	private ParkMapper mapper;
	
	@Transactional(rollbackFor = Exception.class)
	public Park selectParkByNo(int parkNo, int memNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("parkNo", parkNo);		
		map.put("memNo", memNo);
		Park park = mapper.selectParkByNo(map); 
		
		
		return park; 
	}
	
	public int selectParkCount(Map<String, Object> param) {
		return mapper.selectParkCount(param);
	}
	
	
	public List<Park> selectParkList(PageInfo pageInfo, Map<String, Object> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList()-1));
		return mapper.selectParkList(param);
	}
	
	
	public int insertPark(Park park) {
		return mapper.insertPark(park);
	}
	
	

	public List<ParkReply> selectParkReplyListByNo(int parkNo) {
		return mapper.selectParkReplyListByNo(parkNo);
	}
	
	public int selectParkReplyCountByNo(int parkRno) {
		return mapper.selectParkReplyCountByNo(parkRno);
	}
	
	public List<ParkClip> selectParkClipList(PageInfo pageInfo, Map<String, String> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() - 1));
		return mapper.selectParkClipList(param);
	}
	
	public ParkClip selectParkClipByNo(Map<String, String> param) {
		return mapper.selectParkClipByNo(param);
	}

	public int insertParkReply(ParkReply parkReply) {
		int result=0;
		result=mapper.insertParkReply(parkReply);
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int deleteParkReply(int parkRno) {
		return mapper.deleteParkReply(parkRno);
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int updateParkReply(ParkReply parkReply) {
		return mapper.updateParkReply(parkReply);
	}
	
	public List<Park> selectParkListHot(List<String> parkItem) {
		return mapper.selectParkListHot(parkItem);
	}
	
	// 스크랩		
	@Transactional(rollbackFor = Exception.class)
	public int clipPark(int memNo, int parkNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("parkNo", ""+parkNo);
		return mapper.insertParkClip(map);
	}
	// 스크랩 풀기
	@Transactional(rollbackFor = Exception.class)
	public int unClipPark(int memNo, int parkNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("parkNo", ""+parkNo);
		return mapper.deleteParkClip(map);
	}


}
