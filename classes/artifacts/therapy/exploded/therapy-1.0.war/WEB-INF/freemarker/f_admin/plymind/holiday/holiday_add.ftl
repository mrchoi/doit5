<#assign _page_id="holiday-holiday-add">
<#assign _page_parent_name="플리마인드 관리">
<#assign _page_current_name="캘린더 휴일 등록">
<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}"
canMoveParent=true/>
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

<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false"
                 data-widget-deletebutton="false">
                <@am.widget_title icon="fa-edit">캘린더 휴일 등록 </@am.widget_title>
                <div>
                    <div class="widget-body">
                        <div class="row">
                            <div class="col-md-12">
                                <fieldset>
                                    <legend>휴일을 등록합니다.</legend>

                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label>휴일명</label>
                                                <input id="holiday_title" name="push_contents" class="form-control" placeholder="휴일명을 입력하세요.">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group">
                                                <label>예약날짜</label>
                                                <input id="holiday_date" name="holiday_date" class="form-control" data-holiday-date="" readonly>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>
                                <div class="form-actions">
                                    <div class="btn-group full-right">
                                        <button id="btn_holiday_add" class="btn btn-sm btn-success" type="button">
                                            <i class="fa fa-check"></i> 등록
                                        </button>
                                    </div>
                                    <div class="btn-group pull-left">
                                        <button class="btn btn-sm btn-info btn-prev-page"  type="button">
                                            <i class="fa fa-chevron-left"></i> 목록
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </article>
    </div>
</section>
<section id="widget-grid" class="">
    <div class="row">
        <article class="col-sm-12 col-md-12 col-lg-12">
            <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false" data-widget-deletebutton="false">
            <@am.widget_title icon="fa-table">예약정보변경</@am.widget_title>
                <div>
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
                    </div>
                </div>
            </div>
        </article>
    </div>
</section>
<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    var holidayEntities = [];

    pageSetUp();

    var pagefunction = function() {
        my.fn.showSessionTime(${sessionRestSec});

        $.ajax({
            type: "GET",
            url: '${Request.contextPath}/admin/holiday/holiday_info_list',
            contentType: 'application/json',
            dataType: 'json',
            success: function (data, status, xhr) {
                holidayEntities = data.holidayEntities;
            },
            error: function (xhr, ajaxOptions, thrownError) {
            },
            complete: function (xhr, status) {
                var calendar = new controller();
                calendar.init();
            }
        });

        $('.btn-prev-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/holiday/holiday_list');
        });

        <#-- 게시물 추가 -->
        $('#btn_holiday_add').click(function(e) {
            var holiday_title = $('#holiday_title').val();
            var holiday_date = $('#holiday_date').attr('data-holiday-date');

            if(holiday_title == '') {
                var toastMsg = '휴일명을 입력해 주세요.';
            <@am.jsnoti title="휴일명 확인" content="toastMsg" boxtype="warn" />
                return;
            }

            if(holiday_date == '') {
                var toastMsg = '휴일을 선택해 주세요.';
                <@am.jsnoti title="휴일 확인" content="toastMsg" boxtype="warn" />
                return;
            }

            <#-- ajax 요청 하기 전에 사전 작업 -->
            $('.invalid').hide();
            var $btnAdd = $(this);
            $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 등록중');

            var reqData = {
                member_srl : '0',
                holiday_title : holiday_title,
                holiday_date : holiday_date,
                holiday_time : '00'
            };

            <@am.jsajax_request submituri="${Request.contextPath}/admin/holiday/holiday_add/t/"
            moveuri="#${Request.contextPath}/admin/holiday/holiday_list" errortitle="게시물 등록 실패" ; job >
                <#if job == "success_job">
                    var toastMsg = '휴일을 등록 하였습니다.';
                    <@am.jsnoti title="휴일 등록 성공" content="toastMsg" boxtype="success" />
                    my.fn.removeDatatableStorage('holiday-holiday-list');
                <#elseif job == "common_job">
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 휴일 등록');
                </#if>
            </@am.jsajax_request>
        });
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

        $('#holiday_date').attr('data-holiday-date', selectDate);

        var viewDate = selectDate.substring(0, 4) + "년 " + selectDate.substring(4, 6) + "월 " + selectDate.substring(6, 8) + "일";

        $('#holiday_date').val(viewDate);
    });
    <#-- 달력에서 날짜 선택시 실행하는 스크립트 END -->

    loadScript("${Request.resPath}js/plugin/clockpicker/clockpicker.min.js", function(){
        loadScript("${Request.resPath}js/plugin/moment/moment.min.js", function() {
            loadScript("${Request.resPath}js/plugin/moment/ko.js", function() {
                loadScript("${Request.resPath}js/plugin/jquery-form/jquery-form.min.js", function() {
                    loadScript("${Request.resPath}js/plugin/bootstrap-datetimepicker/bootstrap-datetimepicker.min.js", function () {
                        loadScript("${Request.resPath}js/plugin/summernote/summernote.min.js", pagefunction);
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

                if((today.getTime() < oStartDt.getTime()) && (m_oMonth.getMonth() == oStartDt.getMonth())) {
                    var equalCnt = 0;
                    $.each(holidayEntities, function(index, holidayEntity) {
                        if(tempDate == holidayEntity.holiday_date) {
                            equalCnt ++;
                        }
                    });

                    if(equalCnt > 0) {
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

            that.renderCalendar();
        }

        this.onNextCalendar = function() {
            m_oMonth.setMonth(m_oMonth.getMonth() + 1);

            var tempMonth = "";
            if(m_oMonth.getMonth() < 10) {
                tempMonth = "0" + (m_oMonth.getMonth()+1);
            } else {
                tempMonth = (m_oMonth.getMonth()+1);
            }

            that.renderCalendar();
        }

        this.changeMonth = function() {
            $('#currentDate').text(that.getYearMonth(m_oMonth).substr(0,9));
        }

        this.getYearMonth = function(oDate) {
            return oDate.getFullYear() + '년 ' + (oDate.getMonth() + 1) + '월';
        }
    }

</script>