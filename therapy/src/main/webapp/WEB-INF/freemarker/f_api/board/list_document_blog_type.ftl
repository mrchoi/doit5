<#--
    첨부된 이미지 파일과 본문 내용을 리스트에 그대로 보여 주는 블로그 형태의 리스트
-->
<#import "../../f_comm/api_macro.ftl" as mm>

<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0 minimal-ui"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="${Request.svcResPath}img/splash/splash-114x114.png">
    <link rel="apple-touch-startup-image" href="${Request.svcResPath}img/splash/splash-320x460.png" media="screen and (max-device-width: 320px)" />
    <link rel="apple-touch-startup-image" href="${Request.svcResPath}img/splash/splash-640x920.png" media="(max-device-width: 480px) and (-webkit-min-device-pixel-ratio: 2)" />
    <link rel="apple-touch-startup-image" sizes="640x1096" href="${Request.svcResPath}img/splash/splash-640x1096.png" />
    <link rel="apple-touch-startup-image" sizes="1024x748" href="${Request.svcResPath}img/splash/splash-1024x748.png" media="screen and (min-device-width : 481px) and (max-device-width : 1024px) and (orientation : landscape)" />
    <link rel="apple-touch-startup-image" sizes="768x1004" href="${Request.svcResPath}img/splash/splash-768x1004.png" media="screen and (min-device-width : 481px) and (max-device-width : 1024px) and (orientation : portrait)" />
    <link rel="apple-touch-startup-image" sizes="1536x2008" href="${Request.svcResPath}img/splash/splash-1536x2008.png" media="(device-width: 768px) and (orientation: portrait) and (-webkit-device-pixel-ratio: 2)"/>
    <link rel="apple-touch-startup-image" sizes="2048x1496" href="${Request.svcResPath}img/splash/splash-2048x1496.png" media="(device-width: 768px) and (orientation: landscape) and (-webkit-device-pixel-ratio: 2)"/>

    <title>${category_name} (${currentPage+1}/${totalPage})</title>

    <link href="${Request.svcResPath}css/style.css" rel="stylesheet" type="text/css">
    <link href="${Request.svcResPath}css/framework.css" rel="stylesheet" type="text/css">
    <link href="${Request.svcResPath}css/owl.theme.css" rel="stylesheet" type="text/css">
    <link href="${Request.svcResPath}css/swipebox.css" rel="stylesheet" type="text/css">
    <link href="${Request.svcResPath}css/font-awesome.css" rel="stylesheet" type="text/css">
    <link href="${Request.svcResPath}css/animate.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="${Request.svcResPath}js/jquery.js"></script>
    <script type="text/javascript" src="${Request.svcResPath}js/jqueryui.js"></script>
    <script type="text/javascript" src="${Request.svcResPath}js/framework.plugins.js"></script>
    <script type="text/javascript" src="${Request.svcResPath}js/custom.js"></script>
    <script type="text/javascript" src="${Request.svcResPath}js/framework.js"></script>
</head>
<body>
    <div id="preloader disabled">
        <div id="status">
            <p class="center-text">
                페이지 로딩중 ...
            </p>
        </div>
    </div>

    <div class="all-elements">
        <div class="snap-drawers">
            <#-- Left Sidebar-->
            <div class="snap-drawer snap-drawer-left">
                <#--Sidebar Header-->
                <div class="sidebar-header">
                    <#--<a href="#" class="sidebar-logo"></a>-->
                    <a class="sidebar-logo"></a>
                    <a class="sidebar-close ckpush-svc-cursor-point"><i class="fa fa-times"></i></a>
                </div>
                <div class="deco"></div>

                <#--Page Breadcrumb-->
                <#--
                <div class="breadcrumb">메뉴</div>
                <div class="deco"></div>
                -->

                <#list board as boardItem>
                    <#assign active_submenu="">
                    <#assign rotate_plus="">
                    <#if board_srl == boardItem.board_srl>
                        <#assign active_submenu="active-submenu">
                        <#assign rotate_plus="rotate-plus">
                    </#if>

                    <div class="has-submenu ${active_submenu}">
                        <a class="menu-item ckpush-svc-cursor-point">
                            <i class="fa fa-th"></i>
                            <em>${boardItem.board_name}</em>
                            <i class="fa ${rotate_plus} fa-plus"></i>
                        </a>
                        <div class="submenu ${active_submenu}">
                            <#list boardItem.categories as categoryItem>
                                <#assign active_menu="">
                                <#if categoryItem.category_srl == category_srl>
                                    <#assign active_menu="active-menu">
                                </#if>

                                <div class="deco"></div>
                                <a class="menu-item ${active_menu}" href="${Request.contextPath}/api/open/document/list/category/${categoryItem.category_srl}/pg/0/t/1">
                                    <i class="fa fa-angle-right"></i><em>${categoryItem.category_name}</em><i class="fa fa-circle"></i>
                                </a>
                            </#list>
                        </div>
                    </div>
                    <div class="deco"></div>
                </#list>

                <#-- Page Breadcrumb -->
                <div class="breadcrumb">${BACKOFFICE_VERSION} - CKPush 3.0 © 2015</div>
                <div class="deco"></div>
            </div>

            <#-- Right Sidebar -->
            <div class="snap-drawer snap-drawer-right">
                <a class="sidebar-close right-sidebar-close ckpush-svc-cursor-point"><i class="fa fa-times"></i></a>
                <div class="clear"></div>
                <div class="deco"></div>

                <a class="social-item ckpush-svc-cursor-point ckpush-svc-page-refresh"><i class="fa fa-refresh"></i>새로고침</a>
                <div class="deco"></div>

                <a class="social-item ckpush-svc-cursor-point ckpush-svc-scroll-top"><i class="fa fa-arrow-up"></i>최상단</a>
                <div class="deco"></div>

                <#if currentPage+1 gt 1>
                    <a class="social-item" href="${Request.contextPath}/api/open/document/list/category/${category_srl}/pg/0/t/1"><i class="fa fa-angle-double-left"></i>처음</a>
                    <div class="deco"></div>

                    <a class="social-item" href="${Request.contextPath}/api/open/document/list/category/${category_srl}/pg/${currentPage-1}/t/1"><i class="fa fa-angle-left"></i>이전</a>
                    <div class="deco"></div>
                </#if>

                <#if currentPage+1 lt totalPage>
                    <a class="social-item" href="${Request.contextPath}/api/open/document/list/category/${category_srl}/pg/${currentPage+1}/t/1"><i class="fa fa-angle-right"></i>다음</a>
                    <div class="deco"></div>

                    <a class="social-item" href="${Request.contextPath}/api/open/document/list/category/${category_srl}/pg/${totalPage-1}/t/1"><i class="fa fa-angle-double-right"></i>끝</a>
                    <div class="deco"></div>
                </#if>
            </div>
        </div>

        <#-- Page Header -->
        <div class="header">
            <a class="open-nav ckpush-svc-cursor-point"><i class="fa fa-navicon"></i></a>
            <#--<a class="header-logo" href="#"></a>-->
            <a class="header-message">${category_name} (${currentPage+1}/${totalPage})</a>
            <a class="open-socials ckpush-svc-cursor-point"><i class="fa fa-anchor"></i></a>
        </div>

        <!-- Page Content-->
        <div id="content" class="snap-content">
            <div class="full-bottom"></div>
            <div class="content">
                <div class="portfolio-one">
                    <#list documents as item>
                        <div class="portfolio-one-item full-bottom">
                            <div class="portfolio-one-image">
                                <#if item.file_count gt 0>
                                    <#--
                                    <a href="${item.attach[0].file_url}" class="swipebox" title="Caption Here">
                                        <i class="fa fa-plus"></i>
                                    </a>
                                    -->
                                    <img src="${item.attach[0].file_url}" class="responsive-image">
                                <#else>
                                    <img src="${Request.svcResPath}img/no_thumbnail.png" class="responsive-image">
                                </#if>
                            </div>
                            <div class="portfolio-one-text">
                                <h3 class="title">${item.title}</h3>
                                <em class="subtitle">${item.user_id}</em>
                                <div class="portfolio-one-details">
                                    <#-- 시간 변환은 javascript로 처리 했을때 layout 깨지는 문제가 있어서 freemarker에서 처리 한다 -->
                                    <#--<a class="ckpush-svc-to-human-time">${item.c_date}</a>-->
                                    <a><@mm.document_time1 unix_timestamp=item.c_date /></a>
                                    <a><i class="fa fa-eye"></i> ${item.read_count}</a>
                                    <a><i class="fa fa-thumbs-o-up"></i> ${item.like_count}</a>
                                    <a><i class="fa fa-comment-o"></i> ${item.comment_count}</a>
                                </div>
                                <section class="half-bottom">${item.content}</section>
                                <#--
                                <div class="portfolio-one-links">
                                    <a href="#"><i class="fa fa-arrow-right"></i> Visit Project</a>
                                    <a href="#">Details <i class="fa fa-link"></i></a>
                                </div>
                                -->
                            </div>
                        </div>
                        <div class="decoration"></div>
                    </#list>
                </div>


                <#-- Page Footer-->
                <div class="footer">
                    <#--
                    <div class="footer-socials half-bottom">
                        <a href="#" class="footer-facebook"><i class="fa fa-facebook"></i></a>
                        <a href="#" class="footer-twitter"><i class="fa fa-twitter"></i></a>
                        <a href="#" class="footer-google"><i class="fa fa-google-plus"></i></a>
                        <a href="#" class="footer-up"><i class="fa fa-angle-up"></i></a>
                    </div>
                    -->
                    <p class="center-text">${BACKOFFICE_VERSION} - CKPush 3.0 © 2015</p>
                </div>
            </div>
        </div>
    </div>
</body>
</html>