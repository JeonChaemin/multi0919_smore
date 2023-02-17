function deleteReply(replyNo, boardNo){
	var url = "/board/replyDel?replyNo=";
	var requestURL = url + replyNo +"&boardNo=" + boardNo;
	location.replace(requestURL);
}

 // 초기화 문구
$(function(){
    // 하트 class 초기화
    heartItems = document.getElementsByClassName('heart');
    for(i = 0; i < heartItems.length; i++){
        heartValue = JSON.parse(heartItems[i].getAttribute('value'));

        if(heartValue == 1){
            heartItems[i].classList.add('active');
        }else{
            heartItems[i].classList.remove('active');
        }
    }
});

function onClickLike(id, addLike){
    likeValue = new Number($('#'+id).text());
    likeValue += new Number(addLike);
    // 여기에 AJAX로 DB 업데이트하는 코드 있어야함!!

    $('#'+id).text((likeValue > 0 ? '+' : '') + likeValue);
}

function onClickHeart(id, bbNo){
    heartValue = JSON.parse($('#'+id).attr('value'));
    if(heartValue == 0){
    	heartValue = 1;
    }else{
    	heartValue = 0;
    }
    // 여기에 AJAX로 DB 업데이트하는 코드 있어야함!!
    $.ajax({
		method:'get',
		url:'/board/like?bbNo=' + bbNo +'&isLike=' + heartValue ,
		contentType: 'application/json',
		dataType : 'json',
		success: (result) =>{
			$('#'+id).attr('value', ''+result[0]);
			$('#'+id).html('<i class="uil uil-thumbs-up ms-1"></i> 좋아요 '+result[1]);
			console.dir(result);
	        if(result[0] == 1){
	            $('#'+id).addClass("active");
	            $('#'+id).blur();
	        }else{
	            $('#'+id).removeClass("active");
	            $('#'+id).blur();
	        }
		},
		error : (e) => {
			alert('전송 실패!!');
		}
	});
}