<#assign _page_id="list-gcm-apns-file">
<#assign _page_parent_name="이미지">
<#assign _page_current_name="GCM, APNs 이미지">

<@am.page_simple_depth icon="fa-table" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    Push 메시지에서 첨부로 사용한 이미지 리스트 입니다. Push 용 이미지 파일은 Push 메시지 발송 화면에서 업로드 가능 합니다.
</p>
<p>
    Push용 이미지 파일 뷰는 다음 파리미터를 지원 합니다.
<dl>
    <dt>1. r 파라미터를 이용한 비율로 이미지 리사이즈</dt>
    <dd>http://ckpush.com/resource/push/38?r=0.5 형태로 접속하면 0.5 비율로 리사이즈 합니다.</dd>
</dl>
</p>
<p>
<dl>
    <dt>2. x, y, w, h 파라미터를 이용한 이미지 자르기</dt>
    <dd>http://ckpush.com/resource/push/38?x=10&y=20&w=100&h=100 형태로 접속하면 원본 이미지의 좌측 상단을 기준으로 (10, 20) 위치에서 (100, 100) 크기로 이미지를 잘라서 보여 줍니다.</dd>
</dl>
</p>
<p>
<dl>
    <dt>3. w, h 파라미터를 이용한 이미지 리사이즈</dt>
    <dd>http://ckpush.com/resource/push/38?w=50&h=60 형태로 접속하면 이미지를 (50, 60) 으로 리사이즈 하여 보여 줍니다.</dd>
</dl>
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
                <@am.widget_title icon="fa-table">이미지 리스트</@am.widget_title>

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
                        <table id="list_apns_gcm_file" class="table table-striped table-bordered" width="100%">
                            <thead>
                            <tr>
                                <th data-class="expand" style="text-align: center; max-width: 45px;min-width: 45px;">SN.</th>
                                <th style="text-align: center; max-width: 150px;">원본 파일명</th>
                                <th data-hide="phone" style="text-align: center; max-width: 60px; min-width: 60px;">미리보기</th>
                                <th data-hide="phone" style="text-align: center;">URL</th>
                                <th data-hide="phone" style="text-align: center;">사용 앱</th>
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

        <@am.jsdatatable_simple listId="list_apns_gcm_file" url="${Request.contextPath}/admin/resource/gcm/apns/file/list/t/"
                searchTooltip="원본 파일명 첫글자 기준 검색" ; job>
            <#if job == "order">
                [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'file_srl', mData: 'file_srl', bSortable: true, bSearchable: false},
                    {sName: 'orig_name', mData: 'orig_name', bSortable: true, bSearchable: true,
                        <#-- TODO 추후 이미지 상세 정보 넣어야 함
                        mRender: function(data, type, full){
                            var navHash = $(location).attr('hash').substring(1),
                                    moveURL = '#${Request.contextPath}/admin/message/gcm/apns/app/' + full.app_srl;
                            return '<a class="datatable-row-detail-view" data-move="' +
                                    moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                        }
                        -->
                    },
                    {sName: 'preview', mData: null, sClass:'dt_column_text_center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            var pview = full.file_url + '?w=20&h=18';
                            return '<img src="'+pview+'" style="width:20px;height:18px;">';
                        }
                    },
                    {sName: 'file_url', mData: 'file_url', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            return '<a href="'+full.file_url+'" target="_blank">'+data+'</a>';
                        }
                    },
                    {sName: 'app_name', mData: 'app_name', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(data.length <= 0) return '없음';
                            else return data;
                        }
                    },
                    //{sName: 'push_count', mData: 'push_count', sClass:'dt_column_text_right', bSortable: false, bSearchable: false,
                    //    mRender: function(data, type, full){
                    //        if(data <= 0) return '0개';
                    //        else return data.numberFormat(0)+'개';
                    //    }
                    //}
                ]
            </#if>
        </@am.jsdatatable_simple>
    };

    loadScript("${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js", function () {
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