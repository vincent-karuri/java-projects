package com.example.karuri.vincent;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class HelloController {

    @RequestMapping(method = RequestMethod.GET)
    public String printMessage(ModelMap modelMap) {
        modelMap.addAttribute("message", "Spring MVC Example Webapp");
        return "hello";
    }
}
