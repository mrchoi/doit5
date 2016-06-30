<@ap.py_header_load_script /><#--  -->
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="forget">
<#assign _page_parent_name="">
<#assign _page_current_name="아이디/비밀번호 찾기">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />

<div class="main-container"><!-- main container -->
    <div class="container">

        <div class="row">
            <div class="col-md-12 tp-title-center"><h2>  아이디 / 비밀번호 찾기</h2>
                <!--<p>We're happy to have you back.</p>-->
            </div>

        </div>
        <div class="col-md-offset-3 col-md-6 st-tabs">
            <!-- Nav tabs -->
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab" id="search-id"> 아이디 찾기 </a></li>
                <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab" id="search-pass"> 비밀번호 찾기 </a></li>
            </ul>

            <!-- Tab panes -->
            <div class="tab-content ">
                <div role="tabpanel" class="tab-pane active vendor-login" id="home">
                    <!-- Text input-->
                    <div class="form-group">
                        <label class="control-label" for="user_name">이름<span class="required">*</span></label>
                        <input id="user_name" name="user_name" type="text" placeholder="이름" class="form-control input-md" required>
                    </div>

                    <!-- Text input-->
                    <div class="form-group">
                        <label class="control-label" for="email_address">이메일<span class="required">*</span></label>
                        <input id="email_address" name="email_address" type="text" placeholder="이메일" class="form-control input-md" required>
                    </div>

                    <!-- Button -->
                    <div class="form-group">
                        <button id="search-userid-btn" class="btn tp-btn-primary tp-btn-lg">확인</button>
                        <button id="register-btn-byid" class="btn tp-btn-default tp-btn-lg">회원가입</button>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane active vendor-login" id="home-result" style="display: none;">
                    <div class="call-to-action well-box">
                        <p align="left">
                        개인정보 도용에 대한 피해 방지를 위하여 아이디 뒷자리는 **표기하였습니다.
                        아이디 뒷자리를 확인하려면 회원정보에 등록된 이메일로 받으실 수 있습니다.
                        </p>
                        <p id="home-result-text"></p>
                    </div>

                    <!-- Button -->
                    <div class="form-group">
                        <button id="login-btn" class="btn tp-btn-primary tp-btn-lg">로그인</button>
                        <button id="email-btn" class="btn tp-btn-primary tp-btn-lg">이메일 받기</button>
                    </div>
                </div>
                <div role="tabpanel" class="tab-pane couple-login" id="profile"><!-- Text input-->
                    <!-- Text input-->
                    <div class="form-group">
                        <label class="control-label" for="pass_user_name">이름<span class="required">*</span></label>
                        <input id="pass_user_name" name="pass_user_name" type="text" placeholder="이름" class="form-control input-md" required>
                    </div>

                    <!-- Text input-->
                    <div class="form-group">
                        <label class="control-label" for="pass_user_id">아이디<span class="required">*</span></label>
                        <input id="pass_user_id" name="pass_user_id" type="text" placeholder="아이디" class="form-control input-md" required>
                    </div>

                    <!-- Text input-->
                    <div class="form-group">
                        <label class="control-label" for="pass_email_address">이메일<span class="required">*</span></label>
                        <input id="pass_email_address" name="pass_email_address" type="text" placeholder="이메일" class="form-control input-md" required>
                    </div>

                    <div class="call-to-action well-box" id="temp-pass-text" style="display: none;">
                        <p align="left">
                            이메일로 발급 받으신 비밀번호를 확인하신 후 로그인 해 주십시오.
                            개인정보보호를 위하여 로그인 후 비밀번호를 변경해 주십시오.
                        </p>
                        <p id="home-result-text"></p>
                    </div>

                    <!-- Button -->
                    <div class="form-group">
                        <button id="temp-pass-btn" name="submit" class="btn tp-btn-primary tp-btn-lg">임시비밀번호 전송하기</button>
                        <button id="register-btn-bypass" name="submit" class="btn tp-btn-default tp-btn-lg">회원가입</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- /.main container -->

<script type="text/javascript">
    $(document).ready(function() {
        // 아이디찾기 - 회원가입
        $('#register-btn-byid').click(function(e) {
            location.href = "${Request.contextPath}/user/open/register";
        });
        // 비밀번호찾기 - 회원가입
        $('#register-btn-bypass').click(function(e) {
            location.href = "${Request.contextPath}/user/open/register";
        });
        $('#search-id').click(function(e) {
            $('#home').show();
            $('#home-result').hide();
        });
        $('#search-pass').click(function(e) {
            $('#home').hide();
            $('#home-result').hide();
        });

        // 아이디찾기 -> 확인버튼
        $('#search-userid-btn').click(function(e) {
            var re_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
            if ($('#user_name').val() == '') {
                $('#pop-message').text('이름을 입력해주세요.');
                popup_layer();
                return;
            }
            else if ($('#email_address').val() == '') {
                $('#pop-message').text('이메일을 입력해주세요.');
                popup_layer();
                return;
            }
            else if (re_email.test($('#email_address').val()) != true) {
                $('#pop-message').text('이메일 형식이 맞지 않습니다.');
                popup_layer();
                return;
            }
            var reqData = {
                user_name:            $.trim($('input[name=user_name]').val()),
                email_address:        $.trim($('input[name=email_address]').val())
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/signup/member/search/userId/' + $.now(),
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: reqData,
                success: function (res) {
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("아이디 찾기에 실패하였습니다.");
                    popup_layer();
                    return;
                },
                complete: function (xhr, status) {
                    if (xhr && xhr.responseJSON && xhr.responseJSON.ck_error == my.data.ajaxSuccessCode
                            && xhr.responseJSON.user_id.length > 0) {
                        var userIds = xhr.responseJSON.user_id;
                        var userId_desc = '';
                        for (var i=0 ; i<userIds.length ; i++) {
                            userId_desc += userIds[i].substring(0, userIds[i].length - 2) + "**";
                            if (i<userIds.length-1) userId_desc += ",";
                        }
                        $('#home-result-text').text($('#user_name').val() + '님의 아이디는 ' + userId_desc + ' 입니다.');
                    } else {
                        $('#home-result-text').text($('#user_name').val() + '님의 아이디는 존재하지 않습니다.');
                    }
                    $('#home').hide();
                    $('#home-result').show();
                }
            });
        });
        // 아이디찾기 -> 로그인
        $('#login-btn').click(function(e) {
            location.href = "${Request.contextPath}/user/open/login";
        });
        // 아이디찾기 -> 이메일받기
        $('#email-btn').click(function(e) {
            var reqData = {
                user_name:            $.trim($('input[name=user_name]').val()),
                email_address:        $.trim($('input[name=email_address]').val())
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/signup/member/email/userId/' + $.now(),
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: reqData,
                success: function (res) {
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("메일 보내기에 실패하였습니다.");
                    popup_layer();
                    return;
                },
                complete: function (xhr, status) {
                    if (xhr && xhr.responseJSON && xhr.responseJSON.ck_error == my.data.ajaxSuccessCode) {
                        $('#pop-message').text("회원정보에 등록된 이메일로 아이디를 전송하였습니다.");
                        popup_layer();
                        return;
                    } else {
                        $('#pop-message').text("메일 보내기에 실패하였습니다.");
                        popup_layer();
                        return;
                    }
                }
            });
        });

        $('#temp-pass-btn').click(function(e) {
            var re_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;
            if ($('#pass_user_name').val() == '') {
                $('#pop-message').text('이름을 입력해주세요.');
                popup_layer();
                return;
            }
            else if ($('#pass_user_id').val() == '') {
                $('#pop-message').text('아이디를 입력해주세요.');
                popup_layer();
                return;
            }
            else if ($('#pass_email_address').val() == '') {
                $('#pop-message').text('이메일을 입력해주세요.');
                popup_layer();
                return;
            }
            else if (re_email.test($('#pass_email_address').val()) != true) {
                $('#pop-message').text('이메일 형식이 맞지 않습니다.');
                popup_layer();
                return;
            }
            var reqData = {
                user_name:            $.trim($('input[name=pass_user_name]').val()),
                user_id:              $.trim($('input[name=pass_user_id]').val()),
                email_address:        $.trim($('input[name=pass_email_address]').val())
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/signup/member/passwd/' + $.now(),
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: reqData,
                success: function (res) {
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("메일 보내기에 실패하였습니다.");
                    popup_layer();
                    return;
                },
                complete: function (xhr, status) {
                    if (xhr && xhr.responseJSON && xhr.responseJSON.ck_error == my.data.ajaxSuccessCode) {
                        var text = '이메일로 발급 받으신 비밀번호를 확인하신 후 로그인 해 주십시오.';
                        text += '개인정보보호를 위하여 로그인 후 비밀번호를 변경해 주십시오.';
                        $('#pop-message').text(text);
                        popup_layer();
                        return;
                    } else {
                        $('#pop-message').text("정보를 다시 확인해주세요.");
                        popup_layer();
                        return;
                    }
                    $('#home').hide();
                    $('#home-result').show();
                }
            });
        });
    });
</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->