<@ap.py_header_load_script /><#--  -->
<script src="${Request.svcResPath}js/bootstrap/bootstrap-modal.js"></script>
<style>
    .modal-content {
        height: 100% !important;
        overflow:visible;
    }
    .modal-dialog {
        /*height: 70% !important;*/
        padding-top:15%;
    }

</style>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="company_counsel">
<#assign _page_parent_name="회사 소개">
<#assign _page_current_name="상담사 소개">
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
                            “<span style="color:red;">플리마인드</span>의 상담선생님들은 한국심리학회 산하 <span style="color:#0066FF;">임상 심리학회</span>와 <span style="color:#0066FF;">상담 심리학회</span> 1급 전문가 선생님들과<br>
                            <span style="color:#0066FF;">한국 상담학회</span> 수퍼바이저 선생님들,
                            <span style="color:#0066FF;">한국 미술치료학회, 한국 음악치료협회, 한국 모래놀이치료학회, 한국놀이치료학회, 한국 사이코드라마 소시오드라마 학회</span> 전문가 선생님들,<br>
                            <span style="color:#0066FF;">정신보건 임상심리사</span> 선생님들, <span style="color:#0066FF;">청소년 상담사</span> 선생님들,
                                전현직 <span style="color:#0066FF;">심리학 관련 교수님</span>들과 <span style="color:#0066FF;">임상 및 상담심리학 전공 석박사</span> 선생님들로 구성되어져 있습니다. <br>
                            상담의 비밀유지와 상담자와 내담자의 권익보호를 위하여 실명대신 닉네임으로 소개하고 있습니다. <br>
                            서비스를 이용하시고자 상담자의 약력 및 관련 자격증을 요청하시면 받아보실 수 있습니다.”
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <#list memberEntities as memberEntity>
                <div class="col-md-3 vendor-box"><!-- venue box start-->
                    <#if memberEntity.fileEntities?exists>
                        <#list memberEntity.fileEntities as fileEntity>
                            <#if fileEntity_index = 0>
                            <div class="vendor-image"><!-- venue pic -->
                                <a href="#" data-toggle="modal" data-target="#basicModal1_${memberEntity_index}"><img src="${image_server_host}${Request.contextPath}${document_attach_uri}${fileEntity.file_srl}" alt="wedding venue" id="btn-pass" class="img-responsive"></a>
                            </div>
                            <!-- /.venue pic -->
                            </#if>
                        </#list>
                    <#else>
                        <div class="vendor-image"><!-- venue pic -->
                            <a href="#" data-toggle="modal" data-target="#basicModal1_${memberEntity_index}"><img src="${Request.svcResPath}images/founder.jpg" alt="wedding venue" id="btn-pass" class="img-responsive"></a>
                        </div>
                        <!-- /.venue pic -->
                    </#if>
                    <div class="vendor-detail"><!-- venue details -->
                        <div class="vendor-price text-center"><div class="price form-group"><a href="#" data-toggle="modal" data-target="#basicModal1_${memberEntity_index}">${memberEntity.nick_name}</a></div></div>
                    </div>
                    <!-- venue details -->
                </div>
                <!-- /.venue box start-->
            </#list>
        </div>
        <#--<div class="row">
            <div class="col-md-12 tp-pagination">
                <ul class="pagination">
                    <li> <a href="#" aria-label="Previous"> <span aria-hidden="true">Previous</span> </a> </li>
                    <li class="active"><a href="#">1</a></li>
                    <li><a href="#">2</a></li>
                    <li><a href="#">3</a></li>
                    <li><a href="#">4</a></li>
                    <li><a href="#">5</a></li>
                    <li> <a href="#" aria-label="Next"> <span aria-hidden="true">NEXT</span> </a> </li>
                </ul>
            </div>
        </div>-->
    </div>
</div>
<!-- /.main container -->

<#list memberEntities as member>
<@ap.py_bstrap_modal modalId="basicModal1_${member_index}" formId="modal-document-link-form" title="상담사 소개" okButtonCheck=false>
<h3>${member.nick_name}</h3>
<p>${member.memberExtraEntity.self_introduction}
</p>
</@ap.py_bstrap_modal>
</#list>
</div>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
