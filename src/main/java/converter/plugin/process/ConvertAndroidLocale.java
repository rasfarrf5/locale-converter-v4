package converter.plugin.process;

import converter.plugin.support.ReplaceHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.List;

import static converter.plugin.support.ColumnInfo.*;

public class ConvertAndroidLocale {

	private static final String HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources xmlns:tools=\"http://schemas.android.com/tools\" tools:ignore=\"MissingTranslation\">\n";
	private static final String FOOTER = "</resources>";
	private static final String OUTPUT_XML_FILENAME = "/strings.xml";

	private ReplaceHelper replaceHelper = new ReplaceHelper();

	public boolean loadSheet(List<Integer> languageToConvert, String filePath, String outputFilePath)
			throws IOException {

		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		Sheet sheet = WorkbookFactory.create(fileInputStream).getSheetAt(0);

		for (Integer localeType : languageToConvert) {
			if (localeType == COLUMN_ENGLISH) {
				createXmlForLocale(sheet, COLUMN_ENGLISH, outputFilePath + "/values");
			}
			if (localeType == COLUMN_SIMPLIFIED_CHINESE) {
				createXmlForLocale(sheet, COLUMN_SIMPLIFIED_CHINESE, outputFilePath + "/values-zh-rCN");
			}
			if (localeType == COLUMN_TRADITIONAL_CHINESE) {
				createXmlForLocale(sheet, COLUMN_TRADITIONAL_CHINESE, outputFilePath + "/values-zh-rTW");
			}
			if (localeType == COLUMN_GERMAN) {
				createXmlForLocale(sheet, COLUMN_GERMAN, outputFilePath + "/values-de");
			}
			if (localeType == COLUMN_PORTUGAL) {
				createXmlForLocale(sheet, COLUMN_PORTUGAL, outputFilePath + "/values-pt");
			}
			if (localeType == COLUMN_FRENCH) {
				createXmlForLocale(sheet, COLUMN_FRENCH, outputFilePath + "/values-fr");
			}
			if (localeType == COLUMN_JAPANESE) {
				createXmlForLocale(sheet, COLUMN_JAPANESE, outputFilePath + "/values-ja");
			}
			if (localeType == COLUMN_KOREAN) {
				createXmlForLocale(sheet, COLUMN_KOREAN, outputFilePath + "/values-ko");
			}
			if (localeType == COLUMN_RUSSIAN) {
				createXmlForLocale(sheet, COLUMN_RUSSIAN, outputFilePath + "/values-ru");
			}
			if (localeType == COLUMN_SPANISH) {
				createXmlForLocale(sheet, COLUMN_SPANISH, outputFilePath + "/values-es");
			}
			if (localeType == COLUMN_INDONESIA) {
				createXmlForLocale(sheet, COLUMN_INDONESIA, outputFilePath + "/values-in");
			}
			if (localeType == COLUMN_DUTCH) {
				createXmlForLocale(sheet, COLUMN_DUTCH, outputFilePath + "/values-nl");
			}
			if (localeType == COLUMN_ITALY) {
				createXmlForLocale(sheet, COLUMN_ITALY, outputFilePath + "/values-it");
			}
			if (localeType == COLUMN_THAI) {
				createXmlForLocale(sheet, COLUMN_THAI, outputFilePath + "/values-th");
			}
		}
		return true;
	}

	private void createXmlForLocale(
			Sheet sheet,
			int columnNumber,
			String outputFolderPath) throws IOException {

		String localeString = loadLanguageByType(sheet, columnNumber);

		File outputFolder = new File(outputFolderPath);
		if (!outputFolder.exists()) {
			outputFolder.mkdirs();
		}

		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter(outputFolder + ConvertAndroidLocale.OUTPUT_XML_FILENAME))) {
			writer.write(localeString);
		}
	}

	private String loadLanguageByType(Sheet sheet, int valueColumn) {
		StringBuilder languageBuilder = new StringBuilder();
		languageBuilder.append(HEADER);
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

		languageBuilder.append(FOOTER);
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
		return "\t\t<string name=\""
				+ key.getStringCellValue().trim() + "\">"
				+ replaceHelper.replaceValueAndroid(value.getStringCellValue()) + "</string>";
	}

	private String getHeader(Cell header) {
		return "\n\t<!--region " + header.getStringCellValue().toUpperCase() + "-->";
	}

	private String getSubHeader(Cell subHeader) {
		return "\n\t\t<!--region " + subHeader.getStringCellValue().toUpperCase() + "-->";
	}
}
