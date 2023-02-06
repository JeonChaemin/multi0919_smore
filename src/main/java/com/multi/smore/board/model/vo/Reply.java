package com.multi.smore.board.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
	private int rNo;
	private int bNo;
	private int mNo;
	private String id;
	private String content;	
	private Date createDate;
	private Date modifyDate;
}