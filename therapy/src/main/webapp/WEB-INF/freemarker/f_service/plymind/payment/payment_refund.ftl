<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js"></script>
<script src="${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js"></script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-payment-canecl">
<#assign _page_parent_name="">
<#assign _page_current_name="결제취소">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<div class="main-container">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h3>담당자가 무통장 입금 내역 확인 후 입금 확인 문자를 드리겠습니다.</h3>
                <h3>입금확인 후 예약한 케어테라피를 진행하실 수 있습니다.</h3>
            </div>
        </div>

        <blockquote>무통장입금 결제정보</blockquote>
        <hr>
        <div class="row">
            <div class="col-md-3">선택한 검사 유형</div>
            <div class="col-md-9">
                ${paymentEntity.title}
                <#if paymentEntity.advice_type &gt; 0>
                    <#if paymentEntity.advice_type == 1>으뜸 </#if>
                    <#if paymentEntity.advice_type == 2>가장 </#if>
                    <#if paymentEntity.advice_type == 3>한마루 </#if>
                </#if>
                <#if paymentEntity.advice_period &gt; 0>
                    <#if paymentEntity.advice_period == 1 && paymentEntity.advice_number == 4>1일 ( 4회 )</#if>
                    <#if paymentEntity.advice_period == 1 && paymentEntity.advice_number == 22>1주</#if>
                    <#if paymentEntity.advice_period == 1 && paymentEntity.advice_number == 1>1주</#if>
                    <#if paymentEntity.advice_period == 4>4주 ( 1개월 ) </#if>
                    <#if paymentEntity.advice_period == 12>12주 ( 3개월 ) </#if>
                    <#if paymentEntity.advice_period == 24>24주 ( 6개월 ) </#if>
                    <#if paymentEntity.advice_period == 52>52주 ( 12개월 ) </#if>
                </#if>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">입금 예정 금액</div>
            <div class="col-md-9">${paymentEntity.price?string.number} 원</div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">무통장 입금 결제정보</div>
            <div class="col-md-9">
                <div class="row">
                    <div class="col-md-12">은행명 : 신한은행</div>
                </div>
                <div class="row">
                    <div class="col-md-12">계좌번호 : 110 - 209 - 646468</div>
                </div>
                <div class="row">
                    <div class="col-md-12">예금주 : 정혜인 ( 플리마인드 )</div>
                </div>
                <div class="row">
                    <div class="col-md-12">입금자 : ${paymentEntity.payment_name}</div>
                </div>
                <div class="row">
                    <div class="col-md-12">전화번호 : ${paymentEntity.payment_phone}</div>
                </div>
            </div>
        </div>
        <hr>
        <blockquote>환불 계좌 정보</blockquote>
        <hr>
        <div class="row">
            <div class="col-md-3">환불은행</div>
            <div class="col-md-9">
                <input type="text" id="refund_bank" name="refund_bank" value="${paymentEntity.refund_bank}"/>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">계좌번호</div>
            <div class="col-md-9">
                <input type="text" id="refund_account" name="refund_account" value="${paymentEntity.refund_account}"/>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">예금주</div>
            <div class="col-md-9">
                <input type="text" id="refund_name" name="refund_name" value="${paymentEntity.refund_name}"/>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-12 tp-title-center form-group">
                <button id="btn_refund" name="btn_refund" class="btn tp-btn-default tp-btn-lg">환불 요청</button>
                <button id="btn_list" name="btn_list" class="btn tp-btn-default tp-btn-lg">목록</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        $("#btn_refund").click(function() {
            if($.trim($('#refund_bank').val()) == '') {
                $('#pop-message').text("환불은행을 입력해 주세요.");
                popup_layer();
                return;
            }

            if($.trim($('#refund_account').val()) == '') {
                $('#pop-message').text("계좌번호를 입력해 주세요.");
                popup_layer();
                return;
            }

            if($.trim($('#refund_name').val()) == '') {
                $('#pop-message').text("예금주를 입력해 주세요.");
                popup_layer();
                return;
            }

            var dataString = {
                "payment_srl"       : ${paymentEntity.payment_srl},
                "application_group" : ${paymentEntity.application_group},
                "payment_status"    : 2,
                "refund_bank"       : $.trim($('#refund_bank').val()),
                "refund_account"    : $.trim($('#refund_account').val()),
                "refund_name"       : $.trim($('#refund_name').val())
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/payment/paymentRefund',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    $('#pop-message').text("환불요청이 정상적으로 이루어졌습니다.");
                    popup_layer();

                    timer = setInterval(function() {
                        my.fn.pmove('${Request.contextPath}/user/payment/cancel_list');
                    }, 1500);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("환불요청이 실패했습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });

        });

        $("#btn_list").click(function(e) {
            <@ap.py_jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/payment/payment_list');
        });
    });

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->