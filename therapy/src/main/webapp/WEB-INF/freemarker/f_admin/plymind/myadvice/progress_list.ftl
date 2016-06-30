<#assign _page_id="myadvice-progress-list">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담목록">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />
<#-- widget grid -->
<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">심리상담 목록</@am.widget_title>
                <div>
                    <div class="widget-body no-padding">
                        <table id="${_page_id}" class="table table-striped table-bordered" width="100%">
                            <thead>
                            <tr>
                                <th class="hasinput smart-form"></th>
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="상담명 검색"
                                           rel="tooltip" data-placement="top" data-original-title="상담명 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-3" placeholder="이름 검색"
                                           rel="tooltip" data-placement="top" data-original-title="이름 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-4" placeholder="상담사 검색"
                                           rel="tooltip" data-placement="top" data-original-title="상담사 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th rclass="text-center" style="min-width:50px;">번호</th>
                                <th rclass="text-center" style="min-width:50px;">구분</th>
                                <th rclass="text-center" style="min-width:100px;">신청내역</th>
                                <th rclass="text-center" style="max-width:50px;min-width:50px;">회차</th>
                                <th rclass="text-center" style="min-width:50px;">신청자</th>
                                <th rclass="text-center" style="min-width:50px;">상담사</th>
                                <th rclass="text-center" style="min-width:50px;">신청일</th>
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

<#-- SCRIPTS ON PAGE EVENT -->
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

        <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/plymind/myadvice/progress_list/t/{tid}" ; job>
            <#if job == "buttons">
                    []
            <#elseif job == "order">
                    [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'application_srl', mData: 'application_srl', sClass: 'text-center', bSortable: true, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            return data;
                        }
                    },
                    {sName: 'kind', mData: 'kind', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if(data == '1') {
                                return '심리 상담';
                            } else {
                                return '심리 검사';
                            }
                        }
                    },
                    {sName: 'title', mData: 'title', sClass: 'text-center', bSortable: true, bSearchable: true,
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

                            return '<a class="application-detail-view" data-member-srl="' + full.member_srl + '"data-application-group="' + full.application_group + '" style="cursor: pointer;">' + viewStr + '</a>';
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
                    },
                    {sName: 'user_name', mData: 'user_name', sClass: 'text-center', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full, dtObj) {
                            var application_member = "";
                            for(var i=0; i<full.application_member.length; i++) {
                                application_member += full.application_member[i].user_name + " ";
                            }

                            return application_member;
                        }
                    },
                    {sName: 'advisor_name', mData: 'advisor_name', sClass: 'text-center', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full, dtObj) {
                            if (data == '') {
                                return '-';
                            } else {
                                return data;
                            }
                        }
                    },
                    {sName: 'c_date', mData: 'c_date', sClass: 'text-center', bSortable: true, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            return new Date(my.fn.toNumber(data) * 1000).format('yyyy.MM.dd HH:mm');
                        }
                    }
                ]
            </#if>
        </@am.jsdatatable>
    };

    $(document).on("click", '.application-detail-view', function(e) {
        var $this = $(this);

        var member_srl = $this.attr('data-member-srl');

        var application_group = $this.attr('data-application-group');

        var application_srl = $('#' + $this.attr('data-application-group') + '_application_srl').val();

        my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/progress_detail/' + member_srl +'/'+ application_srl +'/'+ application_group);
    });

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