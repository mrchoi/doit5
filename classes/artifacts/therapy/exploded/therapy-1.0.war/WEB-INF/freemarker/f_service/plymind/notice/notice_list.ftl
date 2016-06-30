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
<#assign _page_id="user-notice-list">
<#assign _page_parent_name="공지사항/이벤트">
<#assign _page_current_name="공지사항/이벤트 목록">
<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h2>${_page_parent_name}</h2>
                    </div>
                    <div class="col-md-12">
                        <div class="call-to-action well-box">
                           “PLYMind의 서비스 변경, 업데이트 및 다양한 소식을 알려드립니다.”
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            
            <div class="col-md-12 view-cart"><!-- view cart -->
                <#-- widget content -->
                <#-- datable 의 header toolbar 를 사용하지 않고 위젯 타이틀 밑에 딱 붙이기 위해서 -19px margin-top 을 주었음 -->
                <#--<div class="widget-body no-padding" style="margin-top: -19px;">-->
                <#-- button 을 넣을 자리가 없어서 다시 top 영역을 사용 하기로 함. 위의 주석은 추후 버튼 넣을거 없다면 참고용으로 그대로 둔다. -->
                <div class="widget-body no-padding" style="margin-top: -19px;">

                <table class="shop_table" id="${_page_id}" >
                    <!-- shop table -->
                    <thead>
                    <tr>
                        <th class="text-center" style="max-width:50px;">No</th>
                        <th class="text-center" style="min-width: 80px; max-width:80px;">구분</th>
                        <th class="text-center" style="max-width:300px; min-width: 300px;">제목</th>
                        <th data-hide="phone,tablet" class="text-center" style="max-width:50px;">날짜</th>
                        <th data-hide="phone,tablet" class="text-center" style="max-width:50px;">작성자</th>
                        <th data-hide="phone,tablet" class="text-center" style="max-width:50px;min-width:50px;">조회수</th>
                        <th data-hide="phone,tablet" class="text-center" style="max-width:50px;min-width:50px;">댓글수</th>
                    </tr>
                    </thead>
                    <tbody>
                   <#-- <tr class="cart_item">
                        <td class="text-center">이벤트</td>
                        <td ><a href="" class="datatable-row-detail-view">Ply Mind 홈페이지 오픈기념 이벤트</a></td>
                        <td class="text-center">2015.12.01</td>
                        <td class="text-center">500</td>
                        <td class="text-center">200</td>
                    </tr>
                    <tr class="cart_item">
                        <td class="text-center">이벤트</td>
                        <td ><a href="" class="datatable-row-detail-view">Ply Mind 홈페이지 오픈기념 이벤트</a></td>
                        <td class="text-center">2015.12.01</td>
                        <td class="text-center">500</td>
                        <td class="text-center">200</td>
                    </tr>
                    <tr class="cart_item">
                        <td class="text-center">공지</td>
                        <td ><a href="" class="datatable-row-detail-view">Ply Mind 홈페이지 오픈기념 이벤트</a></td>
                        <td class="text-center">2015.12.01</td>
                        <td class="text-center">500</td>
                        <td class="text-center">200</td>
                    </tr>-->
                    </tbody>
                </table>
                <!-- shop table end -->
                </div>
            </div>
            <!-- view cart end -->
        </div>

    </div>
</div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {

    <@ap.py_jsdatatable listId="${_page_id}" searchTooltip="제목 첫글자 기준 검색" url="${Request.contextPath}/user/notice/list/t/" ; job>
        <#if job == "order">
                [[0, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'document_srl', mData: 'document_srl', sClass: 'text-center', bSortable: true, bSearchable: false},
                {sName: 'allow_notice', mData: 'allow_notice', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function(data, type, full){
                        if(data == ${mdv_yes})      return '<p class="color-box">공지사항</p>';
                        else if(data == ${mdv_no})  return '<p class="color-box">이벤트</p>';
                        return '-';
                    }
                },
                {sName: 'document_title', mData: 'document_title', sClass: 'text-center', bSortable: false, bSearchable: true,
                    mRender: function(data, type, full, dtObj){
                    <#-- 클릭시 a link 로 detail 페이지로 이동 -->
                        var navHash = $(location).attr('hash').substring(1),
                                moveURL = '${Request.contextPath}/user/notice/' + full.document_srl ;
                        return '<a class="datatable-row-detail-view" style="cursor: pointer;" data-move="' +
                                moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                    }
                },
                {
                    sName: 'c_date', mData: 'c_date', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function (data, type, full) {
                        return new Date(my.fn.toNumber(data) * 1000).format('yyyy-MM-dd');
                    }
                },
                {sName: 'user_id', mData: 'user_id' , sClass: 'text-center', bSortable: false, bSearchable: false},
                {sName: 'read_count', mData: 'read_count' , sClass: 'text-center', bSortable: false, bSearchable: false},
                {sName: 'comment_count', mData: 'comment_count', sClass: 'text-center', bSortable: false, bSearchable: false}
                ]
        </#if>
    </@ap.py_jsdatatable>

    });

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->