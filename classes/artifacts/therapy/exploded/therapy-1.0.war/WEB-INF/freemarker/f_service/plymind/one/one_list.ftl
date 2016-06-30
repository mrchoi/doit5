<@ap.py_header_load_script />

<script src="${Request.resPath}js/plugin/jquery-blockui/jquery.blockUI.js"></script>
<script src="${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js"></script>
<style>
    .dataTables_filter .input-group-addon {
        width: 50px;
        margin-top: 0;
        float: left;
        height: 47px;
        padding-top: 14px;
        border: 1px solid #ccc;
    }
    input.form-control{
        margin-left:0px;
    }
    .color-box {
        font-size: 11px;
        text-transform: uppercase;
        background: #fdfdfb;
        margin-bottom: 4px;
        padding: 0px 10px;
        color: #706a68 font-weight: 700;
        display: inline-block;
        border: 1px solid #e9e6e0;
    }
</style>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-one-list">
<#assign _page_parent_name="나의 활동">
<#assign _page_current_name="1:1 답변확인 목록">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<div class="filter-box collapse in" id="searchform" aria-expanded="true"><!-- filter form -->
    <div class="container">
        <div class="row filter-form">
            <form>
                <div id="present_advice" class="col-md-3 text-center">
                    <a href="#">현재 진행중인 상담<h1>${presentMap.advice_cnt}건</h1></a>
                </div>
                <div id="present_checkup" class="col-md-3 text-center">
                    <a href="#">심리 검사<h1>${presentMap.checkup_cnt}건</h1></a>
                </div>
                <div id="present_complete" class="col-md-3 text-center">
                    <a href="#">결과 보고서<h1>${presentMap.complete_cnt}건</h1></a>
                </div>
                <div id="present_one" class="col-md-3 text-center">
                    <a href="#">1:1 답변 확인<h1>${presentMap.one_cnt}건</h1></a>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h2>${_page_current_name}</h2>
                    </div>
                    <#--<div class="col-md-12">
                        <div class="call-to-action well-box">
                            <p></p>
                        </div>
                    </div>-->
                </div>
            </div>
        </div>

        <div class="row">

            <div class="col-md-12 view-cart"><!-- view cart -->
                <#-- widget content -->
                <#-- datable 의 header toolbar 를 사용하지 않고 위젯 타이틀 밑에 딱 붙이기 위해서 -19px margin-top 을 주었음 -->
                <#--<div class="widget-body no-padding" style="margin-top: -19px;">-->
                <#-- button 을 넣을 자리가 없어서 다시 top 영역을 사용 하기로 함. 위의 주석은 추후 버튼 넣을거 없다면 참고용으로 그대로 둔다. -->
                <div class="widget-body" >
                    <table class="shop_table" id="${_page_id}" >
                        <!-- shop table -->
                        <thead>
                        <tr><th class="text-center" style="max-width:50px;">번호</th>
                            <th class="text-center" style="max-width:300px; min-width: 300px;">제목</th>
                            <th data-hide="phone,tablet" class="text-center" style="max-width:100px;">작성자</th>
                            <th data-hide="phone,tablet" class="text-center" style="max-width:50px;">문의일</th>
                            <th data-hide="phone,tablet" class="text-center" style="max-width:50px;">답변일</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    <!-- shop table end -->
                </div>
            </div>
            <!-- view cart end -->

            <div class="col-md-12">
                <div class="btn-group pull-right" style="margin-bottom:10px;">
                    <button id="add-document" name="add-document"class="btn tp-btn-primary" type="button">
                        <i class="fa fa-pencil"></i> 1:1 문의하기
                    </button>
                </div>
            </div>
        </div>

    </div>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

$(document).ready(function() {

    $("#present_advice").click(function(e) {
        <@am.jsevent_prevent />
        my.fn.pmove('${Request.contextPath}/user/myadvice/progress_list');
    });

    $("#present_checkup").click(function(e) {
        <@am.jsevent_prevent />
        my.fn.pmove('${Request.contextPath}/user/mycheckup/list');
    });

    $("#present_complete").click(function(e) {
        <@am.jsevent_prevent />
        my.fn.pmove('${Request.contextPath}/user/mycomplete/list');
    });

    $("#present_one").click(function(e) {
        <@am.jsevent_prevent />
        my.fn.pmove('${Request.contextPath}/user/one/list');
    });

    $("#add-document").click(function() {
        my.fn.pmove('${Request.contextPath}/user/one/add');
    });

    <@ap.py_jsdatatable listId="${_page_id}" searchTooltip="제목 첫글자 기준 검색" url="${Request.contextPath}/user/one/list/t/" ; job>
        <#if job == "order">
                [[0, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'document_srl', mData: 'document_srl', sClass: 'text-center', bSortable: true, bSearchable: false},
                {sName: 'document_title', mData: 'document_title', sClass: 'text-center', bSortable: false, bSearchable: true,
                    mRender: function(data, type, full, dtObj){
                    <#-- 클릭시 a link 로 detail 페이지로 이동 -->
                        var navHash = $(location).attr('hash').substring(1),
                                moveURL = '${Request.contextPath}/user/one/' + full.document_srl ;
                        return '<a class="datatable-row-detail-view" style="cursor: pointer;" data-move="' +
                                moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                    }
                },
                {sName: 'user_id', mData: 'user_id', sClass: 'text-center', bSortable: false, bSearchable: false},
                {sName: 'c_date', mData: 'c_date', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function (data, type, full) {
                        return new Date(my.fn.toNumber(data) * 1000).format('yyyy-MM-dd HH:mm');
                    }
                },
                {sName: 'comment_date', mData: 'comment_date', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function (data, type, full) {
                        return new Date(my.fn.toNumber(data) * 1000).format('yyyy-MM-dd HH:mm');
                    }
                }
                ]
        </#if>
    </@ap.py_jsdatatable>


});

</script>


<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->