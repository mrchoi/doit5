<#assign _page_id="add-board">
<#assign _page_parent_name="게시판 관리">
<#assign _page_current_name="게시판 추가">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}"
        canMoveParent=true/>

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    게시판을 생성 합니다. 게시판은 앱별로 매핑 됩니다.
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
                <@am.widget_title icon="fa-edit">게시판 추가</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <form id="checkout-form">
                            <fieldset>
                                <legend>추가할 게시판 정보를 입력 합니다.</legend>

                                <div class="row">
                                    <@am.bstrap_disabled_txt value="${appEntity.app_name}" title="앱" />

                                    <@am.bstrap_in_txt name="board_name" value="" title="게시판 이름"
                                            maxlength="64" placeholder="최대 64byte 까지 가능 합니다." />

                                    <@am.bstrap_in_txt name="board_description" value="" title="게시판 설명"
                                            maxlength="256" placeholder="최대 256byte 까지 가능 합니다" />

                                    <@am.bstrap_in_select name="enabled" title="활성 여부">
                                        <option value="${mdv_yes}">활성</option>
                                        <option value="${mdv_no}">비활성</option>
                                    </@am.bstrap_in_select>

                                    <@am.bstrap_in_select name="open_type" title="공개 여부">
                                        <option value="${mdv_public}">공개</option>
                                        <option value="${mdv_private}">비공개(로그인 필수)</option>
                                    </@am.bstrap_in_select>
                                </div>
                            </fieldset>

                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 게시판 추가
                                    </button>
                                </div>
                                <div class="btn-group pull-left">
                                    <button class="btn btn-sm btn-info btn-prev-page" type="button">
                                        <i class="fa fa-chevron-left"></i> 게시판 관리
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

        drawBreadCrumb(['게시판 추가']);

        <#-- 게시판 관리로 이동 -->
        $('.btn-prev-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/board/manage');
        });

        <#-- input form validator -->
        my.fn.addValidator('maxbytelength');
        $("#checkout-form").validate({
            rules: {
                board_name:             {required: true, minlength:2, maxbytelength: 64},
                board_description:      {maxbytelength: 256}
            },
            messages : {
                board_name: {
                    required: '게시판 이름은 필수값 입니다.',
                    minlength: '게시판 이름은 {0}자 이상 입니다.',
                    maxbytelength: '게시판 이름은 {0}byte 이하 입니다.'
                },
                board_description: {
                    maxbytelength: '게시판 설명은 {0}byte 이하 입니다.'
                }
            },
            <@am.jsbstrap_validator_comm />
        });

        <#-- 게시판을 생성 한다 -->
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
                app_srl:            ${appEntity.app_srl},
                board_name:         $.trim($('input[name=board_name]').val()),
                board_description:  $.trim($('input[name=board_description]').val()),
                enabled:            $.trim($('select[name=enabled]').val()),
                open_type:          $.trim($('select[name=open_type]').val())
            };

            <#-- 게시판 추가 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/add/t/"
                    moveuri="#${Request.contextPath}/admin/board/manage" errortitle="게시판 추가 실패" ; job >
                <#if job == "success_job">
                    <#-- 게시판 추가 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = reqData.board_name + ' 게시판을 추가 하였습니다.';
                    <@am.jsnoti title="게시판 추가 성공" content="toastMsg" boxtype="success" />
                    <#-- 게시판 추가 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                    my.fn.removeDatatableStorage('manage-board-board-list');
                <#elseif job == "common_job">
                    <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시판 추가');
                </#if>
            </@am.jsajax_request>
        });

    };

    pagefunction();
</script>