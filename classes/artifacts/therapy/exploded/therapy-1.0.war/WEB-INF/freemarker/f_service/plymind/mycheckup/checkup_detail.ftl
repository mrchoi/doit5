<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/jquery-fileupload/jquery.fileupload.js"></script>


<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-checkup-detail">
<#assign _page_parent_name="나의 상담 정보">
<#assign _page_current_name="심리 검사 상세보기">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<div class="main-container">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <blockquote>${_page_current_name}</blockquote>
            </div>
        </div>

        <div class="col-md-12 vendor-box vendor-box-grid"><!-- venue box start-->
            <div class="row">
                <div class="col-md-12 vendor-detail"><!-- venue details -->
                    <div class="caption"><!-- caption -->
                        <div class="col-md-4"><h3>${applicationEntity.title}</h3></div>
                        <div class="col-md-6">
                            <p class="location">
                                (<#if applicationEntity.application_status == 0>접수</#if>
                                <#if applicationEntity.application_status == 1>준비중</#if>
                                <#if applicationEntity.application_status == 2>진행중</#if>
                                <#if applicationEntity.application_status == 3>완료</#if>
                                <#if applicationEntity.application_status == 4>취소</#if>)
                            </p>
                        </div>

                    </div><!-- /.caption -->

                    <#if plymindDocumentEntities?size == 0>
                        <#--
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well">
                                    <div class="row">
                                        <div class="col-md-12 text-center">
                                            작성된 상담사 코멘트가 없습니다.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        -->
                    <#else>
                        <#assign cnt = 0>
                        <#list plymindDocumentEntities as plymindDocumentEntity>
                            <div class="vendor-price">
                                <p>STEP1 : 하단 버튼을 클릭하여 검사지를 다운 받아주세요.</p>
                                <a href="${plymindDocumentEntity.checkup_file_url}" class="btn tp-btn-default">  <i class="fa fa-download"></i> 검사지 다운로드</a>
                            <#--<button id="reply_file_download" class="btn btn-sm btn-default" type="button" onclick="window.open('${plymindDocumentEntity.checkup_file_url}')">
                                <i class="fa fa-download"></i> 다운로드
                            </button>-->

                            </div>
                            <div class="vendor-price">
                                <p>STEP2 : 작성 완료 후 하단 버튼을 클릭하여 검사지를 첨부해주세요.</p>

                                <#if plymindDocumentEntity.reply_file_srl gt 0 > <#--답변지가 존재 하는 경우 -->
                                    <#--<input class="form-control" id="reply_file" name="reply_file" data-file-srl="${plymindDocumentEntity.reply_file_srl}" value="${plymindDocumentEntity.reply_file_name}" readonly>
                                    <br>

                                    <button id="btn_result_file_modify" class="btn btn-sm btn-default" type="button">
                                        <i class="fa fa-download"></i> 파일 변경
                                    </button>
                                    ( 버튼을 클릭 하시면 답변지를 변경을 하실 수 있습니다. )-->
                                    <a href="${plymindDocumentEntity.reply_file_url}" class="btn tp-btn-default">  <i class="fa fa-download"></i> 답변지 다운로드</a>
                                <#else> <#--답변지가 존재 하지 않는 경우 -->

                                </#if>

                                <div id="reply_file_form" style="margin-top:10px;">
                                    <form id="fileupload-form1">
                                        <input id="image-attach1" type="file" name="attach_file" style="display: none;">
                                        <input id="image-type1" type="hidden" name="file_type" value="1">
                                    </form>
                                    <form id="checkout-form">
                                        <fieldset>
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
                                    <div class="btn-group pull-right">
                                        <button id="btn_modify" name="submit" class="btn tp-btn-primary" type="button">
                                            <i class="fa fa-check"></i> 답변지 등록
                                        </button>
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
            </div>
            <!-- venue details -->
        </div>

        <!-- Button -->
        <div class="form-group">
            <button class="btn tp-btn-primary btn-prev-page">
                <i class="fa fa-chevron-left"></i> 목록
            </button>
            <#--<div class="btn-group pull-right">
                <button id="btn_modify" name="submit" class="btn tp-btn-primary" type="button">
                    <i class="fa fa-check"></i> 심리 검사 수정
                </button>
            </div>-->
        </div>

    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        $('.btn-prev-page').click(function(e){
            <@ap.py_jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/mycheckup/list');
        });

        $('#btn_result_file_modify').click(function(e){
            $('#reply_file_form').show();
        });

        <#-- 업로드 버튼을 눌렀을 때 파일 선택 윈도우를 출력 하고 파일을 업로드 시킨다.(파일 저장소) -->
        <@am.bstrap_in_txt_rbtn_uploader id="image-attach1"
        uri="${Request.contextPath}/user/resource/repository/upload/file/t/"
        showid="file_url1" isImage=false>
            $('input[name=file_srl1]').val(result.file_srl);
            $('#backup-url1').val(result.domain + result.uri);
        </@am.bstrap_in_txt_rbtn_uploader>

            $('.bstrap-in-txt-rbtn').click(function(e){
            <@am.jsevent_prevent />
                $('#image-attach1').trigger('click');
            });

    var checkup_file_srl = 0;
    var reply_file_srl = 0;
    var result_file_srl = 0;

    <#assign cnt1 = 0>
    <#if plymindDocumentEntities?size != 0 >
        <#list plymindDocumentEntities as plymindDocumentEntity>
            checkup_file_srl = ${plymindDocumentEntity.checkup_file_srl};
            reply_file_srl = ${plymindDocumentEntity.reply_file_srl};
            result_file_srl = ${plymindDocumentEntity.result_file_srl};

            <#assign document_srl = plymindDocumentEntity.document_srl >

            <#if plymindDocumentEntity?exists && plymindDocumentEntity.reply_file_srl gt 0 > <#--답변지가 존재 하는 경우 -->
                $('#reply_file_form').hide();
            <#else> <#--답변지가 존재 하지 않는 경우 -->
                $('#reply_file_form').show();
            </#if>

            <#assign cnt1 = cnt1+1?int>
            <#if cnt1 == 1>
                <#break>
            </#if>
        </#list>


        $('#btn_modify').click(function(e) {

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
                "file_type"         : 2 //1.검사지, 2.답변지, 3.결과지
            };

            if($.trim($('#backup-url1').val()) != $.trim(reqData.file_url1)) reqData.file_srl1 = ${mdv_none};

        <#-- 게시물 수정 요청 -->
            <@ap.py_jsajax_request submituri="${Request.contextPath}/user/mycheckup/${document_srl}/t/" method="PUT"
            moveuri="${Request.contextPath}/user/mycheckup/list" errortitle="수정 실패" ; job >
                <#if job == "success_job">
                <#-- 게시믈 수정 성공하면 toaset 메시지를 보여 준다 -->
                    var toastMsg = '심리검사를 수정 하였습니다.';
                    <@ap.py_jsnoti title="수정 성공" content="toastMsg" boxtype="success" contenttype="var" />
                <#elseif job == "common_job">
                <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    //$btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 게시물 수정');
                </#if>
            </@ap.py_jsajax_request>
        });

    </#if>

    });

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->