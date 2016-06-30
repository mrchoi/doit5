package com.ckstack.ckpush.controller.admin;

import com.ckstack.ckpush.common.MDV;
import com.ckstack.ckpush.service.accessory.WebUtilService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by dhkim94 on 15. 3. 30..
 *
 * 인증 없이 접속 가능한 페이지 처리
 */
@Controller
@RequestMapping("/admin/open")
public class OpenController {
    private final static Logger LOG = LoggerFactory.getLogger(OpenController.class);

    @Autowired
    private WebUtilService webUtilService;

    /**
     * 로그인 페이지를 보여 준다.
     *
     * x-editable 요청시 security broken 발생하면 로그인 페이지로 튕기기 위해서
     * request method 를 GET 으로 한정 하지 않았음.
     *
     * @return 로그인 페이지 view name
     */
    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
        // ajax GET 요청이 아니라면 자동으로 로그인 페이지로 튕귄다.

        // 만일 ajax 요청이라면 (브라우저에서 요청) 로그인 페이지로 이동하는 view 를 보여 준다.
        // 브라우저에서 새로운 탭이나 새로운 창으로 신규 session 생성 되었을때
        // 기존 세션에서 action 을 하면 security error 발생 한다.
        // 이럴때 로그인 페이지로 강제로 튕겨 버리기 위해서 로그인에서도 체크 되어야 한다.(필터에서 100% 체크 안됨)
        String ajaxHeader = request.getHeader("X-Requested-With");
        if(StringUtils.equals(ajaxHeader, "XMLHttpRequest")) {
            LOG.info("user session broken. redirect login page");
            response.setStatus(MDV.HTTP_ERR_AJAX_SESSION_TIMEOUT);
            return "f_comm/login_redirect";
        }
        return "f_admin/open/login";
    }

    /**
     * 관리자 가입을 위한 폼을 보여 준다.
     *
     * @return 가입 페이지 view name
     */
    @RequestMapping(value = "/register")
    public String regist() {
        return "f_admin/open/regist";
    }

    /**
     * 사용자 추가 후, 기존 관리자에게 가입 요청을 보낸다.
     *
     * @return
     */
    @RequestMapping(value = "/request/register", method = RequestMethod.POST)
    public String registRequest() {

        // TODO 신규 가입자 DB에 추가 하고, 기존 관리자에게 신규 가입자 정보를 보내야 한다. 추후 진행 하자.

        return "";
    }

    /**
     * UUID 를 생성 하여 리턴 한다.
     * @param tid request transaction id
     * @return ModelAndView object
     */
    @RequestMapping(value = "/uuid/t/{tid}", method = RequestMethod.GET)
    public ModelAndView makeUUID(@PathVariable("tid") String tid) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        ModelAndView mav = new ModelAndView();

        mav.setViewName("f_api/not_support");
        webUtilService.setCommonApiResponse(mav, tid, "success");
        mav.addObject("uuid", uuid);

        LOG.info("make uuid["+uuid+"]");

        return mav;
    }
}
