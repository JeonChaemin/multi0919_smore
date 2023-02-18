package com.multi.smore.trade.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyTrade {
	private int tradeRNo;
	private int tradeNo;
	private int writerNo;
	private String writerId;
	private String content;	
	private String status;	
	private Date createDate;
	private Date modifyDate;
}
