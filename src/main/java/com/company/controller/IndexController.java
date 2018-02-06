package com.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by zhangguilin on 2/6/2018.
 */
@Controller
public class IndexController {

    @GetMapping("/index")
    public String Index(){
        return "index";
    }
}
