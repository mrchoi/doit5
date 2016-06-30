<#assign _page_id="detail-app">
<#assign _page_parent_name="앱 목록">
<#assign _page_current_name="앱 정보">

<@am.page_simple_depth icon="fa-puzzle-piece" parent="${_page_parent_name}" current="${_page_current_name}"
        canMoveParent=true />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    등록된 앱 정보를 확인 하고 수정 하는 페이지 입니다. 수정할 항목의 내용을 클릭하고 내용을 변경하면 곧바로 변경 됩니다. 상위 페이지인
    앱 리스트로 이동 하고 싶으면 앱 정보 위젯 하단의 이전 버튼을 클릭 하면 앱 리스트로 이동 됩니다.
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
                <@am.widget_title icon="fa-eye">${appEntity.app_name}</@am.widget_title>

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
                                    <td style="width:30%;"><strong>앱 시리얼 넘버</strong></td>
                                    <td style="width:70%">${appEntity.app_srl}</td>
                                </tr>
                                <tr>
                                    <td><strong>앱 아이디</strong></td>
                                    <td>${appEntity.app_id}</td>
                                </tr>
                                <tr>
                                    <td><strong>앱 이름</strong></td>
                                    <#--<td><a href="${Request.contextPath}/admin#" id="app_name" data-type="text" data-pk="1" data-original-title="앱 이름을 수정 합니까?" data-placeholder="앱 이름을 입력 하세요">${appEntity.app_name}</a></td>-->
                                    <td><a id="app_name" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="앱 이름을 수정 합니까?" data-placeholder="앱 이름을 입력 하세요">${appEntity.app_name}</a></td>
                                </tr>
                                <tr>
                                    <td><strong>앱 버전</strong></td>
                                    <td><a id="app_version" class="my-x-editable-pointer" data-type="text" data-pk="1" data-original-title="앱 버전을 수정 합니까?" data-placeholder="앱 버전을 입력 하세요">${appEntity.app_version}</a></td>
                                </tr>
                                <tr>
                                    <td><strong>Api Key</strong></td>
                                    <td>${appEntity.api_key}</td>
                                </tr>
                                <tr>
                                    <td><strong>Api Secret</strong></td>
                                    <td>${appEntity.api_secret}</td>
                                </tr>
                                <tr>
                                    <td><strong>상태</strong></td>
                                    <td><a id="enabled" class="my-x-editable-pointer" data-type="select" data-pk="1" data-value="${appEntity.enabled}" data-original-title="앱 상태를 변경 합니까?"></a></td>
                                </tr>
                                <tr>
                                    <td><strong>앱 관리자</strong></td>
                                    <td><a id="app_manager" class="my-x-editable-pointer" data-type="select2" data-pk="1" data-original-title="앱 관리자를 변경 합니까?">
                                        <#list appManager as manager>
                                            ${manager.user_id}<#if manager_has_next>,</#if>
                                        </#list>
                                    </a></td>
                                </tr>
                                <tr>
                                    <td><strong>생성일</strong></td>
                                    <td>${(appEntity.c_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm:ss")}</td>
                                </tr>
                                <tr>
                                    <td><strong>수정일</strong></td>
                                    <td>${(appEntity.u_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm:ss")}</td>
                                </tr>
                            </tbody>
                        </table>

                        <form>
                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-delete" class="btn btn-sm btn-danger" type="button">
                                        <i class="fa fa-trash-o"></i> 앱 삭제
                                    </button>
                                </div>
                                <div class="btn-group pull-left">
                                    <button class="btn btn-sm btn-info btn-prev-page" type="button">
                                        <i class="fa fa-chevron-left"></i> 앱 목록
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

        drawBreadCrumb(['${appEntity.app_name}']);

        <#-- x-editable app_name -->
        <@am.jsxeditable id="app_name" uri="${Request.contextPath}/admin/app/${appEntity.app_srl}/t/">
            function(value) {
                value = $.trim(value);
                if(value.length <= 0)   return '앱 이름은 필수값 입니다.';
                if(value.length > 64)   return '앱 이름은 64자 이하 입니다.';
            }
        </@am.jsxeditable>

        <#-- x-editable app_version -->
        <@am.jsxeditable id="app_version" uri="${Request.contextPath}/admin/app/${appEntity.app_srl}/t/">
            function(value) {
                value = $.trim(value);
                if(value.length <= 0)   return '앱 버전은 필수값 입니다.';
                if(value.length > 16)   return '앱 버전은 16자 이하 입니다.';

                var pattern = new RegExp(/^[0-9]+(\.[0-9]+){0,2}/i);
                if(!pattern.test(value))return '앱 버전의 포맷은 x.y.z 형태 이어야 합니다.';
            }
        </@am.jsxeditable>

        <#-- x-editable enabled -->
        <@am.jsxeditable id="enabled" uri="${Request.contextPath}/admin/app/${appEntity.app_srl}/t/" type="select">
            [{value: 1, text: '활성'}, {value: 2, text: '중단'}, {value: 3, text: '차단'}]
        </@am.jsxeditable>

        <#-- x-editable app_manager -->
        <@am.jsxeditable id="app_manager" uri="${Request.contextPath}/admin/app/${appEntity.app_srl}/t/"
                type="select2" select2id="user_id" select2uri="${Request.contextPath}/admin/member/list/select2/1/t/">
            function(value) {
                value = $.trim(value);
                if(value.length <= 0)   return '앱 관리자는 필수값 입니다.';
            }
        </@am.jsxeditable>

        <#-- 리스트로 이동 -->
        $('.btn-prev-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/app/list');
        });

        <#-- 앱 삭제 -->
        $('#btn-delete').click(function(e){
            <@am.jsevent_prevent />

            var $this = $(this);
            var reqData = {i_keys: [${appEntity.app_srl}]};
            var successText = '${appEntity.app_name} 을 삭제 했습니다.';
            var process = function() {
                $this.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');

                <@am.jsajax_request submituri="${Request.contextPath}/admin/app/t/"
                        method="DELETE" errortitle="앱 삭제 실패" ; job >
                    <#if job == "success_job">
                        <#-- 삭제 성공시 리스트 페이지로 이동 -->
                        <@am.jsnoti title="앱 삭제 완료" content="successText" boxtype="success" />

                        <#-- 헐...이거 click 반응으로 사용자 반응 이후 호출 되는 곳 말고, 자동으로 시나리오 타고 오는 경우
                             my.fn.pmove 를 바로 호출 하니 message box 에 문제 생긴다. 약간의 딜레이를 준다 -->
                        setTimeout(function(){
                            my.fn.pmove('#${Request.contextPath}/admin/app/list');
                        }, 300);

                    <#elseif job == "common_job">
                        $this.removeClass('disabled').html('<i class="fa fa-trash-o"></i> 앱 삭제');
                    </#if>
                </@am.jsajax_request>
            };

            my.fn.showUserConfirm('앱 삭제',
                    "${appEntity.app_name} 을 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                    '삭제를 입력하세요',
                    '네', '삭제', process, '앱 삭제 실패', '삭제를 입력하지 않았습니다.');
        });
    };

    var pagedestroy = function(){
        $('#app_name').editable('destroy');
        $('#app_version').editable('destroy');
        $('#enabled').editable('destroy');
        $('#app_manager').editable('destroy');
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