package com.multi.smore.outdoor.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkReply {

	private int	parkRno;	// 공원 리뷰 번호
	private int	parkNo;		// 공원 게시글 번호 
	private int	memNo;		// 회원 일련번호
	private String	writer;		// 회원 아이디 	
	private String	writerName;	// 회원 이름 
	private String	content;	
	private String	status;	
	private Date	createDate;	
	private Date	modifyDate;	
}	
