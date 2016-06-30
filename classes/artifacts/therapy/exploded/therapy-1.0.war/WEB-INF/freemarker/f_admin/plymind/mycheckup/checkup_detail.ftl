<#assign _page_id="myadvice-progress-detail">
<#assign _page_parent_name="심리검사관리">
<#assign _page_current_name="심리검사 상세">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">진행중인 심리 검사 정보</@am.widget_title>
            <#-- widget div-->
                <div>
                <#-- widget content -->
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well">
                                    <div class="row">
                                        <div class="col-md-12">
                                        <@am.bstrap_disabled_txt value="${applicationEntity.user_name}" title="신청자" />
                                        <@am.bstrap_disabled_txt value="${applicationEntity.title?html}" title="심리검사 항목명" />
                                        <#assign status = '${applicationEntity.application_status}'>
                                        <#if applicationEntity.application_status == 0>
                                            <#assign status = '접수'>
                                        <#elseif applicationEntity.application_status == 1>
                                            <#assign status = '준비중'>
                                        <#elseif applicationEntity.application_status == 2>
                                            <#assign status = '진행중'>
                                        <#elseif applicationEntity.application_status == 3>
                                            <#assign status = '완료'>
                                        <#elseif applicationEntity.application_status == 4>
                                            <#assign status = '취소'>
                                        <#else> -
                                        </#if>
                                        <@am.bstrap_disabled_txt value="${status?html}" title="심리검사 진행상태" />
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                        <br>

                        <#if plymindDocumentEntities?size == 0><#-- 등록된 심리 검사지(tbl_plymind_document, tbl_file_repository)가 있는 경우 -->
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="well">
                                        <div class="row">
                                            <div class="col-md-12 text-center">
                                                <p>작성된 상담사 코멘트가 없습니다.</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="pull-right">
                                        <button id="btn_comment_add" class="btn btn-sm btn-default" type="button">
                                            <i class="fa fa-check"></i> 상담사 코멘트 등록
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <br>
                        <#else>
                            <#assign cnt = 0>
                            <#list plymindDocumentEntities as plymindDocumentEntity>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="well">
                                            <div class="row">
                                                <div class="col-sm-12 inline-group">
                                                    <div class="form-group">
                                                        <label>검사지 파일</label>
                                                        <input class="form-control" id="checkup_file" name="checkup_file" data-file-srl="${plymindDocumentEntity.checkup_file_srl}" value="${plymindDocumentEntity.checkup_file_name}" readonly>
                                                    </div>

                                                    <div class="btn-group">
                                                        <#--<button id="btn_file_search" class="btn btn-sm btn-default" type="button">심리 검사지 파일 검색</button>-->
                                                        <button id="btn_checkup_download" class="btn btn-sm btn-default" type="button" onclick="window.open('${plymindDocumentEntity.checkup_file_url}')">
                                                            <i class="fa fa-download"></i> 검사지 다운로드
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                            <div class="row">
                                                <div class="col-sm-12 inline-group">
                                                    <div class="form-group">
                                                        <label>답변지 파일</label>
                                                        <input class="form-control" id="reply_file" name="reply_file" data-file-srl="${plymindDocumentEntity.reply_file_srl}" value="${plymindDocumentEntity.reply_file_name}" readonly>
                                                    </div>
                                                    <#if plymindDocumentEntity.reply_file_srl gt 0 > <#--답변지가 존재 하는 경우 -->
                                                        <div class="btn-group">
                                                            <button id="reply_file_download" class="btn btn-sm btn-default" type="button" onclick="window.open('${plymindDocumentEntity.reply_file_url}')">
                                                                <i class="fa fa-download"></i> 답변지 다운로드
                                                            </button>
                                                        </div>
                                                    <#else> <#--답변지가 존재 하지 않는 경우 -->
                                                        작성 된 답변지가  존재 하지 않습니다.
                                                    </#if>
                                                </div>
                                            </div>
                                            <br>
                                            <#if plymindDocumentEntity.reply_file_srl gt 0 ><#--답변지가 존재 하는 경우 : 결과지 등록 폼 보여줌-->
                                            <div class="row">
                                                <div class="col-sm-12 inline-group">
                                                    <div class="form-group">
                                                        <label>결과보고서 파일</label>
                                                        <input class="form-control" id="result_file" name="result_file" data-file-srl="${plymindDocumentEntity.result_file_srl}" value="${plymindDocumentEntity.result_file_name}" readonly>
                                                    </div>
                                                    <#if plymindDocumentEntity.result_file_srl gt 0 > <#--결과지가 존재 하는 경우 -->
                                                    <#-- <button id="btn_result_file_modify" class="btn btn-sm btn-default" type="button">
                                                         <i class="fa fa-download"></i> 파일 변경
                                                     </button>
                                                     ( 버튼을 클릭 하시면 결과보고서를 변경을 하실 수 있습니다. )-->
                                                        <div class="btn-group">
                                                            <button id="result_file_download" class="btn btn-sm btn-default" type="button" onclick="window.open('${plymindDocumentEntity.result_file_url}')">
                                                                <i class="fa fa-download"></i> 결과보고서 다운로드
                                                            </button>
                                                        </div>
                                                    <#else> <#--결과지가 존재 하지 않는 경우 -->
                                                       <#-- 결과보고서가 존재하지 않습니다.-->
                                                    </#if>

                                                    <div id="result_file_form">
                                                        <form id="fileupload-form1">
                                                            <input id="image-attach1" type="file" name="attach_file" style="display: none;">
                                                            <input id="image-type1" type="hidden" name="file_type" value="1">
                                                        </form>
                                                        <form id="checkout-form">
                                                            <fieldset>
                                                                <legend>등록할 결과보고서 파일 정보를 입력 합니다.</legend>

                                                                <div class="row" id="repository-file">
                                                                    <input type="hidden" name="file_srl1" value="${mdv_none}" />
                                                                    <input type="hidden" id="backup-url1" value="">
                                                                    <@am.bstrap_in_txt_rbtn name="file_url1" value="" title="파일 선택"
                                                                    maxlength="128" placeholder="업로드할 파일을 선택 하세요" btn="업로드" />

                                                                    <@am.bstrap_in_txtarea name="file_comment" value="" title="파일 설명"
                                                                    rows=1 placeholder="파일 설명을 입력 하세요" widthClass="col-sm-12" />
                                                                </div>
                                                            </fieldset>
                                                        </form>
                                                        <div class="pull-right">
                                                            <button id="btn_modify" class="btn btn-sm btn-default" type="button">
                                                                <i class="fa fa-check"></i> 결과보고서 등록
                                                            </button>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>
                                            <#else>
                                            답변지 확인 후 결과보고서를 등록 하실 수 있습니다.
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                                <#assign cnt = cnt+1?int>
                                <#if cnt == 1>
                                    <#break>
                                </#if>
                            </#list>
                        </#if>
                    </div>
                <#-- end widget content -->
                </div>
            </div>
        </article>
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="btn-group pull-left">
               <#-- <button id="btn_progress_list" class="btn btn-sm btn-info" type="button">
                    <i class="fa fa-chevron-left"></i> 목록
                </button>-->
                <button class="btn btn-sm btn-info btn-prev-page" type="button">
                    <i class="fa fa-chevron-left"></i> 목록보기
                </button>
            </div>
        </article>

    </div>
</section>

<@am.bstrap_popup_tag_modal/>


<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
    <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };

        <#-- 심리검사지 검색 이동 -->
        <@am.jsselect2 id="file_list" placeholder="파일명을 입력하세요" uniq="file_srl"
        showColumn="orig_name" uri="${Request.contextPath}/admin/mycheckup/select2/t/"
        miniumLength=5 />

        $('#btn_file_search').click(function(e){
        <@am.jsevent_prevent />
            $('#target-tag-form').each(function(){ this.reset(); });
            $('#target-tag-remote-modal').modal('show');
        });

        <#-- 심리 검사 관리로 이동 -->
        $('.btn-prev-page').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/mycheckup/list');
        });

        <#-- 상담사 코멘트 등록 페이지로 이동 -->
        $('#btn_comment_add').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/mycheckup/advisor_comment_add/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
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

        <#assign cnt1 = 0>
        <#if plymindDocumentEntities?size != 0 >

            <#list plymindDocumentEntities as plymindDocumentEntity>
                <#assign document_srl = plymindDocumentEntity.document_srl >

                <#if plymindDocumentEntity?exists && plymindDocumentEntity.result_file_srl gt 0 > <#--결과지가 존재 하는 경우 -->
                    $('#result_file_form').hide();
                <#else> <#--결과지가 존재 하지 않는 경우 -->
                    $('#result_file_form').show();
                </#if>
                <#assign cnt1 = cnt1+1?int>
                <#if cnt1 == 1>
                    <#break>
                </#if>

            </#list>

            $('#btn_modify').click(function(e) {
                var result_file_srl = '${mdv_none}';
                if($('#result_file').attr('data-file-srl') != '') {
                    result_file_srl = $('#result_file').attr('data-file-srl');
                }

                if(result_file_srl == '${mdv_none}') {
                    var toastMsg = '심리 검사 결과 보고서 파일을 등록 해 주세요.';
                <@am.jsnoti title="심리 검사 결과 보고서 등록 오류" content="toastMsg" boxtype="warn" />
                    return;
                }

                var checkup_file_srl = $('#checkup_file').attr('data-file-srl');
                var reply_file_srl = $('#reply_file').attr('data-file-srl');

                var reqData = {
                    "application_srl"   : ${applicationEntity.application_srl},
                    "application_group" : ${applicationEntity.application_group},
                    "advisor_comment"   : '',
                    "checkup_file_srl"  : checkup_file_srl,
                    "reply_file_srl"    : reply_file_srl,
                    "result_file_srl"   : result_file_srl,
                    "file_srl1"         : $('input[name=file_srl1]').val(),
                    "file_url1"         : $('input[name=file_url1]').val(),
                    "file_comment"      : $('textarea[name=file_comment]').val(),
                    "file_type"         : 3 //1.검사지, 2.답변지, 3.결과지
                };

                if($.trim($('#backup-url1').val()) != $.trim(reqData.file_url1)) reqData.file_srl1 = ${mdv_none};

            <#-- 게시물 수정 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/mycheckup/${document_srl}/t/" method="PUT"
            moveuri="#${Request.contextPath}/admin/mycheckup/list" errortitle="수정 실패" ; job >
                <#if job == "success_job">
                <#-- 심리검사 수정 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = '게시물을 수정 하였습니다.';
                    <@am.jsnoti title="심리검사 수정 성공" content="toastMsg" boxtype="success" />

                <#elseif job == "common_job">
                <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    //$btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 심리검사 수정');
                </#if>
            </@am.jsajax_request>

            });
        </#if>

    };

    loadScript("${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js", function() {
        loadScript("${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js", function () {
            loadScript("${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js", function () {
                loadScript("${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js", function () {
                    loadScript("${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js", function () {
                        loadScript("${Request.resPath}js/plugin/jquery-fileupload/jquery.fileupload.js", function () {
                            loadScript("${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js", pagefunction)
                        });
                    });
                });
            });
        });
    });
</script>