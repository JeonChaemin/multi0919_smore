var calendar;
//화면 온로드 시
document.addEventListener('DOMContentLoaded', function () {
    initCalendar();
});

var initCalendar = function() {
    //캘린더가 생성되어 있을 경우 삭제
    if(calendar) {
        document.getElementById('calendar').destroy();
    }
    //캘린더 html 요소
    var calendarEl = document.getElementById('calendar');
    //풀캘린더 인스턴스 생성
    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: '',
            center: '',
            right: ''
        },
        footerToolbar: {
          center: 'prev today next'
        },
        editable: true,
        droppable: true,
        locale: 'ko',
        // 캘린더에 출력되는 데이터
        events : [
          {
            title:'공과금',
            start:'2023-02-16',
            type: '지출',
            amount: '76,000', //info.event.extendedProps["amount"]
            color: "#F9E1E3",
            textColor: "#DA3746"
          },
          {
            title:'생활용품',
            start:'2023-02-13',
            type: '지출',
            amount: '64,000', //info.event.extendedProps["amount"]
            color: "#F9E1E3",
            textColor: "#DA3746"
          },
          {
            title:'월급',
            start:'2023-02-10',
            type: '수입',
            amount: '2,500,000', //info.event.extendedProps["amount"]
            color: "#DAEDE8",
            textColor: "#048565"
          },
        ],
        // events: [
        //     //비동기로 DB데이터 요청하기
        //     $.ajax({
        //         type : "GET",
        //         url  : "/schedule/retrieve",
        //         contentType : "application/json; charset=UTF-8",
        //         //성공 시
        //         success : function(response) {
        //             for (var i=0; i < response.length; i++) {
        //                 res = response[i].json();
        //                 calendar.addEvent({
        //                     title : res['파라미터1(제목)'],
        //                     start : res['파라미터2(날짜)'],
        //                     end   : res['파라미터3(날짜)']
        //                 })
        //             }
        //         }
        //     })
        // ],
        //드롭 완료 시
        eventDrop: function(info) {
            //비동기로 업데이트 요청하기
            $.ajax({
                type : "POST",
                url  : "/schedule/update",
                data : {
                    '파라미터1(제목)' : info.title,
                    '파라미터2(수정된 날짜)' : dateFormat(info.event.start),
                    '파라미터3(기존 날짜)'   : dateFormat(info.oldEvent.start)
                },
                contentType : "application/json; charset=UTF-8",
                dataType : text,
                //성공 시 요소 제거
                success : function(response) {
                    
                }
            })
        },
        //항목 클릭 시
        eventClick: function(info) {
            // if(confirm(info.event.title+" - 삭제 하시겠습니까?")) {
            //     //비동기로 삭제 요청하기
            //     $.ajax({
            //         type : "POST",
            //         url  : "/schedule/delete",
            //         data : {
            //             '파라미터1(제목)' : info.title,
            //             '파라미터2(날짜)' : dateFormat(info.event.start)
            //         },
            //         contentType : "application/json; charset=UTF-8",
            //         dataType : text,
            //         //성공 시 요소 제거
            //         success : function(response) {
            //             alert(info.event.title+" - 삭제 되었습니다.");
            //             info.event.remove();
            //         }
            //     })
            // }
            openModal();
        },

    });
    
    calendar.render();
}

//날짜 포맷(YYYY-MM-DD)
var dateFormat = function(date) {
    return date.toISOString().substr(0,10);
}

//등록 버튼 클릭 이벤트
var regist = function() {
    var title = document.getElementById('아이디1').value;
    var date  = document.getElementById('아이디2').value;
    var amt   = document.getElementById('아이디3').value;
    //비동기로 등록하기
    $.ajax({
        type : "POST",
        url  : "/schedule/insert",
        data : {
            '파라미터1(제목)' : title,
            '파라미터2(날짜)' : date,
            '파라미터3(금액)' : amt
        },
        contentType : "application/json; charset=UTF-8",
        dataType : text,
        //성공 시 캘린더 새로고침
        success : function(response) {
            initCalendar();
        }
    })
}

var singleCategory=new Choices("#choices-single-category", {
    shouldSort: false
});
var singlePart=new Choices("#choices-single-part", {
    shouldSort: false
});
// var singlePart2=new Choices("#choices-single-part2", {
//     shouldSort: false
// });

var singleRange=new Choices("#choices-single-range", {
    shouldSort: false
});
var singleDate=new Choices("#choices-single-date", {
    shouldSort: false
});
var type=new Choices("#choices-single-type", {
    shouldSort: false
});

var singlemCategory=new Choices("#choices-single-mcategory", {
    shouldSort: false
});
var singlemPart=new Choices("#choices-single-mpart", {
    shouldSort: false
});

var openModal = function() {
    document.getElementById("budgetTrigger").click();
}
