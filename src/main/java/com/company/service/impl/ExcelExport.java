package com.company.service.impl;

import com.company.base.util.EmptyUtil;
import com.company.base.util.ExportUtil;
import com.company.base.util.ReflectionUtils;
import com.company.entity.ExportCell;
import com.company.entity.Type;
import com.company.exception.FileExportException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.company.service.FileExport;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangguilin on 1/31/2018.
 */
public class ExcelExport implements FileExport {
    @Override
    public Workbook getExportResult(List<?> data, List<ExportCell> exportCells) throws FileExportException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row titleRow = sheet.createRow(0);
        createTitleRow(workbook, titleRow, exportCells, sheet);
        /**
         * 判断data.getClass() 是不是 List.class.isAssignableFrom的子类或子接口
         * */
        if (List.class.isAssignableFrom(data.getClass())) {
            if (CollectionUtils.isNotEmpty(data)) {
                if (data.get(0) instanceof Map) {
                    createContentRowsByMap(workbook, (List<Map>) data, exportCells, sheet);
                } else if (data.get(0) instanceof Object) {
                    createContentRowsByBean(workbook, (List<Object>) data, exportCells, sheet);
                }
            }
        }
        return workbook;
    }

    /**
     * 解析List数据
     */
    private void createContentRowsByBean(Workbook workbook, List<Object> dataList, List<ExportCell> exportCells, Sheet sheet) throws FileExportException {
        int rowNum = 1;
        if (CollectionUtils.isNotEmpty(dataList)) {
            CellStyle cellStyle = createCellStyle(workbook);
            for (Object o : dataList) {
                Row row = sheet.createRow(rowNum);
                row.setHeightInPoints(23.0F);
                for (int colNum = 0; colNum < exportCells.size(); colNum++) {
                    Cell cell = row.createCell(colNum);
                    cell.setCellStyle(cellStyle);
                    ExportCell exportCell = exportCells.get(colNum);
                    Object obj = null;
                    try {
                        obj = ReflectionUtils.excuteMethod(o, ReflectionUtils.returnGetMethodName(exportCell.getAlias()));
                    } catch (Exception e) {
                        throw new FileExportException("执行executeMethod  出错 Alias is " + exportCell.getAlias() + " at " + e.getMessage());
                    }
                    ExportUtil.setCellValue(obj, cell);
                }
                ++rowNum;
            }
        }
    }


    /**
     * 解析Map数据
     */
    private static void createContentRowsByMap(Workbook workbook, List<Map> dataList, List<ExportCell> exportCells, Sheet sheet) throws FileExportException {
        if (CollectionUtils.isNotEmpty(dataList)) {
            int rowNum = 1;
            CellStyle cellStyle = createCellStyle(workbook);
            for (Map map : dataList) {
                Row row = sheet.createRow(rowNum);
                row.setHeightInPoints(23.0F);
                for (int colNum = 0; colNum < exportCells.size(); colNum++) {
                    Cell cell = row.createCell(colNum);
                    cell.setCellStyle(cellStyle);
                    ExportCell exportCell = exportCells.get(colNum);
                    Object obj = null;
                    obj = map.get(exportCell.getAlias());
                    if (exportCell.getExport().equals(ExportCell.Export.EXPORT)) {
                        ExportUtil.setCellValue(obj, cell);
                    }

                }
                ++rowNum;
            }
        }
    }


    /**
     * 设置标题行
     */
    private void createTitleRow(Workbook workbook, Row titleRow, List<ExportCell> exportCells, Sheet sheet) {
        CellStyle cellStyle = createTitleCellStyle(workbook);
        titleRow.setHeightInPoints(25.0F);
        Font font = workbook.createFont();
        font.setColor((short) 12);
        cellStyle.setFont(font);
        cellStyle.setFillBackgroundColor((short) 13);
        int i = 0;
        for (ExportCell exportCell : exportCells) {
            sheet.setColumnWidth(i, exportCell.getWidth() != null ? Integer.valueOf(exportCell.getWidth()) : 3600);
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(exportCell.getTitle());
            cell.setCellStyle(cellStyle);
            ++i;
        }
    }

    /**
     * 修改标题样式
     */
    public static CellStyle createTitleCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    /**
     * 修改正文样式
     */
    private static CellStyle createCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        return cellStyle;
    }
}
