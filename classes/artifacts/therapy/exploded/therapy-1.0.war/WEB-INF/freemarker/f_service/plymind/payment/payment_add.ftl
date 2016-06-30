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
<#assign _page_id="user-payment-add">
<#assign _page_parent_name="">
<#assign _page_current_name="결제">
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
                ${applicationEntity.title}
                <#if (applicationEntity.contents_srl == 1 || applicationEntity.contents_srl == 2) && applicationEntity.advice_period &gt; 0>
                    <#if applicationEntity.advice_period == 1>1주</#if>
                    <#if applicationEntity.advice_period == 4>4주 ( 1개월 ) </#if>
                    <#if applicationEntity.advice_period == 12>12주 ( 3개월 ) </#if>
                    <#if applicationEntity.advice_period == 24>24주 ( 6개월 ) </#if>
                    <#if applicationEntity.advice_period == 52>52주 ( 12개월 ) </#if>
                </#if>
                <#if (applicationEntity.contents_srl == 3 || applicationEntity.contents_srl == 4) && applicationEntity.advice_type &gt; 0>
                    <#if applicationEntity.advice_type == 1>으뜸 ${applicationEntity.advice_number}회</#if>
                    <#if applicationEntity.advice_type == 2>가장 ${applicationEntity.advice_number}회</#if>
                    <#if applicationEntity.advice_type == 3>한마루 ${applicationEntity.advice_number}회</#if>
                </#if>
                <#if applicationEntity.contents_srl == 5 && applicationEntity.advice_period &gt; 0>
                    <#if applicationEntity.advice_period == 1 && applicationEntity.advice_number == 4>1일 ( ${applicationEntity.advice_number}회 )</#if>
                    <#if applicationEntity.advice_period == 1 && applicationEntity.advice_number == 22>1주 ( ${applicationEntity.advice_number}회 )</#if>
                    <#if applicationEntity.advice_period == 4>4주 ( ${applicationEntity.advice_number}회 ) </#if>
                    <#if applicationEntity.advice_period == 12>12주 ( ${applicationEntity.advice_number}회 ) </#if>
                </#if>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">입금 예정 금액</div>
            <div class="col-md-9">${price?string.number} 원</div>
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
                    <div class="col-md-12">
                        입금자 <input type="text" id="payment_name" name="payment_name" calss="form-group"/>
                        <h5>입금자명이 다를 경우 입금 확인니 오래 걸릴 수 있사오니 정확히 입력해 주시기 바랍니다.</h5>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        입금기한 : 신청일 기준, 다음날까지 미입금 시 자동 취소됩니다. ( 휴일 / 공휴일 제외 )
                    </div>
                </div>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">휴대전화번호</div>
            <div class="col-md-9">
                <input type="text" id="payment_phone" name="payment_phone" placeholder=" -없이 입력" maxlength="11"/>
            </div>
        </div>
        <hr>

        <blockquote>현금영수증</blockquote>
        <hr>
        <div class="row">
            <div class="col-md-12">
                <label for="receipt_kind_1"><input type="radio" name="receipt_kind" id="receipt_kind_1" value="1" class="receipt-kind"> 소득공제용 ( 개인 )</label>&nbsp;&nbsp;&nbsp;
                <label for="receipt_kind_2"><input type="radio" name="receipt_kind" id="receipt_kind_2" value="2" class="receipt-kind"> 지출증빙용 ( 사업자 )</label>&nbsp;&nbsp;&nbsp;
                <label for="receipt_kind_3"><input type="radio" name="receipt_kind" id="receipt_kind_3" value="3" class="receipt-kind" checked> 신청안함</label>
            </div>
        </div>
        <hr>
        <div id="receipt_phone_area">
            <div class="row">
                <div class="col-md-12">
                    휴대전화번호
                    <input type="text" id="receipt_phone" name="receipt_phone" placeholder=" -없이 입력" maxlength="11"/>
                </div>
            </div>
            <hr>
        </div>

        <blockquote>환불 계좌 정보</blockquote>
        <h5>환불계좌 정보는 필수 입력 사항이 아닙니다. 환불요청 시 입력 가능합니다.</h5>
        <hr>
        <div class="row">
            <div class="col-md-3">환불은행</div>
            <div class="col-md-9">
                <input type="text" id="refund_bank" name="refund_bank"/>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">계좌번호</div>
            <div class="col-md-9">
                <input type="text" id="refund_account" name="refund_account"/>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">예금주</div>
            <div class="col-md-9">
                <input type="text" id="refund_name" name="refund_name"/>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-12 tp-title-center form-group">
                <button id="btn_application" name="btn_application" class="btn tp-btn-default tp-btn-lg">결제정보 등록</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        $('#receipt_phone_area').hide();

        $(".receipt-kind").click(function() {
            var receipt_kind = 0;
            $('input:radio[name="receipt_kind"]:checked').each(function() {
                receipt_kind = this.value;
            });

            if(receipt_kind == 3) {
                $('#receipt_phone_area').hide();
            } else {
                $('#receipt_phone_area').show();
            }
        });

        $("#btn_application").click(function() {
            var receipt_kind = 0;
            $('input:radio[name="receipt_kind"]:checked').each(function() {
                receipt_kind = this.value;
            });

            var payment_name = $.trim($('#payment_name').val());
            var payment_phone = $.trim($('input[name=payment_phone]').val());
            var receipt_phone = $.trim($('input[name=receipt_phone]').val());

            if(payment_name == '') {
                $('#pop-message').text("입금자명을 입력해 주세요.");
                popup_layer();
                return;
            }

            if(payment_phone == '') {
                $('#pop-message').text("입금자 휴대폰번호를 입력해 주세요.");
                popup_layer();
                return;
            }

            if(payment_phone.length < 10 || payment_phone.length > 11) {
                $('#pop-message').text("입금자 휴대폰번호를 확인해 주세요.");
                popup_layer();
                return;
            }

            if(receipt_kind == 0) {
                $('#pop-message').text("현금영수증 정보를 선택해 주세요.");
                popup_layer();
                return;
            }

            if(!(receipt_kind == 0 || receipt_kind == 3) && receipt_phone == '') {
                $('#pop-message').text("현금영수증 휴대폰번호를 입력해 주세요.");
                popup_layer();
                return;
            }

            if(!(receipt_kind == 0 || receipt_kind == 3 || receipt_phone == '') && (receipt_phone.length < 10 || receipt_phone.length > 11)) {
                $('#pop-message').text("현금영수증 휴대폰번호를 확인해 주세요.");
                popup_layer();
                return;
            }

            var dataString = {
                "product_srl"       : ${applicationEntity.product_srl},
                "application_group" : ${applicationEntity.application_group},
                "payment_name"      : payment_name,
                "payment_phone"     : payment_phone,
                "price"             : ${price},
                "receipt_kind"      : receipt_kind,
                "receipt_phone"     : receipt_phone,
                "payment_status"    : 0,
                "refund_bank"       : $.trim($('#refund_bank').val()),
                "refund_account"    : $.trim($('#refund_account').val()),
                "refund_name"       : $.trim($('#refund_name').val())
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/payment/paymentAdd',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    if(data.resultMap.result_code == 1) {
                        $('#pop-message').text("결제 정보가 정상적으로 등록되었습니다.");
                    } else {
                        $('#pop-message').text("이미 저장된 결제 정보입니다.");
                    }
                    popup_layer();

                    timer = setInterval(function() {
                        my.fn.pmove('${Request.contextPath}/user/payment/paymentResult/' + data.resultMap.payment_srl);
                    }, 1500);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("결제 정보 저장이 실패 했습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });
        });
    });

    <#-- 입금자 휴대폰번호 제어 START -->
    $(document).on("keyup", '#payment_phone', function() {
        $(this).val( $(this).val().replace(/[^0-9]/gi, "") );
    });
    <#-- 입금자 휴대폰번호 제어 END -->

    <#-- 현금영수증 휴대폰번호 제어 START -->
    $(document).on("keyup", '#receipt_phone', function() {
        $(this).val( $(this).val().replace(/[^0-9]/gi, "") );
    });
    <#-- 현금영수증 휴대폰번호 제어 END -->

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->