<#--
    추가를 통해서 리스트 페이지로 올때는 리스트 페이지 테이블의 localStorage 를 초기화 해야 한다.
    아니면 이전 화면이 보이기 때문에 많이 이상함.
    localStorage 초기화 하면 리스트의 1번 페이지를 보여 준다.
-->

<#assign _page_id="list-advisor">
<#assign _page_parent_name="플리마인드 관리">
<#assign _page_current_name="상담사 관리">

<@am.page_simple_depth icon="fa-table" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    Backoffice에 가입된 상담사 리스트를 보여 줍니다.
<p>
    상담사 삭제를 위해서는 열의 제일 앞에 있는 선택을 표시하고 <strong>상담사 삭제</strong> 버튼을 누르면 선택된 상담사가
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="상담사 삭제 버튼"><strong>삭제</strong></span> 됩니다.
</p>
</@am.page_desc>

<#-- widget grid -->
<section id="widget-grid" class="">
<#-- row -->
    <div class="row">
    <#-- NEW WIDGET START -->
        <article class="col-sm-12 col-md-12 col-lg-12">
        <#-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-list-app-0" data-widget-colorbutton="false" data-widget-editbutton="false"
                 data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">상담사 목록</@am.widget_title>

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
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="사용자 아이디 검색"
                                           rel="tooltip" data-placement="top" data-original-title="사용자 아이디 첫글자 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-3" placeholder="별명 검색"
                                           rel="tooltip" data-placement="top" data-original-title="별명 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-4" placeholder="이름 검색"
                                           rel="tooltip" data-placement="top" data-original-title="이름 첫글자 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <select class="form-control input-xs dt-column dt-column-5" style="margin-left: -0px;">
                                        <option value="0">전체</option>
                                        <option value="1">으뜸</option>
                                        <option value="2">가장</option>
                                        <option value="3">한마루</option>
                                    </select>
                                </th>
                                <th class="hasinput">
                                    <select class="form-control input-xs dt-column dt-column-5" style="margin-left: -0px;">
                                        <option value="${mdv_nouse}">전체</option>
                                        <option value="${mdv_yes}">활성</option>
                                        <option value="${mdv_no}">중지</option>
                                        <option value="${mdv_deny}">차단</option>
                                    </select>
                                </th>
                                <th class="hasinput">
                                    <select class="form-control input-xs dt-column dt-column-6" style="margin-left: -0px;">
                                        <option value="${mdv_nouse}">전체</option>
                                        <option value="${mdv_no}">가입</option>
                                        <option value="${mdv_yes}">탈퇴</option>
                                    </select>
                                </th>
                                <th class="hasinput"></th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th data-hide="phone,tablet" style="text-align: center;max-width: 15px;min-width: 15px;"><i class="fa fa-check-circle-o"></i></th>
                                <th data-class="expand" style="text-align: center; max-width: 45px;min-width: 45px;">SN.</th>
                                <th style="text-align: center;">아이디</th>
                                <th data-hide="phone" style="...">별명</th>
                                <th data-hide="phone" style="text-align: center; max-width: 100px;">이름</th>
                                <th data-hide="phone" style="text-align: center; max-width: 100px;">등급</th>
                                <th data-hide="phone" style="text-align: center; max-width: 50px; min-width: 50px;">상태</th>
                                <th data-hide="phone,tablet" style="text-align: center; max-width: 50px; min-width: 50px;">탈퇴</th>
                                <th data-hide="phone" style="text-align: center; max-width: 138px; min-width: 138px;">가입일</th>
                                <th data-hide="phone,tablet" style="text-align: center; max-width: 138px; min-width: 138px;">로그인</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    <#-- Action 버튼 -->
                        <div class="widget-footer smart-form">
                            <div class="btn-group">
                                <button id="add-advisor" class="btn btn-sm btn-default" type="button">
                                    <i class="fa"></i> 상담사 추가
                                </button>
                            </div>
                        </div>
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
    <h6>사용자 삭제 중 입니다. 잠시만 대기 하세요.</h6>
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
        $('#add-advisor').click(function(e) {
            var moveURL = '#${Request.contextPath}/admin/advisor/add';
        <@am.jschangecontent moveuri="moveURL" navuri="navHash" isVarMove=true isVarNav=true />
        });

    <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/advisor/list/t/" ; job>
        <#if job == "buttons">
                [{
                    sButtonText: '상담사 삭제',
                    sExtends: 'text',
                    fnClick: function(nButton, oConfig, oFlash) {
                    <#-- 선택 된 member_srl을 구한다 -->
                        var reqData = {l_keys: []};
                        $('.check-row-data').each(function(){
                            var $this = $(this);
                            if($this.is(':checked')) reqData.l_keys.push($this.data('sn'));
                        });

                        if(reqData.l_keys.length <= 0) {
                            <@am.jsnoti title="상담사 삭제 실패" content="선택된 상담사가 없습니다" contenttype="text" />
                            return;
                        }
                        var successText = reqData.l_keys.length+'명의 상담사를 삭제 했습니다.';
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

                        <#-- 선택 된 상담사 삭제 요청 -->
                            var $btn = $(nButton);
                            $btn.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');

                            <@am.jsajax_request submituri="${Request.contextPath}/admin/member/t/"
                            method="DELETE" errortitle="상담사 삭제 실패" ; job >
                                <#if job == "success_job">
                                <#-- 삭제 성공시 테이블을 다시 로딩. remove 하면 내부 데이터만 삭제되지 화면에 반영 되지 않는다.
                                     draw 를 호출 해야 반영 된다. -->
                                    <@am.jsnoti title="상담사 삭제 완료" content="successText" boxtype="success" />
                                <#-- ajax 로 로딩 하기 때문에 row 삭제가 의미 없다. 의미 없어도 메뉴얼에서 하라는데로 해 두었음. -->
                                    _.each(reqData.l_keys, function(sn){
                                        otable.row($('#dt-row-'+sn)).remove();
                                    });
                                    otable.draw(false);
                                <#elseif job == "common_job">
                                    $.unblockUI();
                                    $btn.removeClass('disabled').html('<span>상담사 삭제</span>');
                                </#if>
                            </@am.jsajax_request>
                        };

                        my.fn.showUserConfirm('상담사 삭제',
                                "선택된 상담사를 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                                '삭제를 입력하세요',
                                '네', '삭제', process, '상담사 삭제 실패', '삭제를 입력하지 않았습니다.');
                    }
                }]
        <#elseif job == "order">
                [[1, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'checked', mData: null, bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        return '<label class="checkbox"><input type="checkbox" class="check-row-data" data-sn="'+full.member_srl+'"><i></i></label>';
                    }
                },
                {sName: 'member_srl', mData: 'member_srl', bSortable: true, bSearchable: false},
                {sName: 'user_id', mData: 'user_id', bSortable: true, bSearchable: true,
                    mRender: function(data, type, full, dtObj){
                    <#-- 클릭시 a link 로 detail 페이지로 이동 -->
                        var navHash = $(location).attr('hash').substring(1),
                                moveURL = '#${Request.contextPath}/admin/advisor/' + full.member_srl;
                        return '<a class="datatable-row-detail-view" data-move="' +
                                moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                    }
                },
                {sName: 'nick_name', mData: 'nick_name', bSortable: false, bSearchable: true},
                {sName: 'user_name', mData: 'user_name', bSortable: false, bSearchable: true},
                {sName: 'class_srl', mData: 'class_srl', sClass: 'dt_column_text_center', bSortable: false, bSearchable: true,
                    mRender: function(data, type, full){
                        if(data == 1) {
                            return '으뜸';
                        } else if(data == 2) {
                            return '가장';
                        } else if(data == 3) {
                            return '한마루';
                        } else {
                            return '-';
                        }
                        return data;
                    }
                },
                {sName: 'enabled', mData: 'enabled', sClass: 'dt_column_text_center', bSortable: false, bSearchable: true,
                    mRender: function(data, type, full){
                        // 탈퇴한 회원의 상태는 무시 한다.
                        if(full.sign_out == ${mdv_yes}) return '-';

                        if(data == ${mdv_yes})      return '활성';
                        else if(data == ${mdv_no})  return '중단';
                        else if(data == ${mdv_deny})return '차단';
                        return '-';
                    }
                },
                {sName: 'sign_out', mData: 'sign_out', sClass: 'dt_column_text_center', bSortable: false, bSearchable: true,
                    mRender: function(data, type, full){
                        if(data == ${mdv_no})       return '가입';
                        else if(data == ${mdv_yes}) return '탈퇴';
                        return '-';
                    }
                },
                {sName: 'c_date', mData: 'c_date', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        return new Date(my.fn.toNumber(data) * 1000).format('yyyy-MM-dd HH:mm:ss');
                    }
                },
                {sName: 'last_login_date', mData: 'last_login_date', bSortable: false, bSearchable: false,
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