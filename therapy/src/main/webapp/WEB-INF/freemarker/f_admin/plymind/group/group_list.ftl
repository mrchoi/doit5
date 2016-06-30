<#--
    추가를 통해서 리스트 페이지로 올때는 리스트 페이지 테이블의 localStorage 를 초기화 해야 한다.
    아니면 이전 화면이 보이기 때문에 많이 이상함.
    localStorage 초기화 하면 리스트의 1번 페이지를 보여 준다.
-->

<#assign _page_id="group_list">
<#assign _page_parent_name="회원 단체 관리">
<#assign _page_current_name="회원 단체 목록">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    회원 가입 시 단체 선택 후 단체 검색에서 보여진다. <br>
    회원 단체는 한 번 등록 시 삭제 불가능하니 차단여부로 관리하여 주시기 바랍니다.
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
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="제목 검색"
                                           rel="tooltip" data-placement="top" data-original-title="단체 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th data-class="expand" style="text-align: center; max-width: 50px;min-width: 50px;">SN.</th>
                                <th data-class="expand" style="text-align: center;">단체명</th>
                                <th data-hide="phone,tablet" style="text-align: center; max-width:150px;min-width: 150px;" >등록일자</th>
                                <th data-hide="phone,tablet" style="text-align: center; max-width:50px;min-width: 50px;" >차단여부</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>

                    <#-- Action 버튼 -->
                        <div class="widget-footer smart-form">
                            <div class="btn-group">
                                <button id="add-document" class="btn btn-sm btn-default" type="button">
                                    <i class="fa"></i> 단체 추가
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

<#-- 삭제 대기를 위해 화면 전체 block -->
<div id="ckpush-window-block" style="display:none;">
    <h6>삭제 중 입니다. 잠시만 대기 하세요.</h6>
</div>


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

    <#-- 단체 추가를 선택하면 추가 폼을 보여 준다. -->
        $('#add-document').click(function(e) {
            var moveURL = '#${Request.contextPath}/admin/group/add';
        <@am.jschangecontent moveuri="moveURL" navuri="navHash" isVarMove=true isVarNav=true />
        });

    <@am.jsdatatable listId="${_page_id}" useCustomButton=false url="${Request.contextPath}/admin/group/list/t/" ; job>
        <#if job == "order">
                [[0, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'document_srl', mData: 'document_srl', sClass: 'dt_column_text_center', bSortable: true, bSearchable: false},

                {sName: 'document_title', mData: 'document_title', bSortable: true, bSearchable: true,
                    mRender: function(data, type, full, dtObj){
                    <#-- 클릭시 a link 로 detail 페이지로 이동 -->
                        var navHash = $(location).attr('hash').substring(1),
                                moveURL = '#${Request.contextPath}/admin/group/' + full.document_srl + '/';
                        return '<a class="datatable-row-detail-view" data-move="' +
                                moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                    }
                },
                {sName: 'c_date', mData: 'c_date', sClass: 'dt_column_text_center', bSortable: true, bSearchable: false,
                    mRender: function(data, type, full){
                        return new Date(my.fn.toNumber(data) * 1000).format('yyyy-MM-dd HH:mm:ss');
                    }
                },
                {sName: 'block', mData: 'block', sClass: 'dt_column_text_center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        if(data == ${mdv_yes})      return 'YES';
                        else if(data == ${mdv_no})  return 'NO';
                        return '-';
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