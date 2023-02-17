package com.multi.smore.housebudget.model.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Housebudget {
	private int 	memNo; 	// INT,
	private String 	type; 	// VARCHAR(10),
	private String 	title; 	// VARCHAR(30),
	private int 	amount; // INT,
	private String 	memo; 	// VARCHAR(100),
	private Date 	start; 	// DATE,
	private int		sum;	
}
