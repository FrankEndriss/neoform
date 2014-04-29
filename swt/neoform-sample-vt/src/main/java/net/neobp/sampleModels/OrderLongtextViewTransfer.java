package net.neobp.sampleModels;

/** Sample class of line based floating text of OrderHeader
 */
public class OrderLongtextViewTransfer {
    
    private String guid;
    private int lineNo;
    private String line;
    private String format;

    public String getGuid()
    {
        return guid;
    }
    public void setGuid(String guid)
    {
        this.guid = guid;
    }
    public int getLineNo()
    {
        return lineNo;
    }
    public void setLineNo(int lineNo)
    {
        this.lineNo = lineNo;
    }
    public String getLine()
    {
        return line;
    }
    public void setLine(String line)
    {
        this.line = line;
    }
    public String getFormat()
    {
        return format;
    }
    public void setFormat(String format)
    {
        this.format = format;
    }

}
