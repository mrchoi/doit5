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
                <blockquote>마음일기 등록</blockquote>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <h3>짧게라도 하루를 정리하며, 어제보다 내가 달라진 점이 무엇인지 내일은 어떻게 달라지고 싶은지에 대해서 간단하게 적어보세요.</h3>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="well-box">
                    <form >
                        <div class="form-group">
                            <label class="control-label" for="document_title">마음 일기</label>
                            <textarea id="mind_diary" name="mind_diary" class="form-control" rows="10"></textarea>
                        </div>

                        <!-- Button -->
                        <div class="form-group">
                            <button class="btn tp-btn-primary btn-prev-page">
                                <i class="fa fa-chevron-left"></i> 이전
                            </button>
                            <div class="btn-group pull-right">
                                <button id="btn-add" name="submit" class="btn tp-btn-primary" type="button">
                                    <i class="fa fa-check"></i> 마음일기 등록
                                </button>
                            </div>
                        </div>

                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $('.btn-prev-page').click(function(e){
            <@ap.py_jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/myadvice/progress_detail/${menu_type}/${application_srl}/${application_group}');
        });

        $('#btn-add').click(function(e) {
            var mind_diary = $('#mind_diary').val().replace(/\n/g, '<br>');

            if(mind_diary == '') {
                $('#pop-message').text("마음일기를 작성해 주세요.");
                popup_layer();
                return;
            }

            var dataString = {
                "application_group" : ${application_group},
                "application_srl"   : ${application_srl},
                "mind_diary"        : $.trim(mind_diary)
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/myadvice/mind_diary_add',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    $('#pop-message').text("마음일기가 정상적으로 등록되었습니다.");
                    popup_layer();

                    timer = setInterval(function() {
                        my.fn.pmove('${Request.contextPath}/user/myadvice/progress_detail/${menu_type}/${application_srl}/${application_group}');
                    }, 1500);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("마음일기 등록이 실패되었습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });
        });
    });

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->