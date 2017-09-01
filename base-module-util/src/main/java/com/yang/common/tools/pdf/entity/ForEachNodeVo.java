package com.yang.common.tools.pdf.entity;

import java.util.ArrayList;
import java.util.List;

public class ForEachNodeVo extends BaseNodeVo
{
    /**
     * 内容
     */
    private String content;
    
    private String items;
    
    /**
     * 所有需要替换的变量
     */
    private List<String> contentVar;
    
    /**
     * 单元格
     */
    private List<CellNodeVo> cellNodes;
    
    /**
     * 传入变量
     */
    private List<String> inputVar;
    
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
    
    public List<CellNodeVo> getCellNodes()
    {
        return cellNodes;
    }
    
    public void setCellNodes(List<CellNodeVo> cellNodes)
    {
        this.cellNodes = cellNodes;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getItems()
    {
        return items;
    }
    
    public void setItems(String items)
    {
        this.items = items;
    }
    
    public List<String> getContentVar()
    {
        return contentVar;
    }
    
    public void setContentVar(List<String> contentVar)
    {
        this.contentVar = contentVar;
    }

    public List<String> getInputVar()
    {
        return inputVar;
    }

    public void setInputVar(List<String> inputVar)
    {
        this.inputVar = inputVar;
    }
}
