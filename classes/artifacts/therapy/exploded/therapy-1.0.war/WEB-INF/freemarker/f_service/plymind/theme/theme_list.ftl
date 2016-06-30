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
</style>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-theme-list">
<#assign _page_parent_name="테마공간">
<#assign _page_current_name="테마공간 목록">
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
                        <div class="call-to-action well-box" style="font:24px 'SDMiSaeng','Istok Web',sans-serif; line-height: 30px;">
                           “우린 무슨 사이(Long Distance Diary)?”<br>
                            멀고도 가까운 거리에 있는 우리... 이야기 나누고 공감하기로 해요.
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">

            <div class="col-md-12 view-cart"><!-- view cart -->
                <table class="shop_table" id="${_page_id}" >
                    <!-- shop table -->
                    <thead>
                    <tr>
                        <th class="text-center" style="max-width:50px;">번호</th>
                        <th class="text-center" style="max-width:300px; min-width: 300px;">제목</th>
                        <th data-hide="phone,tablet" class="text-center" style="max-width:50px;">날짜</th>
                        <th data-hide="phone,tablet" class="text-center" style="max-width:50px;min-width:50px;">조회수</th>
                        <th data-hide="phone,tablet" class="text-center" style="max-width:50px;min-width:50px;">댓글수</th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <!-- shop table end -->
            </div>

            <div class="col-md-12">
                <div class="btn-group pull-right" style="margin-bottom:10px;">
                    <button id="add-document" name="add-document"class="btn tp-btn-primary" type="button">
                        <i class="fa fa-pencil"></i> 글쓰기
                    </button>
                </div>
            </div>
            <!-- view cart end -->
        </div>

    </div>
</div>


<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {

        var navHash = $(location).attr('hash').substring(1),
                responsiveHelper_list1 = undefined,
                breakpointDefinition = {
                    tablet : 1024,
                    phone : 480
                };


        $("#add-document").click(function() {
            my.fn.pmove('${Request.contextPath}/user/theme/add');
        });

        $(".datatable-row-detail-view").click(function() {
            my.fn.pmove('${Request.contextPath}/user/theme/1');
        });

    <#--<@ap.py_jsnoti title="파일 업로드" content="업로드 성공 했습니다." contenttype="val" boxtype="info" />-->

    <@ap.py_jsdatatable listId="${_page_id}" searchTooltip="제목 첫글자 기준 검색" url="${Request.contextPath}/user/theme/list/t/" ; job>
        <#if job == "order">
                [[0, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'document_srl', mData: 'document_srl', sClass: 'text-center', bSortable: true, bSearchable: false},
                {sName: 'document_title', mData: 'document_title', sClass: 'text-center', bSortable: false, bSearchable: true,
                    mRender: function(data, type, full, dtObj){
                    <#-- 클릭시 a link 로 detail 페이지로 이동 -->
                        var navHash = $(location).attr('hash').substring(1),
                                moveURL = '${Request.contextPath}/user/theme/' + full.document_srl ;
                        return '<a class="datatable-row-detail-view" style="cursor: pointer;" data-move="' +
                                moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                    }
                },
                {
                    sName: 'u_date', mData: 'u_date', sClass: 'text-center', bSortable: false, bSearchable: false,
                    mRender: function (data, type, full) {
                        return new Date(my.fn.toNumber(data) * 1000).format('yyyy-MM-dd HH:mm:ss');
                    }
                },
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