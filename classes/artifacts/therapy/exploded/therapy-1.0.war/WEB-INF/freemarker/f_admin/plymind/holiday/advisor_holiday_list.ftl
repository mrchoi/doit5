<#assign _page_id="holiday-advisor-holiday-list">
<#assign _page_parent_name="플리마인드 관리">
<#assign _page_current_name="상담사 일정 관리">
<@am.page_simple_depth icon="fa-table" parent="${_page_parent_name}" current="${_page_current_name}" />
<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-list-app-0" data-widget-colorbutton="false" data-widget-editbutton="false"
                 data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">일정 목록</@am.widget_title>
                <div>
                    <div class="widget-body no-padding">
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
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="일정명 검색"
                                           rel="tooltip" data-placement="top" data-original-title="일정명 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-3" placeholder="별명 검색"
                                           rel="tooltip" data-placement="top" data-original-title="별명 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th data-hide="phone,tablet" style="text-align: center;max-width: 15px;min-width: 15px;"><i class="fa fa-check-circle-o"></i></th>
                                <th data-class="expand" style="text-align: center; max-width: 45px;min-width: 45px;">SN.</th>
                                <th style="text-align: center;">일정명</th>
                                <th data-hide="phone" style="text-align: center; max-width: 150px;">상담사</th>
                                <th data-hide="phone" style="text-align: center; max-width: 228px;">날짜</th>
                                <th data-hide="phone" style="text-align: center; max-width: 228px;">시간</th>
                                <th data-hide="phone" style="text-align: center; max-width: 228px;">등록일</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    <#if Request.loginAuthority == 'ROLE_ADVISOR' ><#--상담사 권한으로 보기-->
                        <div class="widget-footer smart-form">
                            <div class="btn-group">
                                <button id="add_holiday" class="btn btn-sm btn-default" type="button">
                                    <i class="fa"></i> 일정 등록
                                </button>
                            </div>
                        </div>
                    </#if>

                    </div>
                </div>
            </div>
        </article>
    </div>
</section>

<div id="ckpush-window-block" style="display:none;">
    <h6>삭제 중 입니다. 잠시만 대기 하세요.</h6>
</div>

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

        <#-- 게시물 추가를 선택하면 추가 폼을 보여 준다. -->
        $('#add_holiday').click(function(e) {
            var moveURL = '#${Request.contextPath}/admin/holiday/advisor_holiday_add';
            <@am.jschangecontent moveuri="moveURL" navuri="navHash" isVarMove=true isVarNav=true />
        });

    <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/holiday/advisor_holiday_list/t/" ; job>
        <#if job == "buttons">
                [{
                    sButtonText: '일정 삭제',
                    sExtends: 'text',
                    fnClick: function(nButton, oConfig, oFlash) {
                    <#-- 선택 된 member_srl을 구한다 -->
                        var reqData = {l_keys: []};
                        $('.check-row-data').each(function(){
                            var $this = $(this);
                            if($this.is(':checked')) reqData.l_keys.push($this.data('sn'));
                        });

                        if(reqData.l_keys.length <= 0) {
                            <@am.jsnoti title="일정 삭제 실패" content="선택된 일정이 없습니다" contenttype="text" />
                            return;
                        }
                        var successText = reqData.l_keys.length+'개의 일정을 삭제 했습니다.';
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

                            var $btn = $(nButton);
                            $btn.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');

                            <@am.jsajax_request submituri="${Request.contextPath}/admin/holiday/holiday_del/t/"
                            method="DELETE" errortitle="일정 삭제 실패" ; job >
                                <#if job == "success_job">
                                    <@am.jsnoti title="일정 삭제 완료" content="successText" boxtype="success" />
                                    _.each(reqData.l_keys, function(sn){
                                        otable.row($('#dt-row-'+sn)).remove();
                                    });
                                    otable.draw(false);
                                <#elseif job == "common_job">
                                    $.unblockUI();
                                    $btn.removeClass('disabled').html('<span>휴일 삭제</span>');
                                </#if>
                            </@am.jsajax_request>
                        };

                        my.fn.showUserConfirm('일정 삭제',
                                "선택된 일정을 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                                '삭제를 입력하세요',
                                '네', '삭제', process, '삭제 실패', '삭제를 입력하지 않았습니다.');
                    }
                }]
        <#elseif job == "order">
                [[1, 'desc']]
        <#elseif job == "columns">
            [
                {sName: 'checked', mData: null, sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        return '<label class="checkbox"><input type="checkbox" class="check-row-data" data-sn="'+full.holiday_srl+'"><i></i></label>';
                    }
                },
                {sName: 'holiday_srl', mData: 'holiday_srl', sClass: 'text-center', bSortable: true, bSearchable: false},
                {sName: 'holiday_title', mData: 'holiday_title', bSortable: true, bSearchable: true},
                {sName: 'nick_name', mData: 'nick_name', sClass: 'text-center', bSortable: true, bSearchable: true},
                {sName: 'holiday_date', mData: 'holiday_date', sClass: 'text-center', bSortable: true, bSearchable: false,
                    mRender: function(data, type, full){
                        return data.substring(0,4) + '.' + data.substring(4,6) + '.' + data.substring(6,8);
                    }
                },
                {sName: 'holiday_time', mData: 'holiday_time', sClass: 'text-center', bSortable: true, bSearchable: false,
                    mRender: function(data, type, full){
                        return data + '시';
                    }
                },
                {sName: 'c_date', mData: 'c_date', sClass: 'text-center', bSortable: true, bSearchable: false,
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