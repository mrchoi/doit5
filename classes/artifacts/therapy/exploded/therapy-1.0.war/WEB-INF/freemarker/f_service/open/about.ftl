<@ap.py_header_load_script />


<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="about">
<#--<#assign _page_parent_name="">-->
<#assign _page_current_name="상담안내 소개">
<@ap.py_page_simple_depth icon="" parent="" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-3 side-nav" id="leftCol">
                <div class="hide-side">
                    <ul class="listnone nav" id="sidebar">
                        <li class="active"><a href="#team">상담 영역</a></li>
                        <li><a href="#aboutus" >상담 방법</a></li>
                        <li><a href="#history">상담 진행</a></li>
                    </ul>
                </div>
            </div>
            <div class="col-md-9 content-right">
                <div class="row">

                    <div class="col-md-12" id="team">
                        <h2>상담영역 소개</h2>
                    </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-3 text-center team-block">
                                <div class="team-pic"><!-- team pic -->
                                    <img src="${Request.svcResPath}images/area_01.jpg" class="img-responsive" alt=""> </div>
                                <!-- /.team pic -->
                                <h3>일과 업무</h3>
                                <span>일요일 밤만 되면 심장이 두근거리고 사람을 상대하느라 지쳐 다크써클이 무릎까지 내려와있다.</span>
                            </div>
                            <div class="col-md-3 text-center team-block">
                                <div class="team-pic"><!-- team pic -->
                                    <img src="${Request.svcResPath}images/area_02.jpg" class="img-responsive" alt=""> </div>
                                <!-- /.team pic -->
                                <h3>삶</h3>
                                <span>어쩌다 어른이 되어 준비없는 어른이 되어 하지말아야 할것, 해야할 것 투성이다.</span>
                            </div>
                            <div class="col-md-3 text-center team-block">
                                <div class="team-pic"><!-- team pic -->
                                    <img src="${Request.svcResPath}images/area_03.jpg" class="img-responsive" alt=""> </div>
                                <!-- /.team pic -->
                                <h3>부모</h3>
                                <span>부모도 자격과정이 있다면 좋겠다. 그럼 1종, 2종으로 구분해서 나눠 따고 싶다.</span>
                            </div>
                            <div class="col-md-3 text-center team-block">
                                <div class="team-pic"><!-- team pic -->
                                    <img src="${Request.svcResPath}images/area_04.jpg" class="img-responsive" alt=""> </div>
                                <!-- /.team pic -->
                                <h3>진로</a></h3>
                                <span>앞으로 뭘 하면서 살아야 하나? 정말 내가 하고싶은, 찾고 싶은 꿈이 있을까?</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-3 text-center team-block">
                                <div class="team-pic"><!-- team pic -->
                                    <img src="${Request.svcResPath}images/area_05.jpg" class="img-responsive" alt=""> </div>
                                <!-- /.team pic -->
                                <h3>대인관계</a></h3>
                                <span>차라리 혼자 외딴 섬에서 살고싶다. 어떤 말을 어떻게 시작해야할지 알 수가 없다.</span>
                            </div>
                            <div class="col-md-3 text-center team-block">
                                <div class="team-pic"><!-- team pic -->
                                    <img src="${Request.svcResPath}images/area_06.jpg" class="img-responsive" alt=""> </div>
                                <!-- /.team pic -->
                                <h3>학습과 시험</a></h3>
                                <span>세상에 시험이 없다면 좋겠다. 어떻게 해도 그저 불안하다. 공부를 아무리 해도 끝이 없다.</span>
                            </div>
                            <div class="col-md-3 text-center team-block">
                                <div class="team-pic"><!-- team pic -->
                                    <img src="${Request.svcResPath}images/area_07.jpg" class="img-responsive" alt=""> </div>
                                <!-- /.team pic -->
                                <h3>중독</a></h3>
                                <span>나의 삶을 지배하는 중독 몰입으로 힘들 때가 많다. 그러나 누구에게도 말하기가 두렵다.</span>
                            </div>
                            <div class="col-md-3 text-center team-block">
                                <div class="team-pic"><!-- team pic -->
                                    <img src="${Request.svcResPath}images/area_08.jpg" class="img-responsive" alt=""> </div>
                                <!-- /.team pic -->
                                <h3>분노와 충동성</a></h3>
                                <span>화가 나기 시작하거나 새로운 재미가 생기면 그것을 억누르고 참기가 어려워서 어떻게든 행동하게 된다.</span>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-12 aboutus" id="aboutus">
                        <h2>상담 방법 소개</h2>
                    </div>
                    <div class="col-md-12">
                        <div class="call-to-action well-box">
                            <h3>싸이케어테라피(Psy Care Therapy)</h3>
                            <img src="${Request.svcResPath}images/therapy_psy.jpg" alt="" class="graphic img-responsive">
                                싸이케어테라피는 휴대용 디바이스를 이용한 24시간 정신건강 모니터링 테라피입니다.<br>
                                싸이케어테라피는 생활에 밀착해 돌봄을 제공하는 웰니스 테라피입니다.<br>
                                싸이케어테라피는 현재 정신건강 상태를 점검한 최적화된 맞춤케어 테라피입니다.<br>
                                싸이케어테라피는 개인, 커플, 가족과 함께 할 수 있는 테라피입니다.<br>
                                싸이케어테라피는 싸이케어전문가와 함께하는 테라피입니다.
                        </div>

                        <#--<div class="call-to-action well-box">
                            <h3>커플싸이케어테라피(패밀리싸이케어테라피)</h2>
                            <img src="${Request.svcResPath}images/therapy_couple.jpg" alt="" class="graphic img-responsive">
                            <p> 커플 싸이케어테라피(패밀리 싸이케어서비스)는 서비스를 받고자 원하는 커플(가족)에게 각각 개별적으로 제공되는
                                테라피 이외에 부모와 자녀, 부부, 연인, 친구, 가족 간에 서로 마인드가 전하는 메세지를 통해서 관계 갈등이나 어려움 등을 극복할 수 있도록 도와드립니다.
                                또한 커플(가족)간에 서로 지지가 되어줄 수 있는 마인드 메세지를 공유할 수 있도록 해드립니다.
                            </p>
                        </div>-->
                        <div class="call-to-action well-box">
                            <h3>스카이프케어테라피(Skype Care Therapy) : 스카이프 상담</h3>
                            <img src="${Request.svcResPath}images/therapy_sky.jpg" alt="" class="graphic img-responsive">
                                스카이프케어테라피는 오프라인 상담의 장점과 온라인 상담의 장점을 혼합한 형태로 내담자가 원하는 순간 자신의 걱정거리를 전달하고 문제의 해결을 신속하게 해결하는 테라피입니다.<br>
                                스카이프케어테라피는 으뜸, 가장, 한마루 세 가지 종류와 마음리포트가 있습니다.
                        </div>
                        <div class="call-to-action well-box">
                            <h3>텍스트케어테라피(Text Care Therapy) : 온라인 1:1 카톡 상담</h3>
                            <img src="${Request.svcResPath}images/therapy_text.jpg" alt="" class="graphic img-responsive">
                                텍스트케어테라피는 1:1 카카오톡 대화형 테라피입니다.<br>
                                텍스트케어테라피는 으뜸, 가장, 한마루 세 가지 종류와 마음리포트가 있습니다.
                        </div>
                        <div class="call-to-action well-box">
                            <h3>컨텐츠케어테라피(Content Care Therapy) : 인문치유테라피</h3>
                            <img src="${Request.svcResPath}images/therapy_content.jpg" alt="" class="graphic img-responsive">
                                컨텐츠케어테라피는 인문형치유테라피로 내담자의 삶의 이야기를 바탕으로 미술치료, 이야기치료와 스토리텔링 형식을 활용한 테라피입니다.
                        </div>
                    </div>

                    <div class="col-md-12 history" id="history">
                        <h2>상담진행 소개</h2>
                    </div>
                    <div class="col-md-12">
                        <div class="call-to-action well-box">
                            플리마인드는 당신에게 꼭 맞는 맞춤 테라피를 제공하기 위해 문제나 걱정거리라고 생각되는 부분을 찾고 확인하는 일을 먼저 하겠습니다.<br>
                            정신과나 상담센터에서 문제를 찾기 위해 임상심리전문가의 도움을 받지만 모든 기관에 임상심리전문가가 상주하지 않습니다.<br>
                            플리마인드는 인테이크를 임상심리전문가 선생님이 진행하고 마인드가 전하는 메세지를 듣겠습니다.<br>
                            당신에게 제공 되어질 각각의 테라피는 현재 가지고 있는 문제가 무엇인지에 따라서 달라질 수 있으며 보다 적절한 테라피를 선택하실 수 있도록 도움을 드리겠습니다.<br>

                        </div>
                    </div>

                    <div class="col-md-12 tp-title-center form-group">
                        <a href="#" class="btn tp-btn-primary tp-btn-lg" id="checkup"> 검 사 예 약</a>
                        <a href="#" class="btn tp-btn-primary tp-btn-lg" id="adviceup"> 상 담 예 약</a>
                    </div>

                </div>

            </div>
        </div>
    </div>
</div>
<!-- /.main container -->

<!-- 하단에 위치에 있어야 오류 없이 정상적으로 동작함.  -->
<script src="${Request.svcResPath}js/offset.js"></script>


<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {

        $('#checkup').click(function(e){
            my.fn.pmove('${Request.contextPath}/user/product/checkupAddForm/0');
        });
        $('#adviceup').click(function(e){
            my.fn.pmove('${Request.contextPath}/user/product/adviceAddForm/0/0/0/0');
        });

    });

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->