<#--
    추가를 통해서 리스트 페이지로 올때는 리스트 페이지 테이블의 localStorage 를 초기화 해야 한다.
    아니면 이전 화면이 보이기 때문에 많이 이상함.
    localStorage 초기화 하면 리스트의 1번 페이지를 보여 준다.
-->

<#assign _page_id="list-repository-file">
<#assign _page_parent_name="파일">
<#assign _page_current_name="임의 파일 목록">

<@am.page_simple_depth icon="fa-table" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    등록된 임의 파일 목록을 보여 줍니다. 임의 파일이 이미지 파일 일때는 view 를 지원하고, 이미지 파일이 아닌 경우는 다운로드를 지원 합니다.
</p>
<p>
    이미지 파일 뷰는 다음 파리미터를 지원 합니다.
    <dl>
        <dt>1. r 파라미터를 이용한 비율로 이미지 리사이즈</dt>
        <dd>http://ckpush.com/resource/repo/38?r=0.5 형태로 접속하면 0.5 비율로 리사이즈 합니다.</dd>
    </dl>
</p>
<p>
    <dl>
        <dt>2. x, y, w, h 파라미터를 이용한 이미지 자르기</dt>
        <dd>http://ckpush.com/resource/repo/38?x=10&y=20&w=100&h=100 형태로 접속하면 원본 이미지의 좌측 상단을 기준으로 (10, 20) 위치에서 (100, 100) 크기로 이미지를 잘라서 보여 줍니다.</dd>
    </dl>
</p>
<p>
    <dl>
        <dt>3. w, h 파라미터를 이용한 이미지 리사이즈</dt>
        <dd>http://ckpush.com/resource/repo/38?w=50&h=60 형태로 접속하면 이미지를 (50, 60) 으로 리사이즈 하여 보여 줍니다.</dd>
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
            <@am.widget_title icon="fa-table">임의 파일 목록</@am.widget_title>

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
                                    <th style="text-align: center; max-width: 200px;">원본 파일명</th>
                                    <th data-hide="phone" style="text-align: center; max-width: 80px; min-width: 80px;">파일 사이즈</th>
                                    <th data-hide="phone" style="text-align: center;">URL</th>
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

        <@am.jsdatatable_simple listId="list_apns_gcm_file" url="${Request.contextPath}/user/resource/repository/file/list/t/"
                searchTooltip="원본 파일명 첫글자 기준 검색" ; job>
            <#if job == "order">
                    [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'file_srl', mData: 'file_srl', bSortable: true, bSearchable: false},
                    {sName: 'orig_name', mData: 'orig_name', bSortable: true, bSearchable: true},
                    {sName: 'file_size', mData: 'file_size', sClass:'dt_column_text_right', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            var mega = data / 1000000;
                            if(Math.floor(mega) > 0) return mega.numberFormat(1, '.', ',') + ' M';

                            var kbyte = data / 1000;
                            if(Math.floor(kbyte) > 0) return kbyte.numberFormat(1, '.', ',') + ' K';

                            return data;
                        }
                    },
                    {sName: 'file_url', mData: 'file_url', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            return '<a href="'+full.file_url+'" target="_blank">'+data+'</a>';
                        }
                    }
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
