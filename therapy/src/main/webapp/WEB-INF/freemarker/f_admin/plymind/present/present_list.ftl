<#assign _page_id="list-present">
<#assign _page_parent_name="플리마인드 관리">
<#assign _page_current_name="상담현황 관리">
<@am.page_simple_depth icon="fa-table" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-list-app-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">상담현황</@am.widget_title>
                <div>
                    <div class="widget-body no-padding">
                        <table id="${_page_id}" class="table table-striped table-bordered" width="100%">
                            <thead>
                            <tr>
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-1" placeholder="별명 검색"
                                           rel="tooltip" data-placement="top" data-original-title="별명 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="이름 검색"
                                           rel="tooltip" data-placement="top" data-original-title="이름 검색" />
                                </th>
                                <th class="hasinput">
                                    <select class="form-control input-xs dt-column dt-column-3" style="margin-left: -0px;">
                                        <option value="0">전체</option>
                                        <option value="1">으뜸</option>
                                        <option value="2">가장</option>
                                        <option value="3">한마루</option>
                                    </select>
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th data-class="expand" style="text-align: center; min-width: 50px;">SN.</th>
                                <th data-hide="phone" style="text-align: center; min-width: 50px;">별명</th>
                                <th data-hide="phone" style="text-align: center; min-width: 50px;">이름</th>
                                <th data-hide="phone" style="text-align: center; min-width: 50px;">등급</th>
                                <th class="hasinput" style="text-align: center; min-width: 50px;">현재 진행 중인 상담</th>
                                <th class="hasinput" style="text-align: center; min-width: 50px;">심리검사</th>
                                <th class="hasinput" style="text-align: center; min-width: 50px;">결과보고서</th>
                                <th class="hasinput" style="text-align: center; min-width: 50px;">1:1 답변</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                </div>
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

        <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/present/present_list/t/" ; job>
            <#if job == "buttons">
                []
            <#elseif job == "order">
                [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'member_srl', mData: 'member_srl', sClass: 'dt_column_text_center', bSortable: true, bSearchable: false},
                    {sName: 'nick_name', mData: 'nick_name', sClass: 'dt_column_text_center', bSortable: true, bSearchable: true},
                    {sName: 'user_name', mData: 'user_name', sClass: 'dt_column_text_center', bSortable: true, bSearchable: true},
                    {sName: 'class_srl', mData: 'class_srl', sClass: 'dt_column_text_center', bSortable: false, bSearchable: true,
                        mRender: function(data, type, full) {
                            if (data == 1) {
                                return '으뜸';
                            } else if (data == 2) {
                                return '가장';
                            } else if (data == 3) {
                                return '한마루';
                            } else {
                                return '-';
                            }
                            return data;
                        }
                    },
                    {sName: 'presentMap', mData: 'presentMap', sClass: 'dt_column_text_center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            return data.advice_cnt;
                        }
                    },
                    {sName: 'presentMap', mData: 'presentMap', sClass: 'dt_column_text_center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            return data.checkup_cnt;
                        }
                    },
                    {sName: 'presentMap', mData: 'presentMap', sClass: 'dt_column_text_center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            return data.complete_cnt;
                        }
                    },
                    {sName: 'presentMap', mData: 'presentMap', sClass: 'dt_column_text_center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            return data.one_cnt;
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