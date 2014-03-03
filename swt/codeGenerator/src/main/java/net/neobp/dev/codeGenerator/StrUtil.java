package net.neobp.dev.codeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StrUtil
{
    public final static String firstUp(String s) {
        return s.substring(0, 1).toUpperCase()+s.substring(1, s.length());
    }

    public final static String firstDown(String s) {
        return s.substring(0, 1).toLowerCase()+s.substring(1, s.length());
    }

    /** Converts dotted notation into camel case
     * @param prop the dotted property name
     * @return the camel case name
     */
    public static String camelCase(final String prop) {
    	String ret="";
    	boolean first=true;
    	for(String s : getSplittedDotString(prop)) {
    		if(first) {
    			ret+=s;
    			first=false;
    		}else
    			ret+=firstUp(s);
    	}
    
    	return ret;
    }

    /** @param a String with dots
     * @return List of Strings, dotted splitted at the dots
     */
    public static List<String> getSplittedDotString(String dotted)
    {
    	List<String> path=new ArrayList<String>();
    	path.addAll(Arrays.asList(dotted.split("\\.")));
    	return path;
    }
}
