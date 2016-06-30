<@ap.py_header_load_script />
<style>
.vendor-detail .caption{
    text-align: center;
}
</style>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-product-list">
<#assign _page_parent_name="상담 비용 안내">
<#assign _page_current_name="상담 비용 안내 목록">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<div class="main-container"><!-- main container -->
    <div class="container">

        <div class="row">
            <div class="col-md-12">
                <blockquote>상담 비용 안내</blockquote>
            </div>
        </div>

        <div class="st-tabs">
            <div class="row">
                <div class="col-md-12">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="contents-srl active" value="1" id="tab-1"><a href="#single_therapy" aria-controls="single_therapy" role="tab" data-toggle="tab">싸이케어테라피</a></li>
                        <li role="presentation" class="contents-srl" value="2" id="tab-2"><a href="#couple_therapy" aria-controls="couple_therapy" role="tab" data-toggle="tab">커플/패밀리싸이케어테라피</a></li>
                        <li role="presentation" class="contents-srl" value="3" id="tab-3"><a href="#skype_therapy" aria-controls="skype_therapy" role="tab" data-toggle="tab">스카이프케어테라피</a></li>
                        <li role="presentation" class="contents-srl" value="4" id="tab-4"><a href="#text_therapy" aria-controls="text_therapy" role="tab" data-toggle="tab">텍스트케어테라피</a></li>
                        <li role="presentation" class="contents-srl" value="5" id="tab-5"><a href="#contents_therapy" aria-controls="contents_therapy" role="tab" data-toggle="tab">컨텐츠케어테라피</a></li>
                    </ul>

                    <!-- Tab panes -->
                    <div class="tab-content">
                        <!-- 싸이케어테라피 ----------------------------------------------------------------------------->
                        <div role="tabpanel" class="tab-pane active" id="single_therapy">
                            <#--<div class="row col-md-12">
                                싸이케어테라피 비용안내
                            </div>-->
                            <div class="row">
                                <#list 1..4 as X>
                                    <div class="col-md-3 vendor-box pull-center"><!-- venue box start-->
                                        <div class=""><!-- venue pic -->
                                            <a href="#"><img src="${Request.svcResPath}images/product_psy_0${X}.jpg" alt="wedding venue" class="img-responsive"></a>
                                        </div>
                                        <!-- /.venue pic -->
                                        <div class="vendor-detail"><!-- venue details -->
                                            <div class="caption"><!-- caption -->
                                                <button id="btn_psy_therapy_${X}" class="btn btn-default tp-btn-lg">신청하기</button>
                                            </div>
                                            <!-- /.caption -->
                                        </div>
                                        <!-- venue details -->
                                    </div>
                                    <!-- /.venue box start-->
                                </#list>
                            </div>
                        </div>
                        <!-- 커플싸이케어테라피 --------------- --------------------------------------------------------->
                        <div role="tabpanel" class="tab-pane" id="couple_therapy">
                            <#--<div class="row col-md-12">
                                커플/패밀리 싸이케어테라피
                            </div>-->
                            <div class="row">
                                <#list 1..4 as X>
                                    <div class="col-md-3 vendor-box pull-center"><!-- venue box start-->
                                        <div class=""><!-- venue pic -->
                                            <a href="#"><img src="${Request.svcResPath}images/product_couple_0${X}.jpg" alt="wedding venue" class="img-responsive"></a>
                                        </div>
                                        <!-- /.venue pic -->
                                        <div class="vendor-detail"><!-- venue details -->
                                            <div class="caption"><!-- caption -->
                                                <button id="btn_couple_therapy_${X}" class="btn btn-default tp-btn-lg">신청하기</button>
                                            </div>
                                            <!-- /.caption -->
                                        </div>
                                        <!-- venue details -->
                                    </div>
                                    <!-- /.venue box start-->
                                </#list>
                            </div>
                        </div>
                        <!-- 스카이프케어테라피  ------------------------------------------------------------------------->
                        <div role="tabpanel" class="tab-pane" id="skype_therapy">
                            <#--<div class="row col-md-12">
                                스카이프케어테라피
                            </div>-->
                            <div class="row">
                            <#list 1..3 as X>
                                <div class="col-md-4 vendor-box pull-center"><!-- venue box start-->
                                    <div class=""><!-- venue pic -->
                                        <a href="#"><img src="${Request.svcResPath}images/product_sky_0${X}.jpg" alt="wedding venue" class="img-responsive"></a>
                                    </div>
                                    <!-- /.venue pic -->
                                    <div class="vendor-detail"><!-- venue details -->
                                        <div class="caption"><!-- caption -->
                                            <button id="btn_sky_therapy_${X}" class="btn btn-default tp-btn-lg">신청하기</button>
                                        </div>
                                        <!-- /.caption -->
                                    </div>
                                    <!-- venue details -->
                                </div>
                                <!-- /.venue box start-->
                            </#list>
                            </div>
                        </div>
                        <!-- 텍스트케어테라피 --------------------------------------------------------------------------->
                        <div role="tabpanel" class="tab-pane" id="text_therapy">
                            <#--<div class="row col-md-12">
                                텍스트케어테라피
                            </div>-->
                            <div class="row">
                            <#list 1..3 as X>
                                <div class="col-md-4 vendor-box pull-center"><!-- venue box start-->
                                    <div class=""><!-- venue pic -->
                                        <a href="#"><img src="${Request.svcResPath}images/product_text_0${X}.jpg" alt="wedding venue" class="img-responsive"></a>
                                    </div>
                                    <!-- /.venue pic -->
                                    <div class="vendor-detail"><!-- venue details -->
                                        <div class="caption"><!-- caption -->
                                            <button id="btn_text_therapy_${X}" class="btn btn-default tp-btn-lg">신청하기</button>
                                        </div>
                                        <!-- /.caption -->
                                    </div>
                                    <!-- venue details -->
                                </div>
                                <!-- /.venue box start-->
                            </#list>
                            </div>
                        </div>
                        <!-- 컨텐츠케어테라피 --------------------------------------------------------------------------->
                        <div role="tabpanel" class="tab-pane" id="contents_therapy">
                            <#--<div class="row col-md-12">
                                컨텐츠케어테라피
                            </div>-->
                            <div class="row">
                            <#list 1..4 as X>
                                <div class="col-md-3 vendor-box pull-center"><!-- venue box start-->
                                    <div class=""><!-- venue pic -->
                                        <a href="#"><img src="${Request.svcResPath}images/product_content_0${X}.jpg" alt="wedding venue" class="img-responsive"></a>
                                    </div>
                                    <!-- /.venue pic -->
                                    <div class="vendor-detail"><!-- venue details -->
                                        <div class="caption"><!-- caption -->
                                            <button id="btn_content_therapy_${X}" class="btn btn-default tp-btn-lg">신청하기</button>
                                        </div>
                                        <!-- /.caption -->
                                    </div>
                                    <!-- venue details -->
                                </div>
                                <!-- /.venue box start-->
                            </#list>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {
        var contents_srl = 0;
        var advice_period = 0;
        var advice_type = 0;

        <#list 1..4 as X>
            $('#btn_psy_therapy_'+${X}).click(function(e){
                contents_srl = 1;

                if(${X} == 1) {
                    advice_period = 4;
                } else if(${X} == 2) {
                    advice_period = 12;
                } else if(${X} == 3) {
                    advice_period = 24;
                } else {
                    advice_period = 52;
                }

                <@am.jsevent_prevent />
                my.fn.pmove('${Request.contextPath}/user/product/adviceAddForm/' + contents_srl + '/' + advice_period + '/' + advice_type + '/0');
            });

            $('#btn_couple_therapy_'+${X}).click(function(e){
                contents_srl = 2;

                if(${X} == 1) {
                    advice_period = 4;
                } else if(${X} == 2) {
                    advice_period = 12;
                } else if(${X} == 3) {
                    advice_period = 24;
                } else {
                    advice_period = 52;
                }

                <@am.jsevent_prevent />
                my.fn.pmove('${Request.contextPath}/user/product/adviceAddForm/' + contents_srl + '/' + advice_period + '/' + advice_type + '/0');
            });

            $('#btn_sky_therapy_'+${X}).click(function(e){
                contents_srl = 3;

                if(${X} == 1) {
                    advice_type = 1;
                } else if(${X} == 2) {
                    advice_type = 2;
                } else {
                    advice_type = 3;
                }

                <@am.jsevent_prevent />
                my.fn.pmove('${Request.contextPath}/user/product/adviceAddForm/' + contents_srl + '/' + advice_period + '/' + advice_type + '/0');
            });

            $('#btn_text_therapy_'+${X}).click(function(e){
                contents_srl = 4;

                if(${X} == 1) {
                    advice_type = 1;
                } else if(${X} == 2) {
                    advice_type = 2;
                } else {
                    advice_type = 3;
                }

                <@am.jsevent_prevent />
                my.fn.pmove('${Request.contextPath}/user/product/adviceAddForm/' + contents_srl + '/' + advice_period + '/' + advice_type + '/0');
            });

            $('#btn_content_therapy_'+${X}).click(function(e){
                contents_srl = 5;

                if(${X} == 1) {
                    advice_period = 1;
                    advice_type = 4;
                } else if(${X} == 2) {
                    advice_period = 1;
                    advice_type = 22;
                } else if(${X} == 3) {
                    advice_period = 4;
                    advice_type = 88;
                } else {
                    advice_period = 12;
                    advice_type = 264;
                }

                <@am.jsevent_prevent />
                my.fn.pmove('${Request.contextPath}/user/product/adviceAddForm/' + contents_srl + '/' + advice_period + '/' + advice_type + '/0');
            });
        </#list>


    });

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->