package com.yang.common.tools.pdf.entity;

import java.util.List;

public class ImageNodeVo extends BaseNodeVo
{
    private String src = "";
    
    private AttributeVo attributeVo;
    
    /**
     * 所有需要替换的变量
     */
    private List<String> allVariables;
    
    public String getSrc()
    {
        return src;
    }
    
    public void setSrc(String src)
    {
        this.src = src;
    }
    
    public AttributeVo getAttributeVo()
    {
        return attributeVo;
    }
    
    public void setAttributeVo(AttributeVo attributeVo)
    {
        this.attributeVo = attributeVo;
    }

    public List<String> getAllVariables()
    {
        return allVariables;
    }

    public void setAllVariables(List<String> allVariables)
    {
        this.allVariables = allVariables;
    }
    
}
