package com.ckstack.ckpush.service.accessory;

//import static org.hamcrest.CoreMatchers.nullValue;
//import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.Rollback;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.test.context.transaction.TransactionConfiguration;
//import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by dhkim94 on 15. 3. 18..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "file:src/main/resources/spring/application-config.xml",
        "file:src/main/resources/spring/security-context.xml",
        "file:src/main/resources/database/redis-context.xml",
        "file:src/main/resources/database/mybatis-context-junit.xml"})
//@Transactional(value = "transactionManager")
//@TransactionConfiguration(defaultRollback = false)
public class WebUtilServiceTest {
    @Autowired
    private WebUtilService webUtilService;
    @Autowired
    protected Properties confCommon;
    @Autowired
    private MessageSource messageSource;

    @Before
    public void setUp() {

    }

    /**
     * request url 에서 tid 가져오는 메소드 테스트
     */
    @Test
    public void getTidBasicTest() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        when(httpServletRequest.getRequestURI()).thenReturn("/1/api/sample/t/1");
        //when(httpServletRequest.getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE)).thenReturn("/1/api/sample/t/1");
        String tid = webUtilService.getTid(httpServletRequest);
        assertThat(tid, is("1"));

        when(httpServletRequest.getRequestURI()).thenReturn("/1/api/sample/t/501");
        //when(httpServletRequest.getAttribute(WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE)).thenReturn("/1/api/sample/t/1");
        tid = webUtilService.getTid(httpServletRequest);
        assertThat(tid, is("501"));

        when(httpServletRequest.getRequestURI()).thenReturn("/1/api/sample/t/");
        tid = webUtilService.getTid(httpServletRequest);
        assertThat(tid, is(""));

        when(httpServletRequest.getRequestURI()).thenReturn("/1/api/sample/t");
        tid = webUtilService.getTid(httpServletRequest);
        assertThat(tid, is(""));

        when(httpServletRequest.getRequestURI()).thenReturn("/1/api/sample/");
        tid = webUtilService.getTid(httpServletRequest);
        assertThat(tid, is(""));

        when(httpServletRequest.getRequestURI()).thenReturn("/1/api/sample");
        tid = webUtilService.getTid(httpServletRequest);
        assertThat(tid, is(""));
    }

    /**
     * 숫자 만으로 이루어진 전화번호로 바꾸는 메소드 테스트
     */
    @Test
    public void getPhoneNumberTest() {
        // dash 삭제 테스트
        String number1 = "010-5372-8336";
        String result = webUtilService.getPhoneNumber(number1);
        assertThat(result, is("01053728336"));

        // 알파벳이 들어가면 없앤다
        number1 = "010abcd1234";
        result = webUtilService.getPhoneNumber(number1);
        assertThat(result, is("0101234"));

        number1 = "  010 5372 8336  ";
        result = webUtilService.getPhoneNumber(number1);
        assertThat(result, is("01053728336"));

        number1 = "010 - 5372 - 8336";
        result = webUtilService.getPhoneNumber(number1);
        assertThat(result, is("01053728336"));

        // empty string 을 변환 시키면 empty string 을 얻는다.
        number1 = "";
        result = webUtilService.getPhoneNumber(number1);
        assertThat(result, is(""));

        // null 을 변환 시키면 empty string 을 얻는다.
        result = webUtilService.getPhoneNumber(null);
        assertThat(result, is(""));
    }

    /**
     * REST api 호출시 공통으로 들어가는 값을 만들어 주는 메소드를 테스트
     */
    @Test
    public void setCommonApiResponseTest() {
        ModelAndView mav = new ModelAndView();

        Locale locale = LocaleContextHolder.getLocale();

        // tid 가 null 일때

        // 정의된 코드를 사용
        String code = "success";
        webUtilService.setCommonApiResponse(mav, "1", code);

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_tid")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_tid")).toString(), is("1"));

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_error")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_error")).toString(), is(messageSource.getMessage("ecd."+code, null, locale)));

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_message")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_message")).toString(), is(messageSource.getMessage("emg." + code, null, locale)));

        // tid 가 null 일때
        webUtilService.setCommonApiResponse(mav, null, code);

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_tid")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_tid")).toString(), is(""));

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_error")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_error")).toString(), is(messageSource.getMessage("ecd."+code, null, locale)));

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_message")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_message")).toString(), is(messageSource.getMessage("emg." + code, null, locale)));

        // tid 가 공백을 포함 하고 있을때
        webUtilService.setCommonApiResponse(mav, " 123  ", code);

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_tid")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_tid")).toString(), is("123"));

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_error")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_error")).toString(), is(messageSource.getMessage("ecd."+code, null, locale)));

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_message")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_message")).toString(), is(messageSource.getMessage("emg."+code, null, locale)));


        // 정의 되지 않은 코드를 사용
        code = "none_default_code_12345";
        webUtilService.setCommonApiResponse(mav, "2", code);

        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_tid")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_tid")).toString(), is("2"));

        // failed 를 기본으로 사용
        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_error")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_error")).toString(), is(messageSource.getMessage("ecd.failed", null, locale)));

        // failed 를 기본으로 사용
        assertThat(mav.getModel().containsKey(confCommon.getProperty("json_message")), is(true));
        assertThat(mav.getModel().get(confCommon.getProperty("json_message")).toString(), is(messageSource.getMessage("emg.failed", null, locale)));
    }

    /**
     * 에러를 response 할때 공통으로 처리하는 부분 테스트
     */
    @Test
    public void setCommonErrorResponseTest() {
        String definedError = "invalid_method_parameter";
        Locale locale = LocaleContextHolder.getLocale();

        assertThat(confCommon.containsKey("json_tid"), is(true));
        assertThat(confCommon.containsKey("json_error"), is(true));
        assertThat(confCommon.containsKey("json_message"), is(true));
        assertThat(confCommon.containsKey("json_reason"), is(true));
        assertThat(confCommon.containsKey("json_request_uri"), is(true));
        assertThat(confCommon.containsKey("json_method"), is(true));

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        // 존재하는 에러 코드로 공통 에러 처리, reason 이 null
        ModelAndView mav1 = new ModelAndView();
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/1/admin/abc");
        webUtilService.setCommonErrorResponse(mav1, httpServletRequest, definedError, null);

        assertThat(mav1.getViewName(), is("f_comm/error"));

        // tid 값은 없다.
        assertThat(mav1.getModel().containsKey(confCommon.getProperty("json_tid")), is(true));
        assertThat(mav1.getModel().get(confCommon.getProperty("json_tid")).toString(), is(""));

        // error code 체크
        assertThat(mav1.getModel().containsKey(confCommon.getProperty("json_error")), is(true));
        assertThat(mav1.getModel().get(confCommon.getProperty("json_error")).toString(),
                is(messageSource.getMessage("ecd." + definedError, null, locale)));

        // error message 체크
        assertThat(mav1.getModel().containsKey(confCommon.getProperty("json_message")), is(true));
        assertThat(mav1.getModel().get(confCommon.getProperty("json_message")).toString(),
                is(messageSource.getMessage("emg."+definedError, null, locale)));

        // reason 은 empty hash map 이다.
        assertThat(mav1.getModel().containsKey(confCommon.getProperty("json_reason")), is(true));
        assertThat(mav1.getModel().get(confCommon.getProperty("json_reason")), is(notNullValue()));
        assertThat(((HashMap<?, ?>) mav1.getModel().get(confCommon.getProperty("json_reason"))).size(), is(0));

        // request_uri 값 체크
        assertThat(mav1.getModel().containsKey(confCommon.getProperty("json_request_uri")), is(true));
        assertThat(mav1.getModel().get(confCommon.getProperty("json_request_uri")).toString(), is("/1/admin/abc"));

        // method 값 체크
        assertThat(mav1.getModel().containsKey(confCommon.getProperty("json_method")), is(true));
        assertThat(mav1.getModel().get(confCommon.getProperty("json_method")).toString(), is("GET"));

        // 존재하지 않는 에러 코드로 공통 에러 처리, reason 이 null. tid 존재함.
        ModelAndView mav2 = new ModelAndView();
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletRequest.getRequestURI()).thenReturn("/1/admin/abc/ddd/t/10231");
        webUtilService.setCommonErrorResponse(mav2, httpServletRequest, "junit_not_exist_error_code", null);

        assertThat(mav2.getViewName(), is("f_comm/error"));

        // tid 값 존재 함
        assertThat(mav2.getModel().containsKey(confCommon.getProperty("json_tid")), is(true));
        assertThat(mav2.getModel().get(confCommon.getProperty("json_tid")).toString(), is("10231"));

        // error code 체크
        assertThat(mav2.getModel().containsKey(confCommon.getProperty("json_error")), is(true));
        assertThat(mav2.getModel().get(confCommon.getProperty("json_error")).toString(),
                is(messageSource.getMessage("ecd.failed", null, locale)));

        // error message 체크
        assertThat(mav2.getModel().containsKey(confCommon.getProperty("json_message")), is(true));
        assertThat(mav2.getModel().get(confCommon.getProperty("json_message")).toString(),
                is(messageSource.getMessage("emg.failed", null, locale)));

        // reason 은 empty hash map 이다.
        assertThat(mav2.getModel().containsKey(confCommon.getProperty("json_reason")), is(true));
        assertThat(mav2.getModel().get(confCommon.getProperty("json_reason")), is(notNullValue()));
        assertThat(((HashMap<?, ?>) mav2.getModel().get(confCommon.getProperty("json_reason"))).size(), is(0));

        // request_uri 값 체크
        assertThat(mav2.getModel().containsKey(confCommon.getProperty("json_request_uri")), is(true));
        assertThat(mav2.getModel().get(confCommon.getProperty("json_request_uri")).toString(), is("/1/admin/abc/ddd/t/10231"));

        // method 값 체크
        assertThat(mav2.getModel().containsKey(confCommon.getProperty("json_method")), is(true));
        assertThat(mav2.getModel().get(confCommon.getProperty("json_method")).toString(), is("POST"));

        // reason 이 있을때 체크
        Map<String, String> reason = new HashMap<>();
        reason.put("user_name", "no exist user name. empty string");

        ModelAndView mav3 = new ModelAndView();
        when(httpServletRequest.getMethod()).thenReturn("POST");
        when(httpServletRequest.getRequestURI()).thenReturn("/1/admin/add/member/t/441");
        webUtilService.setCommonErrorResponse(mav3, httpServletRequest, "junit_not_exist_error_code", reason);

        assertThat(mav3.getViewName(), is("f_comm/error"));

        // tid 값 존재 함
        assertThat(mav3.getModel().containsKey(confCommon.getProperty("json_tid")), is(true));
        assertThat(mav3.getModel().get(confCommon.getProperty("json_tid")).toString(), is("441"));

        // error code 체크
        assertThat(mav3.getModel().containsKey(confCommon.getProperty("json_error")), is(true));
        assertThat(mav3.getModel().get(confCommon.getProperty("json_error")).toString(),
                is(messageSource.getMessage("ecd.failed", null, locale)));

        // error message 체크
        assertThat(mav3.getModel().containsKey(confCommon.getProperty("json_message")), is(true));
        assertThat(mav3.getModel().get(confCommon.getProperty("json_message")).toString(),
                is(messageSource.getMessage("emg.failed", null, locale)));

        // reason 은 empty hash map 이다.
        assertThat(mav3.getModel().containsKey(confCommon.getProperty("json_reason")), is(true));
        assertThat(mav3.getModel().get(confCommon.getProperty("json_reason")), is(notNullValue()));
        assertThat(((HashMap<?, ?>) mav3.getModel().get(confCommon.getProperty("json_reason"))).size(), is(1));
        assertThat(((HashMap<?, ?>) mav3.getModel().get(confCommon.getProperty("json_reason"))).containsKey("user_name"),
                is(true));
        assertThat(((HashMap<?, ?>) mav3.getModel().get(confCommon.getProperty("json_reason"))).get("user_name").toString(),
                is(reason.get("user_name").toLowerCase()));

        // request_uri 값 체크
        assertThat(mav3.getModel().containsKey(confCommon.getProperty("json_request_uri")), is(true));
        assertThat(mav3.getModel().get(confCommon.getProperty("json_request_uri")).toString(), is("/1/admin/add/member/t/441"));

        // method 값 체크
        assertThat(mav3.getModel().containsKey(confCommon.getProperty("json_method")), is(true));
        assertThat(mav3.getModel().get(confCommon.getProperty("json_method")).toString(), is("POST"));
    }

    /**
     * smart admin ajax 버전용 공통 URL 작성 테스트
     */
    @Test
    public void commonRequestURLTest() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        // requestURI 는 hash tag 하단은 주지 않는다.
        when(httpServletRequest.getContextPath()).thenReturn("/1");
        when(httpServletRequest.getScheme()).thenReturn("http");
        when(httpServletRequest.getServerName()).thenReturn("ckpush.com");
        when(httpServletRequest.getServerPort()).thenReturn(8080);

        String commonURL = webUtilService.getAnchorWithCommonRequestURL(httpServletRequest, "/show/me");
        assertThat(commonURL, is(notNullValue()));
        assertThat(commonURL, is("http://ckpush.com:8080/1/admin#/1/show/me"));

        // requestURI 는 hash tag 하단은 주지 않는다.
        when(httpServletRequest.getContextPath()).thenReturn("/2");
        when(httpServletRequest.getScheme()).thenReturn("https");
        when(httpServletRequest.getServerName()).thenReturn("ckpush.com");
        when(httpServletRequest.getServerPort()).thenReturn(80);

        commonURL = webUtilService.getAnchorWithCommonRequestURL(httpServletRequest, "/help/us");
        assertThat(commonURL, is(notNullValue()));
        assertThat(commonURL, is("https://ckpush.com/2/admin#/2/help/us"));
    }

    /**
     * http request object 에서 request ip 가져 오는 것 테스트
     */
    @Test
    public void getRequestIpTest() {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        // HTTP_X_FORWARDED_FOR 이 설정 되었을때
        when(httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("10.0.0.1");
        when(httpServletRequest.getHeader("REMOTE_ADDR")).thenReturn("127.0.0.1");

        assertThat(webUtilService.getRequestIp(httpServletRequest), is("10.0.0.1"));

        // HTTP_X_FORWARDED_FOR 가 설정 되지 않았을 때
        httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader("REMOTE_ADDR")).thenReturn("127.0.0.1");

        assertThat(webUtilService.getRequestIp(httpServletRequest), is("127.0.0.1"));

    }

    // NOTE mock request data 를 만드는게 너무 힘듬. parsingDataTableParameter는 그냥 무시 한다.
    /**
     * datatable 의 파라미터 파싱을 테스트.
     * mock request 에서 뭔가 무슨 짓을 하는 듯 함.
     */
    /**
    @Test
    public void parsingDataTableParameterTest() {
        // 설정값 존재 여부 확인
        assertThat(confCommon.containsKey("dtreq_column_header"), is(true));
        assertThat(confCommon.containsKey("dtreq_attr_name"), is(true));
        assertThat(confCommon.containsKey("dtreq_attr_data"), is(true));
        assertThat(confCommon.containsKey("dtreq_attr_searchable"), is(true));
        assertThat(confCommon.containsKey("dtreq_attr_orderable"), is(true));
        assertThat(confCommon.containsKey("dtreq_attr_search_value"), is(true));
        assertThat(confCommon.containsKey("dtreq_attr_search_regex"), is(true));
        assertThat(confCommon.containsKey("dtreq_draw"), is(true));
        assertThat(confCommon.containsKey("dtreq_start"), is(true));
        assertThat(confCommon.containsKey("dtreq_length"), is(true));
        assertThat(confCommon.containsKey("dtreq_search_value"), is(true));
        assertThat(confCommon.containsKey("dtreq_search_regex"), is(true));
        assertThat(confCommon.containsKey("dtreq_order_header"), is(true));

        // mock data 설정
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("draw", 1);
        parameters.put("columns[0][data]", "");
        parameters.put("columns[0][name]", "checked");
        parameters.put("columns[0][searchable]", "false");
        parameters.put("columns[0][orderable]", "false");
        parameters.put("columns[0][search][value]", "");
        parameters.put("columns[0][search][regex]", "false");
        parameters.put("columns[1][data]", "app_srl");
        parameters.put("columns[1][name]", "app_srl");
        parameters.put("columns[1][searchable]", "false");
        parameters.put("columns[1][orderable]", "true");
        parameters.put("columns[1][search][value]", "");
        parameters.put("columns[1][search][regex]", "false");
        parameters.put("columns[2][data]", "app_name");
        parameters.put("columns[2][name]", "app_name");
        parameters.put("columns[2][searchable]", "true");
        parameters.put("columns[2][orderable]", "true");
        parameters.put("columns[2][search][value]", "");
        parameters.put("columns[2][search][regex]", "false");
        parameters.put("order[0][column]", 1);
        parameters.put("order[0][dir]", "desc");
        parameters.put("start", 0);
        parameters.put("length", 10);
        parameters.put("search[value]", "");
        parameters.put("search[regex]", "false");

        Set<String> keys = parameters.keySet();
        List<String> paramList = new ArrayList<>(keys);
        Enumeration<String> mockParameterNames = Collections.enumeration(paramList);

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        when(httpServletRequest.getParameterNames()).thenReturn(mockParameterNames);
        for(String key : keys) {
            String [] values = new String[1];
            values[0] = parameters.get(key).toString();

            when(httpServletRequest.getParameterValues(key)).thenReturn(values);
        }

        // 파싱 한다.
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        List<Map<String, String>> orderValue = new ArrayList<>();
        Map<String, String> extraValue = new HashMap<>();

        webUtilService.parsingDataTableParameter(columnValue, orderValue, extraValue,
                httpServletRequest);

        assertThat(columnValue.size(), is(3));
    }
    */

    /**
     * jquery data 테이블의 파라미터에서 뽑은 데이터에서 테이블 검색값을 구하는 메소드 테스트
     */
    @Test
    public void getSearchFilterTest() {
        Map<String, Map<String, String>> columnValue = new HashMap<>();
        Map<String, String> extraValue = new HashMap<>();

        // 데이터 준비
        // search 를 지원하지마녀 search value 가 empty string 이기 때문에 search value 는 null 임.
        Map<String, String> map1 = new HashMap<>();
        map1.put(confCommon.getProperty("dtreq_attr_name"), "column1");
        map1.put(confCommon.getProperty("dtreq_attr_data"), "column1");
        map1.put(confCommon.getProperty("dtreq_attr_searchable"), "true");
        map1.put(confCommon.getProperty("dtreq_attr_orderable"), "true");
        map1.put(confCommon.getProperty("dtreq_attr_search_value"), "");
        map1.put(confCommon.getProperty("dtreq_attr_search_regex"), "");
        columnValue.put("column1", map1);

        // search 를 지원하지 않기 때문에 getSearchFilter 하면 값을 넣어 주지 않음
        Map<String, String> map2 = new HashMap<>();
        map2.put(confCommon.getProperty("dtreq_attr_name"), "column2");
        map2.put(confCommon.getProperty("dtreq_attr_data"), "column2");
        map2.put(confCommon.getProperty("dtreq_attr_searchable"), "false");
        map2.put(confCommon.getProperty("dtreq_attr_orderable"), "true");
        map2.put(confCommon.getProperty("dtreq_attr_search_value"), "");
        map2.put(confCommon.getProperty("dtreq_attr_search_regex"), "");
        columnValue.put("column2", map2);

        // search 를 지원하고 search value 가 abc 임.
        Map<String, String> map3 = new HashMap<>();
        map3.put(confCommon.getProperty("dtreq_attr_name"), "column3");
        map3.put(confCommon.getProperty("dtreq_attr_data"), "column3");
        map3.put(confCommon.getProperty("dtreq_attr_searchable"), "true");
        map3.put(confCommon.getProperty("dtreq_attr_orderable"), "true");
        map3.put(confCommon.getProperty("dtreq_attr_search_value"), "abc");
        map3.put(confCommon.getProperty("dtreq_attr_search_regex"), "ddd");     // 이건 무시 된다. regexp 검색은 지원하지 않음.
        columnValue.put("column3", map3);

        Map<String, String> filter1 = webUtilService.getSearchFilter(columnValue, extraValue);
        assertThat(filter1.size(), is(2));
        assertThat(filter1.containsKey("column1"), is(true));
        assertThat(filter1.get("column1"), is(nullValue()));
        assertThat(filter1.containsKey("column3"), is(true));
        assertThat(filter1.get("column3"), is(notNullValue()));
        assertThat(filter1.get("column3"), is("abc"));

        // 칼럼별 검색값이 아닌 전체 검색값이 설정되어 있다면 검색값이 없는 칼럼은 전체 검색값으로 대치 된다.
        // 검색 값이 존재하는 칼럼은 그 칼럼의 검색값을 그대로 사용함.
        extraValue.put(confCommon.getProperty("dtreq_search_value"), "total search value");

        Map<String, String> filter2 = webUtilService.getSearchFilter(columnValue, extraValue);
        assertThat(filter2.size(), is(2));
        assertThat(filter2.containsKey("column1"), is(true));
        // 검색값이 없기 때문에 전체 검색 값으로 대치됨
        assertThat(filter2.get("column1"), is(notNullValue()));
        assertThat(filter2.get("column1"), is("total search value"));
        assertThat(filter2.containsKey("column3"), is(true));
        // 검색값이 존재 하기 때문에 칼럼의 검색값을 그대로 사용
        assertThat(filter2.get("column3"), is(notNullValue()));
        assertThat(filter2.get("column3"), is("abc"));
    }

    /**
     * 원하는 길이 만큼 영어 소문자 문자열을 가져오는 메소드 테스트
     */
    @Test
    public void getRandomAlphabetLowerTest() {
        int count = 20;
        for(int i=0 ; i<count ; i++) {
            String randomString = webUtilService.getRandomAlphabetLower(i);
            assertThat(randomString, is(notNullValue()));
            assertThat(randomString.length(), is(i));

            // 모두 소문자 인지 체크 하기 위해서 random 문자열을 소문자로 바꿔서
            // 원래 문자열과 소문자 변경 문자열 비교
            String lowerTest = StringUtils.lowerCase(randomString);
            assertThat(StringUtils.equals(randomString, lowerTest), is(true));
        }
    }

}
