package com.multi.smore.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.board.model.vo.Board;
import com.multi.smore.board.model.vo.BoardReply;

@Mapper
public interface BoardMapper {
	List<Board> selectBoardList(Map<String, String> map);
	int selectBoardCount(Map<String, String> map);
	Board selectBoardByNo(Map<String, String> map);
	int insertBoard(Board board);
	int insertReply(BoardReply reply);
	int updateBoard(Board board);
	int updateReadCount(Board board);
	int deleteBoard(int no);
	int deleteReply(int no);
	int likeBoard(Map<String, String> map);
	int unLikeBoard(Map<String, String> map);
	int likeCount(int no);
	List<Board> selectBoardListHome(Map<String, String> param);
	int updateReply(BoardReply boardReply);
}
