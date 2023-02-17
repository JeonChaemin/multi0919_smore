package com.multi.smore.news.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.news.model.mapper.NewsMapper;
import com.multi.smore.news.model.vo.News;

@Service
public class NewsService {
	@Autowired
	NewsMapper mapper;
	
	@Transactional(rollbackFor = Exception.class)
	public int insertNews(News n) {
		return mapper.insertNews(n);
	}
	
	public int getNewsCount(Map<String, Object> param) {
		return mapper.selectNewsCount(param);
	}
	
	public List<News> getNewsList(PageInfo pageInfo, Map<String, Object> param){
		param.put("limit", ""+pageInfo.getListLimit());
		param.put("offset", ""+(pageInfo.getStartList() - 1));
		return mapper.selectNewsList(param);
	}
	
	public int deleteAllNewsList() {
		return mapper.deleteAllNewsList();
	}
}
