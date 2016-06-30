<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js"></script>
<script src="${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js"></script>
<script src="${Request.resPath}js/plugin/jquery-fileupload/jquery.fileupload.js"></script>
<style>
    .modal-dialog {
        /*height: 70% !important;*/
        padding-top:10%;
    }
</style>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-progress-sc-detail">
<#assign _page_parent_name="마이페이지">
<#assign _page_current_name="현재 진행중인 상담">
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
        <div class="row">
            <div class="col-md-12">
                <div class="well">
                    <div class="row">
                        <div class="col-md-12">
                            ${applicationEntity.title}

                            <#if applicationEntity.contents_srl == 1 || applicationEntity.contents_srl == 2>
                                <#if applicationEntity.advice_period == 1> - 1주 </#if>
                                <#if applicationEntity.advice_period == 4> - 4주 ( 1개월 ) </#if>
                                <#if applicationEntity.advice_period == 12> - 12주 ( 3개월 ) </#if>
                                <#if applicationEntity.advice_period == 24> - 24주 ( 6개월 ) </#if>
                                <#if applicationEntity.advice_period == 52> - 52주 ( 12개월 ) </#if>
                                중 <span style="color:red;">${applicationEntity.application_number}</span>주차 상담
                            </#if>

                            <#if applicationEntity.contents_srl == 3 || applicationEntity.contents_srl == 4>
                                <#if applicationEntity.advice_type == 1> - 으뜸 ${applicationEntity.advice_number}회 </#if>
                                <#if applicationEntity.advice_type == 2> - 가장 ${applicationEntity.advice_number}회 </#if>
                                <#if applicationEntity.advice_type == 3> - 한마루 ${applicationEntity.advice_number}회 </#if>
                                중 <span style="color:red;">${applicationEntity.application_number}</span>회차 상담
                            </#if>

                            <#if applicationEntity.contents_srl == 5>
                                <#if applicationEntity.advice_period == 1 && applicationEntity.advice_number == 4>1일 ( 4회 )</#if>
                                <#if applicationEntity.advice_period == 1 && applicationEntity.advice_number == 22>1주</#if>
                                <#if applicationEntity.advice_period == 1 && applicationEntity.advice_number == 1>1주</#if>
                                <#if applicationEntity.advice_period == 4> - 4주 ( ${applicationEntity.advice_number}회 ) </#if>
                                <#if applicationEntity.advice_period == 12> - 12주 ( ${applicationEntity.advice_number}회 ) </#if>
                                <#if applicationEntity.advice_period == 24> - 24주 ( ${applicationEntity.advice_number}회 ) </#if>
                                <#if applicationEntity.advice_period == 52> - 52주 ( ${applicationEntity.advice_number}회 ) </#if>
                                중 <span style="color:red;">${applicationEntity.application_number}</span>회차 상담
                            </#if>
                            <span style="color:red;">
                            <#if applicationEntity.application_status == 0>접수</#if>
                            <#if applicationEntity.application_status == 1>준비중</#if>
                            <#if applicationEntity.application_status == 2>진행중</#if>
                            <#if applicationEntity.application_status == 3>완료</#if>
                            <#if applicationEntity.application_status == 4>취소</#if>
                            </span>
                        </div>
                    </div>
                    <#if applicationEntity.advisor_name?? && applicationEntity.advisor_name != ''>
                        <br>
                        <div class="row">
                            <div class="col-md-12">
                                상담사 : ${applicationEntity.advisor_name}
                                <div class="pull-right">
                                    <button id="btn_advisor_view" name="submit" class="btn btn-sm tp-btn-primary" type="button">
                                        <i class="fa fa-search"></i> 상담사 정보 보기
                                    </button>
                                </div>
                            </div>
                        </div>
                    </#if>

                    <#if (applicationEntity.contents_srl == 1 || applicationEntity.contents_srl == 2) && applicationEntity.application_status == 1 && applicationEntity.application_number == 1>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="pull-right">
                                <button id="btn_receive_check" name="submit" class="btn tp-btn-primary" type="button">
                                    <i class="fa fa-check"></i> 수령 확인
                                </button>
                            </div>
                        </div>
                    </div>
                    </#if>

                    <#if applicationEntity.appointment_date?? && applicationEntity.appointment_date != ''>
                    <div class="row">
                        <div class="col-md-12">
                            상담 일정 :
                            ${applicationEntity.appointment_date?substring(0,4)}년
                            ${applicationEntity.appointment_date?substring(4,6)}월
                            ${applicationEntity.appointment_date?substring(6,8)}일
                            ${applicationEntity.appointment_time}시 00분
                        </div>
                    </div>
                    </#if>
                </div>
            </div>
        </div>

        <#if applicationEntity.contents_srl == 1 || applicationEntity.contents_srl == 2>
        <div class="row">
            <div class="col-md-12">
                <div class="well-box">
                    <div class="col-md-6">마음일기</div>
                    <#if diaryEntities?size == 0>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well">
                                    <div class="row">
                                        <div class="col-md-12 text-center">
                                            작성된 마음 일기가 없습니다.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    <#else>
                        <div class="pull-right"><a href="#" id="mind_diary_more"><i class="fa fa-plus"></i> 더보기</a></div>
                        <#assign cnt = 0>
                        <#list diaryEntities as diaryEntity>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="well">
                                        <div class="row">
                                            <div class="col-md-9">
                                                ${diaryEntity.mind_diary}
                                            </div>
                                            <div class="text-center">
                                                ${(diaryEntity.c_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm:ss")}
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                            <#assign cnt = cnt+1?int>
                            <#if cnt == 3>
                                <#break>
                            </#if>
                        </#list>
                    </#if>
                    <#if applicationEntity.application_status?int == 2>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="pull-right">
                                <button id="mind_diary_add" class="btn tp-btn-primary">
                                    <i class="fa fa-pencil"></i> 마음일기 쓰기
                                </button>
                            </div>
                        </div>
                    </div>
                    </#if>
                </div>
            </div>
        </div>
        </#if>


        <#if applicationEntity.contents_srl == 5>
        <div class="row">
            <div class="col-md-12">
                <div class="well-box">
                    <div class="col-md-6">인문 컨텐츠</div>
                    <#if pushEntities?size == 0>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well">
                                    <div class="row">
                                        <div class="col-md-12 text-center">
                                            발송된 인문컨텐츠가 없습니다.
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    <#else>
                        <div class="pull-right"><a href="#" id="push_more"><i class="fa fa-plus"></i> 더보기</a></div>
                        <#assign cnt = 0>
                        <#list pushEntities as pushEntity>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="well">
                                        <div class="row">
                                            <div class="col-md-9">
                                                ${pushEntity.push_contents}
                                            </div>
                                            <div class="text-center">
                                                ${(pushEntity.push_date1 * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <#assign cnt = cnt+1?int>
                            <#if cnt == 3>
                                <#break>
                            </#if>
                        </#list>
                    </#if>
                </div>
            </div>
        </div>
        </#if>

        <div class="row">
            <div class="col-md-12">
                <div class="well-box">
                    <div class="col-md-6">메시지</div>

                <#if messageEntities?size == 0>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                <div class="row">
                                    <div class="col-md-12 text-center">
                                        메세지가 없습니다.
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                <#else>
                    <div class="pull-right"><a href="#" id="message_more"><i class="fa fa-plus"></i> 더보기</a></div>
                    <#assign cnt = 0>
                    <#list messageEntities as messageEntity>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well">
                                    <div class="row">
                                        <div class="col-md-9">
                                            ${messageEntity.push_text}
                                        </div>
                                        <div class="text-center">
                                            ${(messageEntity.noti_time1 * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                        <#assign cnt = cnt+1?int>
                        <#if cnt == 3>
                            <#break>
                        </#if>
                    </#list>
                </#if>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="well-box">
                    <div class="col-md-6">상담사 코멘트</div>

                    <#if plymindDocumentEntities?size == 0>
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
                    <#else>
                        <div class="pull-right"><a href="#" id="advisor_comment_more"><i class="fa fa-plus"></i> 더보기</a></div>
                        <#assign cnt = 0>
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
                                                <a href="${plymindDocumentEntity.checkup_file_url}" class="btn tp-btn-default">  <i class="fa fa-download"></i> 검사지 다운로드</a>
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

                                                    <#--<input class="form-control" id="reply_file" name="reply_file" data-file-srl="${plymindDocumentEntity.reply_file_srl}" value="${plymindDocumentEntity.reply_file_name}" readonly>
                                                    <br>
                                                    <button id="btn_result_file_modify" class="btn btn-sm btn-default" type="button">
                                                        <i class="fa fa-download"></i> 파일 변경
                                                    </button>
                                                    ( 버튼을 클릭 하시면 답변지를 변경을 하실 수 있습니다. )
                                                    -->
                                                    <a href="${plymindDocumentEntity.reply_file_url}" class="btn tp-btn-default">  <i class="fa fa-download"></i> 답변지 다운로드</a>
                                                <#else> <#--답변지가 존재 하지 않는 경우 -->

                                                </#if>

                                                <div id="reply_file_form" style="margin-left:10px;margin-top:20px;">
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
                                        </div>
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
            </div>
        </div>
        <div class="form-group">
            <button class="btn tp-btn-primary btn-prev-page">
                <i class="fa fa-chevron-left"></i> 목록
            </button>
        </div>
    </div>
</div>


<#if applicationEntity.advisor_name?? && applicationEntity.advisor_name != ''>

<div class="modal fade" id="advisor_view_modal" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true" style="padding-top: 5%;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h1 class="modal-title text-center" id="myModalLabel">[${advisorEntity.nick_name}] 상담사 정보</h1>
            </div>
            <div class="modal-body">
                <#if advisorEntity.memberExtraEntity.certificate?? && advisorEntity.memberExtraEntity.certificate != ''>
                    <div class="row">
                        <div class="col-md-2">
                            <b>자격증명</b>
                        </div>
                        <div class="col-md-10">
                            ${advisorEntity.memberExtraEntity.certificate}
                        </div>
                    </div>
                    <hr>
                </#if>
                <#if advisorEntity.memberExtraEntity.class_srl?int &gt; 0>
                    <div class="row">
                        <div class="col-md-2">
                            <b>등급</b>
                        </div>
                        <div class="col-md-10">
                            <#if advisorEntity.memberExtraEntity.class_srl?int == 1>으뜸</#if>
                            <#if advisorEntity.memberExtraEntity.class_srl?int == 2>가장</#if>
                            <#if advisorEntity.memberExtraEntity.class_srl?int == 3>한마루</#if>
                        </div>
                    </div>
                    <hr>
                </#if>
                <#if advisorEntity.memberExtraEntity.academic_srl?int &gt; 0>
                    <div class="row">
                        <div class="col-md-4">
                            학력
                        </div>
                        <div class="col-md-8">
                            <#if advisorEntity.memberExtraEntity.academic_srl?int == 1>초등학교졸업</#if>
                            <#if advisorEntity.memberExtraEntity.academic_srl?int == 2>중학교졸업</#if>
                            <#if advisorEntity.memberExtraEntity.academic_srl?int == 3>고등학교졸업</#if>
                            <#if advisorEntity.memberExtraEntity.academic_srl?int == 4>대학교졸업</#if>
                            <#if advisorEntity.memberExtraEntity.academic_srl?int == 5>대학원졸업</#if>
                        </div>
                    </div>
                    <hr>
                </#if>
                <#if advisorEntity.memberExtraEntity.self_introduction?? && advisorEntity.memberExtraEntity.self_introduction != ''>
                    <div class="row">
                        <div class="col-md-2">
                            <b>자기소개</b>
                        </div>
                        <div class="col-md-10">
                            ${advisorEntity.memberExtraEntity.self_introduction}
                        </div>
                    </div>
                </#if>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
</#if>


<script type="text/javascript">

    $(document).ready(function() {
        $("#btn_receive_check").click(function(e) {
            var dataString = {
                "application_group" : ${application_group}
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/myadvice/receive_check',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    $('#pop-message').text("수령 확인 처리가 정상적으로 이루어졌습니다.");
                    popup_layer();

                    timer = setInterval(function() {
                        my.fn.pmove('${Request.contextPath}/user/myadvice/progress_detail/${menu_type}/${application_srl}/${application_group}');
                    }, 1000);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("수령 확인 처리가 실패 되었습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });
        });

        $("#btn_advisor_view").click(function(e) {
            $('#advisor_view_modal').modal('show');
        });

        $("#mind_diary_add").click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/myadvice/mind_diary_add/${menu_type}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        $("#mind_diary_more").click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/myadvice/mind_diary_view/${menu_type}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        $("#push_more").click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/myadvice/push_view/${menu_type}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        $("#message_more").click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/myadvice/message_view/${menu_type}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        $("#advisor_comment_more").click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/myadvice/advisor_comment_view/${menu_type}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        $('.btn-prev-page').click(function(e){
            <@am.jsevent_prevent />
            <#if menu_type == "complete" >
                my.fn.pmove('${Request.contextPath}/user/mycomplete/list');
            <#else>
                my.fn.pmove('${Request.contextPath}/user/myadvice/progress_list');
            </#if>
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
            <@ap.py_jsajax_request submituri="${Request.contextPath}/user/myadvice/${document_srl}/t/" method="PUT"
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
        });
        </#if>

    });



</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->