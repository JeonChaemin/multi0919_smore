package com.multi.smore.news.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News{
	private int nno;
	private String title;
	private String originallink;
	private String link;
	private String description;
	private Date pubDate;
}
