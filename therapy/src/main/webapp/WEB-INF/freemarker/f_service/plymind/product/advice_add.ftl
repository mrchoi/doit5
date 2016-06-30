<@ap.py_header_load_script />
<link rel="stylesheet" type="text/css" media="screen" href="${Request.svcResPath}css/smartadmin-production-plugins.min.css">
<link rel="stylesheet" type="text/css" media="screen" href="${Request.svcResPath}css/smartadmin-production.min.css">
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
<#assign _page_id="user-advice-application">
<#assign _page_parent_name="">
<#assign _page_current_name="심리상담 신청">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<style>
    #calendar table {
        border-collapse: collapse;
        text-align: center;
    }
    #calendar table thead td {
        height: 30px;
        font-weight: bold;
    }
    #calendar table td {
        border: solid 1px #ddd;
    }
    #calendar table td.date-cell {
        height: 40px;
    }
    #calendar table td.sun {
        color: red;
    }
    #calendar table td.sat {
        color: blue;
    }
    #calendar table td.not-this-month {
        background: #ddd;
        color: #999;
    }
    #calendar table td.not-select {
        background: red;
        color: #ddd;
    }
    #calendar table td.possible {
        cursor: pointer;
    }
    .impossible {
        background: #ff99cc;
    }
</style>
<div class="main-container">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <blockquote><a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">심리상담 진행 절차</a></blockquote>
            </div>
        </div>

        <div id="collapseOne" class="row panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
            <div class="col-md-12">
                <div class="well-box">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP1. 인테이크(사전질문지) : 현재 내담자의 문제나 성격에 따라 상담 영역과 추천되는 테라피의 종류가 달라지면 미실시 시 제한점이 있습니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP2. 상담 선택 : 내담자가 원하는 테라피를 선택합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP3. 상담기간 및 상담사 등을 선택합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP4. 상담비용 입금 : 내담자가 테라피에 따라 상이한 상담 비용을 확인하고 입금계좌 확인 후 입금합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP5. 입금 확인 안내 : 운영자가 입금 확인 후 상담안내 문자를 발송합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP6. 상담 실시 : 내담자의 선택에 따른 케어테라피를 진행합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP7. 종결 : 내담자가 10회기 이상 상담을 진행할 시에는 종결에 따른 보고서를 제공받을 수 있습니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <h5>진행과정에서의 의문사항이 발생할 경우 Q&A나 문의 게시판을 이용해 주세요.</h5>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <blockquote>심리상담 신청</blockquote>
            </div>
        </div>
        <div class="st-tabs">
            <div class="row">
                <div class="col-md-12">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="contents-srl active" value="1" id="tab-1"><a href="#single_therapy" aria-controls="single_therapy" role="tab" data-toggle="tab">싸이케어테라피</a></li>
                        <li role="presentation" class="contents-srl" value="2" id="tab-2"><a href="#couple_therapy" aria-controls="couple_therapy" role="tab" data-toggle="tab">커플/패밀리싸이케어테라피</a></li>
                        <li role="presentation" class="contents-srl" value="3" id="tab-3"><a href="#text_skype_therapy" aria-controls="text_skype_therapy" role="tab" data-toggle="tab">스카이프케어테라피</a></li>
                        <li role="presentation" class="contents-srl" value="4" id="tab-4"><a href="#text_skype_therapy" aria-controls="text_skype_therapy" role="tab" data-toggle="tab">텍스트케어테라피</a></li>
                        <li role="presentation" class="contents-srl" value="5" id="tab-5"><a href="#contents_therapy" aria-controls="contents_therapy" role="tab" data-toggle="tab">컨텐츠케어테라피</a></li>
                    </ul>

                    <!-- Tab panes -->
                    <div class="tab-content">
                        <!-- 싸이케어세라피  -------------------------------------->
                        <div role="tabpanel" class="tab-pane active" id="single_therapy" data-contents-srl="1">
                            <div class="row col-md-12">
                                <div class="row">
                                    <div class="col-md-12">
                                        연령과 직업, 학습 및 업무습관, 생활습관, 기호식품, 라이프스타일 등을 고려한 스마트 프로그램을 제공 받습니다.
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담기간 선택</div>
                                    <div class="col-md-9 inline-group">
                                        <label for="single_advice_period_1"><input type="radio" name="single_advice_period" id="single_advice_period_1" value="4" class="single-advice-period" checked> 4주(1개월)</label>&nbsp;&nbsp;&nbsp;
                                        <label for="single_advice_period_2"><input type="radio" name="single_advice_period" id="single_advice_period_2" value="12" class="single-advice-period"> 12주(3개월)</label>&nbsp;&nbsp;&nbsp;
                                        <label for="single_advice_period_3"><input type="radio" name="single_advice_period" id="single_advice_period_3" value="24" class="single-advice-period"> 24주(6개월)</label>&nbsp;&nbsp;&nbsp;
                                        <label for="single_advice_period_4"><input type="radio" name="single_advice_period" id="single_advice_period_4" value="52" class="single-advice-period"> 52주(12개월)</label>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담 요금 안내</div>
                                    <div class="col-md-9"><span id="single_price"></span>원 ( 스마트밴드 기기 포함 )</div>
                                </div>
                                <hr>
                                <#--
                                <div class="row">
                                    <div class="col-md-3"> 상담 시작일 지정</div>
                                    <div class="col-md-9">
                                        <input type="text" id="single_advice_start_date" name="single_advice_start_date"/>
                                    </div>
                                </div>
                                <hr>
                                -->
                                <div class="row">
                                    <div class="col-md-3">수령인 이름</div>
                                    <div class="col-md-9">
                                        <input type="text" id="single_receive_name" name="single_receive_name"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">수령인 휴대폰번호</div>
                                    <div class="col-md-9">
                                        <input type="text" id="single_receive_phone" name="single_receive_phone" placeholder=" -없이 입력" maxlength="11"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">수령지 주소</div>
                                    <div class="col-md-9">
                                        <input type="text" id="single_receive_address" name="single_receive_address" style='width:80%' readonly/>
                                        <button id="zipcodetext" class="btn btn-default" data-toggle="modal" data-target="#zipcode" data-box-number="1">주소검색</button>
                                    </div>
                                </div>
                                <br>
                                <div class="row">
                                    <div class="col-md-3"></div>
                                    <div class="col-md-9">
                                        <input type="text" id="single_receive_address_plus" name="single_receive_address_plus" placeholder="상세주소 입력" style='width:80%'/>
                                    </div>
                                </div>
                                <hr>
                                <h5>스마트밴드 기기를 수령할 수령인 이름. 배송 받을 주소 및 휴대폰번호를 정확하게 입력해 주세요.</h5>
                            </div>
                            <div class="row">
                                <div class="col-md-12 tp-title-center form-group">
                                    <#if checkMap.checkUserInfo?string != "true" && checkMap.checkPretesting?string != "true">
                                        <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo-pretesting">상담 예약하기</button>
                                    <#elseif checkMap.checkUserInfo?string != "true">
                                        <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo">상담 예약하기</button>
                                    <#elseif checkMap.checkPretesting?string != "true">
                                        <button class="btn tp-btn-default tp-btn-lg btn-precheck-pretesting">상담 예약하기</button>
                                    <#else>
                                        <button id="btn_single_therapy" class="btn tp-btn-default tp-btn-lg">상담 예약하기</button>
                                    </#if>
                                </div>
                            </div>
                        </div>
                        <!-- 커플싸이케어세라피 / 패밀리싸이케어세라피 -------------------------------------->
                        <div role="tabpanel" class="tab-pane" id="couple_therapy" data-contents-srl="2">
                            <div class="row col-md-12">
                                <div class="row">
                                    <div class="col-md-12">
                                        부모와 자녀, 부부, 연인, 친구, 가족 간에 서로 마인드가 전하는 메세지를 통해서 관계 갈등이나 어려움 등을 극복할 수 있도록 도와드립니다.
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담기간 선택</div>
                                    <div class="col-md-9 inline-group">
                                        <label for="couple_advice_period_1"><input type="radio" name="couple_advice_period" id="couple_advice_period_1" value="4" class="couple-advice-period" checked> 4주(1개월)</label>&nbsp;&nbsp;&nbsp;
                                        <label for="couple_advice_period_2"><input type="radio" name="couple_advice_period" id="couple_advice_period_2" value="12" class="couple-advice-period"> 12주(3개월)</label>&nbsp;&nbsp;&nbsp;
                                        <label for="couple_advice_period_3"><input type="radio" name="couple_advice_period" id="couple_advice_period_3" value="24" class="couple-advice-period"> 24주(6개월)</label>&nbsp;&nbsp;&nbsp;
                                        <label for="couple_advice_period_4"><input type="radio" name="couple_advice_period" id="couple_advice_period_4" value="52" class="couple-advice-period"> 52주(12개월)</label>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담 요금 안내</div>
                                    <div class="col-md-9"><span id="couple_price"></span>원 ( 스마트밴드 기기 포함 )</div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">추가 인원</div>
                                    <div class="col-md-3">
                                        <select id="couple_person_select" name="couple_person_select">
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                            <option value="8">8</option>
                                            <option value="9">9</option>
                                            <option value="10">10</option>
                                        </select> 명
                                    </div>
                                    <div class="col-md-6">
                                        <span id="person_area">
                                            <div class="row col-md-12 form-group">
                                                <input type="text" id="couple_member_id1" name="couple_member_id" data-member-srl="" readonly/>
                                                <button class="btn couple-member-search" data-box-number="1">검색</button>
                                            </div>
                                        </span>
                                    </div>
                                </div>
                                <hr>
                                <#--
                                <div class="row">
                                    <div class="col-md-3"> 상담 시작일 지정</div>
                                    <div class="col-md-9">
                                        <input type="text" id="couple_advice_start_date" name="couple_advice_start_date"/>
                                    </div>
                                </div>
                                <hr>
                                -->
                                <div class="row">
                                    <div class="col-md-3">수령인 이름</div>
                                    <div class="col-md-9">
                                        <input type="text" id="couple_receive_name" name="couple_receive_name"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">수령인 휴대폰번호</div>
                                    <div class="col-md-9">
                                        <input type="text" id="couple_receive_phone" name="couple_receive_phone" placeholder=" -없이 입력"  maxlength="11"/>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">수령지 주소</div>
                                    <div class="col-md-9">
                                        <input type="text" id="couple_receive_address" name="couple_receive_address" style='width:80%' readonly/>
                                        <button id="zipcodetext" class="btn btn-default" data-toggle="modal" data-target="#zip" data-box-number="1">주소검색</button>
                                    </div>
                                </div>
                                <br>
                                <div class="row">
                                    <div class="col-md-3"></div>
                                    <div class="col-md-9">
                                        <input type="text" id="couple_receive_address_plus" name="couple_receive_address_plus" placeholder="상세주소 입력" style='width:80%'/>
                                    </div>
                                </div>
                                <hr>
                                <h5>스마트밴드 기기를 수령할 수령인 이름. 배송 받을 주소 및 휴대폰번호를 정확하게 입력해 주세요.</h5>
                            </div>
                            <div class="row">
                                <div class="col-md-12 tp-title-center form-group">
                                <#if checkMap.checkUserInfo?string != "true" && checkMap.checkPretesting?string != "true">
                                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo-pretesting">상담 예약하기</button>
                                <#elseif checkMap.checkUserInfo?string != "true">
                                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo">상담 예약하기</button>
                                <#elseif checkMap.checkPretesting?string != "true">
                                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-pretesting">상담 예약하기</button>
                                <#else>
                                    <button id="btn_couple_therapy" class="btn tp-btn-default tp-btn-lg">상담 예약하기</button>
                                </#if>
                                </div>
                            </div>
                        </div>
                        <!-- 텍스트케어세라피 / 스카이프케어세라피 --------------------------------------------------------->
                        <div role="tabpanel" class="tab-pane" id="text_skype_therapy" data-contents-srl="3">
                            <div class="row col-md-12">
                                <div class="row">
                                    <div class="col-md-12" id="view_desc"></div>
                                </div>
                                <hr>
                                <h3>1. 상담 횟수를 선택해 주세요.</h3>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담유형 선택</div>
                                    <div class="col-md-9 inline-group">
                                        <label for="ts_advice_type_1"><input type="radio" name="ts_advice_type" id="ts_advice_type_1" value="1" class="styled ts-advice-type" checked> 으뜸</label>&nbsp;&nbsp;&nbsp;
                                        <label for="ts_advice_type_2"><input type="radio" name="ts_advice_type" id="ts_advice_type_2" value="2" class="styled ts-advice-type"> 가장</label>&nbsp;&nbsp;&nbsp;
                                        <label for="ts_advice_type_3"><input type="radio" name="ts_advice_type" id="ts_advice_type_3" value="3" class="styled ts-advice-type"> 한마루</label>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담횟수 선택</div>
                                    <div class="col-md-9 inline-group">
                                        <label for="ts_advice_number_1"><input type="radio" name="ts_advice_number" id="ts_advice_number_1" value="1" class="styled ts-advice-number" checked> 1회</label>&nbsp;&nbsp;&nbsp;
                                        <label for="ts_advice_number_2"><input type="radio" name="ts_advice_number" id="ts_advice_number_2" value="10" class="styled ts-advice-number"> 10회</label>&nbsp;&nbsp;&nbsp;
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담 요금안내</div>
                                    <div class="col-md-9"><span id="ts_price"></span>원 ( 총 <span id="ts_cnt"></span>회 )</div>
                                </div>
                                <hr>
                            </div>
                            <div class="row col-md-12">
                                <h3>2. 상담사를 선택해 주세요.</h3>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담사 선택</div>
                                    <div class="col-md-9" id="advisor-area">

                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담사 소개</div>
                                    <div class="col-md-9" id="advisor-desc-area">

                                    </div>
                                </div>
                                <hr>
                            </div>
                            <div id="calendar_area" class="row col-md-12">
                                <h3>3. 상담 가능한 날짜 및 시간을 선택해 주세요.</h3>
                                <hr>
                                <span id="cal_advisor_name"></span> 상담사 상담 가능한 날짜
                                <div class="row" >
                                    <div class="col-md-12">
                                        <div class="row inline-group">
                                            <div class="col-md-4"></div>
                                            <div class="col-md-1 tp-title-center"><button id='btnPrev'><</button></div>
                                            <div class="col-md-2 tp-title-center"><span id='currentDate'></span></div>
                                            <div class="col-md-1 tp-title-center"><button id='btnNext'>></button></div>
                                            <div class="col-md-4"></div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div style="display: inline-block; border: solid 1px #000; background-color: azure; width: 12px; height: 12px;"></div>
                                                <div style="display: inline-block;">선택 가능 날짜</div>&nbsp;&nbsp;&nbsp;
                                                <div style="display: inline-block; border: solid 1px #000; background-color: #00aeaf; width: 12px; height: 12px;"></div>
                                                <div style="display: inline-block;">선택 날짜</div>&nbsp;&nbsp;&nbsp;
                                                <div style="display: inline-block; border: solid 1px #000; background-color: #ff99cc; width: 12px; height: 12px;"></div>
                                                <div style="display: inline-block;">선택 불가 날짜</div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div id="calendar"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-1">
                                        <div id="ts_time_9" data-time="9" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 09:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_10" data-time="10" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 10:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_11" data-time="11" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 11:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_12" data-time="12" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 12:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_14" data-time="14" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 14:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_15" data-time="15" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 15:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_16" data-time="16" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 16:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_17" data-time="17" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 17:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_18" data-time="18" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 18:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_19" data-time="19" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 19:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_20" data-time="20" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 20:00 </div>
                                    </div>
                                    <div class="col-md-1">
                                        <div id="ts_time_21" data-time="21" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 21:00 </div>
                                    </div>
                                </div>
                                <hr>
                            </div>
                            <div class="row col-md-12">
                                <h3>4. 선택한 정보를 확인해 주세요.</h3>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담 유형</div>
                                    <div class="col-md-9"><span id="view_advice_type"></span> <span id="view_advice_number"></span>회</div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담 요금</div>
                                    <div class="col-md-9"><span id="view_advice_price">0</span>원</div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담사</div>
                                    <div class="col-md-9"><span id="view_advisor_name"></span></div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담 날짜</div>
                                    <div class="col-md-9"><span id="view_advice_date" ts-data-date=""></span></div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담 시간</div>
                                    <div class="col-md-9"><span id="view_advice_time" ts-data-time=""></span></div>
                                </div>
                                <hr>
                            </div>
                            <div class="row">
                                <div class="col-md-12 tp-title-center">
                                <#if checkMap.checkUserInfo?string != "true" && checkMap.checkPretesting?string != "true">
                                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo-pretesting">상담 예약하기</button>
                                <#elseif checkMap.checkUserInfo?string != "true">
                                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo">상담 예약하기</button>
                                <#elseif checkMap.checkPretesting?string != "true">
                                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-pretesting">상담 예약하기</button>
                                <#else>
                                    <button id="btn_text_skype_therapy" class="btn tp-btn-default tp-btn-lg">상담 예약하기</button>
                                </#if>
                                </div>
                            </div>
                        </div>
                        <!-- 컨텐츠케어세라피 --------------------------------------------------------------------------->
                        <div role="tabpanel" class="tab-pane" id="contents_therapy" data-contents-srl="5">
                            <div class="row col-md-12">
                                <div class="row">
                                    <div class="col-md-12">
                                        좋은 습관을 점진적으로 만들거나 부정적인 부분을 버리고 싶은 분들에게 적합한 케어테파리입니다.
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">
                                        상담기간 선택
                                    </div>
                                    <div class="col-md-9">
                                        <div class="inline-group">
                                            <label for="c_advice_period_1"><input type="radio" name="c_advice_period" id="c_advice_period_1" value="1" class="c-advice-period" data-advice-number="4" checked> 1일(4회)</label>&nbsp;&nbsp;&nbsp;
                                            <label for="c_advice_period_2"><input type="radio" name="c_advice_period" id="c_advice_period_2" value="1" class="c-advice-period" data-advice-number="22"> 1주</label>&nbsp;&nbsp;&nbsp;
                                            <label for="c_advice_period_3"><input type="radio" name="c_advice_period" id="c_advice_period_3" value="4" class="c-advice-period" data-advice-number="88"> 4주(1개월)</label>&nbsp;&nbsp;&nbsp;
                                            <label for="c_advice_period_4"><input type="radio" name="c_advice_period" id="c_advice_period_4" value="12" class="c-advice-period" data-advice-number="264"> 12주(3개월)</label>
                                        </div>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담 요금 안내</div>
                                    <div class="col-md-9"><span id="c_price"></span>원 ( 총 <span id="c_number"></span>회 )</div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">상담 시작일 지정</div>
                                    <div class="col-md-9">
                                        <input type="text" id="c_advice_start_date" placeholder="상담 시작일을 입력하세요" value="" class="datepicker" size="21" readonly>
                                    </div>
                                </div>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3">SMS PUSH 시간 설정</div>
                                    <div class="col-md-9">
                                        <select name="c_sms_push_time">
                                            <option value="09">09</option>
                                            <option value="10">10</option>
                                            <option value="11">11</option>
                                            <option value="12">12</option>
                                            <option value="13">13</option>
                                            <option value="14">14</option>
                                            <option value="15">15</option>
                                            <option value="16">16</option>
                                            <option value="17">17</option>
                                        </select>시
                                        <select name="c_sms_push_minute">
                                            <option value="00">00</option>
                                            <option value="30">30</option>
                                        </select>분
                                    </div>
                                </div>
                                <hr>
                            </div>
                            <div class="row">
                                <div class="col-md-12 tp-title-center">
                                <#if checkMap.checkUserInfo?string != "true" && checkMap.checkPretesting?string != "true">
                                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo-pretesting">상담 예약하기</button>
                                <#elseif checkMap.checkUserInfo?string != "true">
                                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo">상담 예약하기</button>
                                <#elseif checkMap.checkPretesting?string != "true">
                                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-pretesting">상담 예약하기</button>
                                <#else>
                                    <button id="btn_content_therapy" class="btn tp-btn-default tp-btn-lg">상담 예약하기</button>
                                </#if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="member_search_modal" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true" style="padding-top: 15%;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">사용자 검색</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <fieldset class="smart-form">
                            <section>
                                <label >사용자</label>
                                <label for="file" class="input input-file" style="display: block;">
                                    <div id="member_list" class="select2" style="width:100%;height:30px;"></div>
                                </label>
                            </section>
                        </fieldset>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button id="btn_member_add" type="button" class="btn btn-default" data-dismiss="modal" data-box-number="">추가</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="zip" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true" style="padding-top: 15%;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">주소 검색</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <fieldset class="smart-form">
                            <label for="zip_gubun1" style="cursor: pointer;">
                                <input type="radio" name="zip_gubun" id="zip_gubun1" value="1" class="styled" style="cursor: pointer;"> 지번검색
                            </label>&nbsp;&nbsp;&nbsp;
                            <label for="zip_gubun2" style="cursor: pointer;">
                                <input type="radio" name="zip_gubun" id="zip_gubun2" value="2" class="styled" style="cursor: pointer;"> 도로명검색
                            </label>
                            <section id="zipview1" style="cursor: pointer;">
                                <label >도로명 입력</label>
                                <label for="file" class="input input-file" style="display: block;">
                                    <div id="zip_list" class="select2" style="width:100%;height:30px;"></div>
                                </label>
                            </section>
                            <section id="zipview2">
                                <label >시, 군, 구 입력</label>
                                <label for="file" class="input input-file" style="display: block;">
                                    <div id="zip_list1" class="select2" style="width:100%;height:30px;"></div>
                                </label>
                            </section>
                        </fieldset>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button id="btn_zip_add1" type="button" class="btn btn-default" data-dismiss="modal" data-box-number="" >추가</button>
                <button id="btn_zip_add2" type="button" class="btn btn-default" data-dismiss="modal" data-box-number="" >추가</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="zipcode" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true" style="padding-top: 15%;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">주소 검색</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <fieldset class="smart-form">
                            <label for="zipcode_gubun1" style="cursor: pointer;">
                                <input type="radio" name="zipcode_gubun" id="zipcode_gubun1" value="1" class="styled" style="cursor: pointer;"> 지번검색
                            </label>&nbsp;&nbsp;&nbsp;
                            <label for="zipcode_gubun2" style="cursor: pointer;">
                                <input type="radio" name="zipcode_gubun" id="zipcode_gubun2" value="2" class="styled" style="cursor: pointer;"> 도로명검색
                            </label>
                            <section id="zipcodeview1">
                                <label >도로명 입력</label>
                                <label for="file" class="input input-file" style="display: block;">
                                    <div id="zipcode_list" class="select2" style="width:100%;height:30px;"></div>
                                </label>
                            </section>
                            <section id="zipcodeview2">
                                <label >시, 군, 구 입력</label>
                                <label for="file" class="input input-file" style="display: block;">
                                    <div id="zipcode_list1" class="select2" style="width:100%;height:30px;"></div>
                                </label>
                            </section>
                        </fieldset>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button id="btn_zipcode_add1" type="button" class="btn btn-default" data-dismiss="modal" data-box-number="" >추가</button>
                <button id="btn_zipcode_add2" type="button" class="btn btn-default" data-dismiss="modal" data-box-number="" >추가</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="precheck_userinfo_modal" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true" style="padding-top: 15%;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">필수 항목 등록 안내</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <p id="precheck_userinfo_text"></p>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" id="btn_precheck_userinfo_cancel" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" id="btn_precheck_userinfo_ok" class="btn btn-default" data-dismiss="modal" data-box-number="">확인</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="precheck_pretesting_modal" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true" style="padding-top: 15%;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="myModalLabel">인테이크지 검사 안내</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <p id="precheck_pretesting_text"></p>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" id="btn_precheck_pretesting_cancel" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button type="button" id="btn_precheck_pretesting_ok" class="btn btn-default" data-dismiss="modal" data-box-number="">확인</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var appointmentEntites = [];
    var contentsSrl = "1";
    var currentYYYYMM = '';
    var that = '';

    <#-- 싸이케어세라피를 위한 function START -->
    function single_therapy() {
        var singleAdvicePeriod = "";
        var singleAdviceNumber = "";
        $('input:radio[name="single_advice_period"]:checked').each(function() {
            singleAdvicePeriod = this.value;
            singleAdviceNumber = this.value;
        });

        var htmlCode = "";
        var cnt = 0;
        if(singleAdvicePeriod != '') {
            <#list productEntities as productEntity>
                if(contentsSrl == '${productEntity.contents_srl}' && singleAdvicePeriod == '${productEntity.advice_period}') {
                    $('#single_price')[0].innerHTML = '${productEntity.advice_price?string.number}';
                }
            </#list>
        }
    }
    <#-- 싸이케어세라피를 위한 function END -->

    <#-- 커플싸이케어세파리 / 패밀리싸이케어세라피를 위한 function START -->
    function couple_therapy() {
        var coupleAdvicePeriod = "";
        var coupleAdviceNumber = "";
        var couple_price = 0;

        $('input:radio[name="couple_advice_period"]:checked').each(function() {
            coupleAdvicePeriod = this.value;
            coupleAdviceNumber = this.value;
        });

        var select_value = $('#couple_person_select option:selected').val();

        if(coupleAdvicePeriod != '') {
            <#list productEntities as productEntity>
                if(contentsSrl == '${productEntity.contents_srl}' && coupleAdvicePeriod == '${productEntity.advice_period}') {
                    couple_price = ${productEntity.advice_price};
                }
            </#list>
        }

        if(select_value > 1) {
            couple_price = couple_price + ((select_value - 1) * 200000);
        }

        $('#couple_price')[0].innerHTML = couple_price.toLocaleString();
    }
    <#-- 커플싸이케어세파리 / 패밀리싸이케어세라피를 위한 function END -->

    <#-- 텍스트케어세라피 / 스카이프케어세라피를 위한 function START -->
    function text_skype_therapy() {
        var tsAdviceType = "";
        $('input:radio[name="ts_advice_type"]:checked').each(function() {
            tsAdviceType = this.value;
        });

        var tsAdviceNumber = "";
        $('input:radio[name="ts_advice_number"]:checked').each(function() {
            tsAdviceNumber = this.value;
        });

        if(tsAdviceType != '') {
        <#list productEntities as productEntity>
            if(contentsSrl == '${productEntity.contents_srl}' && tsAdviceType == '${productEntity.advice_type}' && tsAdviceNumber == '${productEntity.advice_number}') {
                $('#ts_price')[0].innerHTML = '${productEntity.advice_price?string.number}';
                $('#ts_cnt')[0].innerHTML = tsAdviceNumber;

                $('#view_advice_price')[0].innerHTML = '${productEntity.advice_price?string.number}';
                $('#view_advice_number')[0].innerHTML = tsAdviceNumber;
            }
        </#list>

        var cnt = 0;
        var htmlCode = "";
        <#list memberEntities as memberEntity>
            if(tsAdviceType == '${memberEntity.memberExtraEntity.class_srl}') {
                if(cnt % 4 == 0) {
                    htmlCode += '<div class="inline-group">';
                }

                htmlCode += '<label for="ts_advisor_${memberEntity.member_srl}">';
                htmlCode += '<input type="radio" name="ts_advisor_srl" id="ts_advisor_${memberEntity.member_srl}" value="${memberEntity.member_srl}" class="styled ts-advisor-srl"> ${memberEntity.nick_name}';
                htmlCode += '</label>&nbsp;&nbsp;&nbsp;';

                if(cnt % 4 == 3) {
                    htmlCode += '</div>'
                }

                cnt++;
            }
        </#list>

            if(cnt % 4 != 0) {
                htmlCode += '</div>'
            }

            $('#advisor-area')[0].innerHTML = htmlCode;
        }

        var view_product = "";
        var view_desc = "";
        if(contentsSrl == '3') {
            view_product += "스카이프케어테라피 ";
            view_desc = "스카이프를 이용하여 내담자와 상담자가 1:1 영상 상담을 진행하는 방식으로 도움을 받을 수 있습니다.";
        } else if(contentsSrl == '4') {
            view_product += "텍스트케어테라피 ";
            view_desc = "카톡을 이용하여 내담자와 상담자가 1:1 채팅상담을 진행하고 방식으로 도움을 받을 수 있습니다.";
        }

        if(tsAdviceType == 1) {
            view_product += " 으뜸 ";
        } else if(tsAdviceType == 2) {
            view_product += " 가장 ";
        } else if(tsAdviceType == 3) {
            view_product += " 한마루 ";
        }

        $('#view_desc')[0].innerHTML = view_desc;

        $('#view_advice_type')[0].innerHTML = view_product;
        $('#view_advice_number')[0].innerHTML = tsAdviceNumber;

    }
    <#-- 텍스트케어세라피 / 스카이프케어세라피를 위한 function END -->

    <#-- 컨텐츠케어세라피를 위한 function START -->
    function contents_therapy() {
        var cAdvicePeriod = "";
        $('input:radio[name="c_advice_period"]:checked').each(function() {
            cAdvicePeriod = this.value;
        });

        var cAdviceNumber = $('input:radio[name="c_advice_period"]:checked').attr("data-advice-number");
        $('#c_number')[0].innerHTML = cAdviceNumber;

        var htmlCode = "";
        var cnt = 0;
        if(cAdvicePeriod != '') {
            <#list productEntities as productEntity>
                if(contentsSrl == '${productEntity.contents_srl}' && cAdvicePeriod == '${productEntity.advice_period}' && cAdviceNumber == '${productEntity.advice_number}') {
                    $('#c_price')[0].innerHTML = '${productEntity.advice_price?string.number}';
                }
            </#list>
        }
    }
    <#-- 컨텐츠케어세라피를 위한 function END -->

    function precheck() {
        $.ajax({
            type: "POST",
            url: '${Request.contextPath}/user/product/precheck',
            contentType : 'application/json',
            //data: JSON.stringify(dataString),
            dataType: 'json',
            success: function (data, status, xhr) {
                if(!(data.checkMap.checkUserInfo || data.checkMap.checkPretesting)) {
                    $('#precheck_userinfo_text').text("상담/검사에 필요한 기초 정보 및 인테이크지 검사 정보가 없습니다. 해당 정보 등록 페이지로 이동합니다.");
                    $('#precheck_userinfo_modal').modal('show');
                    return false;
                } else if(!data.checkMap.checkUserInfo) {
                    $('#precheck_userinfo_text').text("상담/검사에 필요한 기초 정보가 없습니다. 해당 정보 등록 페이지로 이동합니다.");
                    $('#precheck_userinfo_modal').modal('show');
                    return false;
                } else if(!data.checkMap.checkPretesting) {
                    $('#precheck_pretesting_text').text("인테이크지 검사 정보가 없습니다. 해당 정보 등록 페이지로 이동합니다.");
                    $('#precheck_pretesting_modal').modal('show');
                    return false;
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
            }
        });
    }

    $(document).ready(function() {
        precheck();

        /* 디폴트값 셋팅 START */
        if(${contents_srl} > 0) {
            contentsSrl = ${contents_srl};
            $('.contents-srl').each(function(i, obj) {
                var $contents_srl_this = $(this);
                if(obj.value == contentsSrl)  {
                    $contents_srl_this.addClass('active');
                } else {
                    $contents_srl_this.removeClass('active');
                }
            });

            $('.tab-pane').each(function(i, obj) {
                var $tab_pane_this = $(this);
                if($tab_pane_this.attr('data-contents-srl') == contentsSrl)  {
                    $tab_pane_this.addClass('active');
                } else {
                    $tab_pane_this.removeClass('active');
                }
            });

            if(contentsSrl == 4) {
                $('#text_skype_therapy').addClass('active');
            }
        }

        if(${advice_period} > 0) {
            var $period_this;
            if (${contents_srl} == 1) {
                $('input:radio[name="single_advice_period"]').each(function(i, obj) {
                    $period_this = $(this);
                    if(obj.value == ${advice_period}) {
                        $period_this.attr('checked', true);
                    } else {
                        $period_this.attr('checked', false);
                    }
                });
            } else if (${contents_srl} == 2) {
                $('input:radio[name="couple_advice_period"]').each(function(i, obj) {
                    $period_this = $(this);
                    if(obj.value == ${advice_period}) {
                        $period_this.attr('checked', true);
                    } else {
                        $period_this.attr('checked', false);
                    }
                });
            } else if (${contents_srl} == 5) {
                $('input:radio[name="c_advice_period"]').each(function(i, obj) {
                    $period_this = $(this);

                    if(obj.value == ${advice_period} && $period_this.attr('data-advice-number') == ${advice_number}) {
                        $period_this.attr('checked', true);
                    } else {
                        $period_this.attr('checked', false);
                    }
                });
            }
        }

        if(${advice_type} > 0) {
            var $type_this;
            $('input:radio[name="ts_advice_type"]').each(function(i, obj) {
                $type_this = $(this);

                if(obj.value == ${advice_type}) {
                    $type_this.attr('checked', true);
                } else {
                    $type_this.attr('checked', false);
                }
            });
        }
        /* 디폴트값 셋팅 END */

        single_therapy();
        couple_therapy();
        text_skype_therapy();
        contents_therapy();

        <@ap.jsselect2 id="member_list" placeholder="사용자 ID를 입력하세요" uniq="member_srl"
        showColumn="user_name" uri="${Request.contextPath}/user/product/member_search/t/"
        miniumLength=5 />

        <@ap.jsselect2 id="zipcode_list" placeholder="도로명 주소를 입력하세요." uniq="zipcode_srl"
        showColumn="full" uri="${Request.contextPath}/user/product/zipcode_search/t/"
        miniumLength=2 />

        <@ap.jsselect2 id="zipcode_list1" placeholder="지번주소를 입력하세요." uniq="zipcode_srl"
        showColumn="full" uri="${Request.contextPath}/user/product/zipcode_search/jibun/t/"
        miniumLength=2 />

        <@ap.jsselect2 id="zip_list" placeholder="도로명 주소를 입력하세요." uniq="zipcode_srl"
        showColumn="full" uri="${Request.contextPath}/user/product/zipcode_search/t/"
        miniumLength=2 />

        <@ap.jsselect2 id="zip_list1" placeholder="지번주소를 입력하세요." uniq="zipcode_srl"
        showColumn="full" uri="${Request.contextPath}/user/product/zipcode_search/jibun/t/"
        miniumLength=2 />

        $('#zipcodeview1').hide();
        $('#zipcodeview2').hide();
        $('#zipview1').hide();
        $('#zipview2').hide();
        $('#btn_zipcode_add1').hide();
        $('#btn_zipcode_add2').hide();
        $('#btn_zip_add1').hide();
        $('#btn_zip_add2').hide();

        $('#zipcode_gubun1').click(function(e){
            $('#zipcodeview1').hide();
            $('#zipcodeview2').show();
            $('#btn_zipcode_add1').hide();
            $('#btn_zipcode_add2').show();
            return;
        });

        $('#zipcode_gubun2').click(function(e){
            $('#zipcodeview2').hide();
            $('#zipcodeview1').show();
            $('#btn_zipcode_add2').hide();
            $('#btn_zipcode_add1').show();
            return;
        });

        $('#zip_gubun1').click(function(e){
            $('#zipview1').hide();
            $('#zipview2').show();
            $('#btn_zip_add1').hide();
            $('#btn_zip_add2').show();
            return;
        });

        $('#zip_gubun2').click(function(e){
            $('#zipview2').hide();
            $('#zipview1').show();
            $('#btn_zip_add2').hide();
            $('#btn_zip_add1').show();
            return;
        });

        $('#btn_zipcode_add1').click(function (e) {
            var select2Data = $('#zipcode_list').select2('data').full;
            $('#single_receive_address').val(select2Data);
        });

        $('#btn_zipcode_add2').click(function (e) {
            var select2Data = $('#zipcode_list1').select2('data').full;
            $('#single_receive_address').val(select2Data);
        });

        $('#btn_zip_add1').click(function (e) {
            var select2Data = $('#zip_list').select2('data').full;
            $('#couple_receive_address').val(select2Data);
        });

        $('#btn_zip_add2').click(function (e) {
            var select2Data = $('#zip_list1').select2('data').full;
            $('#couple_receive_address').val(select2Data);
        });

        var globleToday = new Date();
        var currentMonth = globleToday.getMonth() + 1;
        if(currentMonth < 10) {
            currentMonth = "0" + currentMonth;
        }
        currentYYYYMM = globleToday.getFullYear() + "" + currentMonth;

        var advisor_srl = 0;
        $('input:radio[name="ts_advisor_srl"]:checked').each(function() {
            advisor_srl = this.value;
        });

        $.ajax({
            type: "GET",
            url: '${Request.contextPath}/user/appointment/check/' + advisor_srl + '/' + currentYYYYMM,
            contentType : 'application/json',
            //data: JSON.stringify(dataString),
            dataType: 'json',
            success: function (data, status, xhr) {
                appointmentEntites = data.appointmentEntites;
            },
            error: function (xhr, ajaxOptions, thrownError) {},
            complete: function(xhr, status) {
                var calendar = new controller();
                calendar.init();
            }
        });

        <#-- 탭메뉴 선택 시 실행하는 스크립트 START -->
        $(".contents-srl").click(function() {
            contentsSrl = this.value;

            single_therapy();
            couple_therapy();
            text_skype_therapy();
            contents_therapy();
        });
        <#-- 탭메뉴 선택 시 실행하는 스크립트 END -->

        <#-- 싸이케어세라피 / 커플싸이케어세파리 / 패밀리싸이케어세라피에서 실행하는 스크립트 START -->
        <#-- 상담유형 선택 시 실행하는 스크립트 START -->
        $(".single-advice-period").click(function() {
            single_therapy();
        });
        <#-- 상담유형 선택 시 실행하는 스크립트 START -->

        <#-- 상담유형 선택 시 실행하는 스크립트 START -->
        $(".couple-advice-period").click(function() {
            couple_therapy();
        });
        <#-- 상담유형 선택 시 실행하는 스크립트 START -->

        <#-- 상담 시작일 설정을 위한 달력 START -->
        //$('#single_advice_start_date').datepicker({});
        <#-- 상담 시작일 설정을 위한 달력 END -->

        <#-- 상담 시작일 설정을 위한 달력 START -->
        //$('#couple_advice_start_date').datepicker({});
        <#-- 상담 시작일 설정을 위한 달력 END -->

        <#-- 등록 버튼 START -->
        $("#btn_single_therapy").click(function() {
            var advice_period = 0;
            var advice_number = 0;
            $('input:radio[name="single_advice_period"]:checked').each(function() {
                advice_period = this.value;
                advice_number = this.value;
            });
            //var advice_start_date = $('#single_advice_start_date').val().replace(/-/g, "");
            var advice_start_date = "";
            var receive_name = $('#single_receive_name').val();
            var receive_phone = $('#single_receive_phone').val();
            var receive_address = $('#single_receive_address').val()+$('#single_receive_address_plus').val();

            var product_srl = 0;
            var advice_price = 0;
            <#list productEntities as productEntity>
                if(contentsSrl == '${productEntity.contents_srl}' && advice_period == '${productEntity.advice_period}') {
                    product_srl = ${productEntity.product_srl};
                    advice_price = ${productEntity.advice_price};
                }
            </#list>

            if(advice_period == 0) {
                $('#pop-message').text("상담 기간을 선택해 주세요.");
                popup_layer();
                return;
            }
/*
            if(advice_start_date == '') {
                $('#pop-message').text("상담 시작일을 선택해 주세요.");
                popup_layer();
                return;
            }
*/
            if(receive_name == '') {
                $('#pop-message').text("수령인을 입력해 주세요.");
                popup_layer();
                return;
            }

            if(receive_phone == '') {
                $('#pop-message').text("수령인 휴대폰번호를 입력해 주세요.");
                popup_layer();
                return;
            }

            if(receive_phone.length < 10 || receive_phone.length > 11) {
                $('#pop-message').text("수령인 휴대폰번호를 확인해 주세요.");
                popup_layer();
                return;
            }

            if(receive_address == '') {
                $('#pop-message').text("수령지 주소를 입력해 주세요.");
                popup_layer();
                return;
            }

            var dataString = {
                "contents_srl"       : contentsSrl,
                "product_srl"       : product_srl,
                "advice_type"       : 0,
                "advice_period"     : advice_period,
                "advice_number"     : advice_number,
                "advice_price"      : advice_price,
                "advisor_srl"       : 0,
                "start_date"        : advice_start_date,
                "push_date"         : '',
                "push_time"         : '',
                "appointment_date"  : '',
                "appointment_time"  : '',
                "receive_name"      : receive_name,
                "receive_address"   : receive_address,
                "receive_phone"     : receive_phone,
                "member_srls"        : null
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/product/adviceAdd',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    if(data.resultCode > 0) {
                        $('#pop-message').text("상담 신청이 정상적으로 이루어졌습니다.");
                        popup_layer();

                        timer = setInterval(function() {
                            my.fn.pmove('${Request.contextPath}/user/payment/paymentForm/' + advice_price + "/" + data.resultCode);
                        }, 1500);
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("상담 신청이 실패되었습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });
        });
        <#-- 등록 버튼 END -->
        <#-- 싸이케어세라피 / 커플싸이케어세파리 / 패밀리싸이케어세라피에서 실행하는 스크립트 END -->

        $('#couple_person_select').change(function() {
            var select_value = $('#couple_person_select option:selected').val();

            var coupleAdvicePeriod = "";
            $('input:radio[name="couple_advice_period"]:checked').each(function() {
                coupleAdvicePeriod = this.value;
            });

            var couple_price = 0
            <#list productEntities as productEntity>
                if(contentsSrl == '${productEntity.contents_srl}' && coupleAdvicePeriod == '${productEntity.advice_period}') {
                    couple_price = ${productEntity.advice_price};
                }
            </#list>

            if(select_value > 1) {
                couple_price = couple_price + ((select_value - 1) * 200000);
            }

            var str_html = '';
            for(var i=0; i<select_value; i++) {
                str_html += '<div class="row col-md-12 form-group"><input type="text" id="couple_member_id' + (i+1) + '" name="couple_member_id" data-member-srl="" readonly/> <button class="btn couple-member-search" data-box-number="' + (i+1) + '">검색</button></div>';
            }

            $('#couple_price')[0].innerHTML = couple_price.toLocaleString();

            $('#person_area')[0].innerHTML = str_html;
        });

        $(document).on("click", '.couple-member-search', function() {
            var $this = $(this);
            $('#member_search_modal').modal('show');

            $('#btn_member_add').attr('data-box-number', $this.attr('data-box-number'));
        });

        $(document).on("click", '#btn_member_add', function() {
            var $this = $(this);

            var select2Data = $('#member_list').select2('data');

            $('#couple_member_id' + $this.attr('data-box-number')).val(select2Data.user_name);
            $('#couple_member_id' + $this.attr('data-box-number')).attr('data-member-srl', select2Data.member_srl);
        });

        <#-- 등록 버튼 START -->
        $("#btn_couple_therapy").click(function() {
            var advice_period = 0;
            var advice_number = 0;
            $('input:radio[name="couple_advice_period"]:checked').each(function() {
                advice_period = this.value;
                advice_number = this.value;
            });
            //var advice_start_date = $('#couple_advice_start_date').val().replace(/-/g, "");
            var advice_start_date = "";
            var receive_name = $('#couple_receive_name').val();
            var receive_phone = $('#couple_receive_phone').val();
            var receive_address = $('#couple_receive_address').val()+$('#couple_receive_address_plus').val();

            var product_srl = 0;

            <#list productEntities as productEntity>
                if(contentsSrl == '${productEntity.contents_srl}' && advice_period == '${productEntity.advice_period}') {
                    product_srl = ${productEntity.product_srl};
                }
            </#list>

            var advice_price = 0;

            var select_value = $('#couple_person_select option:selected').val();

            var coupleAdvicePeriod = "";
            $('input:radio[name="couple_advice_period"]:checked').each(function() {
                coupleAdvicePeriod = this.value;
            });

            var couple_price = 0
            <#list productEntities as productEntity>
                if(contentsSrl == '${productEntity.contents_srl}' && coupleAdvicePeriod == '${productEntity.advice_period}') {
                    advice_price = ${productEntity.advice_price};
                }
            </#list>

            if(select_value > 1) {
                advice_price = advice_price + ((select_value - 1) * 200000);
            }

            if(advice_period == 0) {
                $('#pop-message').text("상담 기간을 선택해 주세요.");
                popup_layer();
                return;
            }
/*
            if(advice_start_date == '') {
                $('#pop-message').text("상담 시작일을 선택해 주세요.");
                popup_layer();
                return;
            }
*/
            if(receive_name == '') {
                $('#pop-message').text("수령인을 입력해 주세요.");
                popup_layer();
                return;
            }

            if(receive_phone == '') {
                $('#pop-message').text("수령인 휴대폰번호를 입력해 주세요.");
                popup_layer();
                return;
            }

            if(receive_phone.length < 10 || receive_phone.length > 11) {
                $('#pop-message').text("수령인 휴대폰번호를 확인해 주세요.");
                popup_layer();
                return;
            }

            if(receive_address == '') {
                $('#pop-message').text("수령지 주소를 입력해 주세요.");
                popup_layer();
                return;
            }

            var member_srls = new Array();
            var cnt = 0;
            $('input:text[name="couple_member_id"]').each(function() {
                var $this = $(this);

                if($this.value == '') {
                    $('#pop-message').text("커플 또는 가족을 선택해 주세요.");
                    popup_layer();
                    return;
                } else {
                    member_srls[cnt] = $this.attr('data-member-srl');
                }

                cnt++;
            });

            var dataString = {
                "contents_srl"      : contentsSrl,
                "product_srl"       : product_srl,
                "advice_type"       : 0,
                "advice_period"     : advice_period,
                "advice_number"     : advice_number,
                "advice_price"      : advice_price,
                "advisor_srl"       : 0,
                "start_date"        : advice_start_date,
                "push_date"         : '',
                "push_time"         : '',
                "appointment_date"  : '',
                "appointment_time"  : '',
                "receive_name"      : receive_name,
                "receive_address"   : receive_address,
                "receive_phone"     : receive_phone,
                "member_srls"        : member_srls
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/product/adviceAdd',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    if(data.resultCode > 0) {
                        $('#pop-message').text("상담 신청이 정상적으로 이루어졌습니다.");
                        popup_layer();

                        timer = setInterval(function() {
                            my.fn.pmove('${Request.contextPath}/user/payment/paymentForm/' + advice_price + "/" + data.resultCode);
                        }, 1500);
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("상담 신청이 실패되었습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });
        });

        <#-- 텍스트케어세라피 / 스카이프케어세라피에서 실행하는 스크립트 START -->
        $(".ts-advice-type").click(function() {
            text_skype_therapy();
        });

        $(".ts-advice-number").click(function() {
            text_skype_therapy();
        });

        $("#btn_text_skype_therapy").click(function() {
            var advice_type = 0;
            $('input:radio[name="ts_advice_type"]:checked').each(function() {
                advice_type = this.value;
            });

            var advice_number = "";
            $('input:radio[name="ts_advice_number"]:checked').each(function() {
                advice_number = this.value;
            });

            var product_srl = 0;
            var advice_price = 0;
            <#list productEntities as productEntity>
                if(contentsSrl == '${productEntity.contents_srl}' && advice_type == '${productEntity.advice_type}' && advice_number == '${productEntity.advice_number}') {
                    product_srl = ${productEntity.product_srl};
                    advice_price = ${productEntity.advice_price};
                }
            </#list>

            var advisor_srl = 0;
            $('input:radio[name="ts_advisor_srl"]:checked').each(function() {
                advisor_srl = this.value;
            });

            var appointment_date = $('#view_advice_date').attr('ts-data-date');
            var appointment_time = $('#view_advice_time').attr('ts-data-time');

            if(advice_type == 0) {
                $('#pop-message').text("상담 유형을 선택해 주세요.");
                popup_layer();
                return;
            }

            if(advisor_srl == 0) {
                $('#pop-message').text("상담사를 선택해 주세요.");
                popup_layer();
                return;
            }

            if(appointment_date == '') {
                $('#pop-message').text("상담 날짜를 선택해 주세요.");
                popup_layer();
                return;
            }

            if(appointment_time == '') {
                $('#pop-message').text("상담 시간을 선택해 주세요.");
                popup_layer();
                return;
            }

            var dataString = {
                "contents_srl"       : contentsSrl,
                "product_srl"       : product_srl,
                "advice_type"       : advice_type,
                "advice_period"     : 0,
                "advice_number"     : advice_number,
                "advice_price"      : advice_price,
                "advisor_srl"       : advisor_srl,
                "start_date"        : '',
                "push_date"         : '',
                "push_time"         : '',
                "appointment_date"  : appointment_date,
                "appointment_time"  : appointment_time,
                "receive_name"      : '',
                "receive_address"   : '',
                "receive_phone"     : '',
                "member_srls"        : null
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/product/adviceAdd',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    if(data.resultCode > 0) {
                        $('#pop-message').text("상담 신청이 정상적으로 이루어졌습니다.");
                        popup_layer();

                        timer = setInterval(function() {
                            my.fn.pmove('${Request.contextPath}/user/payment/paymentForm/' + advice_price + "/" + data.resultCode);
                        }, 1500);
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("상담 신청이 실패되었습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });
        });
        <#-- 텍스트케어세라피 / 스카이프케어세라피에서 실행하는 스크립트 END -->

        <#-- 컨텐츠세라피에서 실행하는 스크립트 START -->
        $(".c-advice-period").click(function() {
            contents_therapy();
        });

        $('#c_advice_start_date').datepicker();

        $("#btn_content_therapy").click(function() {
            var advice_period = 0;
            $('input:radio[name="c_advice_period"]:checked').each(function() {
                advice_period = this.value;
            });

            var advice_number = $('input:radio[name="c_advice_period"]:checked').attr("data-advice-number");

            var advice_start_date = $('#c_advice_start_date').val().replace(/-/g, "");

            var product_srl = 0;
            var advice_price = 0;
            <#list productEntities as productEntity>
                if(contentsSrl == '${productEntity.contents_srl}' && advice_period == '${productEntity.advice_period}' && advice_number == '${productEntity.advice_number}') {
                    product_srl = ${productEntity.product_srl};
                    advice_price = ${productEntity.advice_price};
                }
            </#list>

            var sms_push_time = $('select[name="c_sms_push_time"]').val() +  $('select[name="c_sms_push_minute"]').val();

            if(advice_period == 0) {
                $('#pop-message').text("상담 기간을 선택해 주세요.");
                popup_layer();
                return;
            }

            if(advice_start_date == '') {
                $('#pop-message').text("상담 시작일을 선택해 주세요.");
                popup_layer();
                return;
            }

            if(sms_push_time == '') {
                $('#pop-message').text("SMS PUSH 시간을 선택해 주세요.");
                popup_layer();
                return;
            }

            var dataString = {
                "contents_srl"      : contentsSrl,
                "product_srl"       : product_srl,
                "advice_type"       : 0,
                "advice_period"     : advice_period,
                "advice_number"     : advice_number,
                "advice_price"      : advice_price,
                "advisor_srl"       : 0,
                "start_date"        : advice_start_date,
                "push_date"         : '',
                "push_time"         : sms_push_time,
                "appointment_date"  : '',
                "appointment_time"  : '',
                "receive_name"      : '',
                "receive_address"   : '',
                "receive_phone"     : '',
                "member_srls"        : null
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/product/adviceAdd',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    if(data.resultCode > 0) {
                        $('#pop-message').text("상담 신청이 정상적으로 이루어졌습니다.");
                        popup_layer();

                        timer = setInterval(function() {
                            my.fn.pmove('${Request.contextPath}/user/payment/paymentForm/' + advice_price + "/" + data.resultCode);
                        }, 1500);
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("상담 신청이 실패되었습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });
        });
    <#-- 컨텐츠세라피에서 실행하는 스크립트 END -->
    });

    <#-- 상담사 선택에 따른 상담사 소개 변경 (동적으로 생성된 태그에 이벤트를 발생시키려면 on function을 상용해야 한다.)-->
    <#-- 상담사 선택 시 실행하는 스크립트 START -->
    $(document).on("click", '.ts-advisor-srl', function() {
        var advisor_srl = 0;
        $('input:radio[name="ts_advisor_srl"]:checked').each(function() {
            advisor_srl = this.value;
        });

        var htmlCode = "";
        var cnt = 0;
        if(advisor_srl != '') {
            <#list memberEntities as memberEntity>
                if(advisor_srl == '${memberEntity.member_srl}') {

                    $('#advisor-desc-area').text('${memberEntity.memberExtraEntity.self_introduction}');
                    $('#view_advisor_name').text('${memberEntity.nick_name}');
                    $('#cal_advisor_name').text('${memberEntity.nick_name}');
                }
            </#list>
        }

        /* 상담사가 변경되었을 때 달력을 리셋한다. */
        $.ajax({
            type: "GET",
            url: '${Request.contextPath}/user/appointment/check/' + advisor_srl + '/' + currentYYYYMM,
            contentType : 'application/json',
            //data: JSON.stringify(dataString),
            dataType: 'json',
            success: function (data, status, xhr) {
                appointmentEntites = data.appointmentEntites;
            },
            error: function (xhr, ajaxOptions, thrownError) {},
            complete: function(xhr, status) {
                that.renderCalendar();
            }
        });
    });
    <#-- 상담사 선택 시 실행하는 스크립트 END -->

    <#-- 달력에서 날짜 선택시 실행하는 스크립트 START -->
    $(document).on("click", '.possible', function() {
        if(!$('input:radio[name="ts_advisor_srl"]').is(':checked')) {
            $('#pop-message').text("상담사를 먼저 선택해 주세요.");
            popup_layer();
            return;
        }

        var $selectThis = $(this);

        $('.possible').each(function(index, value) {
            if($selectThis.attr('data-current') == $(this).attr('data-current')) {
                $(this).css("background", "#00aeaf");
            } else {
                $(this).css("background", "");
            }
        });

        var selectDate = $selectThis.attr('data-current');

        var arrTime = new Array('9', '10', '11', '12', '14', '15', '16', '17', '18', '19', '20', '21');
        for(var i=0; i<arrTime.length; i++) {
            $.each(appointmentEntites, function (index, appointmentEntity) {
                if (selectDate == appointmentEntity.appointment_date && arrTime[i] == appointmentEntity.appointment_time) {
                    $('#ts_time_' + arrTime[i]).removeClass("ts-time-select");
                    $('#ts_time_' + arrTime[i]).css("background", "#ff99cc");
                    return false;
                } else {
                    $('#ts_time_' + arrTime[i]).addClass("ts-time-select");
                    $('#ts_time_' + arrTime[i]).css("background", "");
                }
            });
        }

        $('#view_advice_date').attr('ts-data-date', selectDate);

        var viewDate = selectDate.substring(0, 4) + "년 " + selectDate.substring(4, 6) + "월 " + selectDate.substring(6, 8) + "일";

        $('#view_advice_date')[0].innerHTML = viewDate;
    });
    <#-- 달력에서 날짜 선택시 실행하는 스크립트 END -->

    <#-- 예약 시간 선택시 실행하는 스크립트 START -->
    $(document).on("click", '.ts-time-select', function() {
        if($('#view_advice_date').attr('ts-data-date') == '') {
            $('#pop-message').text("예약 날짜를 먼저 선택해 주세요.");
            popup_layer();
            return;
        }

        var $selectThis = $(this);

        $('.ts-time-select').each(function(index, value) {
            if($selectThis.attr("data-time") == $(this).attr("data-time")) {
                $(this).css("background", "#00aeaf");
            } else {
                $(this).css("background", "");
            }
        })

        $('#view_advice_time').attr('ts-data-time', $selectThis.attr("data-time"));

        $('#view_advice_time')[0].innerHTML = $selectThis.attr("data-time") + "시 00분";
    });
    <#-- 예약 시간 선택시 실행하는 스크립트 END -->

    <#-- 싸이케어테라피 전화번호 입력 제어 -->
    $(document).on("keyup", '#single_receive_phone', function() {
        $(this).val( $(this).val().replace(/[^0-9]/gi, "") );
    });
    <#-- 싸이케어테라피 전화번호 입력 제어 -->

    <#-- 싸이케어테라피 전화번호 입력 제어 -->
    $(document).on("keyup", '#couple_receive_phone', function() {
        $(this).val( $(this).val().replace(/[^0-9]/gi, "") );
    });
    <#-- 싸이케어테라피 전화번호 입력 제어 -->

    $(document).on("click", '.btn-precheck-userinfo-pretesting', function() {
        $('#precheck_userinfo_text').text("상담/검사에 필요한 기초 정보 및 인테이크지 검사 정보가 없습니다. 해당 정보 등록 페이지로 이동합니다.");
        $('#precheck_userinfo_modal').modal('show');
        return false;
    });

    $(document).on("click", '.btn-precheck-userinfo', function() {
        $('#precheck_userinfo_text').text("상담/검사에 필요한 기초 정보가 없습니다. 해당 정보 등록 페이지로 이동합니다.");
        $('#precheck_userinfo_modal').modal('show');
        return false;
    });

    $(document).on("click", '.btn-precheck-pretesting', function() {
        $('#precheck_pretesting_text').text("인테이크지 검사 정보가 없습니다. 해당 정보 등록 페이지로 이동합니다.");
        $('#precheck_pretesting_modal').modal('show');
        return false;
    });

    $(document).on("click", '#btn_precheck_userinfo_ok', function() {
        my.fn.pmove('${Request.contextPath}/user/product/precheck_add');
        return false;
    });

    $(document).on("click", '#btn_precheck_userinfo_cancel', function() {
        my.fn.pmove('${Request.contextPath}/user');
        return false;
    });

    $(document).on("click", '#btn_precheck_pretesting_ok', function() {
        my.fn.pmove('${Request.contextPath}/user/pretesting/addForm');
        return false;
    });

    $(document).on("click", '#btn_precheck_pretesting_cancel', function() {
        my.fn.pmove('${Request.contextPath}/user');
        return false;
    });

    function controller(target) {

        that = this;
        var m_oMonth = new Date();
        m_oMonth.setDate(1);

        this.init = function() {
            that.renderCalendar();
            that.initEvent();
        }

        this.renderCalendar = function() {
            $('.appointment-time').addClass("ts-time-select");
            $('.appointment-time').css("background", "");

            var arrTable = [];

            arrTable.push('<table>');
            arrTable.push('<colgroup>');

            for(var i=0; i<7; i++) {
                arrTable.push('<col width="200">');
            }
            arrTable.push('</colgroup>');
            arrTable.push('<thead>');
            arrTable.push('<tr>');

            var arrWeek = "일월화수목금토".split("");

            for(var i=0, len=arrWeek.length; i<len; i++) {
                var sClass = '';
                sClass += i % 7 == 0 ? 'sun' : '';
                sClass += i % 7 == 6 ? 'sat' : '';
                arrTable.push('<td class="'+sClass+'">' + arrWeek[i] + '요일</td>');
            }
            arrTable.push('</tr></thead>');
            arrTable.push('<tbody>');

            var oStartDt = new Date(m_oMonth.getTime());
            oStartDt.setDate(oStartDt.getDate() - oStartDt.getDay());

            var tempMonth = "";
            if(m_oMonth.getMonth() < 10) {
                tempMonth = "0" + (m_oMonth.getMonth()+1);
            } else {
                tempMonth = (m_oMonth.getMonth()+1);
            }

            var today = new Date();
            today.setDate(today.getDate() -1 );

            for(var i=0; i<100; i++) {
                if(i % 7 == 0) {
                    arrTable.push('<tr>');
                }

                var tempDay = "";
                if(oStartDt.getDate() < 10) {
                    tempDay = "0" + oStartDt.getDate();
                } else {
                    tempDay = oStartDt.getDate();
                }

                var tempDate = m_oMonth.getFullYear() + tempMonth + tempDay;

                var sClass = 'date-cell '
                sClass += m_oMonth.getMonth() != oStartDt.getMonth() ? 'not-this-month ' : '';
                sClass += i % 7 == 0 ? 'sun ' : '';
                sClass += i % 7 == 6 ? 'sat ' : '';

                var todayTime = today.getTime() + ( 60 * 60 * 24 * 1000 );

                if((todayTime < oStartDt.getTime()) && (m_oMonth.getMonth() == oStartDt.getMonth())) {
                    var equalCnt = 0;
                    $.each(appointmentEntites, function(index, appointmentEntity) {
                        if(tempDate == appointmentEntity.appointment_date) {
                            equalCnt++;
                        }
                    });

                    if(equalCnt == 12) {
                        sClass += 'impossible ';
                    } else {
                        sClass += 'possible ';
                    }
                } else {
                    sClass += 'impossible ';
                }

                arrTable.push('<td class="'+sClass+'" data-current="' + tempDate + '">' + oStartDt.getDate() + '</td>');

                oStartDt.setDate(oStartDt.getDate() + 1);

                if(i % 7 == 6) {
                    arrTable.push('</tr>');

                    if(m_oMonth.getMonth() != oStartDt.getMonth()) {
                        break;
                    }
                }
            }
            arrTable.push('</tbody></table>');

            $('#calendar').html(arrTable.join(""));

            that.changeMonth();
        }

        this.initEvent = function() {
            $('#btnPrev').click(that.onPrevCalendar);
            $('#btnNext').click(that.onNextCalendar);
        }

        this.onPrevCalendar = function() {
            m_oMonth.setMonth(m_oMonth.getMonth() - 1);

            var tempMonth = "";
            if(m_oMonth.getMonth() < 10) {
                tempMonth = "0" + (m_oMonth.getMonth()+1);
            } else {
                tempMonth = (m_oMonth.getMonth()+1);
            }
            currentYYYYMM = m_oMonth.getFullYear() + "" + tempMonth;

            var advisor_srl = 0;
            $('input:radio[name="ts_advisor_srl"]:checked').each(function() {
                advisor_srl = this.value;
            });

            $.ajax({
                type: "GET",
                url: '${Request.contextPath}/user/appointment/check/' + advisor_srl + '/' + currentYYYYMM,
                contentType : 'application/json',
                //data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    appointmentEntites = data.appointmentEntites;
                },
                error: function (xhr, ajaxOptions, thrownError) {},
                complete: function(xhr, status) {
                    that.renderCalendar();
                }
            });
        }

        this.onNextCalendar = function() {
            m_oMonth.setMonth(m_oMonth.getMonth() + 1);

            var tempMonth = "";
            if(m_oMonth.getMonth() < 10) {
                tempMonth = "0" + (m_oMonth.getMonth()+1);
            } else {
                tempMonth = (m_oMonth.getMonth()+1);
            }
            currentYYYYMM = m_oMonth.getFullYear() + "" + tempMonth;

            var advisor_srl = 0;
            $('input:radio[name="ts_advisor_srl"]:checked').each(function() {
                advisor_srl = this.value;
            });

            $.ajax({
                type: "GET",
                url: '${Request.contextPath}/user/appointment/check/' + advisor_srl + '/' + currentYYYYMM,
                contentType : 'application/json',
                //data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    appointmentEntites = data.appointmentEntites;
                },
                error: function (xhr, ajaxOptions, thrownError) {},
                complete: function(xhr, status) {
                    that.renderCalendar();
                }
            });
        }

        this.changeMonth = function() {
            $('#currentDate').text(that.getYearMonth(m_oMonth).substr(0,9));
        }

        this.getYearMonth = function(oDate) {
            return oDate.getFullYear() + '년 ' + (oDate.getMonth() + 1) + '월';
        }
    }

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->