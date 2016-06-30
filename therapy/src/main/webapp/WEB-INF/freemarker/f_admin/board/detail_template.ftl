<#assign _page_id="detail-template">
<#assign _page_parent_name="템플릿 목록">
<#assign _page_current_name="템플릿 정보">

<@am.page_simple_depth icon="fa-puzzle-piece" parent="${_page_parent_name}" current="${_page_current_name}"
        canMoveParent=true />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    게시물 템플릿 정보를 확인하고 수정하는 페이지 입니다. 상위 페이지인 템플릿 목록으로 이동 하고 싶으면 사용자 정보 위젯 하단의 이전 버튼을 클릭 하면 됩니다.
    템플릿 정보 변경을 위해서는 변경 사항을 모두 작성 후, 템플릿 수정 버튼을 클릭 합니다.
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
                <@am.widget_title icon="fa-edit">템플릿 수정</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <form id="checkout-form">
                            <fieldset>
                                <legend>템플릿 정보를 변경 합니다.</legend>

                                <div class="row">
                                    <@am.bstrap_in_txt name="template_title" value="${templateEntity.template_title}" title="템플릿 이름"
                                        maxlength="64" placeholder="최대 64byte 까지 가능 합니다." />

                                    <@am.bstrap_in_txt name="template_description" value="${templateEntity.template_description}" title="템플릿 설명"
                                        maxlength="256" placeholder="최대 256자 까지 가능 합니다." />

                                    <@am.bstrap_disabled_txt value="${(templateEntity.c_date * 1000)?number_to_date?string('yyyy.MM.dd HH:mm:ss')}" title="생성일" />
                                    <@am.bstrap_disabled_txt value="${(templateEntity.u_date * 1000)?number_to_date?string('yyyy.MM.dd HH:mm:ss')}" title="수정일" />
                                </div>
                                <hr>
                                <div class="row">
                                    <@am.bfs_input_template_element />
                                    <@am.bfs_template_struct hideEmpty=true />
                                </div>
                                <hr>
                                <div class="row">
                                    <@am.bstrap_select2 id="app_srls" title="템플릿 사용 앱 선택" widthClass="col-sm-12" />
                                </div>
                            </fieldset>

                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 템플릿 수정
                                    </button>
                                </div>
                                <div class="btn-group pull-left">
                                    <button class="btn btn-sm btn-info btn-prev-page" type="button">
                                        <i class="fa fa-chevron-left"></i> 템플릿 목록
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

        drawBreadCrumb(['${templateEntity.template_title?js_string}']);

        <#-- 템플릿이 등록될 앱 선택 -->
        <@am.jsselect2 id="app_srls" placeholder="단말기가 등록될 앱 이름을 입력 하세요." uniq="app_srl"
            showColumn="app_name" uri="${Request.contextPath}/admin/app/list/${mdv_yes}/select2/t/"
            miniumLength=2 usingTag=true />

        <#-- 기존에 매핑 되어 있는 앱 정보를 보여 준다 -->
        $('#app_srls').select2('data', [
                <#list appEntities as appEntity>
                    {"app_srl": ${appEntity.app_srl}, "app_name": "${appEntity.app_name?js_string} (${appEntity.app_version?js_string})"},
                </#list>
        ]);

        <#-- 템플릿 요소 변경시 -->
        $('#menu-template-element').click(function(e){
            if(!e.target || e.target.tagName != 'A') return;
            var $target = $(e.target),
                    val = $target.data('element');

            if(!val || val == 'close') return;
            var name = $target.text(),
                    $selElement = $('#selected-template-element');

            $selElement.text(name)
                    .attr('data-element', val);
        });

        <#-- active nestable list -->
        var $templateStruct = $('#template-struct');
        $templateStruct.nestable();

        <#-- 템플릿 1depth 요소 추가 -->
        $('#add-template-element').click(function(e){
        <@am.jsevent_prevent />

            var $input = $('#template-element-name'),
                    name = $.trim($input.val());
            if(!name) {
            <@am.jsnoti title="실수?" content="템플릿 요소의 변수명을 입력 해야 합니다." contenttype="val" />
                return;
            }

            if(my.fn.checkDocumentTemplateDuplicateKey($templateStruct, name)) {
            <@am.jsnoti title="변수명 중복" content="이미 존재하는 변수명 입니다." contenttype="val" />
                return;
            }

            <#-- 안내는 숨긴다 -->
            $('#template-struct-help').hide();

            var $selected = $('#selected-template-element'),
                    type = $selected.attr('data-element'),
                    desc = $selected.text();

            // 템플릿 추가하고 변수명 입력창은 초기화
            my.fn.addDocumentTemplateElement($templateStruct, name, type, desc);
            $input.val('');
        });

        <#-- 템플릿 구성 UI의 필수 이벤트 반응 등록 -->
        my.fn.setDocumentTemplateListListner(function(kind){
            if(kind != 'remove') return;
            if($('#template-struct-inner').children().length <= 0) $('#template-struct-help').show();
        });

        <#-- 기존에 설정 되어 있는 템플릿 구성을 로딩 한다. 템플릿 데이터는 반듯이 존재 해야 한다. -->
        var templatePrevData = eval('(${templateEntity.template_content})');
        my.fn.loadDocumentTemplateElement($templateStruct, templatePrevData);

        my.fn.addValidator('maxbytelength');
        $("#checkout-form").validate({
            rules: {
                template_title:         {required: true, maxbytelength: 64},
                template_description:   {maxbytelength: 256}
            },
            messages : {
                template_title: {
                    required: '템플릿 이름은 필수값 입니다.',
                    maxbytelength: '템플릿 이름은 {0}byte 이하 입니다.'
                },
                template_description: {
                    maxbytelength: '템플릿 설명은 {0}byte 이하 입니다.'
                }
            },
            <@am.jsbstrap_validator_comm />
        });

        <#-- 리스트로 이동 -->
        $('.btn-prev-page').click(function(e){
        <@am.jsevent_prevent />
            my.fn.pmove('#${Request.contextPath}/admin/board/template/list');
        });

        <#-- 템플릿을 수정 한다. btn-add는 귀찮아서 수정하지 않았음. -->
        $('#btn-add').click(function(e) {
            <@am.jsevent_prevent />

            <#-- jquery validator -->
            var check = $('#checkout-form').valid();
            if(!check) { return; }

            var template = my.fn.setDocumentTemplateJsonData($templateStruct);
            if(_.isEmpty(template)) {
            <@am.jsnoti title="템플릿 구성" content="구성된 템플릿 형태가 없습니다." contenttype="val" />
                return;
            }

            var appSrls = $('#app_srls').select2('data');
            if(appSrls.length <= 0) {
            <@am.jsnoti title="앱 선택" content="템플릿을 사용하는 앱은 최소 하나 이상 선택 되어야 합니다." contenttype="val" />
                return;
            }

            <#-- ajax 요청 하기 전에 사전 작업 -->
            $('.invalid').hide();
            var $btnAdd = $(this);
            $btnAdd.addClass('disabled').html('<i class="fa fa-gear fa-spin"></i> 추가중');

            <#-- request 할 데이터를 만든다.(reqData 라는 이름을 왠만하면 바꾸지 말자. 이름을 그대로 사용해야 macro 사용이 편해짐) -->
            var arrSrl = [];
            for(var prop in appSrls) {
                if(appSrls[prop].app_srl && appSrls[prop].app_srl > 0)
                    arrSrl.push(appSrls[prop].app_srl);
            }

            var reqData = {
                template_title:         $.trim($('input[name=template_title]').val()),
                template_description:   $.trim($('input[name=template_description]').val()),
                template_content:       JSON.stringify(template),
                app_srls:               arrSrl
            };

            <#-- 템플릿 수정 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/template/${templateEntity.template_srl}/t/" method="PUT"
                    moveuri="#${Request.contextPath}/admin/board/template/list" errortitle="템플릿 수정 실패" ; job >
                <#if job == "success_job">
                    <#-- 템플릿 수정 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = reqData.template_title + '를 추가 하였습니다.';
                    <@am.jsnoti title="템플릿 수정 성공" content="toastMsg" boxtype="success" />

                    <#-- 앱 추가 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                    //my.fn.removeDatatableStorage('list-template');
                <#elseif job == "common_job">
                    <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 템플릿 수정');
                </#if>
            </@am.jsajax_request>
        });

    };

    loadScript("${Request.resPath}js/plugin/jquery-nestable/jquery.nestable.min.js", pagefunction)
</script>