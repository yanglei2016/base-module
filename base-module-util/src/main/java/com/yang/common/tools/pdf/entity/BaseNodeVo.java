package com.yang.common.tools.pdf.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;

public class BaseNodeVo implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String nodeName;
    
    public String getNodeName()
    {
        return nodeName;
    }
    
    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }
    
    public Object deepClone()
        throws IOException, OptionalDataException, ClassNotFoundException
    {
        // 将对象写到流里
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(this);// 从流里读出来
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        return (oi.readObject());
    }
}
