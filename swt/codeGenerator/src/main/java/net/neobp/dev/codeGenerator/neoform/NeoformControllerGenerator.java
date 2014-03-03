package net.neobp.dev.codeGenerator.neoform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.neobp.dev.codeGenerator.JavaCodeTemplate;
import net.neobp.dev.codeGenerator.StrUtil;
import net.neobp.dev.codeGenerator.neoform.model.Classname;
import net.neobp.dev.codeGenerator.neoform.model.NeoformProperty;

public class NeoformControllerGenerator extends NeoformGenerator
{
    public void generate() throws IOException {
        final String classname=getModel().getControllerClassnameTemplate().getGenClassname().getName();
        final File file=fileForClass(classname);
        PrintWriter pw=null;
        try {
            pw=new PrintWriter(new FileWriter(file));
            pw.println(getCode());
        }finally{
            pw.close();
        }
    }
    
    private String getCode() {
        final JavaCodeTemplate jct=new JavaCodeTemplate();

        final Classname classname=getModel().getControllerClassnameTemplate().getGenClassname();
        jct.setPackageName(classname.getPackageName());
        jct.setClassName(classname.getSimpleName());
        
        if(getModel().getControllerClassnameTemplate().getExtClassname()!=null) {
            jct.addImport(getModel().getControllerClassnameTemplate().getExtClassname().getName());
            jct.setExtends(getModel().getControllerClassnameTemplate().getExtClassname().getSimpleName());
        }

        jct.addImport("java.util.List");
        jct.addImport("java.util.Collection");
        jct.addImport("net.neobp.neoform.validation.ValidationMessage");
        jct.addImport("net.neobp.neoform.value.DirtyListener");
        jct.addImport("net.neobp.neoform.value.DirtyProvider");
        jct.addImport("net.neobp.neoform.validation.ValidationStateListener");
        jct.addImport(getModel().getModelClass().getName());

        List<String> mContentsCreated=new ArrayList<String>();
        mContentsCreated.add("/** Called after the forms createContents() was called");
        mContentsCreated.add(" * @param form, the initialized form */");
        jct.addImport(getModel().getFormClassnameTemplate().getUsrClassName().getName());
        mContentsCreated.add("public void contentsCreated("+getModel().getFormClassnameTemplate().getUsrClassName().getSimpleName()+
                " form) {");
        for(NeoformProperty prop : getModel().getProperties()) {
            mContentsCreated.add("\tform.get"+StrUtil.firstUp(prop.getName())+"Adapter().addDirtyListener(dirtyTracker);");
            mContentsCreated.add("\tform.get"+StrUtil.firstUp(prop.getName())+"Adapter().addValidationResultListener(validationTracker);");
        }
        /*
        mContentsCreated.add("\tdirtyTracker.addDirtyListener(new DirtyListener() {");
        mContentsCreated.add("\t    @Override");
        mContentsCreated.add("\t    public void isDirty(DirtyProvider src, boolean isDirty)");
        mContentsCreated.add("\t    {");
        mContentsCreated.add("\t        onDirtyChanged(isDirty);");
        mContentsCreated.add("\t    }");
        mContentsCreated.add("\t});\n");
        
        mContentsCreated.add("\tvalidationTracker.addValidationStateListener(new ValidationStateListener() {");
        mContentsCreated.add("\t    @Override");
        mContentsCreated.add("\t    public void validationStateChanged(boolean isValid)");
        mContentsCreated.add("\t    {");
        mContentsCreated.add("\t        onValidationStateChanged(isValid);");
        mContentsCreated.add("\t    }");

        mContentsCreated.add("\t    @Override");
        mContentsCreated.add("\t    public void validationMessagesChanged(List<Collection<ValidationMessage>> messages)");
        mContentsCreated.add("\t    {");
        mContentsCreated.add("\t        onValidationMessagesChanged(messages);");
        mContentsCreated.add("\t    }");
        mContentsCreated.add("\t});\n");
        */

        String simpleModelClassname=getModel().getModelClass().getSimpleName();
        mContentsCreated.add("\tform.addModelObjectChangeListener(new ModelObjectChangeListener<"+
                simpleModelClassname+">() {");
        mContentsCreated.add("\t    @Override");
        mContentsCreated.add("\t    public void modelObjectWasSet("+simpleModelClassname+" model, boolean isNew)");
        mContentsCreated.add("\t    {");
        mContentsCreated.add("\t        onModelObjectWasSet(model, isNew);");
        mContentsCreated.add("\t    }");
        mContentsCreated.add("\t});");
        mContentsCreated.add("}");
        jct.addMethod(mContentsCreated);

        List<String> mMOWC=new ArrayList<String>();
        mMOWC.add("/** Override this method to get ModelObjectChanged events.");
        mMOWC.add("* @param model the set model object, most likely another one than the last call to this");
        mMOWC.add("* method.");
        mMOWC.add("* @param isNew indicates if the model object is one existing on the persistence layer, or a ");
        mMOWC.add("* new one.");
        mMOWC.add("*/");
        mMOWC.add("public void onModelObjectWasSet(final "+simpleModelClassname+" model, boolean isNew) {");
        mMOWC.add("}");
        jct.addMethod(mMOWC);
        
        jct.addImport("net.neobp.neoform.gui.ModelObjectChangeListener");
        List<String> mPropChange=new ArrayList<String>();
        mPropChange.add("/** Override this method to get propertyChangedEvents");
        mPropChange.add(" * @param prop the changed Property */");
        mPropChange.add("public void propertyChanged(final "+getModel().getFormClassnameTemplate().getGenClassname().getSimpleName()+".Props prop) {");
        mPropChange.add("\t//TODO perform model validation");
        mPropChange.add("}");
        jct.addMethod(mPropChange);
        
        List<String> mVMC=new ArrayList<String>();
        mVMC.add("/** Override this method to get validationMessagesState-events");
        mVMC.add(" * @param messages the new list of ValidationMessages");
        mVMC.add(" */");
        mVMC.add("protected void onValidationMessagesChanged(List<Collection<ValidationMessage>> messages) {");
        mVMC.add("\t//TODO update widgets to show the messages");
        mVMC.add("}");
        jct.addMethod(mVMC);

        List<String> mVSC=new ArrayList<String>();
        mVSC.add("/** Override this method to get validationState-events");
        mVSC.add(" * @param isValid the new state of the validationTracker");
        mVSC.add(" */");
        mVSC.add("protected void onValidationStateChanged(boolean isValid) {");
        mVSC.add("\t//TODO update button enabled states");
        mVSC.add("}");
        jct.addMethod(mVSC);

        List<String> mDC=new ArrayList<String>();
        mDC.add("/** Override this method to get dirtyState-events");
        mDC.add(" * @param the new state of the dirtyTracker");
        mDC.add(" */");
        mDC.add("public void onDirtyChanged(boolean isDirty) {");
        mDC.add("\t//TODO update button enabled states");
        mDC.add("}");
        jct.addMethod(mDC);

        return jct.toString();
    }

}
