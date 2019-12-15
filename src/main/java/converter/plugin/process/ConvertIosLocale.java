package converter.plugin.process;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static converter.plugin.support.ColumnInfo.*;

public class ConvertIosLocale {

	public boolean loadSheet(List<Integer> languageToConvert, String filePath, String outputFilePath)
			throws IOException {
		Sheet sheet = WorkbookFactory.create(new File(filePath)).getSheetAt(0);

		for (Integer localeType : languageToConvert) {
			if (localeType == COLUMN_ENGLISH) {
				createXmlForLocale(sheet, COLUMN_ENGLISH, outputFilePath, "/Localizable_en.strings");
			}
			if (localeType == COLUMN_SIMPLIFIED_CHINESE) {
				createXmlForLocale(sheet, COLUMN_SIMPLIFIED_CHINESE, outputFilePath,
						"/Localizable_cn.strings");
			}
			if (localeType == COLUMN_TRADITIONAL_CHINESE) {
				createXmlForLocale(sheet, COLUMN_TRADITIONAL_CHINESE, outputFilePath,
						"/Localizable_cn_trad.strings");
			}
			if (localeType == COLUMN_GERMAN) {
				createXmlForLocale(sheet, COLUMN_GERMAN, outputFilePath, "/Localizable_de.strings");
			}
			if (localeType == COLUMN_PORTUGAL) {
				createXmlForLocale(sheet, COLUMN_PORTUGAL, outputFilePath, "/Localizable_pt.strings");
			}
			if (localeType == COLUMN_FRENCH) {
				createXmlForLocale(sheet, COLUMN_FRENCH, outputFilePath, "/Localizable_fr.strings");
			}
			if (localeType == COLUMN_JAPANESE) {
				createXmlForLocale(sheet, COLUMN_JAPANESE, outputFilePath, "/Localizable_ja.strings");
			}
			if (localeType == COLUMN_KOREAN) {
				createXmlForLocale(sheet, COLUMN_KOREAN, outputFilePath, "/Localizable_ko.strings");
			}
			if (localeType == COLUMN_RUSSIAN) {
				createXmlForLocale(sheet, COLUMN_RUSSIAN, outputFilePath, "/Localizable_ru.strings");
			}
			if (localeType == COLUMN_SPANISH) {
				createXmlForLocale(sheet, COLUMN_SPANISH, outputFilePath, "/Localizable_es.strings");
			}
			if (localeType == COLUMN_INDONESIA) {
				createXmlForLocale(sheet, COLUMN_INDONESIA, outputFilePath, "/Localizable_in.strings");
			}
			if (localeType == COLUMN_DUTCH) {
				createXmlForLocale(sheet, COLUMN_DUTCH, outputFilePath, "/Localizable_nl.strings");
			}
			if (localeType == COLUMN_ITALY) {
				createXmlForLocale(sheet, COLUMN_ITALY, outputFilePath, "/Localizable_it.strings");
			}
			if (localeType == COLUMN_THAI) {
				createXmlForLocale(sheet, COLUMN_THAI, outputFilePath, "/Localizable_th.strings");
			}
		}
		return true;
	}

	private void createXmlForLocale(
			Sheet sheet,
			int columnNumber,
			String outputFolderPath,
			String outputXmlFileName) throws IOException {

		String localeString = loadLanguageByType(sheet, columnNumber);

		File outputFolder = new File(outputFolderPath);
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFolder + outputXmlFileName))) {
			writer.write(localeString);
		}
	}

	private String loadLanguageByType(Sheet sheet, int valueColumn) {
		StringBuilder languageBuilder = new StringBuilder();
		for (Row row : sheet) {
			if (row.getRowNum() > 1) {
				Cell headerCell = row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK);

				if (isNotNullOrEmpty(headerCell)) {
					languageBuilder.append(getHeader(headerCell));
					languageBuilder.append("\n");
				}

				Cell subHeaderCell = row.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if (isNotNullOrEmpty(subHeaderCell)) {
					languageBuilder.append(getSubHeader(subHeaderCell));
					languageBuilder.append("\n");
				}

				Cell key = row.getCell(3, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				Cell value = row.getCell(valueColumn, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				if (isNotNullOrEmpty(key) && isNotNullOrEmpty(value)) {
					languageBuilder.append(getString(key, value));
					languageBuilder.append("\n");
				}
			}
		}

		return languageBuilder.toString();
	}

	private boolean isNotNullOrEmpty(Cell key) {
		return key.getStringCellValue() != null
				&& !key.getStringCellValue().isEmpty()
				&& !key.getStringCellValue().contains("See #")
				&& !key.getStringCellValue().contains("[REMOVED]")
				&& !key.getStringCellValue().contains("[NOTVALID]");
	}

	private String getString(Cell key, Cell value) {
		return "\"" + key.getStringCellValue().trim() + "\" = \"" +
				replaceValue(value.getStringCellValue()) + "\";";
	}

	private String getHeader(Cell header) {
		return "\n// region " + header.getStringCellValue().toUpperCase();
	}

	private String getSubHeader(Cell subHeader) {
		return "\n// region " + subHeader.getStringCellValue().toUpperCase();
	}

	private String replaceValue(String value) {
		return value
				.replace("\n", "\\n")
				.replace("'", "\\'")
				.replace("<power>, <screen>, <bed>, <seat value> seat.", "%@")
				.replace("<power>、<screen>、<bed>、<seat value> 座位。", "%@")
				.replace("<US customs website link", "")
				.replace("<hh:mm>, <D(D) Mmm YYYY>", "%@")
				.replace("•", "\\u2022")
				.replace("<number>", "%@")
				.replace("<number 2>", "%@")
				.replace("<number 3>", "%@")
				.replace("<number_decimal_2>", "%@")
				.replace("<value>", "%@")
				.replace("<value 1>", "%@")
				.replace("<value 2>", "%@")
				.replace("<value 3>", "%@")
				.replace("<value 4>", "%@")
				.replace("<number 1>", "%@")
				.replace("<value 1> à <value 2>", "%@ à %@")
				.replace("<value int>", "%d").trim();
	}
}
