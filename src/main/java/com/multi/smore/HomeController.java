package com.multi.smore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.multi.smore.api.naver.NaverSearchAPI;
import com.multi.smore.board.model.service.BoardService;
import com.multi.smore.board.model.vo.Board;
import com.multi.smore.common.util.PageInfo;
import com.multi.smore.member.model.vo.Member;
import com.multi.smore.news.model.service.NewsService;
import com.multi.smore.news.model.vo.News;
import com.multi.smore.oneprogram.model.service.OneProgramService;
import com.multi.smore.oneprogram.model.vo.OneProgram;
import com.multi.smore.recipe.model.service.RecipeService;
import com.multi.smore.recipe.model.vo.Recipe;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private OneProgramService oprogramService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, @SessionAttribute(name = "loginMember", required = false) Member loginMember) {
		initDB();
		Map<String, Object> newsMap = new HashMap<>();
		newsMap.put("title", "1인 가구");
		PageInfo pageInfo = new PageInfo(1, 5, 5, 5);
		List<News> newsList = newsService.getNewsList(pageInfo, newsMap);
		
		Map<String, String> boardMap = new HashMap<>();
		boardMap.put("type", "notice");
		List<Board> boardList = boardService.getBoardList(pageInfo,boardMap);
		
		String memNo1 = "";
		if (loginMember != null) {
			memNo1 = ""+loginMember.getMemNo();
		}
		List<Recipe> recipeList = new ArrayList<>();
		recipeList.add(recipeService.getRecipeByNo(711, memNo1));
		recipeList.add(recipeService.getRecipeByNo(993, memNo1));
		recipeList.add(recipeService.getRecipeByNo(773, memNo1));
		
		int memNo2 = 0;
		if(loginMember != null) {
			memNo2 = loginMember.getMemNo();
		}
		List<OneProgram> oprogramList = new ArrayList<>();
		oprogramList.add(oprogramService.findByNo(384, memNo2));
		oprogramList.add(oprogramService.findByNo(59, memNo2));
		oprogramList.add(oprogramService.findByNo(508, memNo2));
		oprogramList.add(oprogramService.findByNo(558, memNo2));
		
		model.addAttribute("oprogramList", oprogramList);
		model.addAttribute("recipeList", recipeList);
		model.addAttribute("newsList", newsList);
		model.addAttribute("boardList", boardList);
		model.addAttribute("pageTitle", "smore | Home");
		return "home";
	}
	
	private void initDB() {
		List<News> nlist = new ArrayList<>();
		newsService.deleteAllNewsList();
		nlist.addAll(NaverSearchAPI.getNewsList("1인 가구", 6, 1));
		for (News news : nlist) {
			newsService.insertNews(news);
		}
	}
	
}
