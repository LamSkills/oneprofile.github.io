package com.newlight77.sample;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelServiceSample {

    public static void main(String[] args) {

        ExcelService service = new ExcelService(new Config());
        File file = service.create(new Items());

        //send file
    }

}

class Config {

    private String dateformat = "yyyyMMdd-HHmmss-SSS";
    private String sheetName = "Sheet name";
    private String font = "Arial";
    private String headerTitle = "Header Title";
    private String addressTitle = "Address";
    private String itemsTitle = "Items";
    private String[] itemsHeaders = new String[] {"Streee, city, postal code"};

    public String getDateformat() {
        return dateformat;
    }

    public String getSheetName() {
        return sheetName;
    }

    public String getFont() {
        return font;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public String getItemsTitle() {
        return itemsTitle;
    }

    public String[] getItemsHeaders() {
        return itemsHeaders;
    }
}

class Item {
    private String uid;
    private String name;
    private String quantity;
    private String status;

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}

class Address {
    private String street;
    private String postalCode;
    private String city;

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }
}

class Items {

    private Address address;
    private List<Item> items;

    public Address getAddress() {
        return address;
    }

    public List<Item> getItems() {
        return items;
    }
}

public class ExcelService {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelServiceSample.class);

    private Config excelConfig;

    public ExcelService(Config excelConfig) {
        this.excelConfig = excelConfig;
    }

    public File create(Items items) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = sheet(workbook);
        header(workbook, sheet);
        CellStyle titleStyle = titleStyle(workbook);
        CellStyle style = cellStyle(workbook);
        
        int startRow = 2;
        startRow = adresse(items, sheet, titleStyle, style, startRow++);
        long oks = items.getItems().stream().filter(d -> "OK".equals(d.getStatus())).count();
        items(items, sheet, titleStyle, style, startRow);
        return createFile(items, workbook, oks);
    }

    private Sheet sheet(Workbook workbook) {
        Sheet sheet = workbook.createSheet(excelConfig.getSheetName());
        sheet.setColumnWidth(0, 10500);
        for (int i = 1; i <= 18; i++) {
            sheet.setColumnWidth(i, 6000);
        }
        return sheet;
    }

    private CellStyle cellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        return style;
    }

    private CellStyle titleStyle(Workbook workbook) {
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont titleFont = ((XSSFWorkbook) workbook).createFont();
        titleFont.setFontName(excelConfig.getFont());
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setUnderline(FontUnderline.SINGLE);
        titleStyle.setFont(titleFont);
        return titleStyle;
    }

    private void header(Workbook workbook, Sheet sheet) {
    	
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName(excelConfig.getFont());
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setWrapText(false);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue(excelConfig.getHeaderTitle());
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
    }
    
    private static void createCell(String cellValue, Row row, int indext, CellStyle style) {
    	String value = StringUtils.isNotEmpty(cellValue) ? cellValue : "";
    	Cell cell = row.createCell(indext);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private int adresse(Items items, Sheet sheet, CellStyle titleStyle, CellStyle style, int startRow) {
    	
    	LOGGER.info("Produce Address from row {}", startRow);
    	
    	Row row = sheet.createRow(startRow);
        ExcelServiceSample.createCell(excelConfig.getAddressTitle(), row, 0, titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 0, 1));
        
        startRow = startRow + 1;
        
        Row adresseSiteRow = sheet.createRow(startRow++);
        Cell adresseSiteCell = adresseSiteRow.createCell(0);
        Address address = items.getAddress();
        StringBuffer addressSb = new StringBuffer();
        addressSb.append(address.getStreet()).append(' ');
        addressSb.append(address.getCity()).append(' ');
        addressSb.append(address.getPostalCode()).append(' ');
        adresseSiteCell.setCellValue(addressSb.toString());
        adresseSiteCell.setCellStyle(style);
        startRow = startRow + 1;
        
        return startRow;
    }

    private void items(Items items, Sheet sheet, CellStyle titleStyle, CellStyle style, int startRow) {
    	
    	Row row = sheet.createRow(startRow);
        ExcelService.createCell(excelConfig.getItemsTitle(), row, 0, titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 0, 1));
        
        startRow = startRow + 1;
        
        // Headers vertical
        String[] headers = excelConfig.getItemsHeaders();
        row = sheet.createRow(startRow++);
        for(int cellIdx = 0; cellIdx < headers.length; cellIdx++) {
            ExcelService.createCell(headers[cellIdx], row, cellIdx, style);
        }

        for (Item item : items.getItems()) {
        	row = sheet.createRow(startRow++);
            ExcelService.createCell(item.getUid(), row, 0, style);
            ExcelService.createCell(item.getName(), row, 1, style);
            ExcelService.createCell(String.valueOf(item.getQuantity()), row, 2, style);
            String status = (item.getStatus() != null ? item.getStatus().toString() : "UNKNOWN");
            ExcelService.createCell(status, row, 3, style);
        }
    }

    private File createFile(Items items, Workbook workbook, long numberOK) {
        StringBuffer xlsxFileSb = fileName(items, numberOK);
        File resultFile = new File(System.getProperty("java.io.tmpdir"), xlsxFileSb.toString());
        try {
            FileOutputStream outputStream = new FileOutputStream(resultFile);
            workbook.write(outputStream);
        } catch (Exception ex) {
            LOGGER.error("Error", ex);
        } finally {
            try {
                workbook.close();
            } catch (Exception e) {
                // CANT HAPPEN
            }
        }
        return resultFile;
    }

    private StringBuffer fileName(Items items, long numberOK) {
        StringBuffer xlsxFileSb = new StringBuffer();
        xlsxFileSb.append("items");
        xlsxFileSb.append('-').append(dateTimeFormatter.format(LocalDateTime.now()));
        xlsxFileSb.append('-').append(items.getAddress().getPostalCode());
        xlsxFileSb.append('-').append(numberOK);
        xlsxFileSb.append('-').append(items.getItems().size());

        xlsxFileSb.append(".xlsx");
        return xlsxFileSb;
    }
}
