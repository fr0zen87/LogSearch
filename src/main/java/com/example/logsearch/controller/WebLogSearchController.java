package com.example.logsearch.controller;

import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.service.WebLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebLogSearchController {

    private final WebLogService webLogService;

    @Autowired
    public WebLogSearchController(WebLogService webLogService) {
        this.webLogService = webLogService;
    }

    @GetMapping(value = "/logSearch")
    public String logSearch(Model model) {
        SearchInfo searchInfo = new SearchInfo();
        model.addAttribute(searchInfo);
        return "logSearch";
    }

    @PostMapping(value = "/logSearch")
    public String logSearch(SearchInfo searchInfo, BindingResult bindingResult, Model model) {
        webLogService.logSearch(searchInfo);
        return "logSearch";
    }
}
