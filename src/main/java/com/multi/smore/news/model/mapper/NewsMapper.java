package com.multi.smore.news.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.news.model.vo.News;

@Mapper
public interface NewsMapper {
	int insertNews(News n);
	int selectNewsCount(Map<String, Object> param);
	List<News> selectNewsList(Map<String, Object> param);
	int deleteAllNewsList();
}
