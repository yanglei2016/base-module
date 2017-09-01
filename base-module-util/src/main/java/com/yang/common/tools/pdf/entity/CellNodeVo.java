package com.yang.common.tools.pdf.entity;

import java.util.ArrayList;
import java.util.List;

public class CellNodeVo extends BaseNodeVo
{
    private AttributeVo attributeVo;
    
    private PNodeVo pNodeVo;
    
    private TableNodeVo tableNodeVo;
    
    private ImageNodeVo imageNodeVo;
    
    private List<IfNodeVo> listIf;
    
    public PNodeVo getpNodeVo()
    {
        return pNodeVo;
    }
    
    public void setpNodeVo(PNodeVo pNodeVo)
    {
        this.pNodeVo = pNodeVo;
    }
    
    public TableNodeVo getTableNodeVo()
    {
        return tableNodeVo;
    }
    
    public void setTableNodeVo(TableNodeVo tableNodeVo)
    {
        this.tableNodeVo = tableNodeVo;
    }
    
    public AttributeVo getAttributeVo()
    {
        return attributeVo;
    }
    
    public void setAttributeVo(AttributeVo attributeVo)
    {
        this.attributeVo = attributeVo;
    }
    
    public ImageNodeVo getImageNodeVo()
    {
        return imageNodeVo;
    }
    
    public void setImageNodeVo(ImageNodeVo imageNodeVo)
    {
        this.imageNodeVo = imageNodeVo;
    }
    
    public List<IfNodeVo> getListIf()
    {
        return listIf;
    }
    
    public void setListIf(List<IfNodeVo> listIf)
    {
        this.listIf = listIf;
    }
    
    public void addIfNode(IfNodeVo ifNode)
    {
        if (this.listIf == null)
        {
            this.listIf = new ArrayList<IfNodeVo>();
        }
        this.listIf.add(ifNode);
    }
}
