package com.yang.common.tools.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yang.common.tools.DateUtils;

@Service
public class ExcelUtils {

	private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	
	/**
	 * 导出Excel
	 * @param excels
	 * @param headers	列标题#getMethodName
	 * @param fileName
	 * @param title
	 * @param request
	 * @param response
	 * @author yanglei
	 * 2017年7月26日 上午10:27:08
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void exportExcel(List excels, String[] headers,
			String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		OutputStream out = null;
		Workbook wb = null;
		try {
			if (StringUtils.isBlank(fileName)) {
				fileName = DateUtils.getDateStr(DateUtils.DATETIME_FMT);
			}
			// 如果是IE浏览器，则用URLEncode解析
			if (isMSBrowser(request)) {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {// 如果是谷歌、火狐则解析为ISO-8859-1
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setHeader("Content-Disposition", "attachment;filename="+ fileName + ".xlsx");// 指定下载的文件名
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			out = response.getOutputStream();
			
			ExportExcel<T> ex = new ExportExcel<T>();
			wb = ex.createExcel(headers, excels);
			wb.write(out);
		} catch (Exception e) {
			logger.error("导出Excel异常", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 判断前端浏览器是否ie
	 * @param request
	 * @return
	 */
	private static boolean isMSBrowser(HttpServletRequest request) {
		String[] IEBrowserSignals = { "MSIE", "Trident", "Edge" };
		String userAgent = request.getHeader("User-Agent");
		for (String signal : IEBrowserSignals) {
			if (userAgent.contains(signal)) {
				return true;
			}
		}
		return false;
	}
}
