<#assign _page_id="manage-board">
<#assign _page_parent_name="커뮤니티">
<#assign _page_current_name="게시판 관리">

<@am.page_simple_depth icon="fa-edit" parent="${_page_parent_name}" current="${_page_current_name}" />

<@am.page_desc title="도움말" id="${_page_id}-helper">
<p>
    게시판 관리 페이지 입니다. 순서 대로 앱 선택, 게시판 선택, 카테고리 선택을 하면 게시물 목록을 확인 할 수 있습니다.
</p>
<p>
    게시판은 활성 여부, 공개 여부 상태를 가질 수 있습니다. 각각의 항목 성격은 다음과 같습니다.(게시판 목록에서 게시판이 비활성이면 D 표시를 하며, 공개용 게시판이면 P 표시를 합니다.)
</p>
<p>
    - 활성 여부 : 일반 사용자에게 게시판을 On, Off 할지 여부. 활성이면 사용자가 접근 할 수 있는 게시판 입니다.<br/>
    - 공개 여부 : 일반 사용자의 서비스 로그인 여부에 따라 접근 허용을 설정 하는 것 입니다. 공개이면 로그인 하지 않아도 접근 가능 합니다.<br/>
</p>
</@am.page_desc>

<div class="row">
    <div class="col-sm-12 col-md-12 col-lg-4">
        <#-- app list -->
        <div class="jarviswidget jarviswidget-color-blueDark">
            <header><h2> 앱 목록</h2></header>
            <div>
                <div class="widget-body no-padding" style="margin-top: -19px;">
                    <table id="${_page_id}-app-list" class="table table-striped table-bordered" width="100%">
                        <thead>
                            <tr>
                                <th data-class="expand" style="text-align: center; max-width: 35px;min-width: 35px;">SN.</th>
                                <th style="text-align: center;">앱</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>

        <#-- board list -->
        <div class="jarviswidget jarviswidget-color-blueDark">
            <header>
                <h2 id="board-list-title"> 게시판 </h2>
                <div class="widget-toolbar" id="board-widget-toolbar" style="display: none;">
                    <!-- add: non-hidden - to disable auto hide -->
                    <div class="btn-group">
                        <button class="btn dropdown-toggle btn-xs btn-default" data-toggle="dropdown">
                            도구 <i class="fa fa-caret-down"></i>
                        </button>
                        <ul class="dropdown-menu js-status-update pull-right">
                            <li><a id="add-board">추가</a></li>
                            <li><a id="modify-board">수정</a></li>
                            <li><a id="remove-board">삭제</a></li>
                        </ul>
                    </div>
                </div>
            </header>
            <div>
                <div id="no-app-select" class="no-padding" style="text-align: center; margin-bottom: 10px;">
                    <span>앱을 선택 하세요</span>
                </div>
                <div id="ok-app-select" class="widget-body no-padding" style="display: none;">
                    <table id="${_page_id}-board-list" class="table table-striped table-bordered" width="100%">
                        <thead>
                        <tr>
                            <th class="hasinput smart-form" data-hide="phone,tablet" style="text-align: center;max-width: 15px;min-width: 15px;">
                                <label class="checkbox input-md">
                                    <input type="checkbox"><i></i>
                                </label>
                            </th>
                            <th data-class="expand" style="text-align: center; max-width: 35px;min-width: 35px;">SN.</th>
                            <th style="text-align: center;">게시판</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>

        <#-- category list -->
        <div class="jarviswidget jarviswidget-color-blueDark">
            <header>
                <h2 id="category-list-title"> 카테고리 </h2>
                <div class="widget-toolbar" id="category-widget-toolbar" style="display: none;">
                    <!-- add: non-hidden - to disable auto hide -->
                    <div class="btn-group">
                        <button class="btn dropdown-toggle btn-xs btn-default" data-toggle="dropdown">
                            도구 <i class="fa fa-caret-down"></i>
                        </button>
                        <ul class="dropdown-menu js-status-update pull-right">
                            <li><a id="add-category">추가</a></li>
                            <li><a id="modify-category">수정</a></li>
                            <li><a id="remove-category">삭제</a></li>
                        </ul>
                    </div>
                </div>
            </header>
            <div>
                <div id="no-board-select" class="no-padding" style="text-align: center; margin-bottom: 10px;">
                    <span>게시판을 선택 하세요</span>
                </div>
                <div id="ok-board-select" class="widget-body no-padding" style="display: none;">
                    <table id="${_page_id}-category-list" class="table table-striped table-bordered" width="100%">
                        <thead>
                            <tr>
                                <th class="hasinput smart-form" data-hide="phone,tablet" style="text-align: center;max-width: 15px;min-width: 15px;">
                                    <label class="checkbox input-md">
                                        <input type="checkbox"><i></i>
                                    </label>
                                </th>
                                <th data-class="expand" style="text-align: center; max-width: 35px;min-width: 35px;">SN.</th>
                                <th style="text-align: center;">카테고리</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>

    </div>

    <#-- document list -->
    <div class="col-sm-12 col-md-12 col-lg-8">
        <div class="jarviswidget jarviswidget-color-blueDark">
            <header>
                <span class="widget-icon"> <i class="fa fa-book"></i> </span>
                <h2 id="document-list-title"> 게시물 </h2>
                <div class="widget-toolbar" id="document-widget-toolbar" style="display: none;">
                    <!-- add: non-hidden - to disable auto hide -->
                    <div class="btn-group">
                        <button class="btn dropdown-toggle btn-xs btn-default" data-toggle="dropdown">
                            도구 <i class="fa fa-caret-down"></i>
                        </button>
                        <ul class="dropdown-menu js-status-update pull-right">
                            <li><a id="add-document">추가</a></li>
                            <li><a id="add-document-link">링크 생성</a></li>
                            <li><a id="remove-document">삭제</a></li>
                        </ul>
                    </div>
                </div>
            </header>
            <div>
                <div id="no-category-select" class="no-padding" style="text-align: center; margin-bottom: 10px;">
                    <span>카테고리를 선택 하세요</span>
                </div>
                <div id="ok-category-select" class="widget-body no-padding" style="display: none;">
                    <table id="${_page_id}-document-list" class="table table-striped table-bordered" width="100%">
                        <thead>
                            <tr>
                                <th class="hasinput smart-form" data-hide="phone,tablet" style="text-align: center;max-width: 15px;min-width: 15px;">
                                    <label class="checkbox input-md">
                                        <input type="checkbox"><i></i>
                                    </label>
                                </th>
                                <th data-class="expand" style="text-align: center; max-width: 35px;min-width: 35px;">SN.</th>
                                <th style="text-align: center;">제목</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<#-- 링크 게시물 생성을 위한 모달 -->
<@am.bstrap_modal modalId="modal-document-link-modal" formId="modal-document-link-form"
        title="링크 게시물 생성" okButtonLabel="생성" okButtonId="make-document-link">
    <div class="well well-sm well-light">
        선택한 게시물의 링크 게시물을 생성 합니다. 링크 게시물은 <b>링크 카테고리</b>에 포함 됩니다. 게시판 및 카테고리 선택은 링크 카테고리의 존재 유무에
        따라서 선택 가능 합니다. 링크 생성 대상이 되는 게시판, 카테고리는 게시물이 포함된 카테고리의 성격에 종속 됩니다.<br/><br/>
        [참고]<br/>
        &nbsp;1. 게시물을 포함하는 카테고리가 공개이면 대상 게시판, 카테고리는 모두 공개 이어야 링크 게시물 포함하는 대상으로 선택 가능<br/>
        &nbsp;2. 게시물을 포함하는 카테고리가 비공개이면 대상 게시판, 카테고리는 모두 비공개 이어야 링크 게시물 포함하는 대상으로 선택 가능
    </div>
    <div class="row">
        <@am.bstrap_in_select name="link_board_srl" title="게시판">
        </@am.bstrap_in_select>
        <@am.bstrap_in_select name="link_category_srl" title="카테고리">
        </@am.bstrap_in_select>

        <#--<div class="col-md-12">-->
            <#--헬로 모달-->
        <#--</div>-->
    </div>
</@am.bstrap_modal>

<#-- 삭제 대기를 위해 화면 전체 block -->
<div id="ckpush-window-block" style="display:none;">
    <h6>삭제 중 입니다. 잠시만 대기 하세요.</h6>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">
    pageSetUp();

    var pagefunction = function() {
        <#-- session time out 시간을 표시 한다. -->
        my.fn.showSessionTime(${sessionRestSec});

        <#-- 페이지 도움말 영역 visible, invisible 처리 -->
        my.fn.initPageHelper('${_page_id}-helper');

        var navHash = $(location).attr('hash').substring(1),
            responsiveHelper_list1 = undefined,
            breakpointDefinition = {
                tablet : 1024,
                phone : 480
            };

        <#-- 앱 리스트 -->
        <@am.jsdatatable_very_simple1 listId="${_page_id}-app-list" url="${Request.contextPath}/admin/app/list/t/"
                tableVarName="otable1" responsiveHelper="responsiveHelper_list1" ; job>
            <#if job == "order">
                [[0, 'desc']]
            <#elseif job == "columns">
                [
                    {sName: 'app_srl', mData: 'app_srl', bSortable: true, bSearchable: false},
                    {sName: 'app_name', mData: 'app_name', bSortable: true, bSearchable: false,
                        mRender: function(data, type, full){
                            return '<a class="datatable-row-detail-view selected-app" data-app-srl="' +
                                    full.app_srl + '">' + data + '</a>';
                        }
                    }
                ]
            </#if>
        </@am.jsdatatable_very_simple1>

        var boardManageData = my.fn.loadLocalStorageForCk('board_manage_data');
        if(!boardManageData) {
            boardManageData = {
                selected_app_srl: 0,
                selected_app_name: '',
                selected_board_srl: 0,
                selected_board_name: '',
                selected_category_srl: 0,
                selected_category_name: '',
                selected_category_type: ${normal_category_type}
            }
        }

        <#-- 게시물 아이템을 클릭 했을때 게시물 상태를 보여 주는 action 처리 -->
        var clickDocumentItem = function(e) {
            <@am.jsevent_prevent />

            var $target = $(e.target),
                documentSrl = $target.attr('data-document-srl'),
                linkFlag = $target.attr('data-link-flag');
            var moveURL = '#${Request.contextPath}/admin/board/document/' +
                    documentSrl + '/link/' + linkFlag;

            <@am.jschangecontent moveuri="moveURL" navuri="navHash" isVarMove=true isVarNav=true />
        };

        var documentListData = {
            tableId: '${_page_id}-document-list',
            responsiveHelper: undefined,
            useCheck: true,
            itemClickAction: clickDocumentItem,
            breakpointDefinition: breakpointDefinition,
            url: '',
            iDisplayLength: 25,
            aLengthMenu: [25, 50, 75, 100],
            pagingType: 'full_numbers',
            defaultOrder: [[1, 'desc']],
            columns: [
                {sName: 'checked', mData: null, bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        if(_.has(full, 'orig_document_srl'))
                            return '<label class="checkbox"><input type="checkbox" class="check-row-data check-document-row-data" data-sn="' +
                                    full.document_srl + '" data-link-flag="${mdv_yes}"><i></i></label>';
                        else
                            return '<label class="checkbox"><input type="checkbox" class="check-row-data check-document-row-data" data-sn="' +
                                    full.document_srl + '" data-link-flag="${mdv_no}"><i></i></label>';
                    }
                },
                {sName: 'document_srl', mData: 'document_srl', bSortable: true, bSearchable: false},
                {sName: 'document_title', mData: 'document_title', bSortable: true, bSearchable: true,
                    mRender: function(data, type, full){
                        if(_.has(full, 'orig_document_srl'))
                            return '<a class="datatable-row-detail-view selected-document" data-document-srl="' +
                                    full.orig_document_srl + '" data-link-flag="${mdv_yes}">' + my.fn.showHTMLTag(data) + '</a>';
                        else
                            return '<a class="datatable-row-detail-view selected-document" data-document-srl="' +
                                    full.document_srl + '" data-link-flag="${mdv_no}">' + my.fn.showHTMLTag(data) + '</a>';
                    }
                }
            ]
        };

        <#-- 만일 기존에 선택된 카테고리가 있다면 그 카테고리의 게시물 리스트를 자동으로 보여 준다 -->
        if(boardManageData.selected_category_srl) {
            $('#no-category-select').hide();
            $('#ok-category-select').show();

            documentListData.responsiveHelper = null;
            documentListData.url = '${Request.contextPath}/admin/board/category/'+boardManageData.selected_category_srl+'/list/t/';

            if(boardManageData.selected_category_type == ${link_category_type}) {
                documentListData.columns[1] = {sName: 'document_link_srl', mData: 'document_srl',
                    bSortable: true, bSearchable: false};
            } else {
                documentListData.columns[1] = {sName: 'document_srl', mData: 'document_srl',
                    bSortable: true, bSearchable: false};
            }

            my.fn.createVerySimleDataTable(documentListData);
            my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);

            if(boardManageData.selected_category_type == ${link_category_type}) {
                $('#add-document').hide();
                $('#add-document-link').hide();
                $('#document-list-title').text('링크 게시물(' + boardManageData.selected_category_name + ')');
            } else {
                $('#add-document').show();
                $('#add-document-link').show();
                $('#document-list-title').text('게시물(' + boardManageData.selected_category_name + ')');
            }

            $('#document-widget-toolbar').show();
        }

        <#-- 카테고리 아이템을 클릭 했을때 게시물 리스트를 보여 주는 action 처리 -->
        var clickCategoryItem = function(e) {
            var $target = $(e.target),
                $nonDocumentList = $('#no-category-select'),
                $documentListWrap = $('#ok-category-select'),
                categorySrl = $target.data('categorySrl'),
                categoryType = $target.data('categoryType'),
                // 태그 표시 span 은 제외
                categoryName = $target.prop('firstChild').nodeValue;

            //if(categoryName.length > 17) categoryName = categoryName.substring(0, 17) + ' ...';

            if($.fn.DataTable.isDataTable('#${_page_id}-document-list')) {
                // 이전 datatable 을 destroy 시키고 storage data도 초기화 시킨다.
                my.fn.removeDatatableStorage('${_page_id}-document-list');
                $('#${_page_id}-document-list').DataTable().destroy();

            } else {
                $nonDocumentList.hide();
                $documentListWrap.show();
            }

            documentListData.responsiveHelper = null;
            documentListData.url = '${Request.contextPath}/admin/board/category/'+categorySrl+'/list/t/';

            if(categoryType == ${link_category_type}) {
                documentListData.columns[1] = {sName: 'document_link_srl', mData: 'document_srl',
                    bSortable: true, bSearchable: false};
            } else {
                documentListData.columns[1] = {sName: 'document_srl', mData: 'document_srl',
                    bSortable: true, bSearchable: false};
            }

            my.fn.createVerySimleDataTable(documentListData);

            boardManageData.selected_category_srl = categorySrl;
            boardManageData.selected_category_name = categoryName;
            boardManageData.selected_category_type = categoryType;
            my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);

            if(categoryType == ${link_category_type}) {
                $('#add-document').hide();
                $('#add-document-link').hide();
                $('#document-list-title').text('링크 게시물('+categoryName+')');
            } else {
                $('#add-document').show();
                $('#add-document-link').show();
                $('#document-list-title').text('게시물('+categoryName+')');
            }

            $('#document-widget-toolbar').show();
        };

        var categoryListData = {
            tableId: '${_page_id}-category-list',
            responsiveHelper: undefined,
            useCheck: true,
            itemClickAction: clickCategoryItem,
            breakpointDefinition: breakpointDefinition,
            url: '',
            defaultOrder: [[1, 'desc']],
            columns: [
                {sName: 'checked', mData: null, bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        return '<label class="checkbox"><input type="checkbox" class="check-row-data check-category-row-data" data-sn="'+full.category_srl+'"><i></i></label>';
                    }
                },
                {sName: 'category_srl', mData: 'category_srl', bSortable: true, bSearchable: false},
                {sName: 'category_name', mData: 'category_name', bSortable: true, bSearchable: true,
                    mRender: function(data, type, full){
                        var badge = '';
                        if(full.enabled == ${mdv_no}) badge = 'D';
                        if(full.open_type == ${mdv_public}) {
                            if(badge == '') badge += 'P';
                            else            badge += ',P';
                        }

                        if(full.category_type == ${link_category_type}) {
                            if(badge == '') badge += 'L';
                            else            badge += ',L';
                        }

                        var badgeSpan = '';
                        if(badge != '') badgeSpan = '<span class="badge pull-right inbox-badge">'+badge+'</span>';

                        return '<a class="datatable-row-detail-view selected-category" data-category-srl="' +
                                full.category_srl + '" data-category-type="' + full.category_type  + '">' + data + badgeSpan +'</a>';
                    }
                }
            ]
        };

        <#-- 만일 기존에 선택된 게시판이 있다면 그 게시판의 카테고리 리스트를 자동으로 보여 준다 -->
        if(boardManageData.selected_board_srl) {
            $('#no-board-select').hide();
            $('#ok-board-select').show();

            categoryListData.responsiveHelper = null;
            categoryListData.url = '${Request.contextPath}/admin/board/' + boardManageData.selected_board_srl + '/category/list/t/';
            my.fn.createVerySimleDataTable(categoryListData);
            my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);

            $('#category-list-title').text('카테고리('+boardManageData.selected_board_name+')');
            $('#category-widget-toolbar').show();
        }

        <#-- 게시판 아이템을 클릭 했을때 카테고리 리스트를 보여 주는 action 처리 -->
        var clickBoardItem = function(e) {
            var $target = $(e.target),
                $nonCategoryList = $('#no-board-select'),
                $categoryListWrap = $('#ok-board-select'),
                boardSrl = $target.data('boardSrl'),
                // 태그 표시 span 은 제외
                boardName = $target.prop('firstChild').nodeValue;

            if(boardName.length > 17) boardName = boardName.substring(0, 17) + ' ...';

            if($.fn.DataTable.isDataTable('#${_page_id}-category-list')) {
                // 이전 datatable 을 destroy 시키고 storage data도 초기화 시킨다.
                my.fn.removeDatatableStorage('${_page_id}-category-list');
                $('#${_page_id}-category-list').DataTable().destroy();

            } else {
                $nonCategoryList.hide();
                $categoryListWrap.show();
            }

            categoryListData.responsiveHelper = null;
            categoryListData.url = '${Request.contextPath}/admin/board/' + boardSrl + '/category/list/t/';

            my.fn.createVerySimleDataTable(categoryListData);

            boardManageData.selected_board_srl = boardSrl;
            boardManageData.selected_board_name = boardName;
            my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);

            $('#category-list-title').text('카테고리('+boardName+')');
            $('#category-widget-toolbar').show();

            <#-- 기존에 보여지고 있는 게시물 리스트는 없앤다 -->
            if($.fn.DataTable.isDataTable('#${_page_id}-document-list')) {
                my.fn.removeDatatableStorage('${_page_id}-document-list');
                $('#${_page_id}-document-list').DataTable().destroy();
            }
            $('#document-list-title').text('게시물');
            $('#ok-category-select').hide();
            $('#no-category-select').show();

            boardManageData.selected_category_srl = 0;
            boardManageData.selected_category_name = '';
            my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);
        };

        var boarListData = {
            tableId: '${_page_id}-board-list',
            responsiveHelper: undefined,
            useCheck: true,
            itemClickAction: clickBoardItem,
            breakpointDefinition: breakpointDefinition,
            url: '',
            defaultOrder: [[1, 'desc']],
            columns: [
                {sName: 'checked', mData: null, bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        return '<label class="checkbox"><input type="checkbox" class="check-row-data check-board-row-data" data-sn="'+full.board_srl+'"><i></i></label>';
                    }
                },
                {sName: 'board_srl', mData: 'board_srl', bSortable: true, bSearchable: false},
                {sName: 'board_name', mData: 'board_name', bSortable: true, bSearchable: true,
                    mRender: function(data, type, full){
                        var badge = '';
                        if(full.enabled == ${mdv_no}) badge = 'D';
                        if(full.open_type == ${mdv_public}) {
                            if(badge == '') badge += 'P';
                            else            badge += ',P';
                        }

                        var badgeSpan = '';
                        if(badge != '') badgeSpan = '<span class="badge pull-right inbox-badge">'+badge+'</span>';

                        return '<a class="datatable-row-detail-view selected-board" data-board-srl="' +
                                full.board_srl + '">' + data + badgeSpan +'</a>';
                    }
                }
            ]
        };

        <#-- 만일 기존에 선택된 앱이 있다면 그 앱의 게시판 리스트를 자동으로 보여 준다 -->
        if(boardManageData.selected_app_srl) {
            $('#no-app-select').hide();
            $('#ok-app-select').show();

            boarListData.responsiveHelper = null;
            boarListData.url = '${Request.contextPath}/admin/board/list/app/' + boardManageData.selected_app_srl + '/t/';
            my.fn.createVerySimleDataTable(boarListData);
            my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);

            $('#board-list-title').text('게시판('+boardManageData.selected_app_name+')');
            $('#board-widget-toolbar').show();
        }

        <#-- 앱 목록에서 앱을 클릭 했을때, 앱에 포함된 게시판 리스트를 보여 준다 -->
        $(document).on('click', '.selected-app', function(e){
            <@am.jsevent_prevent />

            var $target = $(e.target),
                $nonBoardList = $('#no-app-select'),
                $boardListWrap = $('#ok-app-select'),
                appSrl = $target.data('appSrl'),
                appName = $target.text();

            if(appName.length > 17) appName = appName.substring(0, 17) + ' ...';

            if($.fn.DataTable.isDataTable('#${_page_id}-board-list')) {
                // 이전 datatable 을 destroy 시키고 storage data도 초기화 시킨다.
                my.fn.removeDatatableStorage('${_page_id}-board-list');
                $('#${_page_id}-board-list').DataTable().destroy();

            } else {
                $nonBoardList.hide();
                $boardListWrap.show();
            }

            boarListData.responsiveHelper = null;
            boarListData.url = '${Request.contextPath}/admin/board/list/app/' + appSrl + '/t/';

            my.fn.createVerySimleDataTable(boarListData);
            boardManageData.selected_app_srl = appSrl;
            boardManageData.selected_app_name = appName;
            my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);

            $('#board-list-title').text('게시판('+appName+')');
            $('#board-widget-toolbar').show();

            <#-- 기존에 보여지고 있는 카테고리 리스트는 없앤다 -->
            if($.fn.DataTable.isDataTable('#${_page_id}-category-list')) {
                // 이전 datatable 을 destroy 시키고 storage data도 초기화 시킨다.
                my.fn.removeDatatableStorage('${_page_id}-category-list');
                $('#${_page_id}-category-list').DataTable().destroy();
            }
            $('#category-list-title').text('카테고리');
            $('#ok-board-select').hide();
            $('#no-board-select').show();

            <#-- 기존에 보여지고 있는 게시물 리스트는 없앤다 -->
            if($.fn.DataTable.isDataTable('#${_page_id}-document-list')) {
                my.fn.removeDatatableStorage('${_page_id}-document-list');
                $('#${_page_id}-document-list').DataTable().destroy();
            }
            $('#document-list-title').text('게시물');
            $('#ok-category-select').hide();
            $('#no-category-select').show();

            boardManageData.selected_board_srl = 0;
            boardManageData.selected_board_name = '';
            boardManageData.selected_category_srl = 0;
            boardManageData.selected_category_name = '';
            my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);
        });

        <#-- 게시판 추가를 선택하면 추가 폼을 보여 준다. -->
        $('#add-board').click(function(e){
            if(boardManageData.selected_app_srl) {
                var moveURL = '#${Request.contextPath}/admin/board/add/app/' + boardManageData.selected_app_srl;
                <@am.jschangecontent moveuri="moveURL" navuri="navHash"
                        isVarMove=true isVarNav=true />
            } else {
                console.warn('no selected app.');
            }
        });

        <#-- 게시판 삭제를 선택하면 선택된 게시판을 삭제 한다. -->
        $('#remove-board').click(function(e){
            var reqData = {l_keys: []};
            $('.check-board-row-data').each(function(){
                var $this = $(this);
                if($this.is(':checked')) reqData.l_keys.push($this.data('sn'));
            });

            if(reqData.l_keys.length <= 0) {
                <@am.jsnoti title="게시판 삭제 실패" content="선택된 게시판이 없습니다" contenttype="text" />
                return;
            }

            var successText = reqData.l_keys.length+'개의 게시판을 삭제 했습니다.';
            var process = function() {
                <#-- 화면 전체 block 을 띄운다 -->
                $.blockUI({
                    message: $('#ckpush-window-block'),
                    css: {
                        'background-color': '#F5F5F5',
                        'border': '1px solid #CCCCCC',
                        'border-radius': '1px',
                        'border-collapse': 'collapse',
                    }
                });

                <#-- 선택 된 게시판 삭제 요청 -->
                <@am.jsajax_request submituri="${Request.contextPath}/admin/board/t/"
                        method="DELETE" errortitle="게시판 삭제 실패" ; job >
                    <#if job == "success_job">
                        <#-- 삭제 성공시 테이블을 다시 로딩. remove 하면 내부 데이터만 삭제되지 화면에 반영 되지 않는다.
                             draw 를 호출 해야 반영 된다. -->
                        <@am.jsnoti title="게시판 삭제 완료" content="successText" boxtype="success" />
                        <#-- ajax 로 로딩 하기 때문에 row 삭제가 의미 없다. 의미 없어도 메뉴얼에서 하라는데로 해 두었음. -->
                        _.each(reqData.l_keys, function(sn){
                            $('#${_page_id}-board-list').DataTable().row($('#dt-board-row-'+sn)).remove();
                        });
                        $('#${_page_id}-board-list').DataTable().draw(false);

                        <#-- 카테고리 리스트를 초기화 -->
                        if($.fn.DataTable.isDataTable('#${_page_id}-category-list')) {
                            // 이전 datatable 을 destroy 시키고 storage data도 초기화 시킨다.
                            my.fn.removeDatatableStorage('${_page_id}-category-list');
                            $('#${_page_id}-category-list').DataTable().destroy();
                        }
                        $('#category-list-title').text('카테고리');
                        $('#ok-board-select').hide();
                        $('#no-board-select').show();

                        <#-- 게시물 리스트를 초기화 -->
                        if($.fn.DataTable.isDataTable('#${_page_id}-document-list')) {
                            my.fn.removeDatatableStorage('${_page_id}-document-list');
                            $('#${_page_id}-document-list').DataTable().destroy();
                        }
                        $('#document-list-title').text('게시물');
                        $('#ok-category-select').hide();
                        $('#no-category-select').show();

                        boardManageData.selected_board_srl = 0;
                        boardManageData.selected_board_name = '';
                        boardManageData.selected_category_srl = 0;
                        boardManageData.selected_category_name = '';
                        my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);
                    <#elseif job == "common_job">
                        $.unblockUI();
                    </#if>
                </@am.jsajax_request>
            };

            my.fn.showUserConfirm('게시판 삭제',
                    "선택된 게시판을 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                    '삭제를 입력하세요',
                    '네', '삭제', process, '게시판 삭제 실패', '삭제를 입력하지 않았습니다.');
        });

        <#-- 게시판 수정을 선택하면 선택된 게시판을 수정하는 폼을 보여준다. -->
        $('#modify-board').click(function(e){
            var reqData = {l_keys: []};
            $('.check-board-row-data').each(function(){
                var $this = $(this);
                if($this.is(':checked')) reqData.l_keys.push($this.data('sn'));
            });

            if(reqData.l_keys.length <= 0) {
                <@am.jsnoti title="게시판 수정 실패" content="선택된 게시판이 없습니다" contenttype="text" />
                return;
            } else if(reqData.l_keys.length > 1) {
                <@am.jsnoti title="게시판 수정 실패" content="게시판 하나만 선택되어야 합니다" contenttype="text" />
                return;
            }

            var moveURL = '#${Request.contextPath}/admin/board/' + reqData.l_keys[0];
            <@am.jschangecontent moveuri="moveURL" navuri="navHash"
                    isVarMove=true isVarNav=true />
        });

        <#-- 카테고리 추가를 선택하면 추가 폼을 보여 준다. -->
        $('#add-category').click(function(e){
            if(boardManageData.selected_board_srl) {
                var moveURL = '#${Request.contextPath}/admin/board/' + boardManageData.selected_board_srl +
                        '/category/add';
                <@am.jschangecontent moveuri="moveURL" navuri="navHash"
                        isVarMove=true isVarNav=true />
            } else {
                console.warn('no selected board.');
            }
        });

        <#-- 카테고리 삭제를 선택하면 선택된 카테고리를 삭제 한다. -->
        $('#remove-category').click(function(e){
            var reqData = {l_keys: []};
            $('.check-category-row-data').each(function(){
                var $this = $(this);
                if($this.is(':checked')) reqData.l_keys.push($this.data('sn'));
            });

            if(reqData.l_keys.length <= 0) {
                <@am.jsnoti title="카테고리 삭제 실패" content="선택된 카테고리가 없습니다" contenttype="text" />
                return;
            }

            var successText = reqData.l_keys.length+'개의 카테고리를 삭제 했습니다.';
            var process = function() {
                <#-- 화면 전체 block 을 띄운다 -->
                $.blockUI({
                    message: $('#ckpush-window-block'),
                    css: {
                        'background-color': '#F5F5F5',
                        'border': '1px solid #CCCCCC',
                        'border-radius': '1px',
                        'border-collapse': 'collapse',
                    }
                });

                <#-- 선택 된 카테고리 삭제 요청 -->
                <@am.jsajax_request submituri="${Request.contextPath}/admin/board/category/t/"
                        method="DELETE" errortitle="카테고리 삭제 실패" ; job >
                    <#if job == "success_job">
                        <#-- 삭제 성공시 테이블을 다시 로딩. remove 하면 내부 데이터만 삭제되지 화면에 반영 되지 않는다.
                             draw 를 호출 해야 반영 된다. -->
                        <@am.jsnoti title="카테고리 삭제 완료" content="successText" boxtype="success" />
                        <#-- ajax 로 로딩 하기 때문에 row 삭제가 의미 없다. 의미 없어도 메뉴얼에서 하라는데로 해 두었음. -->
                        _.each(reqData.l_keys, function(sn){
                            $('#${_page_id}-category-list').DataTable().row($('#dt-category-row-'+sn)).remove();
                        });
                        $('#${_page_id}-category-list').DataTable().draw(false);

                        <#-- 게시물 리스트를 초기화 시킨다 -->
                        if($.fn.DataTable.isDataTable('#${_page_id}-document-list')) {
                            my.fn.removeDatatableStorage('${_page_id}-document-list');
                            $('#${_page_id}-document-list').DataTable().destroy();
                        }
                        $('#document-list-title').text('게시물');
                        $('#ok-category-select').hide();
                        $('#no-category-select').show();
                        boardManageData.selected_category_srl = 0;
                        boardManageData.selected_category_name = '';
                        my.fn.saveLocalStorageForCk('board_manage_data', boardManageData);

                    <#elseif job == "common_job">
                        $.unblockUI();
                    </#if>
                </@am.jsajax_request>
            };

            my.fn.showUserConfirm('카테고리 삭제',
                    "선택된 카테고리를 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                    '삭제를 입력하세요',
                    '네', '삭제', process, '카테고리 삭제 실패', '삭제를 입력하지 않았습니다.');
        });

        <#-- 카테고리 수정을 선택하면 선택된 게시판을 수정하는 폼을 보여준다. -->
        $('#modify-category').click(function(e){
            var reqData = {l_keys: []};
            $('.check-category-row-data').each(function(){
                var $this = $(this);
                if($this.is(':checked')) reqData.l_keys.push($this.data('sn'));
            });

            if(reqData.l_keys.length <= 0) {
                <@am.jsnoti title="카테고리 수정 실패" content="선택된 카테고리가 없습니다" contenttype="text" />
                return;
            } else if(reqData.l_keys.length > 1) {
                <@am.jsnoti title="카테고리 수정 실패" content="카테고리 하나만 선택되어야 합니다" contenttype="text" />
                return;
            }

            var moveURL = '#${Request.contextPath}/admin/board/category/' + reqData.l_keys[0];
            <@am.jschangecontent moveuri="moveURL" navuri="navHash"
                    isVarMove=true isVarNav=true />
        });

        <#-- 게시물 추가를 선택하면 추가 폼을 보여 준다. -->
        $('#add-document').click(function(e){
            if(boardManageData.selected_category_srl) {
                var moveURL = '#${Request.contextPath}/admin/board/category/'+boardManageData.selected_category_srl+'/document/add';
                <@am.jschangecontent moveuri="moveURL" navuri="navHash"
                        isVarMove=true isVarNav=true />
            } else {
                console.warn('no selected category.');
            }
        });

        <#-- 링크 게시물 추가를 선택하면 링크 게시물 추가를 위한 팝업창을 보여 준다. -->
        var boardForLink = [], categoryForLink = {},
            $modalBoardList = $('select[name=link_board_srl]'),
            $modalCategoryList = $('select[name=link_category_srl]'),
            linkSourceDocumentSrl = 0;
        $('#add-document-link').click(function(e){
            var reqData = {l_keys: []};
            $('.check-document-row-data').each(function(){
                var $this = $(this);
                if($this.is(':checked')) reqData.l_keys.push($this.data('sn'));
            });

            if(reqData.l_keys.length <= 0) {
                <@am.jsnoti title="링크 게시물 추가 실패" content="선택 된 게시물이 없습니다." contenttype="text" />
                return;
            }

            if(reqData.l_keys.length > 1) {
                <@am.jsnoti title="링크 게시물 추가 실패" content="하나의 게시물을 선택 해야 합니다." contenttype="text" />
                return;
            }

            linkSourceDocumentSrl = reqData.l_keys[0];

            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/category/list/ctype/${link_category_type}/t/"
                    errortitle="링크 게시물 추가 실패" ; job >
                <#if job == "success_job">
                    if(!data.board_list || data.board_list.length <= 0) {
                        <@am.jsnoti title="링크 게시물 추가 실패" content="선택 된 가능한 게시판및 카테고리가 없습니다." contenttype="text" />
                        return;
                    }

                    <#-- 선택 가능한 게시판, 카테고리 정보를 저장 한다 -->
                    boardForLink = [];
                    _.each(data.board_list, function(element){
                        var map = {
                            board_description: element.board_description,
                            board_name: element.board_name,
                            board_srl: element.board_srl,
                            enabled: element.enabled,
                            open_type: element.open_type
                        };

                        categoryForLink[element.board_srl] = [];

                        if(element.categories && element.categories.length > 0) {
                            _.each(element.categories, function(celem){
                                categoryForLink[celem.board_srl].push({
                                   board_srl: celem.board_srl,
                                   category_description: celem.category_description,
                                   category_name: celem.category_name,
                                   category_srl: celem.category_srl,
                                   category_type: celem.category_type,
                                   document_count: celem.document_count,
                                   enabled: celem.enabled,
                                   open_type: celem.open_type
                               });
                            });
                        }

                        boardForLink.push(map);
                    });

                    $modalBoardList.empty();
                    _.each(boardForLink, function(belem){
                        $modalBoardList.append('<option value="'+belem.board_srl+'">'+belem.board_name+'</option>');
                    });

                    $modalCategoryList.empty();
                    _.each(categoryForLink[boardForLink[0].board_srl], function(celem){
                        $modalCategoryList.append('<option value="'+celem.category_srl+'">'+celem.category_name+'</option>');
                    });

                    <#-- 파일 업로드 폼을 초기화 하고 모달을 보여 준다 -->
                    $('#modal-document-link-form').each(function(){ this.reset(); });
                    $('#modal-document-link-modal').modal('show');
                </#if>
            </@am.jsajax_request>
        });
        <#-- 링크 대상 게시판이 바뀌면 카테고리도 변경 시킨다 -->
        $modalBoardList.on('change', function(e){
            <@am.jsevent_prevent />

            var $this = $(this);
            $modalCategoryList.empty();
            _.each(categoryForLink[$this.val()], function(celem){
                $modalCategoryList.append('<option value="'+celem.category_srl+'">'+celem.category_name+'</option>');
            });
        });

        <#-- 링크 게시물 생성 모달에서 생성 버튼을 클릭 했을 때, 링크 게시물을 생성 한다 -->
        $('#make-document-link').click(function(){
            var reqData = {
                board_srl: $modalBoardList.val(),
                category_srl: $modalCategoryList.val(),
                document_srl: linkSourceDocumentSrl
            };
            var board_name = my.fn.showHTMLTag($('select[name=link_board_srl] :selected').text()),
                category_name = my.fn.showHTMLTag($('select[name=link_category_srl] :selected').text());

            <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/link/add/t/"
                    errortitle="링크 게시물 생성 실패" ; job >
                <#if job == "success_job">
                    var alertMsg = '링크 게시물을 생성 하였습니다.<br/>&nbsp;&nbsp;&nbsp;- 게시판 : ' + board_name +
                            '<br/>&nbsp;&nbsp;&nbsp;- 카테고리 : ' + category_name +
                            '<br/>&nbsp;&nbsp;&nbsp;- 게시물 : ' + my.fn.showHTMLTag(data.document_title);
                    <@am.jsnoti title="링크 게시물 생성" content="alertMsg" boxtype="info" timeout=0 />
                </#if>
            </@am.jsajax_request>
        });

        <#-- 게시물 삭제를 선택하면 선택된 게시물을 삭제 한다. -->
        $('#remove-document').click(function(e){
            var reqData = {l_keys: [], i_keys: []}, linkFlag = '${mdv_no}';
            $('.check-document-row-data').each(function(){
                var $this = $(this), eleLNFlag = $this.attr('data-link-flag');
                if(eleLNFlag != linkFlag) linkFlag = eleLNFlag;
                if($this.is(':checked')) reqData.l_keys.push($this.data('sn'));
            });

            if(reqData.l_keys.length <= 0) {
                <@am.jsnoti title="게시물 삭제 실패" content="선택 된 게시물이 없습니다" contenttype="text" />
                return;
            }

            <#-- 링크 인지 아닌지 표시 -->
            reqData.i_keys.push(linkFlag);

            var successText = reqData.l_keys.length+'개의 게시물을 삭제 했습니다.';
            var process = function() {
                <#-- 화면 전체 block 을 띄운다 -->
                $.blockUI({
                    message: $('#ckpush-window-block'),
                    css: {
                        'background-color': '#F5F5F5',
                        'border': '1px solid #CCCCCC',
                        'border-radius': '1px',
                        'border-collapse': 'collapse',
                    }
                });

                <#-- 선택 된 게시물 삭제 요청 -->
                <@am.jsajax_request submituri="${Request.contextPath}/admin/board/document/t/"
                        method="DELETE" errortitle="게시물 삭제 실패" ; job >
                    <#if job == "success_job">
                        <#-- 삭제 성공시 테이블을 다시 로딩. remove 하면 내부 데이터만 삭제되지 화면에 반영 되지 않는다.
                             draw 를 호출 해야 반영 된다. -->
                        <@am.jsnoti title="게시물 삭제 완료" content="successText" boxtype="success" />
                        <#-- ajax 로 로딩 하기 때문에 row 삭제가 의미 없다. 의미 없어도 메뉴얼에서 하라는데로 해 두었음. -->
                        _.each(reqData.l_keys, function(sn){
                            $('#${_page_id}-document-list').DataTable().row($('#dt-document-row-'+sn)).remove();
                        });
                        $('#${_page_id}-document-list').DataTable().draw(false);
                    <#elseif job == "common_job">
                        $.unblockUI();
                    </#if>
                </@am.jsajax_request>
            };

            my.fn.showUserConfirm('게시물 삭제',
                    "선택 된 게시물을 삭제 합니다.<br/>삭제를 원하면 \'삭제\'를 입력 후 \'네\' 버튼을 클릭 하세요.",
                    '삭제를 입력하세요',
                    '네', '삭제', process, '게시물 삭제 실패', '삭제를 입력하지 않았습니다.');
        });
    };

    var pagedestroy = function(){
        $('#${_page_id}-board-list').Datatable().destroy();
    };

    loadScript("${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js", function() {
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