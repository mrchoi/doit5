<@ap.py_header_load_script />
<script type="text/javascript" src="${Request.resPath}js/ckpush/md5.js"></script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="drop">
<#assign _page_parent_name="">
<#assign _page_current_name="회원탈퇴">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12 tp-title-center">
                <h1>플리마인드를 이용해 주셔서 감사합니다.</h1>
                <h4>플리마인드 회원탈퇴를 하실 경우 아래 내용과 같이 회원정보가 처리됩니다.</h4>
                <br>
                <h4>- 회원탈퇴신청 즉시 회원탈퇴처리되며, 해당 아이디의 회원정보는 즉시 삭제처리됩니다.</h4>
                <h4>- 회원님이 작성하신 문의내역은 삭제되지 않습니다.</h4>
                <h4>- 회원탈퇴 이후 같은 아이디로는 재가입이 불가능합니다.</h4>
            </div>
        </div>

        <div class="row">
            <div class="col-md-offset-2 col-md-8 singup-couple">

                <input type="hidden" name="member_srl" value="${member_srl}">
                <input type="hidden" name="user_id" value="${user_id}">

                <div class="form-group">
                    <a href="${Request.contextPath}/user"><button class="btn tp-btn-primary tp-btn-lg">취소</button></a>
                    <button id="btn-signout" class="btn tp-btn-lg btn-danger">회원탈퇴</button>
                </div>

            </div>
        </div>

    </div>
</div>
<!-- /.main container -->

<script type="text/javascript">

    <#-- 회원 가입을 진행한다. -->
    $('#btn-signout').click(function(e){

        var reqData = {
            member_srl:         $.trim($('input[name=member_srl]').val()),
            user_id:            $.trim($('input[name=user_id]').val()),
            email_address:      $.trim($('input[name=email_address]').val()),
            user_name:          $.trim($('input[name=user_name]').val()),
            nick_name:          $.trim($('input[name=nick_name]').val())
        };

        if(reqData.user_password == null || reqData.user_password != '')
            reqData.user_password = CryptoJS.MD5(reqData.user_password).toString();

        console.log(my.data);

        $.ajax({
            type: "POST",
            url: '${Request.contextPath}/user/mydata/drop/signOut',
            contentType: 'application/json',
            data: JSON.stringify(reqData),
            success: function (res) {
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $('#pop-message').text("회원 탈퇴에 실패하였습니다.");
                popup_layer();
                return;
            },
            complete: function (xhr, status) {
                if (xhr && xhr.responseJSON && xhr.responseJSON.ck_error == my.data.ajaxSuccessCode) {
                    $('#pop-message').text("탈퇴 성공하였습니다.");
                    popup_layer();
                    location.href = "${Request.contextPath}/user/open/logout";
                } else {
                    console.log(xhr);
                    $('#pop-message').text("회원 탈퇴에 실패하였습니다.");
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
