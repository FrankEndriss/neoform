package net.neobp.dev.codeGenerator.neoform.model;

public interface NeoformProperty {
	enum Type {
		Text,
		Date,
		Time,
		Checkbox,
		Table,
		Dropdown,
		Generic,
		Subform
	}

	/** @return the name of the property */
	public String getName();

    /**
     * @return true if this property has an Label, else false. defaults to true
     */
    public boolean isLabeled();

	/** @return the label for this property, called if isLabeled() returns true */
	public String getLabelKey();
	
	/** @return the (display) type of the property */
	public Type getType();

	/**
	 * @return the widget used to edit/display this property
	 */
	public Widget getWidget();

    /**
     * @return the name of the model objects property, defaults to getName()
     */
    public String getModelPropertyName();

    /** Indicates if this is a readonly-Property, for which there is no setter-method
     * in the model class.
     * @return true if this property should not be written, only readed. Default is false.
     */
    public boolean isReadonly();

    /**
     * @return the name of the subform this Property uses
     */
    public String getSubform();
}
