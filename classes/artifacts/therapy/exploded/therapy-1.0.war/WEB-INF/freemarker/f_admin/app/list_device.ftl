<#--
    추가를 통해서 리스트 페이지로 올때는 리스트 페이지 테이블의 localStorage 를 초기화 해야 한다.
    아니면 이전 화면이 보이기 때문에 많이 이상함.
    localStorage 초기화 하면 리스트의 1번 페이지를 보여 준다.
-->

<#assign _page_id="list-all-device">
<#assign _page_parent_name="단말기">
<#assign _page_current_name="단말기 목록">

<@am.page_simple_depth icon="fa-table" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    앱에 등록된 단말기 목록을 보여 줍니다.
</p>
<p>
    삭제를 위해서는 열의 제일 앞에 있는 선택을 표시하고 <strong>단말기 삭제</strong> 버튼을 누르면 선택된 단말기가
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="단말기 삭제 버튼"><strong>삭제</strong></span> 됩니다.
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
            <@am.widget_title icon="fa-table">단말기 목록</@am.widget_title>

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
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="단말기 아이디 검색"
                                           rel="tooltip" data-placement="top" data-original-title="첫글자 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-3" placeholder="벤더, 모델명 검색"
                                           rel="tooltip" data-placement="top" data-original-title="첫글자 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-4" placeholder="버전 검색"
                                           rel="tooltip" data-placement="top" data-original-title="첫글자 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-5" placeholder="전화번호 검색"
                                           rel="tooltip" data-placement="top" data-original-title="첫글자 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-6" placeholder="앱 이름 검색"
                                           rel="tooltip" data-placement="top" data-original-title="첫글자 기준 검색" />
                                </th>
                                <th class="hasinput" style="text-align: center;">
                                    <select class="form-control input-xs dt-column dt-column-7" style="margin-left: -0px;">
                                        <option value="${mdv_nuse}">선택</option>
                                        <option value="1">안드로이드</option>
                                        <option value="2">iOS</option>
                                    </select>
                                </th>
                            </tr>
                            <tr>
                                <th data-hide="phone,tablet" style="text-align: center;max-width: 15px;min-width: 15px;"><i class="fa fa-check-circle-o"></i></th>
                                <th data-class="expand" style="text-align: center; max-width: 45px;min-width: 45px;">SN.</th>
                                <th style="text-align: center; max-width: 280px; min-width: 240px;">단말기 아이디</th>
                                <th data-hide="phone,tablet" style="text-align: center;">벤더, 모델명</th>
                                <th data-hide="phone" style="text-align: center; max-width: 50px; min-width: 50px;">OS 버전</th>
                                <th data-hide="phone,tablet" style="text-align: center; max-width: 100px; min-width: 100px;">전화번호</th>
                                <th style="text-align: center;">앱</th>
                                <th style="text-align: center; max-width: 70px; min-width: 70px;">구분</th>
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

<#-- 삭제 대기를 위해 화면 전체 block -->
<div id="ckpush-window-block" style="display:none;">
    <h6>단말기 삭제 중 입니다. 잠시만 대기 하세요.</h6>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        <#-- 페이지 도움말 영역 visible, invisible 처리 -->
        my.fn.initPageHelper('${_page_id}-helper');

        <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/app/device/list/t/" ; job>
            <#if job == "buttons">
                    [{
                        sButtonText: '단말기 삭제',
                        sExtends: 'text',
                        fnClick: function(nButton, oConfig, oFlash) {
                            <#-- 선택 된 device_srl을 구한다 -->
                            var reqData = {c_keys: []};
                            $('.check-row-data').each(function(){
                                var $this = $(this);
                                if($this.is(':checked')) {
                                    var element = $this.data('sn').split('|');
                                    reqData.c_keys.push({device_srl:element[0], app_srl:element[1]});
                                }
                            });

                            if(reqData.c_keys.length <= 0) {
                                <@am.jsnoti title="단말기 삭제 실패" content="선택된 단말기가 없습니다" contenttype="text" />
                                return;
                            }

                            var successText = reqData.c_keys.length+'개의 단말기를 삭제 했습니다.';
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

                                <#-- 선택 된 단말기 삭제 요청 -->
                                var $btn = $(nButton);
                                $btn.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');

                                <@am.jsajax_request submituri="${Request.contextPath}/admin/app/device/t/"
                                        method="DELETE" errortitle="단말기 삭제 실패" ; job >
                                    <#if job == "success_job">
                                        <#-- 삭제 성공시 테이블을 다시 로딩. remove 하면 내부 데이터만 삭제되지 화면에 반영 되지 않는다.
                                             draw 를 호출 해야 반영 된다. -->
                                        <@am.jsnoti title="단말기 삭제 완료" content="successText" boxtype="success" />
                                        otable.draw(false);
                                    <#elseif job == "common_job">
                                        $.unblockUI();
                                        $btn.removeClass('disabled').html('<span>선택 된 단말기 삭제</span>');
                                    </#if>
                                </@am.jsajax_request>
                            };

                            my.fn.showUserConfirm('단말기 삭제',
                                    "선택된 단말기를 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                                    '삭제를 입력하세요',
                                    '네', '삭제', process, '앱 삭제 실패', '삭제를 입력하지 않았습니다.');
                        }
                    }]
            <#elseif job == "order">
                    [[1, 'desc']]
            <#elseif job == "columns">
                    [
                    {sName: 'checked', mData: null, bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            return '<label class="checkbox"><input type="checkbox" class="check-row-data" data-sn="'+full.device_srl+'|'+full.app_srl+'"><i></i></label>';
                        }
                    },
                    {sName: 'device_srl', mData: 'device_srl', bSortable: true, bSearchable: false},
                    {sName: 'device_id', mData: 'device_id', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full, dtObj){
                            <#-- 클릭시 a link 로 detail 페이지로 이동 -->
                            var navHash = $(location).attr('hash').substring(1),
                                    moveURL = '#${Request.contextPath}/admin/app/' + full.app_srl + '/device/' + full.device_srl;
                            return '<a class="datatable-row-detail-view" data-move="' +
                                    moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                        }
                    },
                    {sName: 'device_type', mData: 'device_type', bSortable: true, bSearchable: true},
                    {sName: 'os_version', mData: 'os_version', bSortable: true, bSearchable: true},
                    {sName: 'mobile_phone_number', mData: 'mobile_phone_number', bSortable: true, bSearchable: true},
                    {sName: 'app_name', mData: 'app_name', bSortable: false, bSearchable: true},
                    {sName: 'device_class', mData: 'device_class', sClass:'dt_column_text_center', bSortable: false, bSearchable: true,
                        mRender: function(data, type, full){
                            if(data == ${android_device_class}) {
                                return '<img src="${Request.resPath}img/android.png" alt="Android" width="18">';
                            } else if(data == ${ios_device_class}) {
                                return '<img src="${Request.resPath}img/apple.png" alt="iOS" width="18">';
                            } else {
                                return '-';
                            }
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