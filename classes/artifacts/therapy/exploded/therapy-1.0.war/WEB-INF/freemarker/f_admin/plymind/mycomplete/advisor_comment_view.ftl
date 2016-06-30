<#assign _page_id="mycomplete-advisor_comment">
<#assign _page_parent_name="완료내역관리">
<#assign _page_current_name="코멘트상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-3" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-eye">상담사 코멘트</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well-box">
                                <#list plymindDocumentEntities as plymindDocumentEntity>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="well">
                                                <div class="row">
                                                    <div class="col-md-12 text-center">
                                                        <p>${(plymindDocumentEntity.c_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}</p>
                                                    </div>
                                                </div>
                                                <br>
                                                <div class="row">
                                                    <div class="col-md-12 text-center">
                                                        <p>
                                                        ${plymindDocumentEntity.advisor_comment}
                                                        </p>
                                                    </div>
                                                </div>

                                                <#if plymindDocumentEntity.checkup_file_srl?int != 0>
                                                    <br>
                                                    <div class="row">
                                                        <div class="col-md-12 text-center">
                                                            <p>STEP1. 하단 버튼을 클릭하여 검사지를 다운 받아 주세요.</p>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-12 text-center">
                                                            <button id="btn_checkup_download" class="btn btn-sm btn-default" type="button" onclick="window.open('${plymindDocumentEntity.checkup_file_url}')">
                                                                <i class="fa fa-download"></i> 검사지 다운로드
                                                                <input class="hidden" id="checkup_file" name="checkup_file" data-file-srl="${plymindDocumentEntity.checkup_file_srl}" value="${plymindDocumentEntity.checkup_file_name}" readonly>
                                                            </button>
                                                        </div>
                                                    </div>
                                                    <br>
                                                    <div class="row">
                                                        <div class="col-md-12 text-center">
                                                            <p>STEP2. 하단 버튼을 클릭하여 작성 완료된 검사지를 다운 받아 주세요.</p>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="col-md-12 text-center">
                                                            <#if plymindDocumentEntity.reply_file_srl gt 0 > <#--답변지가 존재 하는 경우 -->
                                                                <button id="btn_reply_download" class="btn btn-sm btn-default" type="button" onclick="window.open('${plymindDocumentEntity.reply_file_url}')">
                                                                    <i class="fa fa-download"></i> 답변지 다운로드
                                                                </button>
                                                            <#else>
                                                                작성 된 답변지가 존재하지 않습니다.
                                                            </#if>

                                                            <input type="hidden" class="form-control" id="reply_file_${plymindDocumentEntity_index}" name="reply_file" data-file-srl="${plymindDocumentEntity.reply_file_srl}" value="${plymindDocumentEntity.reply_file_name}" readonly>

                                                            <input type="hidden" class="form-control" id="document_srl_${plymindDocumentEntity_index}" name="document_srl" data-file-srl="${plymindDocumentEntity.document_srl}" >
                                                            <input type="hidden" class="form-control" id="application_srl_${plymindDocumentEntity_index}" name="application_srl" data-file-srl="${plymindDocumentEntity.application_srl}" >
                                                            <input type="hidden" class="form-control" id="checkup_file_${plymindDocumentEntity_index}" name="checkup_file" data-file-srl="${plymindDocumentEntity.checkup_file_srl}" value="${plymindDocumentEntity.checkup_file_name}" readonly>
                                                            <input type="hidden" class="form-control" id="result_file_${plymindDocumentEntity_index}" name="result_file" data-file-srl="${plymindDocumentEntity.result_file_srl}" value="${plymindDocumentEntity.result_file_name}" readonly>

                                                        </div>
                                                    </div>
                                                </#if>
                                                <#--<br>
                                                <div class="row">
                                                    <div class="col-md-12 text-center">
                                                        <p>STEP3. 결과보고서를 첨부해주세요.</p>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12 text-center">
                                                        <#if plymindDocumentEntity.result_file_srl gt 0 >&lt;#&ndash;결과지가 존재 하는 경우 &ndash;&gt;
                                                        &lt;#&ndash;<button id="btn_result_file_modify" class="btn btn-sm btn-default" type="button">
                                                            <i class="fa fa-download"></i> 파일 변경
                                                        </button>
                                                        ( 버튼을 클릭 하시면 결과보고서를 변경을 하실 수 있습니다. )&ndash;&gt;
                                                            <button id="btn_result_download" class="btn btn-sm btn-default" type="button" onclick="window.open('${plymindDocumentEntity.result_file_url}')">
                                                                <i class="fa fa-download"></i> 결과보고서 다운로드
                                                            </button>
                                                        <#else> &lt;#&ndash;결과지가 존재 하지 않는 경우 &ndash;&gt;
                                                            결과보고서가 존재하지 않습니다.
                                                        </#if>
                                                    </div>
                                                </div>-->
                                            </div>
                                        </div>
                                    </div>
                                    <br>
                                </#list>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>

        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="btn-group pull-left">
                <button id="btn_complete_detail" class="btn btn-sm btn-info" type="button">
                    <i class="fa fa-chevron-left"></i> 이전
                </button>
            </div>
        </article>
    </div>
</section>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
    <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

    <#-- 심리상담 상세정보 페이지로 이동 -->
        $('#btn_complete_detail').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/mycomplete/complete_detail/${member_srl}/${application_srl}/${application_group}');
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