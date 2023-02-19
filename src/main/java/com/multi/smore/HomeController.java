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

import com.multi.smore.api.naver.NaverSearchAPI;
import com.multi.smore.board.model.service.BoardService;
import com.multi.smore.board.model.vo.Board;
import com.multi.smore.common.util.PageInfo;
import com.multi.smore.news.model.service.NewsService;
import com.multi.smore.news.model.vo.News;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private BoardService boardService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model, HttpSession session) {
		initDB();
		Map<String, Object> newsMap = new HashMap<>();
		newsMap.put("title", "1인 가구");
		PageInfo pageInfo = new PageInfo(1, 6, 6, 6);
		List<News> newsList = newsService.getNewsList(pageInfo, newsMap);
		
		Map<String, String> boardMap = new HashMap<>();
		boardMap.put("type", "notice");
		List<Board> boardList = boardService.getBoardList(pageInfo,boardMap);
		
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
