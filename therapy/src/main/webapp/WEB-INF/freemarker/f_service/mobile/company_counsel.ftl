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
<div class="tp-page-head"><!-- page header -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="page-header">
                    <h1>상담사 소개</h1>
                </div>
            </div>
        </div>
    </div>
</div>
<!------------------------------------------------------------------------------------------->

<div class="main-container"><!-- main container -->
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-8">
                        <h1>상담사 소개</h1>
                    </div>
                    <div class="col-md-12">
                        <div class="call-to-action well-box" style="text-align: left">
                            <span style="color: red">플리마인드</span> 선생님들을 소개합니다.
                            상담의 비밀유지와 상담자와 내담자의 권익보호를 위하여 실명대신 닉네임으로 소개하고 있습니다.
                            서비스를 이용하시고자 상담자의 약력 및 관련 자격증을 요청하시면 받아보실 수 있습니다.
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <#list memberEntities as memberEntity>
                <div class="col-md-3 vendor-box"><!-- venue box start-->
                    <div><!-- venue pic -->
                        <a href="http://plus.kakao.com/home/@플리마인드-${memberEntity.nick_name}">
                        <#if memberEntity.fileEntities?exists>
                            <#list memberEntity.fileEntities as fileEntity>
                            <#if fileEntity_index = 0>
                            <img src="${image_server_host}${Request.contextPath}${document_attach_uri}${fileEntity.file_srl}" alt="wedding venue" id="btn-pass" class="img-responsive" style="width: 60%; height: 60%; disply: block; margin-left: auto; margin-right: auto;">
                            <!-- /.venue pic -->
                            </#if>
                            </#list>
                        <#else>
                            <img src="${Request.svcResPath}images/founder.jpg" alt="wedding venue" id="btn-pass" class="img-responsive">
                            <!-- /.venue pic -->
                        </#if>
                        </a>
                     </div>
                    <div><!-- venue details -->
                        <div class="text-center">
                            <div class="price form-group">
                                <a href="http://plus.kakao.com/home/@플리마인드-${memberEntity.nick_name}">${memberEntity.nick_name}</a>
                            </div>
                        </div>
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
