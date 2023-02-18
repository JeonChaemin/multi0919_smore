package com.multi.smore.outdoor.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.outdoor.model.vo.TrackingReply;
import com.multi.smore.outdoor.model.vo.Tracking;
import com.multi.smore.outdoor.model.vo.TrackingClip;

@Mapper
public interface TrackingMapper {
	List<Tracking> selectTrackingList(Map<String, Object> map); // 게시글 리스트 출력
	int selectTrackingCount(Map<String, Object> map);
	Tracking selectTrackingByNo(Map<String, Object> map);	// 게시글 상세보기
	List<Tracking> selectTrackingListHot(List<String> trackItem);	// 인기 게시글 
	List<TrackingReply> selectTrackingReplyListByNo(int tNo);	// 리플 리스트
	int selectTrackingReplyCountByNo(int tNo);
	List<TrackingClip> selectTrackingClipList(Map<String, String> map);	//스크랩 리스트 
	TrackingClip selectTrackingClipByNo(Map<String, String> map);
	List<Tracking> selectTrackingListRamd(List<String> ramdItem); // 랜덤 쿼리 
	
	
	int insertTracking(Tracking tracking);
	
	int insertTrackingReply(TrackingReply trackingReply);
	int deleteTrackingReply(int trackingReply);
	int updateTrackingReply(TrackingReply trackingReply);
	
	int insertTrackingClip(Map<String, String> map);
	int deleteTrackingClip(Map<String, String> map);

}
