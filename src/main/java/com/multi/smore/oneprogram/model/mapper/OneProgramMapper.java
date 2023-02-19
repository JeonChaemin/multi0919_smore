package com.multi.smore.oneprogram.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.oneprogram.model.vo.OneProgram;

@Mapper
public interface OneProgramMapper {
	List<OneProgram> selectOneProgramList(Map<String, String> map);
	int selectOneProgramCount(Map<String, String> map);
	OneProgram selectOneProgramByNo(Map<String, String> map);
	
//	좋아요
	int clipOneProgram(Map<String, String> map);
	int unClipOneProgram(Map<String, String> map);
	List<OneProgram> selectOneProgramListHome(Map<String, String> map);
	
//	int insertOneProgram(OneProgram op);
//	int deleteAllOneProgramList1();
//	int deleteAllOneProgramList2();
//	int deleteAllOneProgramList3();
//	int deleteOneProgram();
}
