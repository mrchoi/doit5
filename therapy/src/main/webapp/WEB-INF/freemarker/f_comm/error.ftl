<!DOCTYPE html>
<html id="extr-page">
<head>
    <meta charset="utf-8">
    <title>Response</title>
    <meta name="description" content="Response">
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
</head>

<#import "api_macro.ftl" as mm>

<body class="animated fadeInDown">

<div id="main" role="main">
    <div id="content">
        <section id="widget-grid" class="">
            <div class="row">
                <article class="col-sm-12 col-md-12 col-lg-12">
                    <div class="jarviswidget" id="wid-id-0" data-widget-colorbutton="false" data-widget-editbutton="false">
                        <@mm.header>[API] ${ck_method} ${ck_request_uri}</@mm.header>
                        <div>
                            <div class="jarviswidget-editbox"></div>
                            <div class="widget-body">
                                <form class="form-horizontal">
                                    <fieldset>
                                        <@mm.coresp tid="${ck_tid}" error="${ck_error}" message="${ck_message}" />

                                        <#-- 실패 했다면 원인 찍기 -->
                                        <@mm.csresp reason=ck_reason />
                                    </fieldset>
                                </form>
                            </div>
                        </div>
                    </div>
                </article>
            </div>
        </section>
    </div>
</div>

</body>
</html>