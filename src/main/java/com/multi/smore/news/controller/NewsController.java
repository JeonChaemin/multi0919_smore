package com.multi.smore.news.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.multi.smore.api.naver.NaverSearchAPI;
import com.multi.smore.common.util.PageInfo;
import com.multi.smore.news.model.service.NewsService;
import com.multi.smore.news.model.vo.News;

import lombok.extern.slf4j.Slf4j;

@Controller
public class NewsController {
	@Autowired
	private NewsService service;
	
	@GetMapping("/news")
	public String mainPage(Model model, @RequestParam Map<String, String> paramMap) {
		initDB();
		Map<String, Object> map = new HashMap<>();
		int count = service.getNewsCount(map);
		int page = 1;
		
		try {
			page = Integer.parseInt(paramMap.get("page"));
		} catch (Exception e) {
		}
		map.put("title", "1인 가구");
		PageInfo pageInfo = new PageInfo(page, 5, count, 8); 
		List<News> list = service.getNewsList(pageInfo, map);
		
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("pageTitle", "smore | News");
		return "category/news";
	}
	
	private void initDB() {
		List<News> nlist = new ArrayList<>();
		service.deleteAllNewsList();
		nlist.addAll(NaverSearchAPI.getNewsList("1인 가구", 100, 1));
		nlist.addAll(NaverSearchAPI.getNewsList("1인 가구", 100, 101));
		for (News news : nlist) {
			service.insertNews(news);
		}
	}
}
