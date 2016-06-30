<#assign _page_id="myadvice-advisor_comment">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담 상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-4" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">상담사 코멘트</@am.widget_title>
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
                                                        <div class="col-md-12">
                                                            <p>${(plymindDocumentEntity.c_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}</p>
                                                        </div>
                                                    </div>
                                                    <br>
                                                    <div class="row">
                                                        <div class="col-md-12">
                                                            <p>
                                                                ${plymindDocumentEntity.advisor_comment}
                                                            </p>
                                                        </div>
                                                    </div>

                                                    <hr>

                                                    <#if plymindDocumentEntity.checkup_file_srl?int &gt; 0>
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <p>STEP1. 하단 버튼을 클릭하여 검사지를 다운 받아 주세요.</p>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <button id="btn_checkup_download" class="btn btn-sm btn-default" type="button" onclick="window.open('${plymindDocumentEntity.checkup_file_url}')">
                                                                    <i class="fa fa-download"></i> 검사지 다운로드
                                                                    <input class="hidden" id="checkup_file" name="checkup_file" data-file-srl="${plymindDocumentEntity.checkup_file_srl}" value="${plymindDocumentEntity.checkup_file_name}" readonly>
                                                                </button>
                                                            </div>
                                                        </div>
                                                        <br>
                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <p>STEP2. 하단 버튼을 클릭하여 작성 완료된 검사지를 다운 받아 주세요.</p>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="col-md-12">
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

                                                                <div id="result_file_form-${plymindDocumentEntity_index}" style="margin-left:10px;margin-top:20px;">
                                                                    <form id="fileupload-form1">
                                                                        <input id="image-attach-${plymindDocumentEntity_index}" type="file" name="attach_file" style="display: none;">
                                                                        <input id="image-type-${plymindDocumentEntity_index}" type="hidden" name="file_type" value="1">
                                                                    </form>
                                                                    <form id="checkout-form">
                                                                        <fieldset>
                                                                            <legend>등록할 결과보고서 파일 정보를 입력 합니다.</legend>

                                                                            <div class="row" id="repository-file">
                                                                                <input type="hidden" id="file_srl-${plymindDocumentEntity_index}" name="file_srl-${plymindDocumentEntity_index}" value="${mdv_none}" />
                                                                                <input type="hidden" id="backup-url-${plymindDocumentEntity_index}" name="backup-url-${plymindDocumentEntity_index}" value="">
                                                                                <@am.bstrap_in_txt_rbtn name="file_url-${plymindDocumentEntity_index}" value="" title="파일 선택"
                                                                                maxlength="128" placeholder="업로드할 파일을 선택 하세요" btn="업로드" />

                                                                                <@am.bstrap_in_txtarea name="file_comment-${plymindDocumentEntity_index}" value="" title="파일 설명"
                                                                                rows=1 placeholder="파일 설명을 입력 하세요" widthClass="col-sm-12" />
                                                                            </div>
                                                                        </fieldset>
                                                                    </form>
                                                                    <div class="pull-right">
                                                                        <button id="btn_modify" name="btn-modify-${plymindDocumentEntity_index}" class="btn btn-sm btn-default btn-modify" type="button">
                                                                            <i class="fa fa-check"></i> 결과보고서 등록
                                                                        </button>
                                                                    </div>
                                                                </div>

                                                            </div>
                                                        </div>
                                                    </#if>


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
                <button id="btn_progress_detail" class="btn btn-sm btn-info" type="button">
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
        $('#btn_progress_detail').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/progress_detail/${member_srl}/${application_srl}/${application_group}');
        });

        <#list plymindDocumentEntities as plymindDocumentEntity>
            <#if plymindDocumentEntity.reply_file_srl gt 0 >
                $('#result_file_form-'+${plymindDocumentEntity_index}).hide();
            <#else>
                $('#result_file_form-'+${plymindDocumentEntity_index}).hide();
            </#if>

        </#list>

        $('.bstrap-in-txt-rbtn').each(function(index) {
            $(this).click(function(e){

            <@am.jsevent_prevent />

                var id = "image-attach-"+index;
                var uri = "${Request.contextPath}/admin/resource/repository/upload/file/t/";
                var isImage = false;
                var showid = "file_url-"+index;
                var maxsize = 5000000;

                $("#"+id).fileupload({
                    url: uri + $.now(),
                    //sequentialUploads: true,
                    replaceFileInput: false,
                    dataType: 'json',
                    add: function(e, data) {
                        var $elementWrapper = $('#errid-'+showid),
                                $elementText = $('#errmsg-'+showid),
                                file = data.files[0];

                        $elementWrapper.hide();

                        if(file.size > maxsize) {

                            $elementText.text('파일 사이즈는 ()M 이하만 가능 합니다.');
                            $elementWrapper.show();
                            return;
                        }

                    <#-- 업로드 버튼을 disabled 시킨다. -->
                        $('input[name='+showid+']').next().find('button').addClass('disabled');

                        data.submit();
                    },
                    done: function(e, data) {
                        var $targetInput = $('input[name='+showid+']');
                        $targetInput.next().find('button').removeClass('disabled');

                        var result = data.result;

                        if(!result || !result.ck_error || !result.ck_message) {
                            $('#errmsg-'+showid).text('파일 업로드를 실패 했습니다.');
                            $('#errid-'+showid).show();
                            return;
                        }

                        if(result.ck_error != my.data.ajaxSuccessCode) {
                            $('#errmsg-'+showid).text(result.ck_message);
                            $('#errid-'+showid).show();
                            return;
                        }

                    <@ap.py_jsnoti title="파일 업로드" content="업로드 성공 했습니다." contenttype="val"
                    boxtype="info" />
                        var _preview = result.domain + result.uri + '?w=50&h=50';
                        $targetInput.val(result.domain + result.uri);
                        $('input[name=file_srl-'+index+']').val(result.file_srl);
                        $('#backup-url-'+index).val(result.domain + result.uri);
                    },
                    fail: function(e, data) {
                        console.warn('file upload error');
                        console.warn(data);

                        $('#errmsg-'+showid).text('파일 업로드를 실패 했습니다.');
                        $('#errid-'+showid).show();
                        $('input[name='+showid+']').next().find('button').removeClass('disabled');
                    }
                });



                $('#image-attach-'+index).trigger('click');


            });
        });

        $('.btn-modify').each(function(index) {
            $(this).click(function(e){
            <@am.jsevent_prevent />

                var checkup_file_srl = $('#checkup_file_'+index).attr('data-file-srl');
                var reply_file_srl = $('#reply_file_'+index).attr('data-file-srl');
                var result_file_srl = $('#result_file_'+index).attr('data-file-srl');
                var application_srl = $('#application_srl_'+index).attr('data-file-srl');
                var document_srl = $('#document_srl').attr('data-file-srl');

                var reqData = {
                    "application_srl"   : application_srl,
                    "application_group" : ${application_group},
                    "advisor_comment"   : '',
                    "checkup_file_srl"  : checkup_file_srl,
                    "reply_file_srl"    : reply_file_srl,
                    "result_file_srl"   : result_file_srl,
                    "file_srl1"         : $('input[name=file_srl-'+index+']').val(),
                    "file_url1"         : $('input[name=file_url-'+index+']').val(),
                    "file_comment"      : $('textarea[name=file_comment-'+index+']').val(),
                    "file_type"         : 3 //1.검사지, 2.답변지, 3.결과지
                };


                if($.trim($('#backup-url-'+index).val()) != $.trim(reqData.file_url1)) reqData.file_srl1 = ${mdv_none};
                <#list plymindDocumentEntities as plymindDocumentEntity>
                    var btn_index = ${plymindDocumentEntity_index};
                    if(btn_index == index) {
                    <#-- 게시물 수정 요청 -->
                        <@ap.py_jsajax_request submituri="${Request.contextPath}/admin/plymind/myadvice/${plymindDocumentEntity.document_srl}/t/" method="PUT"
                        moveuri="#${Request.contextPath}/admin/plymind/myadvice/progress_list" errortitle="수정 실패" ; job >
                            <#if job == "success_job">
                            <#-- 게시믈 수정 성공하면 toaset 메시지를 보여 준다 -->
                                var toastMsg = '심리상담을 수정 하였습니다.';
                                <@ap.py_jsnoti title="수정 성공" content="toastMsg" boxtype="success" contenttype="var" />
                            <#elseif job == "common_job">
                            <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                                //$btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 수정');
                            </#if>
                        </@ap.py_jsajax_request>
                    }
                </#list>
            });
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