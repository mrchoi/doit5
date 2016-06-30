<#assign _page_id="push-gcm-apns-form">
<#assign _page_parent_name="즉시 발송">
<#assign _page_current_name="메시지 작성">

<@am.page_simple_depth icon="fa-bullhorn" parent="${_page_parent_name}" current="${_page_current_name}"
        canMoveParent=true />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    Push 메시지를 발송 하는 페이지 입니다.
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="메시지 작성 위젯"><strong>메시지 발송</strong></span>,
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="메시지 발송 이력 위젯"><strong>메시지 발송 이력</strong></span>,
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="앱에 등록된 전체 단말 위젯"><strong>등록 단말 전체 목록</strong></span>,
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="Push ID 등록된 단말 위젯"><strong>Push ID등록 단말 목록</strong></span> 을 확인 할 수 있습니다.
</p>
<p>
    Push 메시지 발송을 위해서는 <strong>메시지 작성</strong> 위젯에 발송 할 메시지 항목을 넣고 <strong>메시지 발송</strong> 버튼을 클릭하면 됩니다. Push 메시지에서 이미지를 사용 할 때는
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="파일 선택시 자동 업로드"><strong>업로드 버튼</strong></span>을 클릭하여 신규 이미지를
    업로드, 또는 <strong>이미지 주소</strong>에 외부 이미지 주소를 붙여 넣기 하면 됩니다.
    <strong>메시지 발송 대상</strong>은 현재 앱에 Push ID 등록된 전체 단말과 개별 대상이 지원 됩니다. 개별 대상을 선택 했을 때는 메시지 수신되는 단말의 Push ID를 지정 해야 Push 메시지가 발송 됩니다.
</p>
<p>
    Push 메시지에서 사용된 이미지는 <a href="#${Request.contextPath}/admin/resource/gcm/apns/file/list" title="Push 이미지 목록">Push 이미지 목록</a> 메뉴에서 확인 가능 하며, APNs 메시지는 "메시지 제목",
    "메시지 본문", "Callback URL" 만 적용 됩니다.
</p>
<p>
    <span class="text-success help-cursor" rel="tooltip" data-placement="top" data-original-title="메시지 발송 이력 위젯"><strong>메시지 발송 이력</strong></span>은 Push 메시지 카운트를
    보여 주며 다음을 포함 하고 있습니다. 앱의 상태(삭제 여부)에 따라서 Push 메시지 대상 단말 수 및 발송 실패 단말 수는 실시간으로 변경 됩니다.<br/>
    - <strong>총</strong> : Push 메시지 보낼 총 단말 수<br/>
    - <strong>총(유효)</strong> : Push 메시지 보낼 유효한 단말의 총 수<br/>
    - <strong>성공</strong> : Push 메시지 발송 성공한 단말 수<br/>
    - <strong>실패</strong> : Push 메시지 발송 실패한 단말 수<br/>
    - <strong>실패(유효)</strong> : Push 메시지 발송 유효 실패한 단말 수.<br/>
    - <strong>수신 확인</strong> : 앱에서 Push 메시지 수신 수<br/>
    - <strong>사용자 확인</strong> : Push 메시지 수신 이후 사용자가 확인한 수
</p>
<p>
    이미지 주소에 파일 지정은 다음 두 가지 방식을 제공 합니다.
</p>
<p>
    <dl>
        <dt>1. 원격 파일</dt>
        <dd>
            실제 파일을 업로드 하지 않고 파일의 원격 URL 을 알고 있는 경우, Image URL의 <strong>업로드</strong> 버튼을 클릭 하지 않고 원격 URL을 붙여 넣습니다.
            메시지 정보를 모두 입력 후, <strong>메시지 발송</strong> 버튼을 클릭 하면 메시지 발송과 동시에 원격 파일을 서버에서 다운로드 하여 자동으로 파일 생성 됩니다.
        </dd>
    </dl>
</p>
<p>
<dl>
    <dt>2. 내부 파일</dt>
    <dd>
        업로드 하려는 파일을 가지고 있을때 <strong>업로드</strong> 버튼을 클릭하여 소유하고 있는 파일을 선택 합니다. 파일 선택과 동시에 자동을 파일 업로드가
        진행 되며, 파일 정보를 모두 입력 후 <strong>메시지 발송</strong> 버튼을 클릭하면 메시지 발송과 동시에 파일 등록이 완료 됩니다.
    </dd>
</dl>
</p>
</@am.page_desc>

<#-- push 상태 그래프를 보여줄 영역 -->
<div class="row rasui-push-message-status-graph">
    <#-- push message 보내는 graph sample
    <div class="col-sm-4">
        <div class="well">
            <p class="txt-color-blueDark">
                - <strong>메시지</strong> : abcd<br/>
                - <strong>전송량</strong> : 100 / 1000<br/>
            </p>
            <div class="row text-center">
                <ul class="list-inline">
                    <li>
                        <div id="pie-chart-total" class="easy-pie-chart txt-color-blue easyPieChart" data-percent="40" data-size="60" data-pie-size="30">
                            <span class="percent percent-sign txt-color-blue">40</span>
                        </div>
                        <div class="font-xs text-muted" style="text-align: center;">전체</div>
                    </li>
                    <li>
                        <div class="easy-pie-chart txt-color-green easyPieChart" data-percent="80" data-size="60" data-pie-size="30">
                            <span class="percent percent-sign txt-color-green">80</span>
                        </div>
                        <div class="font-xs text-muted" style="text-align: center;">성공</div>
                    </li>

                    <li>
                        <div class="easy-pie-chart txt-color-red easyPieChart" data-percent="80" data-size="60" data-pie-size="30">
                            <span class="percent percent-sign txt-color-red">80</span>
                        </div>
                        <div class="font-xs text-muted" style="text-align: center;">실패</div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    -->
</div>

<#-- widget grid -->
<section id="widget-grid" class="">
    <#-- row -->
    <div class="row">
        <#-- Push message form widget start -->
        <article class="col-sm-12 col-md-12 col-lg-12">
            <#-- Push message form Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false"
                     data-widget-deletebutton="false">
                <@am.widget_title icon="fa-edit">메시지 작성</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <form id="fileupload-form">
                            <input id="push-image-attach" type="file" name="attach_file" style="display: none;">
                        </form>
                        <form id="checkout-form">
                            <fieldset>
                                <legend>Push 할 메시지를 작성 하고 발송 합니다.</legend>

                                <div class="row">
                                    <@am.bstrap_in_txt name="push_title" value="" title="메시지 제목"
                                        maxlength="64" placeholder="메시지 제목은 최대 128 bytes 까지 가능 합니다" widthClass="col-sm-12" />

                                    <@am.bstrap_in_txtarea name="push_text" value="" title="메시지 본문"
                                        rows=6 placeholder="메시지 본문을 입력 하세요" widthClass="col-sm-12" />

                                    <@am.bstrap_in_txt name="callback_url" value="" title="Callback URL"
                                        maxlength="128" placeholder="Callback URL은 최대 128 bytes 까지 가능 합니다" />

                                    <input type="hidden" name="file_srl" value="${mdv_none}" />
                                    <input type="hidden" id="backup-url" value="" />
                                    <@am.bstrap_in_txt_rbtn name="image_url" value="" title="이미지 주소"
                                        maxlength="128" placeholder="Push 메시지에서 보여줄 이미지의 URL" btn="업로드" />

                                    <@am.bstrap_in_select name="push_target" title="메시지 발송 대상">
                                        <option value="${target_all}">전체</option>
                                        <option value="${target_unit}">개별 대상 선택</option>
                                    </@am.bstrap_in_select>

                                    <div id="show-single-target" style="display: none;">
                                        <@am.bstrap_select2 id="single_push_target" title="수신 단말기" />
                                    </div>

                                </div>
                            </fieldset>

                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 메시지 발송
                                    </button>
                                </div>
                                <div class="btn-group pull-left">
                                    <button class="btn btn-sm btn-info btn-prev-page" type="button">
                                        <i class="fa fa-chevron-left"></i> 앱 리스트
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
        <#-- Push message form widget end -->

        <#-- Push message history list start -->
        <article class="col-sm-12 col-md-12 col-lg-12">
            <#-- Push message history list Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-1" data-widget-colorbutton="false" data-widget-editbutton="false"
                    data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">메시지 발송 이력</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body no-padding">

                        <#-- datatable 에서 검색을 지원하는 column 의 th 는 dt-column, dt-column-<column number> class 를 포함해야 한다.(localStorage를 이용한 초기화에 이용됨) -->
                        <table id="push_message_history_list" class="table table-striped table-bordered" width="100%">
                            <thead>
                                <tr>
                                    <th data-class="expand" style="text-align: center; max-width: 45px;min-width: 45px;">SN.</th>
                                    <th style="text-align: center;">제목</th>
                                    <th data-hide="phone,tablet" style="text-align: center; max-width: 70px; min-width: 70px;">총</th>
                                    <th data-hide="phone,tablet" style="text-align: center; max-width: 70px; min-width: 70px;">총(유효)</th>
                                    <th style="text-align: center; max-width: 70px; min-width: 70px;">성공</th>
                                    <th data-hide="phone,tablet" style="text-align: center; max-width: 70px; min-width: 70px;">실패</th>
                                    <th style="text-align: center; max-width: 70px; min-width: 70px;">실패(유효)</th>
                                    <th data-hide="phone,tablet" style="text-align: center; max-width: 70px; min-width: 70px;">수신 확인</th>
                                    <th data-hide="phone,tablet" style="text-align: center; max-width: 70px; min-width: 70px;">사용자 확인</th>
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                <#-- end widget content -->
                </div>
            <#-- end widget div -->
            </div>
        </article>
        <#-- Push message history list end -->

        <#-- All registed device list start -->
        <article class="col-sm-12 col-md-6 col-lg-6">
            <#-- All registed device list Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-2" data-widget-colorbutton="false" data-widget-editbutton="false"
                     data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">앱에 등록된 전체 단말</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body no-padding">

                        <#-- datatable 에서 검색을 지원하는 column 의 th 는 dt-column, dt-column-<column number> class 를 포함해야 한다.(localStorage를 이용한 초기화에 이용됨) -->
                        <table id="push_gcm_apns_form_list_all_terminal" class="table table-striped table-bordered" width="100%">
                            <thead>
                            <tr>
                                <th data-class="expand" style="text-align: center; max-width: 45px;min-width: 45px;">SN.</th>
                                <th style="text-align: center;">단말 아이디</th>
                                <th style="text-align: center; max-width: 26px; min-width: 26px;">상태</th>
                                <th style="text-align: center; max-width: 26px; min-width: 26px;">구분</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>

                    </div>
                    <#-- end widget content -->
                </div>
                <#-- end widget div -->
            </div>
        </article>
        <#-- All registed device list end -->

        <#-- Push ID registed terminal list start -->
        <article class="col-sm-12 col-md-6 col-lg-6">
            <#-- Push ID registed device list Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-3" data-widget-colorbutton="false" data-widget-editbutton="false"
                     data-widget-deletebutton="false">
                <@am.widget_title icon="fa-table">Push ID 등록된 단말</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body no-padding">

                        <#-- datatable 에서 검색을 지원하는 column 의 th 는 dt-column, dt-column-<column number> class 를 포함해야 한다.(localStorage를 이용한 초기화에 이용됨) -->
                        <table id="push_gcm_apns_form_list_regid_terminal" class="table table-striped table-bordered" width="100%">
                            <thead>
                            <tr>
                                <th data-class="expand" style="text-align: center; max-width: 45px;min-width: 45px;">SN.</th>
                                <th style="text-align: center;">단말 아이디</th>
                                <th style="text-align: center; max-width: 26px; min-width: 26px;">상태</th>
                                <th style="text-align: center; max-width: 26px; min-width: 26px;">구분</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>

                    </div>
                    <#-- end widget content -->
                </div>
            <#-- end widget div -->
            </div>
        </article>
        <#-- All registed terminal list end -->
    </div>
    <#-- end row -->
</section>
<#-- end widget grid -->

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        drawBreadCrumb(['Push(${appEntity.app_name})']);

        <#-- 페이지 도움말 영역 visible, invisible 처리 -->
        my.fn.initPageHelper('${_page_id}-helper');

        <#-- 업로드 버튼을 눌렀을 때 파일 선택 윈도우를 출력 하고 파일을 업로드 시킨다. -->
        <@am.bstrap_in_txt_rbtn_uploader id="push-image-attach"
                uri="${Request.contextPath}/admin/resource/gcm/apns/file/upload/t/"
                showid="image_url">
            $('input[name=file_srl]').val(result.file_srl);
            $('#backup-url').val(result.domain + result.uri);
        </@am.bstrap_in_txt_rbtn_uploader>

        $('.bstrap-in-txt-rbtn').click(function(e){
            <@am.jsevent_prevent />
            $('#push-image-attach').trigger('click');
        });

        <#-- 메시지 발송 이력 -->
        <@am.jsdatatable_simple listId="push_message_history_list" url="${Request.contextPath}/admin/message/gcm/apns/app/${appEntity.app_srl}/history/list/t/"
                searchTooltip="메시지 제목 첫글자 기준 검색" tableVarName="htable" ; job>
            <#if job == "order">
                [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'push_srl', mData: 'push_srl', bSortable: true, bSearchable: false},
                    {sName: 'push_title', mData: 'push_title', bSortable: true, bSearchable: true,
                        mRender: function(data, type, full){
                            var stitle = data;
                            if(data.length > 30) stitle = data.substring(0, 30) + ' ...';
                            return stitle;
                        }
                    },
                    {sName: 'total_count', mData: 'total_count', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(data <= 0)   return '확인중';
                            else            return data + '개';
                        }
                    },
                    {sName: 'total_real_count', mData: 'total_real_count', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(full.total_count <= 0)   return '확인중';
                            else                        return data + '개';
                        }
                    },
                    {sName: 'success_count', mData: 'success_count', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            return data + '개';
                        }
                    },
                    {sName: 'fail_count', mData: 'fail_count', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            return data + '개';
                        }
                    },
                    {sName: 'fail_real_count', mData: 'fail_real_count', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            return data + '개';
                        }
                    },
                    {sName: 'received_count', mData: 'received_count', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            return data + '개';
                        }
                    },
                    {sName: 'confirm_count', mData: 'confirm_count', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            return data + '개';
                        }
                    }
                ]
            </#if>
        </@am.jsdatatable_simple>

        <#-- 앱에 등록된 모든 단말 리스트 -->
        <@am.jsdatatable_simple_sm listId="push_gcm_apns_form_list_all_terminal" url="${Request.contextPath}/admin/app/${appEntity.app_srl}/device/list/rpi/0/eald/${mdv_yes}/t/"
                searchTooltip="단말 아이디 첫글자 기준 검색" tableVarName="dtable1" ; job>
            <#if job == "order">
                [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'device_srl', mData: 'device_srl', bSortable: true, bSearchable: false},
                    {sName: 'device_id', mData: 'device_id', bSortable: false, bSearchable: true},
                    {sName: 'push_ok', mData: 'push_ok', sClass:'dt_column_text_center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(data == ${mdv_yes}) {
                                return '<i class="fa fa-fw fa-envelope-o"></i>';
                            } else {
                                return '<i class="fa fa-fw fa-ban"></i>';
                            }
                        }
                    },
                    {sName: 'device_class', mData: 'device_class', sClass:'dt_column_text_center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(data == ${android_device_class}) {
                                return '<img src="${Request.resPath}img/android.png" alt="Android" width="16" style="margin-top: -4px;">';
                            } else if(data == ${ios_device_class}) {
                                return '<img src="${Request.resPath}img/apple.png" alt="iOS" width="16" style="margin-top: -4px;">';
                            } else {
                                return '-';
                            }
                        }
                    }
                ]
            </#if>
        </@am.jsdatatable_simple_sm>

        <#-- 앱에 등록된 Push ID 가 존재하는 단말 리스트 -->
        <@am.jsdatatable_simple_sm listId="push_gcm_apns_form_list_regid_terminal" url="${Request.contextPath}/admin/app/${appEntity.app_srl}/device/list/rpi/${mdv_yes}/eald/${mdv_yes}/t/"
                searchTooltip="단말 아이디 첫글자 기준 검색" tableVarName="dtable2" ; job>
            <#if job == "order">
                    [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'device_srl', mData: 'device_srl', bSortable: true, bSearchable: false},
                    {sName: 'device_id', mData: 'device_id', bSortable: true, bSearchable: true},
                    {sName: 'push_ok', mData: 'push_ok', sClass:'dt_column_text_center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(data == ${mdv_yes}) {
                                return '<i class="fa fa-fw fa-envelope-o"></i>';
                            } else {
                                return '<i class="fa fa-fw fa-ban"></i>';
                            }
                        }
                    },
                    {sName: 'device_class', mData: 'device_class', sClass:'dt_column_text_center', bSortable: false, bSearchable: false,
                        mRender: function(data, type, full){
                            if(data == ${android_device_class}) {
                                return '<img src="${Request.resPath}img/android.png" alt="Android" width="16" style="margin-top: -4px;">';
                            } else if(data == ${ios_device_class}) {
                                return '<img src="${Request.resPath}img/apple.png" alt="iOS" width="16" style="margin-top: -4px;">';
                            } else {
                                return '-';
                            }
                        }
                    }
                ]
            </#if>
        </@am.jsdatatable_simple_sm>

        <#-- push 메시지 보내는 단일 대상 선택 -->
        <@am.jsselect2 id="single_push_target" placeholder="Push 메시지 수신 단말 아이디를 입력 하세요." uniq="device_id"
            uri="${Request.contextPath}/admin/app/${appEntity.app_srl}/device/select2/list/rpi/${mdv_yes}/eald/${mdv_yes}/t/" />

        <#-- 메시지 발송 대상 선택 변경시, 단일 대상 선택 select2 show on/off -->
        $('select[name=push_target]').change(function(e){
            <@am.jsevent_prevent />
            var value = $(this).val();
            if(value == '${target_unit}')   $('#show-single-target').fadeIn(300);
            else                            $('#show-single-target').fadeOut(300);
        });


        // TODO 이제 발송되니, 발송 되는 controller 처리해 주고,
        //      이력 테이블 위젯 추가,
        //      발송 성공 하면 이력 테이블 refresh,
        //      발송 상태 보여주는 그래프 넣어야 한다.

        <#-- input form validator. single_push_target 은 서버에서 체크 -->
        $("#checkout-form").validate({
            rules: {
                push_title:         {required: true, minlength: 1, maxlength: 128},
                push_text:          {required: true, minlength: 1}
            },
            messages : {
                push_title: {
                    required: '메시지 제목은 필수값 입니다.',
                    minlength: '메시지 제목은 {0} 자 이상 입니다.',
                    maxlength: '메시지 제목은 {0} 자 이하 입니다.'
                },
                push_text: {
                    required: '메시지 본문은 필수값 입니다.',
                    minlength: '메시지 본문은 {0} 자 이상 입니다.'
                }
            },
            <@am.jsbstrap_validator_comm />
        });

        $('#btn-add').click(function(e){
            <#-- event bubbling prevent -->
            <@am.jsevent_prevent />

            <#-- jquery validator -->
            var check = $('#checkout-form').valid();
            if(!check) { return; }

            <#-- ajax 요청 하기 전에 사전 작업 -->
            $('.invalid').hide();
            var $btnAdd = $(this);
            $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 메시지 발송 중');

            var pushTarget = $('select[name=push_target]').val(), singlePushTarget='';
            if(pushTarget == '${target_unit}') {
                var $sigleValue = $('#single_push_target').prev().find('.select2-chosen');
                if($sigleValue.length > 0) singlePushTarget = $sigleValue.text();

                <#-- placeholder 문자열이면 무시 -->
                if(singlePushTarget.indexOf('Push 메시지 수신') == 0) singlePushTarget = '';
            }

            <#-- request 할 데이터를 만든다.(reqData 라는 이름을 왠만하면 바꾸지 말자. 이름을 그대로 사용해야 macro 사용이 편해짐) -->
            var reqData = {
                app_srl:        ${appEntity.app_srl},
                push_title:     $('input[name=push_title]').val(),
                push_text:      $('textarea[name=push_text]').val(),
                callback_url:   $('input[name=callback_url]').val(),
                push_target:    pushTarget,
                image_url:      $('input[name=image_url]').val(),
                file_srl:       $('input[name=file_srl]').val(),
                single_push_target: singlePushTarget
            };

            if($.trim($('#backup-url').val()) != $.trim(reqData.image_url)) reqData.file_srl = ${mdv_none};

            <#-- 메시지 발송 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/message/add/t/"
                    errortitle="메시지 발송 실패" ; job >
                <#if job == "success_job">
                    <#-- 메시지 발송 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = 'Push 메시지를 발송 하였습니다.';
                    <@am.jsnoti title="메시지 발송 성공" content="toastMsg" boxtype="success" />

                    <#-- 메시지 이력 테이블 reload -->
                    htable.draw();

                    <#-- push 상태 그래프를 추가 한다 -->
                    _ckras.ui.draw4PieChart({
                        appendFirst: true,
                        ignoreModify: true,     // 만일 이미 존재하는 것이라면 보여지는 데이터를 변경하지 않는다.
                        parentDomClass: 'rasui-push-message-status-graph',
                        id: data.push_srl,
                        text: ['- <strong>메시지</strong> : ' + data.push_title,
                               '- <strong>전송량</strong> : 계산중',
                               '- <strong>발송일</strong> : 발송 대기'],
                        chart: [
                            {name: '전체', percentage: 0, color: '#57889c'},
                            {name: '성공', percentage: 0, color: '#356e35'},
                            {name: '실패', percentage: 0, color: '#a90329'},
                            {name: '확인', percentage: 0, color: '#71843f'},
                        ],
                        canClose: false
                    });

                    <#-- upload data 를 초기화 -->
                    $('#fileupload-form')[0].reset();
                    $('#checkout-form')[0].reset();
                    $('input[name=image_url]').tooltip('destroy');
                    $('#show-single-target').hide();
                <#elseif job == "common_job">
                    <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 메시지 발송');
                </#if>
            </@am.jsajax_request>
        });

        <#-- 리스트로 이동 -->
        $('.btn-prev-page').click(function(e){
            <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/message/gcm/apns/app/list');
        });

        <#-- 실시간 서버에서 받아둔 push message status 데이터가 존재하면 그린다. -->
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

    var pagedestroy = function(){

    };

    loadScript("${Request.resPath}js/plugin/jquery-fileupload/jquery.fileupload.js", function () {
        loadScript("${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js", function () {
            loadScript("${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js", function () {
                loadScript("${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js", function () {
                    loadScript("${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js", function () {
                        loadScript("${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js", pagefunction)
                    });
                });
            });
        });
    });
</script>