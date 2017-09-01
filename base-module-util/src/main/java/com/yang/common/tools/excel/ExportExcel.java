package com.yang.common.tools.excel;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 导出EXCEL
 * 
 * @author HP
 *
 * @param <T>
 */
public class ExportExcel<T> {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 
	 * @param headers	列标题#getMethodName
	 * @param dataset
	 * @return
	 * @author yanglei
	 * 2017年7月26日 下午4:51:40
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Workbook createExcel(String[] headers, Collection<T> dataSet) {
		Workbook workbook = null;
		try {
			// 声明一个工作薄
			workbook = new XSSFWorkbook();
			// 生成一个表格
			Sheet sheet = workbook.createSheet();
			// 设置表格默认列宽度
			sheet.setDefaultColumnWidth(18);
			
			// 生成并设置另一个样式
			CellStyle titleStyle = this.getTitleStyle(workbook);
			CellStyle contentStyle = this.getContentStyle(workbook);
			
			// 产生表格标题行
			Row row = sheet.createRow(0);
			for (int i = 0, len = headers.length; i < len; i++) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(titleStyle);
				XSSFRichTextString text = new XSSFRichTextString(headers[i].split("#", 2)[0]);
				cell.setCellValue(text);
			}
			
			// 遍历集合数据，产生数据行
			Iterator<T> iterator = dataSet.iterator();
			
			for (int index = 1; iterator.hasNext(); index++) {
				T t = (T) iterator.next();
				row = sheet.createRow(index);
				Cell cell = null;
				for (int i = 0, len = headers.length; i < len; i++) {
					cell = row.createCell(i);
					cell.setCellStyle(contentStyle);
					
					String methodName = this.getMethodName(headers[i]);
					if (StringUtils.isNotBlank(methodName)) {
						Class cls = t.getClass();
						Method method = cls.getMethod(methodName);
						Object value = method.invoke(t);
						String textValue = String.valueOf(value);
						if (StringUtils.isNotBlank(textValue)) {
							XSSFRichTextString richString = new XSSFRichTextString(textValue);
							cell.setCellValue(richString);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("ExportExcel.createExcel生成Excel异常", e);
		}
		return workbook;
	}

	/**
	 * 获取标题样式
	 * @param workbook
	 * @return
	 * @author yanglei
	 * 2017年7月26日 下午4:54:37
	 */
	@SuppressWarnings("deprecation")
	private CellStyle getTitleStyle(Workbook workbook) {
		// 生成并设置另一个样式
		CellStyle style = workbook.createCellStyle();

		style.setFillForegroundColor((short) 51);// 设置背景色
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 设置这些样式
		style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		style.setAlignment(CellStyle.ALIGN_CENTER);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 14);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);

		return style;
	}

	/**
	 * 获取内容样式
	 * @param workbook
	 * @return
	 * @author yanglei
	 * 2017年7月26日 下午4:54:26
	 */
	@SuppressWarnings("deprecation")
	private CellStyle getContentStyle(Workbook workbook) {
		// 生成并设置另一个样式
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(CellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		style.setAlignment(CellStyle.ALIGN_CENTER);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		// 把字体应用到当前的样式
		style.setFont(font);

		return style;
	}

	/**
	 * 获取get方法名
	 * @param headers
	 * @return
	 * @author yanglei
	 * 2017年7月26日 下午4:54:11
	 */
	private String getMethodName(String headers){
		String methodName = null;
		if(StringUtils.isNotBlank(headers)){
			String[] tmpArr = headers.split("#", 2);;
			if(tmpArr != null && tmpArr.length == 2){
				methodName = tmpArr[1];
			}
		}
		return methodName;
	}
}
