package com.multi.smore.recipe.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeReply {
	private int rcprNo;
	private int rcpNo;
	private int memNo;
	private String writer;
	private String writerName;
	private String content;
	private int score;
	private String status;
	private Date createDate;
	private Date modifyDate;
}
