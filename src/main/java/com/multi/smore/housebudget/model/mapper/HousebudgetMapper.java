package com.multi.smore.housebudget.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.housebudget.model.vo.Housebudget;

@Mapper
public interface HousebudgetMapper {
	int selectHbCount(int memNo);
	List<Housebudget> selectAllHb(Map<String, Object> searchMap);
	int insertHb(Housebudget hb);
	List<String> selectHbByRange(Map<String, Object> searchMap);
	int updateHb(Map<String, Object> updateMap);
	int deleteHb(Map<String, Object> updateMap);
}
