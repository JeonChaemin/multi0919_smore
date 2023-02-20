package com.multi.smore.recipe.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.recipe.model.mapper.RecipeMapper;
import com.multi.smore.recipe.model.vo.Recipe;
import com.multi.smore.recipe.model.vo.RecipeClip;
import com.multi.smore.recipe.model.vo.RecipeLike;
import com.multi.smore.recipe.model.vo.RecipeReply;

@Service
public class RecipeService {
	@Autowired
	private RecipeMapper mapper;
	
	public List<Recipe> getSearchRecipeList(Map<String, String> param){
		return mapper.selectRecipeSearchList(param);
	}
	
	public List<Recipe> getMypageRecipeList(PageInfo pageInfo, Map<String, Object> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() - 1));
		return mapper.selectRecipeMypageList(param);
	}
	
	public List<Recipe> getRecipeList(PageInfo pageInfo, Map<String, Object> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() - 1));
		return mapper.selectRecipeList(param);
	}
	
	public int getRecipeCount(Map<String, Object> param) {
		return mapper.selectRecipeCount(param);
	}
	
	public List<Recipe> getHotRecipeList(){
		return mapper.selectHotRecipeList();
	}
	
	public Recipe getRecipeByNo(int rcpNo, String memNo) {
		Map<String, String> map = new HashMap<>();
		map.put("rcpNo", ""+rcpNo);
		map.put("memNo", memNo);
		return mapper.selectRecipeByNo(map);
	}
	
	public List<RecipeReply> getRecipeReplyListByNo(int rcpNo){
		return mapper.selectRecipeReplyListByNo(rcpNo);
	}
	
	public int getRecipeReplyCountByNo(int rcpNo) {
		return mapper.selectRecipeReplyCountByNo(rcpNo);
	}
	
	public List<RecipeClip> getRecipeClipList(PageInfo pageInfo, Map<String, String> param){
		param.put("limit", "" + pageInfo.getListLimit());
		param.put("offset", "" + (pageInfo.getStartList() - 1));
		return mapper.selectRecipeClipList(param);
	}
	
	public int getRecipeClipCount(int memNo) {
		return mapper.selectRecipeClipCount(memNo);
	}
	
	public RecipeClip getRecipeClipByNo(Map<String, String> param) {
		return mapper.selectRecipeClipByNo(param);
	}
	
	public RecipeLike getRecipeLikeByNo(Map<String, String> param) {
		return mapper.selectRecipeLikeByNo(param);
	}
	
	public int getRecipeLikeCountByNo(int rcpNo) {
		return mapper.selectRecipeLikeCountByNo(rcpNo);
	}
	
	public int insertRecipe(Recipe recipe) {
		return mapper.insertRecipe(recipe);
	}
	
	public int insertRecipeReply(RecipeReply recipeReply) {
		return mapper.insertRecipeReply(recipeReply);
	}
	
	public int clipRecipe(int rcpNo, int memNo) {
		Map<String, String> map = new HashMap<>();
		map.put("rcpNo", ""+rcpNo);
		map.put("memNo", ""+memNo);
		return mapper.insertRecipeClip(map);
	}
	
	public int likeRecipe(int rcpNo, int memNo) {
		Map<String, String> map = new HashMap<>();
		map.put("rcpNo", ""+rcpNo);
		map.put("memNo", ""+memNo);
		return mapper.insertRecipeLike(map);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int updateRecipe(Recipe recipe) {
		return mapper.updateRecipe(recipe);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int updateRecipeReply(RecipeReply recipeReply) {
		return mapper.updateRecipeReply(recipeReply);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int deleteRecipe(int rcpNo) {
		return mapper.deleteRecipe(rcpNo);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int deleteRecipeReply(int rcprNo) {
		return mapper.deleteRecipeReply(rcprNo);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int unClipRecipe(int rcpNo, int memNo) {
		Map<String, String> map = new HashMap<>();
		map.put("rcpNo", ""+rcpNo);
		map.put("memNo", ""+memNo);
		return mapper.deleteRecipeClip(map);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public int unLikeRecipe(int rcpNo, int memNo) {
		Map<String, String> map = new HashMap<>();
		map.put("rcpNo", ""+rcpNo);
		map.put("memNo", ""+memNo);
		return mapper.deleteRecipeLike(map);
	}
	
}
