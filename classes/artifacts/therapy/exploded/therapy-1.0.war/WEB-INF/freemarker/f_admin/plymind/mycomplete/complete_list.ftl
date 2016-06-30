<#assign _page_id="mycomplete-complete-list">
<#assign _page_parent_name="완료 내역 관리">
<#assign _page_current_name="완료 목록">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />
<#-- widget grid -->
<section id="widget-grid" class="">
<#-- row -->
    <div class="row">
    <#-- NEW WIDGET START -->
        <article class="col-sm-12 col-md-12 col-lg-12">
        <#-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-list-app-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">${_page_current_name}</@am.widget_title>

            <#-- widget div-->
                <div>
                <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                <#-- end widget edit box -->

                <#-- widget content -->
                <#-- datable 의 header toolbar 를 사용하지 않고 위젯 타이틀 밑에 딱 붙이기 위해서 -19px margin-top 을 주었음 -->
                <#--<div class="widget-body no-padding" style="margin-top: -19px;">-->
                <#-- button 을 넣을 자리가 없어서 다시 top 영역을 사용 하기로 함. 위의 주석은 추후 버튼 넣을거 없다면 참고용으로 그대로 둔다. -->
                    <div class="widget-body no-padding">
                    <#-- datatable 에서 검색을 지원하는 column 의 th 는 dt-column, dt-column-<column number> class 를 포함해야 한다.(localStorage를 이용한 초기화에 이용됨) -->
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
                                    <input type="text" class="form-control input-xs dt-column dt-column-4" placeholder="이름 검색"
                                           rel="tooltip" data-placement="top" data-original-title="이름 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-5" placeholder="상담사 검색"
                                           rel="tooltip" data-placement="top" data-original-title="상담사 기준 검색" />
                                </th>
                                <#--<th class="hasinput"></th>-->
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th rclass="text-center" style="min-width:50px;max-width:50px;">번호</th>
                                <th rclass="text-center" style="min-width:50px;max-width:50px;">구분</th>
                                <th rclass="text-center" style="min-width:150px;">신청내역</th>
                                <th rclass="text-center" style="min-width:50px;">회차</th>
                                <th rclass="text-center" style="min-width:50px;max-width:50px;">신청자</th>
                                <th rclass="text-center" style="min-width:50px;max-width:50px;">상담사</th>
                                <#--<th rclass="text-center" style="min-width:50px;max-width:50px;">진행상태</th>-->
                                <th rclass="text-center" style="min-width:100px;max-width:100px;">완료보고서</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                <#-- end widget content -->
                </div>
            <#-- end widget div -->
            </div>
        <#-- end widget -->
        </article>
    <#-- WIDGET END -->
    </div>
<#-- end row -->
</section>
<#-- end widget grid -->

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };

        <@am.jsdatatable listId="${_page_id}" useCustomButton=false url="${Request.contextPath}/admin/mycomplete/list/t/{tid}" ; job>
            <#if job == "order">
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

                            var navHash = $(location).attr('hash').substring(1),
                                    //moveURL = '#${Request.contextPath}/admin/mycheckup/' + full.application_group;
                                    moveURL = '#${Request.contextPath}/admin/mycomplete/complete_detail/' + full.member_srl +'/'+ full.application_srl +'/'+ full.application_group;

                            //return '<a class="datatable-row-detail-view" data-move="' + moveURL + '" data-nav="' + navHash + '">' + viewStr + '</a>';
                            return '<a class="application-detail-view" data-member-srl="' + full.member_srl + '"data-application-group="' + full.application_group + '" style="cursor: pointer;">' + viewStr + '</a>';
                        }
                    },
                    {sName: 'eachApplicationEntities', mData: 'eachApplicationEntities', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            var view_str = "";
                            var eachApplicationEntities = data;

                            if(full.kind == '1') {

                                view_str += '<select id="' + full.application_group + '_application_srl" style="width:100px;">';
                                view_str += '<option value="0">전체</option>';
                                $.each(eachApplicationEntities, function (index, eachApplicationEntity) {
                                    view_str += '<option value="' + eachApplicationEntity.application_srl + '">' + eachApplicationEntity.application_number + '회차 ';

                                    if (eachApplicationEntity.application_status == 0) {
                                        view_str += ' 접수';
                                    } else if (eachApplicationEntity.application_status == 1) {
                                        view_str += ' 준비중';
                                    } else if (eachApplicationEntity.application_status == 2) {
                                        view_str += ' 진행중';
                                    } else if (eachApplicationEntity.application_status == 3) {
                                        view_str += ' 완료';
                                    } else if (eachApplicationEntity.application_status == 4) {
                                        view_str += ' 취소';
                                    }

                                    view_str += '</option>';
                                });
                                view_str += '</select>';
                            }else{
                                view_str += '<input type="hidden" id="' + full.application_group + '_application_srl" value="'+full.application_srl+'">';
                            }
                            return view_str;
                        }
                    },
                    {sName: 'user_name', mData: 'user_name', sClass: 'text-center', bSortable: true, bSearchable: true},
                    {sName: 'advisor_name', mData: 'advisor_name', sClass: 'text-center', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full, dtObj) {
                            if (data == '') {
                                return '-';
                            } else {
                                return data;
                            }
                        }
                    },
                    /*{sName: 'application_status', mData: 'application_status', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if(data == 0) return '접수';
                            else if(data == 1) return '준비중';
                            else if(data == 2) return '진행중';
                            else if(data == 3) return '완료';
                            else if(data == 4) return '취소';
                            else return '-';
                        }
                    },*/
                    {sName: 'result_file_url', mData: 'result_file_url', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            var navHash = $(location).attr('hash').substring(1);
                           /* var moveURL = full.result_file_url;
                            if(moveURL == 'size') {
                                return '--';
                            } else {
                                return '<a class="datatable-row-detail-view" data-move="' + moveURL + '" data-nav="' + navHash + '" style="cursor: pointer;">' + '검사결과보고서' + '</a>';
                            }*/
                            var moveURL =  '#${Request.contextPath}/admin/mycomplete/complete_file_add/' + full.kind +'/' + full.member_srl +'/'+ full.application_srl +'/'+ full.application_group;
                            //return '<a class="datatable-row-detail-view" data-move="' + moveURL + '" data-nav="' + navHash + '" style="cursor: pointer;">' + '검사결과보고서' + '</a>';
                            return '<button type="button" class="btn btn-xs btn-default datatable-row-detail-view" data-move="' + moveURL + '" data-nav="' + navHash + '" >' + '검사결과보고서' + '</button>';

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
        var application_srl = $('#' + application_group + '_application_srl').val();

        my.fn.pmove('#${Request.contextPath}/admin/mycomplete/complete_detail/' + member_srl +'/'+ application_srl +'/'+ application_group);
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