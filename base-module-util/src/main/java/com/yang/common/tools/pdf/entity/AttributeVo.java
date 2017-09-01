package com.yang.common.tools.pdf.entity;

public class AttributeVo extends BaseNodeVo
{
    /**
     * 字体大小
     */
    private float fontSize = 10;
    
    /**
     * 字体颜色
     */
    private String fontColor = "#000000";
    
    /**
     * 首行缩进
     */
    private int firstLineIndent = 0;
    
    /**
     * 水平对齐方式
     */
    private int align = 0;
    
    /**
     * 左缩进
     */
    private int idleft = 0;
    
    /**
     * 右缩进
     */
    private int idright = 0;
    
    private int width = 0;
    
    private int height = 0;
    
    private int marginTop = 0;
    
    /**
     * 列数
     */
    private int colCount;
    
    /**
     * 列宽
     */
    private int[] colWidths;
    
    /**
     * 边宽
     */
    private int borderWidth = -1;
    
    /**
     * 间距
     */
    private int padding = -1;
    
    private float paddingLeft = -1;
    private float paddingTop = -1;
    private float paddingRight = -1;
    private float paddingBottom = -1;
    private float fixedHeight = -1;
    
    /**
     * 合并单元格（列）
     */
    private int colSpan = -1;
    
    /**
     * 合并单元格（行）
     */
    private int rowSpan = -1;
    
    public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public float getFontSize()
    {
        return fontSize;
    }
    
    public void setFontSize(float fontSize)
    {
        this.fontSize = fontSize;
    }
    
    public String getFontColor()
    {
        return fontColor;
    }
    
    public void setFontColor(String fontColor)
    {
        this.fontColor = fontColor;
    }
    
    public int getFirstLineIndent()
    {
        return firstLineIndent;
    }
    
    public void setFirstLineIndent(int firstLineIndent)
    {
        this.firstLineIndent = firstLineIndent;
    }
    
    public int getAlign()
    {
        return align;
    }
    
    public void setAlign(int align)
    {
        this.align = align;
    }
    
    public int getIdleft()
    {
        return idleft;
    }
    
    public void setIdleft(int idleft)
    {
        this.idleft = idleft;
    }
    
    public int getIdright()
    {
        return idright;
    }
    
    public void setIdright(int idright)
    {
        this.idright = idright;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void setWidth(int width)
    {
        this.width = width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public int getMarginTop()
    {
        return marginTop;
    }
    
    public void setMarginTop(int marginTop)
    {
        this.marginTop = marginTop;
    }
    
    public int getColCount()
    {
        return colCount;
    }
    
    public void setColCount(int colCount)
    {
        this.colCount = colCount;
    }
    
    public int[] getColWidths()
    {
        return colWidths;
    }
    
    public void setColWidths(int[] colWidths)
    {
        this.colWidths = colWidths;
    }
    
    public int getBorderWidth()
    {
        return borderWidth;
    }
    
    public void setBorderWidth(int borderWidth)
    {
        this.borderWidth = borderWidth;
    }
    
    public int getPadding()
    {
        return padding;
    }
    
    public void setPadding(int padding)
    {
        this.padding = padding;
    }

    public float getPaddingLeft()
    {
        return paddingLeft;
    }

    public void setPaddingLeft(float paddingLeft)
    {
        this.paddingLeft = paddingLeft;
    }

    public float getPaddingTop()
    {
        return paddingTop;
    }

    public void setPaddingTop(float paddingTop)
    {
        this.paddingTop = paddingTop;
    }

    public float getPaddingRight()
    {
        return paddingRight;
    }

    public void setPaddingRight(float paddingRight)
    {
        this.paddingRight = paddingRight;
    }

    public float getPaddingBottom()
    {
        return paddingBottom;
    }

    public void setPaddingBottom(float paddingBottom)
    {
        this.paddingBottom = paddingBottom;
    }

    public float getFixedHeight()
    {
        return fixedHeight;
    }

    public void setFixedHeight(float fixedHeight)
    {
        this.fixedHeight = fixedHeight;
    }
}
