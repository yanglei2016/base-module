package com.yang.common.tools.pdf.entity;

public class IfNodeVo extends BaseNodeVo
{
    private String test;
    
    private String equals;
    
    private BaseNodeVo baseNodeVo;
    
    public String getTest()
    {
        return test;
    }
    
    public void setTest(String test)
    {
        this.test = test;
    }
    
    public String getEquals()
    {
        return equals;
    }
    
    public void setEquals(String equals)
    {
        this.equals = equals;
    }
    
    public BaseNodeVo getBaseNodeVo()
    {
        return baseNodeVo;
    }
    
    public void setBaseNodeVo(BaseNodeVo baseNodeVo)
    {
        this.baseNodeVo = baseNodeVo;
    }
}
