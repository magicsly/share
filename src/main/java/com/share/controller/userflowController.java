package com.share.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ElNino on 15/6/17.
 */
@Controller
@RequestMapping("/")
public class userflowController {

    @RequestMapping(value = "/userflow")
    @ResponseBody
    public Map userflow() {

        Map<String,Object> map = new HashMap<String, Object>();
        return map;
    }
}
