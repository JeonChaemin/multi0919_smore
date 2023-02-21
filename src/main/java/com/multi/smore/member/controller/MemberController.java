package com.multi.smore.member.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.kakao.KaKaoService;
import com.multi.smore.member.model.service.MemberService;
import com.multi.smore.member.model.vo.Member;
import com.multi.smore.member.model.vo.MemberForm;
import com.multi.smore.oneprogram.model.service.OneProgramService;
import com.multi.smore.oneprogram.model.vo.OneProgram;
import com.multi.smore.outdoor.model.service.ParkService;
import com.multi.smore.outdoor.model.service.TrackingService;
import com.multi.smore.outdoor.model.vo.Park;
import com.multi.smore.outdoor.model.vo.Tracking;
import com.multi.smore.recipe.model.service.RecipeService;
import com.multi.smore.recipe.model.vo.Recipe;
import com.multi.smore.rental.model.service.RentalService;
import com.multi.smore.rental.model.vo.Rental;
import com.multi.smore.trade.model.service.TradeService;
import com.multi.smore.trade.model.vo.Trade;

import lombok.extern.slf4j.Slf4j;



@Slf4j // log4j 선언을 대신 선언 해주는 lombok 어노테이션
@SessionAttributes("loginMember") // loginMember를 Model 취급할때 세션으로 처리하도록 도와주는 어노테이션
@Controller
public class MemberController {

	@Autowired
	private MemberService service;
	
	@Autowired
	private KaKaoService kakaoService;
	
	@Autowired
	private OneProgramService oneProgramService;
	
	@Autowired
	private RentalService rentalService;
	
	@Autowired
	private ParkService parkService;
	
	@Autowired
	private TrackingService trackingService;
	
	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private RecipeService recipeService;
	
	
	@GetMapping("/login")
	String loginPage() {
		return "member/login";
	}
	
	
	@PostMapping("/login")
	String login(Model model, String userId, String userPwd) {
		log.info("id : " + userId + ", pwd : " + userPwd);
		Member loginMember = service.login(userId, userPwd);
		if(loginMember != null) { // 성공
			model.addAttribute("loginMember", loginMember); // 세션에 저장되는 코드, @SessionAttributes
			return "redirect:/";
		}else { // 실패
			model.addAttribute("msg", "아이디 패스워드가 잘못되었습니다.");
			model.addAttribute("location", "/");
			return "common/msg";
		}
	}
	
	
	@GetMapping("/kakaoLogin")
	public String kakaoLogin(Model model, String code) {
		log.info("로그인 요청");
		if(code != null) {
			try {
				String loginUrl = "http://localhost/kakaoLogin";
				String token = kakaoService.getToken(code, loginUrl);
				Map<String, Object> map = kakaoService.getUserInfo(token);
				String kakaoToken = (String) map.get("id");
				Member loginMember = service.loginKaKao(kakaoToken);

				if(loginMember != null) { // 로그인 성공
					model.addAttribute("loginMember",loginMember); // 세션으로 저장되는 코드, 이유: @SessionAttributes
					return "redirect:/";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		model.addAttribute("msg", "로그인에 실패하였습니다.");
		model.addAttribute("location","/");
		return "common/msg";
	}
	

	
	@RequestMapping("/logout")
	public String logout(SessionStatus status) { // status : 세션의 상태 확인과 해제가 가능한 클래스
		log.info("status : " + status.isComplete());
		status.setComplete();
		log.info("status : " + status.isComplete());
		return "redirect:/";
	}
	
	@GetMapping("/member/enroll")
	public String enrollPage() {
		log.info("가입 페이지 요청");
		return "member/enroll";
	}
	
	@GetMapping("/member/enroll/kakao")
	public String enrollKakao(Model model, String code) {
		log.info("가입 페이지 요청");
		if(code != null) {
			try {
				String enrollUrl = "http://localhost/member/enroll/kakao";
				System.out.println("code : " + code);
				String token = kakaoService.getToken(code, enrollUrl);
				System.out.println("token : " + token);
				Map<String, Object> map = kakaoService.getUserInfo(token);
				System.out.println(map);
				model.addAttribute("kakaoMap", map);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "member/enroll";
	}
	
	// 회원가입 
	@PostMapping("/member/enroll")
	public String enroll(Model model, @Validated MemberForm memberForm, BindingResult bindingResult) { // @ModelAttribute 생각가능
		log.info("회원가입, MemberForm : " + memberForm.toString());

		if(bindingResult.hasErrors()) {
			model.addAttribute("msg","회원가입 실패 : " + bindingResult.getAllErrors().get(0).getDefaultMessage());
			model.addAttribute("location", "/member/enroll");
			return "common/msg";
		}

		Member member = memberForm.toMember();
		int result = service.save(member);
		
		if(result > 0) { // 성공
			model.addAttribute("msg", "회원가입에 성공하였습니다.");
			model.addAttribute("location", "/");
		}else { // 실패
			model.addAttribute("msg", "회원가입에 실패하였습니다. 입력정보를 확인하세요.");
			model.addAttribute ("location", "/");
		}
		return "common/msg";
	}
	
	
	// AJAX 회원아이디 중복 검사부
	@GetMapping("/member/idCheck")
	public ResponseEntity<Map<String, Object>> idCheck(String id){
		log.info("아이디 중복 확인 : " + id);
		
		boolean result = service.validate(id);
		Map<String,	Object> map = new HashMap<String, Object>();
		map.put("validate", result);
		
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	
	
	@PostMapping("/member/update")
    public String update(Model model, 
		@ModelAttribute Member updateMember, // request에서 온 값
		@SessionAttribute(name = "loginMember", required = true) Member loginMember // 세션 값
		) {
		log.info("update 요청, updateMember : " + updateMember);

		updateMember.setMemNo(loginMember.getMemNo());
		int result = service.save(updateMember);

		if (result > 0) {
			model.addAttribute("loginMember", service.findById(loginMember.getId())); // DB에서 있는 값을 다시 세션에 넣어주는 코드
			model.addAttribute("msg", "회원정보를 수정하였습니다.");
			model.addAttribute("location", "/member/view");
		} else {
			model.addAttribute("msg", "회원정보 수정에 실패하였습니다.");
			model.addAttribute("location", "/member/view");
		}
		return "common/msg";
	}
	
	
	@GetMapping("/member/view")
	public String memberView(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember) {
		if(loginMember == null) {
			return "/common/error-login";
		}
		Map<String, Object> paramOMap = new HashMap<>();
		paramOMap.put("memNo", ""+loginMember.getMemNo());
		Map<String, String> paramSMap = new HashMap<>();
		paramSMap.put("memNo", ""+loginMember.getMemNo());
		
		PageInfo pageInfoTrade = new PageInfo(1, 1, tradeService.getTradeCount(paramOMap), tradeService.getTradeCount(paramOMap));
		List<Trade> getTradeList = tradeService.getTradeList(pageInfoTrade, paramOMap);
		List<Trade> tradeList = new ArrayList<>();
		for (Trade trade : getTradeList) {
			if (trade.getIsClip() == 1) {
				tradeList.add(trade);
			}
		}
		
		PageInfo pageInfoRecipe = new PageInfo(1, 1, recipeService.getRecipeCount(paramOMap), recipeService.getRecipeCount(paramOMap));
		List<Recipe> getRecipeList = recipeService.getMypageRecipeList(pageInfoRecipe, paramOMap);
		List<Recipe> recipeList = new ArrayList<>();
		for (Recipe recipe : getRecipeList) {
			if (recipe.getIsClip() == 1) {
				recipeList.add(recipe);
			}
		}
		
		PageInfo pageInfoPark = new PageInfo(1, 1, parkService.selectParkCount(paramOMap), parkService.selectParkCount(paramOMap));
		List<Park> getParkList = parkService.selectParkList(pageInfoPark, paramOMap);
		List<Park> parkList = new ArrayList<>();
		for (Park park : getParkList) {
			if (park.getIsClip() == 1) {
				parkList.add(park);
			}
		}
		
		PageInfo pageInfoTracking = new PageInfo(1, 1, trackingService.selectTrackCount(paramOMap), trackingService.selectTrackCount(paramOMap));
		List<Tracking> getTrackingList = trackingService.selectTrackingList(pageInfoTracking, paramOMap);
		List<Tracking> trackingList = new ArrayList<>();
		for (Tracking tracking : getTrackingList) {
			if (tracking.getIsClip() == 1) {
				trackingList.add(tracking);
			}
		}
		
		PageInfo pageInfoRental = new PageInfo(1, 1, rentalService.getRentalCount(paramOMap), rentalService.getRentalCount(paramOMap));
		List<Rental> getRentalList = rentalService.getRentalList(pageInfoRental, paramOMap);
		List<Rental> rentalList = new ArrayList<>();
		for (Rental rental : getRentalList) {
			if (rental.getIsClip() == 1) {
				rentalList.add(rental);
			}
		}
		
		PageInfo pageInfoOneProgram = new PageInfo(1, 1, oneProgramService.getOneProgramCount(paramSMap), oneProgramService.getOneProgramCount(paramSMap));
		List<OneProgram> getOprogramList = oneProgramService.getOneProgramList(pageInfoOneProgram, paramSMap);
		List<OneProgram> oprogramList = new ArrayList<>();
		for (OneProgram oneprogram : getOprogramList) {
			if (oneprogram.getIsClip() == 1) {
				oprogramList.add(oneprogram);
			}
		}
		
		model.addAttribute("tradeList", tradeList);
		model.addAttribute("recipeList", recipeList);
		model.addAttribute("parkList", parkList);
		model.addAttribute("trackingList", trackingList);
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("oprogramList", oprogramList);
		model.addAttribute("pageTitle", "smore | My page");
		return "member/view";
	}
	
	@GetMapping("/member/reset-password")
	public String updatePwdPage() {
		return "member/reset-password";
	}
	
	@PostMapping("/member/updatePwd")
	public String updatePwd(Model model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			String userPwd
			) {
		int result = service.updatePwd(loginMember, userPwd);
		
		if(result > 0) {
			model.addAttribute("msg", "비밀번호 수정에 성공하였습니다.");
		}else {
			model.addAttribute("msg", "비밀번호 변경에 실패했습니다.");
		}
		model.addAttribute("script", "self.close()");
		return "common/msg";
	}
	
	
	@RequestMapping("/member/delete")
	public String delete(Model model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember) {
		int result = service.delete(loginMember.getMemNo());
		if(result > 0) {
			model.addAttribute("msg", "회원탈퇴에 성공하였습니다.");
			model.addAttribute("location","/logout");
		}else {
			model.addAttribute("msg", "회원탈퇴에 실패하였습니다.");
			model.addAttribute("location","/member/view");
		}
		return  "/common/msg";
	}
	
	@GetMapping("/member/error")
	public String error() {
		return "common/error";
	}
	
}
