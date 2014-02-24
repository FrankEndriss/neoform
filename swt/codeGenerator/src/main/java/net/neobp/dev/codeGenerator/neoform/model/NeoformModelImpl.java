package net.neobp.dev.codeGenerator.neoform.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.neobp.dev.codeGenerator.StrUtil;
import net.neobp.dev.codeGenerator.neoform.NeoformDocumentHandler.FormType;

public class NeoformModelImpl implements NeoformModel, NamedForm {

    private String formName;

    private ClassnameTemplate formTempl=new ClassnameTemplate("Form");
    private ClassnameTemplate controllerTempl=new ClassnameTemplate("Controller");
    private ClassnameTemplate contextTempl=new ClassnameTemplate("Context");
    private ClassnameTemplate layoutTempl=new ClassnameTemplate("Layout");
    private ClassnameTemplate componentTempl=new ClassnameTemplate("Component");
    private ClassnameTemplate ipageWrapperTempl=new ClassnameTemplate("IPageWrapper");
    private ClassnameTemplate factoryTempl=new ClassnameTemplate("Factory");
    private ClassnameTemplate builderTempl=new ClassnameTemplate("Builder");

    { // default implementation baseclasses 
        formTempl.setExtClassname("net.neobp.neoform.gui.AbstractNeoform");
        controllerTempl.setExtClassname("net.neobp.neoform.gui.AbstractNeoformController");
    }
    
	private FormType formType;
	private Class<?> modelClass;

	private final List<NeoformProperty> properties=new ArrayList<NeoformProperty>();
    private final List<NeoformAction> actions=new ArrayList<NeoformAction>();
    private final List<NeoformContextField> contextFields=new ArrayList<NeoformContextField>();

    private Set<String> subforms=new HashSet<String>();

    public ClassnameTemplate getFormClassnameTemplate() {
        return formTempl;
    }
    
    public ClassnameTemplate getControllerClassnameTemplate() {
        return controllerTempl;
    }
    
    public ClassnameTemplate getLayoutClassnameTemplate() {
        return layoutTempl;
    }

    public ClassnameTemplate getContextClassnameTemplate() {
        return contextTempl;
    }

    public ClassnameTemplate getComponentClassnameTemplate() {
        return componentTempl;
    }

    public ClassnameTemplate getIPageWrapperClassnameTemplate() {
        return ipageWrapperTempl;
    }

    public ClassnameTemplate getFactoryClassnameTemplate() {
        return factoryTempl;
    }

    public ClassnameTemplate getBuilderClassnameTemplate() {
        return builderTempl;
    }

	public void setFormType(FormType formType) {
		if(formType==null)
			throw new IllegalArgumentException("formType must not be null");
		this.formType=formType;
	}

	public FormType getFormType() {
		return formType;
	}

	public String getModelClassName() {
	    return getModelClass().getSimpleName();
	}

	public void setModelClass(final Class<?> modelClass) {
		if(modelClass==null)
			throw new IllegalArgumentException("modelClass must not be null");
		this.modelClass=modelClass;
	}

	public Class<?> getModelClass() {
		return modelClass;
	}

	public Collection<NeoformProperty> getProperties() {
		return Collections.unmodifiableCollection(properties);
	}
	
	public void addContextRef(String fieldName, String beanName, Classname classname) {
	    NeoformContextFieldImpl field=new NeoformContextFieldImpl(fieldName, beanName);
	    field.setFieldClassname(classname);
	    addContextField(field);
	}

	public void addContextField(NeoformContextField field) {
	    contextFields.add(field);
	}

	public void addProperty(NeoformProperty property) {
		if(property==null)
			throw new IllegalArgumentException("property must not be null");
		properties.add(property);
	}

    public Collection<NeoformContextField> getContextFields()
    {
        return contextFields;
    }

    public Collection<NeoformAction> getActions() {
        return actions;
    }

    public void setFormName(String formName) {
        if(formName==null)
            throw new RuntimeException("formName must not be null");
        this.formName=formName;
    }
    
    public String getFormName() {
        return formName;
    }
    
    public void addSubform(String subformName) {
        subforms.add(subformName);
    }

    public Collection<String> getSubforms() {
        return Collections.unmodifiableCollection(subforms);
    }

    public void addFactory2ContextFields() {
        NeoformContextFieldImpl field=new NeoformContextFieldImpl(
                getFactoryClassnameTemplate().getGenClassname().getSimpleName(),
                getFactoryClassnameTemplate().getSpringName());
        field.setFieldClassname(getFactoryClassnameTemplate().getGenClassname());
        addContextField(field);
    }

    /** Utility to create generic names.
     */
    public class ClassnameTemplate {
        private Classname extClassName;
        private final Classname genClassName;
        private final Classname usrClassName;
        private final Classname ifClassName;
        private String springName;
        
        private final String tag;
        
        /**
         * @param tag one of "Form", "Context" or "Controller" or the like
         */
        public ClassnameTemplate(String tag) {
            this.tag=tag;
            this.extClassName=null;
            this.genClassName=new Classname(NeoformModelImpl.this, tag);
            this.usrClassName=new Classname(NeoformModelImpl.this, tag);
            this.ifClassName=new Classname(NeoformModelImpl.this, tag);
        }

        /**
         * @return name of the class to extend, may be null to indicate "extends nothing"
         */
        public Classname getExtClassname() {
            return extClassName;
        }
        
        public void setExtClassname(String name) {
            if(extClassName==null)
                extClassName=new Classname(NeoformModelImpl.this, tag);
            extClassName.setName(name);
        }
        
        /**
         * @return the name of the generated class
         */
        public Classname getGenClassname() {
            return genClassName;
        }
        
        public void setGenClassname(String name) {
            genClassName.setName(name);
        }

        /** 
         * @return the name of the usr supplied class, which usually extends the generated one
         */
        public Classname getUsrClassName() {
            return usrClassName;
        }
        
        public void setUsrClassName(final String name) {
            usrClassName.setName(name);
        }

        /**
         * @return the name of the bean if used in spring configurations
         */
        public String getSpringName() {
            if(springName==null)
                return StrUtil.firstDown(getFormName())+StrUtil.firstUp(tag)+"Bean";
            return springName;
        }
        
        public void setSpringName(String name) {
            springName=name;
        }

        /**
         * @return the name of the interface class, usually implemented by the generated class,
         * and used in declarations in generated code.
         */
        public Classname getInterfaceClassName() {
            return ifClassName;
        }
        
        public void setIfClassName(final String name) {
            ifClassName.setName(name);
        }
    }
}
