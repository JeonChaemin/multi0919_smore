package com.multi.smore.trade.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.trade.model.vo.Trade;
import com.multi.smore.recipe.model.vo.RecipeClip;
import com.multi.smore.recipe.model.vo.RecipeReply;
import com.multi.smore.trade.model.vo.ReplyTrade;

@Mapper
public interface TradeMapper { //interface 
	List<Trade> selectTradeList(Map<String, Object> map);
	int selectTradeCount(Map<String, Object> map);
	Trade selectTradeByNo(Map<String, Object> map);
	List<ReplyTrade> selectTradeReplyListByNo(int trade); // 리플 번호
	List<Trade> selectTradeRandomList(); // 추천 카테고리 - 랜덤
	int insertTrade(Trade trade);
	int insertReplyTrade(ReplyTrade replyTrade);
	int updateTrade(Trade trade);
	int updateReadCount(Trade trade);
	int updateReplyTrade(ReplyTrade replyTrade);
	int deleteTrade(int no);
	int deleteReplyTrade(int no);
	// 스크랩
	int clipTrade(Map<String, String> map);
	int unClipTrade(Map<String, String> map);
	int clipCount(int tradeNo);
	//home 통합검색용
	List<Trade> selectTradeListHome(Map<String, String> paramMap);
	
}
