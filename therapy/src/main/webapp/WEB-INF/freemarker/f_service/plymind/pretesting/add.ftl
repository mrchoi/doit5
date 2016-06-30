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
<#assign _page_id="user-payment-canecl">
<#assign _page_parent_name="">
<#assign _page_current_name="사전질문(인테이크)">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<div class="main-container">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h3>"다음은 플리마인드를 방문한 당신의 현재 상태와 기분이나 문제 등을 질문하는 것입니다.</h3>
                <h3>여기에 대답한 것과 선택하신 검사 영역을 바탕으로 보다 적합한 상담전문가를 추천 드리고자 합니다."</h3>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <blockquote>인테이크 질문지 작성</blockquote>
            </div>
        </div>

        <#assign questionNumber=0?int>
        <#list questionEntities as questionEntity><#--문의사항 View를 위한 루프 START qqqqqqqqqq-->
            <#assign questionNumber=questionNumber+1?int>

        <#--문의사항 View START -->
        <div class="row">
            <div class="col-md-12">
                <p>${questionNumber}. ${questionEntity.question}</p>
            </div>
        </div>
        <#--문의사항 View END -->

        <#if questionEntity.kind_srl == 999><#--문의사항 중 직접 입력의 경우 IF START -->

        <div class="row">
            <div class="col-md-12 form-group">
                <textarea class="form-control" rows="3" id="textarea_content" name="textarea_content"></textarea>
            </div>
        </div>

        <#else><#--문의사항 중 직접 입력의 경우 ELSE START -->

        <#list kindEntities as kindEntity><#--분류 View를 위한 루프 START kkkkkkkkkk-->
        <#if questionEntity.question_srl == kindEntity.question_srl><#-- 분류 View를 위한 IF START - 현재 문의사항과 일치하는 분류만 표시 -->
        <div class="row">
            <div class="col-md-12 form-group">
                <div class="well">
                    <div class="billing-details">
                        <#--분류제목 View START -->
                        <#if kindEntity.kind_srl != 998>
                        <h2><i class="fa fa-check-square"></i> ${kindEntity.kind}</h2>
                        </#if>
                        <#--분류제목 View START -->

                        <#assign cnt=0?int>
                        <#list pretestingEntities as pretestingEntity><#--항목 View를 위한 루프 START iiiiiiiiii-->
                        <#if kindEntity.kind_srl == pretestingEntity.kind_srl><#-- 항목 View를 위한 IF START - 현재 분류와 일치하는 항목만 표시 -->
                        <#if (cnt % 4) == 0>
                        <div class="row">
                            <div class="col-md-3">
                                <div class="checkbox">
                                    <input type="checkbox" kind="${pretestingEntity.question_srl}" name="checkbox"
                                           id="${pretestingEntity.kind_srl}-${pretestingEntity.item_srl}"
                                           value="${pretestingEntity.question_srl}-${pretestingEntity.kind_srl}-${pretestingEntity.item_srl}" class="styled">
                                    <label for="${pretestingEntity.kind_srl}-${pretestingEntity.item_srl}" class="control-label">${pretestingEntity.item}</label>
                                </div>
                                <#if pretestingEntity.item_srl == 41><input type="text" id="ect_content" name="ect_content"/></#if>
                            </div>
                            <#assign cnt=cnt+1?int>
                        <#elseif (cnt % 4) == 3>
                            <div class="col-md-3">
                                <div class="checkbox">
                                    <input type="checkbox" kind="${pretestingEntity.question_srl}" name="checkbox"
                                           id="${pretestingEntity.kind_srl}-${pretestingEntity.item_srl}"
                                           value="${pretestingEntity.question_srl}-${pretestingEntity.kind_srl}-${pretestingEntity.item_srl}" class="styled">
                                    <label for="${pretestingEntity.kind_srl}-${pretestingEntity.item_srl}" class="control-label">${pretestingEntity.item}</label>
                                </div>
                                <#if pretestingEntity.item_srl == 41><input type="text" id="ect_content" name="ect_content"/></#if>
                            </div>
                        </div>
                            <#assign cnt=cnt+1?int>
                        <#else>
                            <div class="col-md-3">
                                <div class="checkbox">
                                    <input type="checkbox" kind="${pretestingEntity.question_srl}" name="checkbox"
                                           id="${pretestingEntity.kind_srl}-${pretestingEntity.item_srl}"
                                           value="${pretestingEntity.question_srl}-${pretestingEntity.kind_srl}-${pretestingEntity.item_srl}" class="styled">
                                    <label for="${pretestingEntity.kind_srl}-${pretestingEntity.item_srl}" class="control-label">${pretestingEntity.item}</label>
                                </div>
                                <#if pretestingEntity.item_srl == 41><input type="text" id="ect_content" name="ect_content"/></#if>
                            </div>
                            <#assign cnt=cnt+1?int>
                        </#if>

                        </#if><#-- 항목 View를 위한 IF END - 현재 분류와 일치하는 항목만 표시 -->
                        </#list><#--항목 View를 위한 루프 END iiiiiiiiii-->

                        <#if (cnt % 4) != 0>
                        </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
        </#if><#-- 분류 View를 위한 IF END - 현재 문의사항과 일치하는 분류만 표시 -->
        </#list><#--분류 View를 위한 루프 END kkkkkkkkkk-->
        </#if><#--문의사항 중 직접 입력의 경우 END IF START -->
        </#list><#--문의사항 View를 위한 루프 END qqqqqqqqqq-->

        <div class="row">
            <div class="col-md-12">
            </div>
        </div>
        <div class="row">
            <div class="col-md-12 tp-title-center form-group">
                <button id="btn_checkup" name="btn_checkup" class="btn tp-btn-default tp-btn-lg">검사 진행하기</button>
                <button id="btn_advice" name="btn_advice" class="btn tp-btn-default tp-btn-lg">상담 진행하기</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        $(".styled").click(function() {
            var $this = this;
            var checkCnt = $('input:checkbox[kind="1"]:checked').length;

            if(checkCnt > 3) {
                $('#pop-message').text("3가지만 선택 가능합니다.");
                popup_layer();
                $('input:checkbox[kind="1"][value="' + $this.value + '"]').removeAttr("checked");
                return;
            }
        });

        $("#btn_checkup").click(function() {
            var checkboxValues = "";
            $('input:checkbox[name="checkbox"]:checked').each(function() {
                checkboxValues = checkboxValues + "@" + this.value;
            });

            checkboxValues = checkboxValues + "@" + "3-999-67";

            var textareaContent = $('#textarea_content').val();
            var etcContent = $('#ect_content').val();

            var checkCnt1 = $('input:checkbox[kind="1"]:checked').length;
            if(checkCnt1 != 3) {
                $('#pop-message').text("사전질문 1번 항목에 대해서 3가지를 선택해 주세요. ( " + checkCnt1 + "개 선택 )");
                popup_layer();
                return;
            }

            if($('input:checkbox[id="7-41"]').is(':checked') && etcContent == '') {
                $('#pop-message').text("기타 항목을 입력해 주세요. ");
                popup_layer();
                return;
            }

            var checkCnt2 = $('input:checkbox[kind="2"]:checked').length;
            if(checkCnt2 == 0) {
                $('#pop-message').text("사전질문 2번 항목에 대해서 1가지 이상 선택해 주세요.");
                popup_layer();
                return;
            }

            if(textareaContent == '') {
                $('#pop-message').text("사전질문 3번 항목을 입력해 주세요.");
                popup_layer();
                return;
            }

            var dataString = {"checkboxValues":checkboxValues.substring(1),"etcContent":etcContent,"textareaContent":textareaContent};

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/pretesting/add',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    $('#pop-message').text("정상적으로 등록 되었습니다.");
                    popup_layer();

                    timer = setInterval(function() {
                        my.fn.pmove('${Request.contextPath}/user/pretesting/checkup/' + data.c_date);
                    }, 1500);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("등록에 실패했습니다. 다시 시도해 주세요");
                    popup_layer();
                    return;
                }
            });
        });

        $("#btn_advice").click(function() {
            var checkboxValues = "";
            $('input:checkbox[name="checkbox"]:checked').each(function () {
                checkboxValues = checkboxValues + "@" + this.value;
            });

            checkboxValues = checkboxValues + "@" + "3-999-67";

            var textareaContent = $('#textarea_content').val();
            var etcContent = $('#ect_content').val();

            var checkCnt1 = $('input:checkbox[kind="1"]:checked').length;
            if (checkCnt1 != 3) {
                $('#pop-message').text("사전질문 1번 항목에 대해서 3가지를 선택해 주세요. ( " + checkCnt1 + "개 선택 )");
                popup_layer();
                return;
            }

            if ($('input:checkbox[id="7-41"]').is(':checked') && etcContent == '') {
                $('#pop-message').text("기타 항목을 입력해 주세요. ");
                popup_layer();
                return;
            }

            var checkCnt2 = $('input:checkbox[kind="2"]:checked').length;
            if (checkCnt2 == 0) {
                $('#pop-message').text("사전질문 2번 항목에 대해서 1가지 이상 선택해 주세요.");
                popup_layer();
                return;
            }

            if (textareaContent == '') {
                $('#pop-message').text("사전질문 3번 항목을 입력해 주세요.");
                popup_layer();
                return;
            }

            var dataString = {
                "checkboxValues": checkboxValues.substring(1),
                "etcContent": etcContent,
                "textareaContent": textareaContent
            }

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/pretesting/add',
                contentType : 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    $('#pop-message').text("정상적으로 등록 되었습니다.");
                    popup_layer();

                    timer = setInterval(function() {
                        my.fn.pmove('${Request.contextPath}/user/pretesting/advice');
                    }, 1500);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("등록에 실패했습니다. 다시 시도해 주세요");
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