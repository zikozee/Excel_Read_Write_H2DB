package com.hbng.miniapp.charger.services;

import com.hbng.miniapp.charger.abstractClass.ExcelFeatures;
import com.hbng.miniapp.charger.model.Customer;
import com.hbng.miniapp.charger.model.ProcessedCustomer;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class ExcelUtilImpl extends ExcelFeatures implements ExcelUtil {

    private CustomerService customerService;
    private ProcessedCustomerService processedCustomerService;

    public ExcelUtilImpl(CustomerService customerService, ProcessedCustomerService processedCustomerService) {
        this.customerService = customerService;
        this.processedCustomerService = processedCustomerService;
    }

    @Override
    public void loadExcel(String excelPath) {
//        customerList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(excelPath));
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            //Iterate through each row from first sheet
            Iterator<Row> rows = sheet.rowIterator();

            while (rows.hasNext()) {
                Row row = rows.next();
                if (row.getRowNum() == 0) continue;//skip HEADER row

                Iterator<Cell> cellIterator = row.cellIterator();

                ArrayList<String> dataColumn = new ArrayList<>();
                //Iterate through each cell in row
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    dataColumn.add(cellAsString(cell));
                }
                Customer myCustomer = new Customer();
                myCustomer.setAccountNumber(dataColumn.get(0));
                myCustomer.setTransactedAmount(convertToDouble(dataColumn.get(1)));

                log.info("<<<<<<< PROCESSING TRANSACTION >>>>>>>    " + myCustomer);

//                customerList.add(myCustomer);
                customerService.save(myCustomer);
                sn++;
            }
        } catch (IOException | IndexOutOfBoundsException e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    @Override
    public void writeExcel(String destPath) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Merchant Charges");
            //Header font styling

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontName("Arial Narrow");
            headerFont.setFontHeightInPoints((short) 14);
            int rowIdx = 0;
            short cellIdx = 0;
            //Header
            Row headerRow = sheet.createRow(rowIdx);
            headerRow.setHeightInPoints(26);
            XSSFCellStyle headerCellStyle = getXssfCellStyle(workbook, headerFont);

            for (String aEXCEL_HEADER : EXCEL_HEADER) {
                Cell cell = headerRow.createCell(cellIdx++);
                cell.setCellStyle(headerCellStyle);
                cell.setCellValue(aEXCEL_HEADER);
            }

            CellStyle cellStyle = getFont_CellStyle(workbook, sheet);
            List<ProcessedCustomer> processedCustomersList = processedCustomerService.findAll();
            writingLoop(sheet, cellStyle, processedCustomersList);

            FileOutputStream fileOutput = new FileOutputStream(destPath);
            workbook.write(fileOutput);
            fileOutput.flush();
            fileOutput.close();
            log.info("<<<<<<<  WRITTEN TO ProcessedExcel.xlsx SUCCESSFULLY >>>>>>>    ");
        } catch (IOException e) {
            e.getMessage();
        }
    }


    @Override
    public BigDecimal checkBigDecimal(String value) {
        return checkBigValue(value);
    }
}
