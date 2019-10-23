package com.hbng.miniapp.charger.controller;

import com.hbng.miniapp.charger.services.ExcelUtil;
import com.hbng.miniapp.charger.services.ProcessedCustomerService;
import com.hbng.miniapp.charger.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class ExcelController {
    private final StorageService storageService;
    private final ProcessedCustomerService processedCustomerService;
    private final ExcelUtil excelUtil;

    public ExcelController(StorageService storageService, ProcessedCustomerService processedCustomerService, ExcelUtil excelUtil) {
        this.storageService = storageService;
        this.processedCustomerService = processedCustomerService;
        this.excelUtil = excelUtil;
    }

    @PostMapping("/readExcel")
    public String readExcel(RedirectAttributes redirectAttributes) {
        //use below
        log.info("Uploaded File Path " + storageService.getFilePath());
        excelUtil.loadExcel(storageService.getFilePath());
        redirectAttributes.addFlashAttribute("message", "Excel file read and stored to Database successfully!!! ");

        return "redirect:/";
    }

    @PostMapping("/computeValue")
    public String computeValue(RedirectAttributes redirectAttributes,
                               @RequestParam("amount") String value, @RequestParam("limit") String limitValue) {

        if(value == null|| limitValue == null){
            redirectAttributes.addFlashAttribute("message", "Amount to be charged And Threshold value cannot be empty");
            return "redirect:/";
        }
        double checkedValue = excelUtil.checkDouble(value);
        double checkedLimit = excelUtil.checkDouble(limitValue);

        processedCustomerService.loadProcessedCustomer(checkedValue, checkedLimit);
        redirectAttributes.addFlashAttribute("message", "Computed values successfully");

        return "redirect:/";
    }

    @PostMapping("/writeExcel")
    public String write(RedirectAttributes redirectAttributes) {
        String destPath  = storageService.getDestinationPath();
        log.info("dest path "+ destPath);
        excelUtil.writeExcel(destPath);
        redirectAttributes.addFlashAttribute("message", "Excel Written Successfully >>" +
                " Click the Link to download the Processed Excel");

        return "redirect:/";
    }


}
