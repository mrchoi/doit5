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
<#assign _page_id="user-mind_diary">
<#assign _page_parent_name="마이페이지">
<#assign _page_current_name="현재 진행중인 상담">
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

                                    <#if plymindDocumentEntity.checkup_file_srl?int &gt; 0>
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

                                            </#if>
                                        <#--답변지가 최초 등록 후 파일 변경 처리 불가로 처리.
                                             추후 파일 변경 요청이 올 시 수정 예정. 현재는 최초 등록 후 답변지 다운로드만 되도록 처리 -->
                                            <input type="hidden" class="form-control" id="reply_file_${plymindDocumentEntity_index}" name="reply_file" data-file-srl="${plymindDocumentEntity.reply_file_srl}" value="${plymindDocumentEntity.reply_file_name}" readonly>

                                            <input type="hidden" class="form-control" id="document_srl_${plymindDocumentEntity_index}" name="document_srl" data-file-srl="${plymindDocumentEntity.document_srl}" >
                                            <input type="hidden" class="form-control" id="application_srl_${plymindDocumentEntity_index}" name="application_srl" data-file-srl="${plymindDocumentEntity.application_srl}" >
                                            <input type="hidden" class="form-control" id="checkup_file_${plymindDocumentEntity_index}" name="checkup_file" data-file-srl="${plymindDocumentEntity.checkup_file_srl}" value="${plymindDocumentEntity.checkup_file_name}" readonly>
                                            <input type="hidden" class="form-control" id="result_file_${plymindDocumentEntity_index}" name="result_file" data-file-srl="${plymindDocumentEntity.result_file_srl}" value="${plymindDocumentEntity.result_file_name}" readonly>

                                            <div id="reply_file_form-${plymindDocumentEntity_index}" style="margin-left:10px;margin-top:20px;">
                                                <form id="fileupload-form1">
                                                    <input id="image-attach-${plymindDocumentEntity_index}" type="file" name="attach_file" style="display: none;">
                                                    <input id="image-type-${plymindDocumentEntity_index}" type="hidden" name="file_type" value="1">
                                                </form>
                                                <form id="checkout-form">
                                                    <fieldset>
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
                                                <div class="btn-group pull-right">
                                                    <button id="btn-modify" name="btn-modify-${plymindDocumentEntity_index}" class="btn tp-btn-primary btn-modify" type="button">
                                                        <i class="fa fa-check"></i> 답변지 등록
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    </#if>

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
            my.fn.pmove('${Request.contextPath}/user/myadvice/progress_detail/${menu_type}/${application_srl}/${application_group}');
        });

        <#list plymindDocumentEntities as plymindDocumentEntity>
            <#if plymindDocumentEntity.reply_file_srl gt 0 >
                $('#reply_file_form-'+${plymindDocumentEntity_index}).hide();
            <#else>
                $('#reply_file_form-'+${plymindDocumentEntity_index}).show();
            </#if>

        </#list>

        /*$('.btn_result_file_modify').each(function(index) {

            //$('#reply_file_form-'+index).hide();

            $(this).click(function(e){

                $('#reply_file_form-'+index).show();
                var srl = 0;

                if($('#file_srl-'+index).attr('data-file-srl') != '') {
                    srl = $('#file_srl-'+index).attr('data-file-srl');
                }

            <#--<@am.bstrap_in_txt_rbtn_uploader id="image-attach-1"
            uri="${Request.contextPath}/user/resource/repository/upload/file/t/"
            showid="file_url-1" isImage=false>

            </@am.bstrap_in_txt_rbtn_uploader>-->

            });
        });*/



        $('.bstrap-in-txt-rbtn').each(function(index) {
            $(this).click(function(e){

            <@am.jsevent_prevent />

                var id = "image-attach-"+index;
                var uri = "${Request.contextPath}/user/resource/repository/upload/file/t/";
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
                    "file_type"         : 2 //1.검사지, 2.답변지, 3.결과지
                };


                if($.trim($('#backup-url-'+index).val()) != $.trim(reqData.file_url1)) reqData.file_srl1 = ${mdv_none};

            <#list plymindDocumentEntities as plymindDocumentEntity>
            var btn_index = ${plymindDocumentEntity_index};
            if(btn_index == index) {
            <#-- 게시물 수정 요청 -->
                <@ap.py_jsajax_request submituri="${Request.contextPath}/user/myadvice/${plymindDocumentEntity.document_srl}/t/" method="PUT"
                moveuri="${Request.contextPath}/user/myadvice/progress_list" errortitle="수정 실패" ; job >
                    <#if job == "success_job">
                    <#-- 게시믈 수정 성공하면 toaset 메시지를 보여 준다 -->
                        var toastMsg = '심리검사를 수정 하였습니다.';
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


    });

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->