package com.multi.smore.rental.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.rental.model.vo.Rental;

@Mapper
public interface RentalMapper {
	List<Rental> selectRentalList(Map<String, Object> map);
	List<Rental> selectRentalListHome(Map<String, String> paramMap); //통합검색
	int selectRentalCount(Map<String, Object> map);
	int clipRental(Map<String, String> map);
	int unClipRental(Map<String, String> map);
	int clipCount(int rentalNo);
	//푸시
}
