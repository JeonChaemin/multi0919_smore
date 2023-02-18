package com.multi.smore.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MapInfo {
	private String name;
	private String address;
	private double x;
	private double y;
}
