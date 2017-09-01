package com.yang.common.tools.pdf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.yang.common.tools.pdf.entity.AttributeVo;
import com.yang.common.tools.pdf.entity.CellNodeVo;
import com.yang.common.tools.pdf.entity.ForEachNodeVo;
import com.yang.common.tools.pdf.entity.GlobalNodeVo;
import com.yang.common.tools.pdf.entity.ImageNodeVo;
import com.yang.common.tools.pdf.entity.PNodeVo;
import com.yang.common.tools.pdf.entity.TableNodeVo;

public class PDFHelperUtil {
	public static Paragraph createParagraph(PNodeVo pnodeVo, GlobalNodeVo globalNodeVo) {
		if (pnodeVo == null || globalNodeVo == null) {
			return null;
		}

		String content = pnodeVo.getContent();
		if (content.isEmpty()) {
			// return null;
			content = ""; // update by yanglei 2017-03-17
		}

		String foreachText = "";
		List<ForEachNodeVo> listForeachNode = pnodeVo.getListForEachNodeVos();
		if (listForeachNode != null && listForeachNode.size() != 0) {
			for (ForEachNodeVo forEachNodeVo : listForeachNode) {
				foreachText += forEachNodeVo.getContent();
			}
		}

		content += foreachText;
		AttributeVo attributeVo = pnodeVo.getAttributeVo();
		float fontSize = attributeVo.getFontSize();
		if (fontSize == 0) {
			fontSize = globalNodeVo.getFontSize();
		}

		String strColor = attributeVo.getFontColor();
		if (strColor == null || strColor.isEmpty()) {
			strColor = globalNodeVo.getFontColor();
		}

		BaseColor baseColor = convertColor(strColor);
		BaseFont basefont = null;
		try {
			basefont = BaseFont.createFont(globalNodeVo.getFontFamily(), globalNodeVo.getFontCharset(),
					BaseFont.NOT_EMBEDDED);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

		Font font = new Font(basefont, fontSize, 0);
		font.setColor(baseColor);

		Paragraph paragraph = new Paragraph(content, font);
		paragraph.setSpacingAfter(globalNodeVo.getSpacingAfter());
		paragraph.setSpacingBefore(globalNodeVo.getSpacingBefore());
		paragraph.setLeading(fontSize + globalNodeVo.getLeading());
		if (attributeVo.getIdright() != 0) {
			paragraph.setIndentationRight(attributeVo.getIdright() * 2);
		}

		if (attributeVo.getIdleft() != 0) {
			paragraph.setIndentationLeft(attributeVo.getIdleft() * 2);
		}

		if (attributeVo.getFirstLineIndent() != 0) {
			paragraph.setFirstLineIndent(attributeVo.getFirstLineIndent() * 2);
		}

		int align = Paragraph.ALIGN_LEFT;
		if (attributeVo.getAlign() == 1) {
			align = Paragraph.ALIGN_CENTER;
		} else if (attributeVo.getAlign() == 2) {
			align = Paragraph.ALIGN_RIGHT;
		}
		paragraph.setAlignment(align);

		return paragraph;
	}

	public static Paragraph createImage(ImageNodeVo imageNodeVo) {
		if (imageNodeVo == null) {
			return null;
		}

		Paragraph paragraph = new Paragraph(" ");
		try {
			Image image = getImage(imageNodeVo);
			if (image == null) {
				return null;
			}
			paragraph.add(image);
		} catch (BadElementException | IOException e) {
			throw new RuntimeException("生成图片错误");
		}

		return paragraph;
	}

	public static PdfPTable createTable(TableNodeVo tableNodeVo, GlobalNodeVo globalNodeVo) {
		AttributeVo attributeVo = tableNodeVo.getAttributeVo();
		int colCount = attributeVo.getColCount();
		if (colCount == 0) {
			return null;
		}

		PdfPTable table = new PdfPTable(attributeVo.getColCount());
		try {
			table.setWidths(attributeVo.getColWidths());
		} catch (DocumentException e) {
			throw new RuntimeException("生成table错误");
		}

		table.setWidthPercentage(100);// 设置表格宽度为%100
		table.setSplitLate(false);
		table.setSplitRows(true);

		List<CellNodeVo> cells = tableNodeVo.getCellNodes();

		for (CellNodeVo cell : cells) {
			PdfPCell pdfCell = createCell(cell, globalNodeVo, attributeVo);
			if (pdfCell != null) {
				table.addCell(pdfCell);
			}
		}

		List<ForEachNodeVo> listForeachNode = tableNodeVo.getListForEachNodeVos();
		if (listForeachNode != null) {
			for (ForEachNodeVo forEachNodeVo : listForeachNode) {
				List<CellNodeVo> forEachCells = forEachNodeVo.getCellNodes();
				for (CellNodeVo cell : forEachCells) {
					PdfPCell pdfCell = createCell(cell, globalNodeVo, attributeVo);
					if (pdfCell != null) {
						table.addCell(pdfCell);
					}
				}
			}
		}

		return table;
	}

	private static Image getImage(ImageNodeVo imageModel)
			throws BadElementException, MalformedURLException, IOException {

		if (imageModel == null) {
			return null;
		}

		String src = imageModel.getSrc();
		if (src.isEmpty()) {
			return null;
		}

		Image image = Image.getInstance(src);
		if (image == null) {
			return null;
		}

		AttributeVo attributeVo = imageModel.getAttributeVo();
		image.setAlignment(Image.UNDERLYING);
		image.scaleAbsolute(attributeVo.getWidth(), attributeVo.getHeight());
		if (attributeVo.getIdleft() != 0) {
			image.setIndentationLeft(attributeVo.getIdleft());
		} else if (attributeVo.getIdright() != 0) {
			image.setIndentationRight(attributeVo.getIdright());
		}

		return image;
	}

	private static BaseColor convertColor(String color) {
		if ((color == null) || (color.trim().length() == 0)) {
			return BaseColor.BLACK;
		}

		if (color.length() != 7) {
			return BaseColor.BLACK;
		}

		int R = Integer.parseInt(color.substring(1, 3), 16);
		int G = Integer.parseInt(color.substring(3, 5), 16);
		int B = Integer.parseInt(color.substring(5), 16);
		return new BaseColor(R, G, B);
	}

	private static PdfPCell createCell(CellNodeVo cellNodeVo, GlobalNodeVo globalNodeVo, AttributeVo tableAttribute) {
		if (cellNodeVo == null || globalNodeVo == null) {
			return null;
		}

		PdfPCell pdfCell = null;
		PNodeVo pNodeVo = cellNodeVo.getpNodeVo();
		if (pNodeVo != null) {
			Paragraph paragraph = createParagraph(pNodeVo, globalNodeVo);
			pdfCell = new PdfPCell(paragraph);
		}

		TableNodeVo tableNodeVo = cellNodeVo.getTableNodeVo();
		if (tableNodeVo != null) {
			PdfPTable pdfTable = createTable(tableNodeVo, globalNodeVo);
			pdfCell = new PdfPCell(pdfTable);
		}

		ImageNodeVo imageNodeVo = cellNodeVo.getImageNodeVo();
		if (imageNodeVo != null) {
			Paragraph paragraph = createImage(imageNodeVo);
			pdfCell = new PdfPCell(paragraph);
		}

		if (pdfCell == null) {
			pdfCell = new PdfPCell();
		}

		AttributeVo attributeVo = cellNodeVo.getAttributeVo();
		int padding = ((attributeVo.getPadding() == -1) ? tableAttribute.getPadding() : attributeVo.getPadding());
		if (padding != -1) {
			pdfCell.setPadding(padding);
		}

		int borderWidth = ((attributeVo.getBorderWidth() == -1) ? tableAttribute.getBorderWidth()
				: attributeVo.getBorderWidth());
		if (borderWidth != -1) {
			pdfCell.setBorderWidth((float) (borderWidth / 2.0));
		}

		pdfCell.setUseAscender(true);
		pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

		float paddingLeft = ((attributeVo.getPaddingLeft() == -1) ? tableAttribute.getPaddingLeft()
				: attributeVo.getPaddingLeft());
		if (paddingLeft != -1) {
			pdfCell.setPaddingLeft(paddingLeft);
		}

		float paddingTop = ((attributeVo.getPaddingTop() == -1) ? tableAttribute.getPaddingTop()
				: attributeVo.getPaddingTop());
		if (paddingTop != -1) {
			pdfCell.setPaddingTop(paddingTop);
		}

		float paddingRight = ((attributeVo.getPaddingRight() == -1) ? tableAttribute.getPaddingRight()
				: attributeVo.getPaddingRight());
		if (paddingRight != -1) {
			pdfCell.setPaddingRight(paddingRight);
		}

		float paddingBottom = ((attributeVo.getPaddingBottom() == -1) ? tableAttribute.getPaddingBottom()
				: attributeVo.getPaddingBottom());
		if (paddingBottom != -1) {
			pdfCell.setPaddingBottom(paddingBottom);
		}

		float fixedHeight = ((attributeVo.getFixedHeight() == -1) ? tableAttribute.getFixedHeight()
				: attributeVo.getFixedHeight());
		if (fixedHeight != -1) {
			// 设置固定高度
			pdfCell.setFixedHeight(fixedHeight);
		}

		int colSpan = ((attributeVo.getColSpan() == -1) ? tableAttribute.getColSpan() : attributeVo.getColSpan());
		if (colSpan != -1) {
			pdfCell.setColspan(colSpan);
		}

		int rowSpan = ((attributeVo.getRowSpan() == -1) ? tableAttribute.getRowSpan() : attributeVo.getRowSpan());
		if (rowSpan != -1) {
			pdfCell.setRowspan(rowSpan);
		}

		pdfCell.setLeading(globalNodeVo.getFontSize() + globalNodeVo.getLeading(), 0);

		return pdfCell;
	}
}
