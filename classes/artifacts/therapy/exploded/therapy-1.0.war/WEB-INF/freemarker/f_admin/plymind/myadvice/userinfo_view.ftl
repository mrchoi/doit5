<#assign _page_id="myadvice-user-info">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담 상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">상담 필수 항목 정보</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row col-md-12">
                            <div class="well-box">
                                <div class="row">
                                    <div class="col-md-2">
                                        <div class="well text-center">이름</div>
                                    </div>
                                    <div class="col-md-10">
                                        <div class="well">
                                        ${memberEntity.user_name}
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <div class="row">
                                    <div class="col-md-2">
                                        <div class="well text-center">생년월일</div>
                                    </div>
                                    <div class="col-md-10">
                                        <div class="well">
                                            ${memberEntity.memberExtraEntity.birthday?substring(0, 4)}.${memberEntity.memberExtraEntity.birthday?substring(4, 6)}.${memberEntity.memberExtraEntity.birthday?substring(6, 8)}
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <div class="row">
                                    <div class="col-md-2">
                                        <div class="well text-center">직업</div>
                                    </div>
                                    <div class="col-md-10">
                                        <div class="well">
                                            ${memberEntity.memberExtraEntity.job}
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <div class="row">
                                    <div class="col-md-2">
                                        <div class="well text-center">결혼유무</div>
                                    </div>
                                    <div class="col-md-10">
                                        <div class="well">
                                            <#if memberEntity.memberExtraEntity.marriage?int == 1>유</#if>
                                            <#if memberEntity.memberExtraEntity.marriage?int == 2>무</#if>
                                            ( 자녀수 <#if memberEntity.memberExtraEntity.children?? && memberEntity.memberExtraEntity.children != ''>${memberEntity.memberExtraEntity.children}<#else>0</#if> 명 )
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <div class="row">
                                    <div class="col-md-2">
                                        <div class="well text-center">종교</div>
                                    </div>
                                    <div class="col-md-10">
                                        <div class="well">
                                            <#if memberEntity.memberExtraEntity.religion?int == 1>유</#if>
                                            <#if memberEntity.memberExtraEntity.religion?int == 2>무</#if>
                                            <#if memberEntity.memberExtraEntity.religion_name?? && memberEntity.memberExtraEntity.religion_name != ''>
                                                ( ${memberEntity.memberExtraEntity.religion_name} )
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <div class="row">
                                    <div class="col-md-2">
                                        <div class="well text-center">장애</div>
                                    </div>
                                    <div class="col-md-10">
                                        <div class="well">
                                            <#if memberEntity.memberExtraEntity.disability?int == 1>유</#if>
                                            <#if memberEntity.memberExtraEntity.disability?int == 2>무</#if>
                                            <#if memberEntity.memberExtraEntity.disability_type?? && memberEntity.memberExtraEntity.disability_type != ''>
                                                ( ${memberEntity.memberExtraEntity.disability_type} ${memberEntity.memberExtraEntity.disability_rate} 등급 )
                                            </#if>
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <div class="row">
                                    <div class="col-md-2">
                                        <div class="well text-center">학력</div>
                                    </div>
                                    <div class="col-md-10">
                                        <div class="well">
                                            <#if memberEntity.memberExtraEntity.academic_srl?int == 1>박사학위</#if>
                                            <#if memberEntity.memberExtraEntity.academic_srl?int == 2>대학원졸</#if>
                                            <#if memberEntity.memberExtraEntity.academic_srl?int == 3>대학교졸</#if>
                                            <#if memberEntity.memberExtraEntity.academic_srl?int == 4>고등학교 재학</#if>
                                            <#if memberEntity.memberExtraEntity.academic_srl?int == 5>고등학교졸</#if>
                                            <#if memberEntity.memberExtraEntity.academic_srl?int == 6>중학교 재학</#if>
                                            <#if memberEntity.memberExtraEntity.academic_srl?int == 7>중학교 졸</#if>
                                            <#if memberEntity.memberExtraEntity.academic_srl?int == 8>초등학교 재학</#if>
                                            <#if memberEntity.memberExtraEntity.academic_srl?int == 9>초등학교졸</#if>
                                        </div>
                                    </div>
                                </div>
                                <br>
                                <div class="row">
                                    <div class="col-md-2">
                                        <div class="well text-center">성별</div>
                                    </div>
                                    <div class="col-md-10">
                                        <div class="well">
                                            <#if memberEntity.memberExtraEntity.gender?int == 1>남자</#if>
                                            <#if memberEntity.memberExtraEntity.gender?int == 2>여자</#if>
                                        </div>
                                    </div>
                                </div>
                                <br>
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