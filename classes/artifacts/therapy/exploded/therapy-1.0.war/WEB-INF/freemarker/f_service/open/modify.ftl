<@ap.py_header_load_script />
<link rel="stylesheet" type="text/css" media="screen" href="${Request.svcResPath}css/smartadmin-production-plugins.min.css">
<script type="text/javascript" src="${Request.resPath}js/ckpush/md5.js"></script>
<script src="${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js"></script>
<script src="${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js"></script>
<style>
    .inbox-info-bar .select2-container-multi .select2-choices{border-color:#fff!important}.inbox-info-bar .select2-choices>div{display:none}
    .layer1 {
        display: none;
        position: fixed;
        _position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        z-index: 100;
    }
    .layer1 .pop-layer {
        display: block;
    }

    .layer1 .dimBg {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: #000;
        opacity: .5;
        filter: alpha(opacity=50);
    }
    .pop-layer .pop-container {
        padding: 20px 25px;
    }

    .pop-layer .btn-r1 {
        width: 100%;
        margin: 10px 0 10px;
        padding-top: 10px;
        border-top: 1px solid #DDD;
        text-align: right;
    }

    .pop-layer {
        display: none;
        position: absolute;
        top: 40%;
        left: 40%;
        width: 410px;
        height: auto;
        background-color: #ffffff;
        /*border: 5px solid #3571B5;*/
        z-index: 10;
    }
</style>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="modify">
<#assign _page_parent_name="">
<#assign _page_current_name="정보변경">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12 tp-title-center">
            <#--
            <h1>Create a account &amp; save you date.<br>
                <small>Start Planning  It's Free</small> </h1>
            -->
                <h2>회원 정보 수정</h2>
            </div>
        </div>

        <div class="row">
            <div class="col-md-offset-2 col-md-8 singup-couple">

                <div class="well-box">
                    <h3>필수입력 항목</h3>
                    <input type="hidden" name="member_srl" value="${member_srl}">

                    <!-- 이름 -->
                    <div class="form-group">
                        <label class="control-label" for="user_name">이름<span class="required">*</span></label>
                        <input id="user_name" name="user_name" type="text" value="${user_name}" class="form-control input-md" readonly>
                    </div>

                    <!-- Text input-->
                    <div class="form-group">
                        <label class="control-label" for="user_id">아이디<span class="required">*</span></label>
                        <input id="user_id" name="user_id" type="text" value="${user_id}" class="form-control input-md" disabled>
                    </div>

                    <!-- Text input-->
                    <div class="form-group">
                        <label class="control-label" for="nick_name">닉네임<span class="required">*</span></label>
                        <div class="input-group">
                            <input type="hidden" id="nickName" value="${nick_name}">
                            <input id="nick_name" name="nick_name" type="text" placeholder="닉네임" value="${nick_name}" class="form-control input-md" required>
                            <input type="hidden" id="nick_name_check" name="nick_name_check" value="-1">
                            <span class="input-group-btn">
                                <button id="btn-nickname-check" class="btn tp-btn-primary tp-btn-lg" type="button">중복확인</button>
                            </span>
                        </div>
                        <label class="control-label">닉네임은 한글과 영문으로 띄어쓰기 없이 4~8자리까지 사용 가능합니다.</label>
                    </div>

                    <!-- 이메일 -->
                    <div class="form-group">
                        <label class="control-label" for="user_address">이메일<span class="required">*</span></label>
                        <input id="email_address" name="email_address" type="text" placeholder="이메일" value="${email_address}" class="form-control input-md" required>
                    </div>
                    <div class="form-group">
                        <div class="checkbox checkbox-success">
                            <input type="checkbox" name="allow_mailing" id="allow_mailing" value="1" class="styled" ${allow_mailing}>
                            <label for="checkbox-0" class="control-label"> 이메일 수신 동의 (검사결과, 예약일정, 이벤트 안내 등) </label>
                        </div>
                    </div>

                    <!-- 전화번호 -->
                    <div class="form-group">
                        <label class="control-label" for="user_name">휴대폰번호<span class="required">*</span></label>
                        <input id="mobile_phone_number" name="mobile_phone_number" type="text" placeholder="휴대폰번호" value="${mobile_phone_number}" class="form-control input-md" required>
                    </div>
                    <div class="form-group">
                        <div class="checkbox checkbox-success">
                            <input type="checkbox" name="allow_message" id="allow_message" value="1" class="styled" ${allow_message}>
                            <label for="checkbox-0" class="control-label"> 메시지 수신동의 (검사결과 발송안내, 아이디 & 비밀번호 찾기, 예약일정, 컨텐츠 테라피 등) </label>
                        </div>
                    </div>

                    <!-- 회원구분 -->
                    <div class="form-group">
                        <label class="control-label">회원구분<span class="required">*</span></label>
                        <input type="hidden" id="group_gubun" name="group_gubun" value="${group_gubun}" >
                    </div>
                    <div class="form-group">
                        <label for="">
                            <input type="radio" name="member_gubun" id="member_gubun1" value="1" class="styled"> 개인
                        </label>&nbsp;&nbsp;&nbsp;
                        <label for="">
                            <input type="radio" name="member_gubun" id="member_gubun2" value="2" class="styled"> 단체소속
                        </label>&nbsp;&nbsp;&nbsp;
                        <div id="organization_search" style="display: none;">
                            <div class="input-group">
                                <input id="grouptext" name="grouptext" value="${user_group_name}" type="text" placeholder="" class="form-control input-md" readonly>
                                <input type="hidden" name="groupSrl" id="groupSrl">
                                <span class="input-group-btn">
                                    <button id="grouplist" class="btn tp-btn-primary tp-btn-lg" type="button" data-toggle="modal" data-target="#group" data-box-number="1">단체검색</button>
                                </span>
                            </div>
                            <label class="control-label">단체검색에서 소속을 확인한 후 선택해 주시기 바랍니다.</label>
                        </div>
                    </div>

                    <div class="form-group">
                        <a href="${Request.contextPath}/user"><button class="btn tp-btn-primary tp-btn-lg">취소</button></a>
                        <button id="btn-signup" class="btn tp-btn-primary tp-btn-lg">저장</button>
                        <a href="${Request.contextPath}/user/mydata/modify/pass"><button id="btn-pass" class="btn tp-btn-default tp-btn-lg">비밀번호 변경</button></a>
                        <a href="${Request.contextPath}/user/mydata/modify/register_optional?num=${member_srl}"><button id="btn-pass" class="btn tp-btn-default tp-btn-lg">기초 자료 항목 수정</button></a>
                    </div>

                </div>
            </div>
        </div>

    </div>
</div>
<!-- /.main container -->

<div class="modal fade" id="group" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true" style="padding-top: 15%;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">단체 검색</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <fieldset class="smart-form">
                            <section>
                                <label >단체명</label>
                                <label for="file" class="input input-file" style="display: block;">
                                    <div id="group_list" class="select2" style="width:100%;height:30px;"></div>
                                </label>
                            </section>
                        </fieldset>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button id="btn_group_add" type="button" class="btn btn-default" data-dismiss="modal" data-box-number="" >추가</button>
            </div>
        </div>
    </div>
</div>

<div class="layer1">
    <div class="dimBg"></div>
    <div class="pop-layer">
        <div class="pop-container">
            <div class="pop-conts">
                <p id="pop-message1" class="ctxt mb20"></p>
            <#-- <div class="btn-r">
                 <a href="#" class="btn-layerClose">Close</a>
             </div>-->
                <div class="btn-r1">
                    <a href="${Request.contextPath}/user" class="btn btn-default" data-dismiss="modal">닫기</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script language="javascript">
    var group_gubun = $('#group_gubun').val();
    $("input[name=member_gubun][value="+group_gubun+"]").attr("checked",true);
</script>

<script type="text/javascript">

    $(document).ready(function() {
        <@ap.jsselect2 id="group_list" placeholder="단체명을 입력하세요." uniq="document_srl"
        showColumn="document_title" uri="${Request.contextPath}/user/open/register/list/t/"
        miniumLength=2 />

        $('#btn_group_add').click(function (e) {
            var select2Data = $('#group_list').select2('data');
            $('#grouptext').val(select2Data.document_title);
            $('#groupSrl').val(select2Data.document_srl);
        });
    });

    function ajax_request(reqData, submituri) {

        $.ajax({
            type: 'GET',
            url: submituri,
            //data: JSON.stringify(reqData),
            //dataType: 'json',
            beforeSend: function(request) {
                request.setRequestHeader('Accept', 'application/json;charset=UTF-8');
                request.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
            },
            success: function(data, status, xhr) {
            },
            error: function(xhr, status, error) {
                console.log('error ajax request');
                console.log(status);
                console.log(error);

                alert("네트웍 오류 입니다. ");

            },
            complete: function(xhr, status) {
                console.log(xhr);
                if(xhr && xhr.responseJSON) {
                    if (xhr.responseJSON.result == '1') {
                        $('#pop-message').text('존재하는 아이디 입니다.');
                        popup_layer();
                        $('#user_id_check').val('1');
                        return;
                    } else if(xhr.responseJSON.result == '2') {
                        $('#pop-message').text('존재하는 닉네임 입니다.');
                        popup_layer();
                        $('#nick_name_check').val('1');
                        return;
                    } else if(xhr.responseJSON.result == '3') {
                        $('#pop-message').text('사용가능한 닉네임 입니다.');
                        popup_layer();
                        $('#nick_name_check').val('0');
                        return;
                    }
                    else {
                        $('#pop-message').text('사용가능한 아이디 입니다.');
                        popup_layer();
                        $('#user_id_check').val('0');
                        return;
                    }
                }
            }
        });
    }
    if($('input:radio[name=member_gubun]:checked').val() == '2'){
        $('#organization_search').show();
    }

    $('#member_gubun1').click(function(e){
        $('#organization_search').hide();
        return;
    });

    $('#member_gubun2').click(function(e){
        $('#organization_search').show();
        return;
    });

    $('#btn-nickname-check').click(function(e){

        if ($('#nick_name').val() == '') {
            $('#pop-message').text("닉네임은 필수 입력값입니다.");
            popup_layer();
            return;
        }
        var re_nickname = /^[a-zA-z가-힝]{0,}$/;
        var re_nickname_lenght = /^[a-zA-z가-힝]{4,8}$/;
        if (re_nickname.test($('#nick_name').val()) != true){
            $('#pop-message').text("한글과 영문만 사용 가능합니다.");
            popup_layer();
            return;
        }
        if (re_nickname_lenght.test($('#nick_name').val()) != true){
            $('#pop-message').text("4~8자리 사이로 사용 가능합니다.");
            popup_layer();
            return;
        }


        var reqData = {
            nick_name: $.trim($('input[name=nick_name]').val())
        }

        ajax_request(reqData, '${Request.contextPath}/signup/member/nickNameCheck?nick_name=' + reqData.nick_name);
    });

    function test(title) {
        var grouptitle = title.grouptitle.value;
        alert(grouptitle);
        $('#grouptext').val(grouptitle);

        $('#group').dialog("close");
        return;
    }

    <@ap.py_jsdatatable listId="${_page_id}" searchTooltip="제목 첫글자 기준 검색" url="${Request.contextPath}/user/open/register/list/t/" ; job>
        <#if job == "order">
                [[0, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'document_title', mData: 'document_title', bSortable: true, bSearchable: true},

                {sName: 'c_date', mData: 'c_date', sClass: 'dt_column_text_center', bSortable: true, bSearchable: false,
                    mRender: function (data, type, full) {
                        var title = full.document_title;
                        var form = "form_"+full.document_srl;
                        var html = '<form name="'+form+'"><input type="hidden" id="grouptitle" name="grouptitle" value="'+title+'">';
                        html += '<input type="button" data-dismiss="modal" onclick="test(this.form)" value="선택"></form>' ;
                        return html;
                    }
                }
                ]
        </#if>
    </@ap.py_jsdatatable>

    <#-- 회원 수정을 진행한다. -->
    $('#btn-signup').click(function(e){

        var re_phone = /^[0-9]{0,}$/;

        if ($('#nick_name').val() == '') {
            $('#pop-message').text("닉네임은 필수 입력값입니다.");
            popup_layer();
            return;
        }
        if ($('#nick_name').val() != $('#nickName').val()){
            if ($('#nick_name_check').val() != '0'){
                $('#pop-message').text("닉네임 중복체크를 확인해 주세요.");
                popup_layer();
                return;
            }
        }
        else if ($('#email_address').val() == '') {
            $('#pop-message').text("이메일은 필수 입력값입니다.");
            popup_layer();
            return;
        }
        else if ($('#mobile_phone_number').val() == '') {
            $('#pop-message').text("휴대폰번호는 필수 입력값입니다.");
            popup_layer();
            return;
        }
        else if (re_phone.test($('#mobile_phone_number').val()) != true){
            $('#pop-message').text("휴대폰번호는 숫자만 입력해주세요");
            popup_layer();
            return;
        }

        var reqData = {
            member_srl:         $.trim($('input[name=member_srl]').val()),
            user_id:            $.trim($('input[name=user_id]').val()),
            email_address:      $.trim($('input[name=email_address]').val()),
            user_name:          $.trim($('input[name=user_name]').val()),
            nick_name:          $.trim($('input[name=nick_name]').val()),
            mobile_phone_number:$.trim($('input[name=mobile_phone_number]').val()),
            description:        '기본정회원',
            enabled:            '1',
            allow_mailing:      $('input:checked[name=allow_mailing]').is(':checked') ? '1': '2',
            allow_message:      $('input:checked[name=allow_message]').is(':checked') ? '1': '2',
            email_confirm:      '2',
            account_type:       '2',
            app_account_type:   '2',
            app_srl:            '100',
            user_group_srl:    $.trim($('input[name=groupSrl]').val()),
            group_gubun:        $('input:radio[name=member_gubun]:checked').val() ? $('input:radio[name=member_gubun]:checked').val() : '2'
        };

        if(reqData.user_password == null || reqData.user_password != '')
            reqData.user_password = CryptoJS.MD5(reqData.user_password).toString();

        console.log(my.data);

        function layer() {

            var $el = $('.pop_layer');        //레이어의 id를 $el 변수에 저장

            var $elWidth = ~~($el.outerWidth()),
                    $elHeight = ~~($el.outerHeight()),
                    docWidth = $(document).width(),
                    docHeight = $(document).height();

            // 화면의 중앙에 레이어를 띄운다.
            if ($elHeight < docHeight || $elWidth < docWidth) {
                $el.css({
                    marginTop: -$elHeight / 2,
                    marginLeft: -$elWidth / 2
                })
            } else {
                $el.css({top: 0, left: 0});
            }
            $('.layer1').css('display','block');
            return;
        }

        $.ajax({
            type: "POST",
            url: '${Request.contextPath}/user/mydata/modify/t/' + $.now(),
            contentType: 'application/json',
            data: JSON.stringify(reqData),
            success: function (res) {
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $('#pop-message').text("수정에 실패하였습니다.");
                popup_layer();
                return;
            },
            complete: function (xhr, status) {
                if (xhr && xhr.responseJSON && xhr.responseJSON.ck_error == my.data.ajaxSuccessCode) {
                    $('#pop-message1').text("수정 완료되었습니다.");
                    layer();
                } else {
                    console.log(xhr);
                    $('#pop-message').text("수정에 실패하였습니다.");
                    popup_layer();
                    return;
                }
            }
        });
    });

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
