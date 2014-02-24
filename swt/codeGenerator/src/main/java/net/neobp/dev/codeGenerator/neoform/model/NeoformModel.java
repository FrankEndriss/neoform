package net.neobp.dev.codeGenerator.neoform.model;

import java.util.Collection;

import net.neobp.dev.codeGenerator.neoform.NeoformDocumentHandler.FormType;
import net.neobp.dev.codeGenerator.neoform.model.NeoformModelImpl.ClassnameTemplate;

public interface NeoformModel {

	/**  The type of a form controlls the code generation process. Somewhere is a definition
	 * on which generators are generating which FormType.
	 * @return the type of the form
	 **/
	public FormType getFormType();
	
	/** @return the models classname of the form, which means most likely that class must be available at generation time. */
	public Class<?> getModelClass();
	
	/**
	 * @return the informational name of the form, should be unique among all forms, and is used
	 * as a base of other names used while generation of artifacts of the form.
	 */
	public String getFormName();
	
	/**
	 * @return the classname template of the form class
	 */
	public ClassnameTemplate getFormClassnameTemplate();
	
	/**
	 * @return the classname template of the context class
	 */
	public ClassnameTemplate getContextClassnameTemplate();
	
	/**
	 * @return the classname template of the controller
	 */
	public ClassnameTemplate getControllerClassnameTemplate();

	/**
	 * @return the classname template of the layout
	 */
    public ClassnameTemplate getLayoutClassnameTemplate();

	/**
	 * @return the classname template of the component
	 */
    public ClassnameTemplate getComponentClassnameTemplate();

	/**
	 * @return the classname template of the factory class
	 */
    public ClassnameTemplate getFactoryClassnameTemplate();

    /**
     * @return the classname template of the IPageWrapper
     */
    public ClassnameTemplate getIPageWrapperClassnameTemplate();

    /**
     * @return the classname template of the form's Builder
     */
    public ClassnameTemplate getBuilderClassnameTemplate();

	/**
	 * @return the context field definitions (i.e. spring bean names)
	 */
	public Collection<NeoformContextField> getContextFields();
	
	/** @return properties (fields) of this form */
	public Collection<NeoformProperty> getProperties();

	/** 
	 * @return the actions/buttons of this form
	 */
	public Collection<NeoformAction> getActions();

    /**
     * @return a list of subforms used in this form
     */
    public Collection<String> getSubforms();

    /** Adds a context field, used in subform feature.
     * @param springName bean name of the context field
     * @param classname of the bean
     */
    public void addContextRef(String fieldName, String beanName, Classname classname);

    /** Adds the factory class of this NeoformModel to the contextFields of this model.
     * Usually called once after parsing.
     */
    public void addFactory2ContextFields();

}
