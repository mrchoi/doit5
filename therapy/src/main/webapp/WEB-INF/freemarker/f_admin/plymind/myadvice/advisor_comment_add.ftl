<#assign _page_id="myadvice-comment_add">
<#assign _page_parent_name="심리상담관리">
<#assign _page_current_name="심리상담 상세정보">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-4" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">상담사 코멘트 등록</@am.widget_title>
                <div>
                    <div class="widget-body">
                        <form id="checkout-form">
                            <fieldset>
                                <#if applicationMemberEntities?size &gt; 1>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="row">
                                                <div class="col-sm-12">
                                                    <p>사용자 선택</p>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-sm-12">
                                                    <div class="form-group">
                                                        <label for="0">
                                                            <input type="radio" name="application_member" id="0" value="0" class="styled application-member" checked> 전체
                                                        </label>&nbsp;&nbsp;&nbsp;
                                                        <#list applicationMemberEntities as applicationMemberEntity>
                                                            <label for="${applicationMemberEntity.member_srl}">
                                                                <input type="radio" name="application_member" id="${applicationMemberEntity.member_srl}" value="${applicationMemberEntity.member_srl}" class="styled application-member" <#if member_srl == applicationMemberEntity.member_srl>checked</#if>> ${applicationMemberEntity.user_name}
                                                            </label>&nbsp;&nbsp;&nbsp;
                                                        </#list>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </#if>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="form-group">
                                            <label>상담사 코멘트</label>
                                            <textarea id="advisor_comment" name="advisor_comment" class="form-control" rows="5"></textarea>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-12 inline-group">
                                        <div class="form-group">
                                            <label>검사지 파일</label>
                                            <input class="form-control" id="checkup_file" name="checkup_file" data-file-srl="" value="" readonly>
                                        </div>
                                        <div class="btn-group">
                                            <button id="btn_file_search" class="btn btn-sm btn-default" type="button">검사지 파일 검색</button>
                                        </div>
                                    </div>
                                </div>
                            </fieldset>

                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn_comment_add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 코멘트 추가
                                    </button>
                                </div>
                            </div>
                        </form>
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

<@am.bstrap_popup_tag_modal/>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        <@am.jsselect2 id="file_list" placeholder="파일명을 입력하세요" uniq="file_srl"
        showColumn="orig_name" uri="${Request.contextPath}/admin/plymind/myadvice/select2/t/"
        miniumLength=5 />

        <#-- 심리상담 상세정보 페이지로 이동 -->
        $('#btn-progress-detail').click(function(e) {
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/myadvice/progress_detail/${member_srl}/${application_srl}/${application_group}');
        });

        $('#btn_file_search').click(function(e){
            <@am.jsevent_prevent />
            $('#target-tag-form').each(function(){ this.reset(); });
            $('#target-tag-remote-modal').modal('show');
        });

        $('#btn_file_add').click(function(e){
            var select2Data = $('#file_list').select2('data');

            $('#checkup_file').val(select2Data.orig_name);

            $('#checkup_file').attr('data-file-srl', select2Data.file_srl);
        });

        $('#btn_comment_add').click(function(e) {
            var member_srl = 0;
            $('input:radio[name="application_member"]:checked').each(function() {
                member_srl = this.value;
            });

            var advisor_comment = $.trim($('#advisor_comment').val());

            if(advisor_comment == '') {
                var toastMsg = '상담사 코멘트를 작성해 주세요.';
                <@am.jsnoti title="선택 날짜 확인" content="toastMsg" boxtype="warn" />
                return;
            }

            var file_srl = 0;
            if($('#checkup_file').attr('data-file-srl') != '') {
                file_srl = $('#checkup_file').attr('data-file-srl');
            }

            var file_kind = 0;
            if(file_srl != 0) {
                file_kind = 1;
            }

            var reqData = {
                "member_srl"        : member_srl,
                "application_srl"   : ${application_srl},
                "application_group" : ${application_group},
                "advisor_comment"   : advisor_comment,
                "checkup_file_srl"  : file_srl,
                "reply_file_srl"    : '',
                "result_file_srl"   : ''
            };

            <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/myadvice/advisor_comment_add/t/"
            moveuri="#${Request.contextPath}/admin/plymind/myadvice/progress_detail/${member_srl}/${application_srl}/${application_group}" errortitle="상담사 코멘트 등록 실패" ; job >
                <#if job == "success_job">
                    var toastMsg = '상담사 코멘트 등록이 정상적으로 이루어졌습니다.';
                    <@am.jsnoti title="상담사 코멘트 등록 성공" content="toastMsg" boxtype="success" />
                </#if>
            </@am.jsajax_request>
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