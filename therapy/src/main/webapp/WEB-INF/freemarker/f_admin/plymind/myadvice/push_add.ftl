<#assign _page_id="myadvice-push-add">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담 상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">인문 컨텐츠 등록</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <p>
                                                ${pushEntity.application_number} 회차 인문컨텐츠 등록
                                            </p>
                                            <p>
                                                발송 날짜 :
                                                ${pushEntity.push_date?substring(0,4)}년
                                                ${pushEntity.push_date?substring(4,6)}월
                                                ${pushEntity.push_date?substring(6,8)}일
                                            </p>
                                            <p>
                                                발송 시간 :
                                                ${pushEntity.push_time?substring(0,2)} 시
                                                ${pushEntity.push_time?substring(2,4)} 분
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <form id="checkout-form">
                                    <fieldset>
                                        <legend>인문 컨텐츠 내용을 입력 합니다.</legend>

                                        <div class="row">
                                            <div class="col-md-12">
                                                <div class="form-group">
                                                    <label>인문 컨텐츠 내용</label>
                                                    <#if pushEntity.push_text?? && pushEntity.push_text != ''>
                                                        <input id="push_text" name="push_text" class="form-control"value="${pushEntity.push_text}" placeholder="인문컨텐츠 내용을 입력하세요.">
                                                    <#else>
                                                        <input id="push_text" name="push_text" class="form-control"value="" placeholder="인문컨텐츠 내용을 입력하세요.">
                                                    </#if>
                                                </div>
                                            </div>
                                        </div>
                                    </fieldset>

                                    <div class="form-actions">
                                        <div class="btn-group">
                                            <button id="btn_push_add" class="btn btn-sm btn-success" type="button">
                                                <i class="fa fa-check"></i> 등록
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>

        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="btn-group pull-left">
                <button id="btn-push-list" class="btn btn-sm btn-info" type="button">
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
        $('#btn-push-list').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/push_list/${member_srl}/${application_srl}/${application_group}');
        });

        $('#btn_push_add').click(function(e) {
            var push_text = $.trim($('#push_text').val());

            if(push_text == '') {
                var toastMsg = '인문 컨텐츠를 작성해 주세요.';
                <@am.jsnoti title="인문 컨텐츠 확인" content="toastMsg" boxtype="warn" />
                return;
            }

            var noti_time2 = "${pushEntity.push_date}" + "${pushEntity.push_time}";

            var reqData = {
                "member_srl"        : ${member_srl},
                "application_srl"   : ${pushEntity.application_srl},
                "application_group" : ${pushEntity.application_group},
                "noti_srl"          : ${pushEntity.noti_srl},
                "push_text"         : push_text,
                "noti_time2"        : noti_time2
            };

            <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/myadvice/push_add/t/"
            moveuri="#${Request.contextPath}/admin/plymind/myadvice/push_list/${member_srl}/${application_srl}/${application_group}" errortitle="인문컨텐츠 등록 실패" ; job >
                <#if job == "success_job">
                    var toastMsg = '인문컨텐츠 등록이 정상적으로 이루어졌습니다.';
                    <@am.jsnoti title="인문컨텐츠 등록 성공" content="toastMsg" boxtype="success" />
                </#if>
            </@am.jsajax_request>
        });
    };

    loadScript("${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js", function() {
        loadScript("${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js", function () {
            loadScript("${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js", function () {
                loadScript("${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js", function () {
                    loadScript("${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js", function () {
                        loadScript("${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js", pagefunction)
                    });
                });
            });
        });
    });
</script>