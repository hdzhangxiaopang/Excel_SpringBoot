package com.company.service;

import com.company.entity.ExportCell;
import com.company.exception.FileExportException;

import java.util.List;

/**
 * Created by zhangguilin on 1/31/2018.
 */
public interface FileExport {

    /**
     * 数据导出
     */
    public Object getExportResult(List<?> data, List<ExportCell> exportCells) throws FileExportException;
}
