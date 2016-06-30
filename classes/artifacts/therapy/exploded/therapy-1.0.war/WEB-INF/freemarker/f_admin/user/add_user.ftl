<#assign _page_id="add-user">
<#assign _page_parent_name="사용자">
<#assign _page_current_name="사용자 추가">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    서비스 사용자를 추가 합니다. 만일 회원을 사용하는 Backoffice가 아니라면 Backoffice 관리자 계정 추가만 유효 합니다. 소셜로 인증 받아 가입하는 사용자는 서비스에서 가입 가능하며,
    관리자가 임의로 추가 할 수 없습니다. 또한, 관리자 계정은 소셜 인증으로 가입이 불가능 합니다. 사용자 별명을 추가 하지 않으면 사용자 이름과 동일하게 설정 됩니다.
</p>
<#if "${backoffice_mode}" == "1">
<p>
    사용자는 Backoffice 기본 권한 관리, 각 앱 당 권한 관리 총 2단계로 권한 구분이 됩니다. Backoffice의 계정은 관리자와 일반 회원으로 구분 됩니다.
    일반 회원은 앱을 생성한 사용자나 가입자로써 각각의 앱 별로 앱 관리자, 앱 정회원, 앱 준회원 추가 구분 됩니다.
</p>
</#if>
<p>
    추가된 사용자는 <a href="#${Request.contextPath}/admin/member/list" title="사용자 목록">사용자 목록</a> 메뉴에서 확인 가능 합니다.
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
                <@am.widget_title icon="fa-edit">사용자 추가</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <form id="checkout-form">
                            <fieldset>
                                <legend>추가할 사용자 정보를 입력 합니다.</legend>

                                <div class="row">
                                    <@am.bstrap_in_txt name="user_id" value="" title="사용자 아이디"
                                            maxlength="128" placeholder="최대 128자 까지 가능 합니다." />

                                    <@am.bstrap_in_txt name="email_address" value="" title="메일 주소"
                                            maxlength="128" placeholder="숫자/영문자로 최대 128자 까지 가능 합니다" />

                                    <@am.bstrap_in_txt name="user_password" value="" title="암호"
                                            maxlength="64" placeholder="최대 64자 까지 가능 합니다" password=true />

                                    <@am.bstrap_in_txt name="re_user_password" value="" title="암호 확인"
                                            maxlength="64" placeholder="최대 64자 까지 가능 합니다" password=true />

                                    <@am.bstrap_in_txt name="user_name" value="" title="사용자 이름"
                                            maxlength="64" placeholder="최대 64byte 까지 가능 합니다" />

                                    <@am.bstrap_in_txt name="nick_name" value="" title="사용자 별명"
                                            maxlength="64" placeholder="최대 64byte 까지 가능 합니다" />

                                    <@am.bstrap_in_txt name="mobile_phone_number" value="" title="휴대폰 번호"
                                            maxlength="16" placeholder="최대 16자 까지 가능 합니다" />

                                    <@am.bstrap_in_txt name="description" value="" title="사용자 설명"
                                            maxlength="256" placeholder="최대 256byte 까지 가능 합니다" />

                                    <@am.bstrap_in_select name="enabled" title="사용자 상태">
                                        <option value="${mdv_yes}">활성</option>
                                        <option value="${mdv_no}">일시 중지</option>
                                        <option value="${mdv_deny}">차단</option>
                                    </@am.bstrap_in_select>

                                    <@am.bstrap_in_select name="allow_mailing" title="메일 수신 여부">
                                        <option value="${mdv_yes}">네</option>
                                        <option value="${mdv_no}">아니오</option>
                                    </@am.bstrap_in_select>

                                    <@am.bstrap_in_select name="allow_message" title="메시지 수신 여부">
                                        <option value="${mdv_yes}">네</option>
                                        <option value="${mdv_no}">아니오</option>
                                    </@am.bstrap_in_select>

                                    <@am.bstrap_in_select name="email_confirm" title="메일 주소 확인 여부">
                                        <option value="${mdv_yes}">네</option>
                                        <option value="${mdv_no}">아니오</option>
                                    </@am.bstrap_in_select>

                                    <@am.bstrap_in_select name="account_type" title="계정 종류">
                                        <option value="${backoffice_root}">관리자</option>
                                        <#if "${backoffice_mode}" == "1">
                                            <option value="${backoffice_user}">일반</option>
                                        </#if>
                                    </@am.bstrap_in_select>
                                </div>
                                <hr class="app-user-group-info" style="display: none;" />
                                <div class="row app-user-group-info" style="display: none;">

                                    <@am.bstrap_in_select name="app_account_type" title="계정 종류">
                                        <option value="${app_member}">정회원</option>
                                    </@am.bstrap_in_select>
                                </div>
                            </fieldset>

                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 사용자 추가
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
        my.fn.showSessionTime(${sessionRestSec});

        <#-- 페이지 도움말 영역 visible, invisible 처리 -->
        my.fn.initPageHelper('${_page_id}-helper');

        <#-- 사용자 타입에 따라 앱 권한 부여 할 수 있는 폼을 on/off 한다 -->
        $('select[name=account_type]').change(function(e){
            <@am.jsevent_prevent />
            var value = $(this).val();
            if(value == '2')   $('.app-user-group-info').fadeIn(300);
            else               $('.app-user-group-info').fadeOut(300);
        });

        <#-- input form validator -->
        my.fn.addValidator('regexp');
        my.fn.addValidator('maxbytelength');
        my.fn.addValidator('allowemptyemail');
        my.fn.addValidator('check_password');
        $("#checkout-form").validate({
            rules: {
                user_id:                {required: true, minlength:3, maxlength: 128},
                email_address:          {maxlength: 128, allowemptyemail: true},
                user_password:          {required: true, minlength:3, maxlength:64},
                re_user_password:       {required: true, minlength:3, maxlength:64, check_password: 'user_password'},
                user_name:              {required: true, maxbytelength: 64},
                nick_name:              {maxbytelength: 64},
                mobile_phone_number:    {regexp:/(^$|^([0-9\s]+[0-9\-\s]*)$)/}
            },
            messages : {
                user_id: {
                    required: '사용자 아이디는 필수값 입니다.',
                    minlength: '사용자 아이디는 {0}자 이상 입니다.',
                    maxlength: '사용자 아이디는 {0}자 이하 입니다.'
                },
                email_address: {
                    allowemptyemail: '올바른 메일 주소가 아닙니다.',
                    maxlength: '앱 이름은 {0}자 이하 입니다.'
                },
                user_password: {
                    required: '암호는 필수값 입니다.',
                    minlength: '암호는 {0}자 이상 입니다.',
                    maxlength: '암호는 {0}자 이하 입니다.'
                },
                re_user_password: {
                    required: '암호 확인은 필수값 입니다.',
                    minlength: '암호 확인은 {0}자 이상 입니다.',
                    maxlength: '암호 확인은 {0}자 이하 입니다.',
                    check_password: '암호 확인을 실패 했습니다.'
                },
                user_name: {
                    required: '사용자 이름은 필수값 입니다.',
                    minlength: '사용자 이름은 {0}자 이상 입니다.',
                    maxbytelength: '사용자 이름은 {0}byte 이하 입니다.'
                },
                nick_name: {
                    maxbytelength: '사용자 별명은 {0}byte 이하 입니다.'
                },
                mobile_phone_number: {
                    regexp: '올바른 휴대폰 번호가 아닙니다.'
                }
            },
            <@am.jsbstrap_validator_comm />
        });

        <#-- 사용자를 등록 한다 -->
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
                user_id:            $.trim($('input[name=user_id]').val()),
                email_address:      $.trim($('input[name=email_address]').val()),
                user_password:      $.trim($('input[name=user_password]').val()),
                user_name:          $.trim($('input[name=user_name]').val()),
                nick_name:          $.trim($('input[name=nick_name]').val()),
                mobile_phone_number:$.trim($('input[name=mobile_phone_number]').val()),
                description:        $.trim($('input[name=description]').val()),
                enabled:            $('select[name=enabled]').val(),
                allow_mailing:      $('select[name=allow_mailing]').val(),
                allow_message:      $('select[name=allow_message]').val(),
                email_confirm:      $('select[name=email_confirm]').val(),
                account_type:       $('select[name=account_type]').val(),
                app_account_type:   $('select[name=app_account_type]').val(),
                app_srl:            '100'
            };

            if(reqData.user_password == null || reqData.user_password != '')
                reqData.user_password = CryptoJS.MD5(reqData.user_password).toString();

            <#-- 사용자 추가 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/member/add/t/"
                    moveuri="#${Request.contextPath}/admin/member/list" errortitle="사용자 추가 실패" ; job >
                <#if job == "success_job">
                    <#-- 사용자 추가 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = reqData.user_id + ' 사용자를 추가 하였습니다.';
                    <@am.jsnoti title="사용자 추가 성공" content="toastMsg" boxtype="success" />

                    <#-- 사용자 추가 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                    my.fn.removeDatatableStorage('list-user');
                <#elseif job == "common_job">
                    <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 사용자 추가');
                </#if>
            </@am.jsajax_request>
        });
    };

    loadScript("${Request.resPath}js/ckpush/md5.js", pagefunction)
</script>
