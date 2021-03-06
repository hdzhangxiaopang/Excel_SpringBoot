package com.company.factory;

import com.company.base.constants.UtilConstants;
import com.company.base.parser.ConfigParser;
import com.company.base.util.EmptyUtil;
import com.company.entity.ExportCell;
import com.company.entity.ExportConfig;
import com.company.entity.ExportTag;
import com.company.entity.ExportType;
import com.company.exception.FileExportException;
import com.company.exception.FileImportException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangguilin on 1/31/2018.
 */
public class ExportConfigFactory {
    public static ExportConfig getExportConfig(InputStream inputStream) throws FileExportException {
        return getExportCells(inputStream);
    }

    private static ExportConfig getExportCells(InputStream inputStream) throws FileExportException {
        ExportConfig exportConfig = new ExportConfig();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        Document document = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(inputStream);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new FileExportException(e, UtilConstants.EXPORT_XML_PARSER_ERROR);
        }
        Element element = document.getDocumentElement();
        NodeList elements = element.getElementsByTagName(ExportTag.CELL);
        List<ExportCell> exportCells = initElement(elements);

        String fileName = "";
        String fileType = "";
        try {
            fileName = ConfigParser.getNodeText(element, ExportTag.FILENAME);
            fileType = ConfigParser.getNodeText(element, ExportTag.EXPORTTYPE);
        } catch (FileImportException e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(fileName)) {
            throw new FileExportException(UtilConstants.EXPORT_FILE_NAME_ISEMPTY);
        }
        if (StringUtils.isEmpty(fileType) || !StringUtils.isNumeric(fileType)) {
            throw new FileExportException(UtilConstants.EXPORT_FILE_TYPE_ISEMPTY);
        }
        exportConfig.setFileName(fileName);
        ExportType exportType = ExportType.getExportType(Integer.valueOf(fileType));
        if (EmptyUtil.isEmpty(exportType)) {
            throw new FileExportException(UtilConstants.EXPORT_FILE_TYPE_NOTFOUND);
        }
        exportConfig.setExportType(exportType);
        exportConfig.setExportCells(exportCells);
        return exportConfig;
    }

    private static List<ExportCell> initElement(NodeList elements) throws FileExportException {
        ArrayList<ExportCell> exportCells = new ArrayList<>(elements.getLength());
        for (int i = 0; i < elements.getLength(); i++) {
            ExportCell exportCell = new ExportCell();
            Element item = (Element) elements.item(i);
            String titleText = "";
            String aliasText = "";
            String widthText = "";
            String exportText = "";
            try {
                titleText = ConfigParser.getNodeText(item, ExportTag.TITLE);
                aliasText = ConfigParser.getNodeText(item, ExportTag.ALIAS);
                widthText = ConfigParser.getNodeText(item, ExportTag.WIDTH);
                exportText = ConfigParser.getNodeText(item, ExportTag.EXPORT);
            } catch (FileImportException e) {
                throw new FileExportException(e);
            }
            if (StringUtils.isEmpty(titleText)) {
                throw new FileExportException(UtilConstants.EXPORT_XML_TITLE_ISEMPTY);
            }
            exportCell.setTitle(titleText);
            if (StringUtils.isEmpty(aliasText)) {
                throw new FileExportException(UtilConstants.EXPORT_XML_ALIAS_ISEMPTY);
            }
            exportCell.setAlias(aliasText);
            if (!StringUtils.isEmpty(widthText)) {
                if (!StringUtils.isNumeric(widthText)) {
                    throw new FileExportException(UtilConstants.EXPORT_XML_WIDTH_NOTNUMERIC);
                }
                exportCell.setWidth(widthText);
            }
            if (!StringUtils.isEmpty(exportText)) {
                if (!StringUtils.isNumeric(exportText)) {
                    throw new FileExportException(UtilConstants.EXPORT_XML_EXPORT_NOTNUMERIC);
                }
                exportCell.setExport(ExportCell.Export.getExport(Integer.valueOf(exportText)));
            }
            /**
             * 判断是否导出该单元格项
             * */
            if (exportCell.getExport().equals(ExportCell.Export.EXPORT)) {
                exportCells.add(exportCell);
            }

        }
        if (exportCells.isEmpty()) {
            throw new FileExportException(UtilConstants.EXPORT_XML_FILE_CONTENT_ISEMPTY);
        }
        return exportCells;
    }
}
