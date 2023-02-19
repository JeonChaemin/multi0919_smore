package com.multi.smore.trade.model.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.recipe.model.vo.RecipeReply;
import com.multi.smore.trade.model.mapper.TradeMapper;
import com.multi.smore.trade.model.vo.ReplyTrade;
import com.multi.smore.trade.model.vo.Trade;

@Service
public class TradeService {


	@Autowired
	private TradeMapper mapper;
	
	@Transactional(rollbackFor = Exception.class)
	public int saveTrade(Trade trade) {
		int result = 0;
		if(trade.getTradeNo() == 0) {
			result = mapper.insertTrade(trade);
		}else {
			result = mapper.updateTrade(trade);
		}
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int saveReplyTrade(ReplyTrade replyTrade) {
		return mapper.insertReplyTrade(replyTrade);
	}
	
	public String saveFile(MultipartFile upFile, String savePath) {
		File folder = new File(savePath);
		
		// 폴더 없으면 만드는 코드
		if(folder.exists() == false) {
			folder.mkdir();
		}
		System.out.println("savePath : " + savePath);
		
		// 파일이름을 랜덤하게 바꾸는 코드, test.txt -> 20221213_1728291212.txt
		String originalFileName = upFile.getOriginalFilename();
		String reNameFileName = 
					LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));
		reNameFileName += originalFileName.substring(originalFileName.lastIndexOf("."));
		String reNamePath = savePath + "/" + reNameFileName;
		
		try {
			// 실제 파일이 저장되는 코드
			upFile.transferTo(new File(reNamePath));
			System.out.println(reNamePath);
		} catch (Exception e) {
			return null;
		}
		return reNameFileName;
	}
	
	public int getTradeCount(Map<String, Object> param) {
		return mapper.selectTradeCount(param);
	}
	
	public List<Trade> getTradeList(PageInfo pageInfo, Map<String, Object> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() - 1));
		return mapper.selectTradeList(param);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Trade findByNo(int tradeNo, int memNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("tradeNo", ""+tradeNo);
		map.put("memNo", ""+memNo);
		Trade trade = mapper.selectTradeByNo(map); 
		trade.setReadCount(trade.getReadCount() + 1);  
		System.out.println(trade);
		mapper.updateReadCount(trade);
		return trade; 
	}
	
	public List<ReplyTrade> getRecipeReplyListByNo(int tradeNo){
		return mapper.selectTradeReplyListByNo(tradeNo);
	}
	
	public void deleteFile(String filePath) {
		File file = new File(filePath);
		if(file.exists()) {
			file.delete();
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int deleteTrade(int tradeNo, String rootPath) {
		Map<String, Object> map = new HashMap<>();
		map.put("tradeNo", ""+tradeNo);
		Trade trade = mapper.selectTradeByNo(map);
		deleteFile(rootPath + "\\" + trade.getRenamedFileName());
		return mapper.deleteTrade(tradeNo);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int deleteReplyTrade(int tradeno) {
		return mapper.deleteReplyTrade(tradeno);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int updateReplyTrade(ReplyTrade replyTrade) {
		return mapper.updateReplyTrade(replyTrade);
	}
	
	// 추천 카테고리 랜덤
	@Transactional(rollbackFor = Exception.class)
	public List<Trade> selectTradeRandomList(){
		return mapper.selectTradeRandomList();
		
	}
	// 스크랩		
	@Transactional(rollbackFor = Exception.class)
	public int clipTrade(int memNo, int tradeNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("tradeNo", ""+tradeNo);
		return mapper.clipTrade(map);
	}
	// 스크랩 풀기
	@Transactional(rollbackFor = Exception.class)
	public int unClipTrade(int memNo, int tradeNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("tradeNo", ""+tradeNo);
		return mapper.unClipTrade(map);
	}
	// 실시간 스크랩 수
	@Transactional(rollbackFor = Exception.class)
	public int clipCount(int tradeNo) {
		return mapper.clipCount(tradeNo);
	}
	
	// 통합검색
	public List<Trade> getTradeListHome(Map<String, String> paramMap){
		return mapper.selectTradeListHome(paramMap);
	}
}
