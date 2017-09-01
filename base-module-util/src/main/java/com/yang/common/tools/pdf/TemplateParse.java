package com.yang.common.tools.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yang.common.tools.pdf.entity.AttributeVo;
import com.yang.common.tools.pdf.entity.BaseNodeVo;
import com.yang.common.tools.pdf.entity.CellNodeVo;
import com.yang.common.tools.pdf.entity.ForEachNodeVo;
import com.yang.common.tools.pdf.entity.GlobalNodeVo;
import com.yang.common.tools.pdf.entity.IfNodeVo;
import com.yang.common.tools.pdf.entity.ImageNodeVo;
import com.yang.common.tools.pdf.entity.PNodeVo;
import com.yang.common.tools.pdf.entity.TableNodeVo;

@SuppressWarnings("unchecked")
public class TemplateParse {
	
	private static final Logger logger = LoggerFactory.getLogger(TemplateParse.class);

	public static TemplateVo templateParse(String filePath) {
		if (filePath == null || filePath.isEmpty()) {
			logger.error("filePath null!");
			return null;
		}

		File templateFile = new File(filePath);
		if (!templateFile.exists()) {
			logger.error("模版文件不存在![" + filePath + "]");
			return null;
		}

		SAXBuilder builder = new SAXBuilder();
		InputStream file = null;
		try {
			file = new FileInputStream(filePath);
			Document document = builder.build(file);// 获得文档对象
			Element root = document.getRootElement();// 获得根节点
			if (!root.getName().equalsIgnoreCase("doc")) {
				file.close();
				logger.error("模版文件中root节点不是doc.");
				return null;
			}

			TemplateVo template = new TemplateVo();
			List<Element> list = root.getChildren();
			for (Element ele : list) {
				if (ele.getName().equalsIgnoreCase("global")) {
					nodeGlobalParse(template, ele);
				} else if (ele.getName().equalsIgnoreCase("body")) {
					nodeBodyParse(template, ele);
				}
			}

			return template;

		} catch (FileNotFoundException e) {
			logger.error("异常" + e);
		} catch (JDOMException e) {
			logger.error("异常" + e);
		} catch (IOException e) {
			logger.error("异常" + e);
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					logger.error("异常" + e);
				}
			}
		}

		return null;
	}

	private static void nodeGlobalParse(TemplateVo template, Element element) {
		if (element == null) {
			return;
		}

		GlobalNodeVo globalNodeVo = new GlobalNodeVo();
		template.setGlobalNodeVo(globalNodeVo);
		List<Element> list = element.getChildren();
		for (Element ele : list) {
			String nodeName = ele.getName().trim();
			if (nodeName.equalsIgnoreCase("font")) {
				String value = ele.getAttributeValue("family");
				if (value != null && !value.isEmpty()) {
					globalNodeVo.setFontFamily(value);
				}

				value = ele.getAttributeValue("charset");
				if (value != null) {
					globalNodeVo.setFontCharset(value);
				}

				value = ele.getAttributeValue("size");
				if (value != null && !value.isEmpty()) {
					globalNodeVo.setFontSize(Integer.parseInt(value));
				}

				value = ele.getAttributeValue("color");
				if (value != null && !value.isEmpty()) {
					globalNodeVo.setFontColor(value);
				}
			} else if (nodeName.equalsIgnoreCase("page")) {
				String value = ele.getAttributeValue("marginLeft");
				if (value != null && !value.isEmpty()) {
					globalNodeVo.setPageMarginLeft(Integer.parseInt(value));
				}

				value = ele.getAttributeValue("marginRight");
				if (value != null) {
					globalNodeVo.setPageMarginRight(Integer.parseInt(value));
				}

				value = ele.getAttributeValue("marginTop");
				if (value != null && !value.isEmpty()) {
					globalNodeVo.setPageMarginTop(Integer.parseInt(value));
				}

				value = ele.getAttributeValue("marginBottom");
				if (value != null && !value.isEmpty()) {
					globalNodeVo.setPageMarginBottom(Integer.parseInt(value));
				}

				value = ele.getAttributeValue("lineHeight");
				if (value != null && !value.isEmpty()) {
					globalNodeVo.setPageLineHeight(Integer.parseInt(value));
				}
			} else if (nodeName.equalsIgnoreCase("attribute")) {
				String value = ele.getAttributeValue("spacingAfter");
				if (value != null && !value.isEmpty()) {
					globalNodeVo.setSpacingAfter(Integer.parseInt(value));
				}

				value = ele.getAttributeValue("spacingBefore");
				if (value != null) {
					globalNodeVo.setSpacingBefore(Integer.parseInt(value));
				}

				value = ele.getAttributeValue("leading");
				if (value != null) {
					globalNodeVo.setLeading(Integer.parseInt(value));
				}
			}
		}
	}

	private static void nodeBodyParse(TemplateVo template, Element element) {
		if (element == null) {
			return;
		}

		List<Element> list = element.getChildren();
		for (Element ele : list) {
			template.addNode(nodeParse(ele));
		}
	}

	/**
	 * 解析节点属性
	 * 
	 * @param element
	 * @return
	 */
	private static AttributeVo attributeParse(Element element) {
		if (element == null) {
			return null;
		}
		// 获取属性值
		AttributeVo attributeVo = new AttributeVo();
		String value = element.getAttributeValue("fontSize");
		if (value != null && !value.isEmpty()) {
			attributeVo.setFontSize(Float.parseFloat(value));
		}

		value = element.getAttributeValue("fontColor");
		if (value != null) {
			attributeVo.setFontColor(value);
		}

		value = element.getAttributeValue("firstLineIndent");
		if (value != null && !value.isEmpty()) {
			attributeVo.setFirstLineIndent(Integer.parseInt(value));
		}

		value = element.getAttributeValue("align");
		if (value != null && !value.isEmpty()) {
			attributeVo.setAlign(Integer.parseInt(value));
		}

		value = element.getAttributeValue("idleft");
		if (value != null && !value.isEmpty()) {
			attributeVo.setIdleft(Integer.parseInt(value));
		}

		value = element.getAttributeValue("idright");
		if (value != null && !value.isEmpty()) {
			attributeVo.setIdright(Integer.parseInt(value));
		}

		value = element.getAttributeValue("width");
		if (value != null && !value.isEmpty()) {
			attributeVo.setWidth(Integer.parseInt(value));
		}

		value = element.getAttributeValue("height");
		if (value != null && !value.isEmpty()) {
			attributeVo.setHeight(Integer.parseInt(value));
		}

		value = element.getAttributeValue("colWidths");
		if (value != null && !value.isEmpty()) {
			String[] cols = value.split(",");
			int colsWidth[] = new int[cols.length];
			for (int i = 0; i < cols.length; i++) {
				colsWidth[i] = Integer.parseInt(cols[i]);
			}

			attributeVo.setColWidths(colsWidth);
			attributeVo.setColCount(colsWidth.length);
		}

		value = element.getAttributeValue("borderWidth");
		if (value != null && !value.isEmpty()) {
			attributeVo.setBorderWidth(Integer.parseInt(value));
		}

		value = element.getAttributeValue("padding");
		if (value != null && !value.isEmpty()) {
			attributeVo.setPadding(Integer.parseInt(value));
		}

		value = element.getAttributeValue("paddingLeft");
		if (value != null && !value.isEmpty()) {
			attributeVo.setPaddingLeft(Integer.parseInt(value));
		}

		value = element.getAttributeValue("paddingTop");
		if (value != null && !value.isEmpty()) {
			attributeVo.setPaddingTop(Integer.parseInt(value));
		}

		value = element.getAttributeValue("paddingRight");
		if (value != null && !value.isEmpty()) {
			attributeVo.setPaddingRight(Integer.parseInt(value));
		}

		value = element.getAttributeValue("paddingBottom");
		if (value != null && !value.isEmpty()) {
			attributeVo.setPaddingBottom(Integer.parseInt(value));
		}

		// 固定高度cell
		value = element.getAttributeValue("fixedHeight");
		if (value != null && !value.isEmpty()) {
			attributeVo.setPaddingBottom(Integer.parseInt(value));
		}

		value = element.getAttributeValue("colSpan");
		if (value != null && !value.isEmpty()) {
			attributeVo.setColSpan(Integer.parseInt(value));
		}

		value = element.getAttributeValue("rowSpan");
		if (value != null && !value.isEmpty()) {
			attributeVo.setRowSpan(Integer.parseInt(value));
		}

		return attributeVo;
	}

	/**
	 * 获取内容中的所有变量
	 * 
	 * @param content
	 * @return
	 */
	private static List<String> getAllVariables(String content) {
		if (content == null) {
			return null;
		}

		if (content.trim().isEmpty()) {
			return null;
		}

		List<String> variables = new ArrayList<String>();
		if (content.isEmpty()) {
			return null;
		}

		int start = content.indexOf("${");
		if (start == -1) {
			return variables;
		}

		int end = -1;
		while (start != -1) {
			end = content.indexOf("}", start);
			if (end == -1) {
				return variables;
			}

			String replaceText = content.substring(start + 2, end);
			variables.add(replaceText);
			start = content.indexOf("${", end);
		}

		return variables;
	}

	private static BaseNodeVo nodeParse(Element element) {
		if (element == null) {
			return null;
		}

		String nodeName = element.getName();
		if (nodeName == null) {
			return null;
		}

		nodeName = nodeName.trim();
		if (nodeName.equalsIgnoreCase("p")) {
			PNodeVo pNodeVo = new PNodeVo();
			AttributeVo attributeVo = attributeParse(element);
			pNodeVo.setAttributeVo(attributeVo);

			String tmpContent = element.getText();
			pNodeVo.setContent(tmpContent);
			pNodeVo.setAllVariables(getAllVariables(pNodeVo.getContent()));

			List<Element> list = element.getChildren();
			for (Element ele : list) {
				BaseNodeVo baseNode = nodeParse(ele);
				if (baseNode instanceof ForEachNodeVo) {
					pNodeVo.setForEachNodeVo((ForEachNodeVo) baseNode);
				}
			}

			return pNodeVo;
		} else if (nodeName.equalsIgnoreCase("table")) {
			TableNodeVo tableNodeVo = new TableNodeVo();
			AttributeVo attributeVo = attributeParse(element);
			tableNodeVo.setAttributeVo(attributeVo);
			List<Element> list = element.getChildren();
			for (Element ele : list) {
				BaseNodeVo baseNode = nodeParse(ele);
				if (baseNode instanceof CellNodeVo) {
					tableNodeVo.addCellNode(baseNode);
				} else if (baseNode instanceof ForEachNodeVo) {
					tableNodeVo.setForEachNodeVo((ForEachNodeVo) baseNode);
				}
			}

			return tableNodeVo;
		} else if (nodeName.equalsIgnoreCase("cell")) {
			CellNodeVo cellNodeVo = new CellNodeVo();
			AttributeVo attributeVo = attributeParse(element);
			cellNodeVo.setAttributeVo(attributeVo);
			List<Element> list = element.getChildren();
			for (Element ele : list) {
				BaseNodeVo baseNode = nodeParse(ele);
				if (baseNode instanceof PNodeVo) {
					PNodeVo pNodeVo = (PNodeVo) baseNode;
					cellNodeVo.setpNodeVo(pNodeVo);
				} else if (baseNode instanceof TableNodeVo) {
					TableNodeVo tableNodeVo = (TableNodeVo) baseNode;
					cellNodeVo.setTableNodeVo(tableNodeVo);
				} else if (baseNode instanceof IfNodeVo) {
					IfNodeVo ifNodeVo = (IfNodeVo) baseNode;
					cellNodeVo.addIfNode(ifNodeVo);
				}
			}

			return cellNodeVo;
		} else if (nodeName.equalsIgnoreCase("forEach")) {
			ForEachNodeVo forEachNodeVo = new ForEachNodeVo();
			List<Element> list = element.getChildren();
			for (Element ele : list) {
				BaseNodeVo baseNode = nodeParse(ele);
				if (baseNode instanceof CellNodeVo) {
					forEachNodeVo.addCellNode(baseNode);
				}
			}

			// items解析
			List<String> listVar = getAllVariables(element.getAttributeValue("items"));
			if (listVar != null && listVar.size() >= 1) {
				forEachNodeVo.setItems(listVar.get(0));
			}

			String tmpContent = element.getText();
			if (tmpContent != null && !tmpContent.trim().isEmpty()) {
				forEachNodeVo.setContent(tmpContent);
				forEachNodeVo.setContentVar(getAllVariables(tmpContent));
			}

			// input解析
			listVar = getAllVariables(element.getAttributeValue("input"));
			if (listVar != null && listVar.size() >= 1) {
				forEachNodeVo.setInputVar(listVar);
			}

			return forEachNodeVo;
		} else if (nodeName.equalsIgnoreCase("if")) {
			IfNodeVo ifNodeVo = new IfNodeVo();
			// test解析
			List<String> listVar = getAllVariables(element.getAttributeValue("test"));
			if (listVar != null && listVar.size() >= 1) {
				ifNodeVo.setTest(listVar.get(0));
			} else {
				return null;
			}

			String value = element.getAttributeValue("equals");
			if (value != null && !value.isEmpty()) {
				ifNodeVo.setEquals(value);
			}

			List<Element> list = element.getChildren();
			for (Element ele : list) {
				BaseNodeVo baseNode = nodeParse(ele);
				if (baseNode instanceof PNodeVo) {
					ifNodeVo.setBaseNodeVo(baseNode);
				}
			}

			return ifNodeVo;
		} else if (nodeName.equalsIgnoreCase("img")) {
			ImageNodeVo imageNodeVo = new ImageNodeVo();
			AttributeVo attributeVo = attributeParse(element);
			imageNodeVo.setAttributeVo(attributeVo);
			String value = element.getAttributeValue("src");
			if (value != null && !value.isEmpty()) {
				imageNodeVo.setSrc(value);
				imageNodeVo.setAllVariables(getAllVariables(imageNodeVo.getSrc()));
			}

			return imageNodeVo;
		} else if (nodeName.equalsIgnoreCase("br")) {
			PNodeVo pNodeVo = new PNodeVo();
			AttributeVo attributeVo = attributeParse(element);
			pNodeVo.setAttributeVo(attributeVo);
			pNodeVo.setContent("\n");
			return pNodeVo;
		} else if (nodeName.equalsIgnoreCase("np")) {
			BaseNodeVo baseNodeVo = new BaseNodeVo();
			baseNodeVo.setNodeName("np");
			return baseNodeVo;
		}

		return null;
	}

	public static void main(String[] args) {

	}
}
