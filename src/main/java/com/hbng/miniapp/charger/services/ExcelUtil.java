package com.hbng.miniapp.charger.services;

import java.math.BigDecimal;

public interface ExcelUtil {
    void loadExcel(String excelPath);
    void writeExcel(String destPath);

    BigDecimal checkBigDecimal(String value);
}
