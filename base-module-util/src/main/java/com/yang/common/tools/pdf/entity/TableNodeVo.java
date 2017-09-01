package com.yang.common.tools.pdf.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author iafclub
 *
 */
public class TableNodeVo extends BaseNodeVo
{
    private AttributeVo attributeVo;
    
    /**
     * 单元格
     */
    private List<CellNodeVo> cellNodes;
    
    private ForEachNodeVo forEachNodeVo;
    
    private List<ForEachNodeVo> listForEachNodeVos;
    
    public List<CellNodeVo> getCellNodes()
    {
        return cellNodes;
    }
    
    public void setCellNodes(List<CellNodeVo> cellNodes)
    {
        this.cellNodes = cellNodes;
    }
    
    public void addCellNode(BaseNodeVo baseNodeVo)
    {
        if (cellNodes == null)
        {
            this.cellNodes = new ArrayList<CellNodeVo>();
        }
        
        if (baseNodeVo instanceof CellNodeVo)
        {
            this.cellNodes.add((CellNodeVo)baseNodeVo);
        }
    }
    
    public ForEachNodeVo getForEachNodeVo()
    {
        return forEachNodeVo;
    }
    
    public void setForEachNodeVo(ForEachNodeVo forEachNodeVo)
    {
        this.forEachNodeVo = forEachNodeVo;
    }

    public AttributeVo getAttributeVo()
    {
        return attributeVo;
    }

    public void setAttributeVo(AttributeVo attributeVo)
    {
        this.attributeVo = attributeVo;
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
