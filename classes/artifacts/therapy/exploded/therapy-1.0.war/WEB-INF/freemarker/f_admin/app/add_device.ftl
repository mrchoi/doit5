<#assign _page_id="add-app-device">
<#assign _page_parent_name="단말기">
<#assign _page_current_name="단말기 등록">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    Push 메시지는 Android, iOS 대상으로 지원 하며 Push 메시지 발송을 위해서는 메시지를 수신할 단말기가 등록되어야 합니다. Android, iOS 구분에
    따라 다음의 값이 필요 합니다. 또한 GCM RID 및 Device Token 은 앱 별로 다르게 발급 되기 때문에 앱 별로 단말기가 등록 되어야 합니다.
</p>
<p>
    Android : 단말기에서 발급한 device_id 와 Google 에서 발급된 GCM RID<br/>
    iOS : 단말기에서 발급한 device_id 와 Apple 에서 발급된 Device Token
</p>
<p>
    일반적으로 단말기 등록은 특정 앱을 사용하는 단말기에서 API를 통해서 등록되며 현재 메뉴는 운영자가 임의 등록을 위해서 사용 되어 집니다.
    등록된 단말기는 <a href="#${Request.contextPath}/admin/app/device/list" title="단말기 목록">단말기 목록</a> 메뉴에서 확인 가능 합니다.
</p>
</@am.page_desc>

<#-- widget grid -->
<section id="widget-grid" class="">
    <#-- row -->
    <div class="row">
        <#-- NEW WIDGET START -->
        <article class="col-sm-12 col-md-12 col-lg-12">
            <#-- Widget ID (each widget will need unique ID)-->
            <div class="jarviswidget" id="wid-${_page_id}-0" data-widget-colorbutton="false" data-widget-editbutton="false"
                 data-widget-deletebutton="false">
            <@am.widget_title icon="fa-edit">단말기 등록</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <form id="checkout-form">
                            <fieldset>
                                <legend>등록할 단말기 정보를 입력 합니다.</legend>

                                <div class="row">
                                    <@am.bstrap_in_txt name="device_id" value="${deviceEntity.device_id!''}" title="단말기 아이디"
                                            maxlength="64" placeholder="단말기 아이디는 최대 64 bytes 까지 가능 합니다" />

                                    <@am.bstrap_in_txt name="device_type" value="${deviceEntity.device_type!''}" title="벤더, 모델명"
                                            maxlength="128" placeholder="벤더, 모델명은 최대 128 bytes 까지 가능 합니다" />

                                    <@am.bstrap_in_txt name="os_version" value="${deviceEntity.os_version!''}" title="단말기 OS 버전"
                                            maxlength="32" placeholder="OS 버전은 최대 32자 까지 가능 합니다" />

                                    <@am.bstrap_in_txt name="mobile_phone_number" value="${deviceEntity.mobile_phone_number!''}" title="단말기 전화번호"
                                            maxlength="16" placeholder="단말기의 전화번호를 입력하세요" />

                                    <@am.bstrap_in_select name="device_class" title="단말기 구분">
                                        <#assign terminal>${deviceEntity.device_class!""}</#assign>
                                        <#list supportTerminal?keys as key>
                                            <#if terminal == key>
                                                <option value="${key}" selected="selected">${supportTerminal[key]}</option>
                                            <#else>
                                                <option value="${key}">${supportTerminal[key]}</option>
                                            </#if>
                                        </#list>
                                    </@am.bstrap_in_select>

                                    <@am.bstrap_select2 id="app_srl" title="앱" />

                                    <@am.bstrap_in_txtarea name="push_id" value="" title="Push 아이디"
                                        rows=3 placeholder="GCM 을 위한 RID 나 APNs 를 위한 Device Token 을 입력하세요" widthClass="col-sm-12" />
                                </div>
                            </fieldset>

                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 등록
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
    <#-- end row -->
</section>
<#-- end widget grid -->

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        <#-- 페이지 도움말 영역 visible, invisible 처리 -->
        my.fn.initPageHelper('${_page_id}-helper');

        <#-- 단말기가 등록될 앱 선택 -->
        <@am.jsselect2 id="app_srl" placeholder="단말기가 등록될 앱 이름을 입력 하세요." uniq="app_srl"
                showColumn="app_name" uri="${Request.contextPath}/admin/app/list/${mdv_yes}/select2/t/"
                miniumLength=2 />

        <#-- 앱에 단말기를 등록 한다 -->
        $('#btn-add').click(function(e){
            <#-- event bubbling prevent -->
            <@am.jsevent_prevent />

            <#-- jquery validator -->
            //var check = $('#checkout-form').valid();
            //if(!check) { return; }

            <#-- ajax 요청 하기 전에 사전 작업 -->
            $('.invalid').hide();
            var $btnAdd = $(this);
            $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 등록중');

            <#-- request 할 데이터를 만든다.(reqData 라는 이름을 왠만하면 바꾸지 말자. 이름을 그대로 사용해야 macro 사용이 편해짐) -->
            var reqData = {
                device_id:              $('input[name=device_id]').val(),
                device_type:            $('input[name=device_type]').val(),
                os_version:             $('input[name=os_version]').val(),
                mobile_phone_number:    $('input[name=mobile_phone_number]').val(),
                device_class:           $('select[name=device_class]').val(),
                app_srl:                $('#app_srl').val(),
                push_id:                $('textarea[name=push_id]').val()
            };

            <#-- app에 단말기 추가 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/app/device/add/t/"
                    moveuri="#${Request.contextPath}/admin/app/device/list" errortitle="단말기 등록 실패" ; job >
                <#if job == "success_job">
                    <#-- 앱 추가 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = '앱에 단말기 등록을 완료 하였습니다.';
                    <@am.jsnoti title="단말기 등록 성공" content="toastMsg" boxtype="success" />
                    <#-- 앱 추가 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                    my.fn.removeDatatableStorage('list-all-device');
                <#elseif job == "common_job">
                    <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 등록');
                </#if>
            </@am.jsajax_request>
        });
    };

    pagefunction();
</script>
