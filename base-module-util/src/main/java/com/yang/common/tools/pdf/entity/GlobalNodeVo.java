package com.yang.common.tools.pdf.entity;

public class GlobalNodeVo extends BaseNodeVo
{
    /**
     * 默认字体大小
     */
    private int fontSize;
    
    /**
     * 默认字体族
     */
    private String fontFamily;
    
    /**
     * 默认字体字符集
     */
    private String fontCharset;
    
    /**
     * 默认字体颜色
     */
    private String fontColor;
    
    private int pageMarginLeft;
    
    private int pageMarginRight;
    
    private int pageMarginTop;
    
    private int pageMarginBottom;
    
    private int pageLineHeight;
    
    /**
     * 段落间距
     */
    private int spacingAfter;
    
    /**
     * 段落间距
     */
    private int spacingBefore;
    
    private int leading;
    
    public int getFontSize()
    {
        return fontSize;
    }
    
    public void setFontSize(int fontSize)
    {
        this.fontSize = fontSize;
    }
    
    public String getFontFamily()
    {
        return fontFamily;
    }
    
    public void setFontFamily(String fontFamily)
    {
        this.fontFamily = fontFamily;
    }
    
    public String getFontCharset()
    {
        return fontCharset;
    }
    
    public void setFontCharset(String fontCharset)
    {
        this.fontCharset = fontCharset;
    }
    
    public String getFontColor()
    {
        return fontColor;
    }
    
    public void setFontColor(String fontColor)
    {
        this.fontColor = fontColor;
    }
    
    public int getPageMarginLeft()
    {
        return pageMarginLeft;
    }
    
    public void setPageMarginLeft(int pageMarginLeft)
    {
        this.pageMarginLeft = pageMarginLeft;
    }
    
    public int getPageMarginRight()
    {
        return pageMarginRight;
    }
    
    public void setPageMarginRight(int pageMarginRight)
    {
        this.pageMarginRight = pageMarginRight;
    }
    
    public int getPageMarginTop()
    {
        return pageMarginTop;
    }
    
    public void setPageMarginTop(int pageMarginTop)
    {
        this.pageMarginTop = pageMarginTop;
    }
    
    public int getPageMarginBottom()
    {
        return pageMarginBottom;
    }
    
    public void setPageMarginBottom(int pageMarginBottom)
    {
        this.pageMarginBottom = pageMarginBottom;
    }
    
    public int getPageLineHeight()
    {
        return pageLineHeight;
    }
    
    public void setPageLineHeight(int pageLineHeight)
    {
        this.pageLineHeight = pageLineHeight;
    }
    
    public int getSpacingAfter()
    {
        return spacingAfter;
    }
    
    public void setSpacingAfter(int spacingAfter)
    {
        this.spacingAfter = spacingAfter;
    }
    
    public int getSpacingBefore()
    {
        return spacingBefore;
    }
    
    public void setSpacingBefore(int spacingBefore)
    {
        this.spacingBefore = spacingBefore;
    }
    
    public int getLeading()
    {
        return leading;
    }
    
    public void setLeading(int leading)
    {
        this.leading = leading;
    }
}
