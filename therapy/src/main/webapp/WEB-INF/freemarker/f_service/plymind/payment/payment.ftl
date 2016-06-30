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
<#assign _page_id="user-payment-result">
<#assign _page_parent_name="">
<#assign _page_current_name="결제확인">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<div class="main-container">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <blockquote>
                    <#if paymentEntity.kind == 1>
                        심리상담 예약완료
                    <#else>
                        심리검사 신청완료
                    </#if>
                </blockquote>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <#if paymentEntity.kind == 1>
                    <h3>플리마인드 심리검사를 신청해 주셔서 감사합니다.</h3>
                    <h3>담당자가 입금 확인 후 입금 확인 문자를 발송해 드리겠습니다.</h3>
                <#else>
                    <h3>결제가 완료 되었습니다.</h3>
                    <h3>상담 예약해 주셔서 감사합니다. 아래 예약내역을 확인해 주세요.</h3>
                </#if>

            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="well-box">
                    <h3>신청 내역</h3>
                    <hr>
                    <div class="row">
                        <div class="col-md-3">선택한 검사 유형</div>
                        <div class="col-md-9">
                            ${paymentEntity.title}
                            <#if (paymentEntity.contents_srl == 1 || paymentEntity.contents_srl == 2) && paymentEntity.advice_period &gt; 0>
                                <#if paymentEntity.advice_period == 1>1주</#if>
                                <#if paymentEntity.advice_period == 4>4주 ( 1개월 ) </#if>
                                <#if paymentEntity.advice_period == 12>12주 ( 3개월 ) </#if>
                                <#if paymentEntity.advice_period == 24>24주 ( 6개월 ) </#if>
                                <#if paymentEntity.advice_period == 52>52주 ( 12개월 ) </#if>
                            </#if>
                            <#if (paymentEntity.contents_srl == 3 || paymentEntity.contents_srl == 4) && paymentEntity.advice_type &gt; 0>
                                <#if paymentEntity.advice_type == 1>으뜸 ${paymentEntity.advice_number}회</#if>
                                <#if paymentEntity.advice_type == 2>가장 ${paymentEntity.advice_number}회</#if>
                                <#if paymentEntity.advice_type == 3>한마루 ${paymentEntity.advice_number}회</#if>
                            </#if>
                            <#if paymentEntity.contents_srl == 5 && paymentEntity.advice_period &gt; 0>
                                <#if paymentEntity.advice_period == 1 && paymentEntity.advice_number == 4>1일 ( ${paymentEntity.advice_number}회 )</#if>
                                <#if paymentEntity.advice_period == 1 && paymentEntity.advice_number == 22>1주 ( ${paymentEntity.advice_number}회 )</#if>
                                <#if paymentEntity.advice_period == 4>4주 ( ${paymentEntity.advice_number}회 ) </#if>
                                <#if paymentEntity.advice_period == 12>12주 ( ${paymentEntity.advice_number}회 ) </#if>
                            </#if>
                        </div>
                    </div>
                    <hr>
                    <div class="row">
                        <div class="col-md-3">휴대전화번호</div>
                        <div class="col-md-9">${paymentEntity.payment_phone}</div>
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
                                <div class="col-md-12"><p>은행명 : 신한은행</p></div>
                            </div>
                            <div class="row">
                                <div class="col-md-12"><p>계좌번호 : 110 - 209 - 646468</p></div>
                            </div>
                            <div class="row">
                                <div class="col-md-12"><p>예금주 : 정혜인 ( 플리마인드 )</p></div>
                            </div>
                            <div class="row">
                                <div class="col-md-12"><p>입금자 : ${paymentEntity.payment_name}</p></div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    신청일 : ${(paymentEntity.application_group * 1000)?number_to_date?string("yyyy년 MM월 dd일")}<br>
                                    <h5>신청일 기준, 다음날까지 미입금 시 자동 취소됩니다.(토, 일, 공휴일 제외)</h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <#if paymentEntity.kind == 1>
        <div class="row">
            <div class="col-md-12">
                <blockquote>
                <#if paymentEntity.contents_srl == 3 || paymentEntity.contents_srl == 4>
                    스카이프케어테라피와 텍스트케어테라피 상담신청 후 과정
                <#elseif paymentEntity.contents_srl == 1 || paymentEntity.contents_srl == 2 || paymentEntity.contents_srl == 5>
                    싸이케어테라피와 컨텐츠케어테라피 상담신청 후 과정
                <#else>

                </#if>
                </blockquote>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="call-to-action well-box" style=" text-align: left;">
                    <#if paymentEntity.contents_srl gt 0 && (paymentEntity.contents_srl == 3 || paymentEntity.contents_srl == 4)>
                        플리마인드에서 스카이프케어테라피와 텍스트케어테라피상담을 신청하시면<br>
                        선택한 사전질문지의 내용을 바탕으로  공감과 지지가 필요한 상담, 문제해결이 필요한 상담, 통찰이 필요한 상담, 적성탐색이 필요한 상담으로 나누어 추천드립니다.<br>
                    <#elseif paymentEntity.contents_srl gt 0 && (paymentEntity.contents_srl == 1 || paymentEntity.contents_srl == 2 || paymentEntity.contents_srl == 5)>
                        플리마인드에서 싸이케어테라피와 컨텐츠케어테라피 상담을 신청하시면<br>
                        선택한 사전질문지의 내용과 동기강화 이론 및 인지행동 이론을 바탕으로 공감과 지지가 필요한 상담, 문제해결이 필요한 상담으로 나누어 추천드립니다.<br>
                    <#else>

                    </#if>
                    <#if paymentEntity.contents_srl gt 0 && paymentEntity.contents_srl lt 5 >
                        당신을 위한 케어테라피는 현재 스트레스 정도와 스트레스 상황에 따라서 어떤 방향성으로 케어테라피를 진행하시는 것이 보다 효과적일 수 있을 지 판단합니다.<br>
                        각각의 방향성은 당신의 현재 문제 영역과 성격적 특성을 바탕으로 진행하는 것이므로 문제 영역이 달라지거나 다른 성격특성을 가진 내담자라면 방향성은 달라질 수 있습니다.<br>
                        <br>
                        따라서 당신의 문제가 무엇인지 어떤 성격 특성을 지니고 있는지 신속한 대처가 필요한 지 등의 몇 가지 조건에 따라서 방향성은 달라지게 되며<br>
                        검사실시후 상담을 진행한다면 좀 더 당신에게 잘 맞는 방향성을 제안받을 수 있습니다.
                    </#if>
                </div>
            </div>
        </div>
        </#if>
        <#--<div class="row">
            <div class="col-md-12">
                <blockquote>
                    진행절차
                </blockquote>
            </div>
        </div>-->

        <div class="row">
            <div class="col-md-12 tp-title-center form-group">
                <button id="btn_ok" name="btn_ok" class="btn tp-btn-default tp-btn-lg">확인</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        $("#btn_ok").click(function(e) {
            <@ap.py_jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/payment/payment_list');
        });
    });

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->