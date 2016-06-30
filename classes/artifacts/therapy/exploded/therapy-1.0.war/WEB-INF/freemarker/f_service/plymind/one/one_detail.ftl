<@ap.py_header_load_script />

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-one-detail">
<#assign _page_parent_name="나의 활동">
<#assign _page_current_name="1:1 답변확인">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<@ap.py_popup_layer />

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h2>${_page_current_name} 보기</h2>
                    </div>
                    <#--<div class="col-md-12">
                        <div class="call-to-action well-box">
                            <p></p>
                        </div>
                    </div>-->
                </div>
            </div>
        </div>


        <div class="row">
            <div class="col-md-12">
                <div class="well-box">
                    <form >

                        <!-- Select Basic -->
                        <div class="form-group">
                            <label class="control-label" for="category">상담(검사)</label>
                            <div class="well">${applicationEntity.title}</div>
                        </div>

                        <!-- Text input-->
                        <div class="form-group">
                            <label class="control-label" for="title">제목 </label>
                            <div class="well">${documentEntity.document_title}</div>
                        </div>

                        <#-- 게시물 본문 -->
                        <div class="form-group">
                            <label class="control-label" for="content">내용 </label>
                            <div class="well">${documentEntity.document_content}</div>
                        </div>
                        <#-- 게시물 본문 끝 -->
                        <div class="form-group">
                            <label class="control-label" for="title">문의일 </label>
                            <div class="well">${(documentEntity.c_date * 1000)?number_to_date?string("yyyy-MM-dd HH:mm")}</div>
                        </div>

                        <#--<div class="form-group">
                            <label class="control-label" for="">상담관련 문의를 남겨주시면 상담사가 확인 후 답변을 드립니다.</label>
                        </div>-->

                        <!-- Button -->
                        <div class="form-group">
                            <button class="btn tp-btn-primary btn-prev-page">
                                <i class="fa fa-chevron-left"></i> 1:1 답변확인 목록
                            </button>

                            <button id="btn-delete" name="btn-delete" class="btn tp-btn-lg btn-danger" >
                                <i class="fa fa-trash-o"></i> 삭제
                            </button>

                            <div class="btn-group pull-right">
                                <button id="modify-document" name="modify-document" class="btn tp-btn-primary" type="button">
                                    <i class="fa fa-check"></i> 수정
                                </button>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
            <div class="col-md-12">
                <div class="customer-review">
                    <div class="review-list">
                        <!-- First Comment -->
                    <#list documentCommentEntities as documentCommentEntity>
                        <div class="row" id="dt-row-comment-${documentCommentEntity.comment_srl}">
                            <div class="col-md-12 col-sm-12">
                                <div class="panel panel-default arrow left">
                                    <div class="panel-body">
                                        <div class="text-left">
                                            <div class="col-md-2"><h3>${documentCommentEntity.nick_name}</h3> </div>
                                            <div class="comment-date">
                                                <i class="fa fa-clock-o"></i>
                                                <#assign cDate = (documentCommentEntity.c_date*1000)?number_to_datetime>
                                                ${cDate}
                                            </div>
                                        </div>
                                        <div class="review-post">
                                            <p> ${documentCommentEntity.comment_content} </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </#list>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

$(document).ready(function() {

    $('.btn-prev-page').click(function(e){
    <@am.jsevent_prevent />
        my.fn.pmove('${Request.contextPath}/user/one/list');
    });

    $("#modify-document").click(function() {
        my.fn.pmove('${Request.contextPath}/user/one/modify/${documentEntity.document_srl}');
    });


    <#-- 게시물 삭제 -->
    $('#btn-delete').click(function(e){
    <@am.jsevent_prevent />
        var $this = $(this);
        var reqData = {l_keys: [${documentEntity.document_srl}]};
        var successText = '게시물을 삭제 했습니다.';
        //var process = function() {
            $this.addClass('disabled').html('<span><i class="fa fa-gear fa-spin"></i> 삭제중</span>');
        <@ap.py_jsajax_request submituri="${Request.contextPath}/user/board/document/t/"
        method="DELETE" errortitle="게시물 삭제 실패" ; job >
            <#if job == "success_job">
            <#-- 삭제 성공시 리스트 페이지로 이동 -->
                <@ap.py_jsnoti title="게시물 삭제 완료" content="successText" boxtype="success" />
            <#-- 헐...이거 click 반응으로 사용자 반응 이후 호출 되는 곳 말고, 자동으로 시나리오 타고 오는 경우
                 my.fn.pmove 를 바로 호출 하니 message box 에 문제 생긴다. 약간의 딜레이를 준다 -->
                setTimeout(function(){
                    my.fn.pmove('${Request.contextPath}/user/one/list');
                }, 500);

            <#elseif job == "common_job">
                $this.removeClass('disabled').html('<i class="fa fa-trash-o"></i> 삭제');
            </#if>
        </@ap.py_jsajax_request>
        //};
       // my.fn.showUserConfirm('게시물',
       //         "사항을 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
       //         '삭제를 입력하세요',
       //         '네', '삭제', process, '공지사항 삭제 실패', '삭제를 입력하지 않았습니다.');
    });


});

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
