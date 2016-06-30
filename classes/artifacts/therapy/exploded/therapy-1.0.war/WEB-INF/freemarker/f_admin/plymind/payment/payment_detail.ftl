<#assign _page_id="payment-payment-detail">
<#assign _page_parent_name="결제관리">
<#assign _page_current_name="결제정보">

<@am.page_simple_depth icon="fa-puzzle-piece" parent="${_page_parent_name}" current="${_page_current_name}" canMoveParent=true />

<#-- widget grid -->
<section id="widget-grid" class="">
    <#-- row -->
    <div class="row">
        <#-- NEW WIDGET START -->
        <article class="col-sm-12 col-md-12 col-lg-12">
            <#-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <#if paymentEntity.payment_status == 0><@am.widget_title icon="fa-table">입금 대기</@am.widget_title></#if>
            <#if paymentEntity.payment_status == 1><@am.widget_title icon="fa-table">입금 완료</@am.widget_title></#if>
            <#if paymentEntity.payment_status == 2><@am.widget_title icon="fa-table">환불 요청</@am.widget_title></#if>
            <#if paymentEntity.payment_status == 3><@am.widget_title icon="fa-table">환불 완료</@am.widget_title></#if>
            <#if paymentEntity.payment_status == 4><@am.widget_title icon="fa-table">결제 취소</@am.widget_title></#if>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <table id="user" class="table table-bordered table-striped" style="clear: both">
                            <tbody>
                                <tr>
                                    <td style="width:30%;"><strong>구분</strong></td>
                                    <td style="width:70%">
                                        <#if paymentEntity.kind == 1>심리상담</#if>
                                        <#if paymentEntity.kind == 2>심리검사</#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>신청내역</strong></td>
                                    <td>
                                        ${paymentEntity.title}
                                        <#if paymentEntity.kind == 1>
                                            <#if paymentEntity.advice_type == 1> - 으뜸 ${paymentEntity.advice_number} 회</#if>
                                            <#if paymentEntity.advice_type == 2> - 가장 ${paymentEntity.advice_number} 회</#if>
                                            <#if paymentEntity.advice_type == 3> - 한마루 ${paymentEntity.advice_number} 회</#if>
                                            <#if paymentEntity.advice_period == 1 && paymentEntity.advice_number == 4> - 1일 ( 4회 )</#if>
                                            <#if paymentEntity.advice_period == 1 && paymentEntity.advice_number == 22> - 1주</#if>
                                            <#if paymentEntity.advice_period == 1 && paymentEntity.advice_number == 1> - 1주</#if>
                                            <#if paymentEntity.advice_period == 4> - 4주 ( 1개월 )</#if>
                                            <#if paymentEntity.advice_period == 12> - 12주 ( 3개월 )</#if>
                                            <#if paymentEntity.advice_period == 24> - 24주 ( 6개월 )</#if>
                                            <#if paymentEntity.advice_period == 52> - 52주 ( 12개월 )</#if>
                                        </#if>
                                    </td>
                                </tr>
                                <#if paymentEntity.advisor_name?? && paymentEntity.advisor_name != ''>
                                <tr>
                                    <td><strong>상담사</strong></td>
                                    <td>${paymentEntity.advisor_name}</td>
                                </tr>
                                </#if>
                                <#if paymentEntity.appointment_date?? && paymentEntity.appointment_date != ''>
                                <tr>
                                    <td><strong>예약일정</strong></td>
                                    <td>${paymentEntity.appointment_date} ${paymentEntity.appointment_time}:00</td>
                                </tr>
                                </#if>
                                <tr>
                                    <td><strong>결제방법</strong></td>
                                    <td>무통장 입금</td>
                                </tr>
                                <tr>
                                    <td><strong>가격</strong></td>
                                    <td>${paymentEntity.price?string.number} 원</td>
                                </tr>
                                <tr>
                                    <td><strong>신청자</strong></td>
                                    <td>${paymentEntity.user_name}</td>
                                </tr>
                                <tr>
                                    <td><strong>날짜</strong></td>
                                    <td>
                                        <#if paymentEntity.payment_status == 0> 요청일 : ${(paymentEntity.reporting_date*1000)?number_to_datetime}</#if>
                                        <#if paymentEntity.payment_status == 1> 입금일 : ${(paymentEntity.payment_date*1000)?number_to_datetime}</#if>
                                        <#if paymentEntity.payment_status == 2> 환불요청일 : ${(paymentEntity.refund_req_date*1000)?number_to_datetime}</#if>
                                        <#if paymentEntity.payment_status == 3> 환불완료일 : ${(paymentEntity.refund_date*1000)?number_to_datetime}</#if>
                                        <#if paymentEntity.payment_status == 4> 취소요청일 : ${(paymentEntity.cancel_date*1000)?number_to_datetime}</#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>진행상태</strong></td>
                                    <td>
                                        <#if paymentEntity.payment_status == 0>입금대기</#if>
                                        <#if paymentEntity.payment_status == 1>입금완료</#if>
                                        <#if paymentEntity.payment_status == 2>환불요청</#if>
                                        <#if paymentEntity.payment_status == 3>환불완료</#if>
                                        <#if paymentEntity.payment_status == 4>결제취소</#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>환불은행</strong></td>
                                    <td>${paymentEntity.refund_bank}</td>
                                </tr>
                                <tr>
                                    <td><strong>환불계좌</strong></td>
                                    <td>${paymentEntity.refund_account}</td>
                                </tr>
                                <tr>
                                    <td><strong>환불 예금주</strong></td>
                                    <td>${paymentEntity.refund_name}</td>
                                </tr>
                                <tr>
                                    <td><strong>상담 진행 현황</strong></td>
                                    <td>
                                        <#list applicationEntities as applicationEntity>
                                            ${applicationEntity.application_number} 회차
                                            <#if applicationEntity.application_status == 0> 접수</#if>
                                            <#if applicationEntity.application_status == 1> 준비중</#if>
                                            <#if applicationEntity.application_status == 2> 진행중</#if>
                                            <#if applicationEntity.application_status == 3> 완료</#if>
                                            <#if applicationEntity.application_status == 4> 취소</#if>
                                            <br>
                                        </#list>
                                    </td>
                                </tr>
                            </tbody>
                        </table>

                        <form>
                            <div class="form-actions">
                                <div class="btn-group pull-left">
                                    <#if paymentEntity.payment_status == 0 || paymentEntity.payment_status == 1>
                                    <button id="btn-payment-page" class="btn btn-sm btn-info" type="button">
                                        <i class="fa fa-chevron-left"></i> 목록
                                    </button>
                                    <#else>
                                    <button id="btn-cancel-page" class="btn btn-sm btn-info" type="button">
                                        <i class="fa fa-chevron-left"></i> 목록
                                    </button>
                                    </#if>
                                </div>
                                <div class="btn-group">
                                    <#if paymentEntity.payment_status == 0>
                                    <button id="btn-payment-process" class="btn btn-sm btn-danger" type="button">
                                        <i class="fa fa-chevron-left"></i> 입금완료 처리
                                    </button>
                                    </#if>
                                    <#if paymentEntity.payment_status == 2>
                                    <button id="btn-refund-process" class="btn btn-sm btn-danger" type="button">
                                        <i class="fa fa-chevron-left"></i> 환불완료 처리
                                    </button>
                                    </#if>
                                </div>
                            </div>
                        </form>
                    </div>
                <#-- end widget content -->
                </div>
            <#-- end widget div -->
            </div>
        <#-- end widget -->
        </article>
    <#-- WIDGET END -->
    </div>
<#-- end row -->
</section>
<#-- end widget grid -->

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        drawBreadCrumb(['${_page_current_name}']);

        <#-- 결제내역 리스트로 이동 -->
        $('#btn-payment-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/payment/payment_list');
        });

        <#-- 취소내역 리스트로 이동 -->
        $('#btn-cancel-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/payment/cancel_list');
        });

        <#-- 입금완료 처리 -->
        $('#btn-payment-process').click(function(e){
            var payment_srl = ${paymentEntity.payment_srl};
            var application_group = ${paymentEntity.application_group};
            var appointment_srl = ${paymentEntity.appointment_srl};

            var reqData = {
                "payment_srl"           : payment_srl,
                "application_group"     : application_group,
                "appointment_srl"       : appointment_srl
            };

            <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/payment/deposit/t/"
            moveuri="#${Request.contextPath}/admin/plymind/payment/payment_list" errortitle="입금처리 실패" ; job >
                <#if job == "success_job">
                    var toastMsg = '입금처리가 정상적으로 이루어졌습니다.';
                    <@am.jsnoti title="입금 처리 성공" content="toastMsg" boxtype="success" />
                <#elseif job == "common_job">
                    my.fn.removeDatatableStorage('appointment-appointment-list');
                </#if>
            </@am.jsajax_request>
        });

        <#-- 환불완료 처리 -->
        $('#btn-refund-process').click(function(e){
            var payment_srl = ${paymentEntity.payment_srl};
            var application_group = ${paymentEntity.application_group};
            var appointment_srl = ${paymentEntity.appointment_srl};

            var reqData = {
                "payment_srl"           : payment_srl
            };

        <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/payment/refund/t/"
        moveuri="#${Request.contextPath}/admin/plymind/payment/cancel_list" errortitle="환불처리 실패" ; job >
            <#if job == "success_job">
                var toastMsg = '환불처리가 정상적으로 이루어졌습니다.';
                <@am.jsnoti title="환불 처리 성공" content="toastMsg" boxtype="success" />
            <#elseif job == "common_job">
                my.fn.removeDatatableStorage('payment-payment-list');
            </#if>
        </@am.jsajax_request>
        });

    };

    loadScript("${Request.resPath}js/plugin/x-editable/moment.min.js", function() {
        loadScript("${Request.resPath}js/plugin/x-editable/jquery.mockjax.min.js", function() {
            loadScript("${Request.resPath}js/plugin/x-editable/x-editable.min.js", function() {
                loadScript("${Request.resPath}js/plugin/typeahead/typeahead.min.js", function() {
                    loadScript("${Request.resPath}js/plugin/typeahead/typeaheadjs.min.js", function() {
                        loadScript("${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js", function () {
                            loadScript("${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js", function () {
                                loadScript("${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js", function () {
                                    loadScript("${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js", function () {
                                        loadScript("${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js", function () {
                                            loadScript("${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js", function () {
                                                loadScript("${Request.resPath}js/ckpush/md5.js", pagefunction);
                                            })
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    });
</script>