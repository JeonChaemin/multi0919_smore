package com.multi.smore.oneprogram.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.oneprogram.model.mapper.OneProgramMapper;
import com.multi.smore.oneprogram.model.vo.OneProgram;

@Service
public class OneProgramService {
	@Autowired
	private OneProgramMapper mapper;
	
//	@Transactional(rollbackFor = Exception.class)
//	public int insertOneProgram(OneProgram op) {
//		return mapper.insertOneProgram(op);
//	}
	
	public List<OneProgram> getOneProgramList(PageInfo pageInfo, Map<String, String> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", ""+ (pageInfo.getStartList() -1));
		return mapper.selectOneProgramList(param);
	}
	
	public int getOneProgramCount(Map<String, String> param) {
		return mapper.selectOneProgramCount(param);
	}
	 
	@Transactional(rollbackFor = Exception.class)
	public OneProgram findByNo(int onepNo, int memNo) {
		Map<String, String> map = new HashMap<>();
		map.put("onepNo", ""+onepNo);
		map.put("memNo", ""+memNo);
		OneProgram onePro = mapper.selectOneProgramByNo(map);
		return onePro;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int clipOneProgram(int memNo, int onepNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("onepNo", "" +onepNo);
		return mapper.clipOneProgram(map);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int unClipOneProgram(int memNo, int onepNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("onepNo", "" +onepNo);
		return mapper.unClipOneProgram(map);
	}
	
//	public int deleteAllOneProgramList1() {
//		return mapper.deleteAllOneProgramList1();
//	}
//	
//	public int deleteAllOneProgramList2() {
//		return mapper.deleteAllOneProgramList2();
//	}
//	
//	public int deleteAllOneProgramList3() {
//		return mapper.deleteAllOneProgramList3();
//	}
	
//	public int deleteOneProgram() {
//		return mapper.deleteOneProgram();
//	}
}
