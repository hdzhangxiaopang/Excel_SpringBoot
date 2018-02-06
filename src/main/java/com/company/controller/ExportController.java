package com.company.controller;

import com.company.entity.*;
import com.company.exception.FileExportException;
import com.company.factory.ExportConfigFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangguilin on 2/6/2018.
 */
@RestController
@ResponseBody
public class ExportController {

    @GetMapping("/apiExport")
    public void apiExport(HttpServletResponse response) throws FileExportException, IOException {
        ExportConfig exportConfig = ExportConfigFactory.getExportConfig(new FileInputStream(ResourceUtils.getFile("classpath:export/export_api.xml")));
        ExportResult exportResult = FileExport.getExportResult(exportConfig, initData());
        ExportType exportType = exportResult.getExportType();
        response.setContentType(exportType.getContentType());
        response.setHeader("Content-disposition", "attachment;filename=" + new String((exportResult.getFileName()).getBytes("utf-8"), "iso8859-1") + "." + exportType.getFileType());
        try {
            exportResult.export(response.getOutputStream());
        } catch (FileExportException | IOException e) {
            e.printStackTrace();
        }
    }
    private static List<Api> initData() {
        List<Api> lists = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Api api = new Api();
            api.setID(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            api.setBACKEND_SYSTEM_ID(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            api.setNAME("API_00" + i);
            api.setDESCRIPTION("DESC");
            api.setPATH("/system_api");
            api.setMETHOD("POST");
            api.setSCHEME(1);
            api.setSECURITY_SCHEME(2);
            api.setSTATE(1);
            api.setLIMIT_COUNT(60);
            api.setLIMIT_TIME(80);
            api.setLIMIT_UNIT("SECOND");
            api.setIS_DELETE(1);
            lists.add(api);
        }
        return lists;
    }


}
