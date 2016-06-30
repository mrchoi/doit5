<@ap.py_header_load_script /><#--  -->
<script src="${Request.svcResPath}js/bootstrap/bootstrap-modal.js"></script>
<style>

    .modal-dialog {
        /*height: 70% !important;*/
        padding-top:10%;
    }

</style>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="company_introduce">
<#assign _page_parent_name="기관 소개">
<#assign _page_current_name="기관 소개">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<div class="main-container"><!-- main container -->
    <div class="container">

        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h2>플리마인드 설립 취지</h2>
                    </div>
                    <div class="col-md-12">
                        <div class="call-to-action well-box">
                            안녕하십니까?
                            <span style="text-decoration: underline;">플리마인드</span> 대표 정혜인입니다.<br>
                            <span style="text-decoration: underline;">플리마인드</span>는 사단법인 <span style="font-weight: 600;text-decoration: underline;">한국심리건강진흥원</span>의 부설기관으로 현대인의 심리건강을 우려하고 사회 각 분야에 산재한 심리적 취약성을 고민하는 기관입니다.<br>
                            이를 위해 보다 심도 있는 분석과 다양한 연구를 진행하여 심리건강 프로그램을 개발 및 보급하고자 합니다. 또한 <span style="text-decoration: underline;">플리마인드</span>는 사회적 소외계층과 취약계층을 위한 보급사업과 장애인을 위한 심리건강을 지원하기 위한 사업에도 동참하겠습니다.<br>
                            전문가뿐만 아니라 자원봉사자를 발굴 연계하는 사업, 사회서비스를 개발 및 보급하는 사업, 교육복지 투자 사업에도 참여하겠습니다. 또한 전 국민의 심리건강 증진을 위해 지역적, 국제적 상호교류 협력 사업을 진행하고 발굴하도록 노력하겠습니다.<br>
                            <span style="text-decoration: underline;">플리마인드</span>는 당신의 마인드에 귀 기울이는 당신만의 테라피가 되도록 최선을 다하겠습니다.
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <br>
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h2>플리마인드 소개</h2>
                    </div>
                    <div class="col-md-12">
                        <div class="call-to-action well-box">
                            플리마인드(PlyMind: PLay in Your Mind)는 문제가 있어도 힘들다는 생각이 들어도 상담센터를 찾는데<br>
                            엄청난 용기가 필요한 당신만을 위한 테라피입니다.<br>
                            우리의 마인드는 수많은 메세지로 힘듦을 표현하지만 그것을 놓치지 않고 공감하기는 쉽지 않습니다.<br>
                            <span style="text-decoration: underline;">플리마인드</span>는 첫 걸음을 떼기 어려운 여러분들의 동반자가 되어 치유될 수 있는 더 좋은 방법을 찾아드릴 것입니다.<br>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h2>서비스 제휴 문의</h2>
                    </div>
                    <div class="col-md-12">
                        <div class="call-to-action well-box">
                            <h3>기관에서 단체로 상담을 요청 하실 수 있습니다.</h3>
                            <h3>서비스 제휴 신청서를 작성 하신 뒤에 메일을 보내주시기 바랍니다.</h3>
                            <a href="mailto:plymind@plymind.com" class="btn tp-btn-default tp-btn-lg">이 메 일 보 내 기</a>

                            <a href="${applicationFormFileURL}" class="btn tp-btn-default tp-btn-lg"><i class="fa fa-download"></i> 제휴 신청서 다운로드</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-12">
                        <h2>플리마인드 BI 소개</h2>
                    </div>
                    <div class="col-md-12">
                        <div class="call-to-action well-box">
                            <div class="col-md-2" style="background-color:#00aeaf;margin:10px;padding:10px 10px 10px 25px">
                                <img src="${Request.svcResPath}images/logo.png" alt="PlyMind" class="img-responsive">
                             </div>
                                플리마인드 BI
                                <#--바탕의 핑크는 인간에게 가장 안전하고 완벽한 보호의 공간, 자궁을 상징하는 내벽의 색입니다.
                                태아는 핑크색으로 둘러싸인 자궁속에서 둘러싸여 보호받고, 그 벽을 보며 안심하고 지낸 기억을 가집니다.
                                따라서 우리는 핑크색으로부터 편안함과 안정감을 느끼고, 공격적인 감성을 부드럽게 감싸주는 정화를 경험합니다.
                                녹색의 하트는 알파벳 "M"을 형상화 합니다.
                                하트 테두리의 녹색은 치유의 색으로 힐링과 자연이 주는 평온함을 가지게 합니다.
                                아래 녹색하트(M)의 잘려진 부분은 상담자가 내담자의 상처난 마음(하트)을 이어주길 기원하는 것입니다.-->
                                녹색은 치유의 색으로 힐링과 자연이 주는 평온함을 가지게 합니다.<br>
                                녹색하트의 (M)은 편안함과 안정감을 느끼고, 부드럽게 감싸주는 정화를 경험합니다.<br>
                                하트 우측 라인은 상담자가 내담자의 상처난 마음(하트)을 이어주길 기원하는 것입니다.
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="row">
            <div class="col-md-12 tp-title-center form-group">
                <a href="#" class="btn tp-btn-primary tp-btn-lg " data-toggle="modal" data-target="#basicModal1"> 이  용  약  관 </a>
                <a href="#" class="btn tp-btn-primary tp-btn-lg" data-toggle="modal" data-target="#basicModal2"> 개인 정보 취급 방침</a>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h2>협력기관</h2>
                    </div>
                    <div class="col-md-12">
                        <#--<div class="call-to-action well-box">
                        </div>-->
                        <div class="spacer feature-section well-box" style="margin-bottom:0px;"><!-- Feature Blog Start -->
                            <#--<div class="container">-->
                                <div class="row"><!-- feature center -->
                                    <div class="col-md-4 feature-block" align="center"><!-- feature block -->
                                        <div class="feature-icon" style="width: 250px; height: 75px; vertical-align: middle;">
                                            <img id="kpsyhco" src="${Request.svcResPath}images/organization_chart_01_temp.jpg" alt="한국심리건강진흥원" class="graphic img-responsive" style="cursor: pointer;">
                                        </div>
                                    <#--<h3>한국심리건강진흥원</h3>-->
                                        <#--<p><a href="http://kpsyhco.co.kr/" target="_blank">http://kpsyhco.co.kr/</a></p>-->
                                    </div><!-- /.feature block -->
                                    <div class="col-md-4 feature-block" align="center"><!-- feature block -->
                                        <div class="feature-icon" style="width: 250px; height: 75px; vertical-align: middle;">
                                            <img id="smphc" src="${Request.svcResPath}images/organization_chart_02_temp.png" alt="새미래심리건강연구소" class="graphic img-responsive" style="cursor: pointer;">
                                        </div>
                                    <#--<h3>새미래심리건강연구소</h3>-->
                                        <#--<p><a href="http://www.smphc.org/" target="_blank">http://www.smphc.org/</a></p>-->
                                    </div><!-- /.feature block -->
                                    <div class="col-md-4 feature-block" align="center"><!-- feature block -->
                                        <div class="feature-icon" style="width: 250px; height: 75px; ">
                                            <img id="fpsp" src="${Request.svcResPath}images/organization_chart_03_temp.png" alt="한국FPSP" class="graphic img-responsive" style="cursor: pointer;">
                                        </div>
                                    <#--<h3>한국 FPSP</h3>-->
                                        <#--<p><a href="http://www.fpsp.or.kr/" target="_blank">http://www.fpsp.or.kr/</a></p>-->
                                    </div><!-- /.feature block -->

                                </div><!-- /.feature center -->

                                <div class="row"><!-- feature center -->
                                    <div class="col-md-4 feature-block"><!-- feature block -->
                                        <div class="feature-icon" style="width: 250px; height: 75px; ">
                                            <img id="haeyumbaum" src="${Request.svcResPath}images/organization_chart_04_temp.png" alt="해윰바움" class="graphic img-responsive" style="cursor: pointer;">
                                        </div>
                                    <#--<h3>해윰바움</h3>-->
                                    <#--<p><a href="http://cafe.naver.com/haeyumbaum" target="_blank">http://cafe.naver.com/haeyumbaum</a></p>-->
                                    </div><!-- /.feature block -->
                                    <div class="col-md-4 feature-block"><!-- feature block -->
                                        <div class="feature-icon" style="width: 250px; height: 75px; ">
                                            <img id="saraskey" src="${Request.svcResPath}images/organization_chart_05_temp.PNG" alt="사라의열쇠" class="graphic img-responsive" style="cursor: pointer;">
                                        </div>
                                    <#--<h3>해윰바움</h3>-->
                                    <#--<p><a href="http://cafe.daum.net/saraskey/" target="_blank">http://cafe.daum.net/saraskey/</a></p>-->
                                    </div><!-- /.feature block -->
                                    <div class="col-md-4 feature-block"><!-- feature block -->
                                        <div class="feature-icon"></div>
                                    </div><!-- /.feature block -->
                                </div><!-- /.feature center -->


                            <#--</div>-->
                        </div><!-- Feature Blog End -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- /.main container -->


<@ap.py_bstrap_modal_access_terms modalId="basicModal1" formId="modal-document-link-form" title="이  용  약  관" okButtonCheck=false>
</@ap.py_bstrap_modal_access_terms>

<@ap.py_bstrap_modal_privacy_policy modalId="basicModal2" formId="modal-document-link-form" title="개인 정보 취급 방침" okButtonCheck=false>
</@ap.py_bstrap_modal_privacy_policy>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    $(document).ready(function() {
        $('#kpsyhco').click(function(e){
            e.preventDefault();
            window.open("http://kpsyhco.co.kr/", "_blank");
        });

        $('#smphc').click(function(e){
            e.preventDefault();
            window.open("http://www.smphc.org/", "_blank");
        });

        $('#fpsp').click(function(e){
            e.preventDefault();
            window.open("http://www.fpsp.or.kr/", "_blank");
        });

        $('#haeyumbaum').click(function(e){
            e.preventDefault();
            window.open("http://cafe.naver.com/haeyumbaum", "_blank");
        });

        $('#saraskey').click(function(e){
            e.preventDefault();
            window.open("http://cafe.daum.net/saraskey/", "_blank");
        });
    });
</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
