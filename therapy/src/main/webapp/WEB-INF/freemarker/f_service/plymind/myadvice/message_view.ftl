<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js"></script>
<script src="${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js"></script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-mind_diary">
<#assign _page_parent_name="마이페이지">
<#assign _page_current_name="현재 진행중인 상담">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<div class="main-container">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <blockquote>메세지</blockquote>
            </div>
        </div>
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
                                    <div class="text-center">
                                        ${(messageEntity.noti_time1 * 1000)?number_to_date?string("yyyy.MM.dd HH:mm")}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    </#list>
                </div>
            </div>
        </div>
        <div class="form-group">
            <button class="btn tp-btn-primary btn-prev-page">
                <i class="fa fa-chevron-left"></i> 이전
            </button>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('.btn-prev-page').click(function(e){
            <@ap.py_jsevent_prevent />
            var menuType = '${menu_type}';
            if(menuType == "complete"){
                my.fn.pmove('${Request.contextPath}/user/mycomplete/complete_detail/${application_srl}/${application_group}');
            }else{
                my.fn.pmove('${Request.contextPath}/user/myadvice/progress_detail/${menu_type}/${application_srl}/${application_group}');
            }
        });
    });

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->