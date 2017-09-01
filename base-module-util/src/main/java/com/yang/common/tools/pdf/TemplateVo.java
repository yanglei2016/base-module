package com.yang.common.tools.pdf;

import java.util.ArrayList;
import java.util.List;

import com.yang.common.tools.pdf.entity.BaseNodeVo;
import com.yang.common.tools.pdf.entity.GlobalNodeVo;

public class TemplateVo extends BaseEntity
{
    /**
     * 模版文件名
     */
    private String fileName;
    
    /**
     * 段落
     */
    private List<BaseNodeVo> xmlNodeList;
    
    private GlobalNodeVo globalNodeVo;
    
    public String getFileName()
    {
        return fileName;
    }
    
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    
    public List<BaseNodeVo> getXmlNodeList()
    {
        return xmlNodeList;
    }
    
    public void setXmlNodeList(List<BaseNodeVo> xmlNodeList)
    {
        
        this.xmlNodeList = xmlNodeList;
    }
    
    public void addNode(BaseNodeVo baseNodeVo)
    {
        if (baseNodeVo == null)
        {
            return;
        }
        
        if (xmlNodeList == null)
        {
            xmlNodeList = new ArrayList<BaseNodeVo>();
        }
        
        xmlNodeList.add(baseNodeVo);
    }
    
    public GlobalNodeVo getGlobalNodeVo()
    {
        return globalNodeVo;
    }
    
    public void setGlobalNodeVo(GlobalNodeVo globalNodeVo)
    {
        this.globalNodeVo = globalNodeVo;
    }
}
