package com.company.factory;

import com.company.base.constants.UtilConstants;
import com.company.base.parser.ConfigParser;
import com.company.base.parser.XmlConfigParser;
import com.company.base.util.EmptyUtil;
import com.company.entity.ImportConfig;
import com.company.exception.FileImportException;

/**
 * Created by zhangguilin on 2/1/2018.
 */
public class ConfigurationParserFactory {
    public static ConfigParser getConfigParser(ImportConfig.ParserType parserType) throws FileImportException {
        if (EmptyUtil.isEmpty(parserType)) {
            throw new FileImportException(UtilConstants.PARSER_TYPE_ISEMPTY);
        }
        if (parserType == ImportConfig.ParserType.XML) {
            return new XmlConfigParser();
        }
        return new XmlConfigParser();
    }
}
