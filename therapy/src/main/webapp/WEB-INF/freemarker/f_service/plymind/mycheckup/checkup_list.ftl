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
<#assign _page_id="user-checkup-list">
<#assign _page_parent_name="나의 상담 정보">
<#assign _page_current_name="심리 검사">
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
                        <th rclass="text-center" style="min-width:50px;">번호</th>
                        <th rclass="text-center" style="min-width:50px;">심리검사항목</th>
                        <th rclass="text-center" style="min-width:50px;">상태</th>
                        <th class="text-center" style="min-width:100px;">상세내역보기</th>
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

   <@ap.py_jsdatatable listId="${_page_id}" url="${Request.contextPath}/user/mycheckup/list/t/" ; job>
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

                            if (full.advice_period == '1') {
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

                        return viewStr;
                    }
                },
                {sName: 'application_status', mData: 'application_status', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full, dtObj) {
                        if(data == 0) return '접수';
                        else if(data == 1) return '준비중';
                        else if(data == 2) return '진행중';
                        else if(data == 3) return '완료';
                        else if(data == 4) return '취소';
                        else return '-';

                    }
                },
                {sName: 'application_group', mData: 'application_group', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full, dtObj) {
                        var navHash = $(location).attr('hash').substring(1);
                        var moveURL = '${Request.contextPath}/user/mycheckup/' + data;
                        //var moveURL = '${Request.contextPath}/user/myadvice/progress_detail/progress/' + data;
                        return '<a class="datatable-row-detail-view" data-move="' + moveURL + '" data-nav="' + navHash + '" style="cursor: pointer;">' + '상세보기' + '</a>';
                    }
                }
                ]
        </#if>
        </@ap.py_jsdatatable>
    });

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
