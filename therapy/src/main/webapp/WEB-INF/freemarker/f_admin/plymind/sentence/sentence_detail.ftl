<link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/bootstrap-datetimepicker.min.css">

<#assign _page_id="sentence_detail">
<#assign _page_parent_name="너나들이 관리">
<#assign _page_current_name="너나들이 수정">

<@am.page_simple_depth icon="fa-puzzle-piece" parent="${_page_parent_name}" current="${_page_current_name}"
canMoveParent=true />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    등록된 너나들이를 확인하고 수정하는 페이지 입니다.
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
            <@am.widget_title icon="fa-eye">너나들이 수정</@am.widget_title>

            <#-- widget div-->
                <div>
                <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                <#-- end widget edit box -->

                <#-- widget content -->
                    <div class="widget-body">
                    <#-- 나머지 게시물 정보 입력 -->
                        <div>
                            <@am.bstrap_in_txt name="document_content" value="${documentEntity.document_content?html}" title="문구" maxlength="64" placeholder="최대 64byte 까지 가능 합니다."/>
                            <#-- 선택한 템플릿 형태가 들어 간다 -->
                        </div>
                    <#-- 게시물 본문 -->
                        <#--<div id="summernote-content" class="summernote">${documentEntity.document_content}</div>-->
                    <#-- 게시물 본문 끝 -->
                        <div class="row">
                        <#-- 선택한 템플릿 형태가 들어 간다 -->
                            <div id="template-form"></div>
                        </div>


                    <#-- Action 버튼 -->
                        <div class="widget-footer smart-form">
                            <div class="btn-group">
                                <button id="btn-delete" class="btn btn-sm btn-danger" type="button">
                                    <i class="fa fa-trash-o"></i> 너나들이 삭제
                                </button>
                            </div>
                            <div class="btn-group">
                                <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                    <i class="fa fa-check"></i> 너나들이 수정
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

    <#-- js_string 덕택에 별다른 model 에서 줄때 부터 인코딩 하지 않음. -->
    <#--drawBreadCrumb([decodeURIComponent('${documentEntity.document_title}')]);-->
        drawBreadCrumb(['게시물 수정']);

    <#-- 게시판 관리로 이동 -->
        $('.btn-prev-page').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/sentence/list');
        });

    <#-- summernote -->
        $('#summernote-content').summernote({
            height : 400,
            focus : false,
            tabsize : 2
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


        <#-- 저장된 템플릿 데이터를 변수화 한다 -->
        var templateData = {
        <#list templateEntities as templateEntity>
            ${templateEntity.template_srl} : _.isObject(${templateEntity.template_content}) ?
            ${templateEntity.template_content} : eval('(' + ${templateEntity.template_content} + ')'),
        </#list>
        };

        <#-- 템플릿 선택을 변경 하면 맞는 템플릿 폼을 보여 준다 -->
        var $templateInputForm = $('#template-form');
        my.fn.showSupervisorTemplateForm($templateInputForm, templateData[${templateEntity.template_srl}],
        ${templateEntity.template_srl},
        ${documentEntity.template_srl},
                _.isObject(${documentEntity.template_extra}) ? ${documentEntity.template_extra} :
                        eval('(' + ${documentEntity.template_extra} + ')'));


    <#-- 너나들이 삭제 -->
        $('#btn-delete').click(function(e){
        <@am.jsevent_prevent />

            var $this = $(this);
            var reqData = {l_keys: [${documentEntity.document_srl}]};
            var successText = '너나들이를 삭제 했습니다.';
            var process = function() {
                $this.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');
            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/t/"
            method="DELETE" errortitle="너나들이 삭제 실패" ; job >
                <#if job == "success_job">
                <#-- 삭제 성공시 리스트 페이지로 이동 -->
                    <@am.jsnoti title="너나들이 삭제 완료" content="successText" boxtype="success" />
                <#-- 헐...이거 click 반응으로 사용자 반응 이후 호출 되는 곳 말고, 자동으로 시나리오 타고 오는 경우
                     my.fn.pmove 를 바로 호출 하니 message box 에 문제 생긴다. 약간의 딜레이를 준다 -->
                    setTimeout(function(){
                        my.fn.pmove('#${Request.contextPath}/admin/sentence/list');
                    }, 300);

                <#elseif job == "common_job">
                    $this.removeClass('disabled').html('<i class="fa fa-trash-o"></i> 너나들이 삭제');
                </#if>
            </@am.jsajax_request>
            };

            my.fn.showUserConfirm('너나들이 삭제',
                    "너나들이를 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                    '삭제를 입력하세요',
                    '네', '삭제', process, '너나들이 삭제 실패', '삭제를 입력하지 않았습니다.');
        });

    <#-- 너나들이 수정 -->
        $('#btn-add').click(function(e) {
        <@am.jsevent_prevent />

            var docContent = $('input[name=document_content]').val();//$('#document_title').val();
            if($.trim(docContent) == '') {
            <@am.jsnoti title="너나들이 등록 실패" content="문구를 입력 하세요." contenttype="text" />
                return;
            }
        <#-- ajax 요청 하기 전에 사전 작업 -->
            $('.invalid').hide();
            var $btnAdd = $(this);
            $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 수정 중');

        <#-- 템플릿을 사용하면 템플릿 데이터를 만든다 -->
            var templateSrl = ${templateEntity.template_srl};
            var templateExtra = '';
            if(templateSrl > 0) {
                var templateObj = {};
                my.fn.getPlymindTemplateUsingData(templateData[templateSrl], templateObj);
                templateExtra = JSON.stringify(templateObj);
            }

        <#-- request 할 데이터를 만든다.(reqData 라는 이름을 왠만하면 바꾸지 말자. 이름을 그대로 사용해야 macro 사용이 편해짐) -->
            var reqData = {
                app_srl:            ${appEntity.app_srl},
                board_srl:          ${documentBoardEntity.board_srl},
                category_srl:       ${documentEntity.category_srl},
                document_title:     '11',
                document_content:   docContent,
                read_count:         ${documentEntity.read_count},
                like_count:         ${documentEntity.like_count},
                blame_count:        ${documentEntity.blame_count},
                comment_count:      ${documentEntity.comment_count},
                file_count:         ${documentEntity.file_count},
                outer_link:         '',
                secret:             ${documentEntity.secret},
                block:              ${mdv_no},
                allow_comment:      ${mdv_no},
                allow_notice:       ${documentEntity.allow_notice},
                list_order:         ${documentEntity.list_order},
                template_srl:       templateSrl,
                template_extra:     templateExtra,
                admin_tags:         [],
                user_tags:          [],
                attach_file_srls:   []
            };

        <#-- 너나들이 수정 요청 -->
        <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/${documentEntity.document_srl}/t/" method="PUT"
        moveuri="#${Request.contextPath}/admin/sentence/list" errortitle="수정 실패" ; job >
            <#if job == "success_job">
            <#-- 너나들이 수정 성공하면 toast 메시지를 보여 준다 -->
                var toastMsg = '너나들이를 수정 하였습니다.';
                <@am.jsnoti title="수정 성공" content="toastMsg" boxtype="success" />

            <#elseif job == "common_job">
            <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 너나들이 수정');
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