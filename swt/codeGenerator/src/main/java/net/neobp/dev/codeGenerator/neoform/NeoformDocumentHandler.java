package net.neobp.dev.codeGenerator.neoform;

import net.neobp.dev.codeGenerator.StrUtil;
import net.neobp.dev.codeGenerator.neoform.model.Classname;
import net.neobp.dev.codeGenerator.neoform.model.NeoformContextFieldImpl;
import net.neobp.dev.codeGenerator.neoform.model.NeoformModel;
import net.neobp.dev.codeGenerator.neoform.model.NeoformModelImpl;
import net.neobp.dev.codeGenerator.neoform.model.NeoformProperty;
import net.neobp.dev.codeGenerator.neoform.model.NeoformProperty.Type;
import net.neobp.dev.codeGenerator.neoform.model.NeoformPropertyImpl;
import net.neobp.dev.codeGenerator.neoform.model.Widget;
import net.neobp.dev.codeGenerator.neoform.widget.CheckboxWidget;
import net.neobp.dev.codeGenerator.neoform.widget.GenericWidget;
import net.neobp.dev.codeGenerator.neoform.widget.TableWidget;
import net.neobp.dev.codeGenerator.neoform.widget.TextWidget;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** Sax parser to create instances of NeoformModel from xml files.
 */
public class NeoformDocumentHandler implements ContentHandler {

	public interface ResultHandler {
		public void finishedParsingNeoform(NeoformModel neoformModel);
	}


	public final String TAG_CONTEXT="Context";
	public final String TAG_CONTEXT_FIELD="Field";
	public final String TAG_FORM="Form";
	public final String TAG_PROPERTY="Property";
    public final String TAG_WIDGET = "Widget";
    public final String TAG_CONSTRUCTOR_ARG="ConstructorArg";
	
	/** States of the parser */
	private enum State {
		BASE,
		IN_FORM,
		IN_FORM_CONTEXT,
		IN_FORM_PROPERTY,
		IN_WIDGET, 
		IN_CONSTRUCTOR_ARG
	};
	
	/** Type of the form */
	public enum FormType {
		MULTI,
		SINGLE
	}
	
	private enum MultiFormStyle {
		TABLE,
		TREE,
		FREE
	}
	
	private enum SingleFormStyle {
		STANDARD_FIELDS,
		FREE
	}
	
	
	/** State of the parser, keeps track of which tag occurs within which other tag. */
	private State state;
	
	/** The result handler is called after every finished parsed form */
	private final ResultHandler resultHandler;
	
	/** The current form while parsing. */
	private NeoformModelImpl currentForm;
	
	/** The current property while parsing. */
	private NeoformPropertyImpl currentProperty;

	/** ClassLoader to load the forms model classes */
	private final ClassLoader userClassLoader;

	/** Locator for nice error reporting */
	private Locator locator;

	/**
	 * @param resultHandler callback which is called after every complete form
	 * @param userClassLoader ClassLoader to load the forms model classes
	 */
	public NeoformDocumentHandler(ResultHandler resultHandler, ClassLoader userClassLoader) {
		this.resultHandler=resultHandler;
		this.userClassLoader=userClassLoader;
	}

	public void setDocumentLocator(Locator locator) {
		this.locator=locator;
	}

	public void startDocument() throws SAXException {
		state=State.BASE;
	}

	public void endDocument() throws SAXException {
		if(state!=State.BASE)
			throw new SAXParseException("document end, but form not finished", locator);
	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
	    if(TAG_CONTEXT.equals(qName)) {

			if(state!=State.IN_FORM) 
				throw new SAXParseException("Tag <"+TAG_CONTEXT+"> at bad location (State!=IN_FORM)", locator);
			state=State.IN_FORM_CONTEXT;
			
			String usrClassName=atts.getValue("usrClassName");
			if(usrClassName!=null)
			    currentForm.getContextClassnameTemplate().setUsrClassName(usrClassName);
			String extClassName=atts.getValue("extClassName");
			if(extClassName!=null)
			    currentForm.getContextClassnameTemplate().setExtClassname(extClassName);
			
			/*
			currentForm.addContextField(new NeoformContextFieldImpl(
			        "formToolkit",
			        "formToolkit"
			        ));
			        */

			
	    } else if(TAG_CONTEXT_FIELD.equals(qName)) {
			if(state!=State.IN_FORM_CONTEXT) 
				throw new SAXParseException("Tag <"+TAG_CONTEXT_FIELD+"> at bad location (State!=IN_CONTEXT_FIELD)", locator);

			currentForm.addContextField(new NeoformContextFieldImpl(
			        atts.getValue("name"),
			        atts.getValue("ref")
			        ));

	    } else if(TAG_FORM.equals(qName)) {

			if(state!=State.BASE) 
				throw new SAXParseException("Tag <"+TAG_FORM+"> at bad location (State!=BEFORE_FORM)", locator);
			state=State.IN_FORM;
			
			currentForm=new NeoformModelImpl();
			
			currentForm.setFormName(atts.getValue("formName"));
			//currentForm.getFormClassnameTemplate().usrClassName=atts.getValue("formClass");
			currentForm.setFormType(FormType.valueOf(atts.getValue("formType")));
			String className=atts.getValue("modelClassName");
			if(className==null)
				throw new SAXParseException("attribute modelClassName is null", locator);
			try {
				currentForm.setModelClass(userClassLoader.loadClass(className));
			} catch (ClassNotFoundException e) {
				throw new SAXParseException("cannot load class: "+className, locator, e);
			}
		} else if(TAG_PROPERTY.equals(qName)) {

			if(state!=State.IN_FORM) 
				throw new SAXParseException("Tag <"+TAG_PROPERTY+"> at bad location (State!=IN_FORM)", locator);
			state=State.IN_FORM_PROPERTY;

			currentProperty=new NeoformPropertyImpl();
			// TODO check for null of name and type
			currentProperty.setName(atts.getValue("name"));
			currentProperty.setModelPropertyName(atts.getValue("modelPropertyName"));
			currentProperty.setType(NeoformProperty.Type.valueOf(atts.getValue("type")));
			currentProperty.setWidget(getWidgetForType(currentProperty.getType()));
			currentProperty.setLabeled(!"false".equals(atts.getValue("hasLabel")));
			if(currentProperty.isLabeled())
			    currentProperty.setLabelKey(atts.getValue("labelKey"));
			currentProperty.setReadonly("true".equals(atts.getValue("readonly")));
			currentProperty.setSubform(atts.getValue("subform"));
			if(currentProperty.getSubform()!=null)
			    currentForm.addSubform(currentProperty.getSubform());
			
			currentForm.addProperty(currentProperty);
		} else if(TAG_WIDGET.equals(qName)) {
		    if(state!=State.IN_FORM_PROPERTY)
				throw new SAXParseException("Tag <"+TAG_WIDGET+"> at bad location (State!=IN_FORM_PROPERTY)", locator);
		    state=State.IN_WIDGET;

		    Widget widget=currentProperty.getWidget();
		    String widgetClassname=atts.getValue("widgetClassname");
		    
		    if(widget==null) { // type==Generic || type==Subform
		        GenericWidget gWidget=new GenericWidget(
		                widgetClassname,
		                atts.getValue("valueClassname"));
		        currentProperty.setWidget(gWidget);
		    }
		    
		    if(currentProperty.getType()==Type.Subform) {
		        // add a context field
		        Classname contextFieldClassname=new Classname(widgetClassname);

		        NeoformContextFieldImpl contextField=new NeoformContextFieldImpl(
		                StrUtil.firstDown(contextFieldClassname.getSimpleName()),
		                atts.getValue("valueClassname")
			        );
		        contextField.setFieldClassname(new Classname(widgetClassname));
		        currentForm.addContextField(contextField);
		    }
		} else if(TAG_CONSTRUCTOR_ARG.equals(qName)) {
		    if(state!=State.IN_WIDGET)
				throw new SAXParseException("Tag <"+TAG_CONSTRUCTOR_ARG+"> at bad location (State!=IN_WIDGET)", locator);
		    state=State.IN_CONSTRUCTOR_ARG;
		    Widget widget=currentProperty.getWidget();
		    widget.addConstructorArg(atts.getValue("value"));
		} else
			throw new SAXParseException("unknown qName/tag:<"+qName+">", null);

	}
	
	private Widget getWidgetForType(NeoformProperty.Type type) {
	    switch(type) {
	        case Text: return new TextWidget();
	        case Checkbox: return new CheckboxWidget();
	        case Table: return new TableWidget();
	        case Generic:
	        case Subform: return null;
	        default:
	            throw new IllegalArgumentException("Unknown NeoformProperty.Type: "+type);
	    }
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(TAG_FORM.equals(qName)) {
			resultHandler.finishedParsingNeoform(currentForm);
			currentForm=null;
			state=State.BASE;
		} else if(TAG_PROPERTY.equals(qName)) {
		    if(currentProperty.getWidget()==null)
		        throw new SAXParseException("Property has no Widget, type="+currentProperty.getType(), locator);
			currentProperty=null;
			state=State.IN_FORM;
		} else if(TAG_CONTEXT.equals(qName)) {
			state=State.IN_FORM;
		} else if(TAG_CONTEXT_FIELD.equals(qName)) {
		    // empty
		} else if(TAG_CONSTRUCTOR_ARG.equals(qName)) {
		    state=State.IN_WIDGET;
		} else if(TAG_WIDGET.equals(qName)) {
		    state=State.IN_FORM_PROPERTY;
		}
		    else
			throw new SAXParseException("unknown qName/tag: "+qName, locator);
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// ignore
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// ignore
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
		// ignore
	}

	public void skippedEntity(String name) throws SAXException {
		// ignore
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
	}

	public void endPrefixMapping(String prefix) throws SAXException {
	}


}
