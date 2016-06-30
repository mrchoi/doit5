<#--
    추가를 통해서 리스트 페이지로 올때는 리스트 페이지 테이블의 localStorage 를 초기화 해야 한다.
    아니면 이전 화면이 보이기 때문에 많이 이상함.
    localStorage 초기화 하면 리스트의 1번 페이지를 보여 준다.
-->

<#assign _page_id="doc-file-list">
<#assign _page_parent_name="검사 문서 관리">
<#assign _page_current_name="파일 목록">

<@am.page_simple_depth icon="fa-table" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">

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
                        <table id="list_apns_gcm_file" class="table table-striped table-bordered" width="100%">

                            <thead>
                            <tr>
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <select class="form-control input-xs dt-column dt-column-1" style="margin-left: -0px;">
                                        <option value="${mdv_none}">전체</option>
                                        <option value="1">심리검사지</option>
                                        <option value="4">사전검사지</option>
                                        <option value="5">사후검사지</option>
                                    </select>
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="파일명 검색"
                                           rel="tooltip" data-placement="top" data-original-title="파일명 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th data-class="expand" style="text-align: center; max-width: 45px;min-width: 45px;">SN.</th>
                                <th style="text-align: center; max-width: 70px;">구분</th>
                                <th style="text-align: center; max-width: 200px;">원본 파일명</th>
                                <th data-hide="phone" style="text-align: center; max-width: 80px; min-width: 80px;">파일 사이즈</th>
                                <th data-hide="phone" style="text-align: center;">URL</th>
                                <th data-hide="phone" style="text-align: center;">파일 설명</th>
                            </tr>
                            </thead>


                            <thead>
                                <tr>

                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>

                        <#-- Action 버튼 -->
                            <div class="widget-footer smart-form">
                                <div class="btn-group">
                                    <button id="add-file" class="btn btn-sm btn-default" type="button">
                                        <i class="fa"></i> 파일 추가
                                    </button>
                                </div>
                            </div>
                        <#-- Action 버튼 끝 -->

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

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };

    <#-- 게시물 추가를 선택하면 추가 폼을 보여 준다. -->
        $('#add-file').click(function(e) {
            var moveURL = '#${Request.contextPath}/admin/docFile/add/file';
        <@am.jschangecontent moveuri="moveURL" navuri="navHash" isVarMove=true isVarNav=true />
        });

        <@am.jsdatatable listId="list_apns_gcm_file" useCustomButton=false useCheckBox=false url="${Request.contextPath}/admin/resource/repository/file/list/t/" ; job>
            <#if job == "order">
                    [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'file_srl', mData: 'file_srl', bSortable: true, bSearchable: false},
                    {sName: 'file_type', mData: 'file_type', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full) {
                            if(data == 1) return '심리검사지';
                            else if(data == 2) return '답변지';
                            else if(data == 3) return '결과지';
                            else if(data == 4) return '사전검사지';
                            else if(data == 5) return '사후검사지';
                            else return '기타';
                        }
                    },
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
                    },
                    {sName: 'file_comment', mData: 'file_comment', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full) {
                            return data;
                        }
                    }

                ]
            </#if>
        </@am.jsdatatable>

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
