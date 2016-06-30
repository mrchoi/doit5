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
<#assign _page_id="user-advice-application">
<#assign _page_parent_name="나의 예약/결제 정보">
<#assign _page_current_name="예약 변경">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->
<@ap.py_popup_layer />
<style>
    #calendar table {
        border-collapse: collapse;
        text-align: center;
    }
    #calendar table thead td {
        height: 30px;
        font-weight: bold;
    }
    #calendar table td {
        border: solid 1px #ddd;
    }
    #calendar table td.date-cell {
        height: 40px;
    }
    #calendar table td.sun {
        color: red;
    }
    #calendar table td.sat {
        color: blue;
    }
    #calendar table td.not-this-month {
        background: #ddd;
        color: #999;
    }
    #calendar table td.not-select {
        background: red;
        color: #ddd;
    }
    #calendar table td.possible {
        cursor: pointer;
    }
    .impossible {
        background: #ff99cc;
    }

</style>

<div class="main-container">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <blockquote>심리상담 예약 변경</blockquote>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <hr>
                <div class="row">
                    <div class="col-md-3">상담유형</div>
                    <div class="col-md-9">
                        ${appointmentEntity.title}
                        <#if appointmentEntity.advice_type == 1> 으뜸 </#if>
                        <#if appointmentEntity.advice_type == 2> 가장 </#if>
                        <#if appointmentEntity.advice_type == 3> 한마루 </#if>
                        ( ${appointmentEntity.advice_number} 회 )
                        - ${appointmentEntity.application_number} 회차
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-md-3">상담사</div>
                    <div class="col-md-9">
                    ${appointmentEntity.advisor_name}
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-md-3">상담 일시</div>
                    <div class="col-md-9">
                        ${appointmentEntity.appointment_date?substring(0,4)}년
                        ${appointmentEntity.appointment_date?substring(4,6)}월
                        ${appointmentEntity.appointment_date?substring(6,8)}일
                        ${appointmentEntity.appointment_time}시 00분
                    </div>
                </div>
                <hr>
                <div class="row" style="color: red;">
                    <div class="col-md-3">상담 변경 날짜</div>
                    <div class="col-md-9"><span id="view_advice_date" ts-data-date=""></span></div>
                </div>
                <hr>
                <div class="row" style="color: red;">
                    <div class="col-md-3">상담 변경 시간</div>
                    <div class="col-md-9"><span id="view_advice_time" ts-data-time=""></span></div>
                </div>
                <hr>
                ${appointmentEntity.advisor_name} 상담사 상담 가능한 날짜
                <div class="row" >
                    <div class="col-md-12">
                        <div class="row inline-group">
                            <div class="col-md-4"></div>
                            <div class="col-md-1 tp-title-center"><button id='btnPrev'><</button></div>
                            <div class="col-md-2 tp-title-center"><span id='currentDate'></span></div>
                            <div class="col-md-1 tp-title-center"><button id='btnNext'>></button></div>
                            <div class="col-md-4"></div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div style="display: inline-block; border: solid 1px #000; background-color: azure; width: 12px; height: 12px;"></div>
                                <div style="display: inline-block;">선택 가능 날짜</div>&nbsp;&nbsp;&nbsp;
                                <div style="display: inline-block; border: solid 1px #000; background-color: #00aeaf; width: 12px; height: 12px;"></div>
                                <div style="display: inline-block;">선택 날짜</div>&nbsp;&nbsp;&nbsp;
                                <div style="display: inline-block; border: solid 1px #000; background-color: #ff99cc; width: 12px; height: 12px;"></div>
                                <div style="display: inline-block;">선택 불가 날짜</div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div id="calendar"></div>
                            </div>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-md-1">
                        <div id="ts_time_9" data-time="9" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 09:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_10" data-time="10" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 10:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_11" data-time="11" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 11:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_12" data-time="12" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 12:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_14" data-time="14" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 14:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_15" data-time="15" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 15:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_16" data-time="16" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 16:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_17" data-time="17" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 17:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_18" data-time="18" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 18:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_19" data-time="19" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 19:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_20" data-time="20" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 20:00 </div>
                    </div>
                    <div class="col-md-1">
                        <div id="ts_time_21" data-time="21" class="appointment-time tp-title-center" style="border: solid 1px #ddd; cursor: pointer;"> 21:00 </div>
                    </div>
                </div>
                <hr>
            </div>
            <div class="row">
                <div class="col-md-12 tp-title-center">
                    <button id="btn_text_skype_therapy" name="btn_text_skype_therapy" class="btn tp-btn-default tp-btn-lg">예약 변경</button>
                    <button id="btn_list" name="btn_list"  class="btn tp-btn-default tp-btn-lg">목록</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var appointmentEntites = [];
    var contentsSrl = "1";

    $(document).ready(function() {
        var globleToday = new Date();
        var currentMonth = globleToday.getMonth() + 1;
        if (currentMonth < 10) {
            currentMonth = "0" + currentMonth;
        }

        $.ajax({
            type: "GET",
            url: '${Request.contextPath}/user/appointment/check/' + ${appointmentEntity.advisor_srl} + '/' + (globleToday.getFullYear() + "" + currentMonth),
            contentType: 'application/json',
            //data: JSON.stringify(dataString),
            dataType: 'json',
            success: function (data, status, xhr) {
                appointmentEntites = data.appointmentEntites;
            },
            error: function (xhr, ajaxOptions, thrownError) {
            },
            complete: function (xhr, status) {
                var calendar = new controller();
                calendar.init();
            }
        });

        <#-- 텍스트케어세라피 / 스카이프케어세라피에서 실행하는 스크립트 START -->
        $(".ts-advice-type").click(function () {
            text_skype_therapy();
        });

        $("#btn_text_skype_therapy").click(function () {
            var appointment_date = $('#view_advice_date').attr('ts-data-date');
            var appointment_time = $('#view_advice_time').attr('ts-data-time');

            if(appointment_date == '') {
                $('#pop-message').text("상담 변경 날짜를 선택해 주세요.");
                popup_layer();
                return;
            }

            if(appointment_time == '') {
                $('#pop-message').text("상담 변경 시간을 선택해 주세요.");
                popup_layer();
                return;
            }

            var dataString = {
                "appointment_srl": ${appointmentEntity.appointment_srl},
                "appointment_date": appointment_date,
                "appointment_time": appointment_time
            };

            $.ajax({
                type: "POST",
                url: '${Request.contextPath}/user/appointment/change',
                contentType: 'application/json',
                data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    $('#pop-message').text("상담 예약일시가 정상적으로 변경 되었습니다.");
                    popup_layer();

                    timer = setInterval(function() {
                        my.fn.pmove('${Request.contextPath}/user/appointment/appointment_list');
                    }, 1500);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    $('#pop-message').text("상담 예약일시 변경 실패했습니다. 다시 시도해 주세요.");
                    popup_layer();
                    return;
                }
            });
        });

        $("#btn_list").click(function(e) {
            <@ap.py_jsevent_prevent />
            my.fn.pmove('${Request.contextPath}/user/appointment/appointment_list');
        });
    });
    <#-- 텍스트케어세라피 / 스카이프케어세라피에서 실행하는 스크립트 END -->

    <#-- 달력에서 날짜 선택시 실행하는 스크립트 START -->
    $(document).on("click", '.possible', function() {
        var $selectThis = $(this);

        $('.possible').each(function(index, value) {
            if($selectThis.attr('data-current') == $(this).attr('data-current')) {
                $(this).css("background", "#00aeaf");
            } else {
                $(this).css("background", "");
            }
        });

        var selectDate = $selectThis.attr('data-current');

        var arrTime = new Array('9', '10', '11', '12', '14', '15', '16', '17', '18', '19', '20', '21');
        for(var i=0; i<arrTime.length; i++) {
            $.each(appointmentEntites, function (index, appointmentEntity) {
                if (selectDate == appointmentEntity.appointment_date && arrTime[i] == appointmentEntity.appointment_time) {
                    $('#ts_time_' + arrTime[i]).removeClass("ts-time-select");
                    $('#ts_time_' + arrTime[i]).css("background", "#ff99cc");
                    return false;
                } else {
                    $('#ts_time_' + arrTime[i]).addClass("ts-time-select");
                    $('#ts_time_' + arrTime[i]).css("background", "");
                }
            });
        }

        $('#view_advice_date').attr('ts-data-date', selectDate);

        var viewDate = selectDate.substring(0, 4) + "년 " + selectDate.substring(4, 6) + "월 " + selectDate.substring(6, 8) + "일";

        $('#view_advice_date')[0].innerHTML = viewDate;
    });
    <#-- 달력에서 날짜 선택시 실행하는 스크립트 END -->

    <#-- 예약 시간 선택시 실행하는 스크립트 START -->
    $(document).on("click", '.ts-time-select', function() {
        var $selectThis = $(this);

        $('.ts-time-select').each(function(index, value) {
            if($selectThis.attr("data-time") == $(this).attr("data-time")) {
                $(this).css("background", "#00aeaf");
            } else {
                $(this).css("background", "");
            }
        })

        $('#view_advice_time').attr('ts-data-time', $selectThis.attr("data-time"));

        $('#view_advice_time')[0].innerHTML = $selectThis.attr("data-time") + "시 00분";
    });
    <#-- 예약 시간 선택시 실행하는 스크립트 END -->

    function controller(target) {

        var that = this;
        var m_oMonth = new Date();
        m_oMonth.setDate(1);

        this.init = function() {
            that.renderCalendar();
            that.initEvent();
        }

        this.renderCalendar = function() {
            $('.appointment-time').addClass("ts-time-select");
            $('.appointment-time').css("background", "");

            var arrTable = [];

            arrTable.push('<table>');
            arrTable.push('<colgroup>');

            for(var i=0; i<7; i++) {
                arrTable.push('<col width="200">');
            }
            arrTable.push('</colgroup>');
            arrTable.push('<thead>');
            arrTable.push('<tr>');

            var arrWeek = "일월화수목금토".split("");

            for(var i=0, len=arrWeek.length; i<len; i++) {
                var sClass = '';
                sClass += i % 7 == 0 ? 'sun' : '';
                sClass += i % 7 == 6 ? 'sat' : '';
                arrTable.push('<td class="'+sClass+'">' + arrWeek[i] + '요일</td>');
            }
            arrTable.push('</tr></thead>');
            arrTable.push('<tbody>');

            var oStartDt = new Date(m_oMonth.getTime());
            oStartDt.setDate(oStartDt.getDate() - oStartDt.getDay());

            var tempMonth = "";
            if(m_oMonth.getMonth() < 10) {
                tempMonth = "0" + (m_oMonth.getMonth()+1);
            } else {
                tempMonth = (m_oMonth.getMonth()+1);
            }

            var today = new Date();
            today.setDate(today.getDate() -1 );

            for(var i=0; i<100; i++) {
                if(i % 7 == 0) {
                    arrTable.push('<tr>');
                }

                var tempDay = "";
                if(oStartDt.getDate() < 10) {
                    tempDay = "0" + oStartDt.getDate();
                } else {
                    tempDay = oStartDt.getDate();
                }

                var tempDate = m_oMonth.getFullYear() + tempMonth + tempDay;

                var sClass = 'date-cell '
                sClass += m_oMonth.getMonth() != oStartDt.getMonth() ? 'not-this-month ' : '';
                sClass += i % 7 == 0 ? 'sun ' : '';
                sClass += i % 7 == 6 ? 'sat ' : '';

                var todayTime = today.getTime() + ( 60 * 60 * 24 * 1000 );

                if((todayTime < oStartDt.getTime()) && (m_oMonth.getMonth() == oStartDt.getMonth())) {
                    var equalCnt = 0;
                    $.each(appointmentEntites, function(index, appointmentEntity) {
                        if(tempDate == appointmentEntity.appointment_date) {
                            equalCnt++;
                        }
                    });

                    if(equalCnt == 12) {
                        sClass += 'impossible ';
                    } else {
                        sClass += 'possible ';
                    }
                } else {
                    sClass += 'impossible ';
                }

                arrTable.push('<td class="'+sClass+'" data-current="' + tempDate + '">' + oStartDt.getDate() + '</td>');

                oStartDt.setDate(oStartDt.getDate() + 1);

                if(i % 7 == 6) {
                    arrTable.push('</tr>');

                    if(m_oMonth.getMonth() != oStartDt.getMonth()) {
                        break;
                    }
                }
            }
            arrTable.push('</tbody></table>');

            $('#calendar').html(arrTable.join(""));

            that.changeMonth();
        }

        this.initEvent = function() {
            $('#btnPrev').click(that.onPrevCalendar);
            $('#btnNext').click(that.onNextCalendar);
        }

        this.onPrevCalendar = function() {
            m_oMonth.setMonth(m_oMonth.getMonth() - 1);

            var tempMonth = "";
            if(m_oMonth.getMonth() < 10) {
                tempMonth = "0" + (m_oMonth.getMonth()+1);
            } else {
                tempMonth = (m_oMonth.getMonth()+1);
            }

            $.ajax({
                type: "GET",
                url: '${Request.contextPath}/user/appointment/check/' + ${appointmentEntity.advisor_srl} + '/' + (m_oMonth.getFullYear() + "" + tempMonth),
                contentType : 'application/json',
                //data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    appointmentEntites = data.appointmentEntites;
                },
                error: function (xhr, ajaxOptions, thrownError) {},
                complete: function(xhr, status) {
                    that.renderCalendar();
                }
            });
        }

        this.onNextCalendar = function() {
            m_oMonth.setMonth(m_oMonth.getMonth() + 1);

            var tempMonth = "";
            if(m_oMonth.getMonth() < 10) {
                tempMonth = "0" + (m_oMonth.getMonth()+1);
            } else {
                tempMonth = (m_oMonth.getMonth()+1);
            }

            $.ajax({
                type: "GET",
                url: '${Request.contextPath}/user/appointment/check/' + ${appointmentEntity.advisor_srl} + '/' + (m_oMonth.getFullYear() + "" + tempMonth),
                contentType : 'application/json',
                //data: JSON.stringify(dataString),
                dataType: 'json',
                success: function (data, status, xhr) {
                    appointmentEntites = data.appointmentEntites;
                },
                error: function (xhr, ajaxOptions, thrownError) {},
                complete: function(xhr, status) {
                    that.renderCalendar();
                }
            });
        }

        this.changeMonth = function() {
            $('#currentDate').text(that.getYearMonth(m_oMonth).substr(0,9));
        }

        this.getYearMonth = function(oDate) {
            return oDate.getFullYear() + '년 ' + (oDate.getMonth() + 1) + '월';
        }
    }

</script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->