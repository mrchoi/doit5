<script src="${Request.resPath}js/libs/jquery-2.1.1.min.js"></script>



<script src="${Request.resPath}js/plugin/datatables/jquery.dataTables.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.colVis.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.tableTools.min.js"></script>
<script src="${Request.resPath}js/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="${Request.resPath}js/plugin/datatable-responsive/datatables.responsive.min.js"></script>


<#-- widget grid -->

                <#-- end widget edit box -->

                <#-- widget content -->
                <#-- datable 의 header toolbar 를 사용하지 않고 위젯 타이틀 밑에 딱 붙이기 위해서 -19px margin-top 을 주었음 -->
                <#--<div class="widget-body no-padding" style="margin-top: -19px;">-->
                <#-- button 을 넣을 자리가 없어서 다시 top 영역을 사용 하기로 함. 위의 주석은 추후 버튼 넣을거 없다면 참고용으로 그대로 둔다. -->
                    <div class="widget-body no-padding">
                    <#-- datatable 에서 검색을 지원하는 column 의 th 는 dt-column, dt-column-<column number> class 를 포함해야 한다.(localStorage를 이용한 초기화에 이용됨) -->
                        <table id="pwy"  width="100%">

                                <th data-class="expand" style="text-align: center; max-width: 50px;min-width: 50px;">SN.</th>
                                <th data-class="expand" style="text-align: center;">제목</th>
                                <th data-hide="phone,tablet" style="text-align: center; max-width: 50px;min-width: 50px;" >조회수</th>
                                <th data-hide="phone,tablet" style="text-align: center; max-width: 300px;min-width: 300px;" >등록일자</th>

                        </table>
                    </div>

<#-- SCRIPTS ON PAGE EVENT -->
<script type="text/javascript">




    <@ap.pwy url="${Request.contextPath}/user/counselling/list/t/" ; job>
        <#if job == "columns">
                [
                {sName: 'document_srl', mData: 'document_srl'},
                {sName: 'document_title', mData: 'document_title'},
                {sName: 'read_count', mData: 'read_count'},
                {sName: 'c_date', mData: 'c_date'}
                ]
        </#if>
    </@ap.pwy>


</script>