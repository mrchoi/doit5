<#assign _page_id="appointment-appointment-application-list">
<#assign _page_parent_name="예약관리">
<#assign _page_current_name="예약등록">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />
<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">예약 등록</@am.widget_title>
                <div>
                    <div class="widget-body no-padding">
                        <table id="${_page_id}" class="table table-striped table-bordered" width="100%">
                            <thead>
                            <tr>
                                <th rclass="text-center" style="min-width:50px;">번호</th>
                                <th rclass="text-center" style="min-width:50px;">구분</th>
                                <th rclass="text-center" style="min-width:50px;">신청내역</th>
                                <th rclass="text-center" style="min-width:50px;">회차</th>
                                <th rclass="text-center" style="min-width:50px;">상담사</th>
                                <th rclass="text-center" style="min-width:50px;">신청자</th>
                                <th rclass="text-center" style="min-width:50px;">진행상태</th>
                                <th rclass="text-center" style="min-width:50px;">예약</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                </div>
            </div>
        </article>

        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="btn-group pull-left">
                <button id="btn_appointment_group_list" class="btn btn-sm btn-info" type="button">
                    <i class="fa fa-chevron-left"></i> 상담 목록
                </button>
            </div>
        </article>
    </div>
</section>

<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        my.fn.showSessionTime(${sessionRestSec});

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };

        <#-- 결제내역 리스트로 이동 -->
        $('#btn_appointment_group_list').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/appointment/appointment_group_list');
        });

        <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/plymind/appointment/application_list/${application_group}/t/" ; job>
            <#if job == "buttons">
                    []
            <#elseif job == "order">
                    [[0, 'ASC']]
            <#elseif job == "columns">
                [
                    {sName: 'application_srl', mData: 'application_srl', sClass: 'text-center', bSortable: true, bSearchable: false},
                    {sName: 'kind', mData: 'kind', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if(data == '1') {
                                return '심리 상담';
                            } else {
                                return '심리 검사';
                            }
                        }
                    },
                    {sName: 'title', mData: 'title', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            var viewStr = '';

                            viewStr += data + ' ';

                            if(full.kind == '1') {
                                if (full.advice_type == '1') {
                                    viewStr += ' - 으뜸 ' + full.advice_number + '회 ';
                                }
                                if (full.advice_type == '2') {
                                    viewStr += ' - 가장 ' + full.advice_number + '회 ';
                                }
                                if (full.advice_type == '3') {
                                    viewStr += ' - 한마루 ' + full.advice_number + '회 ';
                                }

                                if (full.advice_period == '1') {
                                    viewStr += ' - 1주';
                                }
                                if (full.advice_period == '4') {
                                    viewStr += ' - 4주 ( 1개월 )';
                                }
                                if (full.advice_period == '12') {
                                    viewStr += ' - 12주 ( 3개월 )';
                                }
                                if (full.advice_period == '24') {
                                    viewStr += ' - 24주 ( 6개월 )';
                                }
                                if (full.advice_period == '52') {
                                    viewStr += ' - 52주 ( 12개월 )';
                                }
                            }

                            return viewStr;
                        }
                    },
                    {sName: 'application_number', mData: 'application_number', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            return data + '회차';
                        }
                    },
                    {sName: 'advisor_name', mData: 'advisor_name', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if (data == '') {
                                return '-';
                            } else {
                                return data;
                            }
                        }
                    },
                    {sName: 'user_name', mData: 'user_name', sClass: 'text-center', bSortable: false, bSearchable: true},
                    {sName: 'application_status', mData: 'application_status', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if (data == '0') { return '접수'; }
                            if (data == '1') { return '준비중'; }
                            if (data == '2') { return '진행중'; }
                            if (data == '3') { return '상담완료'; }
                            if (data == '4') { return '취소'; }
                        }
                    },
                    {sName: 'appointment_date', mData: 'appointment_date', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if (data == '-') {
                                return data;
                            } else if(data == 'NONE') {
                                var navHash = $(location).attr('hash').substring(1),
                                        moveURL = '#${Request.contextPath}/admin/plymind/appointment/appointment_add/' + full.application_srl;
                                return '<a class="datatable-row-detail-view" data-move="' +
                                        moveURL + '" data-nav="' + navHash + '">' + '예약' + '</a>';
                            } else {
                                return data.substring(0,4) + '.' + data.substring(4,6) + '.' + data.substring(6,8) + ' ' + full.appointment_time + ':00';
                            }
                        }
                    }
                ]
            </#if>
        </@am.jsdatatable>
    };

    loadScript("${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js", function() {
        loadScript("${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js", function () {
            loadScript("${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js", function () {
                loadScript("${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js", function () {
                    loadScript("${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js", function () {
                        loadScript("${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js", pagefunction)
                    });
                });
            });
        });
    });
</script>