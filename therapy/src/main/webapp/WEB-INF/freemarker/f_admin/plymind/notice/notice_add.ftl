<link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/bootstrap-datetimepicker.min.css">

<#assign _page_id="notice_add">
<#assign _page_parent_name="공지사항/이벤트">
<#assign _page_current_name="게시물 등록">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}"
canMoveParent=true/>

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    공지사항/이벤트를 등록하는 페이지 입니다.
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
            <@am.widget_title icon="fa-edit">게시물 추가</@am.widget_title>

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
                                        <input id="document_title" placeholder="제목을 입력하세요(최대 64자)" />
                                    </label>
                                </div>
                            </div>
                        </div>
                    <#-- 게시물 제목 끝 -->

                    <#-- 게시물 본문 -->
                        <div id="summernote-content" class="summernote" />
                    <#-- 게시물 본문 끝 -->

                    <#-- 나머지 게시물 정보 입력 -->
                        <form>
                            <fieldset style="padding:10px;">
                                <div class="row">
                                <@am.bstrap_in_select name="allow_notice" title="공지사항/이벤트 구분">
                                    <option value="${mdv_no}">이벤트</option>
                                    <option value="${mdv_yes}">공지사항</option>
                                </@am.bstrap_in_select>
                            </fieldset>
                        </form>
                        <form>
                            <fieldset style="padding:10px;">
                                <div class="row">
                                <@am.bstrap_in_select name="block" title="차단 여부">
                                    <option value="${mdv_no}">차단하지 않음</option>
                                    <option value="${mdv_yes}">차단</option>
                                </@am.bstrap_in_select>
                            </fieldset>
                        </form>
                    <#-- 나머지 게시물 정보 입력 끝 -->

                    <#-- Action 버튼 -->
                        <div class="widget-footer smart-form">
                            <div class="btn-group">
                                <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                    <i class="fa fa-check"></i> 게시물 등록
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

        drawBreadCrumb(['게시물 등록']);

    <#-- 게시판 관리로 이동 -->
        $('.btn-prev-page').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/notice/list');
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

    <#-- 첨부 파일 추가 버튼 클릭시 파일 업로드 창을 보여 준다 -->
        $('#btn-add-file').click(function(e){
        <@am.jsevent_prevent />

        <#-- 파일 업로드 폼을 초기화 하고 모달을 보여 준다 -->
            $('#file-uploader-form').each(function(){ this.reset(); });
            $('#file-uploader-remote-modal').modal('show');
        });

    <#-- 게시물 추가 -->
        $('#btn-add').click(function(e) {
        <@am.jsevent_prevent />

            var docTitle = $('#document_title').val();
            if($.trim(docTitle) == '') {
            <@am.jsnoti title="게시물 등록 실패" content="게시물 제목을 입력 하세요." contenttype="text" />
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
                read_count:         0,
                like_count:         0,
                blame_count:        0,
                comment_count:      0,
                file_count:         0,
                outer_link:         '',
                secret:             ${mdv_none}, //-1 비밀글 아님   mdv_yes:1 mdv_no:2 mdv_none:-1
                block:              $('select[name=block]').val(),
                allow_comment:      ${mdv_yes}, //1 댓글 허용함 2 댓글 허용하지 않음
                allow_notice:       $('select[name=allow_notice]').val(), //1 공지 게시물 2 일반게시물
                list_order:         ${mdv_none},
                template_srl:       ${mdv_none},
                template_extra:     '',
                admin_tags:         [],
                user_tags:          [],
                attach_file_srls:   []
            };

        <#-- 게시물 등록 요청 -->
        <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/add/t/"
        moveuri="#${Request.contextPath}/admin/notice/list" errortitle="게시물 등록 실패" ; job >
            <#if job == "success_job">
            <#-- 게시믈 등록 성공하면 toast 메시지를 보여 준다 -->
                var toastMsg = '게시물을 등록 하였습니다.';
                <@am.jsnoti title="게시물 등록 성공" content="toastMsg" boxtype="success" />
            <#-- 게시물 등록 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                my.fn.removeDatatableStorage('notice_list');
            <#elseif job == "common_job">
            <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 등록');
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