package com.multi.smore.recipe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.multi.smore.common.util.PageInfo;
import com.multi.smore.member.model.vo.Member;
import com.multi.smore.recipe.model.service.RecipeService;
import com.multi.smore.recipe.model.vo.Recipe;
import com.multi.smore.recipe.model.vo.RecipeReply;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RecipeController {
	
	@Autowired
	private RecipeService service;
	
	@GetMapping("/recipe")
	public String list(Model model, @SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam Map<String, Object> paramMap,
			@RequestParam(required = false) List<String> rcpPat2,
			@RequestParam(required = false) List<String> rcpPartsDtls
			) {
		int page = 1;
		
		Map<String, Object> searchMap = new HashMap<>();
		try {
			String all = (String) paramMap.get("all");
			if(all != null && all.length() > 0) {
				searchMap.put("all", all);
			}
			if(loginMember != null) {
				searchMap.put("memNo", loginMember.getMemNo());
			}
			searchMap.put("rcpPat2", rcpPat2);
			searchMap.put("rcpPartsDtls", rcpPartsDtls);
			if(paramMap.get("infoEng") != null) {
				searchMap.put("infoEng", paramMap.get("infoEng"));
			}else {
				searchMap.put("infoEng", "1000");
			}
			page = Integer.parseInt(""+paramMap.get("page"));
		} catch (Exception e) {}
		
		System.out.println("searchMap : " + searchMap);
		
		int recipeCount = service.getRecipeCount(searchMap);
		PageInfo pageInfo = new PageInfo(page, 5, recipeCount, 8);
		List<Recipe> list = service.getRecipeList(pageInfo, searchMap);
//		log.info("Recipe list : " + list);
		
		if(rcpPat2 == null) {
			rcpPat2 = new ArrayList<>();
		}
		paramMap.put("rcpPat2", rcpPat2);
		if(rcpPartsDtls == null) {
			rcpPartsDtls = new ArrayList<>();
		}
		paramMap.put("rcpPartsDtls", rcpPartsDtls);
		if(paramMap.get("infoEng") != null) {
			paramMap.put("infoEng", paramMap.get("infoEng"));
		}else {
			paramMap.put("infoEng", "1000");
		}

		
		System.out.println("paramMap : " + paramMap);
		model.addAttribute("list", list);
		model.addAttribute("hotList", service.getHotRecipeList());
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("pageInfo", pageInfo);
		
		return "category/recipe";
	}
	
	@RequestMapping("/detail/recipe-detail")
	public String view(Model model, @SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam("rcpNo") int rcpNo) {
		String memNo = "";
		if(loginMember != null) {
			memNo = "" +loginMember.getMemNo();
		}
		Recipe recipe = service.getRecipeByNo(rcpNo, memNo);
		List<RecipeReply> replyList = service.getRecipeReplyListByNo(rcpNo);
		if(recipe == null) {
			return "redirect:error";
		}
		
		System.out.println("recipe" + recipe.toString());
		model.addAttribute("recipe", recipe);
		model.addAttribute("replyList", replyList);
		
		return "detail/recipe-detail";
	}
	
	@RequestMapping("/detail/recipe/reply")
	public String writeReply(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@ModelAttribute RecipeReply reply
			) {
		reply.setMemNo(loginMember.getMemNo());
		reply.setWriter(loginMember.getId());
		reply.setWriterName(loginMember.getName());
		log.info("리플 작성 요청 Reply : " + reply);
		
		int result = service.insertRecipeReply(reply);
		
		if(result > 0) {
			model.addAttribute("msg", "댓글이 등록되었습니다.");
		}else {
			model.addAttribute("msg", "댓글 등록에 실패하였습니다.");
		}
		model.addAttribute("location", "/detail/recipe-detail?rcpNo=" + reply.getRcpNo());
		return "common/msg";
	}
	
	@RequestMapping("/recipe/replyDel")
	public String deleteReply(Model model, 
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int rcprNo, int rcpNo
			){
		log.info("리플 삭제 요청");
		int result = service.deleteRecipeReply(rcprNo);
		
		if(result > 0) {
			model.addAttribute("msg", "댓글 삭제가 정상적으로 완료되었습니다.");
		}else {
			model.addAttribute("msg", "댓글 삭제에 실패하였습니다.");
		}
		model.addAttribute("location", "/detail/recipe-detail?rcpNo=" + rcpNo);
		return "/common/msg";
	}
	
	@RequestMapping("/recipe/updateReply") 
	public String updateReply(Model model, RecipeReply recipeReply, int rcpNo){
		
		log.info("리플 수정 요청");
		int result = service.updateRecipeReply(recipeReply);
		
		if(result > 0) {
			model.addAttribute("msg", "댓글 수정이 정상적으로 완료되었습니다.");
		}else {
			model.addAttribute("msg", "댓글 수정에 실패하였습니다.");
		}
		model.addAttribute("location", "/detail/recipe-detail?rcpNo=" + rcpNo);
		
		return "/common/msg";
	}
	
	@ResponseBody
	@RequestMapping(value="/recipe/clip", produces = "application/json; charset=utf-8")
	public int recipeClip(
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int rcpNo, int isClip)
	{
		int result = 0;
		if(isClip == 1) {
			result = service.clipRecipe(rcpNo, loginMember.getMemNo());
			isClip = 1;
		}else {
			result = service.unClipRecipe(rcpNo, loginMember.getMemNo());
			isClip = 0;
		}
		
		System.out.println("isClip : " + isClip);
		
		return isClip;
	}
	
	@ResponseBody
	@RequestMapping(value="/recipe/like", produces = "application/json; charset=utf-8")
	public int[] recipeLike(
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			int rcpNo, int isLike)
	{
		int result = 0;
		if(isLike == 1) {
			result = service.likeRecipe(rcpNo, loginMember.getMemNo());
			isLike = 1;
		}else {
			result = service.unLikeRecipe(rcpNo, loginMember.getMemNo());
			isLike = 0;
		}
		int likeCount = service.getRecipeLikeCountByNo(rcpNo);
		
		int[] resultList = {isLike, likeCount};
		
		System.out.println("isLike : " + isLike);
		
		return resultList;
	}
	
	@GetMapping("/recipe/error")
	public String error() {
		return "common/error";
	}

}
