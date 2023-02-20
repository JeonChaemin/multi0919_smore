package com.multi.smore.board.model.service;

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

import com.multi.smore.board.model.mapper.BoardMapper;
import com.multi.smore.board.model.vo.Board;
import com.multi.smore.board.model.vo.BoardReply;
import com.multi.smore.common.util.PageInfo;

@Service
public class BoardService {
	@Autowired
	private BoardMapper mapper;
	
	@Transactional(rollbackFor = Exception.class)
	public int saveBoard(Board board) {
		int result = 0;
		if(board.getBbNo() == 0) {
			result = mapper.insertBoard(board);
		}else {
			result = mapper.updateBoard(board);
		}
		return result;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int saveReply(BoardReply reply) {
		return mapper.insertReply(reply);
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
		} catch (Exception e) {
			return null;
		}
		return reNameFileName;
	}
	
	public int getBoardCount(Map<String, String> param) {
		return mapper.selectBoardCount(param);
	}
	
	public List<Board> getBoardList(PageInfo pageInfo, Map<String, String> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() - 1));
		return mapper.selectBoardList(param);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Board findByNo(int bbNo, int memNo) {
		Map<String, String> map = new HashMap<>();
		map.put("bbNo", "" + bbNo);
		map.put("memNo", "" + memNo);
		Board board = mapper.selectBoardByNo(map);
		board.setReadCount(board.getReadCount() + 1);  
		mapper.updateReadCount(board);
		return board; 
	}
	
	public void deleteFile(String filePath) {
		File file = new File(filePath);
		if(file.exists()) {
			file.delete();
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int deleteBoard(int bbNo, String rootPath) {
		Map<String, String> map = new HashMap<>();
		map.put("bbNo", "" + bbNo);
		Board board = mapper.selectBoardByNo(map);
		deleteFile(rootPath + "\\" + board.getRenamedFileName());
		return mapper.deleteBoard(bbNo);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int deleteReply(int bbrno) {
		return mapper.deleteReply(bbrno);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int likeBoard(int memNo, int bbNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("bbNo", ""+bbNo);
		return mapper.likeBoard(map);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int unLikeBoard(int memNo, int bbNo) {
		Map<String, String> map = new HashMap<>();
		map.put("memNo", ""+memNo);
		map.put("bbNo", ""+bbNo);
		return mapper.unLikeBoard(map);
	}
	
	public int likeCount(int bbNo) {
		return mapper.likeCount(bbNo);
	}
	
	public List<Board> getBoardListHome(Map<String, String> param){
		return mapper.selectBoardListHome(param);
	}

	@Transactional(rollbackFor = Exception.class)
	public int updateReply(BoardReply boardReply) {
		return mapper.updateReply(boardReply);
	}

}
