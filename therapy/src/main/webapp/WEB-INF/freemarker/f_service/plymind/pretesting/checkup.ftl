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
<#assign _page_current_name="검사추천">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<div class="main-container">
    <div class="container">

        <div class="row">
            <div class="col-md-12">
                <blockquote>사전 질문지를 통한 검사 추천</blockquote>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <p class="text-center">
                    <#if infoMap.recommendCheckupCode == 100>

                    </#if>
                    <#if infoMap.recommendCheckupCode == 200>
                        ${nickName}님 현재 당신의 스트레스 정도 및 적응<#if infoMap.isHousehold?string == 'true' && infoMap.isPersonal?string == 'true'>
                    ${nickName}님은 가정 영역과 대인관계 영역의 위험도가 감지되고 있습니다. 현재 당신의 스트레스 정도 및 적응경향도를 파악하고 얼마나 빨리 일상생할에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다.
                    <#elseif infoMap.isPrivate?string == 'true' && infoMap.isPersonal?string == 'true'>
                    ${nickName}님은 개인 영역과 대인관계 영역의 위험도가 감지되고 있습니다. 현재 당신의 스트레스 정도 및 적응경향도를 파악하고 얼마나 빨리 일상생할에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다
                    <#elseif infoMap.isPrivate?string == 'true' && infoMap.isEtc?string == 'true'>
                    ${nickName}님은 개인 영역과 기타 영역의 위험도가 감지되고 있습니다. 현재 당신의 스트레스 정도 및 적응경향도를 파악하고 얼마나 빨리 일상생할에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다.
                    <#elseif infoMap.isDependence?string == 'true' && infoMap.isEtc?string == 'true'>
                    ${nickName}님은 중독영역 및 기타영역의 위험도가 감지되고 있습니다. 현재 당신의 스트레스 정도 및 적응경향도를 파악하고 얼마나 빨리 일상생할에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다.
                    <#elseif infoMap.isEmotion?string == 'true' && infoMap.isDependence?string == 'true'>
                    ${nickName}님은 정서와 스트레스 및 중독 영역의 위험도가 감지되고 있습니다. 현재 당신의 스트레스 정도 및 적응경향도를 파악하고 얼마나 빨리 일상생할에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다.
                    <#else>
                    ${nickName}님 현재 당신의 스트레스 정도 및 적응경향도를 파악하고 얼마나 빨리 일상생할에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다.
                    </#if>경향도를 파악하고 얼마나 빨리 학업과 직업영역에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다.
                    </#if>
                    <#if infoMap.recommendCheckupCode == 300>
                        ${nickName}님 현재 당신의 스트레스 정도 및 적응경향도를 파악하고 얼마나 빨리 학업과 직업영역에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다.
                    </#if>
                    <#if infoMap.recommendCheckupCode == 400>
                        ${nickName}님 닉네임님은 학습 및 스트레스 영역의 위험도가 감지되고 있습니다. 현재 당신의 학업 및 직업역량 및 적응경향도를 파악하고 얼마나 빨리 학습 및 직업영역에서 자신의 능력수준을 발휘하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다.
                    </#if>
                    <#if infoMap.recommendCheckupCode == 500>
                        ${nickName}님 닉네임님은 개인영역과 학업 및 직업 영역의 위험도가 감지되고 있습니다. 현재 당신의 스트레스 정도 및 적응경향도를 파악하고 얼마나 빨리 학업과 직업영역에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다. </#if>
                    <#if infoMap.recommendCheckupCode == 600>
                        ${nickName}님 현재 당신의 스트레스 정도 및 적응경향도를 파악하고 얼마나 빨리 학업과 직업영역에서 자신의 능력수준을 회복하는 것이 가능할 지를 알 수 있는 다음의 두 가지 검사를 제안합니다.
                    </#if>
                </p>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-2 col-md-2 col-lg-2"></div>
            <#if infoMap.recommendCheckupCode == 100>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                   스트레스적응 검사
                                </p>
                                지금 당신에게는...<br>
                                나의 스트레스 위험 영역을 알려줄 수 있는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="22">신청하기</button>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    회복탄력성 검사
                                </p>
                                지금 당신에게는...<br>
                                위급한 스트레스를 어떻게 대처하고 자신의 역량을 발휘하는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="25">신청하기</button>
                        </div>
                    </div>
                </div>
            </#if>
            <#if infoMap.recommendCheckupCode == 200>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    스트레스적응 검사
                                </p>
                                지금 당신에게는...<br>
                                나의 스트레스 위험 영역을 알려줄 수 있는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="22">신청하기</button>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    학습 검사
                                </p>
                                지금 당신에게는...<br>
                                나의 학업 및 직업영역에 도움을 줄 수 있는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="23">신청하기</button>
                        </div>
                    </div>
                </div>
            </#if>
            <#if infoMap.recommendCheckupCode == 300>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    스트레스적응 검사
                                </p>
                                지금 당신에게는...<br>
                                나의 스트레스 위험 영역을 알려줄 수 있는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="22">신청하기</button>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    창의성 검사
                                </p>
                                지금 당신에게는...<br>
                                내가 모르고 있었던 자신의 역량을 찾아 줄수 있는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="24">신청하기</button>
                        </div>
                    </div>
                </div>
            </#if>
            <#if infoMap.recommendCheckupCode == 400>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    학습 검사
                                </p>
                                지금 당신에게는...<br>
                                나의 학업 및 직업영역에 도움을 줄 수 있는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="23">신청하기</button>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    창의성 검사
                                </p>
                                지금 당신에게는...<br>
                                내가 모르고 있었던 자신의 역량을 찾아 줄수 있는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="24">신청하기</button>
                        </div>
                    </div>
                </div>

            </#if>
            <#if infoMap.recommendCheckupCode == 500>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    학습 검사
                                </p>
                                지금 당신에게는...<br>
                                나의 학업 및 직업영역에 도움을 줄 수 있는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="23">신청하기</button>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    회복탄력성 검사
                                </p>
                                지금 당신에게는...<br>
                                위급한 스트레스를 어떻게 대처하고 자신의 역량을 발휘하는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="25">신청하기</button>
                        </div>
                    </div>
                </div>
            </#if>
            <#if infoMap.recommendCheckupCode == 600>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    창의성 검사
                                </p>
                                지금 당신에게는...<br>
                                내가 모르고 있었던 자신의 역량을 찾아 줄수 있는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="24">신청하기</button>
                        </div>
                    </div>
                </div>
                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="well">
                        <div class="row">
                            <div class="col-md-12">
                                <p class="tp-title-center">
                                    회복탄력성 검사
                                </p>
                                지금 당신에게는...<br>
                                위급한 스트레스를 어떻게 대처하고 자신의 역량을 발휘하는 검사가 필요합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 tp-title-center form-group">
                            <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="25">신청하기</button>
                        </div>
                    </div>
                </div>
            </#if>
            <div class="col-sm-2 col-md-2 col-lg-2"></div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('.btn-application-add').click(function(e) {
            var $this = $(this);

            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/product/checkupAddForm/' + $this.attr('data-product-srl'));
        });
    });
</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->