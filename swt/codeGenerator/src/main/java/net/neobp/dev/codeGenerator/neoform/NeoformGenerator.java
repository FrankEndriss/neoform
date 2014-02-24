package net.neobp.dev.codeGenerator.neoform;

import java.io.File;
import java.util.List;

import net.neobp.dev.codeGenerator.CodeGenerator;
import net.neobp.dev.codeGenerator.StrUtil;
import net.neobp.dev.codeGenerator.neoform.model.NeoformModel;

public abstract class NeoformGenerator implements CodeGenerator
{

    /** The model this CodeGenerator creates code for. */
    private NeoformModel neoformModel;

    /** Base output directory. */
    private File outputDir;

    /** Context for this CodeGenerator */
    private NeoformGeneratorContext context;

    public void setModel(NeoformModel neoformModel) {
        if(neoformModel==null)
            throw new IllegalArgumentException("neoformModel must not be null");

        this.neoformModel=neoformModel;
    }

    public NeoformModel getModel() {
        return neoformModel;
    }
    
    public void setOutputDir(final File outputDir) {
        if(outputDir==null)
            throw new IllegalArgumentException("outputDir must not be null");
        outputDir.mkdirs();
        this.outputDir=outputDir;
    }

    public File getOutputDir() {
        return outputDir;
    }
    
    public void setContext(NeoformGeneratorContext context) {
        this.context=context;
    }
    
    public NeoformGeneratorContext getContext() {
        return context;
    }

    /** Creates a "ready to write to" File from a classname, constructed
     * by getOutputDir() and the package part of the classname.
     * @param fullyQualifiedClassname
     * @return a File ready to write code into
     */
    protected File fileForClass(String fullyQualifiedClassname)
    {
        final List<String> path=StrUtil.getSplittedDotString(fullyQualifiedClassname);
    	String fName=path.get(path.size()-1);
    	path.remove(path.size()-1);
    
    	File packageDir=getOutputDir();
    	while(path.size()>0) {
    		packageDir=new File(packageDir, path.get(0));
    		path.remove(0);
    	}
    	System.out.println("creating dir: "+packageDir);
    	packageDir.mkdirs();
    	final File outputFile=new File(packageDir, fName+".java");
        return outputFile;
    }

    public static String getPackageName(final String fullyQualifiedClassname)
    {
    	List<String> path=StrUtil.getSplittedDotString(fullyQualifiedClassname);
    	path.remove(path.size()-1);
    	StringBuffer sb=new StringBuffer();
    	boolean first=true;
    	for(String p : path)  {
    		if(!first)
    			sb.append(".");
    		sb.append(p);
    		first=false;
    	}
    	return sb.toString();
    }

}