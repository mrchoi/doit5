<link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/bootstrap-datetimepicker.min.css">

<#assign _page_id="add-document">
<#assign _page_parent_name="게시판 관리">
<#assign _page_current_name="게시물 등록">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}"
        canMoveParent=true/>

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    게시물을 생성 합니다. 게시물은 카테고리 별로 매핑 됩니다. 카테고리가 공개이면 사용자가 로그인 하지 않아도 게시물을 읽을 수 있습니다.
</p>
<p>
    관리자가 공개글을 작성 할때의 게시물의 암호는 empty string으로 설정 됩니다. 공개글의 암호가 empty string 일때는 일반 사용자는 글을 수정하거나 삭제 할 수 없습니다.
    때문에, 일반 사용자는 공개글의 암호를 empty string 으로 설정 할 수 없습니다.
</p>
<p>
    게시물의 사용자 태그는 공개글(로그인 되지 않고 작성 가능한 게시물) 일때는 지원 하지 않습니다.
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
                                    <@am.bstrap_disabled_txt value="${appEntity.app_name}" title="앱" />
                                    <@am.bstrap_disabled_txt value="${documentBoardEntity.board_name}" title="게시판" />
                                    <@am.bstrap_disabled_txt value="${documentCategoryEntity.category_name}" title="카테고리" />
                                    <@am.bstrap_in_txt name="outer_link" value="" title="외부 링크"
                                            maxlength="256" placeholder="최대 256자 까지 가능 합니다." />
                                    <@am.bstrap_in_select name="secret" title="비밀글 여부">
                                        <option value="${mdv_none}">비밀글 아님</option>
                                        <option value="${secret_value}">비밀글</option>
                                    </@am.bstrap_in_select>
                                    <@am.bstrap_in_select name="block" title="차단 여부">
                                        <option value="${mdv_no}">차단하지 않음</option>
                                        <option value="${mdv_yes}">차단</option>
                                    </@am.bstrap_in_select>
                                    <@am.bstrap_in_select name="allow_comment" title="댓글 허용 여부">
                                        <option value="${mdv_yes}">허용</option>
                                        <option value="${mdv_no}">허용 않음</option>
                                    </@am.bstrap_in_select>
                                    <@am.bstrap_in_select name="allow_notice" title="공지 여부">
                                        <option value="${mdv_no}">일반글</option>
                                        <option value="${mdv_yes}">공지글</option>
                                    </@am.bstrap_in_select>

                                    <#--
                                        카테고리가 공개이면 게시물에 임의의 게시자 이름과 게시물 패스워드 입력이 가능.
                                        관리자는 로그인 해서 작성 하기 때문에 관리자 이름을 그대로 사용하고 패스워드는 empty string 으로 설정 된다.
                                    -->
                                    <#--
                                    <#if "${documentCategoryEntity.open_type}" == "${mdv_public}">
                                        <@am.bstrap_in_txt name="user_name" value="" title="게시자 이름"
                                            maxlength="64" placeholder="최대 64자 까지 가능 합니다." />
                                        <@am.bstrap_in_txt name="document_password" value="" title="게시물 암호"
                                            maxlength="128" placeholder="최대 128자 까지 가능 합니다." />
                                    </#if>
                                    -->
                                </div>
                                <hr>
                                <div class="row">
                                    <@am.bstrap_select2 id="admin_tag_srls" title="관리자 태그" widthClass="col-sm-12" />
                                    <#-- 공개가 아닐때만 사용자 태그 추가가 가능 하다 -->
                                    <#if "${documentCategoryEntity.open_type}" != "${mdv_public}">
                                        <@am.bstrap_select2 id="user_tag_srls" title="사용자 태그" widthClass="col-sm-12" />
                                    </#if>
                                </div>
                                <hr>
                                <div class="row">
                                    <@am.bstrap_in_select name="template_srl" title="게시물 템플릿 선택">
                                        <option value="${mdv_none}">사용하지 않음</option>
                                        <#list templateEntities as templateEntity>
                                            <option value="${templateEntity.template_srl}">${templateEntity.template_title}</option>
                                        </#list>
                                    </@am.bstrap_in_select>
                                </div>
                                <#-- 선택한 템플릿 형태가 들어 간다 -->
                                <div class="row" id="template-form"></div>

                                <#-- 첨부 파일을 등록하면 첨부 파일이 들어 간다 -->
                                <div id="attach-file-area" style="display: none;">
                                    <hr>
                                    <div class="row">
                                        <div style="padding-left:14px;padding-right:14px;">
                                            <ul class="inbox-download-list" id="attach-file-list"></ul>
                                        </div>
                                    </div>
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
                                <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                    <i class="fa fa-check"></i> 게시물 등록
                                </button>
                            </div>
                            <div class="btn-group pull-left">
                                <button class="btn btn-sm btn-info btn-prev-page" type="button">
                                    <i class="fa fa-chevron-left"></i> 게시판 관리
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

        drawBreadCrumb(['게시물 추가']);

        <#-- 저장된 템플릿 데이터를 변수화 한다 -->
        var templateData = {
            <#list templateEntities as templateEntity>
                ${templateEntity.template_srl} : _.isObject(${templateEntity.template_content}) ?
                    ${templateEntity.template_content} : eval('(' + ${templateEntity.template_content} + ')'),
            </#list>
        };

        <#-- 게시판 관리로 이동 -->
        $('.btn-prev-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/board/manage');
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

        <#-- 관리자용 태그 선택 -->
        <@am.jsselect2 id="admin_tag_srls" placeholder="관리자용 태그를 입력 하세요." uniq="tag_srl"
            showColumn="tag_name" uri="${Request.contextPath}/admin/board/list/app/${appEntity.app_srl}/tag/${mdv_yes}/select2/t/"
            miniumLength=2 usingTag=true createSearchChoice="createSearchChoice" />

        <#-- 사용자용 태그 선택 -->
        <@am.jsselect2 id="user_tag_srls" placeholder="사용자용 태그를 입력 하세요." uniq="tag_srl"
            showColumn="tag_name" uri="${Request.contextPath}/admin/board/list/app/${appEntity.app_srl}/tag/${mdv_no}/select2/t/"
            miniumLength=2 usingTag=true createSearchChoice="createSearchChoice" />

        <#-- 템플릿 선택을 변경 하면 맞는 템플릿 폼을 보여 준다 -->
        var $templateInputForm = $('#template-form');
        $('select[name=template_srl]').change(function(e){
            var $this = $(this), value = $this.val();
            my.fn.showTemplateForm($templateInputForm, templateData[value]);
        });

        <#-- 파일 업로드 ajax form 을 사용해서 파일을 업로드 하고 게시물에 첨부 이미지를 붙인다. -->
        var $attachFileContainer = $('#attach-file-list');
        <@am.bstrap_file_uploader_ajax fileContainer="$attachFileContainer" />

        <#--
            첨부 파일 삭제를 눌렀을때 첨부 파일 이미지 뷰를 삭제 한다. 게시물과 매핑 전이므로 뷰만 삭제하면 됨.
            게시물 작성시와 게시물 수정시가 다르게 동작해야 하기 때문애 bstrap_file_uploader_ajax 에 합치지 않았음.
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

            <#-- 템플릿을 사용하면 템플릿 데이터를 만든다 -->
            var templateSrl = $('select[name=template_srl]').val();
            var templateExtra = '';
            if(templateSrl > 0) {
                var templateObj = {};
                my.fn.getTemplateUsingData(templateData[templateSrl], templateObj);
                templateExtra = JSON.stringify(templateObj);
            }

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
                outer_link:         encodeURIComponent($('input[name=outer_link]').val()),
                secret:             $('select[name=secret]').val(),
                block:              $('select[name=block]').val(),
                allow_comment:      $('select[name=allow_comment]').val(),
                allow_notice:       $('select[name=allow_notice]').val(),
                list_order:         ${mdv_none},
                template_srl:       templateSrl,
                template_extra:     templateExtra,
                <#-- 태그 -->
                <#-- 공개가 아닐때만 태그 추가가 가능 하다 -->
                <#if "${documentCategoryEntity.open_type}" != "${mdv_public}">
                    admin_tags:         $('#admin_tag_srls').select2('data'),
                    user_tags:          $('#user_tag_srls').select2('data'),
                <#else>
                    admin_tags:         [],
                    user_tags:          [],
                </#if>
                <#-- 첨부 파일 -->
                attach_file_srls:   attachFileSrls
            };

            <#-- 게시물 등록 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/add/t/"
                    moveuri="#${Request.contextPath}/admin/board/manage" errortitle="게시물 등록 실패" ; job >
                <#if job == "success_job">
                    <#-- 게시믈 등록 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = '게시물을 등록 하였습니다.';
                    <@am.jsnoti title="게시물 등록 성공" content="toastMsg" boxtype="success" />
                    <#-- 게시물 등록 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                    my.fn.removeDatatableStorage('manage-board-document-list');
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