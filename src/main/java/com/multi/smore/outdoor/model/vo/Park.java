package com.multi.smore.outdoor.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Park {
	private int parkNo; //일련번호
	private String manageNo; //관리번호
	private String parkNm; //공원명
	private String parkSe; //공원구분
	private String rdnmadr; //소재지도로명주소
	private String lnmadr; //소재지지번주소
	private double latitude; //위도
	private double longitude; //경도
	private double parkAr; //공원면적
	private String mvmFclty; //공원보유시설(운동시설)
	private String amsmtFclty; //공원보유시설(유희시설)
	private String cnvnncFclty; //공원보유시설(편익시설)
	private String cltrFclty; //공원보유시설(교양시설)
	private String etcFclty; //공원보유시설(기타시설)
	private Date appnNtfcDate; //지정고시일
	private String institutionNm; //관리기관명
	private String phoneNumber; //전화번호
	private Date referenceDate; //데이터기준일자
	private int insttCode; //제공기관코드
	private int isClip;
}
