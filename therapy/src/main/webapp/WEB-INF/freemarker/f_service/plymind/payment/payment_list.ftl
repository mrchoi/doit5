<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js"></script>
<script src="${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js"></script>
<style>
    .dataTables_filter .input-group-addon {
        width: 50px;
        margin-top: 0;
        float: left;
        height: 47px;
        padding-top: 14px;
        border: 1px solid #ccc;
    }
    input.form-control{
        margin-left:0px;
    }
</style>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-cancel-list">
<#assign _page_parent_name="나의 예약/결제 정보">
<#assign _page_current_name="결제내역">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <blockquote>${_page_current_name}</blockquote>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12 view-cart"><!-- view cart -->
                <table class="shop_table" id="${_page_id}" >
                    <!-- shop table -->
                    <thead>
                    <tr>
                        <th rclass="text-center" style="min-width:50px;">번호</th>
                        <th class="text-center" style="min-width:50px;">구분</th>
                        <th class="text-center" style="min-width:100px;">상담내역</th>
                        <th class="text-center" style="min-width:50px;">결제내역</th>
                        <th data-hide="phone,tablet" class="text-center" style="min-width:50px;">진행상태</th>
                        <th data-hide="phone,tablet" class="text-center" style="min-width:50px;">날짜</th>
                        <th data-hide="phone,tablet" class="text-center" style="min-width:50px;">결제취소</th>
                    </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };

    <#--<@ap.py_jsnoti title="파일 업로드" content="업로드 성공 했습니다." contenttype="val" boxtype="info" />-->

    <@ap.py_jsdatatable listId="${_page_id}" searchTooltip="상담명 검색" url="${Request.contextPath}/user/payment/list/COMPLETE/t/" ; job>
        <#if job == "order">
                [[0, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'payment_srl', mData: 'payment_srl', sClass: 'text-center', bSortable: true, bSearchable: false},
                {sName: 'kind', mData: 'kind', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full, dtObj) {
                        if(data == '1') {  return "심리</br>상담";   }
                        if(data == '2') {  return "심리</br>검사";   }
                    }
                },
                {sName: 'title', mData: 'title', sClass: 'text-center', bSortable: false, bSearchable: true,
                    mRender: function(data, type, full, dtObj) {
                        var viewStr = "";

                        viewStr += full.title + " ";

                        if(full.kind == '1') {
                            if (full.advice_type == '1') {
                                viewStr += " - 으뜸 " + full.advice_number + "회 ";
                            }
                            if (full.advice_type == '2') {
                                viewStr += " - 가장 " + full.advice_number + "회 ";
                            }
                            if (full.advice_type == '3') {
                                viewStr += " - 한마루 " + full.advice_number + "회 ";
                            }

                            if (full.advice_period == '1' && full.advice_number == '4') {
                                viewStr += " - 1일 ( 4회 ) ";
                            }
                            if (full.advice_period == '1' && full.advice_number == '22') {
                                viewStr += " - 1주 ";
                            }
                            if (full.advice_period == '1' && full.advice_number == '1') {
                                viewStr += " - 1주 ";
                            }
                            if (full.advice_period == '4') {
                                viewStr += " - 4주 ( 1개월 ) ";
                            }
                            if (full.advice_period == '12') {
                                viewStr += " - 12주 ( 3개월 ) ";
                            }
                            if (full.advice_period == '24') {
                                viewStr += " - 24주 ( 6개월 ) ";
                            }
                            if (full.advice_period == '52') {
                                viewStr += " - 52주 ( 12개월 ) ";
                            }

                            if (full.appointment_date != '') {
                                viewStr += "<br><br>";
                                viewStr += "상담예약 : "
                                        + full.appointment_date.substring(0,4) + "."
                                        + full.appointment_date.substring(4,6) + "."
                                        + full.appointment_date.substring(6,8) + " "
                                        + full.appointment_time + ":00";
                            }
                            if (full.advisor_name != '') {
                                viewStr += "<br>";
                                viewStr += "상담사 : " + full.advisor_name;
                            }
                        }

                        return viewStr;
                    }
                },
                {sName: 'price', mData: 'price', sClass: 'text-center', bSortable: false, bSearchable: true,
                    mRender: function(data, type, full, dtObj) {
                        return "무통장입금 " + data.toLocaleString() + " 원";
                    }
                },
                {sName: 'payment_status', mData: 'payment_status', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full, dtObj) {
                        var viewStatus = "";
                        if(data == '0') {  viewStatus += "입금대기"; }
                        if(data == '1') {  viewStatus += "입금완료"; }
                        if(data == '2') {  viewStatus += "환불요청"; }
                        if(data == '3') {  viewStatus += "환불완료"; }
                        if(data == '4') {  viewStatus += "취소완료"; }

                        return viewStatus;
                    }
                },
                {sName: 'payment_status', mData: 'payment_status', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full, dtObj) {

                        var viewNal = "";
                        if(data == '0') {  viewNal += "신청일 : " + new Date(my.fn.toNumber(full.reporting_date) * 1000).format('yyyy.MM.dd');      }
                        if(data == '1') {  viewNal += "결제일 : " + new Date(my.fn.toNumber(full.payment_date) * 1000).format('yyyy.MM.dd');        }
                        if(data == '2') {  viewNal += "환불요청일 : " + new Date(my.fn.toNumber(full.refund_req_date) * 1000).format('yyyy.MM.dd');   }
                        if(data == '3') {  viewNal += "환불요청일 : " + new Date(my.fn.toNumber(full.refund_date) * 1000).format('yyyy.MM.dd');       }
                        if(data == '4') {  viewNal += "취소요청일 : " + new Date(my.fn.toNumber(full.cancel_date) * 1000).format('yyyy.MM.dd');       }

                        return viewNal;
                    }
                },
                {sName: 'payment_status', mData: 'payment_status', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full, dtObj) {
                        if(!full.isProgress) {
                            return "상담완료";
                        } else {
                            var viewStatus = "";
                            if(data == '0') {
                                return '<a href="#" class="payment-cancel" data-payment-srl="' + full.payment_srl + '" data-application-group="' + full.application_group + '" style="cursor: pointer;">' + '취소' + '</a>';
                            }
                            if(data == '1') {
                                var navHash = $(location).attr('hash').substring(1);
                                var moveURL = '${Request.contextPath}/user/payment/refundForm/' + full.payment_srl;
                                return '<a class="datatable-row-detail-view" data-move="' + moveURL + '" data-nav="' + navHash + '" style="cursor: pointer;">' + '환불요청' + '</a>';
                            }
                        }
                    }
                }
                ]
        </#if>
        </@ap.py_jsdatatable>

    });

    $(document).on("click", '.payment-cancel', function() {
        var $this = $(this);

        var dataString = {
            "payment_srl"       : $(this).attr('data-payment-srl'),
            "application_group" : $(this).attr('data-application-group'),
            "payment_status"    : 4,
        };

        $.ajax({
            type: "POST",
            url: '${Request.contextPath}/user/payment/paymentCancel',
            contentType : 'application/json',
            data: JSON.stringify(dataString),
            dataType: 'json',
            success: function (data, status, xhr) {
                $('#pop-message').text("취소 요청이 정상적으로 이루어졌습니다.");
                popup_layer();

                timer = setInterval(function() {
                    my.fn.pmove('${Request.contextPath}/user/payment/cancel_list');
                }, 1500);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $('#pop-message').text("취소 요청이 실패했습니다. 다시 시도해 주세요.");
                popup_layer();
                return;
            }
        });
    });

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
