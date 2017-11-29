package com.acs.map.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
// TODO: 29/11/2017 doesn't work, returns `PageNotFound: Request method 'GET' not supported`
public class MapController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "public/google/map.html";
    }
}
