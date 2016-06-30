<@ap.py_header_load_script />
<script type="text/javascript" src="${Request.resPath}js/ckpush/md5.js"></script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="password">
<#assign _page_parent_name="">
<#assign _page_current_name="비밀번호 변경">
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
                <h1>비밀번호 변경</h1>
            </div>
        </div>

        <div class="row">
            <div class="col-md-offset-2 col-md-8 singup-couple">

                <input type="hidden" name="member_srl" value="${member_srl}">
                <input type="hidden" name="user_id" value="${user_id}">
                <input type="hidden" name="passwordVal" value="${user_password}">

                <div class="well-box">
                    <!-- 이름 -->
                    <div class="form-group">
                        <label class="control-label" for="old_user_password">현재 비밀번호<span class="required">*</span></label>
                        <input id="old_user_password" name="old_user_password" type="password" placeholder="비밀번호" value="" class="form-control input-md" required>
                        <label class="control-label">현재 사용중인 비밀번호를 입력해 주세요</label>
                    </div>
                    <div class="form-group">
                        <label class="control-label" for="user_password">새 비밀번호<span class="required">*</span></label>
                        <input id="user_password" name="user_password" type="password" placeholder="새 비밀번호" value="" class="form-control input-md" required>
                        <label class="control-label">비밀번호는 영문과 숫자,특수문자 조합으로 6~15자리까지 사용 가능합니다.</label>
                    </div>
                    <div class="form-group">
                        <label class="control-label" for="re_user_password">새 비밀번호 확인<span class="required">*</span></label>
                        <input id="re_user_password" name="re_user_password" type="password" placeholder="새 비밀번호 확인" value="" class="form-control input-md" required>
                        <label class="control-label">새 비밀번호를 다시 입력해 주시기 바랍니다.</label>
                    </div>

                    <div class="form-group">
                        <a href="${Request.contextPath}/user/mydata/modify"><button class="btn tp-btn-primary tp-btn-lg"> <i class="fa fa-chevron-left"></i> 이전</button></a>
                        <button id="btn-pass" class="btn tp-btn-primary tp-btn-lg">저장</button>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<!-- /.main container -->

<script type="text/javascript">

    <#-- 회원 가입을 진행한다. -->
    $('#btn-pass').click(function(e){
        var re_pass = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,15}$/;
        if ($('#old_user_password').val() == ''){
            $('#pop-message').text('비밀번호는 필수 입력값입니다.');
            popup_layer();
            return;
        }
        else if ($('#user_password').val() == '') {
            $('#pop-message').text('새 비밀번호를 입력해 주세요.');
            popup_layer();
            return;
        }
        else if (re_pass.test($('#user_password').val()) != true){
            $('#pop-message').text('비밀번호는 반드시 영문/숫자/특수기호 혼합하여 6~15자리까지 사용 가능합니다.');
            popup_layer();
            return;
        }
        else if ($('#re_user_password').val() == '') {
            $('#pop-message').text("새 비밀번호를 다시 입력해 주세요.");
            popup_layer();
            return;
        }
        else if ($('#user_password').val() != $('#re_user_password').val()) {
            $('#pop-message').text("새 비밀번호가 일치하지 않습니다.");
            popup_layer();
            return;
        }
        else if ($('#old_user_password').val() == $('#user_password').val()){
            $('#pop-message').text("기존 비밀번호와 다른 비밀번호로 입력해 주세요.");
            popup_layer();
            return;
        }


        var reqData = {
            member_srl:         $.trim($('input[name=member_srl]').val()),
            email_address:      $.trim($('input[name=email_address]').val()),
            user_password:      $.trim($('input[name=user_password]').val()),
            old_password:       $.trim($('input[name=old_user_password]').val()),
            user_name:          $.trim($('input[name=user_name]').val()),
            nick_name:          $.trim($('input[name=nick_name]').val()),
            mobile_phone_number:$.trim($('input[name=mobile_phone_number]').val()),
            allow_mailing:      $('input:checked[name=allow_mailing]').is(':checked') ? '1': '2',
            allow_message:      $('input:checked[name=allow_message]').is(':checked') ? '1': '2'
        };

        if(reqData.user_password == null || reqData.user_password != '')
            reqData.user_password = CryptoJS.MD5(reqData.user_password).toString();

        reqData.old_password = CryptoJS.MD5(reqData.old_password).toString();

        console.log(my.data);

        $.ajax({
            type: "POST",
            url: '${Request.contextPath}/user/mydata/modify/passmodify',
            contentType: 'application/json',
            data: JSON.stringify(reqData),
            success: function (res) {
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $('#pop-message').text("비밀번호 변경에 실패하였습니다.!!");
                popup_layer();
                return;
            },
            complete: function (xhr, status) {
                if (xhr && xhr.responseJSON && xhr.responseJSON.ck_error == my.data.ajaxSuccessCode) {
                    $('#pop-message').text("비밀번호 변경에 성공하였습니다..");
                    popup_layer();
                    location.href = "${Request.contextPath}/user/open/logout";
                    return;
                } else {
                    console.log(xhr);
                    $('#pop-message').text("현재 비밀번호가 일치하지 않습니다.");
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
