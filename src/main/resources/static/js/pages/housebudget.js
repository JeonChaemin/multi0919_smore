var type = new Choices("#type", {
	shouldSort: false,
});
var title = new Choices("#title", {
	shouldSort: false,
});

var calendar;
//화면 온로드 시
document.addEventListener('DOMContentLoaded', function() {
	initCalendar();
});

var initCalendar = function() {
	//캘린더 html 요소
	var calendarEl = document.getElementById('calendar');
	//캘린더가 생성되어 있을 경우 삭제
	if (calendar) {
		calendar.destroy();
	}
	//풀캘린더 인스턴스 생성
	calendar = new FullCalendar.Calendar(calendarEl, {
		initialView: 'dayGridMonth',
		headerToolbar: {
			left: 'title',
			center: '',
			right: 'prev today next'
		},
		footerToolbar: {
			center: ''
		},
		editable: true,
		droppable: true,
		selectable: true,
		unselectAuto: true,
		locale: 'ko',
		// 캘린더에 출력되는 데이터
//		events: [
//			{
//				title: '공과금',
//				start: '2023-02-16',
//				type: '지출',
//				amount: '76,000', //info.event.extendedProps["amount"]
//				color: "#F9E1E3",
//				textColor: "#DA3746"
//			},
//		],
		events: [
//			//비동기로 DB데이터 요청하기
			$.ajax({
				type: "GET",
				url: "/housebudget/retrieve",
				contentType: "application/json; charset=UTF-8",
				dataType: "json",
//				//성공 시
				success: function(response) {
					//var response = JSON.parse(data);
					for (var i = 0; i < response.length; i++) {
						if (response[i]['type'] == '수입') {
							var setColor = "#DAEDE8";
							var setTextColor = "#048565";
						} else {
							var setColor = "#F9E1E3";
							var setTextColor = "#DA3746";
						}
						calendar.addEvent({
							title:  response[i]['title'],
							start:  response[i]['start'],
							type:   response[i]['type'],
							amount: response[i]['amount'], //info.event.extendedProps["amount"]
							memo: 	response[i]['memo'],
							allDay: true,
							color: setColor,
							textColor: setTextColor
						})
					}
				}
			})
		],
		//드롭 완료 시
//		eventDrop: function(info) {
//			//비동기로 업데이트 요청하기
//			$.ajax({
//				type: "POST",
//				url: "/housebudget/update",
//				data: {
//					'title': info.title,
//					'start': dateFormat(info.event.start),
//					'파라미터3(기존 날짜)': dateFormat(info.oldEvent.start)
//				},
//				contentType: "application/json; charset=UTF-8",
//				dataType: text,
//				//성공 시 요소 제거
//				success: function(response) {
//
//				}
//			})
//		},
		//항목 클릭 시
		eventClick: function(info) {
			openModal(info.event.extendedProps["type"], info.event.title, info.event.extendedProps["amount"], info.event.extendedProps["memo"], info.event.start);
		},
		dateClick: function(info) {
			document.getElementById('start').value = (info.dateStr);
		}

	});
		
	calendar.render();
	
}

var openModal = function(type, title, amount, memo, start) {
	document.getElementById("myModalLabel").innerHTML = dateFormat(start);
	document.getElementById("modal_type_title").innerHTML = type + " - " + title;
	document.getElementById("modal_amount").innerHTML = amount;
	document.getElementById("modal_memo").innerHTML = nullCheck(memo);
	
	start.setDate(start.getDate()-1);
	document.getElementById("old_start").value = dateFormat(start);
	document.getElementById("old_type").value = type;
	document.getElementById("old_title").value = title;
	
	modalType.setChoiceByValue("");
	modalTitle.setChoiceByValue("");
	document.getElementById("modal-amount").value = amount;
	document.getElementById("modal-memo").value = nullCheck(memo);
	document.getElementById("budgetTrigger").click();
}

function nullCheck(val) {
	if(!val) {
		return "-";
	}else {
		return val;
	}
} 

//날짜 포맷(YYYY-MM-DD)
var dateFormat = function(date) {
	date.setDate(date.getDate()+1);
	return date.toISOString().substr(0, 10);
}

function checkValid() {
	var startVal  = document.getElementById('start').value;
	var typeVal   = document.getElementById('type').value;
	var titleVal  = document.getElementById('title').value;
	var amountVal = document.getElementById('amount').value;
	
	if(!startVal) {
		alert("날짜를 선택해주세요")
		document.getElementById('start').focus();
		return false;
	}
	
	if(!typeVal) {
		alert("타입을 선택해주세요")
		document.getElementById('type').focus();
		return false;
	}
	
	if(!titleVal) {
		alert("내용을 선택해주세요")
		document.getElementById('title').focus();
		return false;
	}
	
	if(!amountVal) {
		alert("금액을 입력해주세요")
		document.getElementById('amount').focus();
		return false;
	}
	return true;
}

//등록 버튼 클릭 이벤트
function regist() {
	console.dir("시작");
	if(!checkValid()) {
		return;
	}
	console.dir("checkValid 통과!!")
	var startVal  = document.getElementById('start').value;
	var typeVal   = document.getElementById('type').value;
	var titleVal  = document.getElementById('title').value;
	var amountVal = document.getElementById('amount').value;
	var memoVal   = document.getElementById('memo').value;
	
	//비동기로 등록하기
	$.ajax({
		type: "POST",
		url: "/housebudget/insert",
		data: JSON.stringify({
			'start' : startVal,
			'type'  : typeVal,
			'title' : titleVal,
			'amount': amountVal,
			'memo'  : memoVal,
		}),
		contentType: "application/json; charset=utf-8",
		dataType: "TEXT",
		//성공 시 캘린더 새로고침
		success: function() {
			initCalendar();
			alert('등록되었습니다')
			document.getElementById('start').value = '';
			type.setChoiceByValue('');
			title.setChoiceByValue('');
			document.getElementById('amount').value = '';
			document.getElementById('memo').value = '';
		}
	})
}

var today = new Date();
var todayYear = today.getFullYear;
var todayMonth = today.getMonth + 1;
var todayDate = today.getDate;

function cancel() {
	console.dir(title.getValue().value);
	document.getElementById('start').value = '';
	type.setChoiceByValue('');
	title.setChoiceByValue('');
	document.getElementById('amount').value = '';
	document.getElementById('memo').value = '';
}

type.passedElement.element.addEventListener('change', function(e) {
	console.dir("Type optionChanged!!! option : " + e.detail.value);
	title.destroy();
	title.init();
	if (e.detail.value == "수입") {
		title.setChoices(
			[
				{value: '경조사', label: '경조사'},
				{value: '용돈', label: '용돈'},
				{value: '월급', label: '월급'},
				{value: '이자', label: '이자'},
				{value: '기타', label: '기타'}
			],
			'value',
			'label',
			false
		);
	} else if (e.detail.value == "지출") {
		title.setChoices(
			[
				{value: '경조사', label: '경조사'},
				{value: '공과금', label: '공과금'},
				{value: '공부', label: '공부'},
				{value: '문화', label: '문화'},
				{value: '생활용품', label: '생활용품'},
				{value: '쇼핑', label: '쇼핑'},
				{value: '식비', label: '식비'},
				{value: '취미', label: '취미'},
				{value: '기타', label: '기타'}
			],
			'value',
			'label',
			false
		);
	};
	
});

var searchRange = new Choices("#search-range", {
	shouldSort: false
});
var searchStart = new Choices("#search-start", {
	shouldSort: false
});

searchRange.passedElement.element.addEventListener('change', function(e) {
	console.dir("SearchRange optionChanged!!! option : " + e.detail.value);
	var searchRangeValue = e.detail.value;
	console.dir(searchRangeValue);
	if (e.detail.value == "전체") {
		searchStart.destroy();
		searchStart.init();
		searchStart.setChoiceByValue("")
	} else if (e.detail.value == "월간" || e.detail.value == "연간") {
		$.ajax({
			type: "GET",
			url: "/housebudget/search",
			data: {
				"searchRange": searchRangeValue
			},
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			dataType: "json",
//			//성공 시
			success: function(response) {
				var searchStartList = [];
				for (var i = 0; i < response.length; i++) {
					searchStartList.push({value: response[i], label: response[i]})
				}
				console.dir(searchStartList);
				searchStart.destroy();
				searchStart.init();
				searchStart.setChoices(
					searchStartList,
					'value',
					'label',
					false
				);
			}
		})
	}
});

var searchType = new Choices("#search-type", {
	shouldSort: false
});

var modalType = new Choices("#modal-type", {
	shouldSort: false
});

var modalTitle = new Choices("#modal-title", {
	shouldSort: false
});

modalType.passedElement.element.addEventListener('change', function(e) {
	console.dir("Type optionChanged!!! option : " + e.detail.value);
	modalTitle.destroy();
	modalTitle.init();
	if (e.detail.value == "수입") {
		modalTitle.setChoices(
			[
				{value: '경조사', label: '경조사'},
				{value: '용돈', label: '용돈'},
				{value: '월급', label: '월급'},
				{value: '이자', label: '이자'},
				{value: '기타', label: '기타'}
			],
			'value',
			'label',
			false
		);
	} else if (e.detail.value == "지출") {
		modalTitle.setChoices(
			[
				{value: '경조사', label: '경조사'},
				{value: '공과금', label: '공과금'},
				{value: '공부', label: '공부'},
				{value: '문화', label: '문화'},
				{value: '생활용품', label: '생활용품'},
				{value: '쇼핑', label: '쇼핑'},
				{value: '식비', label: '식비'},
				{value: '취미', label: '취미'},
				{value: '기타', label: '기타'}
			],
			'value',
			'label',
			false
		);
	};
	
});

$('#start').datepicker({
	format: "yyyy-mm-dd",
    todayBtn: "linked",
    language: "ko",
    autoclose: true,
    todayHighlight: true,
    defaultViewDate: today
});

function deleteHb(){
	var pStart = document.getElementById('old_start').value;
	var pTitle = document.getElementById('old_title').value;
	var pType = document.getElementById('old_type').value;
	location.href = "/housebudget/delete?start="+pStart+"&title="+pTitle+"&type="+pType;
}