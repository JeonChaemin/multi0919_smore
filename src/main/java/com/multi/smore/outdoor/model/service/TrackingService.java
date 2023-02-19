package com.multi.smore.outdoor.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.outdoor.model.mapper.TrackingMapper;
import com.multi.smore.outdoor.model.vo.TrackingReply;
import com.multi.smore.outdoor.model.vo.Tracking;
import com.multi.smore.outdoor.model.vo.TrackingClip;

@Service
public class TrackingService {

	@Autowired
	private TrackingMapper mapper;
	
	
	public List<Tracking> selectTrackingList(PageInfo pageInfo, Map<String, Object> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() -1) );
		return mapper.selectTrackingList(param);
	}
	
	public int selectTrackCount(Map<String, Object> param) {
		return mapper.selectTrackingCount(param);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Tracking selectTrackingByNo(int tNo, String memNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("tNo", ""+tNo);		
		map.put("memNo", memNo);
		Tracking tracking = mapper.selectTrackingByNo(map); 
		
		
		return tracking; 
	}
	
	public List<Tracking> selectTrackingListHot(List<String> trackItem) {
		return mapper.selectTrackingListHot(trackItem);
	}
	
	public List<TrackingReply> selectTrackingReplyListByNo(int tNo) {
		return mapper.selectTrackingReplyListByNo(tNo);
	}
	
	public List<TrackingClip> selectTrackingClipList(PageInfo pageInfo, Map<String, String> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() - 1));
		return mapper.selectTrackingClipList(param);
	}
	
	public TrackingClip selectTrackingClipByNo(Map<String, String> param) {
		return mapper.selectTrackingClipByNo(param);
	}
	
	public int selectTrackingReplyCountByNo(int tRno) {
		return mapper.selectTrackingReplyCountByNo(tRno);
	}
	public List<Tracking> selectTrackingListRamd(List<String> ramdItem){
		return mapper.selectTrackingListRamd(ramdItem);
	}
	
	
	
	@Transactional(rollbackFor = Exception.class)
	public int insertTracking(Tracking tracking) {
		int result = 0;
		result = mapper.insertTracking(tracking);
		return result;
	}
	
	public int insertTrackingReply(TrackingReply trackingReply) {
		int result=0;
		result=mapper.insertTrackingReply(trackingReply);
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int deleteTrackingReply(int tRno) {
		return mapper.deleteTrackingReply(tRno);
		
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int updateTrackingReply(TrackingReply trackingReply) {
		return mapper.updateTrackingReply(trackingReply);
	}
	

	
	// 스크랩		
		@Transactional(rollbackFor = Exception.class)
		public int insertTrackingClip(int memNo, int tNo) {
			Map<String, String> map = new HashMap<>();
			map.put("memNo", ""+memNo);
			map.put("tNo", ""+tNo);
			return mapper.insertTrackingClip(map);
		}
		// 스크랩 풀기
		@Transactional(rollbackFor = Exception.class)
		public int deleteTrackingClip(int memNo, int tNo) {
			Map<String, String> map = new HashMap<>();
			map.put("memNo", ""+memNo);
			map.put("tNo", ""+tNo);
			return mapper.deleteTrackingClip(map);
		}
}
