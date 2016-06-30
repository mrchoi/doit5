package com.ckstack.ckpush.service.accessory;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by dhkim94 on 15. 3. 18..
 */
public interface WebUtilService {
    /**
     * http request url 에서 tid 를 구한다.
     * @param httpServletRequest request object
     * @return request uri 에서 획득한 tid
     */
    String getTid(HttpServletRequest httpServletRequest);

    /**
     * REST API 요청시 response 의 공통으로 들어가는 부분을 처리 한다.
     * @param mav ModelAndView object(output)
     * @param tid request transaction id
     * @param code error code string. 만일 정의되지 않은 code 라면 failed 를 기본으로 사용 한다.
     */
    void setCommonApiResponse(ModelAndView mav, String tid, String code);

    /**
     * http form request 방식의 API 에서 공통 에러 부분을 처리 한다.
     * @param mav ModelAndView object(output)
     * @param request HttpServletRequest object
     * @param code error code string. 만일 정의되지 않은 code 라면 failed 를 기본으로 사용 한다.
     * @param reason 에러 원인. 만일 값이 없다면 null 로 주면 된다.
     * @return chain 형태를 지원 하기 위해서 받은 mav 를 리턴함
     */
    ModelAndView setCommonErrorResponse(ModelAndView mav, HttpServletRequest request,
                                   String code, Map<String, String> reason);

    /**
     * 숫자로 이루어진 phone_number 로 변환 시킨다.
     * @param phoneNumber 전화 번호
     * @return 숫자로만 이루어진 전화번호
     */
    String getPhoneNumber(String phoneNumber);

    /**
     * smart admin 에서 ajax 로딩을 할 anchor URL 을 구한다.
     * @param request HttpServletRequest object
     * @param anchor 이동할 anchor URI. anchor URI 에는 context path 는 빠져 있다.
     * @return http://localhost:8080/1/admin#/1/app/add 형태의 with anchor URL 을 구한다.
     */
    String getAnchorWithCommonRequestURL(HttpServletRequest request, String anchor);

    /**
     * http request ip 를 구한다.
     * @param request HttpServletRequest object
     * @return request ip
     */
    String getRequestIp(HttpServletRequest request);

    /**
     * datatable 의 parameter 를 파싱 한다. datatable 의 파라미터는 다음 형태로 요청 된다.
     * order 는 order 되는 개수 만큼, columns 는 테이블 칼럼 개수 만큼
     *
     * draw = 1
     * order[0][column] - 숫자임
     * order[0][dir]
     * start
     * length
     * search[value]
     * search[regex]
     * columns[0][data]
     * columns[0][name]
     * columns[0][searchable]
     * columns[0][orderable]
     * columns[0][search][value]
     * columns[0][search][regex]
     *
     * @param columnValue 칼럼 파라미터 데이터(output)
     * @param orderValue order 파라미터 데이터(output). order 는 순서가 있기 때문에 LinkedHashMap 을 사용 해야 한다.
     * @param extraValue 기타 파라키터 데이터(output)
     * @param request HttpServletRequest object
     * @return jquery datatable 파라미터를 파싱 한다. column 값 들은 columnValue 에 저장되고
     *         order 값들은 orderValue 저장 되며, 나머지 값은 extraValue 에 저장 된다.
     */
    boolean parsingDataTableParameter(Map<String, Map<String, String>> columnValue,
                                      Map<String, String> orderValue,
                                      Map<String, String> extraValue,
                                      HttpServletRequest request);

    /**
     * 파싱된 data table 파라미터로 부터 검색 칼럼, 검색값 정보를 구한다.
     *
     * @param columnValue 파싱된 data table 칼럼 정보
     * @param extraValue 검색 할때 칼럼별로 검색 하지 않고 전체로 검색 할때 검색값을 넣기 위해서 필요함
     * @return 검색 칼럼, 검색값이 담겨 있는 map
     */
    Map<String, String> getSearchFilter(Map<String, Map<String, String>> columnValue,
                                        Map<String, String> extraValue);

    /**
     * 알파벳 소문자로 이루어진 랜덤한 문자열을 만든다.
     * @param length 원하는 문자열 길이
     * @return 알파벳 소문자로 이루어진 랜덤한 문자열
     */
    String getRandomAlphabetLower(int length);

    /**
     * 서버에 저장되어 있는 파일의 접근 URI 를 만든다.
     * scheme, domain 은 제외하고 URI 만 만든다.
     *
     * @param httpServletRequest HttpServletRequest object
     * @param fileSrl 파일 시리얼 넘버
     * @param fileType 파일 종류
     * @return 파일 URI
     */
    String getFileURI(HttpServletRequest httpServletRequest, long fileSrl, String fileType);

    /**
     * URL을 받아서 파일을 다운로드 한다.
     *
     * @param url 다운로드 받을 파일의 URL
     * @return 다운로드 받은 데이터
     */
    byte[] serverToServerDownload(String url);

    /**
     * object를 json byte로 변환 한다.
     *
     * @param obj json string으로 변환할 object
     * @return json byte
     */
    byte[] convertObjectToJsonBytes(Object obj);

    /**
     * byte array(json string)에서 object로 변환 한다.
     * className에 맞는 class의 object를 생성 한다.
     *
     * [사용법]
     * String str = "{\"member_srl\":123, \"nick_name\":\"홍길동1\"}";
     * webUtilService.convertJsonToObject("com.ckstack.ckpush.data.cache.AccessTokenExtra", str.getBytes("utf-8"))
     *
     * @param className 변환할 object의 클래스명
     * @param jsonBytes json string에서 변환한 byte array
     * @return 변환된 object. 받는 곳에서 형태에 맞게 casting 해서 사용하면 된다.
     */
    Object convertJsonBytesToObject(String className, byte[] jsonBytes);

    /**
     * 정의된 숫자에 맞는 소셜 타입(소셜 헤더값)을 구한다.
     *
     * @param socialType 미리 정의된 숫자 형태의 소셜 타입
     * @return 소셜 타입. 만일 존재하지 않으면 null을 리턴한다.
     */
    String getSocialHeader(String socialType);
}
