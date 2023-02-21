package com.multi.smore.rental.controller;

import java.util.ArrayList;
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
import com.multi.smore.rental.model.service.RentalService;
import com.multi.smore.rental.model.vo.Rental;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RentalConrtoller {

	@Autowired
	private RentalService service;

//	final static private String savePath = "c:\\smore\\";
//	private void initDB() {
//		List<Rental> rlist = new ArrayList<>();
//		service.deleteAllRentalList();
//		rlist.addAll(RentalHouseAPI.getRentalList();
//		for (Rental news : rlist) {
//			service.insertRental(rental);
//		}
//	}

//	@RequestMapping("/rental")
//	public String rentalList() {
//		return "category/rental";
//	}

	@GetMapping("/rental")
	public String rentalList(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam Map<String, Object> paramMap, 
			@RequestParam(required = false) String searchValue,
			@RequestParam(required = false) String searchRegion,
			@RequestParam(required = false) String searchHouseType,
			@RequestParam(required = false) List<String> searchSuplyType) {

		int page = 1;

		System.out.println("searchValue : " + searchValue);
		System.out.println("searchRegion : " + searchRegion);
		System.out.println("searchHouseType : " + searchHouseType);
		System.out.println("searchSuplyType : " + searchSuplyType);

		try {
			// 로그인한 아이디
			if (loginMember != null) {
				paramMap.put("memNo", "" + loginMember.getMemNo());
			}

			// 페이지
			if (paramMap.get("page") != null) {
				page = Integer.parseInt((String) paramMap.get("page"));
			}
			// 검색
			if (searchValue != null && searchValue.length() > 0) {
				paramMap.put("searchValue", searchValue);
				model.addAttribute("searchValue", searchValue);
			}

			// 지역
				paramMap.put("searchRegion", searchRegion);
				model.addAttribute("searchRegion", searchRegion);

			// 임대주택타입
				paramMap.put("searchHouseType", searchHouseType);
				model.addAttribute("searchHouseType", searchHouseType);

			
			// 공급 유형
			paramMap.put("searchSuplyType", searchSuplyType);
			if (searchSuplyType == null) {
				searchSuplyType = new ArrayList<>();
			}
			model.addAttribute("searchSuplyType", searchSuplyType);

		} catch (Exception e) {
			System.out.println(e);
		}

		System.out.println("paramMap 후확인 : " + paramMap);

		int RentalCount = service.getRentalCount(paramMap);
		PageInfo pageInfo = new PageInfo(page, 5, RentalCount, 8);
		List<Rental> list = service.getRentalList(pageInfo, paramMap);

		model.addAttribute("list", list);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageTitle", "smore | Rental");
		return "category/rental";
	}

	@GetMapping("/rental/clip") 
	public ResponseEntity<int[]> rentalClip(
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int rentalNo, int isClip)
	{
		System.out.println("스크랩 요청 옴");
		
		int result = 0;
		
		if(isClip == 1) {
			result = service.clipRental(loginMember.getMemNo(), rentalNo);	
			System.out.println("isClip == 1 인 상태");
		}else {
			result = service.unClipRental(loginMember.getMemNo(), rentalNo);	
			System.out.println("isClip == 1 이 아닌 상태");
		}
		
		if(result > 0) {
			int clipCount = service.clipCount(rentalNo);
			int[] pArr = {isClip, clipCount};
			return ResponseEntity.status(HttpStatus.OK).body(pArr);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@GetMapping("/rental/error")
	public String error() {
		return "common/error";
	}
	
}
