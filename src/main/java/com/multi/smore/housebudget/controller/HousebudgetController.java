package com.multi.smore.housebudget.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.multi.smore.housebudget.model.service.HousebudgetService;
import com.multi.smore.housebudget.model.vo.Housebudget;
import com.multi.smore.member.model.service.MemberService;
import com.multi.smore.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/housebudget")
@Controller
@SuppressWarnings("unchecked")
public class HousebudgetController {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private HousebudgetService service;
	
	@GetMapping("/list")
	public String list(Model model, @SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam Map<String, String> paramMap) {
		
		Map<String, Object> searchMap = new HashMap<>();
		int sum = 0;
		if(loginMember == null) {
			model.addAttribute("pageTitle", "smore | Login Required");
			return "/common/error-login";
		}
		searchMap.put("memNo", loginMember.getMemNo());
		try {
			String searchStart = paramMap.get("search-start");
			searchMap.put("searchStart", searchStart);
			String searchType = paramMap.get("search-type");
			System.out.println("searchType!!!!! : " + searchType);
			searchMap.put("searchType", searchType);
		} catch (Exception e) {
		}
		List<Housebudget> list = service.selectAllHb(searchMap);
		for (Housebudget housebudget : list) {
			if (housebudget.getType().equals("수입")) {
				sum += housebudget.getAmount();
			} else {
				sum -= housebudget.getAmount();
			}
			housebudget.setSum(sum);
		}
		model.addAttribute("list", list); 
		model.addAttribute("pageTitle", "smore | HouseBudget");
		
		return "category/housebudget";
	}
	
	//조회 JSON
	@RequestMapping(value = "/search", method= RequestMethod.GET)
	@ResponseBody
	public String[] searchRangeHb(
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam("searchRange") String searchRange
			) {
		int memNo = loginMember.getMemNo();
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("memNo", memNo);
		searchMap.put("searchRange", searchRange);
		List<String> resultList = new ArrayList<>(service.selectHbByRange(searchMap));
		String[] searchStartList = new String[resultList.size()];
		for (int i = 0; i < searchStartList.length; i++) {
			searchStartList[i] = resultList.get(i);
			System.out.print("searchStartList " + i + " : " + searchStartList[i]);
			System.out.println();
		}
		
	    return searchStartList;
	}
	
	//조회 JSON
	@GetMapping("/retrieve")
	@ResponseBody
	public List<Map<String, Object>> retrieveHb(
			@SessionAttribute(name = "loginMember", required = false) Member loginMember
			) {
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("searchStart", "");
		searchMap.put("searchType", "");
		int memNo = loginMember.getMemNo();
		searchMap.put("memNo", memNo);
		
	    List<Housebudget> HousebudgetList = service.selectAllHb(searchMap);
	    System.out.println("풀캘린더 ajax 파라미터 : " + searchMap);
	    JSONObject jsonObj = new JSONObject();
	    JSONArray jsonArr = new JSONArray();

	    HashMap<String, Object> hash = new HashMap<>();

	    for (int i = 0; i < HousebudgetList.size(); i++) {
	        hash.put("type", HousebudgetList.get(i).getType());
	        hash.put("title", HousebudgetList.get(i).getTitle());
	        hash.put("amount", HousebudgetList.get(i).getAmount());
	        hash.put("memo", HousebudgetList.get(i).getMemo());
	        hash.put("start", HousebudgetList.get(i).getStart());
	        
	        
	        jsonObj = new JSONObject(hash);
	        jsonArr.add(jsonObj);
	    }
	    log.info("jsonArrCheck: {}", jsonArr);
	    return jsonArr;
	}

	//등록 JSON
	@RequestMapping(value  ="/insert", produces = "application/json; charset=UTF-8", method= RequestMethod.POST)
	@ResponseBody
	public String insertHb(
			@RequestBody Housebudget hb,
			HttpSession session,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember
			) {
		hb.setMemNo(loginMember.getMemNo());
	    int res = service.insertHb(hb);
	    String resStr = res > 0 ? "success" : "fail";
	    return resStr;
	}

	//업데이트
	@PostMapping("/update")
	public String updateHb(Model model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam Map<String, String> paramMap
			) {
		Map<String,Object> updateMap = new HashMap<String,Object>();
		updateMap.put("memNo", loginMember.getMemNo());
		String oldStart = paramMap.get("old_start");
		updateMap.put("oldStart", oldStart);
		String oldTitle = paramMap.get("old_title");
		updateMap.put("oldTitle", oldTitle);
		String oldType = paramMap.get("old_type");
		updateMap.put("oldType", oldType);
		String modalType = paramMap.get("modal-type");
		updateMap.put("modalType", modalType);
		String modalTitle = paramMap.get("modal-title");
		updateMap.put("modalTitle", modalTitle);
		String modalAmount = paramMap.get("modal-amount");
		updateMap.put("modalAmount", modalAmount);
		String modalMemo = paramMap.get("modal-memo");
		updateMap.put("modalMemo", modalMemo);
		
		int	result = service.updateHb(updateMap);
		if(result > 0) {
			model.addAttribute("msg", "내역이 수정 되었습니다.");
			model.addAttribute("location", "/housebudget/list");
		}else {
			model.addAttribute("msg", "내역 수정에 실패하였습니다.");
			model.addAttribute("location", "/housebudget/list");
		}
		return "common/msg";
	}
	
	//삭제
	@GetMapping("/delete")
	public String deleteHb(Model model,
			@SessionAttribute(name = "loginMember", required = false) Member loginMember,
			@RequestParam Map<String, String> paramMap
			) {
		Map<String,Object> updateMap = new HashMap<String,Object>();
		updateMap.put("memNo", loginMember.getMemNo());
		String start = paramMap.get("start");
		updateMap.put("start", start);
		String title = paramMap.get("title");
		updateMap.put("title", title);
		String type = paramMap.get("type");
		updateMap.put("type", type);
		
		int	result = service.deleteHb(updateMap);
		if(result > 0) {
			model.addAttribute("msg", "내역이 삭제 되었습니다.");
			model.addAttribute("location", "/housebudget/list");
		}else {
			model.addAttribute("msg", "내역 삭제에 실패하였습니다.");
			model.addAttribute("location", "/housebudget/list");
		}
		return "common/msg";
	}
	
	@GetMapping("/error")
	public String error() {
		return "common/error";
	}
	
}
