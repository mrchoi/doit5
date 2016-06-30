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
    .modal-content {
        height: 100% !important;
        overflow: visible;
    }
    .modal-dialog {
        /*height: 70% !important;*/
        padding-top:10%;
    }
</style>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->

<#assign _page_id="user-secret-add">
<#assign _page_parent_name="문의게시판">
<#assign _page_current_name="문의게시판 등록">
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
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="well-box">
                    <form >
                        <!-- Text input-->
                        <div class="form-group">
                            <label class="control-label" for="document_title">제목 <span class="required">*</span></label>
                            <input id="document_title" name="document_title" type="text" placeholder="제목을 입력해 주세요" class="form-control input-md" required>
                        </div>

                        <!-- Textarea -->
                        <div class="form-group">
                            <label class="control-label" for="document_content">내용</label>
                            <div id="summernote-content" class="summernote"/>
                        </div>

                        <!-- File -->
                        <div class="form-group">
                        <#-- 첨부 파일을 등록하면 첨부 파일이 들어 간다 -->
                            <div id="attach-file-area" style="display: none;">
                                <hr>첨부파일
                                <div class="row">
                                    <div style="padding-left:14px;padding-right:14px;">
                                        <ul class="inbox-download-list" id="attach-file-list"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Button -->
                        <div class="form-group">
                            <button class="btn tp-btn-primary btn-prev-page">
                                <i class="fa fa-chevron-left"></i> 목록
                            </button>
                            <div class="btn-group pull-right">
                                <div class="btn-group">
                                    <button id="btn-add-file" class="btn btn-sm btn-default" type="button">
                                        <i class="fa fa-save"></i> 첨부파일 추가
                                    </button>
                                </div>

                                <button id="btn-add" name="submit" class="btn tp-btn-primary" type="button">
                                    <i class="fa fa-check"></i> 게시물 등록
                                </button>
                            </div>

                        </div>


                    </form>

                </div>
            </div>
        </div>

    </div>
</div>

<#-- remote modal area -->
<@ap.py_bstrap_file_uploader_modal url="${Request.contextPath}/user/resource/document/add/file" />


<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
$(document).ready(function() {

    $('.btn-prev-page').click(function(e){
    <@ap.py_jsevent_prevent />
        my.fn.pmove('${Request.contextPath}/user/question/list');
    });
    <#-- summernote -->
    $('#summernote-content').summernote({
        height : 400,
        focus : false,
        tabsize : 2
    });



    <#-- 파일 업로드 ajax form 을 사용해서 파일을 업로드 하고 게시물에 첨부 이미지를 붙인다. -->
        var $attachFileContainer = $('#attach-file-list');
    <@am.bstrap_file_uploader_ajax fileContainer="$attachFileContainer" />


    <#--
        첨부 파일 삭제를 눌렀을때 첨부 파일 이미지 뷰를 삭제 한다. 뷰만 삭제하고 실제 삭제는 게시물 수정시에 반영 한다.
    -->
        $('#attach-file-list').off('click').on('click', '.delete-document-attach', function (e) {
        <@ap.py_jsevent_prevent />
            var $this = $(this), fileSrl = $this.attr('data-file-srl'),
                    $element = $this.parents('li');

            $element.remove();
            if($attachFileContainer.children().length <= 0) $('#attach-file-area').hide();
        });

    <#-- 첨부 파일 추가 버튼 클릭시 파일 업로드 창을 보여 준다 -->
        $('#btn-add-file').click(function(e){
        <@ap.py_jsevent_prevent />

        <#-- 파일 업로드 폼을 초기화 하고 모달을 보여 준다 -->
            $('#file-uploader-form').each(function(){ this.reset(); });
            $('#file-uploader-remote-modal').modal('show');
        });


    <#-- 게시물 추가 -->
    $('#btn-add').click(function(e) {
    <@am.jsevent_prevent />

        var docTitle = $('#document_title').val();
        if($.trim(docTitle) == '') {
        <#--<@am.jsnoti title="게시물 등록 실패" content="게시물 제목을 입력 하세요." contenttype="text" />-->
        <@ap.py_jsnoti title="게시물 등록 실패" content="게시물 제목을 입력 하세요." contenttype="text"/>
            return;
        }
        /*var docContent = $('#document_content').val();
        if($.trim(docContent) == '') {
        <@ap.py_jsnoti title="게시물 수정 실패" content="게시물 내용을 입력 하세요." contenttype="text" />
            return;
        }*/

        <#-- ajax 요청 하기 전에 사전 작업 -->
        $('.invalid').hide();
        var $btnAdd = $(this);
        $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 등록중');

        <#-- 첨부 파일이 존재하면 첨부 파일의 시리얼 넘버를 구한다. -->
        var attachFileSrls = [];
        var $attachLi = $attachFileContainer.children();
        if($attachLi.length > 0) {
            $attachLi.each(function(){
                attachFileSrls.push($(this).attr('data-file-srl'));
            });
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
            block:              ${mdv_no}, //1 차단됨 2 차단되지 않음
            allow_comment:      ${mdv_yes}, //1 댓글 허용함 2 댓글 허용하지 않음
            allow_notice:       ${mdv_no}, //1 공지 게시물 2 일반게시물
            list_order:         ${mdv_none},
            template_srl:       ${mdv_none},// -1 이면 템플릿 사용하지 않음
            template_extra:     '',
            admin_tags:         [],
            user_tags:          [],
            attach_file_srls:   attachFileSrls
        };

    <#-- 게시물 등록 요청 -->
    <@ap.py_jsajax_request submituri="${Request.contextPath}/user/board/document/add/t/"
    moveuri="${Request.contextPath}/user/question/list" errortitle="등록 실패" ; job >
        <#if job == "success_job">
        <#-- 게시믈 등록 성공하면 toast 메시지를 보여 준다 -->
            var toastMsg = '게시물을 등록 하였습니다.';
            <@ap.py_jsnoti title="등록 성공" content="toastMsg" boxtype="success" contenttype="var" />
        <#elseif job == "common_job">
        <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
            $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 등록');
        </#if>
    </@ap.py_jsajax_request>
    });

    var pagedestroy = function(){
        $("#summernote-content").summernote('destroy');
    };
});

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
