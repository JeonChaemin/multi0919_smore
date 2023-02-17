package com.multi.smore.model.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.member.model.vo.Member;
import com.multi.smore.model.service.ParkService;
import com.multi.smore.model.vo.Park;
import com.multi.smore.model.vo.ReplyPark;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ParkController {
	
	@Autowired
	private ParkService service;

	@GetMapping("/outdoor")
	public String ParkList(Model model, @RequestParam Map<String, Object> param,
			@RequestParam(required = false) String searchValue, @RequestParam(required = false) String searchType, 
			@RequestParam(required = false) List<String> regions) {
		List<String> parkItem = new ArrayList<String>();
		parkItem.add("올림픽공원");
		parkItem.add("남가좌동어린이공원");
		parkItem.add("분당공원");
		parkItem.add("광교호수공원");
		List<Park> parkList = service.selectParkListHot(parkItem);
		for (Park park : parkList) {
			park.getParkNm(); 
		}
		
		int page = 1;
		try {

			if (param.get("page") != null) {
				page = Integer.parseInt((String) param.get("page"));
			}
			if (searchValue != null && searchValue.length() > 0) {
				param.put("searchValue", searchValue);
				model.addAttribute("searchValue", searchValue);
			}
			
			param.put("searchType", searchType);
			model.addAttribute("searchType", searchType);
			
			param.put("regions", regions);
			if (regions == null) {
				regions = new ArrayList<>();
			}
			model.addAttribute("regions", regions);
		} catch (Exception e) {	}
		
		int count = service.selectParkCount(param);
		PageInfo pageInfo = new PageInfo(page, 5, count, 6);
		List<Park> list = service.selectParkList(pageInfo, param);
		
		model.addAttribute("parkList", parkList);
		model.addAttribute("list", list);
		model.addAttribute("param", param);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageTitle", "smore | Outdoor");
		

		return "category/outdoor";

	}

	@GetMapping("/outdoor/outdoor-detail") // html 의 경로 부분에 작성 
	public String detailView(Model model, @RequestParam("parkNo") int parkNo,  @RequestParam(required = false) String searchType,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember){
		int memNo = 0;
		if(loginMember != null) {
			memNo = loginMember.getMemNo();
		}
				
				
		Park park = service.selectParkByNo(parkNo, memNo);
		if(park == null) {
			return "redirect:error";
		}
		System.out.println(park);
		
		model.addAttribute("searchType", searchType);
		model.addAttribute("park", park);
		model.addAttribute("parkReplys", park.getReplys());
		model.addAttribute("pageTitle", "smore | Outdoor-detail");
		
		return "detail/outdoor-detail";
	}
	
	
	@PostMapping("/outdoor-detail/parkReview")  
	public String writeReply(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@ModelAttribute ReplyPark reply, HttpServletRequest request
			) {
		reply.setMemNo(loginMember.getMemNo());
		log.info("리뷰 작성 요청 Review : " + reply);
		
		int result = service.insertReview(reply);

		if(result > 0) {
			model.addAttribute("msg", "리뷰가 등록되었습니다.");
		}else {
			model.addAttribute("msg", "리뷰 등록에 실패하였습니다.");
		}
		
		model.addAttribute("location", "/outdoor/outdoor-detail?parkNo=" + reply.getParkNo());
		return "common/msg";
	}
	
	@RequestMapping("/outdoor-detail/parkReviewDel")
	public String deleteReply(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int rNo, int parkNo
			){
		log.info("리뷰 삭제 요청");
		int result = service.deleteReview(rNo);
		if(result > 0) {
			model.addAttribute("msg", "리뷰 삭제가 정상적으로 완료되었습니다.");
		}else {
			model.addAttribute("msg", "리뷰 삭제에 실패하였습니다.");
		}
		model.addAttribute("location", "/outdoor/outdoor-detail?parkNo=" + parkNo);
		return "common/msg";
	}
	
	@PostMapping("/outdoor-detail/parkReviewUpd")
	public String updateReply(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@ModelAttribute ReplyPark reply
			){
		log.info("리뷰 수정 요청");
		reply.setParkRno(reply.getParkRno());
//		reply.setContent(reply.getContent());
//		reply.setWriter(reply.getWriter());
//		reply.setModifyDate(reply.getModifyDate());
		
		int result = service.updateReview(reply);
		
		if(result >= 1) {
			model.addAttribute("msg", "리뷰 수정에 성공하습니다.");
		}else {
			model.addAttribute("msg", "리뷰 수정에 실패하였습니다.");
		}
		model.addAttribute("location", "/outdoor/outdoor-detail?parkNo=" + reply.getParkNo());
		return "common/msg";
	}
		
		
	@GetMapping("/error")
	public String error() {
		return "common/error";
	}
	
	@GetMapping("/clip") 
	public ResponseEntity<Integer> parkClip(
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int parkNo, int isClip)
	{
		System.out.println("스크랩 요청 옴");
		
		int result = 0;
		
		if(isClip == 1) {
			result = service.clipPark(loginMember.getMemNo(), parkNo);	
			System.out.println("isClip == 1 인 상태");
		}else {
			result = service.unClipPark(loginMember.getMemNo(), parkNo);	
			System.out.println("isClip == 1 이 아닌 상태");
		}
		
		if(result > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(isClip);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
		
		
}
