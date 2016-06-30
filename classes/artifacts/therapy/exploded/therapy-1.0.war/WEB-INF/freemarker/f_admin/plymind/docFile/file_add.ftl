<#assign _page_id="file-add">
<#assign _page_parent_name="검사 문서 관리">
<#assign _page_current_name="파일 등록">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    파일 Repository 용도로 사용될 임의 파일을 서버에 업로드 합니다. Push 용 이미지 파일은 Push 메시지 발송 화면에서 업로드 가능 합니다. 파일 업로드는 다음 두 가지 기능을 제공 합니다.
</p>
<p>
    <#--<dl>
        <dt>1. 원격 파일</dt>
        <dd>
            실제 파일을 업로드 하지 않고 파일의 원격 URL 을 알고 있는 경우, 파일 선택에서 <strong>업로드</strong> 버튼을 클릭 하지 않고 원격 URL을 붙여 넣습니다.
            파일 정보를 모두 입력 후, <strong>등록</strong> 버튼을 클릭 하면 원격 파일을 서버에서 다운로드 하여 자동으로 파일 생성 됩니다.
        </dd>
    </dl>-->
</p>
<p>
    <dl>
        <dt>2. 내부 파일</dt>
        <dd>
            업로드 하려는 파일을 가지고 있을때 <strong>업로드</strong> 버튼을 클릭하여 소유하고 있는 파일을 선택 합니다. 파일 선택과 동시에 자동으로 파일 업로드가
            진행 되며, 파일 정보를 모두 입력 후 <strong>등록</strong> 버튼을 클릭하면 최종 파일 등록이 완료 됩니다.
        </dd>
    </dl>
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
                <@am.widget_title icon="fa-edit">${_page_current_name}</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <form id="fileupload-form1">
                            <input id="image-attach1" type="file" name="attach_file" style="display: none;">
                            <input id="image-type1" type="hidden" name="file_type" value="1">
                        </form>
                        <form id="checkout-form">
                            <fieldset>
                                <legend>등록할 파일 정보를 입력 합니다.</legend>

                                <div class="row" id="repository-file">

                                <@am.bstrap_in_select name="file_type" title="파일 구분">
                                    <option value="1">심리검사지</option>
                                    <#--<option value="2">심리검사_답변지</option>
                                    <option value="3">심리검사_결과지</option>-->
                                    <option value="4">사전검사지</option>
                                    <option value="5">사후검사지</option>
                                    <option value="6">기타</option>
                                </@am.bstrap_in_select>

                                    <input type="hidden" name="file_srl1" value="${mdv_none}" />
                                    <input type="hidden" id="backup-url1" value="">
                                    <@am.bstrap_in_txt_rbtn name="file_url1" value="" title="파일 선택"
                                        maxlength="128" placeholder="업로드할 파일을 선택 하세요" btn="업로드" />

                                    <@am.bstrap_in_txtarea name="file_comment" value="" title="파일 설명"
                                        rows=6 placeholder="파일 설명을 입력 하세요" widthClass="col-sm-12" />
                                </div>
                            </fieldset>

                            <#--<div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 등록
                                    </button>
                                </div>
                            </div>-->
                        </form>

                    <#-- Action 버튼 -->
                        <div class="widget-footer smart-form">
                            <div class="btn-group">
                                <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                    <i class="fa fa-check"></i> 등록
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

    <#-- 게시판 관리로 이동 -->
        $('.btn-prev-page').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/docFile/list');
        });

        <#-- 업로드 버튼을 눌렀을 때 파일 선택 윈도우를 출력 하고 파일을 업로드 시킨다.(파일 저장소) -->
        <@am.bstrap_in_txt_rbtn_uploader id="image-attach1"
                uri="${Request.contextPath}/admin/resource/repository/upload/file/t/"
                showid="file_url1" isImage=false>
            $('input[name=file_srl1]').val(result.file_srl);
            $('#backup-url1').val(result.domain + result.uri);
        </@am.bstrap_in_txt_rbtn_uploader>

        $('.bstrap-in-txt-rbtn').click(function(e){
            <@am.jsevent_prevent />
            $('#image-attach1').trigger('click');
        });

        $("#checkout-form").validate({
            rules: {
                file_url1:  {required: true, maxlength: 256},
                file_comment:  {required: true, maxlength: 256}
            },
            messages : {
                file_url1: {
                    required: '등록할 파일 URL을 입력 하세요.',
                    maxlength: '파일 URL은 {0} 자 이하 입니다.'
                },
                file_comment: {
                    required: '등록할 파일 설명을 입력 하세요.',
                    maxlength: '파일 설명은 {0} 자 이하 입니다.'
                }
            },
            <@am.jsbstrap_validator_comm />
        });

        <#-- 파일을 등록 한다 -->
        $('#btn-add').click(function(e){
            <#-- event bubbling prevent -->
            <@am.jsevent_prevent />

            <#-- jquery validator -->
            var check = $('#checkout-form').valid();
            if(!check) { return; }

            <#-- ajax 요청 하기 전에 사전 작업 -->
            $('.invalid').hide();
            var $btnAdd = $(this);
            $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 등록중');

            <#-- request 할 데이터를 만든다.(reqData 라는 이름을 왠만하면 바꾸지 말자. 이름을 그대로 사용해야 macro 사용이 편해짐) -->
            var reqData = {
                file_kind:              $('select[name=file_kind]').val(),
                file_srl1:              $('input[name=file_srl1]').val(),
                file_url1:              $('input[name=file_url1]').val(),
                file_comment:           $('textarea[name=file_comment]').val(),
                file_type:              $('select[name=file_type]').val()
            };

            if($.trim($('#backup-url1').val()) != $.trim(reqData.file_url1)) reqData.file_srl1 = ${mdv_none};

            <#-- 이미지 등록 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/resource/repository/add/file/t/"
                    moveuri="#${Request.contextPath}/admin/docFile/list" errortitle="임의 파일 등록 실패" ; job >
                <#if job == "success_job">
                    <#-- 파일 등록 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = '임의 파일 등록을 완료 하였습니다.';
                    <@am.jsnoti title="임의 파일 등록 성공" content="toastMsg" boxtype="success" />
                    <#-- 앱 추가 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                    my.fn.removeDatatableStorage('list-repository-file');
                <#elseif job == "common_job">
                    <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 등록');
                </#if>
            </@am.jsajax_request>
        });
    };

    loadScript("${Request.resPath}js/plugin/jquery-fileupload/jquery.fileupload.js", pagefunction);
</script>
