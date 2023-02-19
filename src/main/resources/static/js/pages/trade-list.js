// 이미지 새창 띄우기
function doImgPop(img) {
	img1 = new Image();
	img1.src = (img);
	imgControll(img);
}
function imgControll(img) {
	if ((img1.width != 0) && (img1.height != 0)) {
		viewImage(img);
	}
	else {
		controller = "imgControll('" + img + "')";
		intervalID = setTimeout(controller, 20);
	}
}
function viewImage(img) {
	W = img1.width;
	H = img1.height;
	/* W="1000px";
	 H="auto";*/
	O = "width=" + W + ",height=" + H + ",scrollbars=yes";
	imgWin = window.open("", "", O);
	imgWin.document.write("<html><head><title>확대보기</title></head>");
	imgWin.document.write("<body topmargin=0 leftmargin=0>");
	imgWin.document.write("<img src=" + img + " onclick='self.close()' style='cursor:pointer;' title ='클릭하시면 창이 닫힙니다.'>");
	imgWin.document.close();
}

$(function() {
	// 1 . #checkAll 클릭
	$("#checkAll-1").click(function() {
		// 2. #checkAll이 체크되었을 때,
		// chk라는 name을 가진 input태그를 찾아 checked를 true로 정의
		if ($("#checkAll-1").prop("checked")) {
			$("input[name=categories]").prop("checked", true)
			// 3. #checkAll이 체크되지 않았을 때,
			// chk라는 name을 가진 input태그를 찾아 checked를 false로 정의
		} else {
			$("input[name=categories]").prop("checked", false)
		}
	})
});

$(function() {
	// 1 . #checkAll 클릭
	$("#checkAll-2").click(function() {
		// 2. #checkAll이 체크되었을 때,
		// chk라는 name을 가진 input태그를 찾아 checked를 true로 정의
		if ($("#checkAll-2").prop("checked")) {
			$("input[name=regions]").prop("checked", true)
			// 3. #checkAll이 체크되지 않았을 때,
			// chk라는 name을 가진 input태그를 찾아 checked를 false로 정의
		} else {
			$("input[name=regions]").prop("checked", false)
		}
	})
});

/* selelct - searchType - 정렬*/
var searchTypeS = new Choices("#searchType", {
	shouldSort: false
});

/* 페이징 */
function movePage(pageUrl) {

	/* 검색값*/
	var searchValue = document.getElementById("searchValue"); // 갤럭시
	pageUrl += '&searchValue=' + searchValue.value;

	/* 검색타입 - select*/
	var searchType = document.getElementById("searchType");
	var value = (searchType.options[searchType.selectedIndex].value);
	pageUrl += '&searchType=' + value;

	/* 검색 카테고리 - 체크박스*/
	var categories = document.getElementsByName("categories");
	for (i = 0; i < categories.length; i++) {
		if (categories[i].checked == true) {
			pageUrl += '&categories=' + categories[i].value;
		}
	}

	/* 검색 지역 - 체크박스*/
	var regions = document.getElementsByName("regions");
	for (i = 0; i < regions.length; i++) {
		if (regions[i].checked == true) {
			pageUrl += '&regions=' + regions[i].value;
		}
	}

	location.href = encodeURI(pageUrl);
}

// 초기화 문구
$(function() {
	// 하트 class 초기화
	clipItems = document.getElementsByClassName('clip');

	for (i = 0; i < clipItems.length; i++) {
		clipValue = JSON.parse(clipItems[i].getAttribute('value'));
		console.log(clipValue);

		if (clipValue == 1) {
			/*clipItems[i].classList.add('active');*/
			$(clipItems[i]).addClass("active")
		}
		else {
			/*clipItems[i].classList.remove('active')';*/
			$(clipItems[i]).removeClass("active")
		}
	}
});

function onClickLike(id, addClip) {
	likeValue = new Number($('#' + id).text());
	likeValue += new Number(addClip);
	// 여기에 AJAX로 DB 업데이트하는 코드 있어야함!!

			$('#' + id).text((likeValue > 0 ? '+' : '') + likeValue);

}

function onClickClip(id, tradeNo) {
	clipValue = JSON.parse($('#' + id).attr('value'));
	if (clipValue == 0) {
		clipValue = 1;
	} else {
		clipValue = 0;
	}
	// 여기에 AJAX로 DB 업데이트하는 코드 있어야함!!
	$.ajax({
		method: 'get',
		url: '/trade/clip?tradeNo=' + tradeNo + '&isClip=' + clipValue,
		contentType: 'application/json',
		dataType: 'json',
		success: (result) => {
			$('#' + id).attr('value', '' + result[0]);
			/*$('#' + id).html(result[1]);*/
			$('#count' + id).html('<p class="text-muted">스크랩 ' + result[1] + '</p>');
			console.dir(result);
			if (result[0] == 1) {
				$('#' + id).addClass("active")
			/*$('#aazz' + id).remove();*/
			/*$('#' + id).append("<div id=" + "aazz" + id + "> + " + result[1] + "<div>");*/
				/*$('#' + id).blur();*/
				/*alert("스크랩에 추가 되었습니다.");*/
			} else {
				$('#' + id).removeClass("active")
				/*$('#aazz' + id).remove();*/
				/*$('#' + id).blur();*/
				/*alert("스크랩에서 제거 되었습니다.");*/
			}
		},
		error: (e) => {
			alert('전송 실패!!');
		}
	});
}