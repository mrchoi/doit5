<#assign _page_id="myadvice-progress-detail">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담 상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">진행중인 상담 정보</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <p>
                                                신청자 : ${applicationEntity.user_name}
                                            </p>
                                            <p>
                                                상담내역 : ${applicationEntity.title}

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
                                            </p>
                                            <#if applicationEntity.contents_srl == 1 || applicationEntity.contents_srl == 2>
                                            <p>
                                                스마트기기 수령자 : ${addressEntity.receive_name}
                                            </p>
                                            <p>
                                                스마트기기 수령자 전화번호 : ${addressEntity.receive_phone}
                                            </p>
                                            <p>
                                                스마트기기 수령지 주소 : ${addressEntity.receive_address}
                                            </p>
                                            </#if>
                                        </div>
                                    </div>
                                    <#if applicationEntity.advisor_name?? && applicationEntity.advisor_name != ''>
                                        <div class="row">
                                            <div class="col-md-12">
                                                <p>상담사 : ${applicationEntity.advisor_name}</p>
                                            </div>
                                        </div>
                                    </#if>
                                    <#if applicationEntity.appointment_date?? && applicationEntity.appointment_date != ''>
                                        <div class="row">
                                            <div class="col-md-12">
                                                <p>
                                                    상담 일정 :
                                                    ${applicationEntity.appointment_date?substring(0,4)}년
                                                    ${applicationEntity.appointment_date?substring(4,6)}월
                                                    ${applicationEntity.appointment_date?substring(6,8)}일
                                                    ${applicationEntity.appointment_time}시 00분
                                                </p>
                                            </div>
                                        </div>
                                    </#if>
                                </div>
                                <#if applicationMemberEntities?size &gt; 1>
                                <br>
                                <div class="well">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <#assign cnt=0?int>
                                            <#list applicationMemberEntities as applicationMemberEntity>
                                            <label for="${applicationMemberEntity.member_srl}">
                                                <input type="radio" name="application_member" id="${applicationMemberEntity.member_srl}" value="${applicationMemberEntity.member_srl}" class="styled application-member" <#if member_srl == applicationMemberEntity.member_srl>checked</#if>> ${applicationMemberEntity.user_name}
                                            </label>&nbsp;&nbsp;&nbsp;
                                            <#assign cnt=cnt+1?int>
                                            </#list>
                                        </div>
                                    </div>
                                </div>
                                </#if>
                                <br>
                                <div class="btn-group pull-right">
                                    <button id="btn_pretesting_view" class="btn btn-sm btn-default" type="button">
                                        <i class="fa fa-search"></i> 인테이크지 정보 보기
                                    </button>
                                </div>
                                <div class="btn-group pull-right">
                                    <button id="btn_userinfo_view" class="btn btn-sm btn-default" type="button">
                                        <i class="fa fa-search"></i> 사용자 정보 보기
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>

        <#if applicationEntity.contents_srl == 1 || applicationEntity.contents_srl == 2>
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-2" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">마음일기</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well-box">
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
                                                            ${(diaryEntity.c_date * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                            <#assign cnt = cnt+1?int>
                                            <#if cnt == 3>
                                                <#break>
                                            </#if>
                                        </#list>
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>
        </#if>

        <#if applicationEntity.contents_srl == 5>
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-3" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">인문 컨텐츠</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well-box">
                                    <#if pushEntities?size == 0>
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div class="well">
                                                    <div class="row">
                                                        <div class="col-md-12 text-center">
                                                            등록된 인문컨텐츠가 없습니다.
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                    <#else>
                                        <#--<div class="pull-right"><a href="#" id="push_more"><i class="fa fa-plus"></i> 더보기</a></div>-->
                                        <#assign cnt = 0>
                                        <#list pushEntities as pushEntity>
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="well">
                                                        <#if pushEntity.push_text??>
                                                            <div class="row">
                                                                <div class="col-md-9">
                                                                ${pushEntity.push_text}
                                                                </div>
                                                                <div class="text-center">
                                                                ${(pushEntity.book_time * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}
                                                                </div>
                                                            </div>
                                                        <#else>
                                                            <div class="row">
                                                                <div class="col-md-12 text-center">
                                                                    등록된 인문컨텐츠가 없습니다.
                                                                </div>
                                                            </div>

                                                        </#if>
                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                            <#assign cnt = cnt+1?int>
                                            <#if cnt == 3>
                                                <#break>
                                            </#if>
                                        </#list>
                                    </#if>
                                    <div class="pull-right">
                                        <button id="btn_push_add" class="btn btn-sm btn-default" type="button">
                                            <i class="fa fa-check"></i> 인문컨텐츠 등록
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>
        </#if>

        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-4" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">메시지</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well-box">
                                <#if messageEntities?size == 0>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="well">
                                                <div class="row">
                                                    <div class="col-md-12 text-center">
                                                        발송된 메세지가 없습니다.
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <br>
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
                                                        <#if messageEntity.noti_time1?? && messageEntity.noti_time1 &gt; 0>
                                                            <div class="text-center">
                                                                ${(messageEntity.noti_time1 * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}
                                                            </div>
                                                        <#else>
                                                            <div class="text-center">
                                                                ${(messageEntity.book_time * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}
                                                            </div>
                                                        </#if>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                        <#assign cnt = cnt+1?int>
                                        <#if cnt == 3>
                                            <#break>
                                        </#if>
                                    </#list>
                                </#if>
                                    <div class="pull-right">
                                        <button id="btn_message_add" class="btn btn-sm btn-default" type="button">
                                            <i class="fa fa-check"></i> 메세지 발송
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>

        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-5" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">상담사 코멘트</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well-box">
                                    <#if plymindDocumentEntities?size == 0>
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
                                    <#else>
                                        <div class="pull-right">
                                            <a href="#" id="advisor_comment_more"><i class="fa fa-plus"></i> 더보기</a>
                                        </div>
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
                                                                    <p>STEP2. 작성 완료 후 하단 버튼을 클릭하여 검사지를 첨부해 주세요.</p>
                                                                    <input class="hidden" id="reply_file" name="reply_file" data-file-srl="${plymindDocumentEntity.reply_file_srl}" value="${plymindDocumentEntity.reply_file_name}" readonly>
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

                                                                </div>
                                                            </div>
                                                        </#if>
<#--

                                                        <#if plymindDocumentEntity.reply_file_srl?int &gt; 0>&lt;#&ndash;답변지가 존재 하는 경우 : 결과지 등록 폼 보여줌&ndash;&gt;
                                                            <br>
                                                            <div class="row">
                                                                <div class="col-md-12">
                                                                    <p>STEP3. 결과보고서를 첨부해주세요.</p>
                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-sm-12 inline-group">
                                                                    <div class="form-group">
                                                                        <input type="hidden" class="form-control" id="result_file" name="result_file" data-file-srl="${plymindDocumentEntity.result_file_srl}" value="${plymindDocumentEntity.result_file_name}" readonly>
                                                                    </div>
                                                                    &lt;#&ndash;결과지가 존재 하는 경우 &ndash;&gt;
                                                                    &lt;#&ndash;<#if plymindDocumentEntity.result_file_srl gt 0 >

                                                                        <button id="btn_result_file_modify" class="btn btn-sm btn-default" type="button">
                                                                            <i class="fa fa-download"></i> 파일 변경
                                                                        </button>
                                                                        ( 버튼을 클릭 하시면 결과보고서를 변경을 하실 수 있습니다. )
                                                                    <#else>&ndash;&gt; &lt;#&ndash;결과지가 존재 하지 않는 경우 &ndash;&gt;


                                                                    &lt;#&ndash;</#if>&ndash;&gt;

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
                                                                    </div>
                                                                    <div class="pull-right">
                                                                        <button id="btn_modify" class="btn btn-sm btn-default" type="button">
                                                                            <i class="fa fa-check"></i> 결과보고서 등록
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        <#else>
                                                            답변지 확인 후 결과보고서를 등록 하실 수 있습니다.
                                                        </#if>
-->

                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                            <#assign cnt = cnt+1?int>
                                            <#if cnt == 1>
                                                <#break>
                                            </#if>
                                        </#list>
                                    </#if>
                                    <div class="pull-right">
                                        <button id="btn_comment_add" class="btn btn-sm btn-default" type="button">
                                            <i class="fa fa-check"></i> 상담사 코멘트 등록
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="btn-group pull-left">
                <button id="btn_progress_list" class="btn btn-sm btn-info" type="button">
                    <i class="fa fa-chevron-left"></i> 목록보기
                </button>
            </div>
        <#if applicationEntity.contents_srl == 3 || applicationEntity.contents_srl == 4>
            <div class="btn-group pull-right">
                <button id="btn_complete" class="btn btn-sm btn-success" type="button">
                    <i class="fa fa-check"></i> 상담완료 처리
                </button>
            </div>
        </#if>
        </article>
    </div>
</section>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        <#-- 사용자 선택 -->
        $('.application-member').click(function(e) {
            var member_srl = 0;
            $('input:radio[name="application_member"]:checked').each(function() {
                member_srl = this.value;
            });

            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/progress_detail/' + member_srl + '/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 심리상담 리스트 페이지로 이동 -->
        $('#btn_progress_list').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/progress_list');
        });

        <#-- 필수항목 보기 -->
        $('#btn_userinfo_view').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/userinfo_view/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 인테이크지 보기 -->
        $('#btn_pretesting_view').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/pretesting_view/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 인문컨텐츠 더보기 페이지로 이동 -->
        $('#push_more').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/push_view/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 인문컨텐츠 더보기 페이지로 이동 -->
        $('#btn_push_add').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/push_list/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 마음일기 더보기 페이지로 이동 -->
        $('#mind_diary_more').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/mind_diary_view/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 인문컨텐츠 더보기 페이지로 이동 -->
        $('#message_more').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/message_view/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 인문컨텐츠 더보기 페이지로 이동 -->
        $('#btn_message_add').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/message_add/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 상담사 코멘트 더보기 페이지로 이동 -->
        $('#advisor_comment_more').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/advisor_comment_view/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 상담사 코멘트 등록 페이지로 이동 -->
        $('#btn_comment_add').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/advisor_comment_add/${applicationEntity.member_srl}/${applicationEntity.application_srl}/${applicationEntity.application_group}');
        });

        <#-- 상담사 완료처리 -->
        $('#btn_complete').click(function(e){
            var reqData = {
                "application_srl" : ${applicationEntity.application_srl}
            };

            <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/myadvice/advice_complete/t/"
            moveuri="#${Request.contextPath}/admin/plymind/myadvice/progress_list" errortitle="상담완료 처리 실패" ; job >
                <#if job == "success_job">
                    var toastMsg = '상담완료 처리가 정상적으로 이루어졌습니다.';
                    <@am.jsnoti title="상담완료 처리 성공" content="toastMsg" boxtype="success" />
                </#if>
            </@am.jsajax_request>
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

                <#if plymindDocumentEntity?exists && plymindDocumentEntity.result_file_srl gt 0 > <#--답변지가 존재 하는 경우 -->
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
            var checkup_file_srl = 0;
            if($('#checkup_file').attr('data-file-srl') != '') {
                checkup_file_srl = $('#checkup_file').attr('data-file-srl');
            }

            if(checkup_file_srl == '') {
                var toastMsg = '심리 검사 파일을 등록 해 주세요.';
            <@am.jsnoti title="심리 검사 등록 오류" content="toastMsg" boxtype="warn" />
                return;
            }

            var reply_file_srl = $('#reply_file').attr('data-file-srl');
            var result_file_srl = $('#result_file').attr('data-file-srl');

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

        <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/myadvice/${document_srl}/t/" method="PUT"
        moveuri="#${Request.contextPath}/admin/plymind/myadvice/progress_list" errortitle="수정 실패" ; job >
            <#if job == "success_job">
            <#-- 심리검사 수정 성공하면 toast 메시지를 보여 준다 -->
                var toastMsg = '게시물을 수정 하였습니다.';
                <@am.jsnoti title="심리상태 수정 성공" content="toastMsg" boxtype="success" />

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