package com.myke.other.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhangjianbin
 * @date 2021年07月27日10:39
 */
@Controller
public class FreeMarkController {

    /**
     * ModelMap就相当于SpringMVC中的ModelAndView
     *
     * @param map
     *
     * @return
     */
    @GetMapping("/home")
    public String index(ModelMap map) {
        // 加入一个属性，用来在模板中读取
        map.addAttribute("content", "welcome");
        // return模板文件的名称，对应src/main/resources/templates/Hello.ftl
        return "Hello";
    }

    @GetMapping("/excel")
    public String excel() {
        return "exportExcel";
    }
}
