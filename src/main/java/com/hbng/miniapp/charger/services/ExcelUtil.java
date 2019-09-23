package com.hbng.miniapp.charger.services;

public interface ExcelUtil {
    void loadExcel(String excelPath);
    void writeExcel(String destPath);

    double checkDouble(String value);
}
