<link rel="stylesheet" type="text/css" media="screen" href="${Request.resPath}css/bootstrap-datetimepicker.min.css">

<#assign _page_id="myadvice-message-add">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담 상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-4" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">메세지 발송</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <form id="checkout-form">
                            <fieldset>
                                <#if applicationMemberEntities?size &gt; 1>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="row">
                                                <div class="col-sm-12">
                                                    <p>사용자 선택</p>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-12">
                                                    <div class="form-group">
                                                        <label for="0">
                                                            <input type="radio" name="application_member" id="0" value="0" class="styled application-member" checked> 전체
                                                        </label>&nbsp;&nbsp;&nbsp;
                                                        <#list applicationMemberEntities as applicationMemberEntity>
                                                            <label for="${applicationMemberEntity.member_srl}">
                                                                <input type="radio" name="application_member" id="${applicationMemberEntity.member_srl}" value="${applicationMemberEntity.member_srl}" class="styled application-member" <#if member_srl == applicationMemberEntity.member_srl>checked</#if>> ${applicationMemberEntity.user_name}
                                                            </label>&nbsp;&nbsp;&nbsp;
                                                        </#list>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </#if>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label>메세지 내용</label>
                                            <input id="push_text" name="push_text" class="form-control"value="" placeholder="메세지 내용을 입력하세요.">
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label>발송 날짜/시간</label>
                                            <div class="input-group">
                                                <input type="text" class="form-control" id="book_time" value="" placeholder="발송 날짜/시간을 선택하세요">
                                                <span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>
                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn_message_add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 메세지 발송
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </article>

        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="btn-group pull-left">
                <button id="btn-progress-detail" class="btn btn-sm btn-info" type="button">
                    <i class="fa fa-chevron-left"></i> 이전
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

        <#-- 심리상담 상세정보 페이지로 이동 -->
        $('#btn-progress-detail').click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/progress_detail/${member_srl}/${application_srl}/${application_group}');
        });

        $('#btn_message_add').click(function(e) {
            var member_srl = 0;
            $('input:radio[name="application_member"]:checked').each(function() {
                member_srl = this.value;
            });

            var push_text = $.trim($('#push_text').val());

            var noti_time2 = $.trim($('#book_time').val()).replace(/-/g, '').replace(/ /g, '').replace(/:/g, '');

            if(push_text == '') {
                var toastMsg = '메세지 내용을 작성해 주세요.';
                <@am.jsnoti title="메세지 내용 확인" content="toastMsg" boxtype="warn" />
                return;
            }

            if(noti_time2 == '') {
                var toastMsg = '메세지 발송 날짜/시간을 선택해 주세요.';
                <@am.jsnoti title="날짜/시간 확인" content="toastMsg" boxtype="warn" />
                return;
            }

            var reqData = {
                "member_srl"        : member_srl,
                "application_srl"   : ${application_srl},
                "application_group" : ${application_group},
                "noti_srl"          : -1,
                "push_text"         : push_text,
                "noti_time2"        : noti_time2
            };

            <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/myadvice/message_add/t/"
            moveuri="#${Request.contextPath}/admin/plymind/myadvice/progress_detail/${member_srl}/${application_srl}/${application_group}" errortitle="메세지 발송 등록 실패" ; job >
                <#if job == "success_job">
                    var toastMsg = '메세지 발송  등록이 정상적으로 이루어졌습니다.';
                    <@am.jsnoti title="메세지 발송  등록 성공" content="toastMsg" boxtype="success" />
                </#if>
            </@am.jsajax_request>
        });

       $('#book_time').datetimepicker({
            format: 'YYYY-MM-DD HH',
            locale: 'ko'
        });
    };

    loadScript("${Request.resPath}js/plugin/clockpicker/clockpicker.min.js", function(){
        loadScript("${Request.resPath}js/plugin/moment/moment.min.js", function() {
            loadScript("${Request.resPath}js/plugin/moment/ko.js", function() {
                loadScript("${Request.resPath}js/plugin/jquery-form/jquery-form.min.js", function() {
                    loadScript("${Request.resPath}js/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js", function () {
                        loadScript("${Request.resPath}js/plugin/summernote/summernote.min.js", pagefunction);
                    });
                });
            });
        });
    });
</script>