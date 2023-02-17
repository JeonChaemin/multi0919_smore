package com.multi.smore.kakao;

import java.util.Date;

import lombok.Data;

@Data
public class KakaoPayReadyVO {
	private String tid;
	private String next_redirect_pc_url;
	private Date created_at;
}
