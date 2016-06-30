<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js"></script>
<script src="${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js"></script>
<script src="${Request.resPath}js/plugin/jquery-fileupload/jquery.fileupload.js"></script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-complete-comment-detail">
<#assign _page_parent_name="나의 상담 정보">
<#assign _page_current_name="상담 완료 내역">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<div class="main-container">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <blockquote>상담사 코멘트</blockquote>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="well-box">

                    <#list plymindDocumentEntities as plymindDocumentEntity>

                        <div class="row">
                            <div class="col-md-12">
                                <div class="well">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <p>${(plymindDocumentEntity.c_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}</p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <p>${plymindDocumentEntity.advisor_comment}</p>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-md-12">
                                            STEP1. 하단 버튼을 클릭하여 검사지를 다운 받아 주세요.
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <a href="${plymindDocumentEntity.checkup_file_url}" class="btn tp-btn-default">  <i class="fa fa-download"></i> 사전 검사지 다운로드</a>
                                        </div>
                                    </div>
                                    <hr>
                                    <div class="row">
                                        <div class="col-md-12">
                                            STEP2. 작성 완료 후 하단 버튼을 클릭하여 검사지를 첨부해 주세요.
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <#if plymindDocumentEntity.reply_file_srl gt 0 > <#--답변지가 존재 하는 경우 -->
                                            <#--답변지가 최초 등록 후 파일 변경 처리 불가로 처리.
                                                추후 파일 변경 요청이 올 시 수정 예정. 현재는 최초 등록 후 답변지 다운로드만 되도록 처리 -->
                                                <#--<input class="form-control" id="reply_file_${plymindDocumentEntity_index}" name="reply_file" data-file-srl="${plymindDocumentEntity.reply_file_srl}" value="${plymindDocumentEntity.reply_file_name}" readonly>
                                                <br>

                                                <button id="btn_result_file_modify" class="btn btn-sm btn-default btn_result_file_modify" type="button">
                                                    <i class="fa fa-download"></i> 파일 변경
                                                </button>
                                                ( 버튼을 클릭 하시면 답변지를 변경을 하실 수 있습니다. )-->
                                                <a href="${plymindDocumentEntity.reply_file_url}" class="btn tp-btn-default">  <i class="fa fa-download"></i> 답변지 다운로드</a>
                                            <#else> <#--답변지가 존재 하지 않는 경우 -->
                                                업로드한 답변지가 존재하지 않습니다.
                                            </#if>
                                        <#--답변지가 최초 등록 후 파일 변경 처리 불가로 처리.
                                             추후 파일 변경 요청이 올 시 수정 예정. 현재는 최초 등록 후 답변지 다운로드만 되도록 처리 -->
                                            <input type="hidden" class="form-control" id="reply_file_${plymindDocumentEntity_index}" name="reply_file" data-file-srl="${plymindDocumentEntity.reply_file_srl}" value="${plymindDocumentEntity.reply_file_name}" readonly>

                                            <input type="hidden" class="form-control" id="document_srl_${plymindDocumentEntity_index}" name="document_srl" data-file-srl="${plymindDocumentEntity.document_srl}" >
                                            <input type="hidden" class="form-control" id="application_srl_${plymindDocumentEntity_index}" name="application_srl" data-file-srl="${plymindDocumentEntity.application_srl}" >
                                            <input type="hidden" class="form-control" id="checkup_file_${plymindDocumentEntity_index}" name="checkup_file" data-file-srl="${plymindDocumentEntity.checkup_file_srl}" value="${plymindDocumentEntity.checkup_file_name}" readonly>
                                            <input type="hidden" class="form-control" id="result_file_${plymindDocumentEntity_index}" name="result_file" data-file-srl="${plymindDocumentEntity.result_file_srl}" value="${plymindDocumentEntity.result_file_name}" readonly>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </div>
        </div>
        <div class="form-group">
            <button class="btn tp-btn-primary btn-prev-page">
                <i class="fa fa-chevron-left"></i> 이전
            </button>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {

        $('.btn-prev-page').click(function(e){
            <@ap.py_jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/mycomplete/complete_detail/${application_srl}/${application_group}');
        });

    });

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->