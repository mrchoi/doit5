<@ap.py_header_load_script />
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-mypage-progress-list">
<#assign _page_parent_name="나의 상담 정보">
<#assign _page_current_name="현재 진행 중인 상담">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<div class="filter-box collapse in" id="searchform" aria-expanded="true"><!-- filter form -->
    <div class="container">
        <div class="row filter-form">
           <#-- <div class="col-md-12">
                <h2>MY PLYMIND</h2>
            </div>-->
            <form>
                <div class="col-md-3 text-center">
                    현재 진행중인 상담<h1>2건</h1>
                </div>
                <div class="col-md-3 text-center">
                    신규 심리 검사<h1>2건</h1>
                </div>
                <div class="col-md-3 text-center">
                    신규 결과 보고서<h1>2건</h1>
                </div>
                <div class="col-md-3 text-center">
                    1:1 답변 확인<h1>2건</h1>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- /. Filter form -->

<div class="main-container"><!-- main container -->
    <div class="container">

        <div class="row">
            <div class="col-md-12">
                <blockquote>${_page_current_name}</blockquote>
            </div>
        </div>

        <div class="col-md-12">
            <div class="well-box">
                <div class="coupon-info">
                    <p>컨텐츠케어테라피<a href="#"> </a>
                        <a role="button" data-toggle="collapse" href="#applycoupon" aria-expanded="false" aria-controls="applycoupon"> 2015년 12월 23일 월요일 / </a>
                        <a role="button" data-toggle="collapse" href="#applycoupon2" aria-expanded="false" aria-controls="applycoupon2"> 2015년 12월 24일 목요일 /</a>
                        <a role="button" data-toggle="collapse" href="#applycoupon3" aria-expanded="false" aria-controls="applycoupon3"> 2015년 12월 25일 금요일 </a>
                    </p>
                </div>
                <div class="collapse" id="applycoupon">
                    <div class="coupon-form">
                        <form class="form-inline">
                            <div class="form-group">
                                <label for="coupon" class="control-label sr-only">Name</label>
                                <p>2015년 12월 23일 월요일 </p>
                                STEP1 : 하단 버튼을 클릭하여 검사지를 다운 받아주세요.
                                <a href="#" class="btn tp-btn-default">  <i class="fa fa-save"></i> 사전 검사지 다운로드</a>
                                <p>STEP2 : 작성 완료 후 하단 버튼을 클릭하여 검사지를 첨부해주세요.</p>
                                <input id="attach" name="attach" type="file" class="form-control" >
                            </div>

                        </form>
                    </div>
                </div>
                <div class="collapse" id="applycoupon2">
                    <div class="coupon-form">
                        <form class="form-inline">
                            <div class="form-group">
                                <label for="coupon" class="control-label sr-only">Name</label>
                                <p>2015년 12월 24일 목요일 </p>
                                오늘의 한줄 인문 컨텐츠 1 10:00<br>
                                오늘의 한줄 인문 컨텐츠 2 10:00<br>
                                오늘의 한줄 인문 컨텐츠 3 10:00<br>
                                오늘의 한줄 인문 컨텐츠 3 10:00
                            </div>
                        </form>
                    </div>
                </div>
                <div class="collapse" id="applycoupon3">
                    <div class="coupon-form">
                        <form class="form-inline">
                            <div class="form-group">
                                <label for="coupon" class="control-label sr-only">Name</label>
                                <p>2015년 12월 25일 금요일 </p>
                                STEP1 : 하단 버튼을 클릭하여 활동지를 다운 받아주세요.
                                <a href="#" class="btn tp-btn-default">  <i class="fa fa-save"></i> 활동지 다운로드</a>
                                <p>STEP2 : 작성 완료 후 하단 버튼을 클릭하여 활동지를 첨부해주세요.</p>
                                <input id="attach" name="attach" type="file" class="form-control" >
                            </div>

                        </form>
                    </div>
                </div>


            </div>
        </div>


        <div class="col-md-12 vendor-box vendor-box-grid"><!-- venue box start-->
            <div class="row">
                <div class="col-md-12 vendor-detail"><!-- venue details -->
                    <div class="caption"><!-- caption -->
                        <div class="col-md-4"><h2><a href="#" class="title">컨텐츠케어테라피</a></h2></div>
                        <div class="col-md-6">
                            <p class="location">총 (22)회 중 (0)회 상담 완료</p>
                        </div>

                    </div>
                    <!-- /.caption -->
                    <div class="vendor-price">
                        <p>STEP1 : 하단 버튼을 클릭하여 검사지를 다운 받아주세요.</p>
                        <a href="#" class="btn tp-btn-default">  <i class="fa fa-save"></i> 사전 검사지 다운로드</a>
                    </div>
                    <div class="vendor-price">
                        <p>STEP2 : 작성 완료 후 하단 버튼을 클릭하여 검사지를 첨부해주세요.</p>
                        <input id="attach" name="attach" type="file" class="form-control" >


                    </div>
                </div>
            </div>
            <!-- venue details -->
        </div>

    </div>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {

        $(".progress-group-list").click(function() {
            my.fn.pmove('${Request.contextPath}/user/mypage/progress/group/1');
        });

    });

</script>


<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->