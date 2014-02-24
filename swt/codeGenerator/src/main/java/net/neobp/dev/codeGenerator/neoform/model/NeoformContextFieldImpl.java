package net.neobp.dev.codeGenerator.neoform.model;


public class NeoformContextFieldImpl implements NeoformContextField {
    private final String fieldName;
    private final String beanRef;
    private Classname fieldClassname;

    public NeoformContextFieldImpl(String fieldName, String beanRef) {
        this.fieldName=fieldName;
        if(beanRef==null)
            throw new IllegalArgumentException("beanRef must not be null");
        this.beanRef=beanRef;
    }

    public String getFieldName()
    {
        if(fieldName==null)
            return getBeanRef();
        return fieldName;
    }

    public String getBeanRef()
    {
        return beanRef;
    }

    public Classname getFieldClassname()
    {
        return fieldClassname;
    }
    
    public void setFieldClassname(Classname classname) {
        fieldClassname=classname;
    }

}
