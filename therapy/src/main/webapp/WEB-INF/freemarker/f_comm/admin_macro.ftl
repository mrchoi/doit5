<#-- 지원하는 매크로 목록
    page_simple_depth
    page_desc
    widget_title
    bstrap_in_txt
    bstrap_in_txtarea
    bstrap_in_txt_rbtn
    bstrap_in_select
    bstrap_select2
    bstrap_in_txt_rbtn_uploader
    bstrap_modal
    bstrap_file_uploader_modal
    bstrap_file_uploader_ajax
    jsnoti
    jsevent_prevent
    jsajax_json_header
    jsajax_fail_break
    jschangecontent
    jsajax_request
    jsbstrap_validator_comm
    jsdatatable_simple
    jsdatatable_simple_sm
    jsdatatable_very_simple1
    jsdatatable
    jsxeditable
    jsselect2
    bfs_input_template_element
    bfs_template_struct

-->

<#--
    페이지 상단의 간단 페이지 depth 를 출력 한다.
    icon    : 페이지 depth 문자 앞에 출력될 Font Awesome 아이콘 class 명
    parent  : 이전 메뉴의 타이틀
    current : 현재 메뉴의 타이틀
-->
<#macro page_simple_depth icon="" parent="" current="" canMoveParent=false>
    <div class="row">
        <div class="col-xs-12 col-sm-7 col-md-7 col-lg-4">
            <h1 class="page-title txt-color-blueDark">
                <i class="fa ${icon} fa-fw "></i>
                <#if parent != "">
                    <#if canMoveParent == true>
                        <a class="btn-prev-page" style="cursor: pointer;">${parent}</a>
                    <#else>
                        ${parent}
                    </#if>
                        <span>>
                        ${current}
                    </span>
                <#else>
                    ${current}
                </#if>
            </h1>
        </div>
    </div>
</#macro>

<#--
    페이지 상단의 페이지 설명을 출력 한다. nested 를 사용한다.
    title   : 설명 부분의 타이틀
    id      : 설명 부분의 id. localStorage 에 저장되어 visible, invisible 하기 위해서 필요함.
-->
<#macro page_desc title="" id="page-helper">
    <div class="row" id="${id}">
        <div class="col-sm-12">
            <div class="well">
                <button class="close" data-dismiss="alert">
                    ×
                </button>
                <h1 class="semi-bold">${title}</h1>
                <#nested>
            </div>
        </div>
    </div>
</#macro>

<#--
    위젯 타이틀을 출력 한다. nested 를 사용 한다.
    icon    : 위젯 타이틀에 포함될 아이콘 font awesome 아이콘 class 명
-->
<#macro widget_title icon="">
    <header>
        <span class="widget-icon"> <i class="fa ${icon}"></i> </span>
        <h2><#nested> </h2>
    </header>
</#macro>

<#--
    bootstrap form input text element 를 출력 한다.(단일 라인의 일반 텍스트 값 입력용)
    name           : request parameter name
    value          : request parameter value
    title          : 항목명
    maxlength      : 입력 최대 길이
    placeholder    : placeholder 값
    widthClass     : 너비 클래스. 기본값 col-sm-6
-->
<#macro bstrap_in_txt name="" value="" title="" maxlength="" placeholder="" widthClass="col-sm-6" password=false>
    <div class="${widthClass}">
        <div class="form-group">
            <label>${title} <span id="errid-${name}" class="invalid text-danger" style="display: none;">- <span id="errmsg-${name}"></span></span></label>
            <#if password>
                <input class="form-control" name="${name}" value="${value!""}"
                       type="password" maxlength="${maxlength}" placeholder="${placeholder}">
            <#else>
                <input class="form-control" name="${name}" value="${value!""}"
                       type="text" maxlength="${maxlength}" placeholder="${placeholder}">
            </#if>

        </div>
    </div>
</#macro>

<#--
    bootstrap form input text element(disabled)를 출력 한다. view 용도임.
    value          : parameter value
    title          : 항목명
    widthClass     : 너비 클래스. 기본값 col-sm-6
-->
<#macro bstrap_disabled_txt value="" title="" widthClass="col-sm-6">
    <div class="${widthClass}">
        <div class="form-group">
            <label>${title}</label>
            <input class="form-control" disabled="disabled" type="text" value="${value!""}">
        </div>
    </div>
</#macro>

<#--
    bootstrap form textarea element 를 출력 한다.
    name            : request parameter name
    value           : request parameter value
    title           : 항목명
    rows            : textarea 의 row count
    placeholder     : placeholder 값
    widthClass      : 너비 클래스. 기본값 col-sm-6
-->
<#macro bstrap_in_txtarea name="" value="" title="" rows=4 placeholder="" widthClass="col-sm-6">
    <div class="${widthClass}">
        <div class="form-group">
            <label>${title} <span id="errid-${name}" class="invalid text-danger" style="display: none;">- <span id="errmsg-${name}"></span></span></label>
            <textarea name="${name}" class="form-control" placeholder="${placeholder}" rows="${rows}"></textarea>
        </div>
    </div>
</#macro>

<#--
    오른쪽에 버튼이 붙어 있는 bootstrap form input text element 를 출력 한다.(단일 라인의 일반 텍스트 값 입력용)
    오른쪽에 붙는 버튼은 공통으로 bstrap-in-txt-rbtn 을 class 로 가진다.
    또한 버튼 클릭시 반응할 대상은 버튼의 data-relation 에 지정 되어 있으므로 대상은 input[name=<찾을 name>] 으로 찾을 수 있다.
    name           : request parameter name
    value          : request parameter value
    title          : 항목명
    maxlength      : 입력 최대 길이
    placeholder    : placeholder 값
    btn            : 버튼명
-->
<#macro bstrap_in_txt_rbtn name="" value="" title="" maxlength="" placeholder="" btn="" widthClass="col-sm-6">
    <div class="${widthClass}">
        <div class="form-group">
            <label>${title} <span id="errid-${name}" class="invalid text-danger" style="display: none;">- <span id="errmsg-${name}"></span></span></label>
            <div class="row">
                <div class="col-sm-12">
                    <div class="input-group">
                        <input class="form-control" name="${name}" value="${value!''}"
                               type="text" maxlength="${maxlength}" placeholder="${placeholder}">
                        <div class="input-group-btn">
                            <button class="btn btn-default bstrap-in-txt-rbtn" data-relation="${name}" type="button">${btn}</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#--
    bootstrap form select element 를 출력 한다. nested 를 사용한다.

    name           : request parameter name
    title          : 항목명
-->
<#macro bstrap_in_select name="" title="" widthClass="col-sm-6">
    <div class="${widthClass}">
        <div class="form-group">
            <label class="control-label">${title} <span id="errid-${name}" class="invalid text-danger" style="display: none;">- <span id="errmsg-${name}"></span></span></label>
            <div class="row smart-form">
                <label class="select">
                    <select style="padding-left:10px;" name="${name}">
                        <#nested>
                    </select> <i></i>
                </label>
            </div>
        </div>
    </div>
</#macro>

<#--
    boostrap form select2 element 를 출력 한다.
    remote 에서 데이터를 가져오는 select2 이며, remote 에서 데이터를 가져올 필요 없으면 다른 것을 사용 한다.
    html 형태만 추가 하는 것이고, 형태를 추가하면 javascript select2 로직을 꼭 추가 해야 한다.
    javascript 로직은 jsselect2 매크로를 사용 하면 된다.
    push_gcm_apns_form.ftl 참고

    id                  : select2 의 id
    title               : select2 항목 제목
    widthClass          : 너비 설정 class
    usingRightButton    : select2 오른쪽에 버튼을 추가 할지 여부. true면 추가 false면 없음
    rightButtonId       : select2 오른쪽 버튼이 추가 될때 버튼의 아이디
    rightButtonText     : select2 오른쪽 버튼이 추가 될때 버튼에 표시될 텍스트
-->
<#macro bstrap_select2 id="" title="" widthClass="col-sm-6" usingRightButton=false rightButtonId=""
        rightButtonText="확인">
    <div class="${widthClass}">
        <div class="form-group">
            <label>${title} <span id="errid-${id}" class="invalid text-danger" style="display: none;">- <span id="errmsg-${id}"></span></span></label>
            <div class="row">
                <div class="col-sm-12">
                    <#if usingRightButton>
                        <div class="input-group">
                            <div id="${id}" class="select2" style="width:100%;height:30px;"></div>
                            <div class="input-group-btn">
                                <button class="btn btn-default bstrap-in-txt-rbtn" id="${rightButtonId}" type="button">${rightButtonText}</button>
                            </div>
                        </div>
                    <#else>
                        <div id="${id}" class="select2" style="width:100%;height:30px;"></div>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#--
    toast 메시지를 보여 준다.(javascript 코드)
    title       : toast 메시지 타이틀
    content     : toast 메시지 본문. ajax 일때는 data.ck_message 를 사용하면 됨(js로 값이 변경됨)
    contenttype : var 라면 content 가 javascript 변수이고, 아니라면 스트링 값임
    timeout     : toast 메시지를 보여줄 시간. millisec 단위
-->
<#macro jsnoti title="" content="" contenttype="var" boxtype="warn" timeout=2000>
    <#assign color = "#C46A69">
    <#assign icon = "fa-warning">
    <#if boxtype != "warn">
        <#assign color="#296191">
        <#assign icon = "fa-check">
    </#if>

    $.smallBox({
        title : '${title}',
        <#if contenttype == 'var'>
            content : '<i class="fa ${icon}"></i> <i>'+${content}+'</i>',
            color : '${color}',
        <#else>
            content : '<i class="fa ${icon}"></i> <i>${content}</i>',
            color : '${color}',
        </#if>
        <#if timeout gt 0>
            timeout : ${timeout}
        </#if>
    });
</#macro>

<#--
    event prevent 를 위한 event 기본 처리. click 등등의 이벤트 함수 내에서 사용
    eventname       : event object name
-->
<#macro jsevent_prevent eventname="e">
    ${eventname}.preventDefault();
    ${eventname}.stopPropagation();
</#macro>

<#--
    ajax request 시에 json 으로 요청하고 json 으로 받기 위한 header 값 설정
    request     : ajax request object
-->
<#macro jsajax_json_header request="request">
    ${request}.setRequestHeader('Accept', 'application/json;charset=UTF-8');
    ${request}.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
</#macro>

<#--
    ajax request 결과는 제대로 받았지만 에러 결과 일때 에러 결과를 화면에 반영 한다.(javascript 코드)
    errcdfield  : error code json attribute 명
    errmsgfield : error message json attribute 명
    reasonfield : error reason json attribute 명
-->
<#macro jsajax_fail_break errcdfield="data.ck_error" errmsgfield="data.ck_message" reasonfield="data.ck_reason"
        title="" xhr="xhr">
    if(${errcdfield} != my.data.ajaxSuccessCode) {
        console.warn('ajax return error code['+${errcdfield}+'], msg['+${errmsgfield}+']');
        if(${xhr}) console.warn(${xhr});

        <#-- 각각의 항목에 에러 메시지를 보여 준다 -->
        if(!_.isNull(${reasonfield}) && !_.isNaN(${reasonfield}) &&
                !_.isUndefined(${reasonfield}) && !_.isEmpty(${reasonfield})) {
            _.each(_.keys(${reasonfield}), function(key){
                if(_.has(${reasonfield}, key)) {
                    $('#errmsg-'+key).text(${reasonfield}[key]);
                    $('#errid-'+key).show();
                }
            });
        }

        <#-- notification 창을 잠깐 보여 준다 -->
        <@jsnoti title="${title!'실패'}" content="${errmsgfield}" />

        <#nested>
        return;
    }
</#macro>

<#--
    smart admin의 content 부분을 교체 한다.(javascript 로직)
    moveuri     : 이동할 페이지 URL(전체 full uri 임. hash 포함)
    navuri      : navigator 메뉴에서 특정 위치로 고정하여 highlight 필요 할때 특정 위치의 URL(has 다음의 uri)
-->
<#macro jschangecontent moveuri="" navuri="" isVarMove=false isVarNav=false>
    <#if navuri?? && navuri != ''>
        $(document).trigger('changecontent', [
            <#if isVarMove> ${moveuri} <#else> '${moveuri}' </#if>,
            <#if isVarNav> ${navuri} <#else> '${navuri}' </#if>
        ]);
    <#else>
        $(document).trigger('changecontent', [
            <#if isVarMove> ${moveuri} <#else> '${moveuri}' </#if>
        ]);
    </#if>
</#macro>

<#--
    smart admin 에서 데이터 등록/수정 등등을 위한 ajax 요청을 한다.(javascript 로직)
    submituri       : request 할 uri
    moveuri         : request 완료 후 완전 성공했을때 이동할 페이지. 만일 이동하지 않으면 empty string 을 사용하면 됨(값을 주지 않으면 됨)
    reqdata         : request 할 request data variable name
    method          : request method
    errortitle      : request 실패 했을때 보여줄 toast 메시지의 제목
-->
<#macro jsajax_request submituri="" moveuri="" reqdata="reqData" method="POST" errortitle="" useTid=true>
    $.ajax({
        type: '${method!"POST"}',
        url: '${submituri}'<#if useTid> + $.now()</#if>,
        <#if reqdata?? && reqdata != ''>
            data: JSON.stringify(${reqdata!'reqData'}),
        </#if>
        dataType: 'json',
        //csrf: false,
        beforeSend: function(request) {
            <@jsajax_json_header />
        },
        success: function(data, status, xhr) {
            <#-- 실패하면 바로 리턴 된다 -->
            <@jsajax_fail_break title="${errortitle!'실패'}" />

            <#-- 페이지 이동은 complete 에서 한다 -->
            <#nested "success_job">
        },
        error: function(xhr, status, error) {
            console.error('error ajax request');
            console.error(status);
            console.error(error);

            <@jsnoti title="${errortitle!'실패'}" content="네트웍 오류 입니다." contenttype="string" timeout=4000 />
        },
        complete: function(xhr, status) {
            if(xhr && xhr.responseJSON && xhr.responseJSON.ck_error == my.data.ajaxSuccessCode) {
                <#if moveuri?? && moveuri != ''>
                    <#-- ajax 완전 성공 하면 페이지 이동 한다 -->
                    <@jschangecontent moveuri="${moveuri}" />
                <#else>
                    <#nested "common_job">
                </#if>
            } else {
                <#-- 공통 작업을 처리 한다 -->
                <#nested "common_job">
            }
        }
    });
</#macro>

<#--
    boostrap form 형태를 사용 했을때 jQuery validator 에서 공통으로 처리하는 부분.
    validator 에러 발생시 action 및 에러 수정시 action 처리
-->
<#macro jsbstrap_validator_comm>
    success: function(label, element) {
        <#-- bootstrap form 형태에서 validator 체크 후 실패 난 것에 대해서 다시 성공 했을때 처리 -->
        var $element = $(element);
        var name = $element.attr('name');
        var $formGroup = $element.parents('.form-group');
        var $errId = $formGroup.find('#errid-'+name);

        $errId.hide();
    },
    errorClass: 'text-danger',
    errorPlacement: function(error, element) {
        <#-- boostrap form 형태에서 에러 처리 하는 것 add_app.ftl 에서 형태를 만들었음 -->
        var name = element.attr('name');
        var $formGroup = element.parents('.form-group');
        var $errId = $formGroup.find('#errid-'+name);
        var $errMsg = $errId.find('#errmsg-'+name);

        $errMsg.text(error.text());
        $errId.show();
    }
</#macro>

<#--
    jquery datatable 울 출력 한다.(테이블 헤더 위에 검색 항목이 추가된 테이블 형태)
    col-12 너비에 들어가는 테이블 이다.
    list_app.ftl 을 참고 하면 된다.

    [nested]
    nested "order"      : 최초의 ordering. [[1, 'desc']] 와 같은 형태가 되어야 함. array.
    nested "columns"    : 테이블의 column 정보. aoColumns 값임. array
    nested "useCustomButton"    : 사용자 정의 버튼

    [parameter]
    listId              : 테이블의 id
    url                 : ajax url. url 은 't/'로 끝나야 하고 자동으로 tid 를 붙인다.
    searchTooltip       : 좌측 상단 search 의 tooltip. 만일 값이 없으면 tooltip 없다.
    tableVarName        : table javascript object name
    useCustomButton     : 사용자 정의 버튼 사용할지 여부
    useCheckBox         : row 제일 앞에 check box 사용할지 여부
-->
<#macro jsdatatable_simple listId="datatable_list" url="" searchTooltip="" tableVarName="otable"
        useCustomButton=false useCheckBox=false>
    <#--
    // DOM Position key index //

    l - Length changing (dropdown)
    f - Filtering input (search)
    t - The Table! (datatable)
    i - Information (records)
    p - Pagination (paging)
    r - pRocessing
    < and > - div elements
    <"#id" and > - div with an id
    <"class" and > - div with a class
    <"#id.class" and > - div with an id and class

    Also see: http://legacy.datatables.net/usage/features
    -->
    var responsiveHelper_list = undefined;
        var breakpointDefinition = {
        tablet : 1024,
        phone : 480
    };

    var ${tableVarName} = $('#${listId}').DataTable({
        <#--
            "bFilter": false,
            "bInfo": false,
            "bLengthChange": false
            "bAutoWidth": false,
            "bPaginate": false,
            "bStateSave": true // saves sort state using localStorage
        -->
        <#if useCustomButton>
            sDom: "<'dt-toolbar'<'col-xs-12 col-sm-4 hidden-xs'f><'font-sm txt-color-greenLight col-sm-4 col-xs-12 hidden-xs'r><'col-sm-4 col-xs-12 hidden-xs'lT>>" +
        <#else>
            sDom: "<'dt-toolbar'<'col-xs-12 col-sm-4 hidden-xs'f><'font-sm txt-color-greenLight col-sm-4 col-xs-12 hidden-xs'r><'col-sm-4 col-xs-12 hidden-xs'l>>" +
        </#if>
            "t" +
            "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
        autoWidth: true,
        preDrawCallback: function() {
            <#-- Initialize the responsive datatables helper once. -->
            if (!responsiveHelper_list) {
                responsiveHelper_list = new ResponsiveDatatablesHelper($('#${listId}'), breakpointDefinition);
            }
        },
        rowCallback: function(nRow) {
            responsiveHelper_list.createExpandIcon(nRow);
        },
        drawCallback: function(oSettings) {
            responsiveHelper_list.respond();
        },
        processing: true,
            serverSide: true,
            ajax: {
            url: '${url}'+ $.now(),
            type: 'POST'
        },
        <#-- saves sort state using localStorage -->
        bStateSave: true,
        <#-- Define the starting point for data display when using DataTables with pagination -->
        iDisplayStart: 0,
        <#-- Number of rows to display on a single page when using pagination -->
        iDisplayLength: 25,
        aLengthMenu: [25, 50, 75, 100],
        language: {
            paginate: {
                first: '처음',
                last: '마지막',
                next: '다음',
                previous : '이전'
            },
            zeroRecords: '검색된 데이터가 없습니다.',
            emptyTable: '데이터가 없습니다.',
            info: '필터링 된 _TOTAL_개에서 _START_번에서 _END_번까지 리스트입니다.',
            infoFiltered : '(총 _MAX_개의 데이터가 있습니다.)',
            infoEmpty: '필터링 된 0개에서 0번에서 0번까지 리스트입니다.',
            processing: '데이터를 가져오는 중입니다.',
            loadingRecords: '데이터를 로딩 중입니다.'
        },
        <#if useCustomButton>
            oTableTools: {aButtons: <#nested "buttons">},
        </#if>
        aaSorting: <#nested "order">,
        aoColumns: <#nested "columns">
    });

    $("#${listId} tbody").on("click", ".datatable-row-detail-view", function(e) {
        <@jsevent_prevent />
        var $this = $(this), move = $this.data('move'), nav = $this.data('nav');
        my.fn.pmove(move, nav);
    });

    <#-- 통합 search input 에 tooltip 추가 -->
    <#if searchTooltip != "">
        $('#${listId}_filter').find('input[type=search]').attr({
            'rel': 'tooltip',
            'data-placement': 'top',
            'data-original-title': '${searchTooltip}'}).tooltip();
    </#if>

    <#if useCheckBox>
        <#-- 컨텐츠 디테일 뷰 링크를 누르면 row 선택하지 않고 디테일 뷰 화면으로 이동 변경 된다 -->
        $("#${listId} tbody").on("click", ".datatable-row-detail-view", function(e) {
            <@jsevent_prevent />
            var $this = $(this), move = $this.data('move'), nav = $this.data('nav');
            my.fn.pmove(move, nav);
        });

        <#-- row select 표시 -->
        $('#${listId} tbody').on('click', 'tr', function(){
            var $this = $(this);
            var selected = $this.hasClass('selected');

            <#-- row 선택을 토글 시키고, checkbox 를 on/off 시킨다. -->
            $this.toggleClass('selected');
            $this.find('.check-row-data').each(function(){
                var $checkbox = $(this);
                    if(selected)    $checkbox.prop('checked', false);
                    else            $checkbox.prop('checked', true);
            });
        });

        <#-- checkbox -->
        $('#${listId} thead th input[type=checkbox]').on('change', function() {
            if($(this).is(':checked')) {
                $('.check-row-data').prop('checked', true);
                $('#${listId} tbody tr').addClass('selected');
            } else {
                $('.check-row-data').prop('checked', false);
                $('#${listId} tbody tr').removeClass('selected');
            }
        });
    </#if>
</#macro>

<#--
    게시판 관리 페이지에 들어가는 초소형 테이블. 앱 목록에서 사용함.
-->
<#macro jsdatatable_very_simple1 listId="datatable_list" url="" tableVarName="otable"
        responsiveHelper="responsiveHelper_list">
    <#--
    // DOM Position key index //

    l - Length changing (dropdown)
    f - Filtering input (search)
    t - The Table! (datatable)
    i - Information (records)
    p - Pagination (paging)
    r - pRocessing
    < and > - div elements
    <"#id" and > - div with an id
    <"class" and > - div with a class
    <"#id.class" and > - div with an id and class

    Also see: http://legacy.datatables.net/usage/features
    -->
    var ${tableVarName} = $('#${listId}').DataTable({
        <#--
            "bFilter": false,
            "bInfo": false,
            "bLengthChange": false
            "bAutoWidth": false,
            "bPaginate": false,
            "bStateSave": true // saves sort state using localStorage
        -->
        sDom: "t" +
              "<'dt-toolbar-footer'<'col-sm-4 col-xs-12'l><'col-xs-12 col-sm-8'p>>",
        autoWidth: true,
        preDrawCallback: function() {
            <#-- Initialize the responsive datatables helper once. -->
            if (!${responsiveHelper}) {
                ${responsiveHelper} = new ResponsiveDatatablesHelper($('#${listId}'), breakpointDefinition);
            }
        },
        rowCallback: function(nRow) {
            ${responsiveHelper}.createExpandIcon(nRow);
        },
        drawCallback: function(oSettings) {
            ${responsiveHelper}.respond();
        },
        processing: true,
        serverSide: true,
        ajax: {
            url: '${url}'+ $.now(),
            type: 'POST'
        },
        <#-- saves sort state using localStorage -->
        bStateSave: true,
        <#-- Define the starting point for data display when using DataTables with pagination -->
        iDisplayStart: 0,
        <#-- Number of rows to display on a single page when using pagination -->
        iDisplayLength: 5,
        aLengthMenu: [5, 10, 25],
        <#--
            simple : 'Previous' and 'Next' buttons only
            simple_numbers : 'Previous' and 'Next' buttons, plus page numbers
            full : 'First', 'Previous', 'Next' and 'Last' buttons
            full_numbers : 'First', 'Previous', 'Next' and 'Last' buttons, plus page numbers
        -->
        pagingType: 'full',
        language: {
            paginate: {
                first: '<i class="fa fa-angle-double-left"></i>',
                last: '<i class="fa fa-angle-double-right"></i>',
                next: '<i class="fa fa-angle-right"></i>',
                previous : '<i class="fa fa-angle-left"></i>'
            },
            zeroRecords: '데이터 없음',
            emptyTable: '데이터 없음',
            info: '_START_ - _END_ (_TOTAL_/_MAX_)',
            infoFiltered : '',
            infoEmpty: '0 - 0 (0/0)',
            processing: '로딩중',
            loadingRecords: '로딩중'
        },
        aaSorting: <#nested "order">,
        aoColumns: <#nested "columns">
    });
</#macro>

<#--
    jquery datatable 울 출력 한다.(테이블 헤더 위에 검색 항목이 추가된 테이블 형태)
    col-6 너비에 들어가는 테이블 이다.
    list_app.ftl 을 참고 하면 된다.

    [nested]
    nested "order"      : 최초의 ordering. [[1, 'desc']] 와 같은 형태가 되어야 함. array.
    nested "columns"    : 테이블의 column 정보. aoColumns 값임. array

    [parameter]
    listId              : 테이블의 id
    url                 : ajax url. url 은 't/'로 끝나야 하고 자동으로 tid 를 붙인다.
    searchTooltip       : 좌측 상단 search 의 tooltip. 만일 값이 없으면 tooltip 없다.
-->
<#macro jsdatatable_simple_sm listId="datatable_list" url="" searchTooltip="" tableVarName="otable">
    <#--
    // DOM Position key index //

    l - Length changing (dropdown)
    f - Filtering input (search)
    t - The Table! (datatable)
    i - Information (records)
    p - Pagination (paging)
    r - pRocessing
    < and > - div elements
    <"#id" and > - div with an id
    <"class" and > - div with a class
    <"#id.class" and > - div with an id and class

    Also see: http://legacy.datatables.net/usage/features
    -->
    var responsiveHelper_list = undefined;
    var breakpointDefinition = {
        tablet : 1024,
        phone : 480
    };

    var ${tableVarName} = $('#${listId}').DataTable({
        <#--
            "bFilter": false,
            "bInfo": false,
            "bLengthChange": false
            "bAutoWidth": false,
            "bPaginate": false,
            "bStateSave": true // saves sort state using localStorage
        -->
        sDom: "<'dt-toolbar'<'col-xs-12 col-sm-6 hidden-xs'f><'font-sm txt-color-greenLight col-sm-4 col-xs-12 hidden-xs'r><'col-sm-2 col-xs-12 hidden-xs'l>>" +
              "t" +
              "<'dt-toolbar-footer'<'col-sm-4 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-8'p>>",
        autoWidth: true,
        preDrawCallback: function() {
            <#-- Initialize the responsive datatables helper once. -->
            if (!responsiveHelper_list) {
                responsiveHelper_list = new ResponsiveDatatablesHelper($('#${listId}'), breakpointDefinition);
            }
        },
        rowCallback: function(nRow) {
            responsiveHelper_list.createExpandIcon(nRow);
        },
        drawCallback: function(oSettings) {
            responsiveHelper_list.respond();
        },
        processing: true,
        serverSide: true,
        ajax: {
            url: '${url}'+ $.now(),
            type: 'POST'
        },
        <#-- saves sort state using localStorage -->
        bStateSave: true,
        <#-- Define the starting point for data display when using DataTables with pagination -->
        iDisplayStart: 0,
        <#-- Number of rows to display on a single page when using pagination -->
        iDisplayLength: 10,
        aLengthMenu: [10, 25, 50, 75, 100],
        language: {
            paginate: {
                first: '처음',
                last: '마지막',
                next: '다음',
                previous : '이전'
            },
            zeroRecords: '검색된 데이터가 없습니다.',
            emptyTable: '데이터가 없습니다.',
            info: '_START_ - _END_ (_TOTAL_/_MAX_)',
            infoFiltered : '',
            infoEmpty: '0 - 0 (0/0)',
            processing: '가져오는 중입니다.',
            loadingRecords: '로딩 중입니다.'
        },
        aaSorting: <#nested "order">,
        aoColumns: <#nested "columns">
    });

    $("#${listId} tbody").on("click", ".datatable-row-detail-view", function(e) {
        <@jsevent_prevent />
        var $this = $(this), move = $this.data('move'), nav = $this.data('nav');
        my.fn.pmove(move, nav);
    });

    <#-- 통합 search input 에 tooltip 추가 -->
    <#if searchTooltip != "">
        $('#${listId}_filter').find('input[type=search]').attr({
            'rel': 'tooltip',
            'data-placement': 'top',
            'data-original-title': '${searchTooltip}'}).tooltip();
    </#if>
</#macro>

<#--
    jquery datatable 울 출력 한다.(테이블 헤더 위에 검색 항목이 추가된 테이블 형태)
    list_app.ftl 을 참고 하면 된다.

    [nested]
    nested "buttons"    : 버튼이 존재하면 존재해야 하는 nested. oTableTools.aButtons 값임. array.
    nested "order"      : 최초의 ordering. [[1, 'desc']] 와 같은 형태가 되어야 함. array.
    nested "columns"    : 테이블의 column 정보. aoColumns 값임. array

    [parameter]
    listId              : 테이블의 id
    useCustomButton     : 테이블 상단에 버튼을 사용하는지 여부.
    url                 : ajax url. url 은 't/'로 끝나야 하고 자동으로 tid 를 붙인다.
    useCheckBox         : 테이블 첫번째 줄에 checkbox 를 사용하는지 여부
    useSearchText       : 테이블 상단에 텍스트 검색을 사용하는지 여부
    useSearchSelect     : 테이블 상단에 실랙트 박스 검색을 사용하는지 여부
-->
<#macro jsdatatable listId="datatable_list" useCustomButton=true url="" useCheckBox=true
        useSearchText=true useSearchSelect=true tableVarName="otable">
    <#--
    // DOM Position key index //

    l - Length changing (dropdown)
    f - Filtering input (search)
    t - The Table! (datatable)
    i - Information (records)
    p - Pagination (paging)
    r - pRocessing
    < and > - div elements
    <"#id" and > - div with an id
    <"class" and > - div with a class
    <"#id.class" and > - div with an id and class

    Also see: http://legacy.datatables.net/usage/features
    -->
    my.fn.setDatatableSearchValue('${listId}');

    var responsiveHelper_list = undefined;
    var breakpointDefinition = {
        tablet : 1024,
        phone : 480
    };

    var ${tableVarName} = $('#${listId}').DataTable({
        <#--
            "bFilter": false,
            "bInfo": false,
            "bLengthChange": false
            "bAutoWidth": false,
            "bPaginate": false,
            "bStateSave": true // saves sort state using localStorage
        -->
        <#if useCustomButton>
            sDom: "<'dt-toolbar'<'col-xs-12 col-sm-3 hidden-xs'T><'font-sm txt-color-greenLight col-sm-6 col-xs-12 hidden-xs'r><'col-sm-3 col-xs-12 hidden-xs'l>>" +
        <#else>
            sDom: "<'dt-toolbar'<'col-xs-12 col-sm-3 hidden-xs'><'font-sm txt-color-greenLight col-sm-6 col-xs-12 hidden-xs'r><'col-sm-3 col-xs-12 hidden-xs'l>>" +
        </#if>
              "t" +
              "<'dt-toolbar-footer'<'col-sm-6 col-xs-12 hidden-xs'i><'col-xs-12 col-sm-6'p>>",
        autoWidth: true,
        preDrawCallback: function() {
            <#-- Initialize the responsive datatables helper once. -->
            if (!responsiveHelper_list) {
                responsiveHelper_list = new ResponsiveDatatablesHelper($('#${listId}'), breakpointDefinition);
            }
        },
        rowCallback: function(nRow) {
            responsiveHelper_list.createExpandIcon(nRow);
        },
        drawCallback: function(oSettings) {
            responsiveHelper_list.respond();
        },
        processing: true,
        serverSide: true,
        ajax: {
            url: '${url}'+ $.now(),
            type: 'POST'
        },
        <#-- saves sort state using localStorage -->
        bStateSave: true,
        <#-- Define the starting point for data display when using DataTables with pagination -->
        iDisplayStart: 0,
        <#-- Number of rows to display on a single page when using pagination -->
        iDisplayLength: 25,
        aLengthMenu: [25, 50, 75, 100],
        language: {
            paginate: {
                first: '처음',
                last: '마지막',
                next: '다음',
                previous : '이전'
            },
            zeroRecords: '검색된 데이터가 없습니다.',
            emptyTable: '데이터가 없습니다.',
            info: '필터링 된 _TOTAL_개에서 _START_번에서 _END_번까지 리스트입니다.',
            infoFiltered : '(총 _MAX_개의 데이터가 있습니다.)',
            infoEmpty: '필터링 된 0개에서 0번에서 0번까지 리스트입니다.',
            processing: '데이터를 가져오는 중입니다.',
            loadingRecords: '데이터를 로딩 중입니다.'
        },
        <#if useCustomButton>
            oTableTools: {aButtons: <#nested "buttons">},
        </#if>
        aaSorting: <#nested "order">,
        aoColumns: <#nested "columns">
    });

    <#if useCheckBox>
        <#-- 컨텐츠 디테일 뷰 링크를 누르면 row 선택하지 않고 디테일 뷰 화면으로 이동 변경 된다 -->
        $("#${listId} tbody").on("click", ".datatable-row-detail-view", function(e) {
            <@jsevent_prevent />
            var $this = $(this), move = $this.data('move'), nav = $this.data('nav');
            my.fn.pmove(move, nav);
        });

        <#-- row select 표시 -->
        $('#${listId} tbody').on('click', 'tr', function(){
            var $this = $(this);
            var selected = $this.hasClass('selected');

            <#-- row 선택을 토글 시키고, checkbox 를 on/off 시킨다. -->
            $this.toggleClass('selected');
            $this.find('.check-row-data').each(function(){
                var $checkbox = $(this);
                if(selected)    $checkbox.prop('checked', false);
                else            $checkbox.prop('checked', true);
            });
        });

        <#-- checkbox -->
        $('#${listId} thead th input[type=checkbox]').on('change', function() {
            if($(this).is(':checked')) {
                $('.check-row-data').prop('checked', true);
                $('#${listId} tbody tr').addClass('selected');
            } else {
                $('.check-row-data').prop('checked', false);
                $('#${listId} tbody tr').removeClass('selected');
            }
        });
    </#if>

    <#if useSearchText>
        <#-- input text 로 검색 처리 -->
        var timeout1, delay = 300;
        $('#${listId} thead th input[type=text]').on('input keyup change', function() {
                var $this = $(this), value = this.value;
                clearTimeout(timeout1);
                timeout1 = setTimeout(function(){
                ${tableVarName}.column($this.parent().index()+':visible').search( value ).draw();
            }, delay);
        });
    </#if>

    <#if useSearchSelect>
        <#-- select 로 검색 처리 -->
        $('#${listId} thead th select').on('change', function() {
            ${tableVarName}.column($(this).parent().index()+':visible').search( this.value ).draw();
        });
    </#if>
</#macro>

<#--
    x-editable 을 active 시킨다. f_app/detail_app.ftl 참고.

    * select2(tag 방식) 을 사용 할때 주의 사항
      - 보여주는 값은 하나를 사용함. 또한 그 하나가 unique 한 값이 되어야 함. x-editable 과 엮이면서 구분해서 사용 불가능.
        (만일 구분 해서 사용해야 한다면 x-editable select2 를 사용하기가 힘듬)
        키의 이름은 freemarker macro 변수 select2Id 로 설정 하면 된다.
      - 서버의 request parameter 는 query, limit, offset 세 개가 올라간다. 파라미터 이름은 고정 시켜둠.
      - 한번에 내려오는 데이터 양은 my.data.select2PageRows 에 설정 해 둠.
      - 서버 response 에서 데이터 리스트는 list 이름으로 넘겨 줘야 함. 역시 고정 시켜 둠. list: [{a:b, c:d}] 형태.
      - 서버 response 에서 전체 데이터 개수는 recordsTotal 이름으로 넘겨야 함.


    [nested]
    text 일 경우   : x-editable validate function. validate 에 들어갈 function 값.
    select 일 경우 : select 소스(select 에 들어갈 값들). select 는 값을 지정해 주기 때문에 validate 할 필요 없음.
                   source 에 들어갈 array list
    select2 일 경우: x-editable validate function. validate 에 들어갈 function 값

    [parameter]
    id          : x-editable 로 동작 시킬 요소의 id
    uri         : 값 수정을 반영할 request uri. 마지막에 /t/ 로 완료 되어야 하며, tid 값은 자동으로 삽입 된다.
    method      : request method
    type        : x-editable 종류. text, select, select2
    select2id   : select2 형태에서 사용할 서버에서 받은 데이터의 구분 아이디 겸 값
    select2uri  : select2 에서 서버로 데이터 요청할 request uri
    onlyNumber  : type 이 text 일때 숫자로만 이루어진 text 를 지원하기 위해서 사용됨.(insert 는 막지 않고 result 표시용)
                  true 업데이트 이후에 결과값을 숫자만 표시함. 비숫자는 replaceAll 로 모두 empty string 으로 교체함.
    minimumInputLength : select2 를 사용할때 입력해야 할 최소 문자 길이
    isselect2tag: select2 를 사용 할때 입력창이 태그 형태 인지 단일 선택 인지 여부. true 라면 tag라면 태그 형태.
    select2showkey   : select2 사용시 보여주는 내용의 object key
-->
<#macro jsxeditable id="" uri="" method="PUT" type="text" select2id="" select2uri="" onlyNumber=false
        isPassword=false minimumInputLength=3 isselect2tag=true select2showkey="">
    <#if id == "" || uri == ""><#return></#if>

    $('#${id}').editable({
        url: '${uri}' + $.now(),
        ajaxOptions: {
            type: '${method}',
            dataType: 'json',
            beforeSend: function(request) {
                <@jsajax_json_header />
            }
        },
        success: function(response, value) {
            if(response.ck_error && response.ck_error != my.data.ajaxSuccessCode && response.ck_message) {
                console.warn('success but return error code [' + response.ck_error + ']');
                return response.ck_message;
            }

            <#if type == "select2" && !isselect2tag>
                <#--
                    x-editable select2 의 tag 모드가 아닐때는 내부에서 정해진 형태로 따라야 하기 때문에
                    수동으로 display 를 처리 하도록 한다.
                -->
                var $this = $(this),
                    readyValue = $this.attr('data-trick-ready');
                $this.attr('data-trick-value', readyValue);
            </#if>
        },
        params: function(params) {
            var name = params.name.slice(0), value = params.value.slice(0), m_key = {};
            <#if isPassword>
                value = CryptoJS.MD5(value).toString();
            </#if>
            m_key[name] = value;
            params = {m_key: m_key};

            return JSON.stringify(params);
        },
        error: function(response, value) {
            console.log('responseText[' + response.responseText + ']');
            console.log('status [' + response.status + ']');

            if(response.responseJSON && response.responseJSON.ck_error &&
                    response.responseJSON.ck_message) {
                return response.responseJSON.ck_message;
            }

            <#-- 만일 인증 시간 만료 라면 에러 메시지 보여주고 로그인 페이지로 이동 시킴 -->
            if(response.status == 901) {
                return '인증 시간이 만료 되었습니다.';
            }
            return '네트웍/서버 에러로 인해 수정 실패 했습니다.';
        },
        <#if type == "text">
            <#if onlyNumber == true>
                display: function(value) {
                    $(this).text(my.fn.getPhoneNumber($.trim(value)));
                },
            </#if>
            emptytext: '설정된 값이 없습니다.',
            validate: <#nested >
        <#elseif type == "select">
            source: <#nested >
        <#elseif type == "select2">
            inputclass: 'input-large',
            <#if !isselect2tag>
            <#--
                x-editable select2 의 tag 모드가 아닐때는 내부에서 정해진 형태로 따라야 하기 때문에
                수동으로 display 를 처리 하도록 한다.
            -->
            display: function() {
                var $this = $(this);
                $this.text($this.attr('data-trick-value'));
            },
            </#if>
            select2: {
                language: 'ko',
                minimumInputLength: ${minimumInputLength},
                id: function(data) { return data.${select2id}; },
                ajax: {
                    url: '${select2uri}' + $.now(),
                    <#-- select2 는 request content-type 을 application/json 으로 간단히 바꾸기가 힘듬. 필요하면 소스 보자. -->
                    type: 'POST',
                    dataType: 'json',
                    data: function (term, page) {
                        var offset = (page - 1) * my.data.select2PageRows;
                        var limit = my.data.select2PageRows;
                        return {query: term, offset:offset, limit:limit};
                    },
                    results: function (data, page) {
                        var more = (page * my.data.select2PageRows) < data.recordsTotal;
                        return {results: data.list, more:more};
                    }
                },
            <#if isselect2tag>
                tags: true,
                tokenSeparators: [",", " "],
                formatResult: function (item) { return item.${select2id}; },
                formatSelection: function (item) { return item.${select2id}; },
                initSelection: function (element, callback) {
                    var saved = element.val();
                    if($.trim(saved) == '') return;
                    var arr = saved.split(','), initValue = [];
                    for(var i=0 ; i<_.size(arr) ; i++) initValue.push({${select2id}: arr[i]});
                    callback(initValue);
                }
            <#else>
                width: 200,
                formatResult: function (item) {
                    <#if select2showkey == "">
                        return item.${select2id};
                    <#else>
                        return item.${select2showkey};
                    </#if>
                },
                formatSelection: function (item) {
                    <#--
                        x-editable select2 의 tag 모드가 아닐때는 내부에서 정해진 형태로 따라야 하기 때문에
                        수동으로 display 를 처리 하도록 한다.
                    -->
                    var $element = $('#${select2id}');
                    <#if select2showkey == "">
                        $element.attr('data-trick-ready', item.${select2id});
                        return item.${select2id};
                    <#else>
                        $element.attr('data-trick-ready', item.${select2showkey});
                        return item.${select2showkey};
                    </#if>
                }
            </#if>
            },
            validate: <#nested >
        </#if>
    });

    <#if isPassword>
        $('#${id}').text('[hidden]');
    </#if>
</#macro>

<#--
    해당 업로더는 bstrap_in_txt_rbtn 과 같이 사용 하는 것임.
    단독으로 사용 하려면 macro 를 따로 만들도록 하자.

    파일을 선택 하고 파일 업로드 버튼을 눌러야 파일 업로드 되는 수동적인 방식이 아닌
    파일을 선택 하면 곧바로 서버로 파일 업로드 하는 active 방식의 파일 업로더 사용.
    push_gcm_apns_form.ftl 참고.

    ajax 리턴값은 domain, uri 를 포함해야 한다.

    [nested]
    파일 업로드 이후 할 후속 작업

    [Parameter]
    id          : fileuploader 로 동작 시킬 요소의 id. input file 폼의 id 임.
    uri         : 값 수정을 반영할 request uri. 마지막에 /t/ 로 완료 되어야 하며, tid 값은 자동으로 삽입 된다.
    showid      : input file 은 숨겨 두고 input text 에서 작동 시키기 때문에 input file 대신 보여지는
                  input text 의 name
    isImage     : 이미지 파일 업로드 여부. 이미지 파일 파일 확장자 체크용
    fileMaxSize : 업로드 되는 파일의 최대 파일 사이즈. byte 단위임. 최소 1M 이상은 줘야 에러 메시지가 정상 처리됨.
-->
<#macro bstrap_in_txt_rbtn_uploader id="" uri="" showid="" isImage=true fileMaxSize=5000000>
    <#if id == "" || uri == ""><#return></#if>

    $('#${id}').fileupload({
        url: '${uri}'+ $.now(),
        //sequentialUploads: true,
        replaceFileInput: false,
        dataType: 'json',
        add: function(e, data) {
            var $elementWrapper = $('#errid-${showid}'),
                $elementText = $('#errmsg-${showid}'),
                file = data.files[0];

            $elementWrapper.hide();

            <#-- 만일 이미지 업로더라면 지원되는 이미지 파일 확장자 체크 -->
            <#if isImage == true>
                <#--if(!(/png|jpe?g|gif/i).test(file.name)) {-->
                if(!(/png|jpe?g/i).test(file.name)) {
                    $elementText.text('이미지 파일만 업로드 가능 합니다.');
                    $elementWrapper.show();
                    return;
                }
            </#if>

            if(file.size > ${fileMaxSize}) {
                $elementText.text('파일 사이즈는 ${(fileMaxSize / 1000000)?string("0.#")}M 이하만 가능 합니다.');
                $elementWrapper.show();
                return;
            }

            <#-- 업로드 버튼을 disabled 시킨다. -->
            $('input[name=${showid}]').next().find('button').addClass('disabled');

            data.submit();
        },
        done: function(e, data) {
            var $targetInput = $('input[name=${showid}]');
            $targetInput.next().find('button').removeClass('disabled');

            var result = data.result;

            if(!result || !result.ck_error || !result.ck_message) {
                $('#errmsg-${showid}').text('파일 업로드를 실패 했습니다.');
                $('#errid-${showid}').show();
                return;
            }

            if(result.ck_error != my.data.ajaxSuccessCode) {
                $('#errmsg-${showid}').text(result.ck_message);
                $('#errid-${showid}').show();
                return;
            }

            <@jsnoti title="파일 업로드" content="업로드 성공 했습니다." contenttype="val"
                    boxtype="info" />

            var _preview = result.domain + result.uri + '?w=50&h=50';

            $targetInput.val(result.domain + result.uri);
            <#-- 이미지가 deleted 로 생성되기 때문에 미리 이미지를 보여 줄 수 없다.
            <#if isImage == true>
                $targetInput.attr({
                    'rel': 'tooltip',
                    'data-placement': 'top',
                    'data-original-title': '<img src="' + _preview + '">',
                    'data-html': 'true'}).tooltip();
            </#if>
            -->
            <#nested >
        },
        fail: function(e, data) {
            console.warn('file upload error');
            console.warn(data);

            $('#errmsg-${showid}').text('파일 업로드를 실패 했습니다.');
            $('#errid-${showid}').show();
            $('input[name=${showid}]').next().find('button').removeClass('disabled');
        }
    });
</#macro>

<#--
    모달 윈도우를 띄운다. 모달에 포함되는 내용은 nested로 변경 가능 하다.

    [nested]
    modal 의 body에 들어가는 내용

    [parameter]
    modalId         : modal 의 id
    formId          : modal 의 form id
    title           : modal 의 타이틀 문자열
    okButtonLabel   : modal 의 ok 버튼 문자열

-->
<#macro bstrap_modal modalId="normal-remote-modal" formId="normal-remote-form" title="Title" okButtonLabel="네"
        okButtonId="modal-ok">
    <form id="${formId}">
        <div class="modal fade" id="${modalId}" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">${title}</h4>
                    </div>
                    <div class="modal-body">
                        <#nested>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                        <button id="${okButtonId}" type="button" class="btn btn-primary" data-dismiss="modal">${okButtonLabel}</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</#macro>

<#--
    파일 업로드를 위한 model 을 추가 한다.

    [parameter]
    modalId         : file uploader modal id
    formId          : file uploader form id
-->
<#macro bstrap_file_uploader_modal modalId="file-uploader-remote-modal" formId="file-uploader-form" url="">
    <form id="${formId}" action="${url}" method="POST" enctype="multipart/form-data">
        <div class="modal fade" id="${modalId}" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">파일 업로드</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-12">
                                <fieldset class="smart-form">
                                    <section>
                                        <label class="label">파일 선택</label>
                                        <label for="file" class="input input-file">
                                            <div class="button"><input type="file" name="attach_file" onchange="this.parentNode.nextSibling.value = this.value">브라우저</div><input type="text" placeholder="파일을 선택하세요" readonly="">
                                        </label>
                                    </section>
                                    <section>
                                        <label class="label">파일 설명</label>
                                        <label class="textarea">
                                            <textarea rows="3" name="file_comment" class="custom-scroll"></textarea>
                                        </label>
                                    </section>
                                </fieldset>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                        <input type="submit" value="업로드" class="btn btn-primary">
                    </div>
                </div>
            </div>
        </div>
    </form>
</#macro>

<#--
    파일 업로드를 위한 modal 의 ajax를 추가 한다.(bstrap_file_uploader_modal 와 쌍으로 사용하는 것임)
    modealId, formId 는 쌍으로 존재하는 bstrap_file_uploader_modal 에서 사용한 것과 동일하게 사용 해야 한다.

    [parameters]
    modalId         : file uploader modal id
    formId          : file uploader form id
    fileAreaId      : 첨부 파일 영역 아이디
    fileContainer   : 첨부 파일이 포함되는 영역
    isModify        : 게시물 신규 작성시 사용인지 수정시 사용인지 여부. true 라면 수정, false 라면 신규 작성
-->
<#macro bstrap_file_uploader_ajax formId="file-uploader-form" modalId="file-uploader-remote-modal"
        fileAreaId="attach-file-area" fileContainer="$attachFileContainer" isModify=false>
    $('#${formId}').ajaxForm({
        beforeSend: function(request) {
            request.setRequestHeader('Accept', 'application/json');
            request.setRequestHeader('X-Tid', $.now());
            <#-- 업로드 요청 하기 전에 파일 업로드 창을 닫는다. -->
            $('#${modalId}').modal('hide');
        },
        success: function(data, status, xhr) {
            if(data.ck_error != 'S000001') {
                <@jsnoti title="파일 업로드 실패" content="파일 업로드를 실패 했습니다." contenttype="text" />
                return;
            }

            <@jsnoti title="파일 업로드 성공" content="파일 업로드를 완료 하였습니다." contenttype="text" boxtype="info" />

            //if($attachFileContainer.children().length <= 0) $('#attach-file-area').show();
            $('#${fileAreaId}').show();

            var obj = {
                file_srl: data.file_srl,
                file_url: data.domain + data.uri,
                orig_name: data.orig_name,
                width: 120, height: 150
            };
            var mega = Math.floor(data.file_size / 1000000);
            if(mega > 0) { obj.file_size = mega + ' mb'; }
            else {
                var kilo = Math.floor(data.file_size / 1000);
                if(kilo > 0) { obj.file_size = kilo + ' kb'; }
                else { obj.file_size = data.file_size + ' b'; }
            }

            var isImage = false;
            if(data.mime_type && (data.mime_type).indexOf('image') != -1) isImage = true;

            my.fn.addDocumentAttachToDocument(${fileContainer}, obj, isImage,
                <#if isModify>true<#else>false</#if>
            );
        },
        error: function(xhr, status, error) {
            <@jsnoti title="파일 업로드 실패" content="파일 업로드를 실패 했습니다." contenttype="text" />
        }
    });
</#macro>

<#--
    bstrap_select2 와 엮여 있는 select2 javascript 를 추가 한다.
    push_gcm_apns_form.ftl 참고

    * select2 을 사용 할때 주의 사항
      - 보여주는 값은 하나를 사용함. 또한 그 하나가 unique 한 값이 되어야 함.
        키의 이름은 freemarker macro 변수 select2Id 로 설정 하면 된다.
      - 서버의 request parameter 는 query, limit, offset 세 개가 올라간다. 파라미터 이름은 고정 시켜둠.
      - 한번에 내려오는 데이터 양은 my.data.select2PageRows 에 설정 해 둠.
      - 서버 response 에서 데이터 리스트는 list 이름으로 넘겨 줘야 함. 역시 고정 시켜 둠. list: [{a:b, c:d}] 형태.
      - 서버 response 에서 전체 데이터 개수는 recordsTotal 이름으로 넘겨야 함.

    [parameter]
    id              : select2 dom 의 id
    placeholder     : placeholder
    uniq            : select2 형태에서 사용할 서버에서 받은 데이터의 구분 아이디 겸 값
    showColumn      : select2 형태에서 사용할 서버에서 받은 데이터 표시(uniq과 구분을 위해서 추가함)
    uri             : select2 에서 서버로 데이터 요청할 request uri
    miniumLength    : 입력값의 최소 길이
    usingTag        : 태그 형태로 결과 표시 할지 여부. true 이면 태그 형태로 표시. false 이면 일반.
    createSearchChoice  : 검색 되지 않는 입력에 대해서 입력을 원할때 사용하는 function name.
                          게시물 태그 입력에서 사용하고 있음.
    initSelection   : 초기값 설정
-->
<#macro jsselect2 id="" placeholder="" uniq="" showColumn="" uri="" miniumLength=3 usingTag=false
        createSearchChoice="" initSelection="">
    $('#${id}').select2({
        <#if usingTag>
            tags: true,
        </#if>
        <#if createSearchChoice != "">
            createSearchChoice: ${createSearchChoice},
        </#if>
        language: 'ko',
        placeholder: '${placeholder}',
        minimumInputLength: ${miniumLength},
        id: function(data) { return data.${uniq}; },
        ajax: {
            url: '${uri}' + $.now(),
            type: 'POST',
            dataType: 'json',
            //quietMillis: 250,
            data: function(term, page) {
                var offset = (page - 1) * my.data.select2PageRows;
                var limit = my.data.select2PageRows;
                return {query: term, offset:offset, limit:limit};
            },
            results: function(data, page) {
                var more = (page * my.data.select2PageRows) < data.recordsTotal;
                return {results: data.list, more:more};
            }
        },
        <#if initSelection != "">
            initSelection: ${initSelection},
        </#if>
        <#if showColumn == "">
            formatResult: function (item) { return item.${uniq}; },
            formatSelection: function (item) { return item.${uniq}; }
        <#else>
            formatResult: function (item) { return item.${showColumn}; },
            formatSelection: function (item) { return item.${showColumn}; }
        </#if>
    });

    <#-- 한번 건드려 주지 않으니 초기값 넣는 것이 나오지 않는다. val을 한번 건드려 주자 -->
    <#if initSelection != "">
        $('#${id}').select2('val', []);
    </#if>

</#macro>

<#--
    게시물 템플릿 구성을 위한 입력창을 추가 한다. backoffice에서만 사용되는 전용 매크로.

    [parameter]
    addButtonId     : 추가 버튼의 id
    typeShowId      : 템플릿 요소 선택한 영역의 id
    keyInputId      : 템플릿 요소 변수값 입력 영역의 id
    widthClass      : 너비
-->
<#macro bfs_input_template_element addButtonId="add-template-element" typeShowId="selected-template-element"
        keyInputId="template-element-name" widthClass="col-sm-6">
    <div class="${widthClass}">
        <div class="form-group">
            <label>템플릿 요소 선택</label>
            <div class="row">
                <div class="col-sm-12">
                    <div class="input-group">
                        <div class="input-group-btn">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" tabindex="-1">
                                <span class="caret"></span>
                            </button>
                            <ul id="menu-template-element" class="dropdown-menu" role="menu">
                                <li><a href="javascript:void(0);" data-element="array">배열</a></li>
                                <li><a href="javascript:void(0);" data-element="date">날짜</a></li>
                                <li><a href="javascript:void(0);" data-element="time">시간</a></li>
                                <li><a href="javascript:void(0);" data-element="datetime">날짜/시간</a></li>
                                <li><a href="javascript:void(0);" data-element="text">문자열</a></li>
                                <li><a href="javascript:void(0);" data-element="textarea">문자영역</a></li>
                                <li class="divider"></li>
                                <li><a href="javascript:void(0);" data-element="close">닫기</a></li>
                            </ul>
                            <button id="${typeShowId}" data-element="array" type="button" class="btn btn-default disabled" style="color: #333; border-color: #ccc;">배열</button>
                        </div>
                        <input id="${keyInputId}" type="text" class="form-control" style="border-left-color: #ccc;" placeholder="요소의 변수명을 입력하세요" />
                        <span class="input-group-btn">
                            <button id="${addButtonId}" class="btn btn-default" type="button">추가</button>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</#macro>

<#--
    bfs_input_template_element 매크로를 이용하여 추가한 요소들을 보여줄 템플릿 구성 뷰를 추가 한다.

    [parameter]
    templateStructId        : 템플릿 구성 view(netable list)의 아이디
    templateStructInnerId   : 템플릿 구성 view 내부 ol 태그의 아이디
    placeHolderId           : 템플릿 구성이 없을때 보여줄 임의의 텍스트 안내창 아이디
    widthClass              : 너비
    hideEmpty               : 구성된 템플릿이 없을때 보여주는 것을 기본으로 on 할지 off 할지 여부
-->
<#macro bfs_template_struct templateStructId="template-struct" templateStructInnerId="template-struct-inner"
        placeHolderId="template-struct-help" widthClass="col-sm-6" hideEmpty=false>
    <div class="${widthClass}">
        <div class="form-group">
            <label>템플릿 구성</label>
            <#if hideEmpty>
                <input id="${placeHolderId}" class="form-control" style="display: none;" disabled="disabled" placeholder="템플릿 요소 선택을 이용하여 템플릿을 구성하세요." type="text">
            <#else>
                <input id="${placeHolderId}" class="form-control" disabled="disabled" placeholder="템플릿 요소 선택을 이용하여 템플릿을 구성하세요." type="text">
            </#if>
            <div class="dd" id="${templateStructId}" style="margin-top: -5px;">
                <ol id="${templateStructInnerId}" class="dd-list"></ol>
            </div>
        </div>
    </div>
</#macro>

<#macro bstrap_popup_tag_modal modalId="target-tag-remote-modal" url="">
<div class="modal fade" id="${modalId}" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">검사지 파일 검색</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <fieldset class="smart-form">
                            <section>
                                <label class="label">검사지 파일</label>
                                <label for="file" class="input input-file">
                                    <div id="file_list" class="select2" style="width:100%;height:30px;"></div>
                                </label>
                            </section>
                        </fieldset>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
                <button id="btn_file_add" type="button" class="btn btn-default" data-dismiss="modal">추가</button>
            </div>
        </div>
    </div>
</div>
</#macro>