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
<#assign _page_id="user-appointment-list">
<#assign _page_parent_name="나의 예약/결제 정보">
<#assign _page_current_name="예약변경">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

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
                        <th class="text-center" style="min-width:50px;">번호</th>
                        <th rclass="text-center" style="min-width:50px;">예약날짜</th>
                        <th rclass="text-center" style="min-width:50px;">예약시간</th>
                        <th class="text-center" style="min-width:50px;">구분</th>
                        <th class="text-center" style="min-width:100px;">상담내역</th>
                        <th class="text-center" style="min-width:50px;">상담사</th>
                        <th class="text-center" style="min-width:50px;">진행상태</th>
                        <th data-hide="phone,tablet" style="min-width:60px;">예약변경</th>
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

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };

        $("#add-document").click(function() {
            my.fn.pmove('${Request.contextPath}/user/secret/add');
        });

        $(".datatable-row-detail-view").click(function() {
            my.fn.pmove('${Request.contextPath}/user/secret/1');
        });

        <@ap.py_jsdatatable listId="${_page_id}" searchTooltip="제목 첫글자 기준 검색" url="${Request.contextPath}/user/appointment/list/t/" ; job>
            <#if job == "order">
                [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'appointment_srl', mData: 'appointment_srl', sClass: 'text-center', bSortable: true, bSearchable: false},
                    {sName: 'appointment_date', mData: 'appointment_date', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            return data.substring(0,4) + "." + data.substring(4,6) + "." + data.substring(6,8);
                        }
                    },
                    {sName: 'appointment_time', mData: 'appointment_time', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            return data + "시 00분";
                        }
                    },
                    {sName: 'kind', mData: 'kind', sClass: 'text-center', bSortable: false, bSearchable: true,
                        mRender: function(data, type, full, dtObj) {
                            if(data == '1') {  return "심리</br>상담";   }
                            if(data == '2') {  return "심리</br>검사";   }
                        }
                    },
                    {sName: 'title', mData: 'title', sClass: 'text-center', bSortable: false, bSearchable: true,
                        mRender: function(data, type, full, dtObj) {
                            var viewStr = "";

                            viewStr += data + " ";

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
                    {sName: 'advisor_name', mData: 'advisor_name', sClass: 'text-center', bSortable: false, bSearchable: false},
                    {sName: 'appointment_status', mData: 'appointment_status', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if(data == '0') {
                                return "예약 접수";
                            } else if(data == '1') {
                                return "예약 완료";
                            } else {
                                return "예약 취소";
                            }
                        }
                    },
                    {sName: 'change_appointment', mData: '', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            var date = new Date();
                            var nowDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                            var appointmentDate = new Date(full.appointment_date.substring(0,4), (full.appointment_date.substring(4,6) - 1), full.appointment_date.substring(6,8));

                            if(full.appointment_status == '0' && nowDate.getTime() < appointmentDate.getTime()) {
                                var navHash = $(location).attr('hash').substring(1);
                                var moveURL = '${Request.contextPath}/user/appointment/changeForm/' + full.appointment_srl;
                                return '<a class="datatable-row-detail-view" data-move="' + moveURL + '" data-nav="' + navHash + '" style="cursor: pointer;">' + '예약변경' + '</a>';
                            } else {
                                return "-";
                            }
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
