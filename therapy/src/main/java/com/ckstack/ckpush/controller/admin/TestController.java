package com.ckstack.ckpush.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by dhkim94 on 15. 3. 11..
 */
@Controller
@RequestMapping("/test")
public class TestController {
    private final static Logger LOG = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        LOG.debug("show hello");
        return "hello";
    }
}
