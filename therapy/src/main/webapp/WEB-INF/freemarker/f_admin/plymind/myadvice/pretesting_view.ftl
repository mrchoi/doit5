<#assign _page_id="myadvice-pretesting-info">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담 상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">인테이크지 검사 정보</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <#list questionEntities as questionEntity>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="well-box">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div class="well">
                                                    <p>${questionEntity.question}</p>
                                                    <hr>

                                                    <#assign kind_srl=0?int>
                                                    <#list kindEntities as kindEntity>
                                                        <#if questionEntity.question_srl == kindEntity.question_srl
                                                        && kindEntity.kind_srl != kind_srl>
                                                            <div class="row">
                                                                <div class="col-md-12">
                                                                    <#if kindEntity.kind_srl != 998 && kindEntity.kind_srl != 999>
                                                                    <p><i class="fa fa-check-square"></i> ${kindEntity.kind}</p>
                                                                    </#if>

                                                                    <#list itemEntities as itemEntity>
                                                                        <#if questionEntity.question_srl == kindEntity.question_srl
                                                                        && kindEntity.kind_srl == itemEntity.kind_srl>
                                                                            <#if itemEntity.item_srl == 41>
                                                                                [ ${itemEntity.item} : ${itemEntity.contents} ]
                                                                            <#elseif itemEntity.item_srl == 67 >
                                                                                [ ${itemEntity.contents} ]
                                                                            <#else>
                                                                                [ ${itemEntity.item} ]
                                                                            </#if>
                                                                        </#if>
                                                                    </#list>
                                                                </div>
                                                            </div>
                                                            <br>
                                                            <#assign kind_srl=kindEntity.kind_srl>
                                                        </#if>
                                                    </#list>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br>
                        </#list>
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
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/progress_detail/${member_srl}/${application_srl}/${application_group}');

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