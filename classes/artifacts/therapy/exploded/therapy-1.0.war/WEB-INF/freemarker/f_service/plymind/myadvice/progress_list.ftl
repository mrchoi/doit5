<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js"></script>
<script src="${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js"></script>
<style>
    .dataTables_filter .input-group-addon {
        width: 50px;
        margin-top: 0;
        float: left;
        height: 47px;
        padding-top: 14px;
        border: 1px solid #ccc;
    }
    input.form-control{
        margin-left:0px;
    }
</style>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-advice-progress-list">
<#assign _page_parent_name="마이페이지">
<#assign _page_current_name="현재 진행중인 상담">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />

<div class="filter-box collapse in" id="searchform" aria-expanded="true"><!-- filter form -->
    <div class="container">
        <div class="row filter-form">
            <form>
                <div id="present_advice" class="col-md-3 text-center">
                    <a href="#">현재 진행중인 상담<h1>${presentMap.advice_cnt}건</h1></a>
                </div>
                <div id="present_checkup" class="col-md-3 text-center">
                    <a href="#">심리 검사<h1>${presentMap.checkup_cnt}건</h1></a>
                </div>
                <div id="present_complete" class="col-md-3 text-center">
                    <a href="#">결과 보고서<h1>${presentMap.complete_cnt}건</h1></a>
                </div>
                <div id="present_one" class="col-md-3 text-center">
                    <a href="#">1:1 답변 확인<h1>${presentMap.one_cnt}건</h1></a>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <blockquote>${_page_current_name}</blockquote>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12 view-cart"><!-- view cart -->
                <table class="shop_table" id="${_page_id}" >
                    <!-- shop table -->
                    <thead>
                    <tr>
                        <th rclass="text-center" style="max-width:50px;min-width:50px;">번호</th>
                        <th rclass="text-center" style="min-width:50px;">상담내역</th>
                        <th class="text-center" style="max-width:100px;min-width:100px;">회차</th>
                    </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {

        $("#present_advice").click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/myadvice/progress_list');
        });

        $("#present_checkup").click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/mycheckup/list');
        });

        $("#present_complete").click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/mycomplete/list');
        });

        $("#present_one").click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/one/list');
        });

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };

    <#--<@ap.py_jsnoti title="파일 업로드" content="업로드 성공 했습니다." contenttype="val" boxtype="info" />-->

    <@ap.py_jsdatatable listId="${_page_id}" url="${Request.contextPath}/user/myadvice/progress_list/t/" ; job>
        <#if job == "order">
                [[0, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'application_srl', mData: 'application_srl', sClass: 'text-center', bSortable: false, bSearchable: false},
                {sName: 'title', mData: 'title', sClass: 'text-center', bSortable: false, bSearchable: true,
                    mRender: function(data, type, full, dtObj) {
                        var viewStr = "";

                        viewStr += full.title + " ";

                        if(full.kind == '1') {
                            if (full.advice_type == '1') {
                                viewStr += " - 으뜸 " + full.advice_number + "회 ";
                            }
                            if (full.advice_type == '2') {
                                viewStr += " - 가장 " + full.advice_number + "회 ";
                            }
                            if (full.advice_type == '3') {
                                viewStr += " - 한마루 " + full.advice_number + "회 ";
                            }

                            if (full.advice_period == '1' && full.advice_number == '4') {
                                viewStr += " - 1일 ( 4회 ) ";
                            }
                            if (full.advice_period == '1' && full.advice_number == '22') {
                                viewStr += " - 1주 ";
                            }
                            if (full.advice_period == '1' && full.advice_number == '1') {
                                viewStr += " - 1주 ";
                            }
                            if (full.advice_period == '4') {
                                viewStr += " - 4주 ( 1개월 ) ";
                            }
                            if (full.advice_period == '12') {
                                viewStr += " - 12주 ( 3개월 ) ";
                            }
                            if (full.advice_period == '24') {
                                viewStr += " - 24주 ( 6개월 ) ";
                            }
                            if (full.advice_period == '52') {
                                viewStr += " - 52주 ( 12개월 ) ";
                            }
                        }

                        return '<a class="application-detail-view" data-application-group="' + full.application_group + '" style="cursor: pointer;">' + viewStr + '</a>';
                        //return viewStr;
                    }
                },
                {sName: 'eachApplicationEntities', mData: 'eachApplicationEntities', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full, dtObj) {
                        var view_str = "";
                        var eachApplicationEntities = data;

                        view_str += '<select id="' + full.application_group + '_application_srl" style="width:100px;">';
                        view_str += '<option value="0">전체</option>';
                        $.each(eachApplicationEntities, function (index, eachApplicationEntity) {
                            view_str += '<option value="' + eachApplicationEntity.application_srl +  '">' + eachApplicationEntity.application_number + '회차 ';

                            if(eachApplicationEntity.application_status == 0) {
                                view_str += ' 접수';
                            } else if(eachApplicationEntity.application_status == 1) {
                                view_str += ' 준비중';
                            } else if(eachApplicationEntity.application_status == 2) {
                                view_str += ' 진행중';
                            } else if(eachApplicationEntity.application_status == 3) {
                                view_str += ' 완료';
                            } else if(eachApplicationEntity.application_status == 4) {
                                view_str += ' 취소';
                            }

                            view_str += '</option>';
                        });
                        view_str += '</select>';

                        return view_str;
                    }
                }
                ]
        </#if>
        </@ap.py_jsdatatable>

    });

    $(document).on("click", '.payment-cancel', function() {
        var $this = $(this);

        //alert($(this).attr('data-payment-srl'));
        //alert($(this).attr('data-application-group'));
        var dataString = {
            "payment_srl"       : $(this).attr('data-payment-srl'),
            "application_group" : $(this).attr('data-application-group'),
            "payment_status"    : 4,
        };

        $.ajax({
            type: "POST",
            url: '${Request.contextPath}/user/payment/paymentCancel',
            contentType : 'application/json',
            data: JSON.stringify(dataString),
            dataType: 'json',
            success: function (data, status, xhr) {
                $('#pop-message').text("취소 요청이 정상적으로 이루어졌습니다.");
                popup_layer();

                timer = setInterval(function() {
                    my.fn.pmove('${Request.contextPath}/user/payment/cancel_list');
                }, 1500);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $('#pop-message').text("취소 요청이 실패했습니다. 다시 시도해 주세요.");
                popup_layer();
                return;
            }
        });
    });

    $(document).on("click", '.application-detail-view', function(e) {
        var $this = $(this);
        //var member_srl = $this.attr('data-member-srl');
        var application_group = $this.attr('data-application-group');
        var application_srl = $('#' + $this.attr('data-application-group') + '_application_srl').val();
        my.fn.pmove('${Request.contextPath}/user/myadvice/progress_detail/progress/'+ application_srl +'/'+ application_group);
        //my.fn.pmove('${Request.contextPath}/user/myadvice/progress_detail/progress/'+ application_group);
    });

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
