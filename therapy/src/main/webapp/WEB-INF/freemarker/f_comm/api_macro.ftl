<#-- web-page api request parameter 리스트 -->
<#macro pareq param>
    <legend>Request Parameters</legend>
    <#if param?? && param?size gt 0>
        <#list param?keys as key>
            <div class="form-group">
                <label class="col-md-2 control-label">${key}</label>
                <div class="col-md-10">
                    <input class="form-control" disabled="disabled" value="${param[key]}" type="text">
                </div>
            </div>
        </#list>
    <#else>
        <div class="row">
            <p class="col-md-12">없음</p>
        </div>
    </#if>
</#macro>

<#-- web-page api response widget title -->
<#macro header>
    <header>
        <span class="widget-icon"> <i class="fa fa-download"></i> </span>
        <h2><#nested></h2>
    </header>
</#macro>

<#-- web-page api response body 에서 공통으로 들어가는 항목 -->
<#macro coresp tid="" error="" message="">
    <legend>Response Body</legend>
    <div class="form-group">
        <label class="col-md-2 control-label">tid</label>
        <div class="col-md-10">
            <input class="form-control" disabled="disabled" value="${tid}" type="text">
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">error</label>
        <div class="col-md-10">
            <input class="form-control" disabled="disabled" value="${error}" type="text">
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">message</label>
        <div class="col-md-10">
            <input class="form-control" disabled="disabled" value="${message}" type="text">
        </div>
    </div>
</#macro>

<#-- web-page api response body 에서 1 depth 단일 항목 -->
<#macro utresp key="" value="">
    <div class="form-group">
        <label class="col-md-2 control-label">${key}</label>
        <div class="col-md-10">
            <input class="form-control" disabled="disabled" value="${value}" type="text">
        </div>
    </div>
</#macro>

<#-- web-page api response cause. 실패 원인 -->
<#macro csresp reason>
    <#if reason?? && reason?size gt 0>
        <div class="form-group">
            <label class="col-md-2 control-label">reason</label>
            <label class="col-md-10 control-label"></label>
        </div>

        <#list reason?keys as key>
            <div class="form-group">
                <label class="col-md-3 control-label">${key}</label>
                <div class="col-md-9">
                    <input class="form-control" disabled="disabled" value="${reason[key]}" type="text">
                </div>
            </div>
        </#list>
    <#else>
        <@utresp key="reason" value="원인을 알 수 없습니다" />
    </#if>
</#macro>

<#--
    게시물 생성일을 n초전, n분전, n시간전, 생성년월일 로 조건에 따른 여러 형태로 표시 하기 위해서
    unix timestamp 시간을 현재 시간과 비교해서 바꾼다.

    javascript로 변환하는 것이 문제 있어서 freemarker로 처음 부터 변환 시킨다.
-->
<#macro document_time1 unix_timestamp=0>
    <#if unix_timestamp lte 0>
        <#return>
    </#if>

    <#assign currentTM=(.now?long / 1000)?string('0')?number>
    <#assign subtract=currentTM-unix_timestamp>

    <#if subtract lt 60>
        ${subtract} 초전
    <#elseif subtract lt 3600>
        ${(subtract/60)?string('0')} 분전
    <#elseif subtract lt 86400>
        ${(subtract/3600)?string('0')} 시간전
    <#else>
        <#-- yyyy. M. d 로 출력 된다 -->
        ${(unix_timestamp*1000)?number_to_date}
    </#if>
</#macro>