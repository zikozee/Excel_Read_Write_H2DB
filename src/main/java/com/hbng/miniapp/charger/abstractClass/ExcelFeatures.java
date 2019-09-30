package com.hbng.miniapp.charger.abstractClass;

import com.hbng.miniapp.charger.globalHandling.MyNumberFormatException;
import com.hbng.miniapp.charger.model.ProcessedCustomer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public abstract class ExcelFeatures {

    protected LinkedList<String> EXCEL_HEADER=
            new LinkedList<>(Arrays.asList("Merchant AccNumber", "Num of Transaction(s)","levy", "Total Charges"));

    protected long sn =0;

    protected double convertToDouble(String amountInString){
        try{
            return Double.parseDouble(amountInString);
        }catch (NumberFormatException e){
            throw new MyNumberFormatException("ONE OR MORE NUMBERS ARE NOT PROPERLY " +
                    "FORMATTED CHECK ROW " + (sn-2) + " TO "+(sn+5) + " >>>CORRECT AND RESTART APPLICATION");
        }
    }

    protected double checkDoubleValue(String amountInString){
        try{
            return Double.parseDouble(amountInString);
        }catch (NumberFormatException e){
            throw new MyNumberFormatException("Amount to be charged Or Threshold value is not a number");
        }
    }

    protected static String cellAsString(Cell cell) {
        String strCellValue = null;
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    strCellValue = cell.toString();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
                        strCellValue = dateFormat.format(cell.getDateCellValue());
                    } else {
                        Double value = cell.getNumericCellValue();
                        long longValue = value.longValue();
                        strCellValue = Long.toString(longValue);
                    }
                    break;
                case BOOLEAN:
                    strCellValue = Boolean.toString(cell.getBooleanCellValue());
                    break;
                case BLANK: case ERROR: case _NONE: case FORMULA:
                    strCellValue = "EMPTY";
                    break;
                default:
                    break;
            }
        }
        return strCellValue;
    }

    protected ArrayList ObjectToArrayList(ProcessedCustomer processed){
        ArrayList<String> list = new ArrayList<>();
        String accountNumber = processed.getAccountNumber();
        int transactedTimes = processed.getTransactedTimes();
        double amount = processed.getChargedAmount();
        double total = processed.getPayable();


        Collections.addAll(list, accountNumber, String.valueOf(transactedTimes),
                String.valueOf(amount), String.valueOf(total));
        return list;
    }

    protected XSSFCellStyle getXssfCellStyle(XSSFWorkbook workbook, Font headerFont) {
        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //Border
        headerCellStyle.setBorderBottom(BorderStyle.THIN);
        headerCellStyle.setBorderLeft(BorderStyle.THIN);
        headerCellStyle.setBorderTop(BorderStyle.THIN);
        headerCellStyle.setBorderRight(BorderStyle.THIN);
        return headerCellStyle;
    }

    protected CellStyle getFont_CellStyle(XSSFWorkbook workbook, XSSFSheet sheet) {
        final XSSFFont font = sheet.getWorkbook().createFont();
        font.setFontName("Arial Narrow");
        font.setBold(true);
        font.setFontHeight(14.0);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(false);// for AutoColumn fit
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(font);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }

    protected void writingLoop(XSSFSheet sheet, CellStyle cellStyle, List<ProcessedCustomer> processedCustomersList) {
        short cellIdx;
        for (int sheetRow = 0; sheetRow < processedCustomersList.size(); sheetRow++) {
            Row row = sheet.createRow(sheetRow + 1);

            ArrayList rowArray = ObjectToArrayList((processedCustomersList.get(sheetRow)));

            cellIdx = 0;
            for (Object aRowArray : rowArray) {
                Cell cell = row.createCell(cellIdx++);
                cell.setCellValue((String) aRowArray);
                cell.setCellStyle(cellStyle);
                //To Adjust Column
                sheet.autoSizeColumn(cellIdx);
                /**
                 * TO DO AUTOSIZE RECEIVER'S ADDRESS
                 */
            }
            log.info("<<<<<<<  WRITING TO ProcessedExcel.xlsx >>>>>>>    ");
        }
    }

}
