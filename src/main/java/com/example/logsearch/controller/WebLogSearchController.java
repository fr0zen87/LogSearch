package com.example.logsearch.controller;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SearchInfoResult;
import com.example.logsearch.entities.SignificantDateInterval;
import com.example.logsearch.service.LogService;
import com.example.logsearch.utils.ConfigProperties;
import com.example.logsearch.utils.WebSearchInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class WebLogSearchController {

    private final LogService logService;
    private final ConfigProperties configProperties;

    @Autowired
    public WebLogSearchController(LogService logService,
                                  @Qualifier(value = "configProperties") ConfigProperties properties) {
        this.logService = logService;
        this.configProperties = properties;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof SearchInfo) {
            binder.setValidator(new WebSearchInfoValidator());
        }
    }

    @ModelAttribute("allExtensions")
    public List<FileExtension> populateTypes() {
        return Arrays.asList(FileExtension.values());
    }

    @GetMapping(value = "/logSearch")
    public String logSearch(Model model) {
        SearchInfo searchInfo = new SearchInfo();
        model.addAttribute(searchInfo);
        return "logSearch";
    }

    @PostMapping(value = "/logSearch")
    public String logSearch(@Valid SearchInfo searchInfo, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            //modelAndView.addObject(new SearchInfo());
            //return modelAndView;
            return "logSearch";
        }
        SearchInfoResult searchInfoResult = logService.logSearch(searchInfo);
        if (searchInfo.isRealization()) {
            modelAndView.setViewName("link");
            modelAndView.addObject("link", configProperties.getFileLink());
            modelAndView.addObject("forHref", "file:///" + configProperties.getFileLink());
        } else {
            modelAndView.setViewName("result");
            modelAndView.addObject("searchInfoResult", searchInfoResult);
        }
        //return modelAndView;
        return "logSearch";
    }

    @RequestMapping(value="/logSearch", params={"addRow"})
    public String addRow(final SearchInfo searchInfo, final BindingResult bindingResult) {
        searchInfo.getDateIntervals().add(new SignificantDateInterval());
        return "logSearch";
    }

    @RequestMapping(value="/logSearch", params={"removeRow"})
    public String removeRow(final SearchInfo searchInfo, final BindingResult bindingResult, final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
        searchInfo.getDateIntervals().remove(rowId.intValue());
        return "logSearch";
    }
}
