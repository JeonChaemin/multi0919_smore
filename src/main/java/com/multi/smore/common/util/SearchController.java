package com.multi.smore.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.multi.smore.board.model.service.BoardService;
import com.multi.smore.board.model.vo.Board;
import com.multi.smore.member.model.vo.Member;
import com.multi.smore.oneprogram.model.service.OneProgramService;
import com.multi.smore.oneprogram.model.vo.OneProgram;
import com.multi.smore.recipe.model.service.RecipeService;
import com.multi.smore.recipe.model.vo.Recipe;
import com.multi.smore.rental.model.service.RentalService;
import com.multi.smore.rental.model.vo.Rental;
import com.multi.smore.trade.model.service.TradeService;
import com.multi.smore.trade.model.vo.Trade;

@Controller
public class SearchController {

	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private RentalService rentalService;
	
	@Autowired
	private OneProgramService oprogramService;
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/search")
	public String searchList(Model model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam Map<String, String> paramMap) {
		String searchValue = paramMap.get("searchValue");
		Map<String, String> searchMap = new HashMap<>();
		searchMap.put("searchValue", searchValue);
		int count = 0;
		
		List<Recipe> recipeList = recipeService.getSearchRecipeList(paramMap);
		List<Trade> tradeList = tradeService.getTradeListHome(paramMap);
		List<Rental> rentalList = rentalService.getRentalListHome(paramMap);
		List<OneProgram> oprogramList = oprogramService.getOneProgramListHome(paramMap);
		List<Board> boardList = boardService.getBoardListHome(paramMap);
		List<Board> noticeList = new ArrayList<>();
		for (Board board : boardList) {
			if (board.getType().equals("notice")) {
				noticeList.add(board);
				boardList.remove(board);
			}
		}
		
		count = recipeList.size() + tradeList.size() + rentalList.size() + oprogramList.size() + boardList.size() + noticeList.size();
		
		model.addAttribute("recipeList", recipeList);
		model.addAttribute("tradeList", tradeList);
		model.addAttribute("rentalList", rentalList);
		model.addAttribute("oprogramList", oprogramList);
		model.addAttribute("boardList", boardList);
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("searchValue", searchValue);
		return "/category/search";
	}
	
}
