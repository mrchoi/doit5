<#assign _page_id="sentence_list">
<#assign _page_parent_name="너나들이 관리">
<#assign _page_current_name="너나들이 목록">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    너나들이<br>

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
                                <th class="hasinput smart-form">
                                    <label class="checkbox input-md">
                                        <input type="checkbox"><i></i>
                                    </label>
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="너나들이 검색"
                                           rel="tooltip" data-placement="top" data-original-title="첫 글자 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th data-hide="phone,tablet" style="text-align: center;max-width: 15px;min-width: 15px;"><i class="fa fa-check-circle-o"></i></th>
                                <th data-class="expand" style="text-align: center; max-width: 50px;min-width: 50px;">SN.</th>
                                <th data-class="expand" style="text-align: center;max-width:400px;min-width: 400px;">너나들이</th>
                                <th data-hide="phone,tablet" style="text-align: center; max-width:150px;min-width: 150px;" >전송 시간</th>
                                <th data-hide="phone,tablet" style="text-align: center; max-width:150px;min-width: 150px;" >등록일자</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>

                    <#-- Action 버튼 -->
                        <div class="widget-footer smart-form">
                            <div class="btn-group">
                                <button id="add-document" class="btn btn-sm btn-default" type="button">
                                    <i class="fa"></i> 너나들이 추가
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

    <#-- 게시물 추가를 선택하면 추가 폼을 보여 준다. -->
        $('#add-document').click(function(e) {
            var moveURL = '#${Request.contextPath}/admin/sentence/add';
        <@am.jschangecontent moveuri="moveURL" navuri="navHash" isVarMove=true isVarNav=true />
        });

    <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/sentence/list/t/" ; job>
        <#if job == "buttons">
                [{
                    sButtonText: '너나들이 삭제',
                    sExtends: 'text',
                    fnClick: function(nButton, oConfig, oFlash) {
                    <#-- 선택 된 member_srl을 구한다 -->
                        var reqData = {l_keys: []};
                        $('.check-row-data').each(function(){
                            var $this = $(this);
                            if($this.is(':checked')) reqData.l_keys.push($this.data('sn'));
                        });

                        if(reqData.l_keys.length <= 0) {
                            <@am.jsnoti title="너나들이 삭제 실패" content="선택된 너나들이 없습니다" contenttype="text" />
                            return;
                        }

                        var successText = reqData.l_keys.length+'개의 너나들이를 삭제 했습니다.';
                        var process = function() {
                        <#-- 화면 전체 block 을 띄운다 -->
                            $.blockUI({
                                message: $('#ckpush-window-block'),
                                css: {
                                    'background-color': '#F5F5F5',
                                    'border': '1px solid #CCCCCC',
                                    'border-radius': '1px',
                                    'border-collapse': 'collapse',
                                }
                            });

                        <#-- 선택 된 디바이스 삭제 요청 -->
                            var $btn = $(nButton);
                            $btn.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');

                            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/t/"
                            method="DELETE" errortitle="너나들이 삭제 실패" ; job >
                                <#if job == "success_job">
                                <#-- 삭제 성공시 테이블을 다시 로딩. remove 하면 내부 데이터만 삭제되지 화면에 반영 되지 않는다.
                                     draw 를 호출 해야 반영 된다. -->
                                    <@am.jsnoti title="너나들이 삭제 완료" content="successText" boxtype="success" />
                                <#-- ajax 로 로딩 하기 때문에 row 삭제가 의미 없다. 의미 없어도 메뉴얼에서 하라는데로 해 두었음. -->
                                    _.each(reqData.l_keys, function(sn){
                                        otable.row($('#dt-row-'+sn)).remove();
                                    });
                                    otable.draw(false);
                                <#elseif job == "common_job">
                                    $.unblockUI();
                                    $btn.removeClass('disabled').html('<span>너나들이 삭제</span>');
                                </#if>
                            </@am.jsajax_request>
                        };

                        my.fn.showUserConfirm('너나들이 삭제',
                                "선택된 너나들이를 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                                '삭제를 입력하세요',
                                '네', '삭제', process, '너나들이 삭제 실패', '삭제를 입력하지 않았습니다.');
                    }
                }]
        <#elseif job == "order">
                [[1, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'checked', mData: null, sClass: 'dt_column_text_center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        if(_.has(full, 'orig_document_srl'))
                            return '<label class="checkbox"><input type="checkbox" class="check-row-data check-document-row-data" data-sn="' +
                                    full.document_srl + '" data-link-flag="${mdv_yes}"><i></i></label>';
                        else
                            return '<label class="checkbox"><input type="checkbox" class="check-row-data check-document-row-data" data-sn="' +
                                    full.document_srl + '" data-link-flag="${mdv_no}"><i></i></label>';
                    }
                },
                {sName: 'document_srl', mData: 'document_srl', sClass: 'dt_column_text_center', bSortable: true, bSearchable: false},
                {sName: 'document_content', mData: 'document_content', sClass: 'dt_column_text_center', bSortable: true, bSearchable: true,
                    mRender: function(data, type, full, dtObj){
                    <#-- 클릭시 a link 로 detail 페이지로 이동 -->
                        var navHash = $(location).attr('hash').substring(1),
                                moveURL = '#${Request.contextPath}/admin/sentence/' + full.document_srl + '/';
                        return '<a class="datatable-row-detail-view" data-move="' +
                                moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                    }
                },



                {sName: 'template_extra_send', mData: 'template_extra.send', sClass: 'dt_column_text_center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        return new Date(my.fn.toNumber(data)).format('yyyy-MM-dd HH:mm:ss');
                    }
                },
                {sName: 'c_date', mData: 'c_date', sClass: 'dt_column_text_center', bSortable: true, bSearchable: false,
                    mRender: function(data, type, full){
                        return new Date(my.fn.toNumber(data) * 1000).format('yyyy-MM-dd HH:mm:ss');
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