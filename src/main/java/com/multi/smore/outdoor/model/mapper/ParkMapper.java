package com.multi.smore.outdoor.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.outdoor.model.vo.Park;
import com.multi.smore.outdoor.model.vo.ParkClip;
import com.multi.smore.outdoor.model.vo.ParkReply;

@Mapper
public interface ParkMapper {
	List<Park> selectParkList(Map<String, Object> map); // 게시글 리스트 출력
	int selectParkCount(Map<String, Object> map);	// 검색 결과
	Park selectParkByNo(Map<String, Object> map);	// 게시글 상세보기
	List<Park> selectParkListHot(List<String> parkItem);	// 인기 게시글 
	List<ParkReply> selectParkReplyListByNo(int parkNo);	// 리플 리스트
	int selectParkReplyCountByNo(int parkNo);
	List<ParkClip> selectParkClipList(Map<String, String> map);	//스크랩 리스트 
	ParkClip selectParkClipByNo(Map<String, String> map);	// 스크랩 상세보기?
	
	int insertPark(Park park);
	
	int insertParkReply(ParkReply parkReply);
	int deleteParkReply(int parkRno);
	int updateParkReply(ParkReply parkReply);
	
//	메서드 수정
	int insertParkClip(Map<String, String> map);	// 스크랩
	int deleteParkClip(Map<String, String> map);	// 스크랩 해제

}
