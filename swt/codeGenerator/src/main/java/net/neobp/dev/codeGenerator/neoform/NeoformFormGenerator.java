package net.neobp.dev.codeGenerator.neoform;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.neobp.dev.codeGenerator.JavaCodeTemplate;
import net.neobp.dev.codeGenerator.StrUtil;
import net.neobp.dev.codeGenerator.neoform.model.Classname;
import net.neobp.dev.codeGenerator.neoform.model.NeoformAction;
import net.neobp.dev.codeGenerator.neoform.model.NeoformProperty;

/** This CodeGenerator generates a class which implements Neoform<Composite, M>.
 * The Neoform consist of the description neoformModel given in setModel(NeoformModel)
 */
public class NeoformFormGenerator extends NeoformGenerator {

	public void generate() throws IOException {
		final File outputFile = fileForClass(getModel().getFormClassnameTemplate().getGenClassname().getName());
		
		PrintWriter pw=null;
		try {
		    pw = new PrintWriter(new FileWriter(outputFile));
		    try {
			    writeCode(pw);
		    }catch(IntrospectionException e) {
			    throw new RuntimeException("Exception while introspection", e);
		    }
		}finally{
		    if(pw!=null)
		        pw.close();
		}
	}

	private void writeCode(PrintWriter pw) throws IntrospectionException {
	    JavaCodeTemplate template=new JavaCodeTemplate();
	    
		template.setPackageName(getModel().getFormClassnameTemplate().getGenClassname().getPackageName());
		template.setClassDoc("/** Generated from "+getClass().getName()+" */");
		template.setClassName(getModel().getFormClassnameTemplate().getGenClassname().getSimpleName());
		template.setExtends("AbstractNeoform<"+getModel().getModelClass().getSimpleName()+">");

		final String[] imports={
		        "java.util.HashMap",
		        "java.util.Map",

		        "net.neobp.neoform.swt.gui.AbstractNeoform",
		        "net.neobp.neoform.gui.ModelHolder",
		        "net.neobp.neoform.swt.gui.NeoformLayout",
		        "net.neobp.neoform.gui.PersistenceCallback",
		        "net.neobp.neoform.validation.ValueValidator",
		        "net.neobp.neoform.swt.value.SwtComponentAdapter",
		        "net.neobp.neoform.value.ValueChangeListener",
		        "net.neobp.neoform.value.ValueHolder",
		        "net.neobp.neoform.swt.widget.NeoformCheckbox",
		        "net.neobp.neoform.swt.widget.NeoformLabel",
		        "net.neobp.neoform.swt.widget.NeoformText",
		        "org.eclipse.swt.widgets.Composite",
		        getModel().getModelClass().getName(),
		};

		for(String s : imports)
		    template.addImport(s);

		// enum Properties
		final List<String> enumProps=new ArrayList<String>();
		enumProps.add("public enum Props {");
		boolean first=true;
		String code="";
		for(NeoformProperty prop : getModel().getProperties()) {
		    code+=((first?"\t\t":",\n\t\t")+prop.getName());
		    first=false;
		}
		enumProps.add(code);
		enumProps.add("}");
		template.addOtherCode(enumProps);
		
		// enum Actions
		final List<String> enumActions=new ArrayList<String>();
		enumActions.add("public enum Actions {");
		first=true;
		code="";
		for(NeoformAction action : getModel().getActions()) {
		    code+=((first?"\t\t":",\n\t\t")+action.getName());
		    first=false;
		}
		enumActions.add(code);
		enumActions.add("}");
		template.addOtherCode(enumActions);

		template.addPropertyDef("private Map<Props, SwtComponentAdapter<?, ?>> property2AdapterMap=new HashMap<Props, SwtComponentAdapter<?, ?>>()");
		template.addPropertyDef("private Map<Props, NeoformLabel> property2LabelMap=new HashMap<Props, NeoformLabel>()");

		for(NeoformAction action : getModel().getActions())
		    template.addPropertyDef("private Button "+action.getName());

		// TODO inner class ExtraProperties
		
		// Factory getter and setter
        final String factoryClassname=getModel().getFactoryClassnameTemplate().getUsrClassName().getName();
        final String factorySimpleClassname=getModel().getFactoryClassnameTemplate().getUsrClassName().getSimpleName();
        template.addImport(factoryClassname);
        template.addPropertyDef("private "+factorySimpleClassname+" factory");
        
        final List<String> mSetFactory=new ArrayList<String>();
        mSetFactory.add("public void setFactory("+factorySimpleClassname+" factory) {");
        mSetFactory.add("\tthis.factory=factory;");
        mSetFactory.add("}");
        template.addMethod(mSetFactory);
        
        final List<String> mGetFactory=new ArrayList<String>();
        mGetFactory.add("public "+factorySimpleClassname+" getFactory() {");
        mGetFactory.add("\treturn factory;");
        mGetFactory.add("}");
        template.addMethod(mGetFactory);
        
		
		// Layout getter and setter
        final Classname clsForm=getModel().getFormClassnameTemplate().getGenClassname();
        final String clsActions=clsForm.getSimpleName()+".Actions";
        final String clsProps=clsForm.getSimpleName()+".Props";
		final String layoutClassname="NeoformLayout<"+clsProps+", "+clsActions+">";

		//final String layoutSimpleClassname=getModel().getLayoutClassnameTemplate().getUsrClassName().getSimpleName();
		//template.addImport(layoutClassname);
		template.addPropertyDef("private "+layoutClassname+" layout");
		
		final List<String> mSetLayout=new ArrayList<String>();
		mSetLayout.add("public void setLayout("+layoutClassname+" layout) {");
		mSetLayout.add("\tthis.layout=layout;");
		mSetLayout.add("}");
		template.addMethod(mSetLayout);
		
		final List<String> mGetLayout=new ArrayList<String>();
		mGetLayout.add("public "+layoutClassname+" getLayout() {");
		mGetLayout.add("\treturn layout;");
		mGetLayout.add("}");
		template.addMethod(mGetLayout);
		
		// Context getter and setter
		final String contextClassname=getModel().getContextClassnameTemplate().getGenClassname().getName();
		final String contextSimpleClassname=getModel().getContextClassnameTemplate().getGenClassname().getSimpleName();
		template.addImport(contextClassname);
		template.addPropertyDef("private "+contextSimpleClassname+" context");

		final List<String> mSetContext=new ArrayList<String>();
		mSetContext.add("public void setContext("+contextSimpleClassname+" context) {");
		mSetContext.add("\tthis.context=context;");
		mSetContext.add("}");
		template.addMethod(mSetContext);
		
		final List<String> mGetContext=new ArrayList<String>();
		mGetContext.add("public "+contextSimpleClassname+" getContext() {");
		mGetContext.add("\treturn context;");
		mGetContext.add("}");
		template.addMethod(mGetContext);
		
		// controller getter and setter
		final String controllerClassname=getModel().getControllerClassnameTemplate().getUsrClassName().getName();
		final String controllerSimpleClassname=getModel().getControllerClassnameTemplate().getUsrClassName().getSimpleName();
		template.addImport(controllerClassname);
		template.addPropertyDef("private "+controllerSimpleClassname+" controller");
		
		final List<String> mSetController=new ArrayList<String>();
		mSetController.add("public void setController("+controllerSimpleClassname+" controller) {");
		mSetController.add("\tthis.controller=controller;");
		mSetController.add("}");
		template.addMethod(mSetController);
		
		final List<String> mGetController=new ArrayList<String>();
		mGetContext.add("public "+controllerSimpleClassname+" getController() {");
		mGetContext.add("\treturn controller;");
		mGetContext.add("}");
		template.addMethod(mGetController);
		
		// initialisation via createContents()
		final List<String> m=new ArrayList<String>();
		m.add("public void createContents(final Composite parent) {");
        m.add("");
        m.add("\tlayout.layoutParent(parent);");
		m.add("\tNeoformLabel label;");
		m.add("\tComposite lParent;");
		m.add("\tObject layoutData;\n");

        int i=0;
		for(NeoformProperty prop : getModel().getProperties()) {
		    i=i+1;
			template.addImport(prop.getWidget().getClassname().getName());

			String lAdapVarName=StrUtil.camelCase(prop.getName()+"Adapter");
            final List<String> impList=new ArrayList<String>();
			final String typeofProp=ClazzUtil.getJavaType(getModel().getModelClass(), prop, impList);
			template.addImport(getModel().getModelClass().getName());
			for(String imp : impList)
			    template.addImport(imp);

		    // constructor call to create label
			m.add("\tlParent=layout.getParentForPropertyLabel(Props."+prop.getName()+");");
			m.add("\tif(lParent!=null) {");
			m.add("\t\tlabel=factory.create"+StrUtil.firstUp(prop.getName())+"Label(context, lParent);");
			m.add("\t\tlayoutData=layout.getLayoutDataForProperty(Props."+prop.getName()+");");
			m.add("\t\tif(layoutData!=null)");
			m.add("\t\t\tlabel.setLayoutData(layoutData);");
		    m.add("\t\tproperty2LabelMap.put(Props."+prop.getName()+", label);");
		    m.add("\t}\n");
		    
		    // constructor call to create control
		    m.add("\tlParent=layout.getParentForProperty("+ "Props."+prop.getName()+");");
		    m.add("\tif(lParent!=null) {");
		    m.add("\t\tfinal "+prop.getWidget().getClassname().getSimpleName()+" control"+i+"="+
		            "factory.create"+StrUtil.firstUp(prop.getName())+"Control(context, lParent);");
		    m.add("\t\tlayoutData=layout.getLayoutDataForProperty(Props."+prop.getName()+");");
			m.add("\t\tif(layoutData!=null)");
		    m.add("\t\t\tcontrol"+i+".setLayoutData(layoutData);");

		    // constructor call to create adapter
		    template.addImport(prop.getWidget().getClassname().getName());
		    m.add("\t\t"+lAdapVarName+"=factory.create"+StrUtil.firstUp(prop.getName())+
		            "Adapter(control"+i+", controller, this);");
		    m.add("\t\tproperty2AdapterMap.put(Props."+prop.getName()+", "+lAdapVarName+");");
		    m.add("\t}\n");

		    // getter method for the Adapter object per widget
			String widgetSimpleName=prop.getWidget().getClassname().getSimpleName();
            // Hack to add type params for NeoformTable
            // TODO: add generic type support to Widget
            if("NeoformTable".equals(widgetSimpleName)) {
                template.addImport("java.util.Collection");
                // typeofProp is "Collection<something>"
                // what we need is "something"
                // TODO import uncardinalTypeofProp (most likely via ClazzUtil)
                String uncardinalTypeofProp=typeofProp.replaceFirst("Collection<", "");
                uncardinalTypeofProp=uncardinalTypeofProp.substring(0, uncardinalTypeofProp.length()-1);
                widgetSimpleName+=("<"+typeofProp+", "+uncardinalTypeofProp+">");
            }

			String adapterType="SwtComponentAdapter<"+widgetSimpleName+", "+typeofProp+"> ";
		    template.addPropertyDef("private "+adapterType+" "+lAdapVarName);

            // one public getter for every properties widget adapter
            final List<String> mAdapterGetter=new ArrayList<String>();
            mAdapterGetter.add("public "+adapterType+"get"+StrUtil.firstUp(lAdapVarName)+"() {");
            mAdapterGetter.add("\treturn "+lAdapVarName+";");
            mAdapterGetter.add("}");
            template.addMethod(mAdapterGetter);

		}

		m.add("}");
		template.addMethod(m);

		// model2Screen()
		final List<String> mModel2Screen=new ArrayList<String>();
		mModel2Screen.add("public void model2Screen() {");
		for(NeoformProperty prop : getModel().getProperties()) {
			String modelReadCode;
		    if("this".equals(prop.getModelPropertyName()))
		        modelReadCode="getModel()";
		    else
		        modelReadCode="getModel()."+getGetterCode(getModel().getModelClass(), prop.getName());
			mModel2Screen.add("\t"+StrUtil.camelCase(prop.getName())+"Adapter.setComponentValue("+modelReadCode+");");
			
		}
		mModel2Screen.add("}");
		template.addMethod(mModel2Screen);
		
		pw.print(template.toString());
	}
	

	/** Returns the code line which reads a property.
	 * @param clazz the class to do the call on
	 * @param prop probably dotted property name
	 * @return the code which calls the getter(s)
	 * @throws IntrospectionException
	 */
	private String getGetterCode(Class<?> clazz, String dottedProp) throws IntrospectionException {
		String ret="";
		boolean first=true;
		Class<?> nextClazz=clazz;
		for(String s : StrUtil.getSplittedDotString(dottedProp)) {
			if(!first)
				ret+=".";
			final Method m=ClazzUtil.getGetterMethod(nextClazz, s);
			ret+=(m.getName()+"()");
			nextClazz=m.getReturnType();
			// TODO: null checks in generated code
		}
		return ret;
	}

	/** Generates a usual getter name "getXyz()"
	 * @param prop the property name
	 * @return the getter name without parantheses
	 */
	private String generateGetterName(String prop) {
		return "get"+StrUtil.firstUp(StrUtil.camelCase(prop));
	}
}
