<#assign _page_id="myadvice-push-list">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담 상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />
<#-- widget grid -->
<section id="widget-grid" class="">
<#-- row -->
    <div class="row">
    <#-- NEW WIDGET START -->
        <article class="col-sm-12 col-md-12 col-lg-12">
        <#-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">인문 컨텐츠 목록</@am.widget_title>

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
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th rclass="text-center" style="min-width:50px;">회차</th>
                                <th rclass="text-center" style="min-width:50px;">인문컨텐츠</th>
                                <th rclass="text-center" style="min-width:50px;">발송일자</th>
                                <th rclass="text-center" style="min-width:50px;">발송시간</th>
                                <th rclass="text-center" style="min-width:50px;">진행상태</th>
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
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="btn-group pull-left">
                <button id="btn-progress-detail" class="btn btn-sm btn-info" type="button">
                    <i class="fa fa-chevron-left"></i> 이전
                </button>
            </div>
        </article>

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

        <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/plymind/myadvice/push_list/${application_group}/t/" ; job>
            <#if job == "buttons">
                    []
            <#elseif job == "order">
                    [[0, 'ASC']]
            <#elseif job == "columns">
                [
                    {sName: 'application_number', mData: 'application_number', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            return data + " 회차";
                        }
                    },
                    {sName: 'push_text', mData: 'push_text', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            <#-- 컨텐츠케어테라피 회차 중 push_day가 지정되지 않는 회차는 검사지를 업로드 한다. -->
                            if(full.push_day == null || full.push_day == '') {
                                var navHash = $(location).attr('hash').substring(1),
                                        moveURL = '#${Request.contextPath}/admin/plymind/myadvice/advisor_comment_add/${member_srl}/' + full.application_srl + '/' + full.application_group;
                                return '<a class="datatable-row-detail-view" data-move="' +
                                        moveURL + '" data-nav="' + navHash + '">' + '검사지 등록' + '</a>';
                            }

                            <#-- 인문컨텐츠가 등록되어 있지 않으면 등록을 하고 있으면 수정을 한다. -->
                            if(data == null || data == '') {
                                var navHash = $(location).attr('hash').substring(1),
                                        moveURL = '#${Request.contextPath}/admin/plymind/myadvice/push_add/${member_srl}/' + full.application_srl + '/' + full.application_group;
                                return '<a class="datatable-row-detail-view" data-move="' +
                                        moveURL + '" data-nav="' + navHash + '">' + '인문컨텐츠 등록' + '</a>';
                            } else {
                                var navHash = $(location).attr('hash').substring(1),
                                        moveURL = '#${Request.contextPath}/admin/plymind/myadvice/push_add/${member_srl}/' + full.application_srl + '/' + full.application_group;
                                return '<a class="datatable-row-detail-view" data-move="' +
                                        moveURL + '" data-nav="' + navHash + '">' + data + '</a>';
                            }
                        }
                    },
                    {sName: 'push_day', mData: 'push_day', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if( data== null || data == '') {
                                return '-';
                            } else {
                                return data.substring(0, 4) + "년 " + data.substring(4, 6) + "월 " + data.substring(6, 8) +"일";
                            }

                        }
                    },
                    {sName: 'push_time', mData: 'push_time', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if( data== null || data == '') {
                                return '-';
                            } else {
                                return data.substring(0, 2) + "시 " + data.substring(2) + "분";
                            }
                        }
                    },
                    {sName: 'application_status', mData: 'application_status', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if(data == '0') { return "접수"; }
                            if(data == '1') { return "준비중"; }
                            if(data == '2') { return "진행중"; }
                            if(data == '3') { return "완료"; }
                            if(data == '4') { return "취소"; }
                        }
                    }
                ]
            </#if>
        </@am.jsdatatable>

        <#-- 심리상담 상세정보 페이지로 이동 -->
        $('#btn-progress-detail').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/progress_detail/${member_srl}/${application_srl}/${application_group}');
        });
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