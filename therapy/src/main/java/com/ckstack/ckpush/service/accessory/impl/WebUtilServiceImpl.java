package com.ckstack.ckpush.service.accessory.impl;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by dhkim94 on 15. 3. 18..
 */
@Service
public class WebUtilServiceImpl implements WebUtilService {
    private final static Logger LOG = LoggerFactory.getLogger(WebUtilServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    protected Properties confCommon;
    @Autowired
    private MessageSource messageSource;

    @Override
    public String getTid(HttpServletRequest httpServletRequest) {
        String tid = null;
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        //String requestURL = urlPathHelper.getOriginatingRequestUri(httpServletRequest);
        String requestURI = urlPathHelper.getRequestUri(httpServletRequest);
        int tidIdx = StringUtils.indexOf(requestURI, "/t/");

        if (tidIdx != -1) {
            tid = StringUtils.substring(requestURI, tidIdx+3);
            int pathIdx = StringUtils.indexOf(tid, "/");
            if (pathIdx != -1) tid = StringUtils.substring(tid, 0, pathIdx);
        } else {
            String requestContentType = httpServletRequest.getHeader("Content-Type");
            if(StringUtils.indexOf(requestContentType, "application/json") != -1) {
                String body;
                StringBuilder buffer = new StringBuilder();
                try {
                    BufferedReader reader = httpServletRequest.getReader();
                    String line;
                    while((line=reader.readLine()) != null) buffer.append(line);
                    body = buffer.toString();
                    tidIdx = StringUtils.indexOf(body, "tid");
                    if(tidIdx != -1) {
                        int commaIdx = StringUtils.indexOf(body, ",", tidIdx);
                        if(commaIdx != -1) {
                            tid = StringUtils.substring(body, tidIdx, commaIdx);
                            tid = StringUtils.replace(tid, "\"", "");
                            tid = StringUtils.replace(tid, "tid", "");
                            tid = StringUtils.replace(tid, ":", "");
                            tid = StringUtils.trim(tid);
                        }
                    }
                } catch (IOException e) {
                    LOG.debug("failed detect tid of requestURL["+requestURI+"], exception["+e.getMessage()+"]");
                }
            }
        }
        if(tid == null && (tid = httpServletRequest.getParameter("tid")) == null) tid = "";

        return tid;
    }

    @Override
    public void setCommonApiResponse(ModelAndView mav, String tid, String code) {
        if(tid == null) mav.addObject(confCommon.getProperty("json_tid"), "");
        else            mav.addObject(confCommon.getProperty("json_tid"), StringUtils.trim(tid));

        Locale locale = LocaleContextHolder.getLocale();

        try {
            mav.addObject(confCommon.getProperty("json_error"), messageSource.getMessage("ecd." + code, null, locale));
            mav.addObject(confCommon.getProperty("json_message"), messageSource.getMessage("emg." + code, null, locale));
        } catch(Exception e) {
            LOG.warn("not found error value of ["+code+"]. let use default code of [failed]");
            mav.addObject(confCommon.getProperty("json_error"), messageSource.getMessage("ecd.failed", null, locale));
            mav.addObject(confCommon.getProperty("json_message"), messageSource.getMessage("emg.failed", null, locale));
        }
    }

    @Override
    public ModelAndView setCommonErrorResponse(ModelAndView mav, HttpServletRequest request, String code,
                                               Map<String, String> reason) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();

        mav.setViewName("f_comm/error");
        mav.addObject(confCommon.getProperty("json_tid"), this.getTid(request));
        mav.addObject(confCommon.getProperty("json_request_uri"), urlPathHelper.getRequestUri(request));
        mav.addObject(confCommon.getProperty("json_method"), request.getMethod());
        mav.addObject(confCommon.getProperty("json_reason"), reason == null ? new HashMap<>() : reason);

        Locale locale = LocaleContextHolder.getLocale();

        try {
            mav.addObject(confCommon.getProperty("json_error"), messageSource.getMessage("ecd." + code, null, locale));
            mav.addObject(confCommon.getProperty("json_message"), messageSource.getMessage("emg." + code, null, locale));
        } catch (Exception e) {
            LOG.warn("not found error value of ["+code+"]. let use default code of [failed]");
            mav.addObject(confCommon.getProperty("json_error"), messageSource.getMessage("ecd.failed", null, locale));
            mav.addObject(confCommon.getProperty("json_message"), messageSource.getMessage("emg.failed", null, locale));
        }

        return mav;
    }

    @Override
    public String getPhoneNumber(String phoneNumber) {
        if(phoneNumber == null) return "";
        return StringUtils.trim(phoneNumber).replaceAll("\\D", "");
    }

    @Override
    public String getAnchorWithCommonRequestURL(HttpServletRequest request, String anchor) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();

        return request.getScheme() + "://" + request.getServerName() +
                (request.getServerPort()==80 ? "" : ":"+request.getServerPort() ) +
                urlPathHelper.getContextPath(request) + "/admin#" +
                urlPathHelper.getContextPath(request) + anchor;
    }

    @Override
    public String getRequestIp(HttpServletRequest request) {
        String requestIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        if(requestIp == null || requestIp.length() <= 0 ||
                requestIp.toLowerCase().equals("unknown")) requestIp = request.getHeader("REMOTE_ADDR");
        if(requestIp == null || requestIp.length() <= 0 ||
                requestIp.toLowerCase().equals("unknown")) requestIp = request.getRemoteAddr();

        return requestIp;
    }

    @Override
    public boolean parsingDataTableParameter(Map<String, Map<String, String>> columnValue,
                                             Map<String, String> orderValue,
                                             Map<String, String> extraValue,
                                             HttpServletRequest request) {
        String columnHeader;
        if(confCommon.containsKey("dtreq_column_header"))
            columnHeader = confCommon.getProperty("dtreq_column_header");
        else
            columnHeader = "columns[";

        // column 카운트를 구한다.
        int columnCount = 0;
        Map<String, Object> columnParams = WebUtils.getParametersStartingWith(request, columnHeader);

        int idx;
        String strNum;
        Set<String> keys = columnParams.keySet();

        for(String key : keys) {
            idx = StringUtils.indexOf(key, "]");
            if(idx < 0) continue;

            strNum = StringUtils.trim(StringUtils.substring(key, 0, idx));
            if(strNum.equals("")) continue;
            if(NumberUtils.isNumber(strNum)) {
                if((idx=Integer.parseInt(strNum, 10)) > columnCount) columnCount = idx;
            }
        }

        if(columnCount <= 0) return false;

        // order 를 위해서 column name 을 순서 대로 저장 해야 한다.
        List<String> columnName = new ArrayList<>();

        // columns 의 attribute 를 구한다.
        String name;
        for(int i=0 ; i<=columnCount ; i++) {
            name = columnParams.get(i+"][name]").toString();
            columnName.add(name);

            Map<String, String> attr = new HashMap<>();
            attr.put(confCommon.getProperty("dtreq_attr_name"), name);
            attr.put(confCommon.getProperty("dtreq_attr_data"), columnParams.get(i + "][data]").toString());
            attr.put(confCommon.getProperty("dtreq_attr_searchable"), columnParams.get(i + "][searchable]").toString());
            attr.put(confCommon.getProperty("dtreq_attr_orderable"), columnParams.get(i + "][orderable]").toString());
            attr.put(confCommon.getProperty("dtreq_attr_search_value"), columnParams.get(i + "][search][value]").toString());
            attr.put(confCommon.getProperty("dtreq_attr_search_regex"), columnParams.get(i + "][search][regex]").toString());

            columnValue.put(name, attr);
        }

        // ordering 을 구한다.
        String orderHeader;
        if(confCommon.containsKey("dtreq_order_header"))orderHeader = confCommon.getProperty("dtreq_order_header");
        else                                            orderHeader = "order[";

        // order 카운트를 구한다.
        int orderCount = 0;
        Map<String, Object> orderParams = WebUtils.getParametersStartingWith(request, orderHeader);

        keys = orderParams.keySet();

        for(String key : keys) {
            idx = StringUtils.indexOf(key, "]");
            if(idx < 0) continue;

            strNum = StringUtils.trim(StringUtils.substring(key, 0, idx));
            if(strNum.equals("")) continue;
            if(NumberUtils.isNumber(strNum)) {
                if((idx=Integer.parseInt(strNum, 10)) > orderCount) orderCount = idx;
            }
        }

        String unitColumn;
        int iUnitColumn;

        for(int i=0 ; i<=orderCount ; i++) {
            unitColumn = StringUtils.trim(orderParams.get(i+"][column]").toString());
            if(StringUtils.equals(unitColumn, "")) continue;
            if(NumberUtils.isNumber(unitColumn))   iUnitColumn = Integer.parseInt(unitColumn, 10);
            else                                    continue;

            String dir = StringUtils.trim(orderParams.get(i+"][dir]").toString());

            // direction 오류 검증 하지 않는다. 실제 사용 하는 곳에서 사용하기 직전에 오류 검증 한다.
            //if(!StringUtils.equalsIgnoreCase(dir, "desc") && !StringUtils.equalsIgnoreCase(dir, "asc")) {
            //    LOG.warn("invalid column ordering. column [" + columnName.get(iUnitColumn) +
            //            "], direction [" + dir + "]");
            //    continue;
            //}
            orderValue.put(columnName.get(iUnitColumn), dir);
        }

        // 나머지 parameter 를 구한다.
        extraValue.put(confCommon.getProperty("dtreq_draw"), request.getParameter("draw"));
        extraValue.put(confCommon.getProperty("dtreq_start"), request.getParameter("start"));
        extraValue.put(confCommon.getProperty("dtreq_length"), request.getParameter("length"));
        extraValue.put(confCommon.getProperty("dtreq_search_value"), request.getParameter("search[value]"));
        extraValue.put(confCommon.getProperty("dtreq_search_regex"), request.getParameter("search[regex]"));

        return true;
    }

    @Override
    public Map<String, String> getSearchFilter(Map<String, Map<String, String>> columnValue,
                                               Map<String, String> extraValue) {
        String oneSearchValue = extraValue.get(confCommon.getProperty("dtreq_search_value"));
        if(oneSearchValue != null) oneSearchValue = StringUtils.trim(oneSearchValue);

        Map<String, String> searchFilter = new HashMap<>();

        Set<String> keys = columnValue.keySet();
        if(keys.size() > 0) {
            for(String key : keys) {
                Map<String, String> column = columnValue.get(key);
                if(!column.containsKey("searchable") || !column.containsKey("orderable") ||
                        !column.containsKey("name") || !column.containsKey("search_value")) {
                    LOG.warn("not found jquery data-table ajax request value. searchable exist[" +
                            column.containsKey("searchable") + "], orderable exist[" +
                            column.containsKey("orderable") + "], name exist["+
                            column.containsKey("name") + "], search_value exist[" +
                            column.containsKey("search_value") + "]");
                    continue;
                }

                // 검색하는 칼럼 정보를 넣는다.
                if(column.get("searchable").equals("true")) {
                    String svalue = column.get("search_value");
                    if(svalue != null) svalue = StringUtils.trim(svalue);

                    if(StringUtils.equals(StringUtils.trim(svalue), "") && !StringUtils.equals(oneSearchValue, ""))
                        searchFilter.put(column.get("name"), oneSearchValue);
                    else
                        searchFilter.put(column.get("name"), svalue);
                }
            }
        }

        return searchFilter;
    }

    @Override
    public String getRandomAlphabetLower(int length) {
        if(length <= 0) return "";

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        String[] lowerCase = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                              "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                              "u", "v", "w", "x", "y", "z"};
        int lowerCaseLength = lowerCase.length;

        random.setSeed(DateTime.now().getMillis());
        for (int i=0 ; i<length ; i++) stringBuilder.append(lowerCase[random.nextInt(lowerCaseLength)]);

        return stringBuilder.toString();
    }

    @Override
    public String getFileURI(HttpServletRequest httpServletRequest, long fileSrl,
                             String fileType) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        return urlPathHelper.getContextPath(httpServletRequest) + confCommon.getProperty(fileType) +
                fileSrl;
    }

    @Override
    public byte[] serverToServerDownload(String url) {
        if(url == null || StringUtils.equals(url, "")) {
            LOG.warn("invalid url. url is null or empty string");
            return null;
        }

        // scheme 이 없으면 기본으로 http 를 붙여 준다.
        int idx = StringUtils.indexOf(url, "://");
        if(idx < 0) url = "http://" + url;

        URI uri;

        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            LOG.warn("invalid url. url [" + url + "]");
            return null;
        }

        LOG.info("try file download at url [" + url + "]");

        byte[] ret;

        try {
            ret = restTemplate.getForObject(uri, byte[].class);
        } catch (Exception e) {
            LOG.warn("failed file download at url [" + url + "], exception [" + e.getMessage() + "]");
            return null;
        }

        return ret;
    }

    @Override
    public byte[] convertObjectToJsonBytes(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        byte[] ret = null;

        try {
            ret = mapper.writeValueAsBytes(obj);
        } catch (IOException e) {
            LOG.warn("failed convert object to json byte. object class [" +
                    obj.getClass().getName() + "]");
        }

        return ret;
    }

    @Override
    public Object convertJsonBytesToObject(String className, byte[] jsonBytes) {
        ObjectMapper mapper = new ObjectMapper();

        Object obj = null;
        try {
            obj = mapper.readValue(jsonBytes, Class.forName(className));
        } catch (IOException e) {
            LOG.warn("can't convert json byte array to object. class [" +
                    className + "]");
        } catch (ClassNotFoundException e) {
            LOG.warn("can't convert json byte array to object. not found class [" +
                    className + "]");
        }

        return obj;
    }

    @Override
    public String getSocialHeader(String socialType) {
        if(!NumberUtils.isNumber(socialType)) {
            LOG.warn("social type is not number. socailType [" + socialType + "]");
            return null;
        }

        String socialHeader = null;
        int iSocialType = Integer.parseInt(socialType, 10);
        switch (iSocialType) {
            case MDV.SOCIAL_NONE:
                socialHeader = "";
                break;

            case MDV.SOCIAL_KAKAOTOC:
                socialHeader = confCommon.getProperty("social_header_kakaotoc");
                break;

            case MDV.SOCIAL_FACEBOOK:
                socialHeader = confCommon.getProperty("social_header_facebook");
                break;

            case MDV.SOCIAL_TWITTER:
                socialHeader = confCommon.getProperty("social_header_twitter");
                break;

            case MDV.SOCIAL_GOOGLE:
                socialHeader = confCommon.getProperty("social_header_google");
                break;

            case MDV.SOCIAL_NAVER:
                socialHeader = confCommon.getProperty("social_header_naver");
                break;

            case MDV.SOCIAL_DAUM:
                socialHeader = confCommon.getProperty("social_header_daum");
                break;

            default:
                LOG.warn("not support social type. socialType [" + socialType + "]");
                break;
        }

        return socialHeader;
    }

}
