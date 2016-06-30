<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/clockpicker/clockpicker.min.js"></script>
<script src="${Request.resPath}js/plugin/moment/moment.min.js"></script>
<script src="${Request.resPath}js/plugin/moment/ko.js"></script>
<script src="${Request.resPath}js/plugin/jquery-form/jquery-form.min.js"></script>
<script src="${Request.resPath}js/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script src="${Request.resPath}js/plugin/summernote/summernote.min.js"></script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-secret-detail">
<#assign _page_parent_name="비밀공간">
<#assign _page_current_name="비밀공간 보기">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<@ap.py_popup_layer />

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
                            <label class="control-label" for="title">제목 <span class="required">*</span></label>
                            <div class="well">${documentEntity.document_title}</div>
                        </div>

                        <!-- Textarea -->
                        <div class="form-group">
                            <label class="control-label" for="content">내용</label>

                            <div class="well">${documentEntity.document_content}</div>
                        </div>

                        <!-- File -->
                        <div class="form-group">
                        <#-- 첨부 파일을 등록하면 첨부 파일이 들어 간다 -->
                            <div id="attach-file-area" style="display: none;">
                                <label class="control-label" for="attach">첨부파일</label>
                                <hr>
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
                            <#if login_id == documentEntity.user_id>
                            <button id="btn-delete" name="btn-delete" class="btn tp-btn-lg btn-danger" >
                                <i class="fa fa-trash-o"></i> 삭제
                            </button>
                            <div class="btn-group pull-right">
                                <button id="btn-modify" name="submit" class="btn tp-btn-primary" type="button">
                                    <i class="fa fa-check"></i> 수정
                                </button>
                            </div>
                            </#if>

                        </div>
                    </form>
                </div>
            </div>
        </div>

        <#-- documentEntity.allow_comment = 1 (댓글 허용함) 일때만 보여줌 -->
        <#if documentEntity.allow_comment == mdv_yes>
        <div class="row"> <!-- 댓글 /  -->
            <div class="col-md-12 leave-comments rc-post-holder">
                <h3>댓글</h3>
                <form class="form-horizontal">
                    <!-- Textarea -->
                    <div class="form-group">
                        <div class="col-md-10">
                            <textarea class="form-control" id="comment_content" rows="5"></textarea>
                        </div>
                        <div class="col-md-1">
                            <button class="btn tp-btn-primary tp-btn-lg">댓글등록</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="col-md-12">
                <div class="customer-review">
                    <div class="review-list">
                        <!-- First Comment -->
                        <#list documentCommentEntities as documentCommentEntity>
                            <div class="row" id="dt-row-comment-${documentCommentEntity.comment_srl}">
                                <div class="col-md-12 col-sm-12">
                                    <div class="panel panel-default arrow left">
                                        <div class="panel-body">
                                            <div class="text-left">
                                                <div class="col-md-2"><h3>${documentCommentEntity.nick_name}</h3> </div>
                                                <div class="comment-date">
                                                    <i class="fa fa-clock-o"></i>
                                                    <#assign c_date = documentCommentEntity.c_date*1000>
                                                    <#assign cDate = c_date?number_to_datetime>
                                                ${cDate}
                                                    <#if login_id == documentCommentEntity.user_id>
                                                    <div class="pull-right">
                                                        <a href="#" class="btn-comment-delete btn tp-btn-default" data-id="${documentCommentEntity.comment_srl}">삭제</a>
                                                    </div>
                                                    </#if>
                                                </div>
                                            </div>
                                            <div class="review-post">
                                                <p> ${documentCommentEntity.comment_content} </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    </div>
                </div>
            </div>
        </div><!-- / 댓글  -->
        </#if>
    </div>
</div>


<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {

        $('.btn-prev-page').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/secret/list');
        });

        $('#btn-modify').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/secret/modify/${documentEntity.document_srl}');
        });
        <#-- 파일 업로드 ajax form 을 사용해서 파일을 업로드 하고 게시물에 첨부 이미지를 붙인다. -->
            var $attachFileContainer = $('#attach-file-list');
        <@am.bstrap_file_uploader_ajax fileContainer="$attachFileContainer" />

        <#-- 게시물에 첨부 파일이 있으면 첨부 파일을 보여 준다 -->
        <#if documentEntity.documentAttachEntities?size gt 0>$('#attach-file-area').show();</#if>
        <#list documentEntity.documentAttachEntities as item>
            my.fn.addDocumentAttachToDocument_plymind($attachFileContainer,
                    {
                        file_srl: ${item.file_srl},
                        file_url: '${image_server_host}${Request.contextPath}${document_attach_download_uri}${item.file_srl}',
                        file_image_url: '${image_server_host}${Request.contextPath}${document_attach_uri}${item.file_srl}',
                        orig_name: '${item.orig_name}',
                        file_size: ${item.file_size},
                        width: 120,
                        height: 150
                    },
                        <#if item.mime_type?index_of("image") gte 0>true<#else>false</#if>, false);
        </#list>


        <#-- 게시물 삭제 -->
        $('#btn-delete').click(function(e){
        <@am.jsevent_prevent />
            var $this = $(this);
            var reqData = {l_keys: [${documentEntity.document_srl}]};
            var successText = '게시물을 삭제 했습니다.';
            //var process = function() {
                $this.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');
            <@ap.py_jsajax_request submituri="${Request.contextPath}/user/board/document/t/"
            method="DELETE" errortitle="게시물 삭제 실패" ; job >
                <#if job == "success_job">
                <#-- 삭제 성공시 리스트 페이지로 이동 -->
                    <@ap.py_jsnoti title="게시물 삭제 완료" content="successText" boxtype="success" />
                <#-- 헐...이거 click 반응으로 사용자 반응 이후 호출 되는 곳 말고, 자동으로 시나리오 타고 오는 경우
                     my.fn.pmove 를 바로 호출 하니 message box 에 문제 생긴다. 약간의 딜레이를 준다 -->
                    setTimeout(function(){
                        my.fn.pmove('${Request.contextPath}/user/secret/list');
                    }, 500);

                <#elseif job == "common_job">
                    $this.removeClass('disabled').html('<i class="fa fa-trash-o"></i> 삭제');
                </#if>
            </@ap.py_jsajax_request>
            //};
            //my.fn.showUserConfirm('게시물',
            //        "사항을 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
            //        '삭제를 입력하세요',
            //        '네', '삭제', process, '공지사항 삭제 실패', '삭제를 입력하지 않았습니다.');
        });

        <#-- 댓글 등록 -->
        $('.tp-btn-lg').click(function(e) {
        <@ap.py_jsevent_prevent />
            //var Comment = $('textarea[name="comment_content"]:visible').val();
            var Comment = $('#comment_content').val();
            if($.trim(Comment) == '') {
            <#--<@ap.py_jsnoti title="댓글 등록 실패" content="댓글을 입력 하세요." contenttype="text"/>-->
                $('#pop-message').text('댓글을 입력 하세요.');
                popup_layer();
                return;
            }

            var reqData = {
                app_srl:            ${appEntity.app_srl},
                board_srl:          ${documentBoardEntity.board_srl},
                category_srl:       ${documentEntity.category_srl},
                document_srl:       ${documentEntity.document_srl},
                head_comment_srl:   ${mdv_none},
                parent_comment_srl: ${mdv_none},
                comment_depth:      ${mdv_none},
                comment_content:    $('#comment_content').val(),
                like_count:         0,
                blame_count:        0,
                child_comment_count:0,
                file_count:         0,
                block:              ${mdv_none},
                member_srl:         '',
                user_id:            '',
                user_name:          '',
                nick_name:          '',
                email_address:      '',
                ipaddress:          ''
            };

        <#-- 댓글 등록 요청 -->
        <@ap.py_jsajax_request submituri="${Request.contextPath}/user/board/document/comment/add/t/" method="POST"
        moveuri="${Request.contextPath}/user/secret/${documentEntity.document_srl}" errortitle="댓글 등록 실패" ; job >
            <#if job == "success_job">
            <#-- 댓글 등록 성공하면 toast 메시지를 보여 준다 -->
                var toastMsg = '댓글을 등록 하였습니다.';
                <@ap.py_jsnoti title="댓글 등록 성공" content="toastMsg" boxtype="success" />

            <#elseif job == "common_job">
            <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 수정');
            </#if>
        </@ap.py_jsajax_request>
        });

        <#-- 댓글 삭제 요청 -->
        $('.btn-comment-delete').on("click",function(e) {
        <@ap.py_jsevent_prevent />
            var commentSrl = $(this).data("id");
            var reqData = {l_keys: [commentSrl]};
            var successText = '댓글을 삭제 했습니다.';
            var $btnDel = $(this);
            $btnDel.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');
        <@ap.py_jsajax_request submituri="${Request.contextPath}/user/board/document/comment/t/"
        method="DELETE" errortitle="댓글 삭제 실패" ; job >
            <#if job == "success_job">
            <#-- 삭제 성공시 테이블을 다시 로딩. remove 하면 내부 데이터만 삭제되지 화면에 반영 되지 않는다.
                 draw 를 호출 해야 반영 된다. -->
                <@ap.py_jsnoti title="댓글 삭제 완료" content="successText" boxtype="success" />
                $('#dt-row-comment-'+commentSrl).remove();
            <#elseif job == "common_job">
                $btnDel.removeClass('disabled').html('<span>댓글 삭제</span>');
            </#if>
        </@ap.py_jsajax_request>
        });


});

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
