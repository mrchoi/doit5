<link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/bootstrap-datetimepicker.min.css">

<#assign _page_id="notice_add">
<#assign _page_parent_name="슈퍼바이저 관리">
<#assign _page_current_name="슈퍼바이저 등록">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}"
canMoveParent=true/>

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    슈퍼바이저를 등록 합니다.<br>
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
            <@am.widget_title icon="fa-edit">슈퍼바이저 등록</@am.widget_title>

            <#-- widget div-->
                <div>
                <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                <#-- end widget edit box -->

                <#-- widget content -->
                    <div class="widget-body">
                    <#-- 나머지 게시물 정보 입력 -->
                        <form>
                            <fieldset style="padding:10px;">
                                <legend>추가할 슈퍼바이저 정보를 입력 합니다.</legend>

                                <div class="row">
                                <@am.bstrap_in_txt name="document_title" value="" title="성함" maxlength="64" placeholder="최대 64byte 까지 가능 합니다."/>
                                </div>
                                <#-- 선택한 템플릿 형태가 들어 간다 -->
                                <div class="row" id="template-form"></div>
                            </fieldset>

                        </form>
                    <#-- 나머지 게시물 정보 입력 끝 -->

                    <#-- Action 버튼 -->
                        <div class="form-actions">
                            <div class="btn-group">
                                <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                    <i class="fa fa-check"></i> 슈퍼바이저 등록
                                </button>
                            </div>
                            <div class="btn-group pull-left">
                                <button class="btn btn-sm btn-info btn-prev-page" type="button">
                                    <i class="fa fa-chevron-left"></i> 목록보기
                                </button>
                            </div>
                        </div>
                    <#-- Action 버튼 끝 -->

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

        drawBreadCrumb(['슈퍼바이저 등록']);

        <#-- 저장된 템플릿 데이터를 변수화 한다 -->
        var templateData = {
            ${templateEntity.template_srl} : _.isObject(${templateEntity.template_content}) ?
            ${templateEntity.template_content} : eval('(' + ${templateEntity.template_content} + ')'),
         };

    <#-- 게시판 관리로 이동 -->
        $('.btn-prev-page').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/supervisor/list');
        });


    <#--
        태그 입력시 기존에 저장되지 않은 것도 선택 가능하게 한다. 신규 태그는 tag_srl 이 string 으로 판단 하면 된다.
        신규 태그는 tag_srl 과 tag_name 이 동일하게 들어 간다.
    -->
        var createSearchChoice = function(term, data) {
        <#-- 만일 기존에 저장된 것이 존재하면 비교해서 결과를 보여 준다 -->
            if(data.length > 0) {
                var isExist = false;
                for(var i=0 ; i<data.length ; i++) {
                    if($.trim(data[i].tag_name) == $.trim(term)) {
                        isExist = true;
                        break;
                    }
                }

                if(isExist) return data;
                else        return data.unshift({tag_srl: term, tag_name: term});
            }

        <#-- 기존에 저장된 것이 존재하지 않으면 입력된 값을 선택 할 수 있게 한다 -->
            if($(data).filter(function() {return this.text.localeCompare(term) === 0;}).length === 0) {
                return {tag_srl: term, tag_name: term};
            }
        };

        <#-- summernote -->
        $('#summernote-content').summernote({
            height : 400,
            focus : false,
            tabsize : 2
        });


         <#-- 템플릿 선택을 변경 하면 맞는 템플릿 폼을 보여 준다 -->
        var $templateInputForm = $('#template-form');
        my.fn.showSupervisorTemplateForm($templateInputForm, templateData[${templateEntity.template_srl}]);


        <#-- 슈퍼바이 추가 -->
        $('#btn-add').click(function(e) {
        <@am.jsevent_prevent />

            var docTitle = $('input[name=document_title]').val();//$('#document_title').val();
            if($.trim(docTitle) == '') {
            <@am.jsnoti title="슈퍼바이저 등록 실패" content="성함을 입력 하세요." contenttype="text" />
                return;
            }

        <#-- ajax 요청 하기 전에 사전 작업 -->
            $('.invalid').hide();
            var $btnAdd = $(this);
            $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 등록중');

        <#-- 템플릿을 사용하면 템플릿 데이터를 만든다 -->
            var templateSrl = ${templateEntity.template_srl};
            var templateExtra = '';
            if(templateSrl > 0) {
                var templateObj = {};
                my.fn.getTemplateUsingData(templateData[templateSrl], templateObj);
                templateExtra = JSON.stringify(templateObj);
            }

        <#-- request 할 데이터를 만든다.(reqData 라는 이름을 왠만하면 바꾸지 말자. 이름을 그대로 사용해야 macro 사용이 편해짐) -->
            var reqData = {
                app_srl:            ${appEntity.app_srl},
                board_srl:          ${documentBoardEntity.board_srl},
                category_srl:       ${documentCategoryEntity.category_srl},
                document_title:     encodeURIComponent(docTitle),
                document_content:   $('#summernote-content').code(),
                read_count:         0,
                like_count:         0,
                blame_count:        0,
                comment_count:      0,
                file_count:         0,
                outer_link:         '',
                secret:             ${mdv_none}, //-1 비밀글 아님   mdv_yes:1 mdv_no:2 mdv_none:-1
                block:              ${mdv_no},
                allow_comment:      ${mdv_yes}, //1 댓글 허용함 2 댓글 허용하지 않음
                allow_notice:       ${mdv_no},
                list_order:         ${mdv_none},
                template_srl:       templateSrl,
                template_extra:     templateExtra,
                admin_tags:         [],
                user_tags:          [],
                attach_file_srls:   []
            };

        <#-- 슈퍼바이저 등록 요청 -->
        <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/add/t/"
        moveuri="#${Request.contextPath}/admin/supervisor/list" errortitle="슈퍼바이저 등록 실패" ; job >
            <#if job == "success_job">
            <#-- 슈퍼바이저 등록 성공하면 toast 메시지를 보여 준다 -->
                var toastMsg = '슈퍼바이저를 등록 하였습니다.';
                <@am.jsnoti title="슈퍼바이저 등록 성공" content="toastMsg" boxtype="success" />
            <#-- 게시물 등록 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                my.fn.removeDatatableStorage('supervisor_list');
            <#elseif job == "common_job">
            <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 슈퍼바이저 등록');
            </#if>
        </@am.jsajax_request>
        });
    };

    var pagedestroy = function(){
        $("#summernote-content").summernote('destroy');
    };

    loadScript("${Request.resPath}js/plugin/clockpicker/clockpicker.min.js", function(){
        loadScript("${Request.resPath}js/plugin/moment/moment.min.js", function() {
            loadScript("${Request.resPath}js/plugin/moment/ko.js", function() {
                loadScript("${Request.resPath}js/plugin/jquery-form/jquery-form.min.js", function() {
                    loadScript("${Request.resPath}js/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js", function () {
                        loadScript("${Request.resPath}js/plugin/summernote/summernote.min.js", pagefunction);
                    });
                });
            });
        });
    });

</script>