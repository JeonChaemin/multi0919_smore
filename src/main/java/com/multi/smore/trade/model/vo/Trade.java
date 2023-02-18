package com.multi.smore.trade.model.vo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
	private int tradeNo;
	private int writerNo;
	private String writerId;
	private String id; // 멤버 ID
	private String type;
	private String category;
	private String region;
	private String title;
	private String conditions;
	private String price;
	private String content;
	private String originalFileName;
	private String renamedFileName;
	private int readCount;
	private String status;
	private List<ReplyTrade> replyTradeList;
	private Date createDate;
	private Date modifyDate;
	private int reviewCount;
	// 스크랩
	private int clipCount;
	private int isClip;
	

}
