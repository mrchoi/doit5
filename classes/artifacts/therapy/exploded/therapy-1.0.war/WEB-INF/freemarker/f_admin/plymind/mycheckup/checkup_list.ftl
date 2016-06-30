<#assign _page_id="mycheckup-ckeckup-list">
<#assign _page_parent_name="심리 검사 관리">
<#assign _page_current_name="심리 검사 목록">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />
<#-- widget grid -->
<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">${_page_current_name}</@am.widget_title>
                <div>
                    <div class="widget-body no-padding">
                        <table id="${_page_id}" class="table table-striped table-bordered" width="100%">
                            <thead>
                            <tr>
                                <th class="hasinput smart-form"></th>
                                <th class="hasinput"></th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-2" placeholder="상담명 검색"
                                           rel="tooltip" data-placement="top" data-original-title="상담명 기준 검색" />
                                </th>
                                <th class="hasinput">
                                    <input type="text" class="form-control input-xs dt-column dt-column-3" placeholder="이름 검색"
                                           rel="tooltip" data-placement="top" data-original-title="이름 기준 검색" />
                                </th>
                                <th class="hasinput"></th>
                            </tr>
                            <tr>
                                <th rclass="text-center" style="min-width:50px;">번호</th>
                                <th rclass="text-center" style="min-width:50px;">구분</th>
                                <th rclass="text-center" style="min-width:50px;">신청내역</th>
                                <th rclass="text-center" style="min-width:50px;">신청자</th>
                                <th rclass="text-center" style="min-width:50px;">진행상태</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                </div>
            </div>
        </article>
    </div>
</section>

<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        my.fn.showSessionTime(${sessionRestSec});

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };

        <@am.jsdatatable listId="${_page_id}" url="${Request.contextPath}/admin/mycheckup/list/t/{tid}" ; job>
            <#if job == "buttons">
                    []
            <#elseif job == "order">
                    [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'application_srl', mData: 'application_srl', sClass: 'text-center', bSortable: true, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            return data;
                        }
                    },
                    {sName: 'kind', mData: 'kind', sClass: 'text-center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if(data == '1') {
                                return '심리 상담';
                            } else {
                                return '심리 검사';
                            }
                        }
                    },
                    {sName: 'title', mData: 'title', sClass: 'text-center', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full, dtObj) {
                            var viewStr = '';

                            viewStr += data + ' ';

                            if(full.kind == '1') {
                                if (full.advice_type == '1') {
                                    viewStr += ' - 으뜸 ' + full.advice_number + '회 ';
                                }
                                if (full.advice_type == '2') {
                                    viewStr += ' - 가장 ' + full.advice_number + '회 ';
                                }
                                if (full.advice_type == '3') {
                                    viewStr += ' - 한마루 ' + full.advice_number + '회 ';
                                }

                                if (full.advice_period == '1') {
                                    viewStr += ' - 1주';
                                }
                                if (full.advice_period == '4') {
                                    viewStr += ' - 4주 ( 1개월 )';
                                }
                                if (full.advice_period == '12') {
                                    viewStr += ' - 12주 ( 3개월 )';
                                }
                                if (full.advice_period == '24') {
                                    viewStr += ' - 24주 ( 6개월 )';
                                }
                                if (full.advice_period == '52') {
                                    viewStr += ' - 52주 ( 12개월 )';
                                }
                            }

                            var navHash = $(location).attr('hash').substring(1),
                                    //moveURL = '#${Request.contextPath}/admin/plymind/myadvice/progress_detail/progress/' + full.member_srl +'/'+ full.application_group;
                                    moveURL = '#${Request.contextPath}/admin/mycheckup/' + full.member_srl +'/'+ full.application_group;
                            return '<a class="datatable-row-detail-view" data-move="' +
                                    moveURL + '" data-nav="' + navHash + '">' + viewStr + '</a>';

                            return viewStr;
                        }
                    },
                    {sName: 'user_name', mData: 'user_name', sClass: 'text-center', bSortable: true, bSearchable: true},
                    {sName: 'application_status', mData: 'application_status', sClass: 'text-center', bSortable: true, bSearchable: false,
                        mRender: function(data, type, full, dtObj) {
                            if(data == 0) return '접수';
                            else if(data == 1) return '준비중';
                            else if(data == 2) return '진행중';
                            else if(data == 3) return '완료';
                            else if(data == 4) return '취소';
                            else return '-';

                        }
                    }
                ]
            </#if>
        </@am.jsdatatable>
    };

    $(document).on("click", '#deposit', function() {
        var $this = $(this);

        var payment_srl = $this.attr('data-payment-srl');
        var application_group = $this.attr('data-application-group');
        var appointment_srl = $this.attr('data-appointment-srl');

        var reqData = {
            "payment_srl"           : payment_srl,
            "application_group"     : application_group,
            "appointment_srl"       : appointment_srl
        };

        <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/payment/deposit/t/"
        moveuri="" errortitle="입금처리 실패" ; job >
            <#if job == "success_job">
                var toastMsg = '입금처리가 정상적으로 이루어졌습니다.';
                <@am.jsnoti title="입금 처리 성공" content="toastMsg" boxtype="success" />
            <#elseif job == "common_job">
                my.fn.removeDatatableStorage('payment-payment-list');
            </#if>
        </@am.jsajax_request>
    });

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