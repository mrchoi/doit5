<#assign _page_id="detail-advisor">
<#assign _page_parent_name="상담사 관리">
<#assign _page_current_name="상담사 정보">

<@am.page_simple_depth icon="fa-puzzle-piece" parent="${_page_parent_name}" current="${_page_current_name}"
canMoveParent=true />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    등록된 상담사 정보를 확인하고 수정하는 페이지 입니다.수정할 항목의 내용을 클릭하고 내용을 변경하면 곧바로 변경 됩니다. 상위 페이지인
    사용자 목록으로 이동 하고 싶으면 사용자 정보 위젯 하단의 이전 버튼을 클릭 하면 앱 리스트로 이동 됩니다.
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
            <@am.widget_title icon="fa-eye">${memberEntity.user_name}</@am.widget_title>

            <#-- widget div-->
                <div>
                <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                <#-- end widget edit box -->

                <#-- widget content -->
                    <div class="widget-body">
                        <table id="user" class="table table-bordered table-striped" style="clear: both">
                            <tbody>
                            <tr>
                                <td style="width:30%;"><strong>사용자 시리얼 넘버</strong></td>
                                <td style="width:70%">${memberEntity.member_srl}</td>
                            </tr>
                            <tr>
                                <td><strong>사용자 아이디</strong></td>
                                <td>${memberEntity.user_id}</td>
                            </tr>
                            <tr>
                                <td><strong>암호</strong></td>
                                <td><a id="user_password" class="my-x-editable-pointer" data-clear="true" data-type="password" data-pk="1" data-original-title="로그인 암호를 수정 합니까?" data-placeholder="새로운 암호를 입력 하세요">${memberEntity.user_password}</a></td>
                            </tr>
                            <tr>
                                <td><strong>사용자 이름</strong></td>
                                <td><a id="user_name" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="사용자 이름을 수정 합니까?" data-placeholder="이름을 입력 하세요">${memberEntity.user_name}</a></td>
                            </tr>
                            <tr>
                                <td><strong>사용자 별명</strong></td>
                                <td><a id="nick_name" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="사용자 별명을 수정 합니까?" data-placeholder="별명을 입력 하세요">${memberEntity.nick_name}</a></td>
                            </tr>
                            <tr>
                                <td><strong>메일 주소</strong></td>
                                <td><a id="email_address" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="메일 주소를 수정 합니까?" data-placeholder="메일 주소를 입력 하세요">${memberEntity.email_address}</a></td>
                            </tr>
                            <tr>
                                <td><strong>휴대폰 번호</strong></td>
                                <td><a id="mobile_phone_number" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="휴대폰 번호를 수정 합니까?" data-placeholder="휴대폰 번호를 입력 하세요">${memberEntity.mobile_phone_number}</a></td>
                            </tr>
                            <tr>
                                <td><strong>메일 수신 여부</strong></td>
                                <td><a id="allow_mailing" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="${memberEntity.allow_mailing}" data-original-title="메일 수신 여부를 변경 합니까?"></a></td>
                            </tr>
                            <tr>
                                <td><strong>메시지 수신 여부</strong></td>
                                <td><a id="allow_message" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="${memberEntity.allow_message}" data-original-title="메시지 수신 여부를 변경 합니까?"></a></td>
                            </tr>
                            <tr>
                                <td><strong>사용자 설명</strong></td>
                                <td><a id="description" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="사용자 설명을 수정 합니까?" data-placeholder="사용자 설명을 입력 하세요">${memberEntity.description}</a></td>
                            </tr>
                            <tr>
                                <td><strong>최종 로그인</strong></td>
                                <td>${(memberEntity.last_login_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm:ss")}</td>
                            </tr>
                            <tr>
                                <td><strong>암호 변경일</strong></td>
                                <td>${(memberEntity.change_password_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm:ss")}</td>
                            </tr>
                            <tr>
                                <td><strong>사용자 상태</strong></td>
                                <td><a id="enabled" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="${memberEntity.enabled}" data-original-title="사용자 상태를 변경 합니까?"></a></td>
                            </tr>
                            <tr>
                                <td><strong>메일 주소 확인 여부</strong></td>
                                <td><a id="email_confirm" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="${memberEntity.email_confirm}" data-original-title="메일 주소 확인 여부를 변경 합니까?"></a></td>
                            </tr>
                            <tr>
                                <td><strong>사용자 탈퇴 여부</strong></td>
                                <td><a id="sign_out" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="${memberEntity.sign_out}" data-original-title="사용자 탈퇴 여부를 변경 합니까?"></a></td>
                            </tr>
                            <tr>
                                <td><strong>총 로그인 횟수</strong></td>
                                <td>${memberEntity.memberExtraEntity.login_count}</td>
                            </tr>
                            <tr>
                                <td><strong>연속 로그인 횟수</strong></td>
                                <td>${memberEntity.memberExtraEntity.serial_login_count}</td>
                            </tr>
                            <tr>
                                <td><strong>가입일</strong></td>
                                <td>${(memberEntity.c_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm:ss")}</td>
                            </tr>
                            <tr>
                                <td><strong>수정일</strong></td>
                                <td>${(memberEntity.u_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm:ss")}</td>
                            </tr>
                            <tr>
                                <td><strong>상담사 자격증명</strong></td>
                                <td><a id="certificate" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="상담사 자격증명을 수정 합니까?" data-placeholder="상담사 자격증명을 입력 하세요"><#if memberEntity.memberExtraEntity.certificate?exists>${memberEntity.memberExtraEntity.certificate}</#if></a></td>
                            </tr>
                            <tr>
                                <td><strong>카카오 아이디</strong></td>
                                <td><a id="kakao_id" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="카카오 아이디를 수정 합니까?" data-placeholder="카카오 아이디를 입력 하세요"><#if memberEntity.memberExtraEntity.kakao_id?exists>${memberEntity.memberExtraEntity.kakao_id}</#if></a></td>
                            </tr>
                            <tr>
                                <td><strong>상담사 등급</strong></td>
                                <td><a id="class_srl" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="<#if memberEntity.memberExtraEntity.class_srl?exists>${memberEntity.memberExtraEntity.class_srl}</#if>" data-original-title="상담사 등급을 변경 합니까?"></a></td>
                            </tr>
                            <tr>
                                <td><strong>상담사 영역</strong></td>
                                <td><a id="domain_srl" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="<#if memberEntity.memberExtraEntity.domain_srl?exists>${memberEntity.memberExtraEntity.domain_srl}</#if>" data-original-title="상담사 영역을 변경 합니까?"></a></td>
                            </tr>
                            <tr>
                                <td><strong>상담사 자기소개</strong></td>
                                <td><a id="self_introduction" class="my-x-editable-pointer" data-type="textarea" data-pk="1" data-original-title="자기소개를 수정 합니까?" data-placeholder="자기소개를 입력 하세요"><#if memberEntity.memberExtraEntity.self_introduction?exists>${memberEntity.memberExtraEntity.self_introduction}</#if></a></td>
                            </tr>
                            </tbody>
                        </table>

                    <#-- 가입되어 있는 앱 정보를 출력 한다 -->
                    <#list appGroupMapList as item>
                        <br/>
                        <table id="user" class="table table-bordered table-striped" style="clear: both">
                            <tbody>
                            <tr>
                                <td style="width:30%;"><strong>앱</strong></td>
                                <td style="width:70%">${item.app_info.app_name}</td>
                            </tr>
                            <tr>
                                <td><strong>권한</strong></td>
                                <td>${item.app_group_info.group_name}</td>
                            </tr>
                            </tbody>
                        </table>
                    </#list>

                        <form>
                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-delete" class="btn btn-sm btn-danger" type="button">
                                        <i class="fa fa-trash-o"></i> 상담사 삭제
                                    </button>
                                </div>
                                <div class="btn-group pull-left">
                                    <button class="btn btn-sm btn-info btn-prev-page" type="button">
                                        <i class="fa fa-chevron-left"></i> 상담사 목록
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

        drawBreadCrumb(['${memberEntity.user_name}']);

    <#-- x-editable user_password -->
    <@am.jsxeditable id="user_password" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/"
    isPassword=true>
        function(value) {
            value = $.trim(value);
            if(value.length <= 0)   return '로그인 암호는 필수값 입니다.';
            if(value.length < 3)    return '로그인 암호는 최소 3자리 이상 입니다.';
            if(value.length > 32)   return '로그인 암호는 32자 이하 입니다.';
        }
    </@am.jsxeditable>

    <#-- x-editable user_name -->
    <@am.jsxeditable id="user_name" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/">
        function(value) {
            value = $.trim(value);
            if(value.length <= 0)               return '사용자 이름은 필수값 입니다.';
            if(my.fn.byteLength(value) > 64)    return '사용자 이름은 64byte 이하 입니다.';
        }
    </@am.jsxeditable>

    <#-- x-editable nick_name -->
    <@am.jsxeditable id="nick_name" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/">
        function(value) {
            value = $.trim(value);
            if(my.fn.byteLength(value) > 64)    return '사용자 별명은 64byte 이하 입니다.';
        }
    </@am.jsxeditable>

    <#-- x-editable email_address -->
    <@am.jsxeditable id="email_address" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/">
        function(value) {
            value = $.trim(value);
            if(value.length > 128)                          return '메일 주소는 128자 이하 입니다.';
            if(!my.fn.checkValidEmailAddress(value, true))  return '올바른 메일 주소가 아닙니다.';
        }
    </@am.jsxeditable>

    <#-- x-editable mobile_phone_number -->
    <@am.jsxeditable id="mobile_phone_number" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/"
    onlyNumber=true>
        function(value) {
            value = $.trim(value);
            if(value.length > 16)                           return '휴대폰 번호는 16자 이하 입니다.';
            if(!/(^$|^([0-9\s]+[0-9\-\s]*)$)/.test(value))  return '올바른 휴대폰 번호가 아닙니다.';
        }
    </@am.jsxeditable>

    <#-- x-editable allow_mailing -->
    <@am.jsxeditable id="allow_mailing" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/" type="select">
            [{value: ${mdv_yes}, text: '네'}, {value: ${mdv_no}, text: '아니오'}]
    </@am.jsxeditable>

    <#-- x-editable allow_message -->
    <@am.jsxeditable id="allow_message" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/" type="select">
            [{value: ${mdv_yes}, text: '네'}, {value: ${mdv_no}, text: '아니오'}]
    </@am.jsxeditable>

    <#-- x-editable description -->
    <@am.jsxeditable id="description" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/">
        function(value) {
            value = $.trim(value);
            if(my.fn.byteLength(value).length > 256) return '사용자 설명은 256byte 이하 입니다.';
        }
    </@am.jsxeditable>

    <#-- x-editable enabled -->
    <@am.jsxeditable id="enabled" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/" type="select">
            [{value: ${mdv_yes}, text: '활성'}, {value: ${mdv_no}, text: '일시 중지'}, {value: ${mdv_deny}, text: '차단'}]
    </@am.jsxeditable>

    <#-- x-editable email_confirm -->
    <@am.jsxeditable id="email_confirm" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/" type="select">
            [{value: ${mdv_yes}, text: '네'}, {value: ${mdv_no}, text: '아니오'}]
    </@am.jsxeditable>

    <#-- x-editable sign_out -->
    <@am.jsxeditable id="sign_out" uri="${Request.contextPath}/admin/member/${memberEntity.member_srl}/t/" type="select">
            [{value: ${mdv_no}, text: '가입 상태'}, {value: ${mdv_yes}, text: '탈퇴 상태'}]
    </@am.jsxeditable>

    <#-- x-editable certificate -->
    <@am.jsxeditable id="certificate" uri="${Request.contextPath}/admin/member/extra/${memberEntity.member_srl}/t/">
        function(value) {
            value = $.trim(value);
            if(my.fn.byteLength(value).length > 64) return '상담사 자격증명은 64byte 이하 입니다.';
        }
    </@am.jsxeditable>

    <#-- x-editable kakao_id -->
    <@am.jsxeditable id="kakao_id" uri="${Request.contextPath}/admin/member/extra/${memberEntity.member_srl}/t/">
        function(value) {
            value = $.trim(value);
            if(my.fn.byteLength(value).length > 128) return '카카오 아이디는 128byte 이하 입니다.';
        }
    </@am.jsxeditable>

    <#-- x-editable class_srl -->
    <@am.jsxeditable id="class_srl" uri="${Request.contextPath}/admin/member/extra/${memberEntity.member_srl}/t/" type="select">
        [{value: '1', text: '으뜸'}, {value: '2', text: '가장'}, {value: '3', text: '한마루'}]
    </@am.jsxeditable>

    <#-- x-editable domain_srl -->
    <@am.jsxeditable id="domain_srl" uri="${Request.contextPath}/admin/member/extra/${memberEntity.member_srl}/t/" type="select">
            [{value: '1', text: '일과 업무'}, {value: '2', text: '삶'}, {value: '3', text: '부모'}, {value: '4', text: '진로'},
                {value: '5', text: '대인관계'}, {value: '6', text: '학습과 시험'}, {value: '7', text: '중독'}, {value: '8', text: '분노와 충동성'}]
    </@am.jsxeditable>

    <#-- x-editable self_introduction -->
    <@am.jsxeditable id="self_introduction" uri="${Request.contextPath}/admin/member/extra/${memberEntity.member_srl}/t/">
        function(value) {
            value = $.trim(value);
            if(my.fn.byteLength(value).length > 512) return '상담사 자기소개는 512byte 이하 입니다.';
        }
    </@am.jsxeditable>

    <#-- 리스트로 이동 -->
        $('.btn-prev-page').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/advisor/list');
        });

    <#-- 상담사 삭제 -->
        $('#btn-delete').click(function(e){
        <@am.jsevent_prevent />

            var $this = $(this);
            var reqData = {l_keys: [${memberEntity.member_srl}]};
            var successText = '${memberEntity.user_id} 상담사를 삭제 했습니다.';
            var process = function() {
                $this.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');

            <@am.jsajax_request submituri="${Request.contextPath}/admin/member/t/"
            method="DELETE" errortitle="상담사 삭제 실패" ; job >
                <#if job == "success_job">
                <#-- 삭제 성공시 리스트 페이지로 이동 -->
                    <@am.jsnoti title="상담사 삭제 완료" content="successText" boxtype="success" />

                <#-- 헐...이거 click 반응으로 사용자 반응 이후 호출 되는 곳 말고, 자동으로 시나리오 타고 오는 경우
                     my.fn.pmove 를 바로 호출 하니 message box 에 문제 생긴다. 약간의 딜레이를 준다 -->
                    setTimeout(function(){
                        my.fn.pmove('#${Request.contextPath}/admin/advisor/list');
                    }, 300);

                <#elseif job == "common_job">
                    $this.removeClass('disabled').html('<i class="fa fa-trash-o"></i> 상담사 삭제');
                </#if>
            </@am.jsajax_request>
            };

            my.fn.showUserConfirm('상담사 삭제',
                    "${memberEntity.user_id} 상담사를 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                    '삭제를 입력하세요',
                    '네', '삭제', process, '상담사 삭제 실패', '삭제를 입력하지 않았습니다.');
        });

    };

    var pagedestroy = function(){
        $('#user_name').editable('destroy');
        $('#nick_name').editable('destroy');
        $('#email_address').editable('destroy');
        $('#mobile_phone_number').editable('destroy');
        $('#allow_mailing').editable('destroy');
        $('#allow_message').editable('destroy');
        $('#description').editable('destroy');
        $('#enabled').editable('destroy');
        $('#email_confirm').editable('destroy');
        $('#sign_out').editable('destroy');
        $('#certificate').editable('destroy');
        $('#kakao_id').editable('destroy');
        $('#class_srl').editable('destroy');
        $('#domain_srl').editable('destroy');
        $('#self_introduction').editable('destroy');
    };

    loadScript("${Request.resPath}js/plugin/x-editable/moment.min.js", function() {
        loadScript("${Request.resPath}js/plugin/x-editable/jquery.mockjax.min.js", function() {
            loadScript("${Request.resPath}js/plugin/x-editable/x-editable.min.js", function() {
                loadScript("${Request.resPath}js/plugin/typeahead/typeahead.min.js", function() {
                    loadScript("${Request.resPath}js/plugin/typeahead/typeaheadjs.min.js", function() {
                        loadScript("${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js", function () {
                            loadScript("${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js", function () {
                                loadScript("${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js", function () {
                                    loadScript("${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js", function () {
                                        loadScript("${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js", function () {
                                            loadScript("${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js", function () {
                                                loadScript("${Request.resPath}js/ckpush/md5.js", pagefunction);
                                            })
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    });
</script>