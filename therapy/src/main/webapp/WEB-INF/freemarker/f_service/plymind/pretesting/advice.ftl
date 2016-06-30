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
<#assign _page_current_name="상담추천">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<div class="main-container">
    <div class="container">

        <div class="row">
            <div class="col-md-12">
                <blockquote>사전질문지를 통한 상담 및 상담사 추천</blockquote>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <p>
                    다른 사람에게 좋았다고 해서, 그 상담사가 나에게도 좋으리란 법은 없습니다.
                    나에게 필요한 상담사를 만나는게 가장 중요합니다.
                </p>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-12 text-center">
                <h3>심리상담 추천</h3>

                <div class="row">
                    <div class="col-md-12">
                        <#if infoMap.recommendAdvisorCode == 100 || infoMap.recommendAdvisorCode == 200 || infoMap.recommendAdvisorCode == 300>
                        <label for="contents_srl_1">
                            <input type="radio" name="contents_srl" id="contents_srl_1" value="1" class="contents-srl"> 싸이케어테라피
                        </label>&nbsp;&nbsp;&nbsp;
                        </#if>

                        <#if infoMap.recommendAdvisorCode == 400 || infoMap.recommendAdvisorCode == 600>
                        <label for="contents_srl_2">
                            <input type="radio" name="contents_srl" id="contents_srl_2" value="2" class="contents-srl"> 커플/패밀리케어테라피
                        </label>&nbsp;&nbsp;&nbsp;
                        </#if>

                        <#if infoMap.recommendAdvisorCode == 100 || infoMap.recommendAdvisorCode == 200 || infoMap.recommendAdvisorCode == 400 || infoMap.recommendAdvisorCode == 500>
                        <label for="contents_srl_3">
                            <input type="radio" name="contents_srl" id="contents_srl_3" value="3" class="contents-srl"> 스카이프케어테라피
                        </label>&nbsp;&nbsp;&nbsp;
                        </#if>

                        <#if infoMap.recommendAdvisorCode == 100 || infoMap.recommendAdvisorCode == 300 || infoMap.recommendAdvisorCode == 400
                                                                 || infoMap.recommendAdvisorCode == 500 || infoMap.recommendAdvisorCode == 600>
                        <label for="contents_srl_4">
                            <input type="radio" name="contents_srl" id="contents_srl_4" value="4" class="contents-srl"> 텍스트케어테라피
                        </label>&nbsp;&nbsp;&nbsp;
                        </#if>

                        <#if infoMap.recommendAdvisorCode == 200 || infoMap.recommendAdvisorCode == 300 || infoMap.recommendAdvisorCode == 500 || infoMap.recommendAdvisorCode == 600>
                        <label for="contents_srl_5">
                            <input type="radio" name="contents_srl" id="contents_srl_5" value="5" class="contents-srl"> 컨텐츠케어테라피
                        </label>&nbsp;&nbsp;&nbsp;
                        </#if>
                    </div>
                </div>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-12 text-center">
                <h3>상담사 추천</h3>

                <div class="row">
                    <div class="col-sm-2 col-md-2 col-lg-2"></div>
                    <#if infoMap.recommendAdvisorCode == 100 || infoMap.recommendAdvisorCode == 200 || infoMap.recommendAdvisorCode == 300>
                    <div class="col-sm-4 col-md-4 col-lg-4">
                        <div class="well">
                            <div class="row">
                                <div class="col-md-12">
                                    <p class="tp-title-center">
                                        공감상담사
                                    </p>
                                    지금 당신에게는...<br>
                                    깊은 이해와 공감이 필요할 때<br>
                                    조용히 들어주고 자상하게 어루만져주는 상담사가 필요합니다.
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 tp-title-center form-group">
                                <label for="advisor_domain_srl_1">
                                    <input type="radio" name="advisor_domain_srl" id="advisor_domain_srl_1" value="1" class="advisor-domain">
                                </label>&nbsp;&nbsp;&nbsp;
                            </div>
                        </div>
                    </div>
                    </#if>

                    <#if infoMap.recommendAdvisorCode == 100 || infoMap.recommendAdvisorCode == 400 || infoMap.recommendAdvisorCode == 600>
                    <div class="col-sm-4 col-md-4 col-lg-4">
                        <div class="well">
                            <div class="row">
                                <div class="col-md-12">
                                    <p class="tp-title-center">
                                        문제해결 상담사
                                    </p>
                                    지금 당신에게는...<br>
                                    문제를 명확히 보고 생각을 정리해야 할 때<br>
                                    나의 생각을 정리하고 안내할 상담사가 필요합니다.
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 tp-title-center form-group">
                                <label for="advisor_domain_srl_2">
                                    <input type="radio" name="advisor_domain_srl" id="advisor_domain_srl_2" value="2" class="advisor-domain">
                                </label>&nbsp;&nbsp;&nbsp;
                            </div>
                        </div>
                    </div>
                    </#if>

                    <#if infoMap.recommendAdvisorCode == 200 || infoMap.recommendAdvisorCode == 400 || infoMap.recommendAdvisorCode == 500>
                    <div class="col-sm-4 col-md-4 col-lg-4">
                        <div class="well">
                            <div class="row">
                                <div class="col-md-12">
                                    <p class="tp-title-center">
                                        적성상담사
                                    </p>
                                    지금 당신에게는...<br>
                                    나의 적성과 학습 및 직업 등에 따른 문제에 도울을 줄 상담사가 필요합니다.<br><br>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 tp-title-center form-group">
                                <div class="col-md-12 tp-title-center form-group">
                                    <label for="advisor_domain_srl_3">
                                        <input type="radio" name="advisor_domain_srl" id="advisor_domain_srl_3" value="3" class="advisor-domain">
                                    </label>&nbsp;&nbsp;&nbsp;
                                </div>
                            </div>
                        </div>
                    </div>
                    </#if>

                    <#if infoMap.recommendAdvisorCode == 300 || infoMap.recommendAdvisorCode == 500 || infoMap.recommendAdvisorCode == 600>
                    <div class="col-sm-4 col-md-4 col-lg-4">
                        <div class="well">
                            <div class="row">
                                <div class="col-md-12">
                                    <p class="tp-title-center">
                                        통찰상담사
                                    </p>
                                    지금 당신에게는...<br>
                                    자신이 알고 있는 연결고리를 함께 풀어갈 상담사가 필요합니다.<br><br>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 tp-title-center form-group">
                                <label for="advisor_domain_srl_4">
                                    <input type="radio" name="advisor_domain_srl" id="advisor_domain_srl_4" value="4" class="advisor-domain">
                                </label>&nbsp;&nbsp;&nbsp;
                            </div>
                        </div>
                    </div>
                    </#if>
                    <div class="col-sm-2 col-md-2 col-lg-2"></div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 tp-title-center form-group">
                    <button class="btn tp-btn-default tp-btn-lg btn-application-add" data-product-srl="">신청하기</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $('.btn-application-add').click(function(e) {
            var contents_srl = 0;
            $('input:radio[name="contents_srl"]:checked').each(function() {
                contents_srl = this.value;
            });

            var advisor_type = 0;
            $('input:radio[name="advisor_domain_srl"]:checked').each(function() {
                advisor_type = this.value;
            });

            if(contents_srl == 0) {
                $('#pop-message').text("추천 상담을 선택해 주세요.");
                popup_layer();
                return;
            }

            if(advisor_type == 0) {
                $('#pop-message').text("추천 상담사를 선택해 주세요.");
                popup_layer();
                return;
            }


            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/product/adviceAddForm/' + contents_srl + '/0/0/' + advisor_type);
        });
    });

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->