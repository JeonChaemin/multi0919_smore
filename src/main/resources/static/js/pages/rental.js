// 초기화 문구
$(function() {
	// 하트 class 초기화
	clipItems = document.getElementsByClassName('clip');

	for (i = 0; i < clipItems.length; i++) {
		clipValue = JSON.parse(clipItems[i].getAttribute('value'));
		console.log(clipValue);

		if (clipValue == 1) {
			$(clipItems[i]).addClass("active")
		}
		else {
			$(clipItems[i]).removeClass("active")
		}
	}
});

function onClickLike(id, addLike) {
	likeValue = new Number($('#' + id).text());
	likeValue += new Number(addLike);
	// 여기에 AJAX로 DB 업데이트하는 코드 있어야함!!

			$('#' + id).text((likeValue > 0 ? '+' : '') + likeValue);

}

function onClickClip(id, rentalNo) {
	clipValue = JSON.parse($('#'+id).attr('value'));
	/*consol.log(clipValue);*/
	if (clipValue == 0) {
		clipValue = 1;
	} else {
		clipValue = 0;
	}
	// 여기에 AJAX로 DB 업데이트하는 코드 있어야함!!
	$.ajax({
		method: 'get',
		url: '/rental/clip?rentalNo=' + rentalNo + '&isClip=' + clipValue,
		contentType: 'application/json',
		dataType: 'json',
		success: (result) => {
			$('#' + id).attr('value', '' + result[0]);
			$('#aazz' + id).html('스크랩 수 : ' + result[1]);
			console.dir(result);
			if (result[0] == 1) {
				$('#' + id).addClass("active")
			} else {
				$('#' + id).removeClass("active")
			}
		},
		error: (e) => {
			alert('전송 실패!!');
		}
	});
}


/* selelct - searchType - 정렬*/
var searchRegion1 = new Choices("#searchRegion", {
	shouldSort: false
});

/* selelct - searchType - 정렬*/
var searchHouseType1 = new Choices("#searchHouseType", {
	shouldSort: false
});

/* 페이징 */
function movePage(pageUrl) {

	/* 검색값*/
	var searchValue = document.getElementById("searchValue"); // 갤럭시
	pageUrl += '&searchValue=' + searchValue.value;

	/* 검색타입 - select*/
	var searchRegion = document.getElementById("searchRegion");
	var value = (searchRegion.options[searchRegion.selectedIndex].value);
	pageUrl += '&searchRegion=' + value;
	
	/* 검색타입 - select*/
	var searchHouseType = document.getElementById("searchHouseType");
	var value = (searchHouseType.options[searchHouseType.selectedIndex].value);
	pageUrl += '&searchHouseType=' + value;

	/* 검색 카테고리 - 체크박스*/
	var searchSuplyType = document.getElementsByName("searchSuplyType");
	for (i = 0; i < searchSuplyType.length; i++) {
		if (searchSuplyType[i].checked == true) {
			pageUrl += '&searchSuplyType=' + searchSuplyType[i].value;
		}
	}

	location.href = encodeURI(pageUrl);
}