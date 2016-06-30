<#assign _page_id="appointment-appointment-list">
<#assign _page_parent_name="예약관리">
<#assign _page_current_name="예약목록">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />
<#-- widget grid -->
<section id="widget-grid" class="">
<#-- row -->
    <div class="row">
    <#-- NEW WIDGET START -->
        <article class="col-sm-12 col-md-12 col-lg-12">
        <#-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">예약 목록</@am.widget_title>

            <#-- widget div-->
                <div>
                <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                <#-- end widget edit box -->

                <#-- widget content -->
                <#-- datable 의 header toolbar 를 사용하지 않고 위젯 타이틀 밑에 딱 붙이기 위해서 -19px margin-top 을 주었음 -->
                <#--<div class="widget-body no-padding" style="margin-top: -19px;">-->
                <#-- button 을 넣을 자리가 없어서 다시 top 영역을 사용 하기로 함. 위의 주석은 추후 버튼 넣을거 없다면 참고용으로 그대로 둔다. -->
                    <div class="widget-body no-padding">
                    <#-- datatable 에서 검색을 지원하는 column 의 th 는 dt-column, dt-column-<column number> class 를 포함해야 한다.(localStorage를 이용한 초기화에 이용됨) -->
                        <table id="${_page_id}" class="table table-striped table-bordered" width="100%">
                            <thead>
                            <tr>
                                <th class="hasinput smart-form"></th>
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="상담명 검색"
                                           rel="tooltip" data-placement="top" data-original-title="상담명 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-4" placeholder="상담사 검색"
                                           rel="tooltip" data-placement="top" data-original-title="상담사 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-8" placeholder="이름 검색"
                                           rel="tooltip" data-placement="top" data-original-title="이름 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th rclass="text-center" style="min-width:50px;">번호</th>
                                <th rclass="text-center" style="min-width:50px;">구분</th>
                                <th rclass="text-center" style="min-width:50px;">신청내역</th>
                                <th rclass="text-center" style="min-width:50px;">상담회차</th>
                                <th rclass="text-center" style="min-width:50px;">상담사</th>
                                <th rclass="text-center" style="min-width:50px;">예약날짜</th>
                                <th rclass="text-center" style="min-width:50px;">예약시간</th>
                                <th rclass="text-center" style="min-width:50px;">신청자</th>
                                <th data-hide="phone,tablet" class="text-center" style="min-width:50px;">진행상태</th>
                                <th data-hide="phone,tablet" class="text-center" style="min-width:50px;">결제상태</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
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

<#-- 삭제 대기를 위해 화면 전체 block -->
<div id="ckpush-window-block" style="display:none;">
    <h6>삭제 중 입니다. 잠시만 대기 하세요.</h6>
</div>


<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };

        <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/plymind/appointment/list/t/" ; job>
            <#if job == "buttons">
                    []
            <#elseif job == "order">
                    [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'appointment_srl', mData: 'appointment_srl', sClass: 'text-center', bSortable: true, bSearchable: false},
                    {sName: 'kind', mData: 'kind', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if(data == '1') {
                                return '심리 상담';
                            } else {
                                return '심리 검사';
                            }
                        }
                    },
                    {sName: 'title', mData: 'title', sClass: 'text-center', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full, dtObj) {
                            var viewStr = '';

                            viewStr += data + ' ';

                            if(full.kind == '1') {
                                if (full.advice_type == '1') {
                                    viewStr += ' - 으뜸 ' + full.advice_number + '회 ';
                                }
                                if (full.advice_type == '2') {
                                    viewStr += ' - 가장 ' + full.advice_number + '회 ';
                                }
                                if (full.advice_type == '3') {
                                    viewStr += ' - 한마루 ' + full.advice_number + '회 ';
                                }

                                if (full.advice_period == '1') {
                                    viewStr += ' - 1주';
                                }
                                if (full.advice_period == '4') {
                                    viewStr += ' - 4주 ( 1개월 )';
                                }
                                if (full.advice_period == '12') {
                                    viewStr += ' - 12주 ( 3개월 )';
                                }
                                if (full.advice_period == '24') {
                                    viewStr += ' - 24주 ( 6개월 )';
                                }
                                if (full.advice_period == '52') {
                                    viewStr += ' - 52주 ( 12개월 )';
                                }
                            }

                            if(full.appointment_status == '0' || full.appointment_status == '1') {
                                var navHash = $(location).attr('hash').substring(1),
                                        moveURL = '#${Request.contextPath}/admin/plymind/appointment/detail/' + full.appointment_srl;
                                return '<a class="datatable-row-detail-view" data-move="' +
                                        moveURL + '" data-nav="' + navHash + '">' + viewStr + '</a>';
                            } else {
                                return viewStr;
                            }
                        }
                    },
                    {sName: 'application_number', mData: 'application_number', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            return data + '회차';
                        }
                    },
                    {sName: 'advisor_name', mData: 'advisor_name', sClass: 'text-center', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full, dtObj) {
                            if (data == '') {
                                return '-';
                            } else {
                                return data;
                            }
                        }
                    },
                    {sName: 'appointment_date', mData: 'appointment_date', sClass: 'text-center', bSortable: true, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if (data == '') {
                                return '-';
                            } else {
                                return data.substring(0,4) + '.' + data.substring(4,6) + '.' + data.substring(6,8);
                            }
                        }
                    },
                    {sName: 'appointment_time', mData: 'appointment_time', sClass: 'text-center', bSortable: true, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if (data == '') {
                                return '-';
                            } else {
                                return data + ':00';
                            }
                        }
                    },
                    {sName: 'user_name', mData: 'user_name', sClass: 'text-center', bSortable: true, bSearchable: true},
                    {sName: 'appointment_status', mData: 'appointment_status', sClass: 'text-center', bSortable: true, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if(data == '0') {  return '예약 접수'; }
                            if(data == '1') {  return '예약 완료'; }
                            if(data == '2') {  return '예약 취소'; }
                        }
                    },
                    {sName: 'payment_status', mData: 'payment_status', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            var viewStatus = "";

                            if(full.payment_status == '0') {  viewStatus += "입금대기"; }
                            if(full.payment_status == '1') {  viewStatus += "입금완료"; }
                            if(full.payment_status == '2') {  viewStatus += "환불요청"; }
                            if(full.payment_status == '3') {  viewStatus += "환불완료"; }
                            if(full.payment_status == '4') {  viewStatus += "취소완료"; }

                            return viewStatus;
                        }
                    }
                ]
            </#if>
        </@am.jsdatatable>
    };

    $(document).on("click", '#deposit', function() {
        var $this = $(this);

        var payment_srl = $this.attr('data-payment-srl');
        var application_group = $this.attr('data-application-group');
        var appointment_srl = $this.attr('data-appointment-srl');

        var reqData = {
            "payment_srl"           : payment_srl,
            "application_group"     : application_group,
            "appointment_srl"       : appointment_srl
        };

        <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/payment/deposit/t/"
        moveuri="" errortitle="입금처리 실패" ; job >
            <#if job == "success_job">
                var toastMsg = '입금처리가 정상적으로 이루어졌습니다.';
                <@am.jsnoti title="입금 처리 성공" content="toastMsg" boxtype="success" />
            <#elseif job == "common_job">
                my.fn.removeDatatableStorage('payment-payment-list');
            </#if>
        </@am.jsajax_request>
<#--
        $.ajax({
            type: "POST",
            url: '${Request.contextPath}/admin/plymind/payment/deposit',
            contentType : 'application/json',
            data: JSON.stringify(dataString),
            dataType: 'json',
            success: function (data, status, xhr) {
                if(data.resultCode > 0) {
                    $('#pop-message').text("상담 신청이 정상적으로 이루어졌습니다.");
                    popup_layer();

                    timer = setInterval(function() {
                        my.fn.pmove('${Request.contextPath}/user/payment/paymentForm/' + product_srl + "/" + data.resultCode);
                    }, 1500);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $('#pop-message').text("상담 신청이 실패되었습니다. 다시 시도해 주세요.");
                popup_layer();
                return;
            }
        });
-->
    });

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