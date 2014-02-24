package net.neobp.dev.codeGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Helper class for generating java code.
 */
public class JavaCodeTemplate {

	private String packageName;
	private final Set<String> imports=new HashSet<String>();
	private String className;
	private String extendsName;
	private final Set<String> interfaces=new HashSet<String>();
	private final List<String> propertyDefs=new ArrayList<String>();
	private final List<List<String>> methods=new ArrayList<List<String>>();
	private final List<List<String>> otherCode=new ArrayList<List<String>>();
	
	public void setPackageName(final String packageName) {
		this.packageName=packageName;
	}

	public void setClassName(final String name) {
		this.className=name;
	}
	
	public void setExtends(final String name) {
		this.extendsName=name;
	}

	public void addImplements(final String interfaceName) {
		interfaces.add(interfaceName);
	}

	public void addImport(final String packageName) {
		imports.add(packageName);
	}
	
	public void addMethod(final List<String> code) {
		methods.add(code);
	}
	
	public void addPropertyDef(final String code) {
		propertyDefs.add(code);
	}
	
	public void addOtherCode(final List<String> code) {
	    otherCode.add(code);
	}

	@Override
	public String toString() {
		StringBuilder ret=new StringBuilder("");
		ret.append("package "+packageName+";\n\n");
		
		final List<String> lImports=new ArrayList<String>(imports);
		Collections.sort(lImports);
		for(String s : lImports)
			ret.append("import "+s+";\n");
			
		ret.append("\n");
		
		ret.append("public class "+className);
		if(extendsName!=null)
			ret.append(" extends "+extendsName);

		if(interfaces.size()>0) {
			ret.append(" implements ");
			boolean first=true;
			for(String s : interfaces) {
				if(!first) {
					ret.append(", ");
				}
				first=false;
				ret.append(s);
			}
		}
		ret.append(" {\n");
			
		for(String s : propertyDefs) 
			ret.append("\t"+s+";\n");
		
		for(List<String> method : methods) {
			ret.append("\n");
			for(String line : method) {
				ret.append("\t");
				ret.append(line);
				ret.append("\n");
			}
		}
			
		for(List<String> oC : otherCode) {
			ret.append("\n");
			for(String line : oC) {
				ret.append("\t");
				ret.append(line);
				ret.append("\n");
			}
		}
			
		ret.append("} // end class \n");
		return ret.toString();
	}
}
