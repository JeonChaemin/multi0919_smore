package com.multi.smore.board.model.vo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
	private int bbNo;
	private int memNo;
	private String id;
	private String title;
	private String content;
	private String type;
	private String originalFileName;
	private String renamedFileName;
	private int readCount;
	private String status;
	private List<BoardReply> replyList;
	private Date createDate;
	private Date modifyDate;
	private int likeCount;
	private int isLike;
	private int viewNo;
}