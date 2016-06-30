<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/clockpicker/clockpicker.min.js"></script>
<script src="${Request.resPath}js/plugin/moment/moment.min.js"></script>
<script src="${Request.resPath}js/plugin/moment/ko.js"></script>
<script src="${Request.resPath}js/plugin/jquery-form/jquery-form.min.js"></script>
<script src="${Request.resPath}js/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>
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
<#assign _page_id="user-one-add">
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
                        <h2>${_page_current_name}</h2>
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
                            <label class="control-label" for="category">진행중인 검사 및 상담 선택 <span class="required">*</span></label>
                            <select id="application_group" name="application_group" class="form-control selectpicker">
                                <option id="index_" value="${mdv_none}" data-file-srl="${mdv_none}">선택없음</option>
                                <#list applicationEntities as applicationEntity>
                                <option id="index_${applicationEntity_index}" value="${applicationEntity.application_group}" data-file-srl="${applicationEntity.advisor_srl}">[<#if applicationEntity.kind == 1 >심리상담<#else>심리검사</#if>] ${applicationEntity.title}</option>
                                </#list>
                            </select>
                        </div>

                        <!-- Text input-->
                        <div class="form-group">
                            <label class="control-label" for="document_title">제목 <span class="required">*</span></label>
                            <input id="document_title" name="document_title" type="text" placeholder="제목을 입력해 주세요" class="form-control input-md" required>
                        </div>

                        <#-- 게시물 본문 -->
                        <div class="form-group" >
                            <label class="control-label" for="document_content">내용 </label>
                            <#--<textarea class="form-control" rows="6" id="document_content" name="document_content" placeholder="내용을 입력해 주세요"></textarea>-->
                            <div id="summernote-content" class="summernote"/>
                        <#-- 선택한 템플릿 형태가 들어 간다 -->
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
                            <div class="btn-group pull-right">
                                <button id="btn-add" name="submit" class="btn tp-btn-primary" type="button">
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

    <#-- 템플릿을 사용하면 템플릿 데이터를 만든다 -->
    var templateSrl = ${templateEntity.template_srl};
    var templateExtra = '{"application_group":"-1","advisor_srl":"-1"}';

    $('#application_group').each(function(index) {

        var application_group = "${mdv_none}";
        var advisor_srl = "${mdv_none}";

        $(this).click(function (e) {
        <@am.jsevent_prevent />
            application_group = $('#application_group option:selected').val();

            //console.log(application_group);
            if(application_group == "" || "${mdv_none}") advisor_srl = $('#index_').attr('data-file-srl');
            else advisor_srl = $('#index_'+index).attr('data-file-srl');
            //console.log(advisor_srl);

            templateExtra = '{"application_group":"'+application_group+'","advisor_srl":"'+advisor_srl+'"}';

        });
    });

    console.log(templateExtra);


<#-- 게시물 추가 -->
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
        $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 등록중');

    <#-- request 할 데이터를 만든다.(reqData 라는 이름을 왠만하면 바꾸지 말자. 이름을 그대로 사용해야 macro 사용이 편해짐) -->
        var reqData = {
            app_srl:            ${appEntity.app_srl},
            board_srl:          ${documentBoardEntity.board_srl},
            category_srl:       ${documentCategoryEntity.category_srl},
            document_title:     encodeURIComponent(docTitle),
            document_content:   $('#summernote-content').code(),
            //document_content:   docContent,
            read_count:         0,
            like_count:         0,
            blame_count:        0,
            comment_count:      0,
            file_count:         0,
            outer_link:         '',
            secret:             ${mdv_none}, //-1 비밀글 아님   mdv_yes:1 mdv_no:2 mdv_none:-1
            block:              ${mdv_no}, //1 차단됨 2 차단되지 않음
            allow_comment:      ${mdv_yes}, //1 댓글 허용함 2 댓글 허용하지 않음
            allow_notice:       ${mdv_no}, //1 공지 게시물 2 일반게시물
            list_order:         ${mdv_none},
            template_srl:       templateSrl,//${mdv_none},// -1 이면 템플릿 사용하지 않음
            template_extra:     templateExtra,
            admin_tags:         [],
            user_tags:          [],
            attach_file_srls:   []
        };

        <#-- 게시물 등록 요청 -->
       <@ap.py_jsajax_request submituri="${Request.contextPath}/user/board/document/add/t/"
        moveuri="${Request.contextPath}/user/one/list" errortitle="게시물 등록 실패" ; job >
            <#if job == "success_job">
            // 게시믈 등록 성공하면 toast 메시지를 보여 준다
                var toastMsg = '게시물을 등록 하였습니다.';
                <@ap.py_jsnoti title="게시물 등록 성공" content="toastMsg" boxtype="success" contenttype="var" />
            // 게시물 등록 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다.
                my.fn.removeDatatableStorage('user-one-list');
            <#elseif job == "common_job">
            // 공통 작업. disabled 시킨 버튼을 enabled 시킨다
                $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 등록');
            </#if>
        </@ap.py_jsajax_request>
    });

    //var pagedestroy = function(){
        $("#summernote-content").summernote('destroy');
    //};

});

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
