<link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/bootstrap-datetimepicker.min.css">

<#assign _page_id="theme_list">
<#assign _page_parent_name="테마게시판">
<#assign _page_current_name="게시물 수정">

<@am.page_simple_depth icon="fa-puzzle-piece" parent="${_page_parent_name}" current="${_page_current_name}"
canMoveParent=true />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    등록된 게시물을 확인하고 수정하는 페이지 입니다.
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
            <@am.widget_title icon="fa-eye">게시물 수정</@am.widget_title>

            <#-- widget div-->
                <div>
                <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                <#-- end widget edit box -->

                <#-- widget content -->
                    <div class="widget-body no-padding">
                    <#-- 게시물 제목 -->
                        <div class="widget-body-toolbar">
                            <div class="row">
                                <div class="smart-form">
                                    <label class="input" style="padding-left:10px;padding-right:10px;">
                                        <input id="document_title" placeholder="제목을 입력하세요(최대 64자)" value="${documentEntity.document_title?html}" />
                                    </label>
                                </div>
                            </div>
                        </div>
                    <#-- 게시물 제목 끝 -->

                    <#-- 게시물 본문 -->
                        <div id="summernote-content" class="summernote">${documentEntity.document_content}</div>
                    <#-- 게시물 본문 끝 -->

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

                    <#-- 나머지 게시물 정보 입력 -->
                        <form>
                            <fieldset style="padding:10px;">
                                <div class="row">
                                <#--<@am.bstrap_disabled_txt value="${documentEntity.user_id}" title="작성자 아이디" />
                                <@am.bstrap_disabled_txt value="${documentEntity.user_name}" title="작성자 이름" />
                                <@am.bstrap_disabled_txt value="${documentEntity.nick_name}" title="작성자 별명" />-->

                                <@am.bstrap_in_select name="block" title="차단 여부">
                                    <option value="${mdv_no}" <#if documentEntity.block == mdv_no>selected</#if>>차단하지 않음</option>
                                    <option value="${mdv_yes}" <#if documentEntity.block == mdv_yes>selected</#if>>차단</option>
                                </@am.bstrap_in_select>
                                </div>

                            </fieldset>
                        </form>
                    <#-- 나머지 게시물 정보 입력 끝 -->

                    <#-- Action 버튼 -->
                        <div class="widget-footer smart-form">

                            <div class="btn-group">
                                <button id="btn-add-file" class="btn btn-sm btn-default" type="button">
                                    <i class="fa fa-save"></i> 첨부파일 추가
                                </button>
                            </div>

                            <div class="btn-group">
                                <button id="btn-delete" class="btn btn-sm btn-danger" type="button">
                                    <i class="fa fa-trash-o"></i> 게시물 삭제
                                </button>
                            </div>
                            <div class="btn-group">
                                <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                    <i class="fa fa-check"></i> 게시물 수정
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

    <#-- documentEntity.allow_comment = 1 (댓글 허용함) 일때만 보여줌 -->
    <#if documentEntity.allow_comment == mdv_yes>
    <#-- Push message history list start -->
        <article class="col-sm-12 col-md-12 col-lg-12">
        <#-- Push message history list Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false"
                 data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">댓글</@am.widget_title>

            <#-- widget div-->
                <div>
                <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                <#-- end widget edit box -->

                    <div class="row"> <!-- 댓글 /  -->
                        <div class="col-md-12">
                            <div class="customer-review">
                                <div class="review-list">
                                    <!-- First Comment -->
                                    <div class="row">
                                        <div class="col-md-12 col-sm-12">
                                            <div class="panel panel-default arrow left">
                                                <div class="panel-body">
                                                    <div class="text-left">
                                                        <div class="comment-date">
                                                            <i class="fa fa-clock-o"></i>
                                                            <#assign nowDate = .now>
                                                            <#assign date = nowDate?datetime>
                                                        ${date}
                                                            <div id="btn-comment-add" class="pull-right">
                                                                <a href="#" class="btn tp-btn-default">댓글등록</a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="review-post">
                                                        <textarea id="comment_content" class="form-control" placeholder="" rows="5"></textarea>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div>

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
                                                                <div class="pull-right">
                                                                    <a href="#" class="btn-comment-delete" class="btn tp-btn-default" data-id="${documentCommentEntity.comment_srl}">삭제</a>
                                                                </div>
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
                </div>
            <#-- end widget div -->
            </div>
        </article>
    <#-- Push message history list end -->
    </#if>

    </div>
<#-- end row -->
</section>
<#-- end widget grid -->

<#-- remote modal area -->
<@am.bstrap_file_uploader_modal url="${Request.contextPath}/admin/resource/document/add/file" />

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
            my.fn.pmove('#${Request.contextPath}/admin/theme/list');
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
        <@am.jsevent_prevent />
            var $this = $(this), fileSrl = $this.attr('data-file-srl'),
                    $element = $this.parents('li');

            $element.remove();
            if($attachFileContainer.children().length <= 0) $('#attach-file-area').hide();
        });

    <#-- 첨부 파일 추가 버튼 클릭시 파일 업로드 창을 보여 준다 -->
        $('#btn-add-file').click(function(e){
            <@am.jsevent_prevent />

            <#-- 파일 업로드 폼을 초기화 하고 모달을 보여 준다 -->
            $('#file-uploader-form').each(function(){ this.reset(); });
            $('#file-uploader-remote-modal').modal('show');
        });

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
                        <#if item.mime_type?index_of("image") gte 0>true<#else>false</#if>, true);
        </#list>


    <#-- 게시물 삭제 -->
        $('#btn-delete').click(function(e){
        <@am.jsevent_prevent />

            var $this = $(this);
            var reqData = {l_keys: [${documentEntity.document_srl}]};
            var successText = '게시물을 삭제 했습니다.';
            var process = function() {
                $this.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');
            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/t/"
            method="DELETE" errortitle="게시물 삭제 실패" ; job >
                <#if job == "success_job">
                <#-- 삭제 성공시 리스트 페이지로 이동 -->
                    <@am.jsnoti title="공지사항 삭제 완료" content="successText" boxtype="success" />
                <#-- 헐...이거 click 반응으로 사용자 반응 이후 호출 되는 곳 말고, 자동으로 시나리오 타고 오는 경우
                     my.fn.pmove 를 바로 호출 하니 message box 에 문제 생긴다. 약간의 딜레이를 준다 -->
                    setTimeout(function(){
                        my.fn.pmove('#${Request.contextPath}/admin/theme/list');
                    }, 300);

                <#elseif job == "common_job">
                    $this.removeClass('disabled').html('<i class="fa fa-trash-o"></i> 게시물 삭제');
                </#if>
            </@am.jsajax_request>
            };

            my.fn.showUserConfirm('게시물 삭제',
                    "게시물을 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                    '삭제를 입력하세요',
                    '네', '삭제', process, '게시물 삭제 실패', '삭제를 입력하지 않았습니다.');
        });

    <#-- 게시물 수정 -->
        $('#btn-add').click(function(e) {
        <@am.jsevent_prevent />

            var docTitle = $('#document_title').val();
            if($.trim(docTitle) == '') {
            <@am.jsnoti title="게시물 수정 실패" content="제목을 입력 하세요." contenttype="text" />
                return;
            }

        <#-- ajax 요청 하기 전에 사전 작업 -->
            $('.invalid').hide();
            var $btnAdd = $(this);
            $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 수정 중');

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
                category_srl:       ${documentEntity.category_srl},
                document_title:     encodeURIComponent(docTitle),
                document_content:   $('#summernote-content').code(),
                read_count:         ${documentEntity.read_count},
                like_count:         ${documentEntity.like_count},
                blame_count:        ${documentEntity.blame_count},
                comment_count:      ${documentEntity.comment_count},
                file_count:         ${documentEntity.file_count},
                outer_link:         '',
                secret:             ${documentEntity.secret},
                block:              $('select[name=block]').val(),
                allow_comment:      ${documentEntity.allow_comment},
                allow_notice:       ${documentEntity.allow_notice},
                list_order:         ${documentEntity.list_order},
                template_srl:       ${documentEntity.template_srl},
                template_extra:     '',
                admin_tags:         [],
                user_tags:          [],
                attach_file_srls:   attachFileSrls
            };

        <#-- 게시물 수정 요청 -->
        <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/${documentEntity.document_srl}/t/" method="PUT"
        moveuri="#${Request.contextPath}/admin/theme/list" errortitle="수정 실패" ; job >
            <#if job == "success_job">
            <#-- 게시믈 수정 성공하면 toast 메시지를 보여 준다 -->
                var toastMsg = '게시물을 수정 하였습니다.';
                <@am.jsnoti title="수정 성공" content="toastMsg" boxtype="success" />

            <#elseif job == "common_job">
            <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 수정');
            </#if>
        </@am.jsajax_request>
        });

    <#-- 댓글 등록 -->
        $('#btn-comment-add').click(function(e) {

        <@am.jsevent_prevent />
            var Comment = $('#comment_content').val();
            if($.trim(Comment) == '') {
            <#--<@am.jsnoti title="게시물 등록 실패" content="게시물 제목을 입력 하세요." contenttype="text" />-->
            <@am.jsnoti title="댓글 등록 실패" content="댓글을 입력 하세요." contenttype="text"/>
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
        <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/comment/add/t/" method="POST"
        moveuri="#${Request.contextPath}/admin/theme/${documentEntity.document_srl}" errortitle="댓글 등록 실패" ; job >
            <#if job == "success_job">
            <#-- 댓글 등록 성공하면 toast 메시지를 보여 준다 -->
                var toastMsg = '댓글을 등록 하였습니다.';
                <@am.jsnoti title="댓글 등록 성공" content="toastMsg" boxtype="success" />

            <#elseif job == "common_job">
            <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 수정');
            </#if>
        </@am.jsajax_request>

        });

        <#-- 댓글 삭제 요청 -->
        $('.btn-comment-delete').on("click",function(e) {
        <@am.jsevent_prevent />
            var commentSrl = $(this).data("id");
            var reqData = {l_keys: [commentSrl]};
            var successText = '댓글을 삭제 했습니다.';
            var $btnDel = $(this);
            $btnDel.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');
        <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/comment/t/"
        method="DELETE" errortitle="댓글 삭제 실패" ; job >
            <#if job == "success_job">
            <#-- 삭제 성공시 테이블을 다시 로딩. remove 하면 내부 데이터만 삭제되지 화면에 반영 되지 않는다.
                 draw 를 호출 해야 반영 된다. -->
                <@am.jsnoti title="댓글 삭제 완료" content="successText" boxtype="success" />
                $('#dt-row-comment-'+commentSrl).remove();
            <#elseif job == "common_job">
                $btnDel.removeClass('disabled').html('<span>댓글 삭제</span>');
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