<@ap.py_header_load_script />
<script type="text/javascript" src="${Request.svcResPath}js/accordion-sign.js"></script>
<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->
<#assign _page_id="user-faq-list">
<#assign _page_parent_name="FAQ">
<#assign _page_current_name="FAQ 목록">
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
                    <#--<div class="col-md-12">
                        <div class="call-to-action well-box">
                            <p></p>
                        </div>
                    </div>-->
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12 st-accordion"> <!-- shortcode -->
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

                    <#list faqEntities as faqEntity>
                        <div class="panel panel-default">
                            <div class="panel-heading" role="tab" id="heading-${faqEntity.document_srl}">
                                <h4 class="panel-title">
                                    <span class="question-sign">Q</span><a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse-${faqEntity.document_srl}" aria-expanded="false" aria-controls="collapse-${faqEntity.document_srl}">
                                    ${faqEntity.document_title}
                                    </a>
                                </h4>
                            </div>
                            <div id="collapse-${faqEntity.document_srl}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading-${faqEntity.document_srl}">
                                <div class="panel-body">
                                    ${faqEntity.document_content}
                                </div>
                            </div>
                        </div>
                    </#list>

                </div>
            </div>
        </div>

    </div>
</div>


<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->