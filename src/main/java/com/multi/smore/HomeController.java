package com.multi.smore;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.multi.smore.board.model.service.BoardService;
import com.multi.smore.member.model.service.MemberService;
import com.multi.smore.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private BoardService service;
	
	@Autowired
	private MemberService memberService;// 삭제하기
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, HttpSession session) {
		logger.info("Welcome home! The client locale is {}.", locale);
		Member loginMember = memberService.login("admin", "1212");// 삭제하기
		session.setAttribute("loginMember", loginMember);// 삭제하기
//		test();
		model.addAttribute("pageTitle", "smore | Home");
		return "home";
	}
	
//	public void test() {
//		Map<String, String> map = new HashMap<>();
//		PageInfo info = new PageInfo(1, 10, service.getBoardCount(map), 10);
//		logger.info("board List : " + service.getBoardList(info, map));
//	}
	
}
