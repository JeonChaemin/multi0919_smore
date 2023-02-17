package com.multi.smore.board.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardReply {
	private int bbrNo;
	private int bbNo;
	private int memNo;
	private String id;
	private String content;	
	private Date createDate;
	private Date modifyDate;
}