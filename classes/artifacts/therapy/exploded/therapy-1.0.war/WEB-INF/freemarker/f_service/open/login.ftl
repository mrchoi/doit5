<@ap.py_header_load_script /><#--  -->
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="login">
<#assign _page_parent_name="">
<#assign _page_current_name="Login">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<div class="main-container"><!-- main container -->
    <div class="container">

        <div class="row">
            <div class="col-md-12 tp-title-center"><h1>  PlyMind</h1>
                <!--<p>We're happy to have you back.</p>-->
            </div>

        </div>
        <div class="col-md-offset-3 col-md-6 st-tabs">
            <!-- Nav tabs -->
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab" data-toggle="tab"> Login </a></li>
                <!--
                <li role="presentation"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">상담사 login</a></li>
                -->
            </ul>

            <!-- Tab panes -->
            <div class="tab-content ">
                <div role="tabpanel" class="tab-pane active vendor-login" id="home">


                    <form action="${Request.contextPath}/user/open/authentication" id="login-form" method="post">

                        <input type="hidden" name="redirecthash" value="">

                        <!-- Text input-->
                        <div class="form-group">
                            <label class="control-label" for="email">아이디<span class="required">*</span></label>
                            <input id="user_id" name="user_id" type="text" placeholder="아이디" class="form-control input-md" required>
                        </div>

                        <!-- Text input-->
                        <div class="form-group">
                            <input type="hidden" name="user_password">
                            <label class="control-label" for="password">비밀번호<span class="required">*</span></label>
                            <input name="password" type="password" placeholder="비밀번호" class="form-control input-md" required>
                        </div>

                        <!-- Button -->
                        <div class="form-group">
                            <button id="submit" name="submit" type="submit" class="btn tp-btn-primary tp-btn-lg">Login</button>
                            <a href="${Request.contextPath}/user/open/forget" class="pull-right"> <small>아이디 / 비밀번호 찾기</small></a> </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- /.main container -->

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
<#-- md5 -->
<script src="${Request.resPath}js/ckpush/md5.js"></script>

<script type="text/javascript">
    //runAllForms();

    <#if error?? && error == '1'>
        $('#pop-message').text("아이디가 존재하지 않습니다.");
        popup_layer();
    </#if>

    <#if error?? && error == '2'>
        $('#pop-message').text("비밀번호를 잘못 입력하셨습니다.");
        popup_layer();
    </#if>

    $(function() {
        // Validation
        $("#login-form").validate({
            // Rules for form validation
            rules : {
                user_id : {
                    required : true
                },
                password : {
                    required : true,
                    minlength : 3,
                    maxlength : 20
                }
            },

            // Messages for form validation
            messages : {
                user_id : {
                    required : '아이디를 입력 하세요'
                },
                password : {
                    required : '패스워드를 입력하세요'
                }
            },

            // Do not change code below
            errorPlacement : function(error, element) {
                error.insertAfter(element.parent());
            },

            submitHandler: function(form) {
                // 로그인 후 특정 페이지로 redirection 하기 위한 로직
                var hashTagIndex = (window.location.href).indexOf("#");
                if(hashTagIndex > 0) {
                    var hashTagValue = (window.location.href).substring(hashTagIndex+1);
                    $('input[name=redirecthash]').val($.trim(hashTagValue));
                } else {
                    $('input[name=redirecthash]').val('');
                }

                var hash = CryptoJS.MD5($.trim($('input[name=password]').val()));
                $('input[name=user_password]').val(hash).toString();
                form.submit();
            }
        });
    });
</script>
