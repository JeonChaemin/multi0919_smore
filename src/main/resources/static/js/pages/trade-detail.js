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
			$('#aazz' + id).html(result[1]);
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

function updateTime() {

	// 2020년 7월 1일 0시 0분 0초
	const date1 = new Date(2020, 6, 1, 0, 0, 0);
	const date2 = new Date(2020, 6, 1, 2, 30, 4);

	const elapsedMSec = date2.getTime() - date1.getTime(); // 9004000
	const elapsedSec = elapsedMSec / 1000; // 9004
	const elapsedMin = elapsedMSec / 1000 / 60; // 150.0666...
	const elapsedHour = elapsedMSec / 1000 / 60 / 60; // 2.501111...

	document.writeln(elapsedMSec);
	document.writeln(elapsedSec);
	document.writeln(elapsedMin);
	document.writeln(elapsedHour);
}


(function elapsedTime(date) {
	//	const start = document.getElementById("oldDate");
	//		date = trade.createDate.value;
	//		date = "${#dates.format(trade.createDate)};
	const start = new Date(date);
	const end = new Date(); // 현재 날짜

	const diff = (end - start) / 1000; // 경과 시간 구하기 (밀리초 기본으로 빼주기)

	const times = [
		{ name: '년', milliSeconds: 60 * 60 * 24 * 365 },
		{ name: '개월', milliSeconds: 60 * 60 * 24 * 30 },
		{ name: '일', milliSeconds: 60 * 60 * 24 },
		{ name: '시간', milliSeconds: 60 * 60 },
		{ name: '분', milliSeconds: 60 },
	];
	// 아래 코드를 위해서는 (년 ~ 분) 순서여야함
	// 년 단위부터 알맞는 단위 찾기
	for (const value of times) {
		const betweenTime = Math.floor(diff / value.milliSeconds);
		// 큰 단위는 0보다 작은 소수 단위 나옴
		if (betweenTime > 0) {
			return `${betweenTime}${value.name} 전`;
		}
	}
	// 모든 단위가 맞지 않을 시
	return "방금 전";
}());

