package com.multi.smore.outdoor.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.member.model.vo.Member;
import com.multi.smore.outdoor.model.service.TrackingService;
import com.multi.smore.outdoor.model.vo.Tracking;
import com.multi.smore.outdoor.model.vo.TrackingReply;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TrackingController {

	@Autowired
	private TrackingService service;
	
	
	@GetMapping("/tracking")
	public String  TrackingList(@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			Model model, @RequestParam Map<String, Object> param,
			@RequestParam(required = false) String searchValue) {
		
		List<String> trackItem = new ArrayList<String>();
		
		int memNo = 0;
		if(loginMember != null) {
			memNo = loginMember.getMemNo();
			param.put("memNo",""+ memNo);
		}
		
		trackItem.add("507");
		trackItem.add("333");
		trackItem.add("1300");
		trackItem.add("117");
		List<Tracking> trackingList = service.selectTrackingListHot(trackItem);
		for(Tracking tracking : trackingList) {
			tracking.getTNo();
		}		
		
		int page =1;
		try {

			if (param.get("page") != null) {
				page = Integer.parseInt((String) param.get("page"));
			}
			if (searchValue != null && searchValue.length() > 0) {
				param.put("searchValue", searchValue);
				model.addAttribute("searchValue", searchValue);
			}
		} catch (Exception e) {	}
		
		int count = service.selectTrackCount(param);
		PageInfo pageInfo = new PageInfo(page, 5, count, 6);
		List<Tracking> list = service.selectTrackingList(pageInfo, param);
		
		model.addAttribute("trackingList", trackingList);
		model.addAttribute("list", list);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("param", param);
		model.addAttribute("pageTitle", "smore | Outdoor");
		
		return "category/tracking";
	}
	
	

	@GetMapping("/tracking/track-detail") // html 의 경로 부분에 작성 
	public String detailView(Model model, @RequestParam("tNo") int tNo,  
			@SessionAttribute(name = "loginMember", required = false) Member loginMember){
		String memNo = "";
		
		if(loginMember != null) {
			memNo =""+ loginMember.getMemNo();
		}	
				
		Tracking tracking = service.selectTrackingByNo(tNo, memNo);
		List<TrackingReply> replyList = service.selectTrackingReplyListByNo(tNo);
		
		int img = (int) (Math.random() * 9);
		
		if(tracking == null) {
			return "redirect:error";
		}
		// 검사용
		System.out.println(tracking);
		
		model.addAttribute("tracking", tracking);
		model.addAttribute("replyList", replyList);
		model.addAttribute("img", img);
		model.addAttribute("pageTitle", "smore | Outdoor-detail");
		
		return "detail/track-detail";
	}
	
	
	@RequestMapping("/tracking/track-detail/trackReview")  
	public String writeReply(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@ModelAttribute TrackingReply reply, HttpServletRequest request
			) {
		reply.setMemNo(loginMember.getMemNo());
		reply.setWriter(loginMember.getId());
		reply.setWriterName(loginMember.getName());
		log.info("리뷰 작성 요청 Review : " + reply);
		
		int result = service.insertTrackingReply(reply);
		
		
		if(result > 0) {
			model.addAttribute("msg", "리뷰가 등록되었습니다.");
		}else {
			model.addAttribute("msg", "리뷰 등록에 실패하였습니다.");
		}

		model.addAttribute("location", "/tracking/track-detail?tNo=" + reply.getTNo());
		return "common/msg";
	}
	
	@RequestMapping("/track-detail/delReply")
	public String deleteReply(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int tRno, int tNo
			){
		log.info("리뷰 삭제 요청 tNo : " + tNo);
		int result = service.deleteTrackingReply(tRno);
		if(result > 0) {
			model.addAttribute("msg", "리뷰 삭제가 정상적으로 완료되었습니다.");
		}else {
			model.addAttribute("msg", "리뷰 삭제에 실패하였습니다.");
		}
		
		model.addAttribute("location", "/tracking/track-detail?tNo=" + tNo);
		return "/common/msg";
	}
	
	@RequestMapping("/track-detail/updateReply")
	public String updateReply(Model model, HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			TrackingReply reply, int tNo
			){
		log.info("리뷰 수정 요청" );
		
		int result = service.updateTrackingReply(reply);
		
		if(result > 0) {
			model.addAttribute("msg", "리뷰 수정에 성공하였습니다.");
		}else {
			model.addAttribute("msg", "리뷰 수정에 실패하였습니다.");
		}
		model.addAttribute("location", "/tracking/track-detail?tNo=" + tNo );

		return "/common/msg";
	}
		
	@GetMapping("/tracking/error")
	public String error() {
		return "/common/error";
	}	

	
	@RequestMapping("/tracking/clip") 
	public ResponseEntity<Integer> trackingClip(
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int tNo, int isClip)
	{
		System.out.println("스크랩 요청 옴");
		System.out.println(loginMember);
		
		int result = 0;
		
		if(isClip == 1) {
			result = service.insertTrackingClip(loginMember.getMemNo(), tNo);	
			System.out.println("isClip == 1 인 상태");
		}else {
			result = service.deleteTrackingClip(loginMember.getMemNo(), tNo);	
			System.out.println("isClip == 1 이 아닌 상태");
		}
		System.out.println(result);
		
		if(result > 0) {
			return ResponseEntity.status(HttpStatus.OK).body(isClip);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
