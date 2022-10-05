package com.erdioran.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Platform;


public class ExcelManager {
    public static final String       testDataExcelFileName = "testdata.xlsx";
    public static final String       currentDir            = System.getProperty("user.dir");
    public static       String       testDataExcelPath     = null;
    private static      XSSFWorkbook excelWBook;
    private static      XSSFSheet    excelWSheet;
    private static      XSSFCell     cell;
    private static      XSSFRow      row;
    public static       int          rowNumber;
    public static       int          columnNumber;

    @SneakyThrows
    public static void setExcelFileSheet(String sheetName) {
        if (Platform.getCurrent().toString().equalsIgnoreCase("MAC")) {
            testDataExcelPath = currentDir + "/src/test/resources/";
        } else if (Platform.getCurrent().toString().contains("WIN")) {
            testDataExcelPath = currentDir + "\\src\\test\\resources\\";
        }
        FileInputStream ExcelFile = new FileInputStream(testDataExcelPath + testDataExcelFileName);
        excelWBook = new XSSFWorkbook(ExcelFile);
        excelWSheet = excelWBook.getSheet(sheetName);
    }


    public static String getCellData(int RowNum, int ColNum) {
        cell = excelWSheet.getRow(RowNum).getCell(ColNum);
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }


    public static XSSFRow getRowData(int RowNum) {
        row = excelWSheet.getRow(RowNum);
        return row;
    }


    @SneakyThrows
    public static void setCellData(String value, int RowNum, int ColNum) {
        row = excelWSheet.getRow(RowNum);
        cell = row.getCell(ColNum);
        if (cell == null) {
            cell = row.createCell(ColNum);
            cell.setCellValue(value);
        } else {
            cell.setCellValue(value);
        }
        FileOutputStream fileOut = new FileOutputStream(testDataExcelPath + testDataExcelFileName);
        excelWBook.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }


    public static void setExcel(String sheetName,String value, int RowNum, int ColNum){

        setExcelFileSheet(sheetName);
        setCellData(value,RowNum,ColNum);
    }
}
