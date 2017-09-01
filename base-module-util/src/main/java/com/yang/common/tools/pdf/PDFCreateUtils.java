package com.yang.common.tools.pdf;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.yang.common.tools.PropertiesUtils;

/**
 * PDF工具类
 * 
 * @author yanglei 2017年7月17日 上午9:52:07
 */
public class PDFCreateUtils {

	private static final Logger logger = LoggerFactory.getLogger(PDFCreateUtils.class);

	public static TemplateVo loadTemplate(String templatePath) {
		// 加载XML模板
		TemplateVo templateVo = TemplateParse.templateParse(templatePath);
		if (templateVo == null) {
			logger.error("加载模板文件失败！");
			throw new RuntimeException("加载模板文件失败！");
		}
		try {
			// 克隆模板
			templateVo = (TemplateVo) templateVo.deepClone();
		} catch (Exception e) {
			throw new RuntimeException("克隆模板文件失败！");
		}
		return templateVo;
	}

	public static String getTemplatePath(String templateId) {
		String templatePath = PropertiesUtils.getProp("template.path");
		if (templatePath == null || templatePath.isEmpty()) {
			logger.error("模板路径template.path没有配置.");
			throw new RuntimeException("模板路径template.path没有配置.");
		}
		templatePath = templatePath + File.separator + templateId + ".xml";
		return templatePath;
	}

	public static void onEndPage(PdfWriter writer, Document document) {
		try {
			PdfContentByte headAndFootPdfContent = writer.getDirectContent();
			headAndFootPdfContent.saveState();
			headAndFootPdfContent.beginText();
			// 设置中文
//			BaseFont bfChinese = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", BaseFont.IDENTITY_H,
//					BaseFont.EMBEDDED);
			BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);
			headAndFootPdfContent.setFontAndSize(bfChinese, 12);
			// 文档页头信息设置
			float x = document.top(-20);
			float x1 = document.top(-5);
			// 页头信息中间
			headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, "******责任公司入库单",
					(document.right() + document.left()) / 2, x, 0);
			// 页头信息左面
			headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_LEFT,
					"2017-07-18", document.left() + 100, x1, 0);
			// 页头信息中间
			headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, "入库单号：",
					(document.right() + document.left()) / 2, x1, 0);
			// 页头信息右面
			headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_RIGHT, " 单位：册", document.right() - 100, x1, 0);
			// 文档页脚信息设置
			float y = document.bottom(-20);
			// 页脚信息左面
			headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_LEFT, "负责人：", document.left() + 100, y, 0);
			// 页脚信息中间
			headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_CENTER, "   库管员：    ",
					(document.right() + document.left()) / 2, y, 0);
			// 页脚信息右面
			headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_RIGHT, " 经手人：", document.right() - 100, y, 0);
			headAndFootPdfContent.endText();
			headAndFootPdfContent.restoreState();
		} catch (DocumentException de) {
			de.printStackTrace();
		} catch (IOException de) {
			de.printStackTrace();
		}
	}

}
