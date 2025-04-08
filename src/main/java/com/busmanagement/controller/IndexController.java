package com.busmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the homepage and common pages
 */
@Controller
public class IndexController {

    /**
     * Return the homepage
     * @return The index view
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    /**
     * Return the homepage (alternative path)
     * @return The index view
     */
    @GetMapping("/index")
    public String indexAlt() {
        return "index";
    }
}