<@ap.py_header_load_script />
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-mypage-progress-group-list">
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


        <div class="col-md-12 vendor-box vendor-box-grid"><!-- venue box start-->
            <div class="row">
                <div class="col-md-12 vendor-detail"><!-- venue details -->
                    <div class="caption"><!-- caption -->
                        <div class="col-md-4"><h2><a href="#" class="title">싸이케어테라피</a></h2></div>
                        <div class="col-md-6">
                            <p class="location">총 (12)주 중 (12)주차 서비스 이용 중</p>
                        </div>
                        <div class="col-md-2">
                            <a href="#" class="progress-list"> 상세 내역 보기 <i class="fa fa-angle-double-right"></i></a>
                        </div>

                    </div>
                    <!-- /.caption -->
                </div>
            </div>
            <!-- venue details -->
        </div>

        <div class="col-md-12 vendor-box vendor-box-grid"><!-- venue box start-->
            <div class="row">
                <div class="col-md-12 vendor-detail"><!-- venue details -->
                    <div class="caption"><!-- caption -->
                        <div class="col-md-4"><h2><a href="#" class="title">텍스트케어테라피</a></h2></div>
                        <div class="col-md-6">
                            <p class="location">총 (10)회 중 (2)회 상담 완료</p>
                        </div>
                        <div class="col-md-2">
                            <a href="#" class="progress-list"> 상세 내역 보기 <i class="fa fa-angle-double-right"></i></a>
                        </div>

                    </div>
                    <!-- /.caption -->
                </div>
            </div>
            <!-- venue details -->
        </div>

        <div class="col-md-12 vendor-box vendor-box-grid"><!-- venue box start-->
            <div class="row">
                <div class="col-md-12 vendor-detail"><!-- venue details -->
                    <div class="caption"><!-- caption -->
                        <div class="col-md-4"><h2><a href="#" class="title">컨텐츠케어테라피</a></h2></div>
                        <div class="col-md-6">
                            <p class="location">총 (22)회 중 (0)회 상담 완료</p>
                        </div>
                        <div class="col-md-2">
                            <a href="#" class="progress-list"> 상세 내역 보기 <i class="fa fa-angle-double-right"></i></a>
                        </div>

                    </div>
                    <!-- /.caption -->
                </div>
            </div>
            <!-- venue details -->
        </div>


    </div>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {

        $(".progress-list").click(function() {
            my.fn.pmove('${Request.contextPath}/user/mypage/progress/group/1');
        });

    });

</script>


<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->