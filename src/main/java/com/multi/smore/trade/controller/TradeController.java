package com.multi.smore.trade.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.member.model.vo.Member;
import com.multi.smore.trade.model.service.TradeService;
import com.multi.smore.trade.model.vo.ReplyTrade;
import com.multi.smore.trade.model.vo.Trade;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RequestMapping("/trade") // 요청 url의 상위 url을 모두 처리할때 사용
@Controller
public class TradeController {

	@Autowired
	private TradeService service;

	final static private String savePath = "c:\\smore\\";

//	@RequestMapping("/list")
//	public String tradeList() {
//		return "trade/trade-list";
//	}

	// http://localhost/trade/list?searchValue=&searchType=%EC%A4%91%EA%B3%A0%ED%8C%90%EB%A7%A4
	@GetMapping("/trade")
	public String tradeList(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam Map<String, Object> paramMap,
			@RequestParam(required = false) String searchValue, 
			@RequestParam(required = false) String searchType,
			@RequestParam(required = false) List<String> categories,
			@RequestParam(required = false) List<String> regions) {

		int page = 1;

//		System.out.println("page : " + page);
//		System.out.println("가져온값 : " + paramMap);
//		System.out.println("searchValue : " + searchValue);
//		System.out.println("searchType : " + searchType);
//		System.out.println("categories : " + categories);
//		System.out.println("regions : " + regions);

		try {
			//로그인한 아이디
			if(loginMember != null) {
				paramMap.put("memNo", ""+loginMember.getMemNo());
			}
			
			// 페이지
			if (paramMap.get("page") != null) {
				page = Integer.parseInt((String) paramMap.get("page"));
			}
			// 검색
			if (searchValue != null && searchValue.length() > 0) {
				model.addAttribute("searchValue", searchValue);
			}

			// 검색 - 타입
			paramMap.put("searchType", searchType);
			model.addAttribute("searchType", searchType);

			// 검색 - 카테고리
			paramMap.put("categories", categories);
			if (categories == null) {
				categories = new ArrayList<>();
			}
			model.addAttribute("categories", categories);

			// 검색 - 도시
			paramMap.put("regions", regions);
			if (regions == null) {
				regions = new ArrayList<>();
			}
			model.addAttribute("regions", regions);

		} catch (Exception e) {
			System.out.println(e);
		}

		System.out.println("paramMap 후확인 : " + paramMap);

		int TradeCount = service.getTradeCount(paramMap);
		PageInfo pageInfo = new PageInfo(page, 5, TradeCount, 5);
		List<Trade> list = service.getTradeList(pageInfo, paramMap);

		model.addAttribute("list", list);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageTitle", "smore | Trade");

		// 추천 카테고리
		List<Trade> ctList = service.selectTradeRandomList();
		model.addAttribute("ctList", ctList);

		return "trade/trade-list";
	}

	@RequestMapping("/trade/detail")
	public String view(Model model, 
			@RequestParam("no") int tradeNo, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember) {
		
		int memNo = 0;
		if(loginMember != null) {
			memNo = loginMember.getMemNo();
		}
		
		Trade trade = service.findByNo(tradeNo, memNo);
		List<ReplyTrade> replyTradeList = service.getRecipeReplyListByNo(tradeNo);
		if (trade == null) {
			return "redirect:error";
		}

		model.addAttribute("trade", trade);
		model.addAttribute("replyTradeList", replyTradeList);
		model.addAttribute("pageTitle", "smore | Trade");
		return "trade/trade-detail";
	}

	@GetMapping("/trade/error")
	public String error() {
		return "common/error";
	}

	@GetMapping("/trade/write")
	public String writeView() {
		return "trade/trade-write";
	}

	@PostMapping("/trade/write")
	public String writeTrade(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember, 
			@ModelAttribute Trade trade,
			@RequestParam("upfile") MultipartFile upfile) {
		log.info("거래게시글 작성 요청");

		// 원래 코드
		trade.setWriterNo(loginMember.getMemNo());
		// test용
//		trade.setWriterNo(1);

		// 파일 저장 로직
		if (upfile != null && upfile.isEmpty() == false) {
			String renameFileName = service.saveFile(upfile, savePath);

			if (renameFileName != null) {
				trade.setOriginalFileName(upfile.getOriginalFilename());
				trade.setRenamedFileName(renameFileName);
			}
		}

		log.debug("trade : " + trade);
		int result = service.saveTrade(trade);

		if (result > 0) {
			model.addAttribute("msg", "거래게시글이 등록 되었습니다.");
			model.addAttribute("location", "/trade");
		} else {
			model.addAttribute("msg", "거래게시글 작성에 실패하였습니다.");
			model.addAttribute("location", "/trade");
		}

		return "common/msg";
	}

	@RequestMapping("/trade/reply")
	public String writeReplyTrade(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@ModelAttribute ReplyTrade replyTrade) {
		replyTrade.setWriterNo(loginMember.getMemNo());
		log.info("리플 작성 요청 Reply : " + replyTrade);

		int result = service.saveReplyTrade(replyTrade);

		if (result > 0) {
			model.addAttribute("msg", "댓글이 등록되었습니다.");
		} else {
			model.addAttribute("msg", "댓글 등록에 실패하였습니다.");
		}
		model.addAttribute("location", "/trade/detail?no=" + replyTrade.getTradeNo());
		return "common/msg";
	}

	@RequestMapping("/trade/delete")
	public String deleteTrade(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember, 
			int tradeNo) {
		
		log.info("게시글 삭제 요청 tradeNo : " + tradeNo);
		int result = service.deleteTrade(tradeNo, savePath);

		if (result > 0) {
			model.addAttribute("msg", "게시글 삭제가 정상적으로 완료되었습니다.");
		} else {
			model.addAttribute("msg", "게시글 삭제에 실패하였습니다.");
		}
		model.addAttribute("location", "/trade");
		return "common/msg";
	}

	@RequestMapping("/trade/replyDel")
	public String deleteReplyTrade(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int replyNo, int tradeNo) {
		log.info("리플 삭제 요청");
		int result = service.deleteReplyTrade(replyNo);

		if (result > 0) {
			model.addAttribute("msg", "댓글 삭제가 정상적으로 완료되었습니다.");
		} else {
			model.addAttribute("msg", "댓글 삭제에 실패하였습니다.");
		}
		model.addAttribute("location", "/trade/detail?no=" + tradeNo);
		return "/common/msg";
	}

	@RequestMapping("/trade/replyUpdate")
	public String updateReplyTrade(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember, ReplyTrade replyTrade, int tradeNo) {
		
		log.info("리플 수정 요청");
		int result = service.updateReplyTrade(replyTrade);

		if (result > 0) {
			model.addAttribute("msg", "댓글이 수정되었습니다.");
		} else {
			model.addAttribute("msg", "댓글 수정에 실패하였습니다.");
		}
		model.addAttribute("location", "/trade/detail?no=" + tradeNo);
		return "/common/msg";
	}
	
	// http://localhost/mvc/board/update?no=27
	@GetMapping("/trade/update")
	public String updateView(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam("no") int tradeNo) {
		Trade trade = service.findByNo(tradeNo, 0);
		System.out.println(trade);
		model.addAttribute("trade", trade);
		return "trade/trade-update";
	}

	@PostMapping("/trade/update")
	public String updateTrade(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember, 
			@ModelAttribute Trade trade,
			@RequestParam("reloadFile") MultipartFile reloadFile) {
		log.info("게시글 수정 요청");

		trade.setWriterNo(loginMember.getMemNo());

		// 파일 저장 로직
		if (reloadFile != null && reloadFile.isEmpty() == false) {
			// 기존 파일이 있는 경우 삭제
			if (trade.getRenamedFileName() != null) {
				service.deleteFile(savePath + "/" + trade.getRenamedFileName());
			}

			String renameFileName = service.saveFile(reloadFile, savePath); // 실제 파일 저장하는 로직

			if (renameFileName != null) {
				trade.setOriginalFileName(reloadFile.getOriginalFilename());
				trade.setRenamedFileName(renameFileName);
			}
		}

		log.debug("trade : " + trade);
		int result = service.saveTrade(trade);

		if (result > 0) {
			model.addAttribute("msg", "게시글이 수정 되었습니다.");
			model.addAttribute("location", "/trade/detail?no=" + trade.getTradeNo());
		} else {
			model.addAttribute("msg", "게시글 수정에 실패하였습니다.");
			model.addAttribute("location", "/trade/detail?no=" + trade.getTradeNo());
		}

		return "common/msg";
	}

	@GetMapping("/trade/file/{fileName}")
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

	@GetMapping("/trade/clip") 
	public ResponseEntity<int[]> tradeClip(
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int tradeNo, int isClip)
	{
		System.out.println("스크랩 요청 옴");
		
		int result = 0;
		
		if(isClip == 1) {
			result = service.clipTrade(loginMember.getMemNo(), tradeNo);	
			System.out.println("isClip == 1 인 상태");
		}else {
			result = service.unClipTrade(loginMember.getMemNo(), tradeNo);	
			System.out.println("isClip == 1 이 아닌 상태");
		}
		
		if(result > 0) {
			int clipCount = service.clipCount(tradeNo);
			int[] pArr = {isClip, clipCount};
			return ResponseEntity.status(HttpStatus.OK).body(pArr);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
}
