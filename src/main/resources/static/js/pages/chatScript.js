var element = $('.floating-chat');
var myStorage = localStorage;


var commandMap = [
    [ // 메세지 하나 처리하는 자료구조 
        [   // 0 = 처리할 메세지의 리스트
            '처음', '스모어 소개','다시 시작'
        ],
        [   // 1 = 응답 할 내용
            '<li class="other">',
            '안녕하세요. 스모어입니다. <br><br>',
            '원하시는 챗봇 키워드를 <b>클릭!</b> 하시면 간단한 추천 문서를 받아보실 수 있습니다.  ',
            '</li>',
            '<li class="self">',
            '<button onclick="inputMsg(\'사이트\');">',
            '  <span class="btn-text" style="color: white;">🧐 스모어는 어떤 사이트 인가요? </span>',
            '</button>',
            '</li>',
            '<li class="self">',
            '<button onclick="inputMsg(\'안내\');">',
            '  <span class="btn-text" style="color: white;">🔍 카테고리 안내</span>',
            '</button>',
            '</li>',
        ]
    ],
    [ // 메세지 하나 처리하는 자료구조 
        [   // 0 = 처리할 메세지의 리스트
            '안내', '카테고리', '카테고리 안내'
        ],
        [   // 1 = 응답 할 내용
            '<li class="other">',
            '<span class="btn-text" style="color:black"><b> ✔️ 라이프 <br> ✔️ 커뮤니티 <br> ✔️ 정보 <br>✔️ 마이 페이지</b> <br> 원하시는 메뉴를 입력해주세요!',
            '</li>',
        ]
    ],

    [ // 메세지 하나 처리하는 자료구조 
        [   // 0 = 처리할 메세지의 리스트
            '스모어 사이트', '사이트'
        ],
        [   // 1 = 응답 할 내용
            
            '<li class="other">',
            '<span class="btn-text" style="color:black">스모어는 1인가구를 위해 만들어진 사이트 입니다. 1인가구들을 위한 유용한 정보들과 다른 1인가구들과 소통하며 더 멋진 삶을 만들어보실 수 있습니다 !<br><br> ⌛️ 처음으로 돌아가길 원하시면\'다시 시작\'을 입력해 주세요.  ',
            '</li>',
        ]
    ],


    [ // 메세지 하나 처리하는 자료구조 
    [   // 0 = 처리할 메세지의 리스트
        '라이프'
    ],
    [   // 1 = 응답 할 내용
        '<li class="other">',
        '<span class="btn-text" style="color:black"><b>✔️ 중고재능거래 <br> ✔️ 레시피 <br> ✔️ 야외활동</b><br> 원하시는 메뉴를 입력해주세요!',
        '</li>',
    ]
],



[ // 메세지 하나 처리하는 자료구조 
[   // 0 = 처리할 메세지의 리스트
    '정보'
],
[   // 1 = 응답 할 내용
    '<li class="other">',
    '<span class="btn-text" style="color:black"><b>✔️ 공공임대주택 <br> ✔️ 정부지원사업 <br> ✔️ 뉴스</b> <br> 원하시는 메뉴를 입력해주세요!',
    '</li>',
]
],

[ // 메세지 하나 처리하는 자료구조 
[   // 0 = 처리할 메세지의 리스트
    '커뮤니티'
],
[   // 1 = 응답 할 내용
    '<li class="other">',
    '<span class="btn-text" style="color:black">자유/ 건의게시판을 통해 자유롭게 의견을 남기며 다른 1인가구 이웃들과 소통할 수 있습니다. <br><br> ⌛️ 처음으로 돌아가길 원하시면 <b>"다시 시작"</b>을 입력해 주세요.  ',
    '</li>',
]
],

[ // 메세지 하나 처리하는 자료구조 
[   // 0 = 처리할 메세지의 리스트
    '마이페이지','마이 페이지','가계부'
],
[   // 1 = 응답 할 내용
    '<li class="other">',
    '<span class="btn-text" style="color:black">개인정보 수정 및 스크랩 한 내역을 확인할 수 있으며, 가계부  기능을 통해 소비습관을 기록할 수 있습니다. <br><br> ⌛️ 처음으로 돌아가길 원하시면 <b>다시 시작</b>을 입력해 주세요. ', 
    '</li>',
]
],

[ // 메세지 하나 처리하는 자료구조 
[   // 0 = 처리할 메세지의 리스트
    '중고거래','중고/재능거래','중고','재능','중고/재능 거래'
],
[   // 1 = 응답 할 내용
    '<li class="other">',
    '<span class="btn-text" style="color:black">중고 관련 카테고리를 통해 쓰지 않는 물건을 업로드 하여 1인가구 이웃들과 함께 판매하거나 무료로 나눔할 수 있으며, 재능 관련 카테고리를 통해 본인의 재능을 필요한 이웃에게 공유하여 도움을 줄 수 있습니다. 또한 모든 거래 및 나눔은 댓글을  통해 소통할 수 있습니다.  <br><br> ⌛️ 처음으로 돌아가길 원하시면 <b>다시 시작</b>을 입력해 주세요.  ',
    '</li>',
]
],

[ // 메세지 하나 처리하는 자료구조 
[   // 0 = 처리할 메세지의 리스트
    '레시피'
],
[   // 1 = 응답 할 내용
    '<li class="other">',
    '<span class="btn-text" style="color:black">혼자사는 1인가구를 위한 음식별 조리법을 인기재료와 카테고리별로 제공하고 있습니다. 원하는 음식 및 재료를 선택하여 레시피를 찾아볼 수 있으며 추천과 댓글을 통해 다른 1인가구 회원들과 소통할 수 있습니다. <br><br> ⌛️ 처음으로 돌아가길 원하시면 <b>다시 시작</b>을 입력해 주세요.  ',
    '</li>',
]
],


[ // 메세지 하나 처리하는 자료구조 
[   // 0 = 처리할 메세지의 리스트
    '야외 활동','야외활동','산책'
],
[   // 1 = 응답 할 내용
    '<li class="other">',
    '<span class="btn-text" style="color:black">혼자 산책하기 좋은 공원과 산책로를 지역별로 제공하고 있으며 원하는 장소를 스크랩하여  마이페이지에서 한번에 모아볼 수 있습니다.  <br><br> ⌛️ 처음으로 돌아가길 원하시면 <b>다시 시작</b>을 입력해 주세요. ',
    '</li>',
]
],



[ // 메세지 하나 처리하는 자료구조 
[   // 0 = 처리할 메세지의 리스트
    '공공임대주택','공공주택','주택','공공 임대 주택','임대주택','임대 주택'
],
[   // 1 = 응답 할 내용
    '<li class="other">',
    '<span class="btn-text" style="color:black">1인 가구를 위한 임대주택 입주자 모집 공고 정보를 제공하고 있습니다. 공고명 및 지역정보, 주택 유형에 따른 공고를 확인 하실 수 있으며 모집여부 또한 함께 제공하고 있습니다. 공고 정보 클릭시 해당 관련 사이트로 이동하여 더 자세한 정보를 확인하실 수 있습니다.  <br><br> ⌛️ 처음으로 돌아가길 원하시면 <b>다시 시작</b>을 입력해 주세요.  ',
    '</li>',
]
],

[ // 메세지 하나 처리하는 자료구조 
[   // 0 = 처리할 메세지의 리스트
    '정부지원사업','정부 사업','정부','정부 지원 사업'
],
[   // 1 = 응답 할 내용
    '<li class="other">',
    '<span class="btn-text" style="color:black"> 지역별 지자체에서 운영하고 있는 1인가구를 위한 정부 지원 사업 프로그램을 모아볼 수 있습니다. <br><br> ⌛️ 처음으로 돌아가길 원하시면 <b>다시 시작</b>을 입력해 주세요.  ',
    '</li>',
]
],

[ // 메세지 하나 처리하는 자료구조 
[   // 0 = 처리할 메세지의 리스트
    '뉴스'
],
[   // 1 = 응답 할 내용
    '<li class="other">',
    '<span class="btn-text" style="color:black"> 경제, 사회, 생활/문화 카테고리별로 실시간 뉴스 정보를 확인할 수 있으며 해당 기사 클릭시 해당 기사 사이트로 이동하여 자세한 기사를 확인할 수 있습니다.  <br><br> ⌛️ 처음으로 돌아가길 원하시면 <b>다시 시작</b>을 입력해 주세요.  ',
    '</li>',
]
],




]


if (!myStorage.getItem('chatID')) {
    myStorage.setItem('chatID', createUUID());
}

setTimeout(function() {
    element.addClass('enter');
}, 1000);

element.click(openElement);

function openElement() {
    var messages = element.find('.messages');
    var textInput = element.find('.text-box');
    element.find('>i').hide();
    element.addClass('expand');
    element.find('.chat').addClass('enter');
    var strLength = textInput.val().length * 2;
    textInput.keydown(onMetaAndEnter).prop("disabled", false).focus();
    element.off('click', openElement);
    element.find('.header button').click(closeElement);
    element.find('#sendMessage').click(sendNewMessage);
    messages.scrollTop(messages.prop("scrollHeight"));
    inputMsg('처음');
}

function closeElement() {
    element.find('.chat').removeClass('enter').hide();
    element.find('>i').show();
    element.removeClass('expand');
    element.find('.header button').off('click', closeElement);
    element.find('#sendMessage').off('click', sendNewMessage);
    element.find('.text-box').off('keydown', onMetaAndEnter).prop("disabled", true).blur();
    setTimeout(function() {
        element.find('.chat').removeClass('enter').show()
        element.click(openElement);
    }, 500);
}

function createUUID() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

function sendNewMessage() {
    var userInput = $('.text-box');
    var newMessage = userInput.html().replace(/\<div\>|\<br.*?\>/ig, '\n').replace(/\<\/div\>/g, '').trim().replace(/\n/g, '<br>');

    if (!newMessage) return;

    var messagesContainer = $('.messages');

    messagesContainer.append([
        '<li class="self">',
        newMessage,
        '</li>'
    ].join(''));
    
    // clean out old message
    userInput.html('');
    // focus on input
    userInput.focus();

    messagesContainer.finish().animate({
        scrollTop: messagesContainer.prop("scrollHeight")
    }, 250);
        
    inputMsg(newMessage);
}

function onMetaAndEnter(event) {
    if ((event.metaKey || event.ctrlKey) && event.keyCode == 13) {
        sendNewMessage();
    }
}



function inputMsg(msg){
    var userInput = $('.text-box');
    var messagesContainer = $('.messages');
    var array = [];

    for(i = 0; i < commandMap.length; i++){
        for(j = 0; j < commandMap[i][0].length; j++){
            if(commandMap[i][0][j].includes(msg)){
                array = commandMap[i][1];
                break;
            }
        }
    }

    messagesContainer.append(array.join(''));
    userInput.html('');
    userInput.focus();
    messagesContainer.finish().animate({
        scrollTop: messagesContainer.prop("scrollHeight")
    }, 250);
}

function enterkey() {
    if (window.event.keyCode == 13) {

         // 엔터키가 눌렸을 때 실행할 내용
         sendNewMessage();
    }
}
