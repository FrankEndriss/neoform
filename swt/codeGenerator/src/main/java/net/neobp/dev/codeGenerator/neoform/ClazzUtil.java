package net.neobp.dev.codeGenerator.neoform;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import net.neobp.dev.codeGenerator.neoform.model.NeoformProperty;

/** Utility class for reflection stuff, most methods return a code snippet String.
 */
public class ClazzUtil {

    /** Returns a programmatic string representation of the type of the property of class clazz.
     * @param clazz the Class<?> to search for the property
     * @param prop the name of the property
     * @param imports list to fill with the fully qualified classnames of generic types of clazz (i.e. the classes which must be imported to compile the returned code snippet)
     * @return something like <pre>"Collection<Map<String, Integer>>"</pre> or whatever type prop is, and imports
     * would be filled with [ "java.util.Map", "java.lang.String", "java.lang.Integer" ]
     * @throws IntrospectionException
     */
    public static String getJavaType(Class<?> clazz, NeoformProperty prop, List<String> imports) throws IntrospectionException {
        // special name "this" to reference the model object itself
        if("this".equals(prop.getModelPropertyName()))
            return clazz.getName();
        
        final BeanInfo info=Introspector.getBeanInfo(clazz);
        final PropertyDescriptor[] descriptors=info.getPropertyDescriptors();
        for(PropertyDescriptor pd : descriptors) {
            if(pd.getName().equals(prop.getModelPropertyName())) {
                Method m=pd.getReadMethod();
                // Class<?> pType=pd.getPropertyType();
                // Note: we cant use getPropertyType(), because we need the generic Type (parameterized)
                Type pType=m.getGenericReturnType(); // return type of getter is type of property
                if(pType instanceof Class) {
                    if(((Class)pType).isPrimitive())
                        pType=mapPrimitiveToObjectClass((Class)pType);
                }
                return makeTypeString(pType, "", imports);
            }
        }
        throw new IllegalStateException("property "+prop.getName()+" notfound in "+clazz.getName());
    }
    
    /** Makes a declarative type string of type, ie. "Collection<Map<String, Integer>" or whatever type is.
     * @param type to inspect
     * @param typeString prefix for the created type string, this method calls itself recursive for nested types
     * @param imports this list is filled whith the fully qualified classnames of the classnames contained in
     * the generated type String. These fully qualified classnames can be used as imports while compiling the
     * generated type String.
     * @return a declarative type string
     */
    private static String makeTypeString(Type type, String typeString, List<String> imports) {
        //System.out.println("makeTypeString: "+type+" encString: "+typeString+" typeClass: "+type.getClass());

        if(type instanceof Class) {
            imports.add(((Class)type).getName());
            return ((Class)type).getSimpleName();
        } else if(type instanceof ParameterizedType) {
            ParameterizedType pt=(ParameterizedType)type;

            Type rawType=pt.getRawType();
            if(rawType instanceof Class) {
                Class<?> clsOfRawType=((Class)rawType);
                imports.add(clsOfRawType.getName());
                typeString+=clsOfRawType.getSimpleName();
            } else
                throw new RuntimeException("raw type not a class, unresolved: "+rawType);

            Type[] params=pt.getActualTypeArguments();
            typeString+="<";
            int i=0;
            for(Type p : params) {
                if(i>0)
                    typeString+=", ";
                i++;
                typeString+=makeTypeString(p, typeString, imports);
            }
            typeString+=">";
        } else 
            throw new RuntimeException("unresolved type: "+type);

        return typeString;
    }
    
    /**
     * @param primClass a Class<?> object of a primitive type
     * @return the Class<?> object of the corresponding class type. i.e. Class<int> -> Class<Integer>
     */
    private static Class<?> mapPrimitiveToObjectClass(Class<?> primClass) {
        if(primClass==Boolean.TYPE)
            return Boolean.class;
        else if(primClass==Short.TYPE)
            return Short.class;
        else if(primClass==Character.TYPE)
            return Character.class;
        else if(primClass==Integer.TYPE)
            return Integer.class;
        else if(primClass==Long.TYPE)
            return Long.class;
        else
            throw new IllegalArgumentException("unknown primitive type: "+primClass);
    }

    /** Finds a method by introspection
     * @param clazz the class to search the method within
     * @param prop the dotted property name
     * @param getGetter if true the getter is searched, if false the setter
     * @return the found Method or null
     * @throws IntrospectionException if the class cannot be loaded or introspected
     */
    private static Method getPropMethod(Class<?> clazz, String prop, boolean getGetter) throws IntrospectionException {
    	final BeanInfo info=Introspector.getBeanInfo(clazz);
    	final PropertyDescriptor[] descriptors=info.getPropertyDescriptors();
    	for(PropertyDescriptor pd : descriptors) {
    		if(pd.getName().equals(prop))
    			if(getGetter)
    				return pd.getReadMethod();
    			else
    				return pd.getWriteMethod();
    	}
    	throw new IllegalStateException("searching "+(getGetter?"getter":"setter")+" on "+clazz.getName()+" failed, prop="+prop);
    }

    public static Method getSetterMethod(Class<?> clazz, String prop) throws IntrospectionException {
    	return getPropMethod(clazz, prop, false);
    }

    public static Method getGetterMethod(Class<?> clazz, String prop) throws IntrospectionException {
    	return getPropMethod(clazz, prop, true);
    }

}
