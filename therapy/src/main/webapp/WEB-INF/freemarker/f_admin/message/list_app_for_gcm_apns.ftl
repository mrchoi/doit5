<#assign _page_id="list-app-gcm-apns">
<#assign _page_parent_name="Push 메시지">
<#assign _page_current_name="즉시 발송">

<@am.page_simple_depth icon="fa-table" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    즉시 발송을 할 앱을 선택하는 화면 입니다. <strong>목록 좌측 상단</strong>의 검색값 입력을 사용하여 앱 이름으로 목록
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="앱 이름 첫글자 기준 검색"><strong>검색</strong></span> 할 수 있습니다.
    <strong>목록 우측 상단</strong>의 select 박스는 목록의 한 페이지에서 보여 주는
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="목록 우측 상단 select 박스"><strong>열의 갯수</strong></span> 입니다. 숫자를 변경하면 한 페이지에 보여지는 열의 개수를 변경 할 수 있습니다.
    항목을 선택 하면 메시지 발송 화면으로 이동 합니다.
</p>
<p>
    목록의 항목은 다음과 같습니다.<br>
    - <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="앱에 등록된 전체 단말 개수"><strong>전체 단말</strong></span> : 앱에 등록된 전체 단말 개수<br>
    - <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="GCM, APNs 키 등록된 단말 개수"><strong>Push 단말</strong></span> : GCM, APNs 키 등록된 단말 개수<br>
    - <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="GCM, APNs 발송한 횟수"><strong>Push 횟수</strong></span> : GCM, APNs 발송한 횟수
</p>
</@am.page_desc>

<#-- widget grid -->
<section id="widget-grid" class="">
<#-- row -->
    <div class="row">
        <#-- NEW WIDGET START -->
        <article class="col-sm-12 col-md-12 col-lg-12">
            <#-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false"
                     data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">앱 리스트</@am.widget_title>

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
                        <table id="list_app_gcm_apns" class="table table-striped table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th data-class="expand" style="text-align: center; max-width: 45px;min-width: 45px;">SN.</th>
                                    <th style="text-align: center;">앱 이름</th>
                                    <th data-hide="phone" style="text-align: center; max-width: 120px;">전체 단말</th>
                                    <th data-hide="phone" style="text-align: center; max-width: 120px;">Push 단말</th>
                                    <th data-hide="phone" style="text-align: center;max-width: 120px;">Push 횟수</th>
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

        <#-- 페이지 도움말 영역 visible, invisible 처리 -->
        my.fn.initPageHelper('${_page_id}-helper');

        <@am.jsdatatable_simple listId="list_app_gcm_apns" url="${Request.contextPath}/admin/message/gcm/apns/app/list/t/"
                searchTooltip="앱 이름 첫글자 기준 검색" ; job>
            <#if job == "order">
                [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'app_srl', mData: 'app_srl', bSortable: true, bSearchable: false},
                    {sName: 'app_name', mData: 'app_name', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full){
                            <#-- 클릭시 a link 로 detail 페이지로 이동 -->
                            var navHash = $(location).attr('hash').substring(1),
                                    moveURL = '#${Request.contextPath}/admin/message/gcm/apns/app/' + full.app_srl;
                            return '<a class="datatable-row-detail-view" data-move="' +
                                    moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                        }
                    },
                    {sName: 'reg_terminal', mData: 'reg_terminal', sClass:'dt_column_text_right', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(data <= 0) return '0개';
                            else return data.numberFormat(0)+'개';
                        }
                    },
                    {sName: 'reg_push_terminal', mData: 'reg_push_terminal', sClass:'dt_column_text_right', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(data <= 0) return '0개';
                            else return data.numberFormat(0)+'개';
                        }
                    },
                    {sName: 'push_count', mData: 'push_count', sClass:'dt_column_text_right', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(data <= 0) return '0개';
                            else return data.numberFormat(0)+'개';
                        }
                    }
                ]
            </#if>
        </@am.jsdatatable_simple>
    };

    loadScript("${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js", function () {
        loadScript("${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js", function () {
            loadScript("${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js", function () {
                loadScript("${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js", function () {
                    loadScript("${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js", pagefunction)
                });
            });
        });
    });
</script>