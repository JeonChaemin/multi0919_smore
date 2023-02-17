package com.multi.smore.outdoor.model.vo;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tracking {

	private	int 	tNo;  				//순번
	private	long 	courseCategory;  	//코스 카테고리
	private	String 	courseCategoryNm;  //코스 카테고리명
	private	String 	areaGu;  			//자치구
	private	String 	distance; 			 //거리
	private	String 	leadTime; 			 //소요시간
	private	String 	courseLevel;  		//코스레벨
	private	String 	relateSubway;  		//연계지하철
	private	String 	trafficInfo;  		//교통편
	private	String 	content;  			//설명
	private	String 	courseName;  		//코스명
	private	String 	detailCourse;  		//세부코스
	private	String 	cpiName;  			//포인트명칭
	private	String 	cpiContent; 		//포인트 설명
	private	double 	latitude;  			//위도
	private	double 	longitude;  		//경도
	private int isClip;
}
