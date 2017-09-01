package com.yang.common.tools.pdf.entity;

import java.util.List;

public class PNodeVo extends BaseNodeVo
{
    /**
     * 内容
     */
    private String content = "";
    
    /**
     * 节点属性
     */
    private AttributeVo attributeVo;
    
    /**
     * 所有需要替换的变量
     */
    private List<String> allVariables;
    
    /**
     * 需要循环处理的数据
     */
    private ForEachNodeVo forEachNodeVo;
    
    private List<ForEachNodeVo> listForEachNodeVos;
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
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
    
    public ForEachNodeVo getForEachNodeVo()
    {
        return forEachNodeVo;
    }
    
    public void setForEachNodeVo(ForEachNodeVo forEachNodeVo)
    {
        this.forEachNodeVo = forEachNodeVo;
    }
    
    public List<ForEachNodeVo> getListForEachNodeVos()
    {
        return listForEachNodeVos;
    }
    
    public void setListForEachNodeVos(List<ForEachNodeVo> listForEachNodeVos)
    {
        this.listForEachNodeVos = listForEachNodeVos;
    }
}
