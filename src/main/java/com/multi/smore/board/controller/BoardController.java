package com.multi.smore.board.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.multi.smore.board.model.service.BoardService;
import com.multi.smore.board.model.vo.Board;
import com.multi.smore.board.model.vo.Reply;
import com.multi.smore.common.util.PageInfo;
import com.multi.smore.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/board") // 요청 url의 상위 url을 모두 처리할때 사용
@Controller
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	final static private String savePath = "c:\\bbs\\";
	
	@GetMapping("/list")
	public String list(Model model, @RequestParam Map<String, String> paramMap) {
		int page = 1;

		// 탐색할 맵을 선언
		Map<String, String> searchMap = new HashMap<String, String>();
		try {
			String searchValue = paramMap.get("searchValue");
			if(searchValue != null && searchValue.length() > 0) {
				String searchType = paramMap.get("searchType");
				searchMap.put(searchType, searchValue);
			}else {
				paramMap.put("searchType", "all");
			}
			page = Integer.parseInt(paramMap.get("page"));
		} catch (Exception e) {}
		
		int boardCount = service.getBoardCount(searchMap);
		PageInfo pageInfo = new PageInfo(page, 10, boardCount, 10);
		List<Board> list = service.getBoardList(pageInfo, searchMap);
		
		model.addAttribute("list", list);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("pageInfo", pageInfo);
		return "board/list";
	}
	
//	@RequestMapping("/board/view")
	@RequestMapping("/view")
	public String view(Model model, @RequestParam("no") int no) {
		Board board = service.findByNo(no);
		if(board == null) {
			return "redirect:error";
		}
		
		model.addAttribute("board", board);
		model.addAttribute("replyList", board.getReplyList());
		return "board/view";
	}
	
	
	@GetMapping("/error")
	public String error() {
		return "common/error";
	}
	
	@GetMapping("/write")
	public String writeView() {
		return "board/write";
	}
	
	@PostMapping("/write")
	public String writeBoard(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@ModelAttribute Board board,
			@RequestParam("upfile") MultipartFile upfile
			) {
		log.info("게시글 작성 요청");
		
		board.setMNo(loginMember.getMNo());
		
		// 파일 저장 로직
		if(upfile != null && upfile.isEmpty() == false) {
			String renameFileName = service.saveFile(upfile, savePath); 
			
			if(renameFileName != null) {
				board.setOriginalFileName(upfile.getOriginalFilename());
				board.setRenamedFileName(renameFileName);
			}
		}
		
		log.debug("board : " + board);
		int result = service.saveBoard(board);

		if(result > 0) {
			model.addAttribute("msg", "게시글이 등록 되었습니다.");
			model.addAttribute("location", "/board/list");
		}else {
			model.addAttribute("msg", "게시글 작성에 실패하였습니다.");
			model.addAttribute("location", "/board/list");
		}
		
		return "common/msg";
	}
	
	
	@RequestMapping("/reply")
	public String writeReply(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@ModelAttribute Reply reply
			) {
		reply.setMNo(loginMember.getMNo());
		log.info("리플 작성 요청 Reply : " + reply);
		
		int result = service.saveReply(reply);
		
		if(result > 0) {
			model.addAttribute("msg", "리플이 등록되었습니다.");
		}else {
			model.addAttribute("msg", "리플 등록에 실패하였습니다.");
		}
		model.addAttribute("location", "/board/view?no="+reply.getBNo());
		return "common/msg";
	}
	
	@RequestMapping("/delete")
	public String deleteBoard(Model model,  HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int boardNo
			) {
		log.info("게시글 삭제 요청 boardNo : " + boardNo);
		int result = service.deleteBoard(boardNo, savePath);
		
		if(result > 0) {
			model.addAttribute("msg", "게시글 삭제가 정상적으로 완료되었습니다.");
		}else {
			model.addAttribute("msg", "게시글 삭제에 실패하였습니다.");
		}
		model.addAttribute("location", "/board/list");
		return "common/msg";
	}
	
	@RequestMapping("/replyDel")
	public String deleteReply(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int replyNo, int boardNo
			){
		log.info("리플 삭제 요청");
		int result = service.deleteReply(replyNo);
		
		if(result > 0) {
			model.addAttribute("msg", "리플 삭제가 정상적으로 완료되었습니다.");
		}else {
			model.addAttribute("msg", "리플 삭제에 실패하였습니다.");
		}
		model.addAttribute("location", "/board/view?no=" + boardNo);
		return "/common/msg";
	}
	
	// http://localhost/mvc/board/update?no=27
	@GetMapping("/update")
	public String updateView(Model model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam("no") int no
			) {
		Board board = service.findByNo(no);
		model.addAttribute("board",board);
		return "board/update";
	}
	

	@PostMapping("/update")
	public String updateBoard(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@ModelAttribute Board board,
			@RequestParam("reloadFile") MultipartFile reloadFile
			) {
		log.info("게시글 수정 요청");
		
		board.setMNo(loginMember.getMNo());
		
		// 파일 저장 로직
		if(reloadFile != null && reloadFile.isEmpty() == false) {
			// 기존 파일이 있는 경우 삭제
			if(board.getRenamedFileName() != null) {
				service.deleteFile(savePath + "/" +board.getRenamedFileName());
			}
			
			String renameFileName = service.saveFile(reloadFile, savePath); // 실제 파일 저장하는 로직
			
			if(renameFileName != null) {
				board.setOriginalFileName(reloadFile.getOriginalFilename());
				board.setRenamedFileName(renameFileName);
			}
		}
		
		log.debug("board : " + board);
		int result = service.saveBoard(board);

		if(result > 0) {
			model.addAttribute("msg", "게시글이 수정 되었습니다.");
			model.addAttribute("location", "/board/list");
		}else {
			model.addAttribute("msg", "게시글 수정에 실패하였습니다.");
			model.addAttribute("location", "/board/list");
		}
		
		return "common/msg";
	}
	
	@GetMapping("/file/{fileName}")
	@ResponseBody
	public Resource downloadImage(@PathVariable("fileName") String fileName, Model model) throws IOException {
		return new UrlResource("file:" + savePath + fileName);
	}
	
	@RequestMapping("/fileDown")
	public ResponseEntity<Resource> fileDown(@RequestParam("oriname") String oriname,
			@RequestParam("rename") String rename, @RequestHeader(name = "user-agent") String userAgent) {
		try {
			Resource resource = new UrlResource("file:" + savePath + rename + "");
			String downName = null;

			// 인터넷 익스플로러 인 경우
			boolean isMSIE = userAgent.indexOf("MSIE") != -1 || userAgent.indexOf("Trident") != -1;

			if (isMSIE) { // 익스플로러 처리하는 방법
				downName = URLEncoder.encode(oriname, "UTF-8").replaceAll("\\+", "%20");
			} else {
				downName = new String(oriname.getBytes("UTF-8"), "ISO-8859-1"); // 크롬
			}
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + downName + "\"")
					.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString()).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 실패했을 경우
	}


}

