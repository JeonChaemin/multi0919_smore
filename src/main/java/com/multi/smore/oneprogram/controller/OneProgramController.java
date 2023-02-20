package com.multi.smore.oneprogram.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.member.model.vo.Member;
import com.multi.smore.oneprogram.model.service.OneProgramService;
import com.multi.smore.oneprogram.model.vo.OneProgram;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@RequestMapping("/program")
@Controller
public class OneProgramController {
	@Autowired
	private OneProgramService service;
	
//	final static private String savePath = "c:\\smore\\";
	
	@RequestMapping("/program")
	public String oneProList(@SessionAttribute(name = "loginMember", required = false) Member loginMember,
								Model model, @RequestParam Map<String, String> paramMap,
										  @RequestParam(required = false) String searchValue,
										  @RequestParam(required = false) String tyNm) {
		
//		initDB();
		
		Map<String, String> searchMap = new HashMap<String, String>();
		int memNo = 0;
		if(loginMember != null) {
			memNo = loginMember.getMemNo();
			searchMap.put("memNo",""+ memNo);
		}
		
		int page = 1;
		
		System.out.println("paramMap : " + paramMap);
		
		
		try {
			if(searchValue != null && searchValue.length() > 0) {
				searchMap.put("searchValue", searchValue);
				model.addAttribute("searchValue", searchValue);
			}
			
			if(tyNm == null || tyNm.equals("전체")) {
				tyNm = "전체";
			}else{
				searchMap.put("tyNm", tyNm);
			}
			model.addAttribute("tyNm", tyNm);
			
			if(paramMap.get("page") != null) {
				page = Integer.parseInt((String) paramMap.get("page"));
			}
		} catch (Exception e) {}
		
//		 추천 프로그램
		OneProgram item1 = service.findByNo(372, memNo);
		OneProgram item2 = service.findByNo(251, memNo);
		OneProgram item3 = service.findByNo(496, memNo);
		OneProgram item4 = service.findByNo(526, memNo);
		List<OneProgram> list = new ArrayList<>();
		list.add(item1);
		list.add(item2);
		list.add(item3);
		list.add(item4);
		model.addAttribute("list", list);
		
		System.out.println("searchValue : "+searchValue);
		System.out.println("tyNm : "+tyNm);
		System.out.println("searchMap : "+searchMap);
		
		int oneProCount = service.getOneProgramCount(searchMap);
		PageInfo pageInfo = new PageInfo(page, 4, oneProCount, 6);
		List<OneProgram> list2 = service.getOneProgramList(pageInfo, searchMap);
		
		model.addAttribute("list2", list2);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageTitle", "smore | Program");
		return "category/program";
	}
	
	@GetMapping("/program-detail")
	public String programdetail(@SessionAttribute(name = "loginMember", required = false) Member loginMember,
								@RequestParam Map<String, String> paramMap,
								Model model, int onepNo
								) {

		int memNo = 0;
		if(loginMember != null) {
			memNo = loginMember.getMemNo();
			paramMap.put("memNo",""+ memNo);
		}
		
		OneProgram oneProgram = service.findByNo(onepNo, memNo);
		System.out.println(oneProgram);
		
		if(oneProgram == null) {
			return "redirect:error";
		}
		System.out.println("no " + oneProgram.getOnepNo());
		System.out.println("getIsClip " + oneProgram.getIsClip() + "\n");
		System.out.println(paramMap);

		
		if(oneProgram.getPartcptnNmpr().equals("")) {
			oneProgram.setPartcptnNmpr("0");
		}
		
		if(oneProgram.getRceptMthLink().equals("")) {
			oneProgram.setRceptMthLink("https://1in.seoul.go.kr/front/partcptn/partcptnListPage.do");
		}else if(oneProgram.getRceptMthLink()!=null && oneProgram.getRceptMthLink().toLowerCase().contains("http") == false) {
			oneProgram.setRceptMthLink("http://" + oneProgram.getRceptMthLink());
		}
		
		if(oneProgram.getCn().equals(" ") || oneProgram.getCn().equals("")) {
			oneProgram.setCn("기관에 문의");
		}
			
		model.addAttribute("oneProgram", oneProgram);
		model.addAttribute("paramMap",paramMap);
		model.addAttribute("pageTitle", "smore | 정부지원사업");
		return "detail/program-detail";
	}
	 
	//program/clip?onepNo=251&isClip=1
		@GetMapping("/program/clip") 
		public ResponseEntity<Integer> oneProgramClip(
				@SessionAttribute(name = "loginMember", required = false) Member loginMember,
				int onepNo, int isClip)
		{
			System.out.println("스크랩 요청 옴");
			System.out.println(loginMember);
			
			int result = 0;
			
			if(isClip == 1) {
				result = service.clipOneProgram(loginMember.getMemNo(), onepNo);	
				System.out.println("isClip == 1 인 상태");
			}else {
				result = service.unClipOneProgram(loginMember.getMemNo(), onepNo);	
				System.out.println("isClip == 1 이 아닌 상태");
			}
			
			if(result > 0) {
				return ResponseEntity.status(HttpStatus.OK).body(isClip);
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		}
	
		
		@GetMapping("/program-detail/clip") 
		public ResponseEntity<Integer> oneProgramDetailClip(
				@SessionAttribute(name = "loginMember", required = false) Member loginMember,
				int onepNo, int isClip)
		{
			System.out.println("스크랩 요청 옴");
			System.out.println(loginMember);
			
			int result = 0;
			
			if(isClip == 1) {
				result = service.clipOneProgram(loginMember.getMemNo(), onepNo);	
				System.out.println("isClip == 1 인 상태");
			}else {
				result = service.unClipOneProgram(loginMember.getMemNo(), onepNo);	
				System.out.println("isClip == 1 이 아닌 상태");
			}
			
			if(result > 0) {
				return ResponseEntity.status(HttpStatus.OK).body(isClip);
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		}
		
		
//	public void initDB() {
//		List<OneProgram> list = new ArrayList<>();
//		service.deleteOneProgram();
//		list.addAll(OneProgramAPI.OneProgramXML());
//		for(OneProgram onepro : list) {
//			service.insertOneProgram(onepro);
//		}
//	}
}

















