<#--

py_header_load_script
py_page_simple_depth
py_page_top_bar
py_page_footer

py_page_navigation


py_jsdatatable
py_jsevent_prevent
py_jsnoti
py_jschangecontent
py_bstrap_modal
-->


<#macro py_header_load_script >

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>PlyMind:PLay in Your Mind</title>

    <!-- Bootstrap -->
    <link href="${Request.svcResPath}css/bootstrap.min.css" rel="stylesheet">
    <!-- Template style.css -->
    <link rel="stylesheet" type="text/css" href="${Request.svcResPath}css/style.css">
    <link rel="stylesheet" type="text/css" href="${Request.svcResPath}css/owl.carousel.css">
    <link rel="stylesheet" type="text/css" href="${Request.svcResPath}css/owl.theme.css">
    <link rel="stylesheet" type="text/css" href="${Request.svcResPath}css/owl.transitions.css">
    <#--<link rel="stylesheet" type="text/css" href="${Request.svcResPath}css/bootstrap-select.min.css">-->
    <link rel="stylesheet" type="text/css" href="${Request.svcResPath}plymind/plymind.css">

    <!-- Font used in template -->
    <link href='https://fonts.googleapis.com/css?family=Montserrat:400,700' rel='stylesheet' type='text/css'>
    <link href='https://fonts.googleapis.com/css?family=Istok+Web:400,400italic,700,700italic' rel='stylesheet' type='text/css'>
    <!--font awesome icon -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <!-- favicon icon -->
    <link rel="shortcut icon" href="${Request.svcResPath}images/favicon.ico" type="image/x-icon">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <script src="${Request.resPath}js/libs/jquery-2.1.1.min.js"></script>
    <script src="${Request.resPath}js/libs/jquery-ui-1.10.3.min.js"></script>

<#-- underscore.js -->
    <script src="${Request.resPath}js/plugin/underscore-js/underscore-min.js"></script>

<#-- ckras에 필요한 것들만 먼저 로드 -->
    <script src="${Request.resPath}js/ckpush/my.preload.js"></script>

<#-- ras js file. ras js 파일은  -->
    <script src="${Request.resPath}js/ckpush/ras/socket.io.js"></script>
    <script src="${Request.resPath}js/ckpush/ras/ras-config.js"></script>
    <script src="${Request.resPath}js/ckpush/ras/ras-model.js"></script>
    <script src="${Request.resPath}js/ckpush/ras/ras-user-ui.js"></script>
    <script src="${Request.resPath}js/ckpush/ras/ras-user-console.js"></script>


<#-- IMPORTANT: APP CONFIG -->
    <script src="${Request.resPath}js/app.config.js"></script>

<#-- JS TOUCH : include this plugin for mobile drag / drop touch events-->
    <script src="${Request.resPath}js/plugin/jquery-touch/jquery.ui.touch-punch.min.js"></script>

<#-- BOOTSTRAP JS -->
    <script src="${Request.resPath}js/bootstrap/bootstrap.min.js"></script>

<#-- CUSTOM NOTIFICATION -->
    <script src="${Request.resPath}js/notification/SmartNotification.min.js"></script>

<#-- JARVIS WIDGETS -->
    <script src="${Request.resPath}js/smartwidgets/jarvis.widget.min.js"></script>

<#-- EASY PIE CHARTS -->
    <script src="${Request.resPath}js/plugin/easy-pie-chart/jquery.easy-pie-chart.min.js"></script>

<#-- SPARKLINES -->
    <script src="${Request.resPath}js/plugin/sparkline/jquery.sparkline.min.js"></script>

<#-- JQUERY VALIDATE -->
    <script src="${Request.resPath}js/plugin/jquery-validate/jquery.validate.min.js"></script>

<#-- JQUERY MASKED INPUT -->
    <script src="${Request.resPath}js/plugin/masked-input/jquery.maskedinput.min.js"></script>

<#-- JQUERY SELECT2 INPUT -->
    <script src="${Request.resPath}js/plugin/select2/select2.min.js"></script>

<#-- JQUERY UI + Bootstrap Slider -->
    <script src="${Request.resPath}js/plugin/bootstrap-slider/bootstrap-slider.min.js"></script>

<#-- browser msie issue fix -->
    <script src="${Request.resPath}js/plugin/msie-fix/jquery.mb.browser.min.js"></script>

<#-- FastClick: For mobile devices: you can disable this in app.js -->
    <script src="${Request.resPath}js/plugin/fastclick/fastclick.min.js"></script>

    <!--[if IE 8]>
    <h1>지금 사용하는 브라우저는 지원하지 않는 브라우저 입니다. www.microsoft.com/download 에 방문 해서 브라우져 업데이트를 하거나, www.google.com/chrome 에서 신규 브라우저를 설치 하세요.</h1>
    <![endif]-->

<#-- Demo purpose only
<script src="js/demo.min.js"></script> -->

<#-- =============================
     다음 순서를 꼭 지켜야 한다.
     app.min.js
     my.js
     overwrite.js
     ============================= -->

<#-- MAIN APP JS FILE -->
<#--
    <script src="${Request.resPath}js/app.min.js"></script>
-->

<#-- custom javascript -->
    <script src="${Request.resPath}js/ckpush/my.js"></script>

<#-- Smart Admin 의 기본 값들을 재정의 한다 -->
    <script src="${Request.resPath}js/ckpush/overwrite.js"></script>

<#-- ENHANCEMENT PLUGINS : NOT A REQUIREMENT -->
<#-- Voice command : plugin -->
    <script src="${Request.resPath}js/speech/voicecommand.min.js"></script>


    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<#--<script src="${Request.svcResPath}js/jquery.min.js"></script>-->
    <!-- Include all compiled plugins (below), or include individual files as needed -->
<#--<script src="${Request.svcResPath}js/bootstrap.min.js"></script>-->
    <script src="${Request.svcResPath}js/nav.js"></script>
<#--<script src="${Request.svcResPath}js/offset.js"></script>-->
    <script src="${Request.svcResPath}js/jquery.sticky.js"></script>
    <script src="${Request.svcResPath}js/header-sticky.js"></script>


<#--<script type="text/javascript" src="${Request.svcResPath}js/bootstrap-select.js"></script>
<script src="${Request.svcResPath}js/owl.carousel.min.js"></script>
<script type="text/javascript" src="js/slider.js"></script>
<script type="text/javascript" src="js/testimonial.js"></script>-->


</head>
<body>

</#macro>


<#--
    페이지 상단의 간단 페이지 depth 를 출력 한다.
    icon    : 페이지 depth 문자 앞에 출력될 Font Awesome 아이콘 class 명
    parent  : 이전 메뉴의 타이틀
    current : 현재 메뉴의 타이틀
-->
<#macro py_page_simple_depth icon="" parent="" current="" canMoveParent=false>
<div class="tp-page-head"><!-- page header -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="page-header">
                    <h1>${current}</h1>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- /.page header -->
<div class="tp-breadcrumb">
    <div class="container">
        <div class="row">
            <div class="col-md-8">
                <ol class="breadcrumb">
                    <li><a href="#">Home</a></li>
                    <#if parent != "">
                        <#if canMoveParent == true>
                            <li>${parent}</li>
                        <#else>
                            <li>${parent}</li>
                        </#if>
                        <li class="active">${current}</li>
                    <#else>
                    <#--${current}-->
                    <li class="active">${current}</li>
                    </#if>
                </ol>
            </div>
        </div>
    </div>
</div>

<div id="noti-area" class="col-md-12" style="display: none; position: fixed; z-index: 100000;">
    <div id="noti" class="col-md-6 alert fade" role="alert" style="float:right;">
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">x</span></button>
        <p></p>
    </div>
</div>

<#--<div id="noti-area" class="col-md-12" style="display: none; position: relative; z-index: 100000;">
    <div id="noti" class="col-md-6 alert fade" role="alert" style="float:right;">
    &lt;#&ndash;<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">x</span></button>&ndash;&gt;
        <p></p>
    </div>
</div>-->

</#macro>

<#--
    loginSession    : 로그인 세션 값
-->
<#macro py_page_top_bar loginSession=false>
<div class="top-bar">
    <div class="container">
        <div class="row">
            <div class="col-md-6 top-message">
            <p>Play in Your Mind</p>
            </div>
            <div class="col-md-6 top-links">
                <ul class="listnone">
                    <#if Request.loginSession == true>
                        <li style="font-size: 11px;color:#fff;">${Request.loginId} 님. 환영합니다.</li>
                        <li><a href="${Request.contextPath}/user/open/logout">Log out</a></li>
                        <#--<li><a href="${Request.contextPath}/mypage/main">마이페이지</a></li>-->
                    <#else>
                        <li><a href="${Request.contextPath}/user/open/login">Log in</a></li>
                        <li><a href="${Request.contextPath}/user/open/register">회원가입</a></li>
                    </#if>
                </ul>
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro py_page_navigation >
<div class="tp-nav" id="headersticky"><!-- navigation start -->
    <div class="container">
        <nav class="navbar navbar-default navbar-static-top">

            <!-- Brand and toggle get grouped for better mobile display -->

            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false"> <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
                <a class="navbar-brand" href="${Request.contextPath}/user"><img src="${Request.svcResPath}images/logo.png" alt="PlyMind" class="img-responsive"></a> </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">기관 소개 <span class="fa fa-angle-down"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="${Request.contextPath}/user/open/company_introduce">기관 소개 </a></li>
                            <li><a href="${Request.contextPath}/user/open/company_counsel">상담사 소개</a></li>
                            <li><a href="${Request.contextPath}/user/open/company_super">슈퍼바이저 </a></li>
                        </ul>
                    </li>
                    <li class="active"><a href="${Request.contextPath}/user/open/about">상담안내 </a></li>
                    <#--
                    <#if Request.loginSession == true>
                    <li><a href="${Request.contextPath}/user/pretesting/addForm">인테이크 </a></li>
                    </#if>
                    -->
                    <li><a href="${Request.contextPath}/user/product/product_list">상담비용 </a></li>

                    <#if Request.loginSession == true>

                        <li class="dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">공지/FAQ <span class="fa fa-angle-down"></span></a>
                            <ul class="dropdown-menu">
                                <li><a href="${Request.contextPath}/user/notice/list">공지사항/이벤트 </a></li>
                                <li><a href="${Request.contextPath}/user/faq/list">FAQ </a></li>
                                <li><a href="${Request.contextPath}/user/question/list">문의게시판 </a></li>
                            </ul>
                        </li>
                    <#else>
                        <li><a href="${Request.contextPath}/user/notice/list">공지사항/이벤트 </a></li>
                    </#if>
                    <li class="dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">비공개 게시판 <span class="fa fa-angle-down"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="${Request.contextPath}/user/secret/list">비밀공간 </a></li>
                            <li><a href="${Request.contextPath}/user/theme/list">테마공간 </a></li>
                        </ul>
                    </li>
                    <#if Request.loginSession == true>
                    <!-- Mega Dropdown -->
                    <li class="dropdown mega-dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown">마이페이지 <span class="fa fa-angle-down"></span></a>
                        <ul class="dropdown-menu mega-dropdown-menu row">
                            <li class="col-sm-2 mega-menu-link">
                                <ul>
                                    <li class="dropdown-header"><i class="fa fa-files-o"></i> 신청하기</li>
                                    <li><a href="${Request.contextPath}/user/product/adviceAddForm/0/0/0/0">심리상담 예약</a></li>
                                    <li><a href="${Request.contextPath}/user/product/checkupAddForm/0">심리검사 신청</a></li>
                                </ul>
                            </li>
                            <li class="col-sm-3 mega-menu-link">
                                <ul>
                                    <li class="dropdown-header"><i class="fa fa-files-o"></i> 나의 상담 정보</li>
                                    <li><a href="${Request.contextPath}/user/myadvice/progress_list">현재 진행중인 상담</a></li>
                                    <li><a href="${Request.contextPath}/user/mycheckup/list">심리검사</a></li>
                                    <li><a href="${Request.contextPath}/user/mycomplete/list">상담완료내역</a></li>
                                </ul>
                            </li>
                            <li class="col-sm-3 mega-menu-link">
                                <ul>
                                    <li class="dropdown-header"><i class="fa fa-files-o"></i> 나의 예약/결제 정보</li>
                                    <li><a href="${Request.contextPath}/user/appointment/appointment_list">예약 변경</a></li>
                                    <li><a href="${Request.contextPath}/user/payment/payment_list">결제 내역</a></li>
                                    <li><a href="${Request.contextPath}/user/payment/cancel_list">취소 내역</a></li>
                                </ul>
                            </li>
                            <li class="col-sm-2 mega-menu-link">
                                <ul>
                                    <li class="dropdown-header"><i class="fa fa-user"></i> 나의 활동</li>
                                    <li><a href="${Request.contextPath}/user/one/add">1:1 문의하기</a></li>
                                    <li><a href="${Request.contextPath}/user/one/list">1:1 답변확인</a></li>
                                </ul>
                            </li>
                            <li class="col-sm-2 mega-menu-link">
                                <ul>
                                    <li class="dropdown-header"><i class="fa fa-user"></i> 나의 정보</li>
                                    <li><a href="${Request.contextPath}/user/mydata/modify">나의 정보 변경</a></li>
                                    <li><a href="${Request.contextPath}/user/mydata/drop">회원 탈퇴</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    </#if>

                </ul>
            </div>

            <!-- /.navbar-collapse -->
        </nav>
    </div>
    <!-- /.container-fluid -->
</div><!-- navigation end -->
</#macro>

<#macro py_popup_layer>
<div class="dim-layer">
    <div class="dimBg"></div>
    <div class="pop-layer">
        <div class="pop-container">
            <div class="pop-conts">
                <p id="pop-message" class="ctxt mb20"></p>
               <#-- <div class="btn-r">
                    <a href="#" class="btn-layerClose">Close</a>
                </div>-->
                <div class="btn-r">
                    <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function popup_layer() {

        var $el = $('.pop_layer');        //레이어의 id를 $el 변수에 저장

        var $elWidth = ~~($el.outerWidth()),
                $elHeight = ~~($el.outerHeight()),
                docWidth = $(document).width(),
                docHeight = $(document).height();

        // 화면의 중앙에 레이어를 띄운다.
        if ($elHeight < docHeight || $elWidth < docWidth) {
            $el.css({
                marginTop: -$elHeight / 2,
                marginLeft: -$elWidth / 2
            })
        } else {
            $el.css({top: 0, left: 0});
        }
        $('.dim-layer').css('display','block');
        return;
    }

    $('.btn-r').click(function(){
        $('.dim-layer').css('display', 'none');
        return false;
    });
</script>
</#macro>

<#macro py_page_footer >

<div class="footer"><!-- footer start -->
</div>
<!-- /.footer -->

<div class="tiny-footer">
    <div class="container">
        <div class="row">
            <div class="col-md-12">

                주소:서울 강남구 논현로 509 8층 806호 플리마인드  TEL:02.564.5575  Mail:admin@plymind.com<br>
                Copyright © PLYMIND 2016. All Rights Reserved
            </div>
        </div>
    </div>
</div>

</body>
</html>

</#macro>

<#--
    jquery datatable 울 출력 한다.(테이블 헤더 위에 검색 항목이 추가된 테이블 형태)
    list_app.ftl 을 참고 하면 된다.

    [nested]
    nested "buttons"    : 버튼이 존재하면 존재해야 하는 nested. oTableTools.aButtons 값임. array.
    nested "order"      : 최초의 ordering. [[1, 'desc']] 와 같은 형태가 되어야 함. array.
    nested "columns"    : 테이블의 column 정보. aoColumns 값임. array

    [parameter]
    listId              : 테이블의 id
    //useCustomButton     : 테이블 상단에 버튼을 사용하는지 여부.
    url                 : ajax url. url 은 't/'로 끝나야 하고 자동으로 tid 를 붙인다.
    //useCheckBox         : 테이블 첫번째 줄에 checkbox 를 사용하는지 여부
    //useSearchText       : 테이블 상단에 텍스트 검색을 사용하는지 여부
    //useSearchSelect     : 테이블 상단에 실랙트 박스 검색을 사용하는지 여부

    searchTooltip       : 좌측 상단 search 의 tooltip. 만일 값이 없으면 tooltip 없다.
-->
<#--<#macro py_jsdatatable listId="datatable_list" url="" tableVarName="otable" searchTooltip="" useCustomButton=false
        useSearchText=false useSearchSelect=false >-->
<#macro py_jsdatatable listId="datatable_list" url="" searchTooltip="" tableVarName="otable">
    <#--
    // DOM Position key index //

    l - Length changing (dropdown)
    f - Filtering input (search)
    t - The Table! (datatable)
    i - Information (records)
    p - Pagination (paging)
    r - pRocessing
    < and > - div elements
    <"#id" and > - div with an id
    <"class" and > - div with a class
    <"#id.class" and > - div with an id and class

    Also see: http://legacy.datatables.net/usage/features
    -->
    <#--my.fn.setDatatableSearchValue('${listId}');-->

    var responsiveHelper_list = undefined;
    var breakpointDefinition = {
        tablet : 1024,
        phone : 480
    };

    var ${tableVarName} = $('#${listId}').DataTable({
    <#--
        "bFilter": false,
        "bInfo": false,
        "bLengthChange": false
        "bAutoWidth": false,
        "bPaginate": false,
        "bStateSave": true // saves sort state using localStorage
    -->

        <#--<#if useCustomButton>
        sDom: "<'dt-toolbar'<'col-xs-12 col-sm-3 hidden-xs'T><'font-sm txt-color-greenLight col-sm-6 col-xs-12 hidden-xs'r><'col-sm-3 col-xs-12 hidden-xs'l>>" +
        <#else>
        sDom: "<'dt-toolbar'<'col-xs-12 col-sm-3 hidden-xs'><'font-sm txt-color-greenLight col-sm-6 col-xs-12 hidden-xs'r><'col-sm-3 col-xs-12 hidden-xs'l>>" +
        </#if>-->

<#--

        sDom: "" +
        "t" +
        "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs small'i><'col-xs-12 col-sm-6 text-right'p>>",
-->

        sDom: "<'dt-toolbar'<'col-xs-12 col-sm-4 hidden-xs'f><'font-sm txt-color-greenLight col-sm-4 col-xs-12 hidden-xs'r><'col-sm-4 col-xs-12 hidden-xs text-right'l>>" +
        "t" +
        "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs small'i><'col-xs-12 col-sm-6 text-right'p>>",
        autoWidth: true,
        preDrawCallback: function() {
        <#-- Initialize the responsive datatables helper once. -->
            if (!responsiveHelper_list) {
                responsiveHelper_list = new ResponsiveDatatablesHelper($('#${listId}'), breakpointDefinition);
            }
        },
        rowCallback: function(nRow) {
            responsiveHelper_list.createExpandIcon(nRow);
        },
        drawCallback: function(oSettings) {
            responsiveHelper_list.respond();
        },
        processing: true,
        serverSide: true,
        ajax: {
            url: '${url}'+ $.now(),
            type: 'POST'
        },
        <#-- saves sort state using localStorage -->
        bStateSave: true,
        <#-- Define the starting point for data display when using DataTables with pagination -->
        iDisplayStart: 0,
        <#-- Number of rows to display on a single page when using pagination -->
        iDisplayLength: 25,
        aLengthMenu: [25, 50, 75, 100],
            language: {
                paginate: {
                first: '처음',
                last: '마지막',
                next: '다음',
                previous : '이전'
            },
            zeroRecords: '검색된 데이터가 없습니다.',
            emptyTable: '데이터가 없습니다.',
            info: '필터링 된 _TOTAL_개에서 _START_번에서 _END_번까지 리스트입니다.',
            infoFiltered : '(총 _MAX_개의 데이터가 있습니다.)',
            infoEmpty: '필터링 된 0개에서 0번에서 0번까지 리스트입니다.',
            processing: '데이터를 가져오는 중입니다.',
            loadingRecords: '데이터를 로딩 중입니다.'
        },
        aaSorting: <#nested "order">,
        aoColumns: <#nested "columns">
    });

    $("#${listId} tbody").on("click", ".datatable-row-detail-view", function(e) {
        <@py_jsevent_prevent />
        var $this = $(this), move = $this.data('move'), nav = $this.data('nav');
        my.fn.pmove(move, nav);
    });

    <#-- 통합 search input 에 tooltip 추가 -->
<#-- 통합 search input 에 tooltip 추가 -->
    <#if searchTooltip != "">
    $('#${listId}_filter').find('input[type=search]').attr({
        'rel': 'tooltip',
        'data-placement': 'top',
        'data-original-title': '${searchTooltip}'}).tooltip();
    </#if>

</#macro>

<#--
    event prevent 를 위한 event 기본 처리. click 등등의 이벤트 함수 내에서 사용
    eventname       : event object name
-->
<#macro py_jsevent_prevent eventname="e">
    ${eventname}.preventDefault();
    ${eventname}.stopPropagation();
</#macro>

<#--
    toast 메시지를 보여 준다.(javascript 코드)
    title       : toast 메시지 타이틀
    content     : toast 메시지 본문. ajax 일때는 data.ck_message 를 사용하면 됨(js로 값이 변경됨)
    contenttype : var 라면 content 가 javascript 변수이고, 아니라면 스트링 값임
    ---------------succss, info, danger
    timeout     : toast 메시지를 보여줄 시간. millisec 단위
-->

<#macro py_jsnoti title="" content="" contenttype="var" boxtype="info" timeout=2000>
    <#assign color = "#C46A69">
    <#assign icon = "fa-warning">
    <#if boxtype != "warn">
        <#assign color="#296191">
        <#assign icon = "fa-check">
    </#if>

    $("#noti-area").show();

    $("#noti").addClass("alert-${boxtype}");
    $("#noti").removeClass("out");
    $("#noti").addClass("in");


    <#if contenttype == 'var'>
        var content = ${content};
        $("#noti p").html("${title}" +", "+ content);
    <#else>
        $("#noti p").html("${title}" +", "+ "${content}");
    </#if>
    <#--setTimeout(function(){
        $("#noti-area").hide();
    },${timeout});-->
<#--
$.smallBox({
title : '${title}',
    <#if contenttype == 'var'>
    content : '<i class="fa ${icon}"></i> <i>'+${content}+'</i>',
    color : '${color}',
    <#else>
    content : '<i class="fa ${icon}"></i> <i>${content}</i>',
    color : '${color}',
    </#if>
    <#if timeout gt 0>
    timeout : ${timeout}
    </#if>
});
-->
</#macro>
<#--
<#macro py_jschangecontent moveuri="" navuri="" isVarMove=false isVarNav=false>
    <#if navuri?? && navuri != ''>
    $(document).trigger('changecontent', [
        <#if isVarMove> ${moveuri} <#else> '${moveuri}' </#if>,
        <#if isVarNav> ${navuri} <#else> '${navuri}' </#if>
    ]);
    <#else>
    $(document).trigger('changecontent', [
        <#if isVarMove> ${moveuri} <#else> '${moveuri}' </#if>
    ]);
    </#if>
</#macro>
-->

<#--
    모달 윈도우를 띄운다. 모달에 포함되는 내용은 nested로 변경 가능 하다.

    [nested]
    modal 의 body에 들어가는 내용

    [parameter]
    modalId         : modal 의 id
    formId          : modal 의 form id
    title           : modal 의 타이틀 문자열
    okButtonLabel   : modal 의 ok 버튼 문자열

-->
<#macro py_bstrap_modal modalId="normal-remote-modal" formId="normal-remote-form" title="Title" okButtonCheck=false okButtonLabel="네" okButtonId="modal-ok">
<form id="${formId}">
    <div class="modal fade" id="${modalId}" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">${title}</h4>
                </div>
                <div class="modal-body">
                    <#nested>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                    <#if okButtonCheck == true>
                    <button id="${okButtonId}" type="button" class="btn btn-primary" data-dismiss="modal">${okButtonLabel}</button>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</form>
</#macro>

<#macro py_jsajax_json_header request="request">
    ${request}.setRequestHeader('Accept', 'application/json;charset=UTF-8');
    ${request}.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
</#macro>

<#macro py_jsajax_fail_break errcdfield="data.ck_error" errmsgfield="data.ck_message" reasonfield="data.ck_reason" title="" xhr="xhr">
    if(${errcdfield} != my.data.ajaxSuccessCode) {
        console.warn('ajax return error code['+${errcdfield}+'], msg['+${errmsgfield}+']');
        if(${xhr}) console.warn(${xhr});

        <#-- 각각의 항목에 에러 메시지를 보여 준다 -->
        if(!_.isNull(${reasonfield}) && !_.isNaN(${reasonfield}) &&
        !_.isUndefined(${reasonfield}) && !_.isEmpty(${reasonfield})) {
        _.each(_.keys(${reasonfield}), function(key){
            if(_.has(${reasonfield}, key)) {
                $('#errmsg-'+key).text(${reasonfield}[key]);
                $('#errid-'+key).show();
            }
        });
    }

<#-- notification 창을 잠깐 보여 준다 -->
    <@py_jsnoti title="${title!'실패'}" content="${errmsgfield}" />

    <#nested>
return;
}
</#macro>
<#--
    플리마인드 사용자 페이지 에서 데이터 등록/수정 등등을 위한 ajax 요청을 한다.(javascript 로직)
    submituri       : request 할 uri
    moveuri         : request 완료 후 완전 성공했을때 이동할 페이지. 만일 이동하지 않으면 empty string 을 사용하면 됨(값을 주지 않으면 됨)
    reqdata         : request 할 request data variable name
    method          : request method
    errortitle      : request 실패 했을때 보여줄 toast 메시지의 제목
-->
<#macro py_jsajax_request submituri="" moveuri="" reqdata="reqData" method="POST" errortitle="" useTid=true>
    $.ajax({
        type: '${method!"POST"}',
        url: '${submituri}'<#if useTid> + $.now()</#if>,
            <#if reqdata?? && reqdata != ''>
            data: JSON.stringify(${reqdata!'reqData'}),
            </#if>
        dataType: 'json',
        //csrf: false,
        beforeSend: function(request) {
            <@py_jsajax_json_header />
        },
        success: function(data, status, xhr) {
        <#-- 실패하면 바로 리턴 된다 -->
            <@py_jsajax_fail_break title="${errortitle!'실패'}" />

        <#-- 페이지 이동은 complete 에서 한다 -->
            <#nested "success_job">
        },
        error: function(xhr, status, error) {
        console.error('error ajax request');
        console.error(status);
        console.error(error);

            <@py_jsnoti title="${errortitle!'실패'}" content="네트웍 오류 입니다." contenttype="string" boxtype="danger" timeout=2000 />
        },
        complete: function(xhr, status) {
            if(xhr && xhr.responseJSON && xhr.responseJSON.ck_error == my.data.ajaxSuccessCode) {
                <#if moveuri?? && moveuri != ''>
                <#-- ajax 완전 성공 하면 페이지 이동 한다 -->
                    <#--<@jschangecontent moveuri="${moveuri}" />-->
                    my.fn.pmove('${moveuri}');
                <#else>
                    <#nested "common_job">
                </#if>
            } else {
            <#-- 공통 작업을 처리 한다 -->
                <#nested "common_job">
            }
        }
    });
</#macro>

<#--
    jquery datatable 울 출력 한다.(테이블 헤더 위에 검색 항목이 추가된 테이블 형태)
    list_app.ftl 을 참고 하면 된다.

    [nested]
    nested "buttons"    : 버튼이 존재하면 존재해야 하는 nested. oTableTools.aButtons 값임. array.
    nested "order"      : 최초의 ordering. [[1, 'desc']] 와 같은 형태가 되어야 함. array.
    nested "columns"    : 테이블의 column 정보. aoColumns 값임. array

    [parameter]
    listId              : 테이블의 id
    //useCustomButton     : 테이블 상단에 버튼을 사용하는지 여부.
    url                 : ajax url. url 은 't/'로 끝나야 하고 자동으로 tid 를 붙인다.
    //useCheckBox         : 테이블 첫번째 줄에 checkbox 를 사용하는지 여부
    //useSearchText       : 테이블 상단에 텍스트 검색을 사용하는지 여부
    //useSearchSelect     : 테이블 상단에 실랙트 박스 검색을 사용하는지 여부

    searchTooltip       : 좌측 상단 search 의 tooltip. 만일 값이 없으면 tooltip 없다.
-->
<#--<#macro py_jsdatatable listId="datatable_list" url="" tableVarName="otable" searchTooltip="" useCustomButton=false
        useSearchText=false useSearchSelect=false >-->
<#macro py_jsdatatable_nosearch listId="datatable_list" url="" tableVarName="otable" isSearch=true>
<#--
// DOM Position key index //

l - Length changing (dropdown)
f - Filtering input (search)
t - The Table! (datatable)
i - Information (records)
p - Pagination (paging)
r - pRocessing
< and > - div elements
<"#id" and > - div with an id
<"class" and > - div with a class
<"#id.class" and > - div with an id and class

Also see: http://legacy.datatables.net/usage/features
-->
<#--my.fn.setDatatableSearchValue('${listId}');-->

var responsiveHelper_list = undefined;
    var breakpointDefinition = {
    tablet : 1024,
    phone : 480
};

var ${tableVarName} = $('#${listId}').DataTable({
<#--
    "bFilter": false,
    "bInfo": false,
    "bLengthChange": false
    "bAutoWidth": false,
    "bPaginate": false,
    "bStateSave": true // saves sort state using localStorage
-->

<#--<#if useCustomButton>
sDom: "<'dt-toolbar'<'col-xs-12 col-sm-3 hidden-xs'T><'font-sm txt-color-greenLight col-sm-6 col-xs-12 hidden-xs'r><'col-sm-3 col-xs-12 hidden-xs'l>>" +
<#else>
sDom: "<'dt-toolbar'<'col-xs-12 col-sm-3 hidden-xs'><'font-sm txt-color-greenLight col-sm-6 col-xs-12 hidden-xs'r><'col-sm-3 col-xs-12 hidden-xs'l>>" +
</#if>-->



        sDom: "" +
        "t" +
        "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs small'i><'col-xs-12 col-sm-6 text-right'p>>",



    autoWidth: true,
    preDrawCallback: function() {
        <#-- Initialize the responsive datatables helper once. -->
        if (!responsiveHelper_list) {
            responsiveHelper_list = new ResponsiveDatatablesHelper($('#${listId}'), breakpointDefinition);
        }
    },
    rowCallback: function(nRow) {
        responsiveHelper_list.createExpandIcon(nRow);
    },
    drawCallback: function(oSettings) {
        responsiveHelper_list.respond();
    },
    processing: true,
    serverSide: true,
    ajax: {
        url: '${url}'+ $.now(),
        type: 'POST'
    },
    <#-- saves sort state using localStorage -->
    bStateSave: true,
    <#-- Define the starting point for data display when using DataTables with pagination -->
    iDisplayStart: 0,
    <#-- Number of rows to display on a single page when using pagination -->
    iDisplayLength: 25,
    aLengthMenu: [25, 50, 75, 100],
    language: {
        paginate: {
            first: '처음',
            last: '마지막',
            next: '다음',
            previous : '이전'
        },
        zeroRecords: '검색된 데이터가 없습니다.',
        emptyTable: '데이터가 없습니다.',
        info: '필터링 된 _TOTAL_개에서 _START_번에서 _END_번까지 리스트입니다.',
        infoFiltered : '(총 _MAX_개의 데이터가 있습니다.)',
        infoEmpty: '필터링 된 0개에서 0번에서 0번까지 리스트입니다.',
        processing: '데이터를 가져오는 중입니다.',
        loadingRecords: '데이터를 로딩 중입니다.'
    },
    aaSorting: <#nested "order">,
    aoColumns: <#nested "columns">
    });

$("#${listId} tbody").on("click", ".datatable-row-detail-view", function(e) {
    <@py_jsevent_prevent />
var $this = $(this), move = $this.data('move'), nav = $this.data('nav');
my.fn.pmove(move, nav);
});

</#macro>

<#macro bstrap_popup_tag_modal modalId="target-tag-remote-modal" url="">
<div class="modal fade" id="${modalId}" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">검사지 파일 검색</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <fieldset class="smart-form">
                            <section>
                                <label class="label">검사지 파일</label>
                                <label for="file" class="input input-file">
                                    <div id="file_list" class="select2" style="width:100%;height:30px;"></div>
                                </label>
                            </section>
                        </fieldset>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button id="btn_file_add" type="button" class="btn btn-default" data-dismiss="modal">추가</button>
            </div>
        </div>
    </div>
</div>
</#macro>

<#macro jsselect2 id="" placeholder="" uniq="" showColumn="" uri="" miniumLength=3 usingTag=false createSearchChoice="" initSelection="">
$('#${id}').select2({
        <#if usingTag>
            tags: true,
        </#if>
        <#if createSearchChoice != "">
            createSearchChoice: ${createSearchChoice},
        </#if>
        language: 'ko',
        placeholder: '${placeholder}',
        minimumInputLength: ${miniumLength},
        id: function(data) { return data.${uniq};
    },
    ajax: {
        url: '${uri}' + $.now(),
        type: 'POST',
        dataType: 'json',
        //quietMillis: 250,
        data: function(term, page) {
            var offset = (page - 1) * my.data.select2PageRows;
            var limit = my.data.select2PageRows;
            return {query: term, offset:offset, limit:limit};
        },
        results: function(data, page) {
            var more = (page * my.data.select2PageRows) < data.recordsTotal;
            return {results: data.list, more:more};
        }
    },
    <#if initSelection != "">
    initSelection: ${initSelection},
    </#if>
    <#if showColumn == "">
    formatResult: function (item) { return item.${uniq}; },
    formatSelection: function (item) { return item.${uniq}; }
    <#else>
    formatResult: function (item) { return item.${showColumn}; },
    formatSelection: function (item) { return item.${showColumn}; }
    </#if>
});

<#-- 한번 건드려 주지 않으니 초기값 넣는 것이 나오지 않는다. val을 한번 건드려 주자 -->
    <#if initSelection != "">
    $('#${id}').select2('val', []);
    </#if>

</#macro>



<#--
    파일 업로드를 위한 model 을 추가 한다.

    [parameter]
    modalId         : file uploader modal id
    formId          : file uploader form id
-->
<#macro py_bstrap_file_uploader_modal modalId="file-uploader-remote-modal" formId="file-uploader-form" url="">
<form id="${formId}" action="${url}" method="POST" enctype="multipart/form-data">
    <div class="modal fade" id="${modalId}" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">파일 업로드</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12">
                            <fieldset class="smart-form-plymind">
                                <section>
                                    <label class="label">파일 선택</label>
                                    <label for="file" class="input input-file">
                                        <div class="button"><input type="file" name="attach_file" onchange="this.parentNode.nextSibling.value = this.value">브라우저</div><input type="text" placeholder="파일을 선택하세요" readonly="">
                                    </label>
                                </section>
                                <section>
                                    <label class="label">파일 설명</label>
                                    <label class="textarea">
                                        <textarea rows="3" name="file_comment" class="custom-scroll"></textarea>
                                    </label>
                                </section>
                            </fieldset>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                    <input type="submit" value="업로드" class="btn btn-primary">
                </div>
            </div>
        </div>
    </div>
</form>
</#macro>


<#macro py_bstrap_modal_access_terms modalId="normal-remote-modal" formId="normal-remote-form" title="Title" okButtonCheck=false okButtonLabel="네" okButtonId="modal-ok">
<form id="${formId}">
    <div class="modal fade" id="${modalId}" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">${title}</h4>
                </div>
                <div class="modal-body">
                    <#--<h3>플리마인드 이용약관</h3>-->
<textarea rows="10" cols="60" autofocus>
﻿플리마인드 이용약관

◎ 제 1 장 총 칙

제 1조(목적)
본 약관은 플리마인드(이하 “기관”)가 운영하는 온라인 심리상담 서비스인 온라인 상담을 이용하는 회원과 기관 혹은 회사 간의 의무, 권리, 책임사항 및 회원의 서비스 이용조건 및 절차 등 기본적인 사항을 규정하여 회원의 권익을 보호함을 목적으로 합니다.

제2조 (정의)

이 약관에서 사용하는 용어의 의미는 다음 각 호와 같습니다.
(1) "이용자" 란 플리마인드에 접속하여 이 약관에 따라 플리마인드가 제공하는 서비스를 받는 회원 및 비회원을 말합니다.
(2) "회원" 이라 함은 플리마인드에 개인정보를 제공하여 약관동의 절차를 거쳐 회원 등록을 한 후 이용자번호를 부여받은 자로서, 플리마인드가 제공하는 정보를 사용하며 상담 및 검사 서비스와 함께, 필요에 따라 전문가와의 연계 서비스를 제공받을 수 있는 이용자를 말합니다.

① 본 약관에서 사용하는 용어의 정의는 다음 각 호와 같습니다.
1. 서비스 : 기관이 제공하는 상품을 본 약관에 제시된 조건에 따라 회원이 구매할 수 있도록 기관에서 유통하는 온라인 심리상담 서비스 및 관련 컨텐츠 제공서비스
2. 상품 : 기관에서 제공하는 양식에 맞추어 이용자회원에게 판매 또는 전송을 위하여 등록한 상담 콘텐츠 및 실제 온라인상담 수행 등의 총칭
3. 이용자회원 : 홈페이지에 접속하여 본 약관에 동의하고, 아이디(ID)와 비밀번호(PASSWORD)를 발급받거나 휴대용 단말기에서 본 약관에 동의하고, 해당 단말기를 등록하여 서비스 및 상품을 구매하여 이용하는 고객
4. 아이디(ID) : 회원 식별과 회원의 서비스 이용을 위하여 회원이 선정하고 기관이 승인하는 영문자와 숫자조합
5. 비밀번호(PASSWORD) : 회원의 정보 보호를 위해 회원 자신이 설정한 문자, 숫자 및 특수문자의 조합
6. 게시물 : 회원이 서비스를 이용하면서 서비스에 게시한 부호, 문자, 소리, 화상, 동영상, 기타 정보 형태의 글, 사진 및 각종 파일 링크

제3조 (약관의 효력 및 변경)
① 본 약관은 서비스를 이용하고자 하는 모든 회원에게 그 효력이 발생합니다.
② 본 약관의 내용은 기관이 서비스의 인터넷 홈페이지 (www.plymind.com과 www.plymind.co.kr 이하 “홈페이지”) 또는 서비스 화면에 게시하거나 기타의 방법으로 회원에게 공지하고, 회원이 이에 동의하면 효력이 발생합니다.
③ 회원이 약관을 상세히 읽지 않아 발생한 피해에 대해서 기관은 책임을 지지 않습니다.
④ 기관이 필요하다고 인정되면 본 약관을 변경할 수 있으며, 기관이 약관을 변경할 때에는 적용일자와 변경사유를 밝혀 본 조 제 2항과 같은 방법으로 그 적용일자 10일 전부터 공지합니다. 다만, 회원에게 불리한 약관을 변경할 때에는 그 적용일자 30일 전부터 공지하며, 이메일(E-mail), 문자메시지(SNS) 등으로 회원에게 개별 통지합니다. (회원이 연락처를 기재하지 않았거나 변경하여 개별 통지가 어려울 때에는 홈페이지에 변경 약관을 공지하는 것을 개별 통지한 것으로 간주합니다.)
⑤ 기관이 본 조 제3항에 따라 변경 약관을 공지하거나 통지하면서, 회원이 약관 변경 적용일 까지 거부 의사를 표시하지 않으면 약관 변경에 동의한 것으로 간주합니다. 이는 내용을 공지 또는 통지하였음에도 회원이 변경된 약관의 효력 발생 일까지 약관 변경에 대한 거부 의사를 표시하지 않은 것이므로 회원이 변경 약관에 동의한 것으로 간주합니다.
회원이 변경된 약관에 동의하지 않으면 서비스 이용을 중단하고 이용계약을 해지할 수 있습니다.
⑥ 본 약관에 동의하는 것은 기관이 운영하는 서비스의 홈페이지를 정기적으로 방문하여 약관의 변경사항을 확인하는 것에 동의함을 의미합니다. 변경된 약관에 대한 정보를 알지 못하여 발생하는 회원의 피해에 대해 기관은 책임이 없습니다.
⑦ 기관은 이용자가 요구하면 본 약관의 사본을 이용자에게 교부하며, 이용자가 홈페이지에서 본 약관을 열람할 수 있도록 조치합니다.

제4조 (약관 외 준칙)
① 본 약관에 밝히지 않은 사항은 전기통신기본법, 전기통신사업법, 전보통신망 이용촉진 및 정보보호 등에 관한 벌률, 전자상거래 등에서의 소비자보호에 관한 법률 등 관계 법령 및 기관이 정한 서비스의 세부이용지침 등의 규정을 따릅니다.
② 기관은 필요하면 특정 서비스에 적용될 사항(이하 “개별약관”)을 정하여 이를 제2조 제2항의 방법에 따라 공지할 수 있습니다.
③ 기관은 필요하면 서비스 이용과 관련된 세부이용지침을 정하여 이를 제2조 제2항의 방법에 따라 공지할 수 있습니다.
④ 본 약관과 관련하여 기관의 정책 변경, 법령의 제·개정 또는 공공기관의 고시나 지침 등에 따라 기관이 홈페이지의 공지사항 게시판 등을 통해 공지하는 내용도 이용계약의 일부를 구성합니다.


◎ 제 2 장 회원의 가입(서비스 이용계약)

제5조 (이용신청)
① 서비스에 가입하여 서비스를 이용하고자 하는 자는 웹페이지와 휴대용 단말기에서 본 약관에 동의하고 가입절차를 밟습니다.
② 서비스에 가입하여 서비스를 이용하고자 하는 자의 가입신청양식에 기재하는 모든 정보는 실제 데이터인 것으로 간주되며 허위의 정보를 입력한 이용자는 법적인 보호를 받을 수 없고, 서비스 사용의 제한을 받을 수 있습니다.

제6조 (이용계약의 성립)
① 이용계약은 회원이 되고자 하는자(이하 “가입신청자”)가 본 약관의 내용에 대하여 동의한 후 회원가입 신청 양식에 따라 필요한 항목을 기재하여 제출하는 방법으로 회원가입을 신청하고 기관이 위 신청을 승낙하면 체결됩니다.

제7조(서비스 이용의 제한)
① 기관은 회원에게 영화 및 비디오오물의 진흥에 관한 벌률, 청소년 보호법 기타 관련 법령에 따른 등급 및 연령 준수를 위하여 이용제한이나 등급별 제한, 기타 필요한 조치를 할 수 있습니다.
② 회원이 본 약관에 동의함은 서비스와 관련하여 기관에서 제공하거나 제공할 모든 서비스에 동의하는 것으로 봅니다.


 ◎ 제 3 장 서비스 이용

제8조 (서비스의 제공 내용)
① 기관은 회원에게 다음과 같은 서비스를 제공합니다.
1. 전자상거래 서비스 : 기관이 PC와 모바일을 통하여 기관과 회원 간에 상담서비스 상품의 거래 및 수행이 이루어질 수 있도록 온라인 거래•소통장소를 제공하는 서비스 및 관련 부가서비스를 말합니다.

제9조 (서비스의 제공 기간과 이용시간)
① 회원에게 서비스를 제공하는 기간은 회원의 서비스 이용신청을 기관이 승낙한 날로부터 서비스의 이용계약을 해지하거나 서비스를 종료할 때까지입니다.
② 서비스의 종류에 따라 이용 가능시간이 다르며, 그 내용은 공지하도록 되어있습니다.

제10조(상품의 구매)
① 기관은 회원이 상담상품을 구매하기 전에 상담자의 성명과 경력정보, 더불어 해당 사실, 개별 상품의 이용에 필요한 최소한의 기술사양[운영체제(OS),  시스템 소프트웨어(SW) 사양 등 〔단, 상품의 특성상 이러한 정보가 불필요할 때에는 그렇지 않음] 전자상거래 등에서 소비자보호에 관한 법률 등 관계 법령에서 규정하는 정보를 회원에게 미리 알립니다.
② 회원은 상품을 구매하기 전에 반드시 본 조항 제1항의 사항 및 기관이 홈페이지 내에 작성한 상품의 상세 내용과 거래의 조건을 정확히 확인해야 합니다. 구매하려는 상품의 내용과 거래 조건을 확인하지 않고 구매하여 발생한 모든 손해에 대한 책임은 회원 본인에게 있습니다.
③ 회원의 실수로 인한 결제에 대한 책임은 회원 본인에게 있습니다.
④ 구매계약은 회원이 기관이 제시한 서비스 상품의 판매조건에 응하고 구매하겠다는 의사표시를 했을 때 체결됩니다.
⑤ 기관은 회원이 휴대전화, 신용카드 등 기타의 방법으로 상품판매 대금을 결제할 방법을 제공합니다.
⑥ 상품 판매 대금의 결제와 관련하여 회원이 입력한 정보 및 그 정보와 관련하여 발생한 책임과 불이익은 전적으로 이용자인 회원이 부담해야합니다.
⑦ 기관은 회원의 상품구매 계약 체결 내용을 마이페이지에서 확인할 수 있도록 조치합니다.
⑧ 미성년자인 회원이 법정 대리인(부모 등)의 동의 없이 상품을 구매하거나 계약 체결 후 법정 대리인이 추인하지 않으면 민법상 취소권을 행사할 수 있습니다 다만, 민법상 취소권이 제한될 때(미성년자가 성년자의 주민등록번호, 결제 정보 등을 기관에 제공하여 성년으로 믿게 한 경우, 기관이 통신 과금 서비스 이용 동의를 이미 얻은 경우 등)에는 취소할 수 없습니다.

제12조 (상품의 결제)
회원이 상품을 결제할 때는 결제대행사에 지급해야 하는 결제대행수수료를 포함하여 결제합니다. 결제대행수수료는 결제대행사가 회원이 상품을 결제하는 데 드는 비용입니다.

제13조 (청약철회 등에 관한 규정)
① 서비스를 통하여 회원이 구매하는 상품은 그 상품의 성격상 그리고 관계 법령(전자상거래 등에서의 소비자보호에 관한 법률, 콘텐츠산업진흥법상의 청약철회 불가 사유 해당 등)상 구매 후 기관이 명시하는 특정시간 이후에는 청약철회가 불가능할 수 있습니다. 다만, 그럴 때 기관은 청약철회가 불가능 하다는 사실을 표시사항에 포함하는 조치를 합니다.
② 본 조 제1항의 청약철회가 제한되는 경우 외에는, 회원은 이미 구매 완료된 상품이라도 기관의 서비스 화면에서 청약철회를 할 수 있습니다.
③ 기관은 필요하면 청약철회 등을 요청하는 회원에게 청약 철회 등에 대한 자료의 제공을 요청할 수 있습니다.
④ 청약 철회, 환급 요청 기간은 관련 법령(전자상거래 등에서의 소비자보호에 관한 법률 등)에서 규정하는 기간을 따르며, 구체적인 기간은 다음과 같습니다.

1. 유료 상품을 공급받는 날 또는 상품 공급에 대한 계약서, 통지 등을 받은 날로부터 7일 이내
2. 공급된 상품의 내용이 표시•광고된 내용과 다르거나 계약 내용과 다르게 이행되었을 때에는 해당 상품을 공급받은 날로부터 3개월 이내, 또는 그 사실을 알거나 알 수 있었던 날로부터 30일 이내

⑤ 기관은 본 조 제4항에 따라 관련 법령(전자상거래 등에서의 소비자보호에 관한 벌률 등)에서 규정하는 기간 내에 지급받은 상품 대금이 있으면 환급하여야 합니다.
⑥ 기관은 회원에게 대금을 환급할 때 이용대금의 결제와 동일한 방법으로 결제 대금의 전부 또는 일부를 환급하며, 동일한 방법으로 환급할 수 없을 때에는 대안을 제시하며 이용자의 선택에 따라 환급합니다.

제14조 (개별 상품의 하자 등으로 인한 회원의 구매 계약 해제•해지)
① 회원은 다음 각 호의 사유가 있으면 개별 상품의 구매 계약을 해제하거나 해지할 수 있습니다.
1. 상품의 제공 시기가 있었으나 그 제공 시기에 상품이 제공되지 않은 경우
2. 하자 있는 상품을 제공한 탓에 회원이 이용목적을 달성 할 수 없는 경우
3. 기타 법률에 규정되어 있거나 당사자가 합의한 경우
② 회원이 본 조 제1항1호 또는 2호의 사유로 개별 상품에 대한 이용계약을 해제하거나 해지하려면 상당한 기간을 정하여 기관 또는 상담자에게 상품을 이용할 수 있게 하거나 하자 없는 상품을 제공하도록 요구해야합니다. 다만, 상담사가 미리 상품을 제공하지 못한다는 사실을 밝혔을 때에는 그렇지 않습니다.
③ 회원이 본 조에 따라 상품 구매계약을 해제하거나 해지하더라도 해당 상품이 무료 상품이거나 회원이 비용을 직접 지급하지 않는 상품일 때에는 기관은 반환 의무를 부담하지 않습니다.
④ 회원이 기관의 고의, 과실 등 귀책사유로 본 조에 따라 개별 상품 구매 계약을 해제하거나 해지하였을 때는 기관에 손해배상을 청구할 수 있습니다. 다만, 기관이 고의 또는 과실이 없으면 그렇지 않습니다.
⑤ 본 조 각항에서 정하는 사항 이외에는 전자상거래 등에서의 소비자보호에 관한 법률, 콘텐츠산업진흥법 등 관련 법령, 고시에 따라 처리합니다.
⑥ 환불 규정은 홈페이지 FAQ 환불규정란에 명시되어 있습니다.

제15조 (서비스의 변경 및 중단)
① 기관은 서비스를 변경하여 제공할 수 있습니다. 이때 기관은 변경될 서비스의 내용과 제공일자를 제 23조에서 정한 방법으로 회원에게 공지하거나 통지합니다.
② 기관은 다음 각 호에 해당하면 서비스의 전부 또는 일부를 제한하거나 중단할 수 있습니다.

1. 서비스용 설비의 보수 등 공사로 부득이한 경우
2. 회원이 기관의 영업활동을 방해하는 경우
3. 정전, 제반 설비의 장애 또는 이용량의 폭주 등으로 정상적인 서비스 이용에 지장이 있는 경우
4. 서비스 제공업자와 계약 종료 등과 같은 기관의 제반 사정으로 서비스를 유지할 수 없는 경우
5. 기타 천재지변, 국가비상사태 등 불가항력의 사유가 있는 경우

③ 기관이 본 조 제2항에 따라 서비스를 중단할 때에는 제23조 제2항에서 정한 방법으로 이용자에게 해당 사실을 통지합니다. 다만, 통제할 수 없는 사유로 서비스가 중단(운영자의 고의나 과실이 없는 장애, 시스템다운 등)되어 기관이 미리 통지할 수 없을 때에는 그렇지 않습니다.
④ 기관은 서비스의 변경, 중단으로 발생하는 문제에 대해서 기관에 고의, 과실이 없는 한 어떠한 책임도 지지 않습니다.

제16조 (서비스 이용료, 과오금 환급)
① 기관은 서비스의 일부 특정 서비스 또는 기능을 제공할 때 이용료를 부과할 수 있습니다.
② 본 조 제1항의 이용료는 본 조 및 개별 약관 또는 별도 신청서에서 정하는 바를 따릅니다.
③ 과오금이 발생하면 기관은 서비스이용 대금의 결제와 동일한 방법으로 과오금을 환급합니다. 다만, 동일한 방법으로 환급할 수 없는 때에는 대안을 제시하고 회원의 선택에 따라 과오금을 환급합니다.
④ 기관의 책임 있는 사유로 과오금이 발생하였다면 기관은 계약비용, 수수료 등에 관계없이 과오금 전액을 환불합니다. 다만 이용자의 책임 있는 사유로 과오금이 발생하였다면, 기관은 과오금을 환급하는데 드는 비용을 합리적인 범위 내에서 공제하고 환급할 수 있습니다.

제17조 (정보의 제공 및 광고의 개재)
기관은 서비스를 운영하는 데 필요한 각종 정보를 서비스 화면에 게재하거나 이메일(E-mail), 문자메시지(SNS/MMS) 등의 방법으로 회원에게 제공할 수 있습니다.

제18조 (게시물 또는 내용물의 삭제)
① 기관은 회원이 게시하거나 전달하는 모든 게시물이 다음 각 호의 경우에 해당한다고 판단되면 사전 통지 없이 게시•전달 등을 중단할 수 있으며, 이에 대해 기관은 어떠한 책임도 지지 않습니다.

1. 기관, 다른 회원 또는 제3자를 비방하거나 중상모략으로 명예를 손상하는 내용인 경우
2. 공공질서와 미풍양속을 거스르는 내용의 게시물을 유포하는 경우
3. 범죄행위에 결부된다고 인정되는 내용일 경우
4. 기관 또는 제3자의 저작권 등 기타 권리를 침해하는 내용인 경우
5. 본 조 제2항의 세부이용지침을 통하여 기관에서 규정한 게시기간을 초과한 경우
6. 불필요하거나 승인되지 않은 광고와 판촉물을 게재하는 경우
7. 기타 관계법령과 기관의 지침 등에 위반된다고 판단되는 경우

② 기관은 게시물과 관련된 세부이용지침을 별도로 제정하여 시행 할 수 있으며, 회원은 그 지침을 따라 각종 게시물을 등록하거나 삭제해야 합니다.

제19조(상품의 지적 재산권)
① 회원은 서비스를 통해 기관으로부터 상품을 구매하는 것이며 상품의 저작권과 기타 지적 재산권을 구매하는 것은 아닙니다. 기관의 명시적인 허락 없이 상품의 무단복제•전송 등 저작권을 침해하는 행위는 허용되지 않습니다.
② 회원은 구매한 상품을 가공•재판매•홍보 등 영리 목적으로 이용하거나 제3자에게 이용하게 할 수 없습니다.

제20조 (게시물의 지적 재산권)
① 기관이 작성한 저작물에 대한 저작권은 기관에 귀속됩니다.
② 회원은 서비스를 이용하여 얻은 정보를 가공•판매하는 행위 등 서비스에 게재된 자료를 영리목적으로 이용하거나 제3자에게 이용하게 할 수 없으며, 게시물에 대한 저작권 침해는 관계법령의 적용을 받습니다.


 ◎ 제4장 계약당사자의 의무

제22조 (기관의 의무)
① 기관은 서비스 제공과 관련하여 알게 된 회원의 정보를 본인의 승낙 없이 제3자에게 누설, 배포하지 않습니다. 단, 관계법령에 따른 수사상의 목적으로 관계 기관으로부터 요구를 받았거나 방송통신심의위원회로부터 요청 등이 있는 등 법률의 규정에 따른 적법한 절차에 의한 경우에는 그렇지 않습니다.
② 기관은 서비스와 관련한 회원의 불만사항이 1:1문의하기를 통하여 접수되면 이를 신속하게 처리해야 하며, 신속한 처리가 어렵다면 그 사유와 처리 일정을 서비스 화면에 게재하거나 이메일(E-mail) 등을 통하여 해당 회원에게 통지합니다.
③ 기관이 제공하는 서비스로 회원에게 손해가 발생하였다면 기관은 그러한 손해가 기관의 고의나 과실 때문에 발생했을 때만 책임을 부담하며, 그 책임의 범위는 통상손해에 한정됩니다. 다만, 특별한 사정으로 회원에게 손해가 발생하였을 때 기관이 그 사정을 알았거나 알 수 있었다면 기관은 배상할 책임이 있습니다.
④ 기관은 정보통신망 이용촉진 및 정보보호 등에 관한 법률, 통신비밀보호법, 전기통신사업법 등 서비스의 운영, 유지와 관련한 법규를 지킵니다.

제23조 (회원의 의무)
① 회원은 서비스를 이용할 때 다음 각 호에 해당하는 행위를 해서는 안 됩니다.

1. 수치심이나 혐오감 또는 공포심을 일으키는 말, 음향, 글이나 화상 또는 영상을 계속하여 상대방에게 도달하게 하는 행위
2. 스토킹(stalking) 등 다른 회원이나 특정 상담자를 괴롭히는 행위
3. 기관이 제공하는 서비스 이용방법을 따르지 않고 비정상적인 방법으로 서비스를 이용하거나 시스템에 접근하는 행위
4. 이용신청 또는 개인정보 변경 때 거짓된 정보를 적거나 다른 회원의 아이디(ID)와 비밀번호(PASSWORD), 카드정보, 계좌정보 등을 무단으로 사용하거나 부정하게 사용하는 행위
5. 기관의 직원이나 운영자로 가장하거나 사칭하여 또는 타인의 명의를 무단으로 사용하여 글을 게시하거나 메일을 발송하는 행위
6. 타인으로 가장하는 행위 및 타인과의 관계를 허위로 명시하는 행위
7. 기관 또는 제3자의 저작권 등 기타 권리를 침해하는 행위
8. 게시판 등에 음란물을 개재하거나 음란 사이트를 연결(링크)하는 행위
9. 수치심이나 혐오감 또는 공포심을 일으키는 말, 음향, 글이나 화상 또는 영상을 계속하여 상대방에게 도달하게 하는 행위
10. 자기 또는 타인에게 재산상의 이익을 주거나 타인에게 손해를 끼칠 목적으로 거짓된 정보를 유통하는 행위
11. 공공질서와 미풍양속을 거스르는 내용의 부호•문자•소리•화상•동영상 등과 기타 정보 형태의 글, 사진 및 각종 파일과 링크 등을 타인에게 유포하는 행위
12. 서비스 운영을 고의로 방해하거나 서비스의 안정적 운영을 방해할 수 있는 정보를 전송하고, 수신자의 명시 적인 수신거부 의사에도 불구하고 광고성 정보를 전송하는 행위
13. 서비스의 관련된 설비의 오작동이나 정보 등의 파괴와 혼란을 유발하는 컴퓨터 소프트웨어, 하드웨어, 전기통신 장비의 정상적인 가동을 방해•파괴할 목적으로 고안된 소프트웨어 바이러스, 기타 다른 컴퓨터 코드, 파일, 프로그램을 포함하고 있는 자료를 등록•판매•게시 하거나 이메일(E-mail)로 발송하는 행위
14. 서비스의 임의 해지, 재가입 등을 반복적으로 행하여 기관이 제공하는 무료서비스, 이벤트 혜택 등의 경제적 이익을 취하는 행위
15. 기타 불법적이거나 부당한 행위
16. 성폭력 특별법에 위배되는 행위

② 회원은 관계 법령, 본 약관의 규정, 이용안내 및 서비스 상에 공지한 주의사항, 기관이 통지하는 사항 등을 지켜야 하며, 기타 기관의 업무에 방해되는 행위를 해서는 안 됩니다.
③ 회원이 본 조 제1항에 명시된 행위를 했을 때 기관은 부가적으로 제공한 혜택의 일부 또는 전부의 회수, 특정 서비스의 이용제한, 이용계약의 해지 및 손해배상의 청구 등의 법적인 조치를 취할 수 있습니다.
④ 기관이 본 조 제4항에 정한 조치를 했을 때 기관은 미리 회원에게 유선 또는 이메일(E-mail)로 해당 내용을 통보하며, 회원과 연락할 수 없거나 긴급을 요하는 것과 같이 부득이할 때에는 먼저 조치한 후에 통보할 수 있습니다.
⑤ 회원은 본 조 제 4항에 따른 기관의 조치에 대하여 항변의 사유가 있으면 항변할 수 있습니다.
⑥ 본 조 제4항에 따라 기관이 회원과의 이용계약을 해지하더라도, 해지 이전에 이미 체결된 매매계약의 완결에 관해서는 본 약관이 계속 적용됩니다.
⑦ 본 조 제4항에서 정한 바에 따라 이용계약이 종료될 때에는 기관은 별도 통지 없이 해당 회원과 관련된 거래를 취소할 수 있고, 회원이 상품의 결제를 신용카드로 결제했을 때에는 그 신용카드 매출을 취소할 수 있습니다.
⑧ 본 조 제4항에서 정한 바에 따라 이용계약이 종료되면 기관은 회원의 재이용 신청을 승낙하지 않을 수 있습니다.

제24조 (회원에 대한 통지)
① 기관은 기관이 발급한 이메일(E-mail) 주소 또는 회원이 등록한 이메일(E-mail) 주소 또는 문자메시지(SNS) 등으로 회원에게 통지할 수 있습니다.
② 기관이 불특정 다수 회원에게 통지할 때에는 서비스 게시판 등에 게시하는 것으로 개별 통지를 대신할 수 있습니다.

제25조 (ID와 비밀번호 관리에 대한 의무와 책임)
① 회원은 자신의 아이디(ID)와 비밀번호(PASSWORD)를  철저히 관리해야 합니다. 아이디(ID)와 비밀번호(PASSWORD)의 관리 소홀, 부정사용으로 발생하는 모든 결과에 대한 책임은 회원 본인에게 있습니다.
② 회원은 본인의 아이디(ID)와 비밀번호(PASSWORD)를 타인에게 이용하게 해서는 안 되며, 회원 본인의 아이디(ID)와 비밀번호(PASSWORD)를 도난당하였거나 타인이 사용하고 있음을 알았을 때에는 바로 기관에 통보하고 기관의 안내가 있으면 그것을 따라야 합니다.

제26조 (개인정보의 변경, 보호)
① 회원은 이용신청 때 입력한 사항이 변경되면 즉시 변경사항을 최신의 정보로 수정해야 합니다. 단, 회원 아이디(ID) 등 변경할 수 없는 사항은 수정할 수 없습니다.
② 정보를 수정하지 않아서 발생하는 손해는 해당 회원이 부담하며, 기관은 이에 대해 아무런 책임을 지지 않습니다.
③ 기관은 회원의 정보를 서비스를 제공할 목적 이외의 용도로 사용하거나 회원의 동의 없이 제3자에게 제공하지 않습니다. 단, 다음의 경우는 예외로 합니다.
1. 서비스 제공에 관한 계약을 이행하고자 상담자에게 거래내역, 정산 내역 제공 등에 필요한 최소한의 회원정보(성명 등)를 제공하는 경우
2. 법률이 회원의 개인정보의 이용과 제3자에 대한 정보 제공을 허용하는 경우
④ 기관은 관련 법령이 정하는 바에 따라 회원 등록정보를 포함한 회원의 개인정보를 보호하고자 노력합니다. 회원의 개인정보보호는 관련 법령 및 기관이 정하는 개인정보취급방침에 정한 바를 따릅니다.
⑤ 기관은 수집된 개인정보의 취급 및 관리 등의 업무를 스스로 수행함을 원칙으로 하지만, 필요하면 회원의 동의를 받거나 해당 내용을 통지한 후 위 업무의 일부 또는 전부를 기관이 정한 기관에 위탁할 수 있습니다.


 ◎ 제5장 계약해지 및 이용제한

제27조 (계약해지 및 이용제한)
① 회원은 다음 각 호에서 정한 바에 따라 서비스 이용계약을 해지할 수 있습니다.
1. 회원은 언제든지 기관에 이용계약 해지의 의사를 통지하여 이용계약을 해지할 수 있습니다. 단, 해지의사를 통지하기 전에 현재 진행 중인 모든 상품의 거래를 완료하거나 철회 또는 취소해야 하며, 거래를 철회하거나 취소하여 발생하는 불이익은 회원본인이 부담하여야 합니다.
2. 회원탈퇴로 방생한 불이익은 회원 본인이 책임져야 하며, 이용계약이 종료되면 기관은 회원에게 부가적으로 제공한 각종 혜택을 회수할 수 있습니다.
② 기관은 제23조 제1항 위반 등을 이유로 회원과의 이용계약을 해지하거나 회원에게 서비스 이용을 제한하고자 할 때에는 미리 그 사유와 내용을 통지합니다. 다만, 제24조 제1항 각호에 해당하는 행위로서 회원과 연락할 수 없거나 긴급을 요하는 경우에는 먼저 이용계약을 해지하거나 서비스 이용을 제한한 후에 통지할 수 있습니다.
③ 기관은 회원 가입 후 6개월 동안 서비스를 사용한 이력이 없는 회원에게는 서비스를 이용할지를 묻는 고지를 하고, 기관이 정한 기간 내에 답변이 없으면 이용계약을 해지할 수 있습니다.
④ 본 조 제2항과 제3항의 기관 조치에 대하여 회원은 기관이 정한 절차에 따라 이의를 신청할 수 있습니다.
⑤ 본 조 제4항의 이의가 정당하다고 기관이 인정하면, 기관은 즉시 서비스의 이용을 재개합니다.
⑥ 환급과 관련하여 회원이 본 조 제1항에 해당하면 기관은 회원의 계약 해지 의사표시에 대하여 회신한 날로부터, 본 조 제2항에 해당하면 회원에게 해지의 의사표시를 한 날로부터 3영업일 이내에 회원의 대금 결제와 동일한 방법으로 환급하고, 동일한 방법으로 환급할 수 없을 때에는 대안을 제시하고 회원의 선택에 따라 과오금을 환급합니다.
⑦ 본 조에 따라 계약을 해지한 후 환급할 때, 기관은 회원이 유료서비스를 이용하면서 얻은 이익과 환불수수료(전체 대금의 30%이내의 금액) 등에 해당하는 금액을 공제하고 환급할 수 있습니다.

제28조 (양도금지)
회원은 서비스의 이용 권한과, 기타 이용 계약상 지위를 타인에게 양도하거나 증여할 수 없으며, 담보로 제공할 수 없습니다.


 ◎ 제6장 손해배상 등

제29조
① 본 약관의 규정을 위반하여 기관에 손해를 끼친 회원은 기관에 발생한 통상의 손해를 배상할 책임이 있으며, 특별한 사정으로 손해가 발생했을 때 회원이 그 사정을 알았거나 알 수 있었다면 회원은 그에 대해 배상할 책임이 있습니다.
② 회원이 서비스를 이용하면서 불법행위를 하거나 본 약관을 위반하는 행위를 하여 기관이 해당 회원 이외의 제3자로부터 손해배상 청구 또는 소송을 비롯한 각종 이의제기를 받는다면 기관은 우선 이이를 제기한 제3자에게 대응하며, 이 과정에서 기관에 비용과 손해가 발생한다면 기관은 그것과 관련하여 해당 회원에게 구상권을 행사합니다.
③ 제공하는 무료 서비스 이용과 관련하여 회원에게 발생한 어떠한 손해에 대해서도 책임을 지지 않습니다.
④ 개별서비스 제공자와 제휴 협정을 맺고 회원에게 개별서비스를 제공함에 있어 회원이 개별서비스 이용약관에 동의를 한 뒤 개별서비스 제공자의 귀책사유로 인해 손해가 발생할 경우 관련 손해에 대해서는 개별서비스 제공자가 책임을 집니다.

제30조 (면책사항)
① 기관은 천재지변 또는 이에 준하는 불가항력으로 서비스를 제공할 수 없는 때에는 서비스 제공에 대한 책임이 면제됩니다.
② 기관은 회원의 귀책사유에 기인한 서비스의 이용 장애에 대해 책임을 지지 않습니다.
③ 서비스를 매개로 회원 상호 간 또는 회원과 제3자 간에 분쟁이 발생했을 때 기관은 그것에 개입할 의무가 없으며, 고의 또는 과실이 없는 한 분쟁으로 인한 손해를 배상할 책임이 없습니다.
④ 회원이 자신의 정보를 타인에게 유출하거나 드러나는 회원의 견해와 기관의 견해는 무관하며, 기관은 고의 또는 과실이 없는 한 회원이 제공하는 상품 또는 정보 등에 대하여 어떠한 책임도 부담하지 않습니다.

제31조 (분쟁의 해결, 관할법원)
① 서비스 이용과 관련하여 기관과 회원 사이에 분쟁이 발생하면, 기관과 회원은 분쟁의 해결을 위해 성실히 협의합니다.
② 기관과 회원 사이에 분쟁이 발생하였을 때 기관 또는 회원은 전자상거래 등에서의 소비자 보호 등에 관한 법률, 콘텐츠산업진흥법 등 관련 법령에서 정하는 분쟁 조정 기구 등에 분쟁조정을 신청할 수 있습니다.
③ 본 조 제1항, 제2항의 협의에서도 분쟁이 해결되지 않아 소송이 제기되었을 때, 관할법원은 제소 당시 회원의 주소를 따르며, 주소가 없다면 거소를 관할하는 법원을 관할로 하되, 제소 당시 회원의 주소 또는 거주가 분명하지 않을 때에는 민사소송법 등 관련 법령에 따라 관할법원을 정합니다.


부칙
(시행일)
본 약관은 2015년 10월 01일부터 시행됩니다.


개인정보 수집 및 이용 동의

플리마인드(이하 ‘기관’이라 한다)는 개인정보 보호법, 정보통신망이용촉진 및 정보보호 등에 관한 법률 등에 따라 회원님의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 취급방침을 수립•공개합니다.

제1조 (개인정보의 수집 항목 및 수집 방법)
1. 기관은 회원가입, 서비스 제공에 따른 요금정산 및 서비스 제공에 관한 계약 이행, 기타 서비스 제공 등을 위해 회원가입 당시 아래와 같은 개인정보를 수집하고 있습니다.
가. 회원 가입 및 관리
- 필수항목 : 로그인ID, 비밀번호, 이메일주소, 닉네임
나. 재화 또는 서비스 제공
- 필수항목 : 성명, 주소, 휴대폰 번호, 신용카드번호, 은행계좌정보 등 결제정보
다. 인터넷 서비스 이용과정에서 아래 개인정보 항목이 자동으로 생성되어 수집될 수 있습니다.
- IP주소, 기기고유번호, 서비스 이용기록, 방문 기록, 불량이용기록 등
2. 개인정보의 수집 방법
웹페이지(회원 가입), 플리마인드 어플리케이션(회원가입), 웹페이지 또는 애플리케이션을 통한 온라인상담

제2조 (개인정보의 수집 및 이용목적)
1. 기관은 다음과 같이 회원의 개인정보를 수집합니다. 처리하고 있는 개인정보는 다음의 목적 이외의 용도로는 이용되지 않으며, 이용목적이 변경되는 경우에는 개인정보 보호법 제 18조에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다. 이하에서는 플리마인드 홈페이지나 애플리케이션을 이하 ‘서비스’라 합니다.
개인정보의 수집 및 이용 목적 : 회원 가입 및 관리 재화 또는 서비스 제공
수집하는 개인정보의 항목 : 이메일주소, 비밀번호, 닉네임, 휴대전화번호, IP주소, 기기고유번호, 서비스 이용기록, 방문 기록, 불량 이용 기록 등 성명, 휴대폰번호
개인정보의 보유•이용 기간 : 사업자/단체 서비스 탈퇴 시까지 재화•서비스 공급완료 및 요금결제•정산 완료시까지
다만, 법령에서 정한 기간이 있는 경우에는 해당 기간 종료 시까지
1) 전자상거래 등에서의 소비자 보호에 관한 법률 표시•광고에 관한 기록 : 6개월
- 계약 또는 청약철회, 대금결제, 재화 등의 공급에 관한 기록 : 5년
- 소비자 불만 또는 분쟁처리에 관한 기록 : 3년
2) 통신비밀보호법 – 인터넷 로그기록자료 : 3개월
3) 전자금융거래법
- 전자금융 거래에 관한 기록 : 5년

제3조(회원님의 권리•의무 및 행사방법)
회원 및 법정 대리인은 담당자( 담당자 권순군, 연락처 T) 02-564-5575,  admin@plymind.com)에게 언제든지 등록되어 있는 자신 혹은 당해 만 14세 미만 아동의 개인정보를 조회하거나 수정, 가입해지를 요청할 수 있습니다.
회원 및 법정대리인의 개인정보 조회, 수정은 기관의 개인정보 관리 책임자에게 서면, 전화, 전자우편 등을 통하여 하실 수 있으며 기관은 이에 대해 지체 없이 조치하겠습니다. 회원이  개인정보의 오류에 대한 정정을 요청하신경우에는 정정을 완료하기 전까지 당해 개인정보를 이용 또는 제공하지 않습니다. 또한 잘못된 개인정보를 제3자에게 이미 제공한 경우에는 정정 처리결과를 제3자에게 지체 없이 통지하여 정정이 이루어지도록 하겠습니다.

제4조(개인정보 자동 수집 장치의 설치/운영 및 거부에 관한 사항)
기관은 회원님의 쿠키를 수집하지 않습니다.

제5조(개인정보의 파기)
① 기관은 개인정보 보유기간의 경과, 처리목적 달성 등 개인정보가 불필요하게 되었을 때에는 지체 없이 해당 개인정보를 파기합니다.
② 회원님으로부터 동의 받은 개인정보 보유기간이 경과하거나 처리목적이 달성되었음에도 불구하고 다른 법령에 따라 개인정보를 계속 보존하여야 하는 경우에는, 해당 개인정보를 별도의 데이터베이스(DB)로 옮기거나 보관 장소를 달리하여 보존합니다.
③ 개인정보 파기의 절차 및 방법은 다음과 같습니다.
1. 파기절차
기관은 파기 사유가 발생한 개인정보를 선정하고, 기관의 개인정보 보호책임자의 승인을 받아 개인정보를 파기합니다.
2. 파기방법
기관은 전자적 파일 형태로 기록•저장된 개인정보는 기록을 재생할 수 없도록 로우레벨포맷(Low Level Format) 등의 방법을 이용하여 파기하며, 종이문서에 기록•저장된 개인정보는 분쇄기로 분쇄하거나 소각하여 파기합니다.

제6조(개인정보의 안전성 확보조치)
기관은 개인정보의 안전성 확보를 위해 다음과 같은 조치를 취하고 있습니다.
1. 관리적 조치 : 내부관리계획 수립•시행, 정기적 직원 교육 등
2. 물리적 조치 : 전산실 자료보관실 등의 접근통제
3. 기술적 조치 : 개인정보처리시스템 등의 접근권한 관리, 접근통제시스템 설치, 고유 식별 정보 등의 암호화, 보안프로그램 설치

제7조 (개인정보 보호책임자)
① 기관은 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 고객의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보 책임자를 지정하고 있습니다.
* 권순군, 연락처) 02-564-5575
② 회원이 기관의 서비스를 이용하시면서 발생한 모든 개인정보 보호 관련 문의, 불만처리, 피해구제 등에 관한 사항을 개인정보 보호책임자에게 문의하실 수 있습니다. 기관은 회원의 문의에 대해 지체 없이 답변 및 처리해드리겠습니다.

</textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                    <#if okButtonCheck == true>
                        <button id="${okButtonId}" type="button" class="btn btn-primary" data-dismiss="modal">${okButtonLabel}</button>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</form>
</#macro>

<#macro py_bstrap_modal_privacy_policy modalId="normal-remote-modal" formId="normal-remote-form" title="Title" okButtonCheck=false okButtonLabel="네" okButtonId="modal-ok">
<form id="${formId}">
    <div class="modal fade" id="${modalId}" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">${title}</h4>
                </div>
                <div class="modal-body">
                    <#--<h3>플리마인드 개인정보 보호정책</h3>-->
<textarea rows="10" cols="60" autofocus>
﻿플리마인드 개인정보 보호정책

플리마인드'에서 운영중인 'www.plymind.com' 과 'www.plymind.co.kr'은(이하 'website"이라 칭함) 내담자의 정보를 소중하게 생각합니다. 내담자의 정보는 개인의 소중한 자산인 동시에 website 운영의 중요한 자료가 됩니다. 그러므로 저희 website는 운영상의 모든 과정에서 내담자의 개인정보를 보호하는데 최선의 노력을 다할 것을 약속드립니다.

개인정보는 서비스의 원활한 제공을 위하여 동의한 목적과 범위 내에서만 이용되므로 법령에 의하거나 별도로 동의하지 아니하는 한 플리마인드가 내담자의 개인정보를 제3자에게 제공하는 일은 결코 없습니다.

이에 website는 다음과 같은 개인정보보호정책을 고지합니다. 이는 현행＜정보통신망이용촉진 및 정보보호에 관한 법률＞과 정부가 제정한＜개인정보 보호지침＞에 기준을 두고 있습니다.

다음의 15가지 사항은 내담자가 website의 서비스를 이용 하시는 동안 개인정보가 어떻게 보호되고 있는가에 대한 길잡이가 될 것입니다. 반드시 유념해주시고 이 페이지를 자주 방문하시어 website의 개인정보 보호 실태를 확인해 주시기 바랍니다.

이 약관에서 사용하는 용어의 정의는 다음과 같습니다.
① "서비스" 라 함은 구현되는 단말기(PC, 휴대형단말기 등의 각종 유무선 장치를 포함)와 상관없이 "내담자"가 이용할 수 있는 플리마인드 제반 상담서비스를 의미합니다.
② "내담자" 라 함은 플리마인드의 "서비스"에 접속하여 이 약관에 따라 "플리마인드"와 이용계약을 체결하고 "플리마인드"가 제공하는 "서비스"를 이용하는 고객을 말합니다.
③ “아이디(ID)" 라 함은 "내담자"의 식별과 "서비스" 이용을 위하여 "내담자"라 정하고 "플리마인드"가 승인하는 문자와 숫자의 조합을 의미합니다.
④ "비밀번호" 라 함은 "내담자"가 부여 받은 "아이디와 일치되는 "내담자"임을 확인하고 비밀보호를 위해 "내담자" 자신이 정한 문자 또는 숫자의 조합을 의미합니다.
⑤ "게시물" 이라 함은 "내담자"가 "서비스"를 이용함에 있어 "서비스상"에 게시한 부호ㆍ문자ㆍ음성ㆍ음향ㆍ화상ㆍ동영상 등의 정보 형태의 글, 사진, 동영상 및 각종 파일과 링크 등을 의미합니다.

website는 개인정보보호정책을 내담자는 언제나 용이하게 보실 수 있도록 조치하고 있습니다.

 제1조 개인정보 수집 동의절차
 제2조 광고정보의 전송
 제3조 개인정보의 수집범위
 제4조 개인정보의 수집목적 및 이용목적
 제5조 개인정보의 재 3자 제공 및 공유
 제6조 개인정보의 이용기간 및 보유기간
 제7조 개인정보의 열람, 정정
 제8조 개인정보 수집, 이용, 제공에 대한 동의철회
 제9조  링크
 제10조 게시물
 제11조 아동의 개인정보 보호
 제12조 이용자의 권리와 의무
 제13조 관리자의 책임
 제14조 의견수렴 및 불만처리
 제15조 고지의 의무


제1조 개인정보 수집 동의절차

website는 내담자가 website의 개인정보보호방침 또는 이용약관의 내용에 대해 「동의한다」 버튼 또는 「동의하지 않는다」 버튼을 클릭할 수 있는 절차를 마련하여, 「동의한다」 버튼을 클릭하면 개인정보 수집에 대해 동의한 것으로 봅니다.

제2조 광고정보의 전송
① website는 내담자의 명시적인 수신거부의사에 반하여 영리목적의 광고성 정보를 전송하지 않습니다.
② website는 내담자가 뉴스레터 등 전자우편 전송에 대한 동의를 한 경우 전자우편의 제목란 및 본문란에 다음 사항과 같이 내담자가 쉽게 알아 볼 수 있도록 조치합니다.
-전자우편의 제목란 : (동의)라는 문구를 제목란에 빈칸 없이 표시하고 전자우편 본문란의 주요 내용을 기재합니다.

-전자우편의 본문란 : 내담자가 수신거부의 의사표시를 할 수 있는 용이한 방법, 전송자의 명칭 및 연락처 (전화번호 또는 전자우편 주소)

③ website는 상품정보 안내 등 온라인 마케팅을 위해 광고성 정보를 전자우편 등으로 전송하는 경우 전자 우편의 제목란 및 본문란에 다음 사항과 같이 내담자가 쉽게 알아 볼 수 있도록 조치합니다.

④ 팩스·휴대폰 문자전송 등 전자우편 이외의 문자전송을 통해 영리목적의 광고성 정보를 전송하는 경우에는 전송내용 처음에 문구를 표기하고 전송내용 중에 전송자의 연락처를 명기하도록 조치합니다.

제3조 개인정보의 수집범위

website는 별도의 회원가입 절차 후 대부분의 컨텐츠에 자유롭게 접근할 수 있습니다.

website의 서비스를 이용하시고자 할 경우 내담자의 정보를 입력해주셔야 하며, 선택항목을 입력하시지 않았다 하여 서비스 이용에 제한은 없으나 추후 번거로운 과정을 재차 진행할 수 있습니다.

제4조 개인정보의 수집목적 및 이용목적

website에 제공해주신 내담자의 정보는 1차적으로는 이용자 구분이나 상담서비스에 이용되며, 2차적으로는 통계·분석을 통한 자료로서 이용자의 성격적 특성에 적합한 서비스를 제공하는데 이용됩니다.

플리마인드가 더 나은 서비스를 제공해 드리기 위해 수집하는 내담자의 개인정보는 아래와 같습니다.

플리마인드는 회원이 서비스에 처음 가입할 때 또는 서비스를 이용하는 과정에서 홈페이지 또는 서비스 내 어플리케이션이나 프로그램 등을 통하여 서비스 이용과정이나 서비스처리 과정에서 IP주소, 쿠키 정보가 자동으로 생성되어 수집될 수 있습니다.

제5조 개인정보의 재 3자 제공 및 공유

website는 어떠한 경우에도 제 2조에 고지한 수집목적 및 이용목적의 범위를 초과하여 내담자의 개인정보를 이용하지 않습니다.
1) 제휴관계
: 제 3의 회사와 제휴관계를 맺을 경우 반드시 사전에 제휴사명과 제휴목적, 제공되는 서비스의 내용, 공유하는 개인정보의 범위, 이용목적, 제휴기간 등에 대해 상세하게 고지할 것이며 반드시 이용자의 적극적인 동의(개인정보의 제 3자 제공 및 공유에 대한 의사를 직접 밝힘)에 의해서만 정보를 제공하거나 공유합니다.

제휴관계에 변화가 있거나 제휴관계가 종결될 때도 같은 절차에 의하여 고지하거나 동의를 구합니다.

2) 위탁 처리
: 원활한 업무 처리를 위해 이용자의 개인정보를 위탁 처리할 경우 반드시 사전에 위탁처리 업체명과 위탁 처리되는 개인정보의 범위, 업무위탁 목적, 위탁 처리되는 과정, 위탁관계 유지기간 등에 대해 상세하게 고지합니다.

3) 고지 및 동의방법
: 고지 및 동의방법은 온라인 홈페이지 초기화면의 공지사항을 통해 최소 30일 이전부터 고지함과 동시에 전자우편(E-mail) 등을 이용하여 1회 이상 개별적으로 고지하고 매각·인수합병에 대해서는 반드시 적극적인 동의 방법 (개인정보의 제 3자 제공 및 공유에 대한 의사를 직접 밝힘)에 의해서만 절차를 진행합니다.

4) 다음은 예외로 합니다.
① 관계법령에 의하여 수사상의 목적으로 관계기관으로부터의 요구가 있을 경우
② 통계작성·학술연구나 시장조사를 위하여 특정 개인을 식별할 수 없는 형태로 광고주·협력사나 연구단체 등에 제공하는 경우
③ 기타 관계법령에서 정한 절차에 따른 요청이 있는 경우

그러나 예외 사항에서도 관계법령에 의하거나 수사기관의 요청에 의해 정보를 제공한 경우에는 이를 당사자에게 고지하는 것을 원칙으로 운영하고 있습니다.

그러나 법률상의 근거에 의해 부득이하게 고지를 하지 못할 수도 있습니다. 본래의 수집목적 및 이용목적에 반하여 무분별하게 정보가 제공되지 않도록 최대한 노력하겠습니다.

플리마인드에 제3자의 서비스가 연결되어 제공되는 경우 서비스 이용을 위해 필요한 범위 내에서 내담자의 동의를 얻은 후에 개인정보를 제 3자에게 제공할 수 있습니다. 내담자의 연결 서비스를 위한 개인정보의 제3자 제공 내용은 다음과 같습니다.
•제공받는 자: 어플리케이션 등 서비스 제공자/제공업체
•이용목적: 해당 어플리케이션 서비스로의 연결을 통한 맞춤 서비스제공
•제공항목: 휴대용 디바이스에서 수집하는 혈압, 맥박, 수면시간, 칼로리, 호흡수, 피로도, 감            정상태 등
•보유 및 이용기간: 내담자가 이용하는 플리마인드 서비스 중 싸이케어서비스의 해당 어플리케이션 이용 시까지

한편, 연결 서비스를 통하여 제3자가 여러분에게 제공하는 서비스에 대해 전화 혹은 홈페이지를 통하여 플리마인드에 문의를 할 수 있습니다. 이 경우, 제3자가 제공하는 서비스 이행을 신속하게 처리하기 위하여, 플리마인드는 여러분의 동의를 얻은 후에 여러분이 문의 과정에서 입력하거나 알려 주신 개인정보를 해당되는 제3자에게 제공할 수 있습니다. 이러한 고객 문의를 처리하기 위한 개인정보의 제3자 제공 내용은 다음과 같습니다.
•제공받는 자: 어플리케이션 등 서비스 제공자/제공업체
•이용목적: 제3자 서비스 관련 고객 문의 및 상담
•제공항목: 휴대폰 번호, 이메일 주소
•개인정보의 보유 및 이용기간: 제공목적 달성 시까지

제6조 개인정보의 이용기간 및 보유기간

내담자의 개인정보는 다음과 같이 개인정보의 수집목적 또는 제공 받은 목적이 달성되면 파기됩니다. 단, 상법 등 법령의 규정에 의하여 보존할 필요성이 있는 경우에는 예외로 합니다.
- 내담자 가입정보의 경우, 내담자가 가입을 탈퇴하거나 내담자에서 제명된 때
- 상담서비스 비용정보의 경우, 바용의 완제일 또는 채권소멸시효기간이 만료된 때
- 내담자 정보의 경우, 물품 또는 서비스가 인도되거나 제공된 때

위 개인정보 수집목적 달성 시 즉시파기 원칙에도 불구하고 다음과 같이 거래 관련 권리 의무 관계의 확인 등을 이유로 일정기간 보유하여야 할 필요가 있을 경우에는 전자상거래 등에서의 소비자보호에 관한 법률 등에 근거하여 일정기간 보유합니다.
- 계약 또는 청약철회 등에 관한 기록 : 5년
- 대금결제 및 재화 등의 공급에 관한 기록 : 5년
- 소비자의 불만 또는 분쟁처리에 관한 기록 : 3년

내담자의 동의를 받아 보유하고 있는 거래정보 등을 내담자께서 열람을 요구하는 경우 website는 지체 없이 그 열람·확인 할 수 있도록 조치합니다.

제7조 개인정보의 열람, 정정

내담자가 제공한 개인정보의 열람, 수정 및 삭제에 대한 접근은 자유롭습니다. 개인정보를 열람, 수정 및 삭제 하고자 할 경우에는 홈페이지 상단의 '회원정보 수정/탈퇴'에서 정해진 순서에 따라 직접 진행하실 수 있습니다. 이 밖에 정보관리책임자에게 전화나, 서면, 이메일로 연락하시면 업무절차에 따라 정해진 최소 시간 내에 처리해 드립니다.

내담자가 개인정보의 오류에 대한 정정을 요청한 경우, 정정을 완료하기 전까지 당해 개인 정보를 이용 또는 제공하지 않습니다. 잘못된 개인정보를 제3자에게 이미 제공한 경우에는 정정 처리결과를 제3자에게 지체 없이 통지하여 정정하도록 조치하겠습니다. 내담자가 제공한 개인정보에 변동사항이 있을 경우 즉각 수정하시어 최신의 정보를 입력해주시기 바랍니다.

제8조 개인정보 수집, 이용, 제공에 대한 동의철회

회원가입 등을 통해 개인정보의 수집, 이용, 제공에 대해 회원께서 동의하신 내용을 귀하는 언제든지 철회하실 수 있습니다. 동의 철회는 로그인후 홈페이지 첫 화면의 『회원정보수정』 내 『회원탈퇴』를 클릭하거나 개인정보관리책임자에게 서면, 전화, E-mail등으로 연락하시면 즉시 개인정보의 삭제 등 필요한 조치를 하겠습니다. 동의 철회를 하고 개인정보를 파기하는 등의 조치를 취한 경우에는 그 사실을 내담자에게 지체 없이 통지하도록 하겠습니다.

website는 개인정보의 수집에 대한 동의철회(회원탈퇴)를 개인정보를 수집하는 방법보다 쉽게 할 수 있도록 필요한 조치를 취하겠습니다.

내담자가 개인정보의 삭제를 요구하는 즉시 website이 보유하고 있던 내담자의 모든 데이터는 영구히 재생할 수 없는 형태로 완전 파기됩니다. 탈퇴 절차를 마친 후 정상적으로 로그인할 수 없으면 모든 정보가 파기된 것입니다.

이메일 무단 수집거부

website는 게시된 이메일 주소가 전자우편 수집 프로그램이나 그 밖의 기술적 장치를 이용하여 무단 수집되는 것을 거부합니다. 이를 위반 시 『정보통신망 이용촉진 및 정보 보호 등에 관한 법률』 등에 의해 처벌 받을 수 있습니다.

제9조 링크

website의 페이지는 파트너쉽을 형성하고 있는 협력기관의 배너와 링크(link)를 포함하고 있으며 여타 사이트의 페이지와 연결되어 있습니다. 이는 협력기관과의 상호협조관계에 의하거나 제공 받은 컨텐츠의 출처를 밝히기 위한 조치입니다.

website가 포함하고 있는 링크를 클릭(click)하여 타 사이트(site)의 페이지로 옮겨갈 경우 해당 사이트의 개인정보보호정책은 플리마인드의 website와 전혀 무관하므로 새로 방문한 사이트의 정책을 검토해 보시기 바랍니다.

제10조 게시물

website는 내담자의 게시물을 소중하게 생각하며 변조, 훼손, 삭제되지 않도록 최선을 다하여 보호합니다. 그러나 다음의 경우는 그렇지 아니합니다.
1) 스팸(spam)성 게시물 (예 : 행운의 편지, 8억 메일, 특정사이트 광고 등)
2) 타인을 비방할 목적으로 허위 사실을 유포하여 타인의 명예를 훼손하는 글
3) 동의 없는 타인의 신상공개
4) website의 저작권, 제 3자의 저작권 등 기타 권리를 침해하는 내용
5) 기타 게시판 주제와 다른 내용의 게시물
6) 플리마인드의 권익을 해칠 수 있는 비방형 게시물

website는 바람직한 게시판 문화를 활성화하기 위하여 동의 없는 타인의 신상공개 시 특정부분을 삭제하거나 기호 등으로 수정하여 게시할 수 있습니다. 다른 주제의 게시판으로 이동 가능한 내용일 경우 오해가 없도록 하고 있습니다. 그 외의 경우 명시적 또는 개별적인 경고 후 삭제 조치할 수 있습니다.

그러나 근본적으로 게시물에 관련된 제반 권리와 책임은 작성자 개인에게 있습니다. 또 게시물을 자발적으로 공개된 정보는 보호 받기 어려우므로 정보 공개 전에 심사숙고하시기 바랍니다.

제11조 아동의 개인정보 보호

website는 만14세 미만 아동의 개인정보를 보호하기 위하여 다음의 장치를 구비하고 있습니다.
1) 만14세 미만 아동의 회원가입은 별도의 양식을 통해 이루어지고 있으며 개인정보 수집 시 법정 대리인의 동의를 구하고 있습니다. 동의의 방법은 법정대리인의 성명과 주민등록번호를 입력하도록 하고 약 일 주일간의 기간 내에 개별적으로 법정대리인과 연락을 취해 이뤄지고 있습니다.
2) 만14세 미만 아동의 법정대리인은 아동의 개인정보에 대한 열람, 수정 및 삭제를 요청할 수 있으며, website는 이러한 요청에 지체 없이 필요한 조치를 취합니다.
3) website는 만14세 미만의 이용자에 관한 정보를 제3자에게 제공하거나 공유하지 않습니다.
4) 만14세 미만으로 표시된 이용자에게는 이메일 형식의 광고나 제안서를 보내지 않으며 게임, 경품 및 이벤트를 이용하여 더 많은 정보를 공개하도록 유도하지 않습니다.

제12조 이용자의 권리와 의무

내담자의 개인정보를 최신의 상태로 정확하게 입력하여 불의의 사고를 예방해 주시기 바랍니다.

이용자가 입력한 부정확한 정보로 인해 발생하는 사고의 책임은 이용자 자신에게 있으며 타인 정보의 도용 등 허위정보를 입력할 경우 회원자격이 상실될 수 있습니다.

내담자는 개인정보를 보호 받을 권리와 함께 스스로를 보호하고 타인의 정보를 침해하지 않을 의무도 가지고 있습니다. 비밀번호를 포함한 내담자의 개인정보가 유출되지 않도록 유의하시고 게시물을 포함한 타인의 개인정보를 훼손하지 않도록 유의해 주십시오.

만약 이 같은 책임을 다하지 못하고 타인의 정보 및 존엄성을 훼손할 시에는 『정보통신망이용촉진 및 정보보호 등에 관한 법률』 등에 의해 처벌 받을 수 있습니다.

제13조 관리자의 책임

website는 내담자가 좋은 정보를 안전하게 이용할 수 있도록 최선을 다하고 있습니다. 개인정보를 보호하는데 있어 내담자에게 고지한 사항들에 반하는 사고가 발생할 시에 website가 모든 책임을 집니다. 그러나 기술적인 보완조치를 했음에도 불구하고, 해킹 등 기본적인 네트워크상의 위험성에 의해 발생하는 예기치 못한 사고로 인한 정보의 훼손 및 방문자가 작성한 게시물에 의한 각종 분쟁에 관해서는 책임이 없습니다. 내담자의 개인정보를 취급하는 책임자는 다음과 같으며 개인정보 관련 문의사항에 신속하고 성실하게 답변해 드리고 있습니다.


개인정보관리/보호 담당자
- 성 명 : 권순군
- 부 서 : 기획연구부
- 직 책 : 부장
- 이메일주소 : admin@plymind.com
- 전화번호 : 02)564-5575

제14조 의견수렴 및 불만처리

website는 내담자의 의견을 소중하게 생각하며 의문사항으로부터 언제나 성실한 답변을 받을 권리가 있습니다. website는 내담자와의 원활환 의사소통을 위해 회원서비스 센터를 운영하고 있습니다. 회원서비스 센터의 연락처는 다음과 같습니다.

- 이메일주소: plymind@plymind.com
- 전화번호 : 02)564-5575
- 주소 : 서울특별시 강남구 논현로 564 8층 806호 플리마인드
실시간 상담 및 전화상담은 오전 9:30 ~ 오후 6:00 에만 가능합니다.

이메일 및 우편을 이용한 상담은 수신 후 24시간 내에 성실하게 답변 드리겠습니다.

기타 개인정보에 관한 상담이 필요한 경우에는 개인정보침해신고센터, 대검찰청 인터넷범죄수사센터, 경찰청 사이버범죄수사대, 한국소비자보호원, 통신위원회 등으로 문의하실 수 있습니다.

- 개인정보침해신고센터 전화: 서울 1336, 지방(02)1336

- 개인정보침해신고센터 URL: privacy.kisa.or.kr

제15조 고지의 의무

현 개인정보보호정책은 2015년 10월 06일에 제정되었으며 내용의 추가·삭제 및 수정이 있을 시에는 개정 최소 30일 전부터 홈페이지의 '공지'란을 통해 고지(告知)할 것입니다.

개인정보보호정책 제정일자: 2015-10-06

개인정보보호정책 게시일자: 2015-10-07
</textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                    <#if okButtonCheck == true>
                        <button id="${okButtonId}" type="button" class="btn btn-primary" data-dismiss="modal">${okButtonLabel}</button>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</form>
</#macro>