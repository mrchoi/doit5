<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/summernote/summernote.min.js"></script>
<style>
    .note-editable{
        background-color: #fdfdfb;
        -webkit-border-radius: 2px;
        -moz-border-radius: 2px;
        border-radius: 2px;
        border: 1px solid #e9e6e0;
    }
    textarea.note-codable{
        display: none;
    }
</style>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-one-modify">
<#assign _page_parent_name="나의 활동">
<#assign _page_current_name="1:1 문의하기">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h2>${_page_current_name} 수정</h2>
                    </div>
                    <#--<div class="col-md-12">
                        <div class="call-to-action well-box">
                            <p></p>
                        </div>
                    </div>-->
                </div>
            </div>
        </div>


        <div class="row">
            <div class="col-md-12">
                <div class="well-box">
                    <form >

                        <!-- Select Basic -->
                        <div class="form-group">
                            <label class="control-label" for="category">상담(검사)</label>
                            <div class="well">${applicationEntity.title}</div>
                        </div>

                        <!-- Text input-->
                        <div class="form-group">
                            <label class="control-label" for="document_title">제목 <span class="required">*</span></label>
                            <input id="document_title" name="document_title" type="text" placeholder="제목을 입력하세요(최대 64자)" class="form-control input-md" value="${documentEntity.document_title?html}" />
                        </div>

                        <#-- 게시물 본문 -->
                        <div class="form-group">
                            <label class="control-label" for="document_content">내용 <span class="required">*</span></label>
                           <#-- <textarea class="form-control" rows="6" id="document_content" name="document_content" placeholder="내용을 입력해 주세요"></textarea>-->
                                <div id="summernote-content" class="summernote">${documentEntity.document_content}</div>
                        </div>
                        <#-- 게시물 본문 끝 -->

                        <div class="form-group">
                            <label class="control-label" for="">상담관련 문의를 남겨주시면 상담사가 확인 후 답변을 드립니다.</label>
                        </div>

                        <!-- Button -->
                        <div class="form-group">
                            <button class="btn tp-btn-primary btn-prev-page">
                                <i class="fa fa-chevron-left"></i> 1:1 답변확인 목록
                            </button>
                            <div class="pull-right">
                                <button id="btn-add" name="btn-add" class="btn tp-btn-primary" >
                                    <i class="fa fa-check"></i> 등록
                                </button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>

    </div>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

$(document).ready(function() {

    $('.btn-prev-page').click(function(e){
    <@am.jsevent_prevent />
        my.fn.pmove('${Request.contextPath}/user/one/list');
    });

    <#-- summernote -->
    $('#summernote-content').summernote({
        height : 400,
        focus : false,
        tabsize : 2
    });

<#-- 게시물 수정 -->
    $('#btn-add').click(function(e) {
    <@am.jsevent_prevent />

        var docTitle = $('#document_title').val();
        if($.trim(docTitle) == '') {
        <#--<@am.jsnoti title="게시물 등록 실패" content="게시물 제목을 입력 하세요." contenttype="text" />-->
        <@ap.py_jsnoti title="게시물 등록 실패" content="게시물 제목을 입력 하세요." contenttype="text"/>
            return;
        }

    <#-- ajax 요청 하기 전에 사전 작업 -->
        $('.invalid').hide();
        var $btnAdd = $(this);
        $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 수정 중');

    <#-- 저장된 템플릿 데이터를 변수화 한다 -->
        /*var templateData = {
        <#list templateEntities as templateEntity>
            ${templateEntity.template_srl} : _.isObject(${templateEntity.template_content}) ?
            ${templateEntity.template_content} : eval('(' + ${templateEntity.template_content} + ')'),
        </#list>
        };*/

    <#-- 템플릿을 사용하면 템플릿 데이터를 만든다 -->
        /*var templateSrl = ${templateEntity.template_srl};
        var templateExtra = '';
        if(templateSrl > 0) {
            var templateObj = {};
            my.fn.getPlymindTemplateUsingData(templateData[templateSrl], templateObj);
            templateExtra = JSON.stringify(templateObj);
        }*/

    <#-- request 할 데이터를 만든다.(reqData 라는 이름을 왠만하면 바꾸지 말자. 이름을 그대로 사용해야 macro 사용이 편해짐) -->
        var reqData = {
            app_srl:            ${appEntity.app_srl},
            board_srl:          ${documentBoardEntity.board_srl},
            category_srl:       ${documentEntity.category_srl},
            document_title:     encodeURIComponent(docTitle),
            document_content:   $('#summernote-content').code(),
            read_count:         ${documentEntity.read_count},
            like_count:         ${documentEntity.like_count},
            blame_count:        ${documentEntity.blame_count},
            comment_count:      ${documentEntity.comment_count},
            file_count:         ${documentEntity.file_count},
            outer_link:         '',
            secret:             ${documentEntity.secret}, //-1 비밀글 아님   mdv_yes:1 mdv_no:2 mdv_none:-1
            block:              ${documentEntity.block}, //1 차단됨 2 차단되지 않음
            allow_comment:      ${documentEntity.allow_comment}, //1 댓글 허용함 2 댓글 허용하지 않음
            allow_notice:       ${documentEntity.allow_notice}, //1 공지 게시물 2 일반게시물
            list_order:         ${documentEntity.list_order},
            //template_srl:       ${documentEntity.template_srl},// -1 이면 템플릿 사용하지 않음
            //template_extra:     ${documentEntity.template_extra},
            admin_tags:         [],
            user_tags:          [],
            attach_file_srls:   []
        };

    <#-- 게시물 수정 요청 -->
    <@ap.py_jsajax_request submituri="${Request.contextPath}/user/board/document/${documentEntity.document_srl}/t/" method="PUT"
    moveuri="${Request.contextPath}/user/one/list" errortitle="수정 실패" ; job >
        <#if job == "success_job">
        <#-- 게시믈 수정 성공하면 toast 메시지를 보여 준다 -->
            var toastMsg = '게시물을 수정 하였습니다.';
            <@ap.py_jsnoti title="수정 성공" content="toastMsg" boxtype="success" contenttype="var" />
        <#elseif job == "common_job">
        <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
            $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 수정');
        </#if>
    </@ap.py_jsajax_request>
    });


});

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
