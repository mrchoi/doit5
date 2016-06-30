<#assign _page_id="appointment-appointment-add">
<#assign _page_parent_name="예약관리">
<#assign _page_current_name="예약등록">

<@am.page_simple_depth icon="fa-puzzle-piece" parent="${_page_parent_name}" current="${_page_current_name}" canMoveParent=true />
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
<#-- widget grid -->
<section id="widget-grid" class="">
    <#-- row -->
    <div class="row">
        <#-- NEW WIDGET START -->
        <article class="col-sm-12 col-md-12 col-lg-12">
            <#-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">예약등록</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <table id="user" class="table table-bordered table-striped" style="clear: both">
                            <tbody>
                                <tr>
                                    <td style="width:30%;"><strong>구분</strong></td>
                                    <td style="width:70%">
                                        <#if applicationEntity.kind == 1>심리상담</#if>
                                        <#if applicationEntity.kind == 2>심리검사</#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>신청내역</strong></td>
                                    <td>
                                        ${applicationEntity.title}
                                        <#if applicationEntity.kind == 1>
                                            <#if applicationEntity.advice_type == 1> - 으뜸 ${applicationEntity.advice_number} 회</#if>
                                            <#if applicationEntity.advice_type == 2> - 가장 ${applicationEntity.advice_number} 회</#if>
                                            <#if applicationEntity.advice_type == 3> - 한마루 ${applicationEntity.advice_number} 회</#if>
                                        </#if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>상담회차</strong></td>
                                    <td>
                                    ${applicationEntity.application_number} 회
                                    </td>
                                </tr>
                                <#if applicationEntity.advisor_name?? && applicationEntity.advisor_name != ''>
                                <tr>
                                    <td><strong>상담사</strong></td>
                                    <td>${applicationEntity.advisor_name}</td>
                                </tr>
                                </#if>
                                <tr>
                                    <td><strong>신청자</strong></td>
                                    <td>${applicationEntity.user_name}</td>
                                </tr>
                                <tr>
                                    <td><strong>예약날짜</strong></td>
                                    <td>
                                        <span id="view_advice_date" ts-data-date=""></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>예약시간</strong></td>
                                    <td>
                                        <span id="view_advice_time" ts-data-time=""></span>
                                    </td>
                                </tr>
                            </tbody>
                        </table>

                        <form>
                            <div class="form-actions">
                                <div class="btn-group pull-left">
                                    <button id="btn-appointment-page" class="btn btn-sm btn-info" type="button">
                                        <i class="fa fa-chevron-left"></i> 목록
                                    </button>
                                </div>
                                <div class="btn-group">
                                    <button id="btn-appointment-process" class="btn btn-sm btn-danger" type="button">
                                        <i class="fa fa-chevron-left"></i> 등록
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                <#-- end widget content -->
                </div>
            <#-- end widget div -->
            </div>
        <#-- end widget -->
        </article>
    <#-- WIDGET END -->
    </div>
</section>
<#-- end row -->
<section id="widget-grid" class="">
    <section id="widget-grid" class="">
    <#-- row -->
        <div class="row">
        <#-- NEW WIDGET START -->
            <article class="col-sm-12 col-md-12 col-lg-12">
            <#-- Widget ID (each widget will need unique ID)-->
                <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">예약정보변경</@am.widget_title>

                <#-- widget div-->
                    <div>
                    <#-- widget edit box -->
                        <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                        <div class="widget-body">
                            <div class="row form-group">
                                <div class="col-sm-4 col-md-4 col-lg-4"></div>
                                <div class="col-sm-1 col-md-1 col-lg-1 text-center"><button id='btnPrev'><</button></div>
                                <div class="col-sm-2 col-md-2 col-lg-2 text-center"><span id='currentDate'></span></div>
                                <div class="col-sm-1 col-md-1 col-lg-1 text-center"><button id='btnNext'>></button></div>
                                <div class="col-sm-4 col-md-4 col-lg-4"></div>
                            </div>
                            <div class="row form-group">
                                <div class="col-sm-12 col-md-12 col-lg-12">
                                    <div style="display: inline-block; border: solid 1px #000; background-color: azure; width: 12px; height: 12px;"></div>
                                    <div style="display: inline-block;">선택 가능 날짜</div>&nbsp;&nbsp;&nbsp;
                                    <div style="display: inline-block; border: solid 1px #000; background-color: #00aeaf; width: 12px; height: 12px;"></div>
                                    <div style="display: inline-block;">선택 날짜</div>&nbsp;&nbsp;&nbsp;
                                    <div style="display: inline-block; border: solid 1px #000; background-color: #ff99cc; width: 12px; height: 12px;"></div>
                                    <div style="display: inline-block;">선택 불가 날짜</div>
                                </div>
                            </div>
                            <div class="row form-group">
                                <div id="calendar" class="col-sm-12 col-md-12 col-lg-12"></div>
                            </div>
                            <hr>
                            <div class="row">
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_9" data-time="9" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 09:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_10" data-time="10" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 10:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_11" data-time="11" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 11:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_12" data-time="12" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 12:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_14" data-time="14" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 14:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_15" data-time="15" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 15:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_16" data-time="16" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 16:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_17" data-time="17" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 17:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_18" data-time="18" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 18:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_19" data-time="19" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 19:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_20" data-time="20" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 20:00 </div>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <div id="ts_time_21" data-time="21" class="appointment-time text-center" style="border: solid 1px #ddd; cursor: pointer;"> 21:00 </div>
                                </div>
                            </div>
                        </div>
                    <#-- end widget content -->
                    </div>
                <#-- end widget div -->
                </div>
            <#-- end widget -->
            </article>
        <#-- WIDGET END -->
        </div>
    </section>
<#-- end widget grid -->

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    var appointmentEntites = [];

    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        var globleToday = new Date();
        var currentMonth = globleToday.getMonth() + 1;
        if (currentMonth < 10) {
            currentMonth = "0" + currentMonth;
        }

        $.ajax({
            type: "GET",
            url: '${Request.contextPath}/admin/plymind/appointment/check/' + ${applicationEntity.advisor_srl} + '/' + (globleToday.getFullYear() + "" + currentMonth),
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

        $("#btn-appointment-process").click(function () {
            var appointment_date = $('#view_advice_date').attr('ts-data-date');
            var appointment_time = $('#view_advice_time').attr('ts-data-time');

            if(appointment_date == '') {
                var toastMsg = '상담 날짜를 선택해 주세요.';
                <@am.jsnoti title="날짜 확인" content="toastMsg" boxtype="warn" />
                return;
            }

            if(appointment_time == '') {
                var toastMsg = '상담 시간을 선택해 주세요.';
                <@am.jsnoti title="시간 확인" content="toastMsg" boxtype="warn" />
                return;
            }

            var reqData = {
                "member_srl": ${applicationEntity.member_srl},
                "advisor_srl": ${applicationEntity.advisor_srl},
                "application_srl": ${applicationEntity.application_srl},
                "appointment_date": appointment_date,
                "appointment_time": appointment_time,
                "appointment_status": 1
            };

            <@am.jsajax_request submituri="${Request.contextPath}/admin/plymind/appointment/appointment_add/t/"
            moveuri="#${Request.contextPath}/admin/plymind/appointment/appointment_application_list/${applicationEntity.application_group}" errortitle="예약 등록 실패" ; job >
                <#if job == "success_job">
                    var toastMsg = '예약 등록이 정상적으로 이루어졌습니다.';
                    <@am.jsnoti title="예약 등록 성공" content="toastMsg" boxtype="success" />
                <#elseif job == "common_job">
                    my.fn.removeDatatableStorage('payment-payment-list');
                </#if>
            </@am.jsajax_request>
        });

        <#-- 결제내역 리스트로 이동 -->
        $('#btn-appointment-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/appointment/appointment_list');
        });

        <#-- 취소내역 리스트로 이동 -->
        $('#btn-cancel-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/plymind/payment/cancel_list');
        });

        var calendar = new controller();
        calendar.init();
    };

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

    loadScript("${Request.resPath}js/plugin/x-editable/moment.min.js", function() {
        loadScript("${Request.resPath}js/plugin/x-editable/jquery.mockjax.min.js", function() {
            loadScript("${Request.resPath}js/plugin/x-editable/x-editable.min.js", function() {
                loadScript("${Request.resPath}js/plugin/typeahead/typeahead.min.js", function() {
                    loadScript("${Request.resPath}js/plugin/typeahead/typeaheadjs.min.js", function() {
                        loadScript("${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js", function () {
                            loadScript("${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js", function () {
                                loadScript("${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js", function () {
                                    loadScript("${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js", function () {
                                        loadScript("${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js", function () {
                                            loadScript("${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js", function () {
                                                loadScript("${Request.resPath}js/ckpush/md5.js", pagefunction);
                                            })
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    });

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

            arrTable.push('<table class="table table-bordered table-striped" style="clear: both">');
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
                url: '${Request.contextPath}/admin/plymind/appointment/check/' + ${applicationEntity.advisor_srl} + '/' + (m_oMonth.getFullYear() + "" + tempMonth),
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
                url: '${Request.contextPath}/admin/plymind/appointment/check/' + ${applicationEntity.advisor_srl} + '/' + (m_oMonth.getFullYear() + "" + tempMonth),
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