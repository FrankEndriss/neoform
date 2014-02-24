package net.neobp.dev.codeGenerator.neoform.model;


public class NeoformPropertyImpl implements NeoformProperty {

	private String name;
	private String modelPropertyName; // defaults to getName()
	private Type type;
	private String labelKey;
	private Widget widget;
    private boolean hasLabel;
    private boolean readonly;
    private String subform;

	public void setName(final String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
	
	public void setModelPropertyName(final String modelPropertyName) {
	    this.modelPropertyName=modelPropertyName;
	}
	
    public String getModelPropertyName() {
	    if(modelPropertyName==null)
	        return getName();
	    return modelPropertyName;
	}

	public void setType(Type type) {
		this.type=type;
	}

	public Type getType() {
		return type;
	}

	public void setLabelKey(final String key) {
		this.labelKey=key;
	}
	public String getLabelKey() {
		return labelKey;
	}

	public void setWidget(final Widget widget) {
	    this.widget=widget;
	}

    public Widget getWidget()
    {
        return widget;
    }

    public boolean isLabeled()
    {
        return hasLabel;
    }

    public void setLabeled(boolean b)
    {
        this.hasLabel=b;
    }
    
    public boolean isReadonly()
    {
        return readonly;
    }

    public void setReadonly(boolean b) {
        this.readonly=b;
    }

    public String getSubform() {
        return subform;
    }

    public void setSubform(String value) {
        this.subform=value;
    }
}
