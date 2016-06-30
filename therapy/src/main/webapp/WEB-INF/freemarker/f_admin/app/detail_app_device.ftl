<#assign _page_id="detail-app-device">
<#assign _page_parent_name="단말기 목록">
<#assign _page_current_name="단말기 정보">

<@am.page_simple_depth icon="fa-mobile" parent="${_page_parent_name}" current="${_page_current_name}"
        canMoveParent=true/>

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
   앱에 등록된 단말기 상세 정보를 확인 하고 수정 하는 페이지 입니다. 수정할 항목의 내용을 클릭하고 내용을 변경하면 곧바로 변경 됩니다. 상위 페이지인
   단말기 목록으로 이동 하고 싶으면 단말기 정보 위젯 하단의 이전 버튼을 클릭 하면 단말기 목록으로 이동 됩니다.
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
                <@am.widget_title icon="fa-edit">${deviceEntity.device_id}</@am.widget_title>

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
                                    <td style="width:30%;"><strong>단말기 시리얼 넘버</strong></td>
                                    <td style="width:70%">${deviceEntity.device_srl}</td>
                                </tr>
                                <tr>
                                    <td><strong>단말기 아이디</strong></td>
                                    <td>${deviceEntity.device_id}</td>
                                </tr>
                                <tr>
                                    <td><strong>단말기 구분</strong></td>
                                    <td>
                                        <#if deviceEntity.device_class == android_device_class>
                                            <img src="${Request.resPath}img/android.png" alt="Android" width="17">
                                        <#elseif deviceEntity.device_class == ios_device_class>
                                            <img src="${Request.resPath}img/apple.png" alt="iOS" width="17">
                                        <#else>
                                            -
                                        </#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>벤더, 모델명</strong></td>
                                    <td><a id="device_type" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="벤더, 모델명을 수정 합니까?" data-placeholder="벤더, 모델명을 입력 하세요">${deviceEntity.device_type}</a></td>
                                </tr>
                                <tr>
                                    <td><strong>단말기 OS 버전</strong></td>
                                    <td><a id="os_version" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="OS 버전을 수정 합니까?" data-placeholder="벤더, 모델명을 입력 하세요">${deviceEntity.os_version}</a></td>
                                </tr>
                                <tr>
                                    <td><strong>단말기 전화번호</strong></td>
                                    <td><a id="mobile_phone_number" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="단말기 전화번호를 수정 합니까?" data-placeholder="단말기의 전화번호를 입력 하세요">${deviceEntity.mobile_phone_number}</a></td>
                                </tr>
                                <tr>
                                    <td><strong>Push 아이디</strong></td>
                                    <td><a id="push_id" class="my-x-editable-pointer" data-type="textarea" data-pk="1" data-original-title="Push ID를 수정 합니까?" data-placeholder="GCM을 위한 RID나 APNs를 위한 Device Token을 입력하세요">${deviceEntity.appDeviceEntity.push_id}</a></td>
                                </tr>
                                <tr>
                                    <td><strong>Push 수신 여부</strong></td>
                                    <td><a id="allow_push" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="${deviceEntity.appDeviceEntity.allow_push}" data-original-title="Push 활성 여부를 변경 합니까?"></a></td>
                                </tr>
                                <tr>
                                    <td><strong>단말기 등록 상태</strong></td>
                                    <td><a id="enabled" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="${deviceEntity.appDeviceEntity.enabled}" data-original-title="단말기 등록 상태를 변경 합니까?"></a></td>
                                </tr>
                                <tr>
                                    <td><strong>등록일</strong></td>
                                    <td>${(deviceEntity.appDeviceEntity.c_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm:ss")}</td>
                                </tr>
                                <tr>
                                    <td><strong>수정일</strong></td>
                                    <td>${(deviceEntity.appDeviceEntity.u_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm:ss")}</td>
                                </tr>
                            </tbody>
                        </table>

                        <form>
                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-delete" class="btn btn-sm btn-danger" type="button">
                                        <i class="fa fa-trash-o"></i> 단말기 삭제
                                    </button>
                                </div>
                                <div class="btn-group pull-left">
                                    <button class="btn btn-sm btn-info btn-prev-page" type="button">
                                        <i class="fa fa-chevron-left"></i> 단말기 목록
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

        drawBreadCrumb(['${deviceEntity.device_id}']);

        <#-- 페이지 도움말 영역 visible, invisible 처리 -->
        my.fn.initPageHelper('${_page_id}-helper');

        <#-- x-editable device_type -->
        <@am.jsxeditable id="device_type" uri="${Request.contextPath}/admin/app/${deviceEntity.appDeviceEntity.app_srl}/device/${deviceEntity.device_srl}/t/">
            function(value) {
                value = $.trim(value);
                if(my.fn.byteLength(value) > 128) return '벤더, 모델명은 128 bytes 이하 입니다.';
            }
        </@am.jsxeditable>

        <#-- x-editable os_version -->
        <@am.jsxeditable id="os_version" uri="${Request.contextPath}/admin/app/${deviceEntity.appDeviceEntity.app_srl}/device/${deviceEntity.device_srl}/t/">
            function(value) {
                value = $.trim(value);
                if(my.fn.byteLength(value) > 32) return 'OS 버전은 32 bytes 이하 입니다.';
            }
        </@am.jsxeditable>

        <#-- x-editable os_version -->
        <@am.jsxeditable id="mobile_phone_number" uri="${Request.contextPath}/admin/app/${deviceEntity.appDeviceEntity.app_srl}/device/${deviceEntity.device_srl}/t/"
                onlyNumber=true>
            function(value) {
                value = my.fn.getPhoneNumber($.trim(value));
                if(my.fn.byteLength(value) > 16) return '단말기 전화번호는 16 bytes 이하 입니다.';
            }
        </@am.jsxeditable>

        <#-- x-editable push_id -->
        <@am.jsxeditable id="push_id" uri="${Request.contextPath}/admin/app/${deviceEntity.appDeviceEntity.app_srl}/device/${deviceEntity.device_srl}/t/">
            function(value) { }
        </@am.jsxeditable>

        <#-- x-editable enabled -->
        <@am.jsxeditable id="allow_push" uri="${Request.contextPath}/admin/app/${deviceEntity.appDeviceEntity.app_srl}/device/${deviceEntity.device_srl}/t/" type="select">
                [{value: ${mdv_yes}, text: '허용'}, {value: ${mdv_no}, text: '중단'}]
        </@am.jsxeditable>

        <#-- x-editable enabled -->
        <@am.jsxeditable id="enabled" uri="${Request.contextPath}/admin/app/${deviceEntity.appDeviceEntity.app_srl}/device/${deviceEntity.device_srl}/t/" type="select">
            [{value: ${mdv_yes}, text: '활성'}, {value: ${mdv_no}, text: '일시중지'}, {value: ${mdv_deny}, text: '차단'}]
        </@am.jsxeditable>

        <#-- 리스트로 이동 -->
        $('.btn-prev-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/app/device/list');
        });

        <#-- 단말기 삭제 -->
        $('#btn-delete').click(function(e){
            <@am.jsevent_prevent />

            var $this = $(this);
            var reqData = {c_keys: [{device_srl:${deviceEntity.device_srl}, app_srl:${deviceEntity.appDeviceEntity.app_srl}}]};
            var successText = '${appEntity.app_name}에 등록된 단말기를 삭제 했습니다.';
            var process = function() {
                $this.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');

            <@am.jsajax_request submituri="${Request.contextPath}/admin/app/device/t/"
                    method="DELETE" errortitle="단말기 삭제 실패" ; job >
                <#if job == "success_job">
                    <#-- 삭제 성공시 리스트 페이지로 이동 -->
                    <@am.jsnoti title="단말기 삭제 완료" content="successText" boxtype="success" />

                    <#-- 헐...이거 click 반응으로 사용자 반응 이후 호출 되는 곳 말고, 자동으로 시나리오 타고 오는 경우
                         my.fn.pmove 를 바로 호출 하니 message box 에 문제 생긴다. 약간의 딜레이를 준다 -->
                    setTimeout(function(){
                        my.fn.pmove('#${Request.contextPath}/admin/app/device/list');
                    }, 300);

                <#elseif job == "common_job">
                    $this.removeClass('disabled').html('<i class="fa fa-trash-o"></i> 단말기 삭제');
                </#if>
            </@am.jsajax_request>
            };

            my.fn.showUserConfirm('단말기 삭제',
                    "${appEntity.app_name}에 등록된 ${deviceEntity.device_id}를 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                    '삭제를 입력하세요',
                    '네', '삭제', process, '단말기 삭제 실패', '삭제를 입력하지 않았습니다.');
        });
    };

    var pagedestroy = function(){
        $('#device_type').editable('destroy');
        $('#os_version').editable('destroy');
        $('#mobile_phone_number').editable('destroy');
        $('#push_id').editable('destroy');
        $('#allow_push').editable('allow_push');
        $('#enabled').editable('enabled');
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
                                            loadScript("${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js", pagefunction)
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