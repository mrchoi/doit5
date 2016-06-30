<#assign _page_id="mycomplete-comment_add">
<#assign _page_parent_name="완료내역관리">
<#assign _page_current_name="결과보고서 파일등록">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-4" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-edit">결과보고서 등록</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <#if plymindDocumentEntities?size != 0 >
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="well">
                                        <div class="row">
                                        <#list plymindDocumentEntities as plymindDocumentEntity>
                                            <input class="form-control" name="result_file" value="${plymindDocumentEntity.result_file_name}" readonly>
                                            <br>
                                            <div class="btn-group">
                                                <button id="result_file_download" class="btn btn-sm btn-default" type="button" onclick="window.open('${plymindDocumentEntity.result_file_url}')">
                                                    <i class="fa fa-download"></i> 결과보고서 다운로드
                                                </button>
                                            </div>
                                        </#list>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </#if>


                        <fieldset>
                            <div id="result_file_form">
                                <form id="fileupload-form1">
                                    <input id="image-attach1" type="file" name="attach_file" style="display: none;">
                                    <input id="image-type1" type="hidden" name="file_type" value="1">
                                </form>
                                <form id="checkout-form">
                                    <fieldset>
                                        <legend>등록할 결과보고서 파일 정보를 입력 합니다.</legend>

                                        <div class="row" id="repository-file">
                                            <input type="hidden" name="file_srl1" value="${mdv_none}" id="file_srl1"/>
                                            <input type="hidden" id="backup-url1" value="">
                                        <@am.bstrap_in_txt_rbtn name="file_url1" value="" title="파일 선택"
                                        maxlength="128" placeholder="업로드할 파일을 선택 하세요" btn="업로드" />

                                        <@am.bstrap_in_txtarea name="file_comment" value="" title="파일 설명"
                                        rows=1 placeholder="파일 설명을 입력 하세요" widthClass="col-sm-12" />
                                        </div>
                                    </fieldset>
                                </form>
                            </div>
                        </fieldset>

                        <div class="form-actions">
                            <div class="btn-group pull-left">
                                <button class="btn btn-sm btn-info btn-prev-page" type="button">
                                    <i class="fa fa-chevron-left"></i> 목록보기
                                </button>
                            </div>
                            <div class="btn-group">
                                <button id="btn_result_file_add" class="btn btn-sm btn-success" type="button">
                                    <i class="fa fa-check"></i> 결과보고서 등록
                                </button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </article>

       <#-- <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="btn-group pull-left">
                <button id="btn-progress-detail" class="btn btn-sm btn-info" type="button">
                    <i class="fa fa-chevron-left"></i> 이전
                </button>
            </div>
        </article>-->
    </div>
</section>

<@am.bstrap_popup_tag_modal/>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

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

        <#-- 완료 리스트 페이지로 이동 -->
        $('.btn-prev-page').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/mycomplete/list');
        });

        <#if plymindDocumentEntities?size != 0 >
            <#list plymindDocumentEntities as plymindDocumentEntity>
                <#if plymindDocumentEntity?exists && plymindDocumentEntity.result_file_srl gt 0 > <#--결과지가 존재 하는 경우 -->
                    $('#result_file_form').hide();
                    $('#btn_result_file_add').hide();
                <#else> <#--결과지가 존재 하지 않는 경우 -->
                    $('#result_file_form').show();
                    $('#btn_result_file_add').show();
                </#if>
            </#list>
        </#if>


        $('#btn_result_file_add').click(function(e) {
            var file_srl1 = 0;
            if($('#file_srl1').attr('data-file-srl') != '') {
                file_srl1 = $('#file_srl1').attr('data-file-srl');
            }

            if(file_srl1 == '') {
                var toastMsg = '결과보고서 파일을 등록 해 주세요.';
            <@am.jsnoti title="결과보고서 파일 등록 오류" content="toastMsg" boxtype="warn" />
                return;
            }

            var reqData = {
                "member_srl"   : ${member_srl},
                "application_srl"   : ${application_srl},
                "application_group" : ${application_group},
                "file_srl1"         : $('input[name=file_srl1]').val(),
                "file_url1"         : $('input[name=file_url1]').val(),
                "file_comment"      : $('textarea[name=file_comment]').val(),
                "file_type"         : 3 //1.검사지, 2.답변지, 3.결과지
            };

            if($.trim($('#backup-url1').val()) != $.trim(reqData.file_url1)) reqData.file_srl1 = ${mdv_none};

        <#-- 결과보고서 등록 요청 -->
        <@am.jsajax_request submituri="${Request.contextPath}/admin/mycomplete/complete_file_add/add/t/" method="PUT"
        moveuri="#${Request.contextPath}/admin/mycomplete/list" errortitle="등록 실패" ; job >
            <#if job == "success_job">
            <#-- 결과보고서 등록 성공하면 toast 메시지를 보여 준다 -->
                var toastMsg = '결과보고서 등록 하였습니다.';
                <@am.jsnoti title="결과보고서 등록 성공" content="toastMsg" boxtype="success" />

            <#elseif job == "common_job">
            <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                //$btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 심리검사 수정');
            </#if>
        </@am.jsajax_request>

        });
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