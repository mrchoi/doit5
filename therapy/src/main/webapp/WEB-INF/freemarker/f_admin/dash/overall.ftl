<#assign _page_id="overall-dashboard">
<#assign _page_parent_name="">
<#assign _page_current_name="알림판">

<@am.page_simple_depth icon="fa-home" parent="${_page_parent_name}" current="${_page_current_name}" />

<div class="row rasui-system-usage-graph rasui-push-message-status-graph">
    <#-- system usage sample
    <div class="col-sm-4">
        <div class="well">
            <div><strong>실시간 서버 상태</strong></div>
            <div class="row show-stats">
                <div class="col-xs-6 col-sm-6 col-md-12 col-lg-12">
                    <span class="text"> CPU <span class="pull-right">30%</span> </span>
                    <div class="progress">
                        <div class="progress-bar bg-color-blueDark" style="width: 65%;"></div>
                    </div>
                </div>

                <div class="col-xs-6 col-sm-6 col-md-12 col-lg-12">
                    <span class="text"> Memory <span class="pull-right">440 GB</span> </span>
                    <div class="progress">
                        <div class="progress-bar bg-color-blue" style="width: 34%;"></div>
                    </div>
                </div>

                <div class="col-xs-6 col-sm-6 col-md-12 col-lg-12">
                    <span class="text"> Load <span class="pull-right">77%</span> </span>
                    <div class="progress">
                        <div class="progress-bar bg-color-blue" style="width: 77%;"></div>
                    </div>
                </div>
            </div>

        </div>
    </div>
    -->
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        <#-- 실시간 서버에서 받아둔 시스템 상태 데이터가 존재하면 업데이트 한다. -->
        var systemUsageData = _ckras.model.getByUITag(my.ras.config.uitag_system_status);
        if (systemUsageData.length > 0) {
            var cpuPct = 0, memoryPct = 0, memoryUsage = 0, serverLoad = 0, loadPct = 0, maxLoad = 10;

            for (var i = 0; i < systemUsageData.length; i++) {
                try { cpuPct = parseFloat(systemUsageData[i].cpu); }
                catch(err) {
                    console.log('invalid system usage cpu. not number. cpu [' + systemUsageData[i].cpu + ']');
                }
                cpuPct = Math.round(cpuPct);

                try { memoryPct = parseFloat(systemUsageData[i].mem_usage); }
                catch(err) {
                    console.log('invalid system usage memory usage. not number. memory usage [' + systemUsageData[i].mem_usage + ']');
                }
                memoryPct = Math.round(systemUsageData[i].mem_usage);
                memoryUsage = my.fn.toNumber(systemUsageData[i].mem_used) / 1000;

                try { serverLoad = parseFloat(systemUsageData[i].loadavg); }
                catch(err) {
                    console.log('invalid system usage load. not number. load [' + systemUsageData[i].loadavg + ']');
                }
                loadPct = serverLoad / maxLoad * 100;

                _ckras.ui.drawLiveProgress({
                    parentDomClass: 'rasui-system-usage-graph',
                    id: systemUsageData[i]._ui_id,
                    text: '실시간 서버 상태',
                    progress: [
                        {left: 'CPU', right: cpuPct+'%', percentage: cpuPct},
                        {left: 'Memory', right: memoryPct + '% ('+memoryUsage.numberFormat(0, '.', ',')+'M)', percentage: memoryPct},
                        {left: 'Load', right: Math.round(loadPct)+'% ('+serverLoad+')', percentage: loadPct},
                    ]
                });
            }
        }

        <#-- 실시간 서버에서 받아둔 Push 메시지 데이터가 존재하면 그린다. -->
        var pushMessageStatusData = _ckras.model.getByUITag(my.ras.config.uitag_push_message);
        var currTotal = 0, allTotal = 0, realTotal = 0, succTotal = 0, confirmTotal = 0,
                failTotal = 0, realFailTotal = 0, recvTotal = 0,
                allPct = 0, succPct = 0, failPct = 0, confirmPct = 0, sendDate = '';

        pushMessageStatusData.sort(function(a, b){
            return my.fn.toNumber(b.c_date) - my.fn.toNumber(a.c_date);
        });

        for(var i=0 ; i<pushMessageStatusData.length ; i++) {
            succTotal = my.fn.toNumber(pushMessageStatusData[i].success_count);
            realTotal = my.fn.toNumber(pushMessageStatusData[i].total_real_count);
            currTotal = succTotal + my.fn.toNumber(pushMessageStatusData[i].fail_count);
            allTotal = my.fn.toNumber(pushMessageStatusData[i].total_count);
            confirmTotal = my.fn.toNumber(pushMessageStatusData[i].confirm_count);
            failTotal = my.fn.toNumber(pushMessageStatusData[i].fail_count);
            realFailTotal = my.fn.toNumber(pushMessageStatusData[i].fail_real_count);
            recvTotal = my.fn.toNumber(pushMessageStatusData[i].received_count);
            sendDate = new Date(my.fn.toNumber(pushMessageStatusData[i].c_date) * 1000).format('yyyy-MM-dd HH:mm:ss');

            allPct = Math.round(currTotal / allTotal * 100);
            succPct = Math.round(succTotal / realTotal * 100);
            failPct = 100 - succPct;
            confirmPct = Math.round(confirmTotal / realTotal * 100);

            _ckras.ui.draw4PieChart({
                parentDomClass: 'rasui-push-message-status-graph',
                id: pushMessageStatusData[i].tid,
                text: ['- <strong>메시지</strong> : ' + pushMessageStatusData[i].push_title,
                    '- <strong>전송량</strong> : ' + currTotal + ' / ' + allTotal,
                    '- <strong>발송일</strong> : ' + sendDate],
                chart: [
                    {name: '전체', percentage: allPct, color: '#57889c'},
                    {name: '성공', percentage: succPct, color: '#356e35'},
                    {name: '실패', percentage: failPct, color: '#a90329'},
                    {name: '확인', percentage: confirmPct, color: '#71843f'}
                ],
                canClose: (pushMessageStatusData[i].status && pushMessageStatusData[i].status == 'end' ? true : false)
            });
        }


    };

    pagefunction();
</script>
