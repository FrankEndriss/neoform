package net.neobp.dev.codeGenerator.neoform;

import net.neobp.dev.codeGenerator.NeoformModelLoader;
import net.neobp.dev.codeGenerator.SpringCallback;

/** Context for NeoformGenerators. Provides ClassLoader and Spring-Bean loader and Neoform loader.
 */
public class NeoformGeneratorContext {
    private ClassLoader appClassLoader;
    private SpringCallback springCallback;
    private NeoformModelLoader neoformCallback;

    /**
     * @return ClassLoader based on the applications classpath
     */
    public ClassLoader getAppClassLoader()
    {
        return appClassLoader;
    }

    public void setAppClassLoader(ClassLoader appClassLoader)
    {
        this.appClassLoader = appClassLoader;
    }

    /**
     * @return Spring ApplicationContext based on getAppClassLoader()
     */
    public SpringCallback getSpringCallback()
    {
        return springCallback;
    }

    public void setSpringCallback(SpringCallback springCallback)
    {
        this.springCallback = springCallback;
    }

    /**
     * @return callback to resolve NeoformModels.
     */
    public NeoformModelLoader getNeoformCallback()
    {
        return neoformCallback;
    }

    public void setNeoformCallback(NeoformModelLoader neoformCallback)
    {
        this.neoformCallback = neoformCallback;
    }
    
}
