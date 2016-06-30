<!DOCTYPE html>
<#--<html lang="en-us" id="extr-page">-->
<html id="extr-page">
<head>
    <meta charset="utf-8">
    <title>PlyMind Admin</title>
    <meta name="description" content="CKPush 관리툴의 로그인 페이지">
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
    <link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/smartadmin-rtl.min.css"> -->

    <#-- We recommend you use "your_style.css" to override SmartAdmin
        specific styles this will also ensure you retrain your customization with each SmartAdmin update.
    <link rel="stylesheet" type="text/css" media="screen" href="css/your_style.css"> -->

    <#-- Demo purpose only: goes with demo.js, you can delete this css when designing your own WebApp
    <link rel="stylesheet" type="text/css" media="screen" href="css/demo.min.css"> -->

    <#-- FAVICONS -->
    <link rel="shortcut icon" href="${Request.resPath}img/favicon/favicon.ico" type="image/x-icon">
    <link rel="icon" href="${Request.resPath}img/favicon/favicon.ico" type="image/x-icon">

    <#-- GOOGLE FONT -->
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,700italic,300,400,700">

    <#-- APP SCREEN / ICONS -->
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
    <!-- ipad landscape -->
    <link rel="apple-touch-startup-image" href="${Request.resPath}img/splash/splash-1024x784.png" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:landscape)">
    <!-- ipad portrait -->
    <link rel="apple-touch-startup-image" href="${Request.resPath}img/splash/splash-784x1024.png" media="screen and (min-device-width: 481px) and (max-device-width: 1024px) and (orientation:portrait)">
    <!-- iphone -->
    <link rel="apple-touch-startup-image" href="${Request.resPath}img/splash/splash-320x460.png" media="screen and (max-device-width: 320px)">
</head>

<body class="animated fadeInDown">

<header id="header">
    <div id="logo-group">
        <span id="logo"> <img src="${Request.resPath}img/logo.png" alt="CKPush BackOffice Tool"> </span>
    </div>
    <#--<span id="extr-page-header-space"> <span class="hidden-mobile hidden-xs">새로운 관리자를 추가 하겠습니까?</span> <a href="${Request.contextPath}/admin/open/register" class="btn btn-danger">관리자 추가</a> </span>-->
</header>

<#-- 로그인 실패나 세션 만료 등으로 인해 로그인 페이지 접근시 에러 메시지를 보여 준다 -->
<#if RequestParameters.error??>
    <div class="alert alert-danger fade in" style="margin-bottom: 0;">
        <i class="fa-fw fa fa-times"></i>
        <#if RequestParameters.error == '1'>
            <strong>Error!</strong> ${(SPRING_SECURITY_LAST_EXCEPTION.message)!"로그인 실패 했습니다."}
        <#elseif RequestParameters.error == '2'>
            <strong>Error!</strong> 세션 정보가 만료 되었습니다. 다시 로그인 하세요.
        <#elseif RequestParameters.error == '3'>
            <strong>Error!</strong> 접근 권한이 없는 사용자 입니다.
        <#else>
            <strong>Error!</strong> 로그인 만료 되었습니다. 다시 로그인 하세요.
        </#if>
    </div>
</#if>

<div id="main" role="main">
    <#-- MAIN CONTENT -->
    <div id="content" class="container">
        <div class="row">
            <div class="col-xs-12 col-sm-12 col-md-7 col-lg-8 hidden-xs hidden-sm">
                <h1 class="txt-color-red login-header-big">PlyMind Admin</h1>
                <div class="hero">
                    <div class="pull-left login-desc-box-l">
                        <h4 class="paragraph-header" style="width:300px;">
                            PLYMIND 서비스 통합 관리를 제공 합니다.<br><br>
                            플리마인드(PlyMind: PLay in Your Mind)는<br>
                            당신의 Mind에<br>
                            공감을 Play하고,<br>
                            위안을 Play하고,<br>
                            행복을 Play하고,<br>
                            사소한 배려로 Mind를 선물하고자 합니다.
                        </h4>
                        <#--
                        <div class="login-app-icons">
                            <a href="javascript:void(0);" class="btn btn-danger btn-sm">Frontend Template</a>
                            <a href="javascript:void(0);" class="btn btn-danger btn-sm">Find out more</a>
                        </div>
                        -->
                    </div>
                    <#--<img src="${Request.resPath}img/demo/iphoneview.png" class="pull-right display-image" alt="" style="width:210px">-->
                </div>

                <div class="row">
                    <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                        <#--<h5 class="about-heading">CKPush Backoffice - 통합 관리</h5>
                        <p>
                            애플리케이션에 Push 메시지및 실시간 메시지 서비스를 CKPush Backoffice를 활용하여 쉽게 적용 하세요. CKPush Backoffice는 관리자의 업무 효율 향상에 많은 도움을 드립니다.
                        </p>-->
                    </div>
                    <div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
                        <#--<h5 class="about-heading">CKPush Developer Console - 개발 환경</h5>
                        <p>
                            CKPush Backoffice 는 관리자에 특화된 기능을 제공 합니다. 만일 개발자 기능이 필요하면 <a href="http://sweet-dev.alysumm.com:20001/console/" target="_blank">CKPush Developer Console</a> 을 통해서 사용 하십시요.
                        </p>-->
                    </div>
                </div>
            </div>

            <div class="col-xs-12 col-sm-12 col-md-5 col-lg-4">
                <div class="well no-padding">
                    <form action="${Request.contextPath}/admin/open/authentication" id="login-form" class="smart-form client-form" method="post">
                        <input type="hidden" name="redirecthash" value="">
                        <header>
                            로그인
                        </header>

                        <fieldset>
                            <section>
                                <label class="label">아이디</label>
                                <label class="input"> <i class="icon-append fa fa-user"></i>
                                    <input type="text" name="user_id">
                                    <b class="tooltip tooltip-top-right"><i class="fa fa-user txt-color-teal"></i> 아이디를 입력하세요</b></label>
                            </section>

                            <section>
                                <input type="hidden" name="user_password">
                                <label class="label">패스워드</label>
                                <label class="input"> <i class="icon-append fa fa-lock"></i>
                                    <input type="password" name="password">
                                    <b class="tooltip tooltip-top-right"><i class="fa fa-lock txt-color-teal"></i> 패스워드를 입력하세요</b>
                                </label>

                                <#-- TODO 패스워드 찾기를 넣고 난 다음 아래 항목을 넣어야 한다.
                                <div class="note">
                                    <a href="forgotpassword.html">패스워드를 잊었습니까?</a>
                                </div>
                                -->
                            </section>

                            <#--
                            <section>
                                <label class="checkbox">
                                    <input type="checkbox" name="remember" checked="">
                                    <i></i>Stay signed in</label>
                            </section>
                            -->
                        </fieldset>
                        <footer>
                            <button type="submit" class="btn btn-primary">
                                로그인
                            </button>
                        </footer>
                    </form>
                </div>

                <#-- TODO social 연동을 넣고 난 다음 아래 항목을 넣어야 한다.
                <h5 class="text-center"> - Or sign in using -</h5>

                <ul class="list-inline text-center">
                    <li>
                        <a href="javascript:void(0);" class="btn btn-primary btn-circle"><i class="fa fa-facebook"></i></a>
                    </li>
                    <li>
                        <a href="javascript:void(0);" class="btn btn-info btn-circle"><i class="fa fa-twitter"></i></a>
                    </li>
                    <li>
                        <a href="javascript:void(0);" class="btn btn-warning btn-circle"><i class="fa fa-linkedin"></i></a>
                    </li>
                </ul>
                -->
            </div>
        </div>
    </div>
</div>

<#--================================================== -->

<#-- PACE LOADER - turn this on if you want ajax loading to show (caution: uses lots of memory on iDevices)-->
<script src="${Request.resPath}js/plugin/pace/pace.min.js"></script>

<#-- Link to Google CDN's jQuery + jQueryUI; fall back to local -->
<#--
<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script> if (!window.jQuery) { document.write('<script src="js/libs/jquery-2.1.1.min.js"><\/script>');} </script> -->
<script src="${Request.resPath}js/libs/jquery-2.1.1.min.js"></script>

<#--
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script> if (!window.jQuery.ui) { document.write('<script src="js/libs/jquery-ui-1.10.3.min.js"><\/script>');} </script> -->
<script src="${Request.resPath}js/libs/jquery-ui-1.10.3.min.js"></script>

<#-- IMPORTANT: APP CONFIG -->
<script src="${Request.resPath}js/app.config.js"></script>

<#-- JS TOUCH : include this plugin for mobile drag / drop touch events
<script src="js/plugin/jquery-touch/jquery.ui.touch-punch.min.js"></script> -->

<#-- BOOTSTRAP JS -->
<script src="${Request.resPath}js/bootstrap/bootstrap.min.js"></script>

<#-- JQUERY VALIDATE -->
<script src="${Request.resPath}js/plugin/jquery-validate/jquery.validate.min.js"></script>

<#-- JQUERY MASKED INPUT -->
<script src="${Request.resPath}js/plugin/masked-input/jquery.maskedinput.min.js"></script>

<#-- JQUERY COOKIE -->
<script src="${Request.resPath}js/plugin/jquery-cookie/jquery.cookie.min.js"></script>

<#-- md5 -->
<script src="${Request.resPath}js/ckpush/md5.js"></script>

<!--[if IE 8]>

<h1>Your browser is out of date, please update your browser by going to www.microsoft.com/download</h1>

<![endif]-->

<#-- MAIN APP JS FILE -->
<script src="${Request.resPath}js/app.min.js"></script>

<script type="text/javascript">
    runAllForms();

    $(function() {
        // Validation
        $("#login-form").validate({
            // Rules for form validation
            rules : {
                user_id : {
                    required : true
                },
                password : {
                    required : true,
                    minlength : 3,
                    maxlength : 20
                }
            },

            // Messages for form validation
            messages : {
                user_id : {
                    required : '아이디를 입력 하세요'
                },
                password : {
                    required : '패스워드를 입력하세요'
                }
            },

            // Do not change code below
            errorPlacement : function(error, element) {
                error.insertAfter(element.parent());
            },

            submitHandler: function(form) {
                // 로그인 후 특정 페이지로 redirection 하기 위한 로직
                var hashTagIndex = (window.location.href).indexOf("#");
                if(hashTagIndex > 0) {
                    var hashTagValue = (window.location.href).substring(hashTagIndex+1);
                    $('input[name=redirecthash]').val($.trim(hashTagValue));
                } else {
                    $('input[name=redirecthash]').val('');
                }

                var hash = CryptoJS.MD5($.trim($('input[name=password]').val()));
                $('input[name=user_password]').val(hash).toString();
                form.submit();
            }
        });
    });
</script>

</body>
</html>