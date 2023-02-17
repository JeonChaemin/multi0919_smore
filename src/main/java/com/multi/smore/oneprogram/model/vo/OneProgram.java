package com.multi.smore.oneprogram.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OneProgram {
	private int onepNo;
	private String partcptnId;
	private String partcptnSj;
	private String tyNm;
	private String seNm;
	private String atdrcNm;
	private Date rceptDe1;
	private Date rceptDe2;
	private Date progrsDe1;
	private Date progrsDe2;
	private String agrdeNm;
	private String sexdstnNm;
	private String trgetInfo;
	private int tme;
	private String partcptnNmpr;
	private String partcptCtNm;
	private String partcptAmount;
	private String rceptMthNm;
	private String rceptMthLink;
	private String progrsInqry;
	private String placeAdres1;
	private String placeAdres2;
	private String cn;
	private Date regDate2;
	private String insttNm;
	private int isClip;
}


