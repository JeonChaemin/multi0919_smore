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

function deleteReply(bbrNo, bbNo){
	var url = "/board/replyDel?bbrNo=";
	var requestURL = url + bbrNo +"&bbNo=" + bbNo;
	location.replace(requestURL);
}

function updateReply(bbrNo){
	var updateBtn = document.getElementById("updateBtn" + bbrNo);
	updateBtn.style.display = "none";
	
	var content = document.getElementById("content" + bbrNo);
	content.style.display = "none";
	
	var updateForm = document.getElementById("updateReply" + bbrNo);
	updateForm.style.display = "inline-block";
}

function updBtn(bbrNo, bbNo){
	var newContent = document.getElementById("inputReply" + bbrNo).value;
	if(newContent.trim() === ''){
		alert("내용을 입력해주세요.")
		return;
	}
	
	var requestURL = '/board/replyUpdate?bbrNo=' + bbrNo +'&content=' + newContent + '&bbNo=' + bbNo;
	location.replace(requestURL);
}

function backBtn(bbrNo){
	var updateBtn = document.getElementById("updateBtn" + bbrNo);
	updateBtn.style.display = "inline-block";
	
	var content = document.getElementById("content" + bbrNo);
	content.style.display = "inline-block";
	
	var updateForm = document.getElementById("updateReply" + bbrNo);
	updateForm.style.display = "none";
}