<link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/bootstrap-datetimepicker.min.css">

<#assign _page_id="detail-document">
<#assign _page_parent_name="게시판 관리">
<#assign _page_current_name="게시물 수정">

<@am.page_simple_depth icon="fa-puzzle-piece" parent="${_page_parent_name}" current="${_page_current_name}"
        canMoveParent=true />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    등록된 게시물을 확인하고 수정하는 페이지 입니다. 수정할 항목의 내용을 클릭하고 내용을 변경하면 곧바로 변경 됩니다. 상위 페이지인
    게시판 관리로 이동 하고 싶으면 카테고리 정보 위젯 하단의 게시판 관리 버튼을 클릭 하면 게시판 관리로 이동 됩니다.
</p>
<#if link_flag == mdv_yes>
    <p>
        링크 게시물을 통해서 게시물을 확인 할때는 게시물 수정, 삭제가 불가능 합니다. <b>게시물 수정은 원본 게시물을 통해서만 지원 됩니다.</b>
        링크 게시물 삭제는 링크 게시물 리스트에서 삭제 가능 합니다.
    </p>
</#if>
<#--
<p>
    게시물의 카테고리 변경은 동일한 게시판 내의 다른 카테고리로 변경 할 수 있습니다.
</p>
-->
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
                <@am.widget_title icon="fa-eye">${documentEntity.document_title?html}</@am.widget_title>

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

                        <#-- 나머지 게시물 정보 입력 -->
                        <form>
                            <fieldset style="padding:10px;">
                                <div class="row">
                                    <@am.bstrap_disabled_txt value="${appEntity.app_name?html}" title="앱" />
                                    <@am.bstrap_disabled_txt value="${documentBoardEntity.board_name?html}" title="게시판" />
                                    <#-- TODO 게시물 이동은 추후에 다시 하도록 하자. 링크 게시물, 카테고리 타입에 따라서 선택 되어야 한다. 일단 view만 시키자
                                    <@am.bstrap_in_select name="category_srl" title="카테고리">
                                        <#list categoryEntities as categoryEntity>
                                            <#if categoryEntity.category_srl == documentCategoryEntity.category_srl>
                                                <option value="${categoryEntity.category_srl}" selected>${categoryEntity.category_name}</option>
                                            <#else>
                                                <option value="${categoryEntity.category_srl}">${categoryEntity.category_name}</option>
                                            </#if>
                                        </#list>
                                    </@am.bstrap_in_select>
                                    -->
                                    <@am.bstrap_disabled_txt value="${documentCategoryEntity.category_name?html}" title="카테고리" />
                                    <@am.bstrap_in_txt name="outer_link" value="${documentEntity.outer_link}" title="외부 링크"
                                        maxlength="256" placeholder="최대 256자 까지 가능 합니다." />
                                    <@am.bstrap_in_select name="secret" title="비밀글 여부">
                                        <#if documentEntity.secret != mdv_none>
                                            <option value="${mdv_none}">비밀글 아님</option>
                                            <option value="${secret_value}" selected>비밀글</option>
                                        <#else>
                                            <option value="${mdv_none}" selected>비밀글 아님</option>
                                            <option value="${secret_value}">비밀글</option>
                                        </#if>
                                    </@am.bstrap_in_select>
                                    <@am.bstrap_in_select name="block" title="차단 여부">
                                        <option value="${mdv_no}" <#if documentEntity.block == mdv_no>selected</#if>>차단하지 않음</option>
                                        <option value="${mdv_yes}" <#if documentEntity.block == mdv_yes>selected</#if>>차단</option>
                                    </@am.bstrap_in_select>
                                    <@am.bstrap_in_select name="allow_comment" title="댓글 허용 여부">
                                        <option value="${mdv_yes}" <#if documentEntity.allow_comment == mdv_yes>selected</#if>>허용</option>
                                        <option value="${mdv_no}" <#if documentEntity.allow_comment == mdv_no>selected</#if>>허용 않음</option>
                                    </@am.bstrap_in_select>
                                    <@am.bstrap_in_select name="allow_notice" title="공지 여부">
                                        <option value="${mdv_no}" <#if documentEntity.allow_notice == mdv_no>selected</#if>>일반글</option>
                                        <option value="${mdv_yes}" <#if documentEntity.allow_notice == mdv_yes>selected</#if>>공지글</option>
                                    </@am.bstrap_in_select>

                                    <#--
                                        이건 나중에 하도록 하자. 공개 게시물의 게시자 이름과 암호 변경
                                    -->
                                    <#--
                                    <#if "${documentCategoryEntity.open_type}" == "${mdv_public}">
                                        <@am.bstrap_in_txt name="user_name" value="${documentEntity.user_name?html}" title="게시자 이름"
                                                maxlength="64" placeholder="최대 64자 까지 가능 합니다." />
                                        <@am.bstrap_in_txt name="document_password" value="${documentEntity.document_password?html}" title="게시물 암호"
                                                maxlength="128" placeholder="최대 128자 까지 가능 합니다." />
                                    </#if>
                                    -->

                                    <#--<@am.bstrap_disabled_txt value="${documentEntity.member_srl}" title="작성자 시리얼 넘버" />-->
                                    <@am.bstrap_disabled_txt value="${documentEntity.user_id}" title="작성자 아이디" />
                                    <@am.bstrap_disabled_txt value="${documentEntity.email_address}" title="작성자 메일 주소" />
                                    <@am.bstrap_disabled_txt value="${documentEntity.user_name}" title="작성자 이름" />
                                    <@am.bstrap_disabled_txt value="${documentEntity.nick_name}" title="작성자 별명" />

                                    <@am.bstrap_disabled_txt value="${documentEntity.read_count}" title="조회수" />
                                    <@am.bstrap_disabled_txt value="${documentEntity.like_count}" title="좋아요 수" />
                                    <@am.bstrap_disabled_txt value="${documentEntity.blame_count}" title="싫어요 수" />
                                    <@am.bstrap_disabled_txt value="${documentEntity.ipaddress}" title="게시물 등록 IP" />
                                    <@am.bstrap_disabled_txt value="${(documentEntity.c_date * 1000)?number_to_date?string('yyyy.MM.dd HH:mm:ss')}" title="생성일" />
                                    <@am.bstrap_disabled_txt value="${(documentEntity.u_date * 1000)?number_to_date?string('yyyy.MM.dd HH:mm:ss')}" title="수정일" />
                                </div>
                                <#-- 공개가 아닐때만 태그 추가가 가능 하다 -->
                                <#if "${documentCategoryEntity.open_type}" != "${mdv_public}">
                                    <hr>
                                    <div class="row">
                                        <@am.bstrap_select2 id="admin_tag_srls" title="관리자 태그" widthClass="col-sm-12" />
                                        <@am.bstrap_select2 id="user_tag_srls" title="사용자 태그" widthClass="col-sm-12" />
                                    </div>
                                </#if>
                                <hr>
                                <div class="row">
                                    <@am.bstrap_in_select name="template_srl" title="게시물 템플릿 선택">
                                        <option value="${mdv_none}">사용하지 않음</option>
                                        <#list templateEntities as templateEntity>
                                            <#if documentEntity.template_srl == templateEntity.template_srl>
                                                <option value="${templateEntity.template_srl}" selected>${templateEntity.template_title}</option>
                                            <#else>
                                                <option value="${templateEntity.template_srl}">${templateEntity.template_title}</option>
                                            </#if>
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
                            <#if link_flag == mdv_no>
                                <div class="btn-group">
                                    <button id="btn-delete" class="btn btn-sm btn-danger" type="button">
                                        <i class="fa fa-trash-o"></i> 게시물 삭제
                                    </button>
                                </div>
                                <div class="btn-group">
                                    <button id="btn-add-file" class="btn btn-sm btn-default" type="button">
                                        <i class="fa fa-save"></i> 첨부파일 추가
                                    </button>
                                </div>
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 게시물 수정
                                    </button>
                                </div>
                            <#else>
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success btn-prev-page" type="button">
                                        <i class="fa fa-check"></i> 확인
                                    </button>
                                </div>
                            </#if>
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

        <#-- js_string 덕택에 별다른 model 에서 줄때 부터 인코딩 하지 않음. -->
        <#--drawBreadCrumb([decodeURIComponent('${documentEntity.document_title}')]);-->
        drawBreadCrumb(['${documentEntity.document_title?js_string?html}']);

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

        <#-- admin tag 가 존재하면 초기값을 넣는다 -->
        var initSelectAdminTag = function(element, callback) {
            var data = [], initObj = {};
            <#list documentEntity.admin_tags as adminTag>
                initObj = {};
                <#list adminTag?keys as key>
                    initObj['${key}'] = '${adminTag[key]}';
                </#list>
                data.push(initObj);
            </#list>
            callback(data);
        };

        <#-- 관리자용 태그 선택 -->
        <@am.jsselect2 id="admin_tag_srls" placeholder="관리자용 태그를 입력 하세요." uniq="tag_srl"
                showColumn="tag_name" uri="${Request.contextPath}/admin/board/list/app/${appEntity.app_srl}/tag/${mdv_yes}/select2/t/"
                miniumLength=2 usingTag=true createSearchChoice="createSearchChoice" initSelection="initSelectAdminTag" />

        <#-- user tag 가 존재하면 초기값을 넣는다 -->
        var initSelectUserTag = function(element, callback) {
            var data = [], initObj = {};
            <#list documentEntity.user_tags as userTag>
                initObj = {};
                <#list userTag?keys as key>
                    initObj['${key}'] = '${userTag[key]}';
                </#list>
                data.push(initObj);
            </#list>
            callback(data);
        };

        <#-- 사용자용 태그 선택 -->
        <@am.jsselect2 id="user_tag_srls" placeholder="사용자용 태그를 입력 하세요." uniq="tag_srl"
                showColumn="tag_name" uri="${Request.contextPath}/admin/board/list/app/${appEntity.app_srl}/tag/${mdv_no}/select2/t/"
                miniumLength=2 usingTag=true createSearchChoice="createSearchChoice" initSelection="initSelectUserTag" />

        <#-- 템플릿 선택을 변경 하면 맞는 템플릿 폼을 보여 준다 -->
        var $templateInputForm = $('#template-form');
        $('select[name=template_srl]').change(function(e){
            var $this = $(this), value = $this.val();
            <#if documentEntity.template_srl != mdv_none>
                my.fn.showTemplateForm($templateInputForm, templateData[value], value,
                        ${documentEntity.template_srl},
                        _.isObject(${documentEntity.template_extra}) ? ${documentEntity.template_extra} :
                        eval('(' + ${documentEntity.template_extra} + ')'));
            <#else>
                my.fn.showTemplateForm($templateInputForm, templateData[value]);
            </#if>
        });

        <#-- 만일 저장된 게시물이 템플릿을 사용하면 저장된 값을 보여 준다 -->
        <#if documentEntity.template_srl != mdv_none>
            my.fn.showTemplateForm($templateInputForm, templateData[${documentEntity.template_srl}],
                    ${documentEntity.template_srl},
                    ${documentEntity.template_srl},
                    _.isObject(${documentEntity.template_extra}) ? ${documentEntity.template_extra} :
                    eval('(' + ${documentEntity.template_extra} + ')'));
        </#if>

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
            my.fn.addDocumentAttachToDocument($attachFileContainer,
                    {
                        file_srl: ${item.file_srl},
                        file_url: '${image_server_host}${Request.contextPath}${document_attach_uri}${item.file_srl}',
                        orig_name: '${item.orig_name}',
                        file_size: ${item.file_size},
                        width: 120,
                        height: 150
                    },
                    <#if item.mime_type?index_of("image") gte 0>true<#else>false</#if>,
                    true);
        </#list>

        <#if link_flag == mdv_no>
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
                            <@am.jsnoti title="게시물 삭제 완료" content="successText" boxtype="success" />
                            <#-- 헐...이거 click 반응으로 사용자 반응 이후 호출 되는 곳 말고, 자동으로 시나리오 타고 오는 경우
                                 my.fn.pmove 를 바로 호출 하니 message box 에 문제 생긴다. 약간의 딜레이를 준다 -->
                            setTimeout(function(){
                                my.fn.pmove('#${Request.contextPath}/admin/board/manage');
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
                    <@am.jsnoti title="게시물 수정 실패" content="게시물 제목을 입력 하세요." contenttype="text" />
                    return;
                }

                <#-- ajax 요청 하기 전에 사전 작업 -->
                $('.invalid').hide();
                var $btnAdd = $(this);
                $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 수정중');

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
                    category_srl:       ${documentEntity.category_srl},
                    document_title:     encodeURIComponent(docTitle),
                    document_content:   $('#summernote-content').code(),
                    read_count:         ${documentEntity.read_count},
                    like_count:         ${documentEntity.like_count},
                    blame_count:        ${documentEntity.blame_count},
                    comment_count:      ${documentEntity.comment_count},
                    file_count:         ${documentEntity.file_count},
                    outer_link:         encodeURIComponent($('input[name=outer_link]').val()),
                    secret:             $('select[name=secret]').val(),
                    block:              $('select[name=block]').val(),
                    allow_comment:      $('select[name=allow_comment]').val(),
                    allow_notice:       $('select[name=allow_notice]').val(),
                    list_order:         ${documentEntity.list_order},
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

                <#-- 게시물 수정 요청 -->
                <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/${documentEntity.document_srl}/t/" method="PUT"
                        moveuri="#${Request.contextPath}/admin/board/manage" errortitle="게시물 수정 실패" ; job >
                    <#if job == "success_job">
                        <#-- 게시믈 수정 성공하면 toast 메시지를 보여 준다 -->
                        var toastMsg = '게시물을 수정 하였습니다.';
                        <@am.jsnoti title="게시물 수정 성공" content="toastMsg" boxtype="success" />

                    <#elseif job == "common_job">
                        <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                        $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 수정');
                    </#if>
                </@am.jsajax_request>
            });
        </#if>
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