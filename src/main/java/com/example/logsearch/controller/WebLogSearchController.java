package com.example.logsearch.controller;

import com.example.logsearch.entities.FileExtension;
import com.example.logsearch.entities.SearchInfo;
import com.example.logsearch.entities.SignificantDateInterval;
import com.example.logsearch.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
public class WebLogSearchController {

    private final LogService logService;

    @Autowired
    public WebLogSearchController(LogService logService) {
        this.logService = logService;
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
    public String logSearch(SearchInfo searchInfo, BindingResult bindingResult, Model model) {
        logService.logSearch(searchInfo);
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
