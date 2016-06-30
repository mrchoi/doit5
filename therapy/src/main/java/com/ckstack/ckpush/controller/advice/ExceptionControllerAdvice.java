package com.ckstack.ckpush.controller.advice;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import com.ckstack.ckpush.common.CustomException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by dhkim94 on 15. 3. 18..
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
    private final static Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @Autowired
    protected Properties confCommon;
    @Autowired
    protected Properties confDym;
    @Autowired
    protected WebUtilService webUtilService;

    /**
     * Database 연결 실패
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(CannotCreateTransactionException.class)
    public ModelAndView cannotConnectDatabase(HttpServletRequest request,
                                              HttpServletResponse response,
                                              CannotCreateTransactionException e) {
        response.setStatus(500);

        Map<String, String> reason = new HashMap<>();
        reason.put("response", "database error1");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "cannot_connect_database", reason);

        LOG.error("Force HTTP 500 error. cannot connect database. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception[" + e.getMessage()+"]");

        return mav;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ModelAndView duplicatedKeyError(HttpServletRequest request,
                                           HttpServletResponse response,
                                           DataIntegrityViolationException e) {
        response.setStatus(500);

        Map<String, String> reason = new HashMap<>();
        reason.put("request", "duplicate key error");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "database_duplicated_key_error", reason);

        LOG.error("Force HTTP 500 error. Database duplicated key error. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception[" + e.getMessage() + "]");

        return mav;
    }

    /**
     * Database 제약 조건 오류로 발생하는 exception 처리
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView databaseConstraintError(HttpServletRequest request,
                                                HttpServletResponse response,
                                                DataIntegrityViolationException e) {
        response.setStatus(500);

        Map<String, String> reason = new HashMap<>();
        reason.put("response", "database error2");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "database_constraint_error", reason);

        LOG.error("Force HTTP 500 error. Database constraint error. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception[" + e.getMessage() + "]");

        return mav;
    }

    /**
     * Database 일반 에러
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(DataAccessException.class)
    public ModelAndView databaseCommonError(HttpServletRequest request,
                                            HttpServletResponse response,
                                            DataAccessException e) {
        response.setStatus(500);

        Map<String, String> reason = new HashMap<>();
        reason.put("response", "database error3");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "database_operation_error", reason);

        LOG.error("Force HTTP 500 error. database job error. ck_request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception["+e.getMessage()+"]");

        return mav;
    }

    /**
     * redis 연결 실패
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(RedisConnectionFailureException.class)
    public ModelAndView redisConnectionError(HttpServletRequest request,
                                             HttpServletResponse response,
                                             RedisConnectionFailureException e) {
        response.setStatus(500);

        Map<String, String> reason = new HashMap<>();
        reason.put("response", "cache server connection error1");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "redis_connection_error", reason);

        LOG.error("Force HTTP 500 error. redis connection error. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception[" + e.getMessage() + "]");

        return mav;
    }

    /**
     * redis 연결 실패
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(JedisConnectionException.class)
    public ModelAndView jedisConnectionError(HttpServletRequest request,
                                             HttpServletResponse response,
                                             JedisConnectionException e) {
        response.setStatus(500);

        Map<String, String> reason = new HashMap<>();
        reason.put("response", "cache server connection error2");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "redis_connection_error", reason);

        LOG.error("Force HTTP 500 error. redis connection error. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception[" + e.getMessage() + "]");

        return mav;
    }

    /**
     * http session 처리 실패
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(HttpSessionRequiredException.class)
    public ModelAndView sessionError(HttpServletRequest request,
                                     HttpServletResponse response,
                                     HttpSessionRequiredException e) {
        response.setStatus(500);

        Map<String, String> reason = new HashMap<>();
        reason.put("response", "session error");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "http_session_data_error", reason);

        LOG.error("Force HTTP 500 error. http session error. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception[" + e.getMessage() + "]");

        return mav;
    }

    /**
     * http request 멀티파트 처리 실패
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(MultipartException.class)
    public ModelAndView multiPartFileError(HttpServletRequest request,
                                           HttpServletResponse response,
                                           MultipartException e) {
        response.setStatus(400);

        Map<String, String> reason = new HashMap<>();
        reason.put("request", "not found multipart data");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "multipart_no_binary_file_error", reason);

        LOG.error("Force HTTP 400 error. multipart error. not found binary data. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception["+e.getMessage()+"]");

        return mav;
    }

    /**
     * http request 에서 request 메시지 파싱 실패
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ModelAndView messagePasingError(HttpServletRequest request,
                                           HttpServletResponse response,
                                           HttpMessageNotReadableException e) {
        response.setStatus(400);

        Map<String, String> reason = new HashMap<>();
        reason.put("request", "message parsing error");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "not_support_param_error", reason);

        LOG.error("Force HTTP 400 error. request parameter parsing error. not support parameter included. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception[" + e.getMessage() + "]");

        return mav;
    }

    /**
     * type casting 에러
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(ClassCastException.class)
    public ModelAndView castingError(HttpServletRequest request,
                                     HttpServletResponse response,
                                     ClassCastException e) {
        response.setStatus(500);

        Map<String, String> reason = new HashMap<>();
        reason.put("response", "type casting error");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "type_casting_error", reason);

        LOG.error("Force HTTP 500 error. type casting error. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception[" + e.getMessage() + "]");

        return mav;
    }

    /**
     * http request method 가 잘못된 경우
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView methodNotSupport(HttpServletRequest request,
                                         HttpServletResponse response,
                                         HttpRequestMethodNotSupportedException e) {
        // 인터셉터 통하기 전에 발생되는 에러이기 때문에 여기에서 resource directory를 잡아 줘야 함
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String contextPath = urlPathHelper.getContextPath(request);
        String resPath;

        if(StringUtils.equals(confDym.getProperty("resource_location"), "1"))
            resPath = contextPath + confDym.getProperty("resource_path");
        else
            resPath = confDym.getProperty("resource_path");
        request.setAttribute("resPath", resPath);

        response.setStatus(MDV.HTTP_ERR_X_REQUEST_405);

        Map<String, String> reason = new HashMap<>();
        reason.put("request", "not support request method");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "page_405_error", reason);

        LOG.error("Force HTTP 405 error. not support method. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri"))
                + "], method[" + request.getMethod() + "] exception[" + e.getMessage() + "]");

        return mav;
    }

    /**
     * 임의로 생성한 exception 처리
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(CustomException.class)
    public ModelAndView customError(HttpServletRequest request,
                                    HttpServletResponse response,
                                    CustomException e) {
        if(e.getHttpStatus() > 0) response.setStatus(e.getHttpStatus());

        Map<String, String> reason = e.getReason();
        if(reason == null) {
            reason = new HashMap<>();
            reason.put("request", "custom exception");
        }

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, e.getCode(), reason);

        if(e.getHttpStatus() > 0)
            LOG.error("Force HTTP " + e.getHttpStatus() + " error. Custom exception request_uri[" +
                    mav.getModel().get(confCommon.getProperty("json_request_uri")) + "], ck_tid[" +
                    mav.getModel().get(confCommon.getProperty("json_tid")) + "], ck_error[" +
                    mav.getModel().get(confCommon.getProperty("json_error")) + "], ck_message[" +
                    mav.getModel().get(confCommon.getProperty("json_message")) + "], httpStatus[" +
                    e.getHttpStatus()+"]");
        else
            LOG.error("Custom exception request_uri[" + mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                    "], ck_tid[" + mav.getModel().get(confCommon.getProperty("json_tid")) + "], ck_error[" +
                    mav.getModel().get(confCommon.getProperty("json_error")) + "], ck_message[" +
                    mav.getModel().get(confCommon.getProperty("json_message")) + "], httpStatus[" +
                    e.getHttpStatus()+"]");

        return mav;
    }

    /**
     * 정의 되지 않은 exception 발생
     * @param request http servlet request
     * @param response http servlet response
     * @param e exception
     * @return exception result
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView commonError(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Exception e) {
        response.setStatus(500);

        Map<String, String> reason = new HashMap<>();
        reason.put("response", "undefined error");

        ModelAndView mav = new ModelAndView();
        webUtilService.setCommonErrorResponse(mav, request, "undefined_exception_throw_error", reason);

        LOG.error("Force HTTP 500 error. undefined exception. request_uri[" +
                mav.getModel().get(confCommon.getProperty("json_request_uri")) +
                "] exception [" + e.getMessage() + "]");

        e.printStackTrace();

        return mav;
    }
}
