package com.company.executor;

import com.company.base.constants.UtilConstants;
import com.company.entity.ImportConfig;
import com.company.entity.ImportResult;
import com.company.exception.FileImportException;
import com.company.service.FileImport;
import com.company.service.impl.ExcelImport;

import java.io.File;

/**
 * Created by zhangguilin on 2/2/2018.
 */
public class FileImportExecutor {

    public static ImportResult importFile(ImportConfig importConfig, File file, String fileName) throws FileImportException {
        FileImport fileImport = getFileImport(importConfig);
        return fileImport.getImportResult(file, fileName);
    }

    private static FileImport getFileImport(ImportConfig importConfig) throws FileImportException {
        if (importConfig.getImportFileType() == ImportConfig.ImportFileType.EXCEL) {
            return new ExcelImport(importConfig);
        }
        throw new FileImportException(UtilConstants.IMPORT_FILE_TYPE_ERROR);
    }
}
