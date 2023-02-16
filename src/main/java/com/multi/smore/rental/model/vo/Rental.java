package com.multi.smore.rental.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Getter
//@Setter
public class Rental {
	private int rentalNo; //db에 분류할 번호
	private String rentalType; //임대, 분양
	private String suplyHoCo; //공급호수 (전세임대 해당)
	private String pblancId; //공고ID
	private int houseSn; //주택 일련번호
	private String sttusNm; //상태_명
	private String pblancNm; //공고명
	private String suplyInsttNm; //공급_기관명
	private String houseTyNm; //주택유형_명
	private String suplyTyNm; //공급유형명
	private String beforePblancId; //이전_공고_ID
	private String rcritPblancDe; //모집공고일자
	private String przwnerPresnatnDe; //당첨자발표일자
	private String refrnc; //문의처
	private String url; //모집공고 URL
	private String pcUrl; //마이홈포털 PC URL
	private String mobileUrl; //마이홈포털 Mobile URL
	private String hsmpNm; //단지_명
	private String brtcNm; //광역시도명
	private String signguNm; //시군구명
	private String fullAdres; //전체주소
	private String rnCodeNm; //도로명 (주소가 도로명주소일 때 표시)
	private String refrnLegaldongNm; //참조_법정동명 (주소가 지번주소일 때 표시)
	private String pnu; //PNU
	private String heatMthdNm; //난방_방식명
	private String totHshldCo; //총세대수
	private int sumSuplyCo; //공급호수
	private int rentGtn; //최소임대보증금
	private int enty; //최소_계약금
	private int prtpay; //최소_중도금
	private int surlus; //최소_잔금
	private int mtRntchrg; //최소월임대료
	private String beginDe; //모집 시작 일자
	private String endDe; //모집 종료 일자
	private int clipCount;
	private int isClip;
}
