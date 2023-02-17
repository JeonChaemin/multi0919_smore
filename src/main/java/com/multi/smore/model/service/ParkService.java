package com.multi.smore.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.model.mapper.ParkMapper;
import com.multi.smore.model.vo.Park;
import com.multi.smore.model.vo.ReplyPark;

@Service
public class ParkService {

	@Autowired
	private ParkMapper mapper;
	
	@Transactional(rollbackFor = Exception.class)
	public Park selectParkByNo(int parkNo, int memNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("parkNo", ""+parkNo);		
		map.put("memNo", ""+memNo);
		Park park = mapper.selectParkByNo(map); 
		
		
		return park; 
	}
	
	public int selectParkCount(Map<String, Object> param) {
		return mapper.selectParkCount(param);
	}
	
	
	public List<Park> selectParkList(PageInfo pageInfo, Map<String, Object> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() - 1));
		return mapper.selectParkList(param);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int insertPark(Park park) {
		int result = 0;
		result = mapper.insertPark(park);
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int insertReview(ReplyPark replypark) {
		int result = 0;
		if(replypark.getParkNo() == 0) {
			result = mapper.insertReplyPark(replypark);
		}
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int deleteReview(int parkRno) {
		return mapper.deleteReplyPark(parkRno);
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int updateReview(ReplyPark replypark) {
		int result= 0;
		if(replypark.getParkRno() >= 1) {
			result = mapper.updateReplyPark(replypark);
		}
		return result;
	}
	
	public List<Park> selectParkListHot(List<String> parkItem) {
		return mapper.selectParkListHot(parkItem);
	}
	
	// 스크랩		
	@Transactional(rollbackFor = Exception.class)
	public int clipPark(int memNo, int parkNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("tradeNo", ""+parkNo);
		return mapper.clipPark(map);
	}
	// 스크랩 풀기
	@Transactional(rollbackFor = Exception.class)
	public int unClipPark(int memNo, int parkNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("tradeNo", ""+parkNo);
		return mapper.unClipPark(map);
	}
}
