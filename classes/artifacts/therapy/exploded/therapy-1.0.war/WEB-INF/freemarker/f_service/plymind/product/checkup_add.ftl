<@ap.py_header_load_script />
<link rel="stylesheet" type="text/css" media="screen" href="${Request.svcResPath}css/smartadmin-production-plugins.min.css">
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
<#assign _page_current_name="심리검사 신청">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<!------------------------------------------------------------------------------------------->
<div class="main-container">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <blockquote>심리검사 소개</blockquote>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="call-to-action well-box" style=" text-align: left;">
                    플리마인드에 있는 검사는
                    당신의 현재 스트레스 정도와 적응능력을 알아보는 검사,
                    학습스타일과 업무능력 스타일을 알아볼 수 있는 검사,
                    당신이 실생활에서 얼마나 어떻게 창의적인지를 알아보는 검사,
                    스트레스 상황에서 회복탄력성의 정도를 알아볼 수 있는 검사들입니다.
                    각각의 검사는 당신의 현재 문제를 파악하는데 있어서 다른 시각으로 보게 되므로 같은 문제를 지닌 내담자가 다른 검사를 실시하게 된다면 파악할 수 있는 측면이 달라지게 됩니다.
                    따라서 당신이 어떤 검사를 실시하느냐에 따라 결과는 달라질 수 있습니다.
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <blockquote><a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">심리검사 진행 절차</a></blockquote>
            </div>
        </div>

        <div id="collapseOne" class="row panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
            <div class="col-md-12">
                <div class="well-box">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP1. 심리검사 선택 : 내담자는 원하는 심리 검사 항목을 선택합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP2. 검사비용 입금 : 심리검사 비용을 확인하고 입금계좌 확인 후 입금합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP3. 입금확인 안내 : 운영자가 입금 확인 후 심리검사 이용안내 문자를 발송합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP4. 심리검사 : 내담자가 신청한 심리검사를 진행합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP5. 검사결과 분석 : 임상 전문가가 검사 결과를 분석 후 보고서를 작성합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP6. 보고서 확인 : 내담자는 마이페이지에서 보고서를 확인합니다.
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="well">
                                STEP7. 심리상담 신청 : 내담자가 검사 후 상담을 진행하려면 신청서를 작성 후 등록합니다.
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <blockquote>심리검사 항목 선택</blockquote>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">
                항목 선택
            </div>
            <div class="col-md-9">
                <div class="inline-group">
                    <#assign cnt=0?int>
                    <#list productEntities as productEntity>
                    <label for="${productEntity.product_srl}">
                        <input type="radio" name="product_srl"
                               id="${productEntity.product_srl}"
                               value="${productEntity.product_srl}"
                               data-conetns-srl="${productEntity.contents_srl}"
                               class="styled"
                               <#if productEntity.product_srl == product_srl>checked</#if><#if product_srl == 0 && cnt == 0>checked</#if>> ${productEntity.title}
                    </label>&nbsp;&nbsp;&nbsp;
                    <#assign cnt=cnt+1?int>
                    </#list>
                </div>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">
                검사 설명
            </div>
            <div class="col-md-9">
                <div class="tab-content">
                    <#assign cnt=0?int>
                    <#list productEntities as productEntity>
                    <div role="tabpanel" class="tab-pane <#if productEntity.product_srl == product_srl>active</#if><#if product_srl == 0 && cnt==0>active</#if>" id="content-${productEntity.product_srl}">
                        ${productEntity.description}
                    </div>
                    <#assign cnt=cnt+1?int>
                    </#list>
                </div>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-3">
                검사 요금안내
            </div>
            <div class="col-md-9">
                모든 검사는 각각 1만원
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-12 tp-title-center form-group">
                <#if checkMap.checkUserInfo?string != "true" && checkMap.checkPretesting?string != "true">
                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo-pretesting">심리검사 신청</button>
                <#elseif checkMap.checkUserInfo?string != "true">
                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-userinfo">심리검사 신청</button>
                <#elseif checkMap.checkPretesting?string != "true">
                    <button class="btn tp-btn-default tp-btn-lg btn-precheck-pretesting">심리검사 신청</button>
                <#else>
                    <button id="btn_application" name="btn_application" class="btn tp-btn-default tp-btn-lg">심리검사 신청</button>
                </#if>
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

    $(document).ready(function() {
        precheck();

        $(".styled").click(function() {
            $('input:radio[name="product_srl"]').each(function() {
                if($(this).is(':checked')) {
                    $('#content-' + this.value).addClass('active');
                } else {
                    $('#content-' + this.value).removeClass('active');
                }
            });
        });

        $("#btn_application").click(function() {
            var contents_srl = "";
            var product_srl = "";
            $('input:radio[name="product_srl"]:checked').each(function() {
                var $this = $(this);
                contents_srl = $this.attr('data-conetns-srl');

                product_srl = this.value;
            });

            var advice_price = 10000;

            var dataString = {
                "contents_srl"      : contents_srl,
                "product_srl"       : product_srl,
                "advice_type"       : 0,
                "advice_period"     : 0,
                "advice_number"     : 1,
                "advice_price"      : advice_price,
                "advisor_srl"       : 0,
                "start_date"        : '',
                "push_date"         : '',
                "push_time"         : '',
                "appointment_date"  : '',
                "appointment_time"  : '',
                "receive_name"      : '',
                "receive_address"   : '',
                "receive_phone"     : '',
                "member_srls"       : null
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/product/adviceAdd',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    if(data.resultCode > 0) {
                        $('#pop-message').text("심리검사 신청이 정상적으로 이루어졌습니다.");
                        popup_layer();

                        timer = setInterval(function() {
                            my.fn.pmove('${Request.contextPath}/user/payment/paymentForm/' + advice_price + "/" + data.resultCode);
                        }, 1500);
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("심리검사 신청이 실패되었습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });
        });
    });

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

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->