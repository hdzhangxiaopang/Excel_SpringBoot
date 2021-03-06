package com.company.service.impl;

import com.company.base.constants.UtilConstants;
import com.company.base.util.DateUtil;
import com.company.base.util.EmptyUtil;
import com.company.base.util.ExportUtil;
import com.company.base.util.ReflectionUtils;
import com.company.entity.ExportCell;
import com.company.entity.Type;
import com.company.exception.FileExportException;
import com.company.service.FileExport;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangguilin on 1/31/2018.
 */
public class CSVExport implements FileExport {

    @Override
    public StringBuilder getExportResult(List<?> data, List<ExportCell> exportCells) throws FileExportException {
        StringBuilder stringBuilder = new StringBuilder();
        createTitles(exportCells, stringBuilder);
        if (CollectionUtils.isNotEmpty(data)) {
            if (List.class.isAssignableFrom(data.getClass())) {
                if (data.get(0) instanceof Map) {
                    createContentByMap((List<Map>) data, exportCells, stringBuilder);
                } else {
                    createContentRowsByBean((List<Object>) data, exportCells, stringBuilder);
                }
            } else {
                throw new FileExportException(UtilConstants.EXPORT_FILE_TYPE_NOTLIST);
            }
        }
        return stringBuilder;
    }

    /**
     * 解析
     */
    private void createContentRowsByBean(List<Object> data, List<ExportCell> exportCells, StringBuilder stringBuilder) throws FileExportException {
        for (Object o : data) {
            int colLen = exportCells.size();
            for (int colNum = 0; colNum < colLen; colNum++) {
                ExportCell exportCell = exportCells.get(colNum);
                Object obj = null;
                try {
                    obj = ReflectionUtils.excuteMethod(o, ReflectionUtils.returnGetMethodName(exportCell.getAlias()));
                } catch (Exception e) {
                    throw new FileExportException("执行executeMethod  出错 Alias is " + exportCell.getAlias() + " at " + e);
                }
                ExportUtil.setValue(obj, stringBuilder);
                if (colNum != colLen - 1) {
                    stringBuilder.append(",");
                } else {
                    addLineSeparator(stringBuilder);
                }
            }
        }
    }

    /**
     * 解析Map填充内容
     */
    private void createContentByMap(List<Map> data, List<ExportCell> exportCells, StringBuilder stringBuilder) throws FileExportException {
        for (Map map : data) {
            int colLen = exportCells.size();
            for (int colNum = 0; colNum < colLen; colNum++) {
                ExportCell exportCell = exportCells.get(colNum);
                Object obj = null;
                obj = map.get(exportCell.getAlias());
                ExportUtil.setValue(obj, stringBuilder);
                if (colNum != colLen - 1) {
                    stringBuilder.append(",");
                } else {
                    addLineSeparator(stringBuilder);
                }
            }
        }
    }

    /**
     * 设置标题
     */
    private void createTitles(List<ExportCell> exportCells, StringBuilder stringBuilder) {
        int length = exportCells.size();
        for (int i = 0; i < length; i++) {
            ExportCell exportCell = exportCells.get(i);
            stringBuilder.append(exportCell.getTitle());
            if (i != length - 1) {
                stringBuilder.append(",");
            } else {
                addLineSeparator(stringBuilder);
            }
        }

    }

    /**
     * 添加行的分割符
     */
    private void addLineSeparator(StringBuilder stringBuilder) {
        stringBuilder.append(System.getProperty("line.separator"));
    }
}
