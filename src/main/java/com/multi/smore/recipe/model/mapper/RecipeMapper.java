package com.multi.smore.recipe.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.multi.smore.recipe.model.vo.Recipe;
import com.multi.smore.recipe.model.vo.RecipeClip;
import com.multi.smore.recipe.model.vo.RecipeLike;
import com.multi.smore.recipe.model.vo.RecipeReply;

@Mapper
public interface RecipeMapper {
	List<Recipe> selectRecipeSearchList(Map<String, String> map);
	List<Recipe> selectRecipeMypageList(Map<String, Object> map);
	List<Recipe> selectRecipeList(Map<String, Object> map);
	int selectRecipeCount(Map<String, Object> map);
	List<Recipe> selectHotRecipeList();
	Recipe selectRecipeByNo(Map<String, String> map);
	List<RecipeReply> selectRecipeReplyListByNo(int rcpNo);
	int selectRecipeReplyCountByNo(int rcpNo);
	List<RecipeClip> selectRecipeClipList(Map<String, String> map);
	int selectRecipeClipCount(int memNo);
	RecipeClip selectRecipeClipByNo(Map<String, String> map);
	RecipeLike selectRecipeLikeByNo(Map<String, String> map);
	int selectRecipeLikeCountByNo(int rcpNo);
	int insertRecipe(Recipe recipe);
	int insertRecipeReply(RecipeReply recipeReply);
	int insertRecipeClip(Map<String, String> map);
	int insertRecipeLike(Map<String, String> map);
	int updateRecipe(Recipe recipe);
	int updateRecipeReply(RecipeReply recipeReply);
	int deleteRecipe(int rcpNo);
	int deleteRecipeReply(int rcprNo);
	int deleteRecipeClip(Map<String, String> map);
	int deleteRecipeLike(Map<String, String> map);
}
