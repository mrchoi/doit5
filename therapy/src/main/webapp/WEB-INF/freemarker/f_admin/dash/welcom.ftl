<!DOCTYPE html>
<#-- <html lang="en-us"> -->
<html>
<head>
    <meta charset="utf-8">
    <title>CKPush BackOffice Tool</title>
    <meta name="description" content="CKPush 관리툴의 Layout 페이지">
    <meta name="author" content="dhkim@ckstack.com">

    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <#-- CSS Links -->
    <#-- Basic Styles -->
    <link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/font-awesome.min.css">

    <#-- SmartAdmin Styles : Caution! DO NOT change the order -->
    <link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/smartadmin-production-plugins.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/smartadmin-production.min.css">
    <link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/smartadmin-skins.min.css">

    <#-- SmartAdmin RTL Support
    <link rel="stylesheet" type="text/css" media="screen" href="css/smartadmin-rtl.min.css"> -->

    <#-- my style -->
    <link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/ckpush.css">

    <#-- Demo purpose only: goes with demo.js, you can delete this css when designing your own WebApp
    <link rel="stylesheet" type="text/css" media="screen" href="css/demo.min.css"> -->

    <#-- FAVICONS -->
    <link rel="shortcut icon" href="${Request.resPath}img/favicon/favicon.ico" type="image/x-icon">
    <link rel="icon" href="${Request.resPath}img/favicon/favicon.ico" type="image/x-icon">

    <#-- GOOGLE FONT -->
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,700italic,300,400,700">

    <#-- #APP SCREEN / ICONS -->
    <#-- Specifying a Webpage Icon for Web Clip
         Ref: https://developer.apple.com/library/ios/documentation/AppleApplications/Reference/SafariWebContent/ConfiguringWebApplications/ConfiguringWebApplications.html -->
    <link rel="apple-touch-icon" href="${Request.resPath}img/splash/sptouch-icon-iphone.png">
    <link rel="apple-touch-icon" sizes="76x76" href="${Request.resPath}img/splash/touch-icon-ipad.png">
    <link rel="apple-touch-icon" sizes="120x120" href="${Request.resPath}img/splash/touch-icon-iphone-retina.png">
    <link rel="apple-touch-icon" sizes="152x152" href="${Request.resPath}img/splash/touch-icon-ipad-retina.png">

    <#-- iOS web-app metas : hides Safari UI Components and Changes Status Bar Appearance -->
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <#-- Startup image for web apps -->
    <#-- ipad landscape -->
    <link rel="apple-touch-startup-image" href="${Request.resPath}img/splash/splash-1024x784.png" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:landscape)">
    <#-- ipad portrait -->
    <link rel="apple-touch-startup-image" href="${Request.resPath}img/splash/splash-784x1024.png" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:portrait)">
    <#-- iphone -->
    <link rel="apple-touch-startup-image" href="${Request.resPath}img/splash/splash-320x460.png" media="screen and (max-device-width: 320px)">

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
    <script type="text/javascript">
        // 인증키를 주면 인증키로 돌아가고, 인증 object 를 주면 인증 object 로 돌아간다.
        // 존재하지 않는 것에 대해서는 ras-config.js에 정의된 값을 사용 한다.
        //_ckras.model.init('1234.');
        _ckras.model.init();
    </script>

<style type="text/css">
.smart-style-3 #header {

    background-color: #00aeaf;
    background-image: -moz-linear-gradient(top,#00aeaf,#019d9e);
    background-image: -webkit-gradient(linear,0 0,0 100%,from(#00aeaf),to(#019d9e));
    background-image: -webkit-linear-gradient(top,#00aeaf,#019d9e);
    background-image: -o-linear-gradient(top,#00aeaf,#019d9e);
    background-image: linear-gradient(to bottom,#00aeaf,#019d9e);
}
</style>
</head>

<#--

TABLE OF CONTENTS.

Use search to find needed section.

===================================================================

|  01. #CSS Links                |  all CSS links and file paths  |
|  02. #FAVICONS                 |  Favicon links and file paths  |
|  03. #GOOGLE FONT              |  Google font link              |
|  04. #APP SCREEN / ICONS       |  app icons, screen backdrops   |
|  05. #BODY                     |  body tag                      |
|  06. #HEADER                   |  header tag                    |
|  07. #PROJECTS                 |  project lists                 |
|  08. #TOGGLE LAYOUT BUTTONS    |  layout buttons and actions    |
|  09. #MOBILE                   |  mobile view dropdown          |
|  10. #SEARCH                   |  search field                  |
|  11. #NAVIGATION               |  left panel & navigation       |
|  12. #MAIN PANEL               |  main panel                    |
|  13. #MAIN CONTENT             |  content holder                |
|  14. #PAGE FOOTER              |  page footer                   |
|  15. #SHORTCUT AREA            |  dropdown shortcuts area       |
|  16. #PLUGINS                  |  all scripts and plugins       |

===================================================================

-->

<#-- BODY -->
<#-- Possible Classes

    * 'smart-style-{SKIN#}'
    * 'smart-rtl'         - Switch theme mode to RTL
    * 'menu-on-top'       - Switch to top navigation (no DOM change required)
    * 'no-menu'			  - Hides the menu completely
    * 'hidden-menu'       - Hides the main menu but still accessable by hovering over left edge
    * 'fixed-header'      - Fixes the header
    * 'fixed-navigation'  - Fixes the main menu
    * 'fixed-ribbon'      - Fixes breadcrumb
    * 'fixed-page-footer' - Fixes footer
    * 'container'         - boxed layout mode (non-responsive: will not work with fixed-navigation & fixed-ribbon)
-->
<body class="smart-style-3">

<#-- HEADER -->
<header id="header">
    <div id="logo-group">
        <#-- PLACE YOUR LOGO HERE -->
        <span id="logo"> <img src="${Request.resPath}img/logo-pale_159x43.png" alt="Plymind BackOffice Tool"> </span>
        <#-- END LOGO PLACEHOLDER -->

        <#-- TODO 로그인 된 사용자에게 보여줄 메시지 카운트를 가져와야 한다 -->

        <#-- Note: The activity badge color changes when clicked and resets the number to 0
             Suggestion: You may want to set a flag when this happens to tick off all checked messages / notifications -->
<#--
        <span id="activity" class="activity-dropdown"> <i class="fa fa-user"></i> <b class="badge"> 21 </b> </span>
-->

        <#-- TODO 로그인 된 사용자에게 보여줄 메시지를 분류별로 보여 준다 -->

        <#-- AJAX-DROPDOWN : control this dropdown height, look and feel from the LESS variable file -->
<#--
        <div class="ajax-dropdown">
-->
            <#-- the ID links are fetched via AJAX to the ajax container "ajax-notifications" -->
<#--
            <div class="btn-group btn-group-justified" data-toggle="buttons">
                <label class="btn btn-default">
                    <input type="radio" name="activity" id="ajax/notify/mail.html">
                    Msgs (14) </label>
                <label class="btn btn-default">
                    <input type="radio" name="activity" id="ajax/notify/notifications.html">
                    notify (3) </label>
                <label class="btn btn-default">
                    <input type="radio" name="activity" id="ajax/notify/tasks.html">
                    Tasks (4) </label>
            </div>
-->
            <#-- notification content -->
<#--
            <div class="ajax-notifications custom-scroll">
                <div class="alert alert-transparent">
                    <h4>Click a button to show messages here</h4>
                    This blank page message helps protect your privacy, or you can show the first message here automatically.
                </div>

                <i class="fa fa-lock fa-4x fa-border"></i>

            </div>
-->
            <#-- end notification content -->

            <#-- footer: refresh area -->
<#--
            <span> Last updated on: 12/12/2013 9:43AM
						<button type="button" data-loading-text="<i class='fa fa-refresh fa-spin'></i> Loading..." class="btn btn-xs btn-default pull-right">
                            <i class="fa fa-refresh"></i>
                        </button> </span>
-->
            <#-- end footer -->
<#--
        </div>
-->
        <#-- END AJAX-DROPDOWN -->
    </div>

    <#-- TODO 현재 진행중인 개발 사항(관리툴)에 대해서-개선점,수정사항 등등- 리스트를 보여 주도록 한다 -->

    <#-- PROJECTS: projects dropdown -->
<#--
    <div class="project-context hidden-xs">
        <span class="label">Projects:</span>
        <span class="project-selector dropdown-toggle" data-toggle="dropdown">Recent projects <i class="fa fa-angle-down"></i></span>
-->
        <#-- Suggestion: populate this list with fetch and push technique -->
<#--
        <ul class="dropdown-menu">
            <li>
                <a href="javascript:void(0);">Online e-merchant management system - attaching integration with the iOS</a>
            </li>
            <li>
                <a href="javascript:void(0);">Notes on pipeline upgradee</a>
            </li>
            <li>
                <a href="javascript:void(0);">Assesment Report for merchant account</a>
            </li>
            <li class="divider"></li>
            <li>
                <a href="javascript:void(0);"><i class="fa fa-power-off"></i> Clear</a>
            </li>
        </ul>
-->
        <#-- end dropdown-menu-->
<#--
    </div>
-->
    <#-- end projects dropdown -->

    <#-- #TOGGLE LAYOUT BUTTONS -->
    <#-- pulled right: nav area -->
    <div class="pull-right">
        <#-- collapse menu button -->
        <div id="hide-menu" class="btn-header pull-right">
            <span> <a href="javascript:void(0);" data-action="toggleMenu" title="사이드바 닫기"><i class="fa fa-reorder"></i></a> </span>
        </div>
        <#-- end collapse menu -->

        <#-- TODO 모바일 단말기에서 어떻게 나오는지 추후 테스트 하면서 수정 하도록 한다 -->

        <#-- MOBILE -->
        <#-- Top menu profile link : this shows only when top menu is active -->
        <ul id="mobile-profile-img" class="header-dropdown-list hidden-xs padding-5">
            <li class="">
                <a href="#" class="dropdown-toggle no-margin userdropdown" data-toggle="dropdown">
                    <img src="${userProfileImage!'${Request.resPath}img/avatars/male.png'}" alt="me" class="online" />
                </a>
                <ul class="dropdown-menu pull-right">
                    <li>
                        <a href="javascript:void(0);" class="padding-10 padding-top-0 padding-bottom-0"><i class="fa fa-cog"></i> Setting</a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="#ajax/profile.html" class="padding-10 padding-top-0 padding-bottom-0"> <i class="fa fa-user"></i> <u>P</u>rofile</a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="javascript:void(0);" class="padding-10 padding-top-0 padding-bottom-0" data-action="toggleShortcut"><i class="fa fa-arrow-down"></i> <u>S</u>hortcut</a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="javascript:void(0);" class="padding-10 padding-top-0 padding-bottom-0" data-action="launchFullscreen"><i class="fa fa-arrows-alt"></i> Full <u>S</u>creen</a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <a href="login.html" class="padding-10 padding-top-5 padding-bottom-5" data-action="userLogout"><i class="fa fa-sign-out fa-lg"></i> <strong><u>L</u>ogout</strong></a>
                    </li>
                </ul>
            </li>
        </ul>

        <#-- logout button -->
        <div id="logout" class="btn-header transparent pull-right">
            <span> <a href="${contextPath}/admin/open/logout" title="로그 아웃" data-action="userLogout" data-logout-msg="로그 아웃 합니다. 로그 아웃 이후 브라우저까지 완전 종료 시키면 보안에 더 많은 도움이 됩니다."><i class="fa fa-sign-out"></i></a> </span>
        </div>
        <#-- end logout button -->

        <#-- TODO 모바일 단말기에서 어떻게 나오는지 추후 테스트 하면서 수정 하도록 한다 -->

        <#-- search mobile button (this is hidden till mobile view port) -->
        <#--
        <div id="search-mobile" class="btn-header transparent pull-right">
            <span> <a href="javascript:void(0)" title="Search"><i class="fa fa-search"></i></a> </span>
        </div>
        -->
        <#-- end search mobile button -->

        <#-- TODO 검색에 대해서 고민해 보고 넣도록 하자 -->

        <#-- SEARCH -->
        <#-- input: search field -->
        <#--
        <form action="#ajax/search.html" class="header-search pull-right">
            <input id="search-fld" type="text" name="param" placeholder="Find reports and more">
            <button type="submit">
                <i class="fa fa-search"></i>
            </button>
            <a href="javascript:void(0);" id="cancel-search-js" title="Cancel Search"><i class="fa fa-times"></i></a>
        </form>
        -->
        <#-- end input: search field -->

        <#-- fullscreen button -->
        <div id="fullscreen" class="btn-header transparent pull-right">
            <span> <a href="javascript:void(0);" data-action="launchFullscreen" title="전체 화면"><i class="fa fa-arrows-alt"></i></a> </span>
        </div>
        <#-- end fullscreen button -->

        <#-- 추후 TODO 환경설정에서 voice command true 로 하고 voice command 먹히는지 테스트 해 보자 -->

        <#-- Voice Command: Start Speech -->
        <#-- NOTE: Voice command button will only show in browsers that support it. Currently it is hidden under mobile browsers.
                   You can take off the "hidden-sm" and "hidden-xs" class to display inside mobile browser-->
        <#--
        <div id="speech-btn" class="btn-header transparent pull-right hidden-sm hidden-xs">
            <div>
                <a href="javascript:void(0)" title="Voice Command" data-action="voiceCommand"><i class="fa fa-microphone"></i></a>
                <div class="popover bottom"><div class="arrow"></div>
                    <div class="popover-content">
                        <h4 class="vc-title">Voice command activated <br><small>Please speak clearly into the mic</small></h4>
                        <h4 class="vc-title-error text-center">
                            <i class="fa fa-microphone-slash"></i> Voice command failed
                            <br><small class="txt-color-red">Must <strong>"Allow"</strong> Microphone</small>
                            <br><small class="txt-color-red">Must have <strong>Internet Connection</strong></small>
                        </h4>
                        <a href="javascript:void(0);" class="btn btn-success" onclick="commands.help()">See Commands</a>
                        <a href="javascript:void(0);" class="btn bg-color-purple txt-color-white" onclick="$('#speech-btn .popover').fadeOut(50);">Close Popup</a>
                    </div>
                </div>
            </div>
        </div>
        -->
        <#-- end voice command -->

        <#-- TODO 다국어 넣는 방법에 대해서 고민해 보고 추가 언어 넣도록 하자. 일단은 한국어 제외하고 주석으로 막아 둔다. -->

        <#-- multiple lang dropdown : find all flags in the flags page -->
        <ul class="header-dropdown-list hidden-xs">
            <li>
                <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <img src="${Request.resPath}img/blank.gif" class="flag flag-kr" alt="한국어"> <span> 한국</span> <i class="fa fa-angle-down"></i> </a>
                <ul class="dropdown-menu pull-right">
                    <li class="active">
                        <a href="javascript:void(0);"><img src="${Request.resPath}img/blank.gif" class="flag flag-kr" alt="한국어"> 한국어</a>
                    </li>
                    <#--
                    <li>
                        <a href="javascript:void(0);"><img src="img/blank.gif" class="flag flag-us" alt="United States"> English (US)</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);"><img src="img/blank.gif" class="flag flag-fr" alt="France"> Français</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);"><img src="img/blank.gif" class="flag flag-es" alt="Spanish"> Español</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);"><img src="img/blank.gif" class="flag flag-de" alt="German"> Deutsch</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);"><img src="img/blank.gif" class="flag flag-jp" alt="Japan"> 日本語</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);"><img src="img/blank.gif" class="flag flag-cn" alt="China"> 中文</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);"><img src="img/blank.gif" class="flag flag-it" alt="Italy"> Italiano</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);"><img src="img/blank.gif" class="flag flag-pt" alt="Portugal"> Portugal</a>
                    </li>
                    <li>
                        <a href="javascript:void(0);"><img src="img/blank.gif" class="flag flag-ru" alt="Russia"> Русский язык</a>
                    </li>
                    -->
                </ul>
            </li>
        </ul>
        <#-- end multiple lang -->

        <#-- ras connection icon 을 추가. 연결 완료 되면 보여 주지 않는다 -->
        <div id="ras-connection-status-btn" class="btn-header transparent pull-right" style="padding-right:10px;">
            <div>
                <div title="RAS 상태" style="margin-top: 14px; color: white; font-size: 17px;">
                    <i id="ras-connection-status" class="fa fa-spinner fa-spin"></i>
                </div>
            </div>
        </div>

    </div>
    <#-- end pulled right: nav area -->
</header>
<#-- END HEADER -->

<#-- NAVIGATION -->
<#-- Left panel : Navigation area -->
<#-- Note: This width of the aside area can be adjusted through LESS/SASS variables -->
<aside id="left-panel">
    <#-- User info -->
    <div class="login-info">
		<span> <#-- User image size is adjusted inside CSS, it should stay as is -->
            <#-- TODO 로그인 한 아이디 눌렀을때 토글 메뉴 나오는 것, 어떤 것을 넣을지 고민해서 추후 추가 해야 한다. data-action 을 없애면 토글 메뉴 나오지 않는다. -->
            <#--<a href="javascript:void(0);" id="show-shortcut" data-action="toggleShortcut">-->
            <a href="javascript:void(0);" id="show-shortcut">
                <img src="${userProfileImage!'${Request.resPath}img/avatars/male.png'}" alt="me" class="online" />
	    	    <span style="text-transform:none;">
                ${userId}
                </span>
                <i class="fa fa-angle-down"></i>
            </a>
		</span>
    </div>
    <#-- end user info -->

    <#-- NAVIGATION : This navigation is also responsive
    To make this navigation dynamic please make sure to link the node
    (the reference to the nav > ul) after page load. Or the navigation
    will not initialize.
    -->
    <nav>
        <#--
        NOTE: Notice the gaps after each icon usage <i></i>..
        Please note that these links work a bit different than
        traditional href="" links. See documentation for details.
        -->
        <ul>
            <#if Request.loginAuthority == 'ROLE_ROOT'><#--관리자 권한으로 보기-->
            <li>
                <a href="${Request.contextPath}/admin/member/list" title="회원 관리"><i class="fa fa-lg fa-fw fa-group"></i> <span class="menu-item-parent">회원 관리</span></a>
                <ul>
                    <li>
                        <a href="${Request.contextPath}/admin/member/add">회원 정보 등록</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/member/list">회원 목록</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/group/list">회원 단체 관리</a>
                    </li>
                </ul>
            </li>
            </#if>
            <li>
                <#if Request.loginAuthority == 'ROLE_ROOT'><#--관리자 권한으로 보기-->
                    <a href="#" title="플리마인드 관리"><i class="fa fa-lg fa-fw fa-group"></i> <span class="menu-item-parent">플리마인드 관리</span></a>
                    <ul>
                         <li>
                            <a href="${Request.contextPath}/admin/supervisor/list">슈퍼바이저 관리</a>
                        </li>
                        <li>
                            <a href="${Request.contextPath}/admin/advisor/list">상담사 관리</a>
                        </li>
                        <li>
                            <a href="${Request.contextPath}/admin/present/present_list">상담 현황 관리</a>
                        </li>
                        <li>
                            <a href="${Request.contextPath}/admin/docFile/list">검사 문서 관리</a>
                        </li>
                        <li>
                            <a href="${Request.contextPath}/admin/holiday/advisor_holiday_list">상담사 일정 관리</a>
                        </li>
                        <li>
                            <a href="${Request.contextPath}/admin/holiday/holiday_list">캘린터 휴일 관리</a>
                        </li>
                    </ul>
                <#elseif Request.loginAuthority == 'ROLE_ADVISOR' ><#--상담사 권한으로 보기-->
                    <a href="${Request.contextPath}/admin/advisor/list" title="플리마인드 관리"><i class="fa fa-lg fa-fw fa-group"></i> <span class="menu-item-parent">플리마인드 관리</span></a>
                    <ul>
                        <li>
                            <a href="${Request.contextPath}/admin/advisor/list">상담사 관리</a>
                        </li>
                        <li>
                            <a href="${Request.contextPath}/admin/holiday/advisor_holiday_list">상담사 일정 관리</a>
                        </li>
                        <li>
                            <a href="${Request.contextPath}/admin/docFile/list">검사 문서 관리</a>
                        </li>
                    </ul>
                </#if><#--/ 관리자 권한으로 보기-->
            </li>


            <li>
                <a href="#" title="상담 관리"><i class="fa fa-lg fa-fw fa-file-text-o"></i> <span class="menu-item-parent">상담 관리</span></a>
                <ul>
                    <li>
                        <a href="${Request.contextPath}/admin/plymind/myadvice/progress_list">심리 상담 관리</a>
                    </li>
                    <#if Request.loginAuthority == 'ROLE_ROOT'><#--관리자 권한에서만 보기 /-->
                    <li>
                        <a href="${Request.contextPath}/admin/mycheckup/list">심리 검사 관리</a>
                    </li>
                    </#if><#--/ 관리자 권한에서만 보기-->
                    <li>
                        <a href="${Request.contextPath}/admin/mycomplete/list">완료 내역 관리</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/one/list">1:1 문의 게시판</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="#" title="예약 관리"><i class="fa fa-lg fa-fw fa-calendar"></i> <span class="menu-item-parent">예약 관리</span></a>
                <ul>
                    <li>
                        <a href="${Request.contextPath}/admin/plymind/appointment/appointment_group_list">예약 등록</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/plymind/appointment/appointment_list">예약 목록</a>
                    </li>
                </ul>
            </li>
            <#if Request.loginAuthority == 'ROLE_ROOT'><#--관리자 권한에서만 보기 /-->
            <li>
                <a href="#" title="결제 관리"><i class="fa fa-lg fa-fw fa-krw"></i> <span class="menu-item-parent">결제 관리</span></a>
                <ul>
                    <li>
                        <a href="${Request.contextPath}/admin/plymind/payment/payment_list">결제 내역</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/plymind/payment/cancel_list">취소 내역</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="#" title="게시판"><i class="fa fa-lg fa-fw fa-pencil-square-o"></i> <span class="menu-item-parent">게시판</span></a>
                <ul>
                    <li>
                        <a href="${Request.contextPath}/admin/question/list">문의 게시판</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/secret/list">비밀 게시판</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/theme/list">테마 게시판</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/faq/list">FAQ 관리</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="${Request.contextPath}/admin/sentence/list" title="너나들이 관리"><i class="fa fa-lg fa-fw fa-bullhorn"></i> <span class="menu-item-parent">너나들이 관리</span></a>
            </li>
            <li>
                <a href="${Request.contextPath}/admin/notice/list" title="공지사항/이벤트 관리"><i class="fa fa-lg fa-fw fa-volume-up"></i> <span class="menu-item-parent">공지사항/이벤트 관리</span></a>
            </li>
            </#if><#--/ 관리자 권한에서만 보기-->
            <#--
            <li>
                <a href="#" title="앱"><i class="fa fa-lg fa-fw fa-qrcode"></i> <span class="menu-item-parent">앱</span></a>
                <ul>
                    <li>
                        <a href="${Request.contextPath}/admin/app/add">앱 생성</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/app/list">앱 목록</a>
                    </li>
                </ul>
            </li>
            -->
            <#--
            <li>
                <a href="#" title="커뮤니티"><i class="fa fa-lg fa-fw fa-leaf"></i> <span class="menu-item-parent">커뮤니티</span></a>
                <ul>
                    <li>
                        <a href="${Request.contextPath}/admin/board/manage">게시판 관리</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/board/template/add">템플릿 생성</a>
                    </li>
                    <li>
                        <a href="${Request.contextPath}/admin/board/template/list">템플릿 목록</a>
                    </li>
                </ul>
            </li>
            -->
            <#--
            <li>
                <a href="#" title="Push 메시지"><i class="fa fa-lg fa-fw fa-bullhorn"></i> <span class="menu-item-parent">Push 메시지</span></a>
                <ul>
                    <li>
                        <a href="${Request.contextPath}/admin/message/gcm/apns/app/list">즉시 발송</a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="#" title="리소스"><i class="fa fa-lg fa-fw fa-briefcase"></i> <span class="menu-item-parent">리소스</span></a>
                <ul>
                    <li>
                        <a href="#">파일</a>
                        <ul>
                            <li>
                                <a href="${Request.contextPath}/admin/resource/repository/add/file"><i class="fa fa-fw fa-plus"></i> 임의 파일 등록</a>
                            </li>
                            <li>
                                <a href="${Request.contextPath}/admin/resource/repository/file/list"><i class="fa fa-fw fa-file-archive-o"></i> 임의 파일 목록</a>
                            </li>
                            <li>
                                <a href="${Request.contextPath}/admin/resource/gcm/apns/file/list"><i class="fa fa-fw fa-file-image-o"></i> Push 이미지 목록</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">단말기</a>
                        <ul>
                            <li>
                                <a href="${Request.contextPath}/admin/app/device/add"><i class="fa fa-fw fa-mobile"></i> 단말기 등록</a>
                            </li>
                            <li>
                                <a href="${Request.contextPath}/admin/app/device/list"><i class="fa fa-fw fa-list"></i> 단말기 목록</a>
                            </li>
                        </ul>
                    </li>

                </ul>
            </li>-->

        <#-- TODO inbox 에 대해서 어떻게 넣을 것인지 고민해 보자. 오늘 날짜의 이력을 넣을까? -->

        <#--
        <li>
            <a href="ajax/inbox.html"><i class="fa fa-lg fa-fw fa-inbox"></i> <span class="menu-item-parent">Inbox</span><span class="badge pull-right inbox-badge">14</span></a>
        </li>
        -->

        <#--
        <li>
            <a href="#"><i class="fa fa-lg fa-fw fa-bar-chart-o"></i> <span class="menu-item-parent">Graphs</span></a>
            <ul>
                <li>
                    <a href="ajax/flot.html">Flot Chart</a>
                </li>
                <li>
                    <a href="ajax/morris.html">Morris Charts</a>
                </li>
                <li>
                    <a href="ajax/inline-charts.html">Inline Charts</a>
                </li>
                <li>
                    <a href="ajax/dygraphs.html">Dygraphs</a>
                </li>
                <li>
                    <a href="ajax/chartjs.html">Chart.js <span class="badge pull-right inbox-badge bg-color-yellow">new</span></a>
                </li>
            </ul>
        </li>
        -->

        <#--
        <li>
            <a href="#"><i class="fa fa-lg fa-fw fa-table"></i> <span class="menu-item-parent">Tables</span></a>
            <ul>
                <li>
                    <a href="ajax/table.html">Normal Tables</a>
                </li>
                <li>
                    <a href="ajax/datatables.html">Data Tables <span class="badge inbox-badge bg-color-greenLight">v1.10</span></a>
                </li>
                <li>
                    <a href="ajax/jqgrid.html">Jquery Grid</a>
                </li>
            </ul>
        </li>
        -->

        <#--
        <li>
            <a href="#"><i class="fa fa-lg fa-fw fa-pencil-square-o"></i> <span class="menu-item-parent">Forms</span></a>
            <ul>
                <li>
                    <a href="ajax/form-elements.html">Smart Form Elements</a>
                </li>
                <li>
                    <a href="ajax/form-templates.html">Smart Form Layouts</a>
                </li>
                <li>
                    <a href="ajax/validation.html">Smart Form Validation</a>
                </li>
                <li>
                    <a href="ajax/bootstrap-forms.html">Bootstrap Form Elements</a>
                </li>
                <li>
                    <a href="ajax/bootstrap-validator.html">Bootstrap Form Validation</a>
                </li>
                <li>
                    <a href="ajax/plugins.html">Form Plugins</a>
                </li>
                <li>
                    <a href="ajax/wizard.html">Wizards</a>
                </li>
                <li>
                    <a href="ajax/other-editors.html">Bootstrap Editors</a>
                </li>
                <li>
                    <a href="ajax/dropzone.html">Dropzone</a>
                </li>
                <li>
                    <a href="ajax/image-editor.html">Image Cropping <span class="badge pull-right inbox-badge bg-color-yellow">new</span></a>
                </li>
            </ul>
        </li>
        -->

        <#--
        <li>
            <a href="#"><i class="fa fa-lg fa-fw fa-desktop"></i> <span class="menu-item-parent">UI Elements</span></a>
            <ul>
                <li>
                    <a href="ajax/general-elements.html">General Elements</a>
                </li>
                <li>
                    <a href="ajax/buttons.html">Buttons</a>
                </li>
                <li>
                    <a href="#">Icons</a>
                    <ul>
                        <li>
                            <a href="ajax/fa.html"><i class="fa fa-plane"></i> Font Awesome</a>
                        </li>
                        <li>
                            <a href="ajax/glyph.html"><i class="glyphicon glyphicon-plane"></i> Glyph Icons</a>
                        </li>
                        <li>
                            <a href="ajax/flags.html"><i class="fa fa-flag"></i> Flags</a>
                        </li>
                    </ul>
                </li>
                <li>
                    <a href="ajax/grid.html">Grid</a>
                </li>
                <li>
                    <a href="ajax/treeview.html">Tree View</a>
                </li>
                <li>
                    <a href="ajax/nestable-list.html">Nestable Lists</a>
                </li>
                <li>
                    <a href="ajax/jqui.html">JQuery UI</a>
                </li>
                <li>
                    <a href="ajax/typography.html">Typography</a>
                </li>
                <li>
                    <a href="#">Six Level Menu</a>
                    <ul>
                        <li>
                            <a href="#"><i class="fa fa-fw fa-folder-open"></i> Item #2</a>
                            <ul>
                                <li>
                                    <a href="#"><i class="fa fa-fw fa-folder-open"></i> Sub #2.1 </a>
                                    <ul>
                                        <li>
                                            <a href="#"><i class="fa fa-fw fa-file-text"></i> Item #2.1.1</a>
                                        </li>
                                        <li>
                                            <a href="#"><i class="fa fa-fw fa-plus"></i> Expand</a>
                                            <ul>
                                                <li>
                                                    <a href="#"><i class="fa fa-fw fa-file-text"></i> File</a>
                                                </li>
                                                <li>
                                                    <a href="#"><i class="fa fa-fw fa-trash-o"></i> Delete</a></li>
                                            </ul>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-fw fa-folder-open"></i> Item #3</a>

                            <ul>
                                <li>
                                    <a href="#"><i class="fa fa-fw fa-folder-open"></i> 3ed Level </a>
                                    <ul>
                                        <li>
                                            <a href="#"><i class="fa fa-fw fa-file-text"></i> File</a>
                                        </li>
                                        <li>
                                            <a href="#"><i class="fa fa-fw fa-file-text"></i> File</a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="#" class="inactive"><i class="fa fa-fw fa-folder-open"></i> Item #4 (disabled)</a>
                        </li>

                    </ul>
                </li>
            </ul>
        </li>
        -->

        <#--
        <li>
            <a href="ajax/calendar.html"><i class="fa fa-lg fa-fw fa-calendar"><em>3</em></i> <span class="menu-item-parent">Calendar</span></a>
        </li>
        -->

        <#--
        <li>
            <a href="ajax/widgets.html"><i class="fa fa-lg fa-fw fa-list-alt"></i> <span class="menu-item-parent">Widgets</span></a>
        </li>
        -->

        <#--
        <li>
            <a href="#"><i class="fa fa-lg fa-fw fa-puzzle-piece"></i> <span class="menu-item-parent">App Views</span></a>
            <ul>
                <li>
                    <a href="ajax/projects.html"><i class="fa fa-file-text-o"></i> Projects</a>
                </li>
                <li>
                    <a href="ajax/blog.html"><i class="fa fa-paragraph"></i> Blog</a>
                </li>
                <li>
                    <a href="ajax/gallery.html"><i class="fa fa-picture-o"></i> Gallery</a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-comments"></i> Forum Layout</a>
                    <ul>
                        <li><a href="ajax/forum.html">General View</a></li>
                        <li><a href="ajax/forum-topic.html">Topic View</a></li>
                        <li><a href="ajax/forum-post.html">Post View</a></li>
                    </ul>
                </li>
                <li>
                    <a href="ajax/profile.html"><i class="fa fa-group"></i> Profile</a>
                </li>
                <li>
                    <a href="ajax/timeline.html"><i class="fa fa-clock-o"></i> Timeline</a>
                </li>
            </ul>
        </li>
        -->

        <#--
        <li>
            <a href="ajax/gmap-xml.html"><i class="fa fa-lg fa-fw fa-map-marker"></i> <span class="menu-item-parent">GMap Skins</span><span class="badge bg-color-greenLight pull-right inbox-badge">9</span></a>
        </li>
        <li>
            <a href="#"><i class="fa fa-lg fa-fw fa-windows"></i> <span class="menu-item-parent">Miscellaneous</span></a>
            <ul>
                <li>
                    <a href="http://bootstraphunter.com/smartadmin-landing/" target="_blank">Landing Page <i class="fa fa-external-link"></i></a>
                </li>
                <li>
                    <a href="ajax/pricing-table.html">Pricing Tables</a>
                </li>
                <li>
                    <a href="ajax/invoice.html">Invoice</a>
                </li>
                <li>
                    <a href="login.html" target="_top">Login</a>
                </li>
                <li>
                    <a href="register.html" target="_top">Register</a>
                </li>
                <li>
                    <a href="lock.html" target="_top">Locked Screen</a>
                </li>
                <li>
                    <a href="ajax/error404.html">Error 404</a>
                </li>
                <li>
                    <a href="ajax/error500.html">Error 500</a>
                </li>
                <li>
                    <a href="ajax/blank_.html">Blank Page</a>
                </li>
                <li>
                    <a href="ajax/email-template.html">Email Template</a>
                </li>
                <li>
                    <a href="ajax/search.html">Search Page</a>
                </li>
                <li>
                    <a href="ajax/ckeditor.html">CK Editor</a>
                </li>
            </ul>
        </li>
        -->

        <#--
        <li class="top-menu-invisible">
            <a href="#"><i class="fa fa-lg fa-fw fa-cube txt-color-blue"></i> <span class="menu-item-parent">SmartAdmin Intel</span></a>
            <ul>
                <li>
                    <a href="ajax/difver.html"><i class="fa fa-stack-overflow"></i> Different Versions</a>
                </li>
                <li>
                    <a href="ajax/applayout.html"><i class="fa fa-cube"></i> App Settings</a>
                </li>
                <li>
                    <a href="http://192.241.236.31/smartadmin/BUGTRACK/track_/documentation/index.html" target="_blank"><i class="fa fa-book"></i> Documentation</a>
                </li>
                <li>
                    <a href="http://192.241.236.31/smartadmin/BUGTRACK/track_/" target="_blank"><i class="fa fa-bug"></i> Bug Tracker</a>
                </li>
                <li>
                    <a href="http://myorange.ca/supportforum/" target="_blank"><i class="fa fa-wechat"></i> SmartAdmin Support</a>
                </li>
            </ul>
        </li>
        -->
        <#--
                    <li class="chat-users top-menu-invisible">
                        <a href="#"><i class="fa fa-lg fa-fw fa-comment-o"><em class="bg-color-pink flash animated">!</em></i> <span class="menu-item-parent">Smart Chat API <sup>beta</sup></span></a>
                        <ul>
                            <li>
        -->
            <!-- DISPLAY USERS -->
        <#--
                                <div class="display-users">

                                    <input class="form-control chat-user-filter" placeholder="Filter" type="text">

                                    <a href="#" class="usr"
                                       data-chat-id="cha1"
                                       data-chat-fname="Sadi"
                                       data-chat-lname="Orlaf"
                                       data-chat-status="busy"
                                       data-chat-alertmsg="Sadi Orlaf is in a meeting. Please do not disturb!"
                                       data-chat-alertshow="true"
                                       data-rel="popover-hover"
                                       data-placement="right"
                                       data-html="true"
                                       data-content="
                                                    <div class='usr-card'>
                                                        <img src='img/avatars/5.png' alt='Sadi Orlaf'>
                                                        <div class='usr-card-content'>
                                                            <h3>Sadi Orlaf</h3>
                                                            <p>Marketing Executive</p>
                                                        </div>
                                                    </div>
                                                ">
                                        <i></i>Sadi Orlaf
                                    </a>

                                    <a href="#" class="usr"
                                       data-chat-id="cha2"
                                       data-chat-fname="Jessica"
                                       data-chat-lname="Dolof"
                                       data-chat-status="online"
                                       data-chat-alertmsg=""
                                       data-chat-alertshow="false"
                                       data-rel="popover-hover"
                                       data-placement="right"
                                       data-html="true"
                                       data-content="
                                                    <div class='usr-card'>
                                                        <img src='img/avatars/1.png' alt='Jessica Dolof'>
                                                        <div class='usr-card-content'>
                                                            <h3>Jessica Dolof</h3>
                                                            <p>Sales Administrator</p>
                                                        </div>
                                                    </div>
                                                ">
                                        <i></i>Jessica Dolof
                                    </a>

                                    <a href="#" class="usr"
                                       data-chat-id="cha3"
                                       data-chat-fname="Zekarburg"
                                       data-chat-lname="Almandalie"
                                       data-chat-status="online"
                                       data-rel="popover-hover"
                                       data-placement="right"
                                       data-html="true"
                                       data-content="
                                                    <div class='usr-card'>
                                                        <img src='img/avatars/3.png' alt='Zekarburg Almandalie'>
                                                        <div class='usr-card-content'>
                                                            <h3>Zekarburg Almandalie</h3>
                                                            <p>Sales Admin</p>
                                                        </div>
                                                    </div>
                                                ">
                                        <i></i>Zekarburg Almandalie
                                    </a>

                                    <a href="#" class="usr"
                                       data-chat-id="cha4"
                                       data-chat-fname="Barley"
                                       data-chat-lname="Krazurkth"
                                       data-chat-status="away"
                                       data-rel="popover-hover"
                                       data-placement="right"
                                       data-html="true"
                                       data-content="
                                                    <div class='usr-card'>
                                                        <img src='img/avatars/4.png' alt='Barley Krazurkth'>
                                                        <div class='usr-card-content'>
                                                            <h3>Barley Krazurkth</h3>
                                                            <p>Sales Director</p>
                                                        </div>
                                                    </div>
                                                ">
                                        <i></i>Barley Krazurkth
                                    </a>

                                    <a href="#" class="usr offline"
                                       data-chat-id="cha5"
                                       data-chat-fname="Farhana"
                                       data-chat-lname="Amrin"
                                       data-chat-status="incognito"
                                       data-rel="popover-hover"
                                       data-placement="right"
                                       data-html="true"
                                       data-content="
                                                    <div class='usr-card'>
                                                        <img src='img/avatars/female.png' alt='Farhana Amrin'>
                                                        <div class='usr-card-content'>
                                                            <h3>Farhana Amrin</h3>
                                                            <p>Support Admin <small><i class='fa fa-music'></i> Playing Beethoven Classics</small></p>
                                                        </div>
                                                    </div>
                                                ">
                                        <i></i>Farhana Amrin (offline)
                                    </a>

                                    <a href="#" class="usr offline"
                                       data-chat-id="cha6"
                                       data-chat-fname="Lezley"
                                       data-chat-lname="Jacob"
                                       data-chat-status="incognito"
                                       data-rel="popover-hover"
                                       data-placement="right"
                                       data-html="true"
                                       data-content="
                                                    <div class='usr-card'>
                                                        <img src='img/avatars/male.png' alt='Lezley Jacob'>
                                                        <div class='usr-card-content'>
                                                            <h3>Lezley Jacob</h3>
                                                            <p>Sales Director</p>
                                                        </div>
                                                    </div>
                                                ">
                                        <i></i>Lezley Jacob (offline)
                                    </a>

                                    <a href="ajax/chat.html" class="btn btn-xs btn-default btn-block sa-chat-learnmore-btn">About the API</a>

                                </div>
        -->
            <!-- END DISPLAY USERS -->
        <#--
                            </li>
                        </ul>
                    </li>
        -->
        </ul>
    </nav>


<#--
<a href="#ajax/difver.html" class="btn btn-primary nav-demo-btn">AngularJS, PHP and .Net Versions</a>
-->



    <span class="minifyme" data-action="minifyMenu"> <i class="fa fa-arrow-circle-left hit"></i> </span>

</aside>
<#-- END NAVIGATION -->

<#-- MAIN PANEL -->
<div id="main" role="main">
<#-- RIBBON -->
    <div id="ribbon">
		<span class="ribbon-button-alignment">
			<span id="refresh" class="btn btn-ribbon" data-action="resetWidgets" data-title="refresh" rel="tooltip" data-placement="bottom" data-original-title="<i class='text-warning fa fa-warning'></i> 주의! 모든 위젯의 설정이 초기화 됩니다." data-html="true" data-reset-msg="위젯 설정과 로컬 저장소의 모든 데이터를 초기화 합니까?"><i class="fa fa-refresh"></i></span>
		</span>

        <#-- breadcrumb -->
        <ol class="breadcrumb">
            <#-- This is auto generated -->
        </ol>
        <#-- end breadcrumb -->

        <#-- You can also add more buttons to the
        ribbon for further usability

        Example below:

        <span class="ribbon-button-alignment pull-right" style="margin-right:25px">
            <a href="#" id="search" class="btn btn-ribbon hidden-xs" data-title="search"><i class="fa fa-grid"></i> Change Grid</a>
            <span id="add" class="btn btn-ribbon hidden-xs" data-title="add"><i class="fa fa-plus"></i> Add</span>
            <button id="search" class="btn btn-ribbon" data-title="search"><i class="fa fa-search"></i> <span class="hidden-mobile">Search</span></button>
        </span> -->

    </div>
    <#-- END RIBBON -->

    <#-- #MAIN CONTENT -->
    <div id="content">

    </div>
    <#-- END #MAIN CONTENT -->

</div>
<#-- END #MAIN PANEL -->

<#-- #PAGE FOOTER -->
<div class="page-footer">
    <div class="row">
        <div class="col-xs-12 col-sm-6">
            <span class="txt-color-white">${BACKOFFICE_VERSION} <span class="hidden-xs"> - CKPush 3.0</span> © 2015</span>
        </div>

        <div class="col-xs-6 col-sm-6 text-right hidden-xs">
            <div class="txt-color-white inline-block">
                <i class="txt-color-blueLight hidden-mobile"><i class="fa fa-clock-o"></i> <strong id="my-session-end-time">세션 만료 시간 계산 중</strong> <span id="my-session-end-time2"></span>&nbsp;&nbsp;</i>

                <#-- 서버 모니터링은 대시보드에서 하는 것으로 한다.
                <div class="btn-group dropup">
                    <button class="btn btn-xs dropdown-toggle bg-color-blue txt-color-white" data-toggle="dropdown">
                        <i class="fa fa-link"></i> <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu pull-right text-left">
                        <li>
                            <div class="padding-5">
                                <p class="txt-color-darken font-sm no-margin">Download Progress</p>
                                <div class="progress progress-micro no-margin">
                                    <div class="progress-bar progress-bar-success" style="width: 50%;"></div>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="padding-5">
                                <p class="txt-color-darken font-sm no-margin">Server Load</p>
                                <div class="progress progress-micro no-margin">
                                    <div class="progress-bar progress-bar-success" style="width: 20%;"></div>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li >
                            <div class="padding-5">
                                <p class="txt-color-darken font-sm no-margin">Memory Load <span class="text-danger">*critical*</span></p>
                                <div class="progress progress-micro no-margin">
                                    <div class="progress-bar progress-bar-danger" style="width: 70%;"></div>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="padding-5">
                                <button class="btn btn-block btn-default">refresh</button>
                            </div>
                        </li>
                    </ul>
                </div>
                -->
                <#-- end btn-group-->
            </div>
            <#-- end div-->
        </div>
        <#-- end col -->
    </div>
    <#-- end row -->
</div>
<#-- END FOOTER -->

<#-- #SHORTCUT AREA : With large tiles (activated via clicking user name tag)
     Note: These tiles are completely responsive, you can add as many as you like -->
<div id="shortcut">
    <ul>
        <li>
            <a href="#ajax/inbox.html" class="jarvismetro-tile big-cubes bg-color-blue"> <span class="iconbox"> <i class="fa fa-envelope fa-4x"></i> <span>Mail <span class="label pull-right bg-color-darken">14</span></span> </span> </a>
        </li>
        <li>
            <a href="#ajax/calendar.html" class="jarvismetro-tile big-cubes bg-color-orangeDark"> <span class="iconbox"> <i class="fa fa-calendar fa-4x"></i> <span>Calendar</span> </span> </a>
        </li>
        <li>
            <a href="#ajax/gmap-xml.html" class="jarvismetro-tile big-cubes bg-color-purple"> <span class="iconbox"> <i class="fa fa-map-marker fa-4x"></i> <span>Maps</span> </span> </a>
        </li>
        <li>
            <a href="#ajax/invoice.html" class="jarvismetro-tile big-cubes bg-color-blueDark"> <span class="iconbox"> <i class="fa fa-book fa-4x"></i> <span>Invoice <span class="label pull-right bg-color-darken">99</span></span> </span> </a>
        </li>
        <li>
            <a href="#ajax/gallery.html" class="jarvismetro-tile big-cubes bg-color-greenLight"> <span class="iconbox"> <i class="fa fa-picture-o fa-4x"></i> <span>Gallery </span> </span> </a>
        </li>
        <li>
            <a href="#ajax/profile.html" class="jarvismetro-tile big-cubes selected bg-color-pinkDark"> <span class="iconbox"> <i class="fa fa-user fa-4x"></i> <span>My Profile </span> </span> </a>
        </li>
    </ul>
</div>
<#-- END SHORTCUT AREA -->

<#--================================================== -->

<#-- PACE LOADER - turn this on if you want ajax loading to show (caution: uses lots of memory on iDevices)
<script data-pace-options='{ "restartOnRequestAfter": true }' src="js/plugin/pace/pace.min.js"></script>-->


<#-- PLUGINS -->
<#-- Link to Google CDN's jQuery + jQueryUI; fall back to local -->
<#--
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script>
    if (!window.jQuery) {
        document.write('<script src="js/libs/jquery-2.1.1.min.js"><\/script>');
    }
</script>
-->
<#--
<script src="${Request.resPath}js/libs/jquery-2.1.1.min.js"></script>
-->

<#--
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script>
    if (!window.jQuery.ui) {
        document.write('<script src="js/libs/jquery-ui-1.10.3.min.js"><\/script>');
    }
</script>
-->
<#--
<script src="${Request.resPath}js/libs/jquery-ui-1.10.3.min.js"></script>
-->

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
<script src="${Request.resPath}js/app.min.js"></script>

<#-- custom javascript -->
<script src="${Request.resPath}js/ckpush/my.js"></script>

<#-- Smart Admin 의 기본 값들을 재정의 한다 -->
<script src="${Request.resPath}js/ckpush/overwrite.js"></script>

<#-- ENHANCEMENT PLUGINS : NOT A REQUIREMENT -->
<#-- Voice command : plugin -->
<script src="${Request.resPath}js/speech/voicecommand.min.js"></script>

<#-- ras js file -->
<#--
<script src="${Request.resPath}js/ckpush/ras/socket.io.js"></script>
<script src="${Request.resPath}js/ckpush/ras/ras-config.js"></script>
<script src="${Request.resPath}js/ckpush/ras/ras-model.js"></script>
<script src="${Request.resPath}js/ckpush/ras/ras-user-ui.js"></script>
<script src="${Request.resPath}js/ckpush/ras/ras-user-console.js"></script>
<script type="text/javascript">
    // 인증키를 주면 인증키로 돌아가고, 인증 object 를 주면 인증 object 로 돌아간다.
    // 존재하지 않는 것에 대해서는 ras-config.js에 정의된 값을 사용 한다.
    //_ckras.model.init('1234.');
    _ckras.model.init();
</script>
-->

<#-- TODO 추후 채팅 준비가 완료되면 넣도록 하자 -->
<#-- SmartChat UI : plugin
<script src="js/smart-chat-ui/smart.chat.ui.min.js"></script>
<script src="js/smart-chat-ui/smart.chat.manager.min.js"></script> -->

<#-- TODO google 통계 준비 되면 넣도록 하자 -->

<#-- Your GOOGLE ANALYTICS CODE Below
<script type="text/javascript">

    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-XXXXXXXX-X']);
    _gaq.push(['_trackPageview']);

    (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();
</script>
-->

</body>

</html>