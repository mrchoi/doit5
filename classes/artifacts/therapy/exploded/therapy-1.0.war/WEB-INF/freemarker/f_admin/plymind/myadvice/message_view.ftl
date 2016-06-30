<#assign _page_id="myadvice-message-view">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담 상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">메세지 보기</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="well-box">
                                    <#list messageEntities as messageEntity>
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div class="well">
                                                    <div class="row">
                                                        <div class="col-md-9">
                                                            ${messageEntity.push_text}
                                                        </div>
                                                        <#if messageEntity.noti_time1?? && messageEntity.noti_time1 &gt; 0>
                                                            <div class="text-center">
                                                                ${(messageEntity.noti_time1 * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}
                                                            </div>
                                                        <#else>
                                                            <div class="text-center">
                                                                ${(messageEntity.book_time * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}
                                                            </div>
                                                        </#if>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                    </#list>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>

        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="btn-group pull-left">
                <button id="btn-progress-detail" class="btn btn-sm btn-info" type="button">
                    <i class="fa fa-chevron-left"></i> 이전
                </button>
            </div>
        </article>
    </div>
</section>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});


        <#-- 심리상담 상세정보 페이지로 이동 -->
        $('#btn-progress-detail').click(function(e){
            <@am.jsevent_prevent />
            var menuType = '${menu_type}';
            if(menuType == "complete"){
                my.fn.pmove('#${Request.contextPath}/admin/mycomplete/complete_detail/${member_srl}/${application_srl}/${application_group}');
            }else{
                my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/progress_detail/${member_srl}/${application_srl}/${application_group}');
            }

        });
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