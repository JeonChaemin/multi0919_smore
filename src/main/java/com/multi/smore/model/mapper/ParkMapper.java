package com.multi.smore.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.model.vo.Park;
import com.multi.smore.model.vo.ReplyPark;

@Mapper
public interface ParkMapper {
	List<Park> selectParkList(Map<String, Object> map); // 게시글 리스트 출력
	int selectParkCount(Map<String, Object> map);	// 검색 
	Park selectParkByNo(Map<String, Object> map);	// 게시글 상세보기
	List<Park> selectParkListHot(List<String> parkItem);	// 인기 게시글 
	
	int insertPark(Park park);
	int insertReplyPark(ReplyPark replyPark);
	int deleteReplyPark(int parkRno);
	int updateReplyPark(ReplyPark replypark);
	
	int clipPark(Map<String, String> map);
	int unClipPark(Map<String, String> map);
	
}
