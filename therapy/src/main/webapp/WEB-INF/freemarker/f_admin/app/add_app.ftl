<#assign _page_id="add-app">
<#assign _page_parent_name="앱">
<#assign _page_current_name="앱 생성">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
    <p>
        GCM 또는 APNS는 앱 별로 Push ID가 할당 되기 때문에 CKPush를 사용하기 위해서는 서비스하는 앱을 생성 합니다.
        생성된 앱은 API Key 을 이용하여 분류 되며 동일한 서비스를 제공하는 앱이라도 단말기(안드로이드폰, 아이폰 등등)로 구분하여 생성 해야 합니다.
    </p>
    <p>
        생성된 앱은 <a href="#${Request.contextPath}/admin/app/list" title="앱 목록">앱 목록</a> 메뉴에서 확인 가능 합니다.
    </p>
</@am.page_desc>

<#-- widget grid -->
<section id="widget-grid" class="">
    <#-- row -->
    <div class="row">
        <#-- NEW WIDGET START -->
        <article class="col-sm-12 col-md-12 col-lg-12">
            <#-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false"
                    data-widget-deletebutton="false">
                <@am.widget_title icon="fa-edit">앱 만들기</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <form id="checkout-form">
                            <fieldset>
                                <legend>생성할 앱 정보를 입력 합니다.</legend>

                                <div class="row">
                                    <@am.bstrap_in_txt name="app_id" value="${appEntity.app_id!''}" title="앱 아이디"
                                            maxlength="128" placeholder="숫자/영문자로 최대 128자 까지 가능 합니다" />

                                    <@am.bstrap_in_txt name="app_name" value="${appEntity.app_name!''}" title="앱 이름"
                                            maxlength="64" placeholder="앱 이름은 최대 64 bytes 까지 가능 합니다" />

                                    <@am.bstrap_in_txt name="app_version" value="${appEntity.app_version!''}" title="앱 버전"
                                            maxlength="16" placeholder="1.0.0" />

                                    <@am.bstrap_in_txt_rbtn name="api_key" value="${appEntity.api_key!''}" title="Api Key"
                                            maxlength="32" placeholder="숫자/영문자로 최대 32자 까지 가능 합니다" btn="생성" />

                                    <@am.bstrap_in_txt_rbtn name="api_secret" value="${appEntity.api_secret!''}" title="Api Secret"
                                            maxlength="32" placeholder="숫자/영문자로 최대 32자 까지 가능 합니다" btn="생성" />
                                </div>
                            </fieldset>

                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 앱 추가
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                    <#-- end widget content -->
                </div>
                <#-- end widget div -->
            </div>
            <#-- end widget -->
        </article>
        <#-- WIDGET END -->
    </div>
    <#-- end row -->
</section>
<#-- end widget grid -->

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        <#-- my.fn.showSessionTime(${sessionRestSec?string("0.#")}); -->
        my.fn.showSessionTime(${sessionRestSec});

        <#-- 페이지 도움말 영역 visible, invisible 처리 -->
        my.fn.initPageHelper('${_page_id}-helper');

        <#-- input form validator -->
        my.fn.addValidator('regexp');
        $("#checkout-form").validate({
            rules: {
                app_id:     {required: true, maxlength: 64, regexp: /^[0-9a-zA-Z]+(\.[0-9a-zA-Z]+)*/},
                app_name:   {required: true, minlength: 1, maxlength: 64},
                app_version:{required: true, regexp: /^[0-9]+(\.[0-9]+){0,2}/},
                api_key:    {required: true, minlength: 1, maxlength: 32, regexp: /[a-zA-Z0-9]{1,32}/},
                api_secret: {required: true, minlength: 1, maxlength: 32}
            },
            messages : {
                app_id: {
                    required: '앱 아이디는 필수값 입니다.',
                    maxlength: '앱 아이디는 {0} 자 이하 입니다.',
                    regexp: '앱 아이디는 숫자/영문자 및 점으로 시작/종료 되면 안 됩니다.'
                },
                app_name: {
                    required: '앱 이름은 필수값 입니다.',
                    minlength: '앱 이름은 {0} 자 이상 입니다.',
                    maxlength: '앱 이름은 {0} 자 이하 입니다.'
                },
                app_version: {
                    required: '앱 버전은 필수값 입니다.',
                    regexp: '앱 버전의 포맷은 x.y.z 형태 이어야 합니다.'
                },
                api_key: {
                    required: 'Api Key는 필수값 입니다.',
                    minlength: 'Api Key는 {0}자 이상 입니다.',
                    maxlength: 'Api Key는 {0}자 이하 입니다.',
                    regexp: 'Api Key는 길이 1 이상의 영어 대소문자 및 숫자값 입니다.'
                },
                api_secret: {
                    required: 'Api Secret는 필수값 입니다.',
                    minlength: 'Api Secret는 {0}자 이상 입니다.',
                    maxlength: 'Api Secret는 {0}자 이하 입니다.'
                }
            },
            <@am.jsbstrap_validator_comm />
        });

        <#-- 생성 버튼을 눌렀을때 api key 나 api secret 를 생성 한다 -->
        $('.bstrap-in-txt-rbtn').click(function(e){
            var $this = $(this);
            var destName = $this.data('relation');

            <#-- uuid 발급 요청 -->
            <#--<@am.jsajax_request submituri="${Request.contextPath}/admin/open/uuid/t/${.now?long?string('##0')}"-->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/open/uuid/t/"
                    reqdata="" method="GET" errortitle="UUID 발급 실패" ; job >
                <#if job == "success_job">
                    <#-- 성공 후 처리할 작업 -->
                    $('input[name='+destName+']').val(data.uuid);
                <#elseif job == "common_job"></#if>
            </@am.jsajax_request>
        });

        <#-- 신규 앱을 등록 한다 -->
        $('#btn-add').click(function(e){
            <#-- event bubbling prevent -->
            <@am.jsevent_prevent />

            <#-- jquery validator -->
            var check = $('#checkout-form').valid();
            if(!check) { return; }

            <#-- ajax 요청 하기 전에 사전 작업 -->
            $('.invalid').hide();
            var $btnAdd = $(this);
            $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 추가중');

            <#-- request 할 데이터를 만든다.(reqData 라는 이름을 왠만하면 바꾸지 말자. 이름을 그대로 사용해야 macro 사용이 편해짐) -->
            var reqData = {
                app_id:         $('input[name=app_id]').val(),
                app_name:       $('input[name=app_name]').val(),
                app_version:    $('input[name=app_version]').val(),
                api_key:        $('input[name=api_key]').val(),
                api_secret:     $('input[name=api_secret]').val(),
                enabled:        1
            };

            <#-- app 추가 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/app/add/t/"
                    moveuri="#${Request.contextPath}/admin/app/list" errortitle="앱 추가 실패" ; job >
                <#if job == "success_job">
                    <#-- 앱 추가 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = reqData.app_name + '를 추가 하였습니다.';
                    <@am.jsnoti title="앱 추가 성공" content="toastMsg" boxtype="success" />

                    <#-- 앱 추가 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                    my.fn.removeDatatableStorage('list-app');

                <#elseif job == "common_job">
                    <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 앱 추가');
                </#if>
            </@am.jsajax_request>
        });
    };

    pagefunction();
</script>
