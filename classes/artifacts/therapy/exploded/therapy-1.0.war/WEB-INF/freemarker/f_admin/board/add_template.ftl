<#assign _page_id="add-template">
<#assign _page_parent_name="커뮤니티">
<#assign _page_current_name="템플릿 추가">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    게시물에서 사용 할 수 있는 템플릿을 생성 합니다. 템플릿은 앱별로 할당이 되며, 앱에 할당된 템플릿을 바탕으로 앱에 포함된 게시물에서
    템플릿 선택이 가능 합니다. 템플릿 작성시에 다음 제한 사항을 유의 해야 합니다.
</p>
<p>
    <ol>
        <li>템플릿은 최대 2 Depth로 구성 할 수 있습니다.</li>
        <li>템플릿의 1 Depth 구성 요소는 배열, 날짜, 문자열, html이 가능 합니다.</li>
        <li>템플릿의 2 Depth 구성 요소는 배열의 구성 항목이 가능 합니다.</li>
        <li>배열은 특정 세트로 구성된 항목을 입력 가능하게 하는 요소 입니다.</li>
        <li>날짜는 년/월/일을 입력 가능하게 하는 요소 입니다.</li>
        <li>시간은 24시간 형태의 시/분을 입력 가능하게 하는 요소 입니다.</li>
        <li>날짜/시간은 년/월/일 날짜와 24시간 형태의 시/분 시간을 입력 가능하게 하는 요소 입니다.</li>
        <li>문자열은 한줄 텍스트를 입력 가능하게 하는 요소 입니다.</li>
        <li>문자영역은 여러 줄의 문자열을 입력 가능하게 하는 요소 입니다.</li>
        <li>구성 항목이 없는 배열은 존재 할 수 없습니다.</li>
        <li>템플릿은 최소 하나 이상의 앱과 매핑이 되어야 합니다.</li>
    </ol>
</p>
<p>
    추가된 템플릿은 <a href="#${Request.contextPath}/admin/board/template/list" title="템플릿 목록">템플릿 목록</a> 메뉴에서 확인 가능 합니다.
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
                <@am.widget_title icon="fa-edit">템플릿 추가</@am.widget_title>

                <#-- widget div-->
                <div>
                    <#-- widget edit box -->
                    <div class="jarviswidget-editbox"><#-- This area used as dropdown edit box --></div>
                    <#-- end widget edit box -->

                    <#-- widget content -->
                    <div class="widget-body">
                        <form id="checkout-form">
                            <fieldset>
                                <legend>추가할 템플릿 정보를 입력 합니다.</legend>

                                <div class="row">
                                    <@am.bstrap_in_txt name="template_title" value="" title="템플릿 이름"
                                            maxlength="64" placeholder="최대 64byte 까지 가능 합니다." />

                                    <@am.bstrap_in_txt name="template_description" value="" title="템플릿 설명"
                                            maxlength="256" placeholder="최대 256자 까지 가능 합니다." />
                                </div>
                                <hr>
                                <div class="row">
                                    <@am.bfs_input_template_element />
                                    <@am.bfs_template_struct />
                                </div>
                                <hr>
                                <div class="row">
                                    <@am.bstrap_select2 id="app_srls" title="템플릿 사용 앱 선택" widthClass="col-sm-12" />
                                </div>
                            </fieldset>

                            <div class="form-actions">
                                <div class="btn-group">
                                    <button id="btn-add" class="btn btn-sm btn-success" type="button">
                                        <i class="fa fa-check"></i> 템플릿 추가
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

        <#-- 템플릿이 등록될 앱 선택 -->
        <@am.jsselect2 id="app_srls" placeholder="단말기가 등록될 앱 이름을 입력 하세요." uniq="app_srl"
            showColumn="app_name" uri="${Request.contextPath}/admin/app/list/${mdv_yes}/select2/t/"
            miniumLength=2 usingTag=true />

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

        $('#btn-add').click(function(e){
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

            <#-- 템플릿 추가 요청 -->
            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/template/add/t/"
                    moveuri="#${Request.contextPath}/admin/board/template/list" errortitle="템플릿 추가 실패" ; job >
                <#if job == "success_job">
                    <#-- 템플릿 추가 성공하면 toast 메시지를 보여 준다 -->
                    var toastMsg = reqData.template_title + '를 추가 하였습니다.';
                    <@am.jsnoti title="템플릿 추가 성공" content="toastMsg" boxtype="success" />

                    <#-- 앱 추가 후 테이블의 1페이지를 보여 주기 위해서 테이블의 localStorage 를 삭제 시킨다. -->
                    my.fn.removeDatatableStorage('list-template');
                <#elseif job == "common_job">
                    <#-- 공통 작업. disabled 시킨 버튼을 enabled 시킨다 -->
                    $btnAdd.removeClass('disabled').html('<i class="fa fa-check"></i> 템플릿 추가');
                </#if>
            </@am.jsajax_request>
        });
    };

    loadScript("${Request.resPath}js/plugin/jquery-nestable/jquery.nestable.min.js", pagefunction)
</script>