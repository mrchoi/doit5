<#assign _page_id="add-category">
<#assign _page_parent_name="게시판 관리">
<#assign _page_current_name="카테고리 추가">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}"
        canMoveParent=true/>

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    카테고리를 생성 합니다. 카테고리는 게시판 별로 매핑 됩니다.
</p>
<p>
    카테고리 생성시 다음 사항에 주의 해야 합니다.<br/>
    <b>1. 카테고리 공개 여부는 게시물의 권한 문제로 인해서 생성된 값에서 다른 값으로 변경이 불가능 합니다.</b><br/>
    <b>2. 카테고리 종류는 게시물 종류의 구분으로 인해서 생성된 값에서 다른 값으로 변경 불가능 합니다.</b>
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
                <@am.widget_title icon="fa-edit">카테고리 추가</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <form id="checkout-form">
                            <fieldset>
                                <legend>추가할 카테고리 정보를 입력 합니다.</legend>

                                <div class="row">
                                    <@am.bstrap_disabled_txt value="${appEntity.app_name}" title="앱" />
                                    <@am.bstrap_disabled_txt value="${documentBoardEntity.board_name}" title="게시판" />

                                    <@am.bstrap_in_txt name="category_name" value="" title="카테고리 이름"
                                            maxlength="64" placeholder="최대 64byte 까지 가능 합니다." />

                                    <@am.bstrap_in_txt name="category_description" value="" title="카테고리 설명"
                                            maxlength="256" placeholder="최대 256byte 까지 가능 합니다" />

                                    <@am.bstrap_in_select name="category_type" title="카테고리 종류">
                                        <option value="${normal_category_type}">일반 카테고리 (일반 게시물)</option>
                                        <option value="${link_category_type}">링크 카테고리 (링크 게시물)</option>
                                    </@am.bstrap_in_select>

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
                                        <i class="fa fa-check"></i> 카테고리 추가
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

        drawBreadCrumb(['카테고리 추가']);

        <#-- 게시판 관리로 이동 -->
        $('.btn-prev-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/board/manage');
        });

        <#-- input form validator -->
        my.fn.addValidator('maxbytelength');
        $("#checkout-form").validate({
            rules: {
                category_name:          {required: true, minlength:2, maxbytelength: 64},
                category_description:   {maxbytelength: 256}
            },
            messages : {
                category_name: {
                    required: '카테고리 이름은 필수값 입니다.',
                    minlength: '카테고리 이름은 {0}자 이상 입니다.',
                    maxbytelength: '카테고리 이름은 {0}byte 이하 입니다.'
                },
                category_description: {
                    maxbytelength: '카테고리 설명은 {0}byte 이하 입니다.'
                }
            },
            <@am.jsbstrap_validator_comm />
        });

        <#-- 카테고리를 생성 한다 -->
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
                app_srl:                ${appEntity.app_srl},
                board_srl:              ${documentBoardEntity.board_srl},
                category_name:          $.trim($('input[name=category_name]').val()),
                category_description:   $.trim($('input[name=category_description]').val()),
                category_type:          $.trim($('select[name=category_type]').val()),
                enabled:                $.trim($('select[name=enabled]').val()),
                open_type:              $.trim($('select[name=open_type]').val())
            };

            <#-- 카테고리 추가 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/category/add/t/"
                    moveuri="#${Request.contextPath}/admin/board/manage" errortitle="카테고리 추가 실패" ; job >
                <#if job == "success_job">
                    <#-- 카테고리 추가 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = reqData.category_name + ' 카테고리를 추가 하였습니다.';
                    <@am.jsnoti title="카테고리 추가 성공" content="toastMsg" boxtype="success" />
                    <#-- 카테고리 추가 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                    my.fn.removeDatatableStorage('manage-board-category-list');
                <#elseif job == "common_job">
                    <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 카테고리 추가');
                </#if>
            </@am.jsajax_request>
        });

    };

    pagefunction();
</script>