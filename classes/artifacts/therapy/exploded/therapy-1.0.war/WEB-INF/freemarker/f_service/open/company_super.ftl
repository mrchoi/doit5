<@ap.py_header_load_script /><#--  -->

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
<#assign _page_id="company_super">
<#assign _page_parent_name="회사 소개">
<#assign _page_current_name="슈퍼바이저">

<@ap.py_page_simple_depth icon="" parent="${_page_parent_name}" current="${_page_current_name}"/>
<!------------------------------------------------------------------------------------------->

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h1>${_page_current_name}</h1>
                    </div>
                    <div class="col-md-12">
                        <div class="call-to-action well-box">
                            플리마인드에 계신 슈퍼바이저를 소개합니다.<br>
                            플리마인드에는 임상심리학회, 상담심리학회, 상담학회, 한국놀이치료학회 슈퍼바이저 선생님들이 계십니다.<br>
                            슈퍼바이저가 되시길 원하는 슈퍼바이지 수련선생님들에게 스카이프 슈퍼비젼을 제공해 드립니다.<br>
                            문의 주시면 자세히 알려드리겠습니다.
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
                            <th class="text-center" style="max-width:50px;">번호</th>
                            <th data-class="text-center" style="text-align: center; max-width: 100px;min-width: 100px;">성함</th>
                            <th data-class="text-center" style="text-align: center; max-width: 300px;min-width: 300px;" >활동영역(자격증번호)</th>
                            <th data-class="text-center" style="text-align: center; max-width: 300px;min-width: 300px;" >근무처(직위)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#--<tr class="cart_item">
                            <td class="text-center">10</td>
                            <td class="text-center"><a href="">정혜인</a></td>
                            <td >임상(*2*번), 상담(**7)번</td>
                            <td >플리마인드(대표), 한국심리진흥원(이사장)</td>
                        </tr>
                        <tr class="cart_item">
                            <td class="text-center">9</td>
                            <td class="text-center"><a href="">손명자</a></td>
                            <td >임상(*0*번)</td>
                            <td >새미래심리건강연구소(원장)</td>
                        </tr>
                        <tr class="cart_item">
                            <td class="text-center">8</td>
                            <td class="text-center"><a href="">신성만</a></td>
                            <td >상담</td>
                            <td >한동대학교(교수), 한국중독상담학회(학회장)</td>
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
<!-- /.main container -->

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">

    $(document).ready(function() {
    <@ap.py_jsdatatable listId="${_page_id}" searchTooltip="성함 첫글자 기준 검색" url="${Request.contextPath}/user/open/company_super/list/t/" ; job>
        <#if job == "order">
                [[0, 'desc']]
        <#elseif job == "columns">
                [
                {sName: 'document_srl', mData: 'document_srl', sClass: 'text-center', bSortable: true, bSearchable: false},
                {sName: 'document_title', mData: 'document_title', sClass: 'text-center', bSortable: false, bSearchable: true},
                    //mRender: function(data, type, full, dtObj){
                    <#-- 클릭시 a link 로 detail 페이지로 이동 -->
                    //    var navHash = $(location).attr('hash').substring(1),
                    <#--           moveURL = '#${Request.contextPath}/user/open/company_super/' + full.document_srl + '/';-->
                    //    return '<a class="datatable-row-detail-view" data-move="' +
                    //            moveURL + '" data-nav="' + navHash + '">'+data+'</a>';
                    //}
                //},
                {sName: 'template_extra_work', mData: 'template_extra.work', sClass: 'text-center', bSortable: false, bSearchable: false},
                {sName: 'template_extra_office', mData: 'template_extra.office', sClass: 'text-center', bSortable: false, bSearchable: false}
                ]
        </#if>
    </@ap.py_jsdatatable>

    });

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->