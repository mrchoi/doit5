<@ap.py_header_load_script />
<script type="text/javascript" src="${Request.resPath}js/ckpush/md5.js"></script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="product-precheck-add">
<#assign _page_parent_name="">
<#assign _page_current_name="심리평가 기초자료 등록">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12 tp-title-center">
                <h1>심리평가 기초자료 항목을 작성해주세요.</h1>
            </div>
        </div>

        <div class="row">
            <div class="col-md-offset-2 col-md-8 singup-couple">
                <div class="well-box">
                    <h3> 심리평가 기초자료 항목 ( 검사 진행 시 필수입니다. ) </h3>
                    <div class="form-group">
                        <label class="control-label" for="phone">생년월일*</label>
                        <input type="hidden" name="birthday">
                    </div>
                    <div class="form-group col-md-4 no-padding">
                        <label class="control-label" for="year">년</label>
                        <div class="">
                            <select id="year" name="year" class="form-control">
                                <option value="0">선택</option>
                                <#list 1900..2050 as year>
                                    <option value="${year}">${year}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group col-md-4 no-padding">
                        <label class="control-label" for="month">월</label>
                        <div class="">
                            <select id="month" name="month" class="form-control">
                                <option value="0">선택</option>
                                <#list 1..12 as month>
                                    <#if month < 10>
                                        <option value="0${month}">0${month}</option>
                                    <#else>
                                        <option value="${month}">${month}</option>
                                    </#if>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group col-md-4 no-padding">
                        <label class="control-label" for="date">일</label>
                        <div class="">
                            <select id="date" name="date" class="form-control">
                                <option value="0">선택</option>
                                <#list 1..31 as date>
                                    <#if date < 10>
                                        <option value="0${date}">0${date}</option>
                                    <#else>
                                        <option value="${date}">${date}</option>
                                    </#if>
                                </#list>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label" for="job">직업*</label>
                        <input id="job" name="job" type="text" placeholder="직업" class="form-control input-md" required>
                        <label class="control-label">직업은 한글 또는 영문으로 띄어쓰기 없이 2~15자리까지 입력 가능합니다.</label>
                    </div>

                    <div class="form-group">
                        <label class="control-label" for="">결혼유무*</label>
                    </div>
                    <div class="form-group">
                        <label for="">
                            <input type="radio" name="marriage" value="2" class="styled"> 무
                        </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <label for="">
                            <input type="radio" name="marriage" value="1" class="styled"> 유
                        </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="children" name="children" type="text" placeholder="" class="input-md"> 명
                    </div>
                    <div class="form-group">
                        <label class="control-label" for="">자녀가 있을 경우 자녀수를 빈칸에 입력해 주세요.</label>
                    </div>

                    <div class="form-group">
                        <label class="control-label">종교*</label>
                    </div>
                    <div class="form-group">
                        <label for="">
                            <input type="radio" name="religion" value="2" class="styled"> 무
                        </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <label for="">
                            <input type="radio" name="religion" value="1" class="styled"> 유
                        </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="religion_name" name="religion_name" type="text" placeholder="" class="input-md">
                    </div>
                    <div class="form-group">
                        <label class="control-label" for="">종교가 있을 경우 종교명을 빈칸에 입력해 주세요.</label>
                    </div>

                    <div class="form-group">
                        <label class="control-label">장애유무*</label>
                    </div>
                    <div class="form-group">
                        <label for="">
                            <input type="radio" name="disability" value="2" class="styled"> 무
                        </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <label for="">
                            <input type="radio" name="disability" value="1" class="styled"> 유
                        </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        유형
                        <input id="disability_type" name="disability_type" type="text" placeholder="" class="input-md">
                        등급
                        <input id="disability_rate" name="disability_rate" type="text" placeholder="" class="input-md">
                    </div>
                    <div class="form-group">
                        <label class="control-label" for="">장애가 있을 경우 장애유형 및 등급을 빈칸에 입력해 주세요.</label>
                    </div>

                    <div class="form-group">
                        <label class="control-label" for="">학력*</label>
                    </div>
                    <div class="form-group">
                        <div class="">
                            <select id="academic" name="academic" class="form-control">
                                <option value="0">선택</option>
                                <option value="1">대학원 재학</option>
                                <option value="2">대학원졸</option>
                                <option value="3">대학교 재학</option>
                                <option value="4">대학교졸</option>
                                <option value="5">고등학교 재학</option>
                                <option value="6">고등학교졸</option>
                                <option value="7">중학교 재학</option>
                                <option value="8">중학교졸</option>
                                <option value="9">초등학교 재학</option>
                                <option value="10">초등학교졸</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label" for="">성별*</label>
                    </div>
                    <div class="form-group">
                        <label for="">
                            <input type="radio" name="gender" value="1" class="styled"> 남
                        </label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <label for="">
                            <input type="radio" name="gender" value="2" class="styled"> 여
                        </label>
                    </div>

                    <div class="form-group">
                        <button id="btn-signup-optional" class="btn tp-btn-primary tp-btn-lg">저장</button>
                    </div>

                </div>
            </div>
        </div>

    </div>
</div>
<!-- /.main container -->

<script type="text/javascript">

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
                    } else {
                        $('#pop-message').text('사용가능한 아이디 입니다.');
                        popup_layer();
                        $('#user_id_check').val('0');
                        return;
                    }
                }
            }
        });
    }

    $('#member_gubun1').click(function(e){
        $('#organization_search').hide();
        return;
    });

    $('#member_gubun2').click(function(e){
        $('#organization_search').show();
        return;
    });

    $('#btn-idcheck').click(function(e){

        if ($('#user_id').val() == '') {
            $('#pop-message').text("아이디는 필수 입력값입니다.");
            popup_layer();
            return;
        }

        var reqData = {
            user_id: $.trim($('input[name=user_id]').val())
        }

        ajax_request(reqData, '${Request.contextPath}/signup/member/idCheck?user_id=' + reqData.user_id);
    });

    $('#btn-nickname-check').click(function(e){

        if ($('#user_id').val() == '') {
            $('#pop-message').text("닉네임은 필수 입력값입니다.");
            popup_layer();
            return;
        }

        var reqData = {
            user_id: $.trim($('input[name=user_id]').val())
        }

        ajax_request(reqData, '${Request.contextPath}/signup/member/idCheck?user_id=' + reqData.user_id);
    });

    <#-- 회원 가입 중 옵션 저장을 진행한다. -->
    $('#btn-signup-optional').click(function(e){
        if($('select[name=year]').val() == '0' || $('select[name=month]').val() == '0' || $('select[name=date]').val() == '0') {
            $('#pop-message').text("생년월일을 선택해 주세요.");
            popup_layer();
            return;
        }

        if($.trim($('input[name=job]').val()) == '') {
            $('#pop-message').text("직업을 입력해 주세요.");
            popup_layer();
            return;
        }

        var marriage = 0;
        $('input:radio[name="marriage"]:checked').each(function() {
            marriage = this.value;
        });
        if(marriage == 0) {
            $('#pop-message').text("결혼유무를 선택해 주세요.");
            popup_layer();
            return;
        }

        var religion = 0;
        $('input:radio[name="religion"]:checked').each(function() {
            religion = this.value;
        });
        if(religion == 0) {
            $('#pop-message').text("종교를 선택해 주세요.");
            popup_layer();
            return;
        }

        if(religion == 1 && $('input[name=religion_name]').val() == '') {
            $('#pop-message').text("종교를 입력해 주세요.");
            popup_layer();
            return;
        }

        var disability = 0;
        $('input:radio[name="disability"]:checked').each(function() {
            disability = this.value;
        });
        if(disability == 0) {
            $('#pop-message').text("장애유무를 선택해 주세요.");
            popup_layer();
            return;
        }

        if(disability == 1 && $('input[name=disability_type]').val() == '') {
            $('#pop-message').text("장애 유형을 입력해 주세요.");
            popup_layer();
            return;
        }

        if(disability == 1 && $('input[name=disability_rate]').val() == '') {
            $('#pop-message').text("장애 등급을 입력해 주세요.");
            popup_layer();
            return;
        }

        if($('select[name="academic"]').val() == '0') {
            $('#pop-message').text("학력을 선택해 주세요.");
            popup_layer();
            return;
        }

        var gender = 0;
        $('input:radio[name="gender"]:checked').each(function() {
            gender = this.value;
        });
        if(gender == 0) {
            $('#pop-message').text("성별을 선택해 주세요.");
            popup_layer();
            return;
        }

        var reqData = {
            member_srl:         ${member_srl},
            birthday:           $('select[name=year]').val() + $('select[name=month]').val() + $('select[name=date]').val() ,
            job:                $.trim($('input[name=job]').val()),
            marriage:           marriage,
            children:           $.trim($('input[name=children]').val()),
            religion:           religion,
            religion_name:      $('input[name=religion_name]').val(),
            disability:         disability,
            disability_type:    $('input[name=disability_type]').val(),
            disability_rate:    $('input[name=disability_rate]').val(),
            academic_srl:       $('select[name="academic"]').val(),
            gender:             gender
        };

        $.ajax({
            type: "POST",
            url: '${Request.contextPath}/signup/member/modify/extra/t/' + $.now(),
            contentType : 'application/json',
            data: JSON.stringify(reqData),
            success: function (res) {
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $('#pop-message').text("심리평가 기초자료 등록 실패하였습니다.");
                popup_layer();
                return;
            },
            complete: function(xhr, status) {
                if (xhr && xhr.responseJSON && xhr.responseJSON.ck_error == my.data.ajaxSuccessCode) {
                    $('#pop-message').text("심리평가 기초자료 등록 정상적으로 이루어졌습니다.");
                    popup_layer();

                    if(${pretestingBool?string}) {
                        timer = setInterval(function() {
                            history.go(-1);
                            return;
                        }, 1500);
                    } else {
                        timer = setInterval(function() {
                            my.fn.pmove('${Request.contextPath}/user/pretesting/addForm');
                            return;
                        }, 1500);
                    }
                } else {
                    console.log(xhr);
                    $('#pop-message').text("심리평가 기초자료 등록 실패하였습니다.");
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
