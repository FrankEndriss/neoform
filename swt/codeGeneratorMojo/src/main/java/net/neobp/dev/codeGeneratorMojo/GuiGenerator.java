package net.neobp.dev.codeGeneratorMojo;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.neobp.dev.codeGenerator.NeoformModelLoader;
import net.neobp.dev.codeGenerator.SpringCallback;
import net.neobp.dev.codeGenerator.neoform.NeoformBuilderGenerator;
import net.neobp.dev.codeGenerator.neoform.NeoformComponentGenerator;
import net.neobp.dev.codeGenerator.neoform.NeoformContextGenerator;
import net.neobp.dev.codeGenerator.neoform.NeoformControllerGenerator;
import net.neobp.dev.codeGenerator.neoform.NeoformDocumentHandler;
import net.neobp.dev.codeGenerator.neoform.NeoformDocumentHandler.ResultHandler;
import net.neobp.dev.codeGenerator.neoform.NeoformFactoryGenerator;
import net.neobp.dev.codeGenerator.neoform.NeoformFormGenerator;
import net.neobp.dev.codeGenerator.neoform.NeoformGenerator;
import net.neobp.dev.codeGenerator.neoform.NeoformGeneratorContext;
import net.neobp.dev.codeGenerator.neoform.NeoformIPageWrapperGenerator;
import net.neobp.dev.codeGenerator.neoform.NeoformLayoutGenerator;
import net.neobp.dev.codeGenerator.neoform.SwtEditorSpringGenerator;
import net.neobp.dev.codeGenerator.neoform.model.NeoformModel;
import net.neobp.dev.codeGenerator.neoform.model.NeoformModelImpl;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
/*
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.convert.ConversionService;
*/
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;

/**
 * Goal which which generates java gui classes for Neoform.
 * 
 * @goal generateNeoforms
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class GuiGenerator extends AbstractMojo {
	
	
    // TODO: make configurable in client pom.xml
    private File outputDirectory;
    
    private File springOutputDir;
    
    // TODO: make configurable in client pom.xml
    private File inputDirectory;

    /**
     * @parameter expression="${project}"
     * @required
     */
    private MavenProject project;
    
    public void execute()
        throws MojoExecutionException
    {
    	try {
    	    outputDirectory=new File(project.getBuild().getDirectory(), "generated/neoform/java");
    	    springOutputDir=new File(project.getBuild().getDirectory(), "generated/neoform/spring");
    	    inputDirectory=new File(project.getBasedir(), "src/main/resources/neoforms");
    		doGeneration();

    		// add path to generated java-sources to compilepath. 
    		// TODO make configurable if set or not, default true
    		project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
    		getLog().debug("Adding compile source root: "+outputDirectory.getAbsolutePath());
    		//throw new RuntimeException("aborded after adding source root");
    		
    		// Add generated spring to Resources
    		Resource resource=new Resource();
    		resource.setDirectory(springOutputDir.getAbsolutePath());
    		ArrayList<String> patterns=new ArrayList<String>();
    		patterns.add("**/*.xml");
    		resource.setIncludes(patterns);
    		resource.setFiltering(false);
    		project.getBuild().addResource(resource);

    	}catch(Exception e) {
    		throw new MojoExecutionException("Exception while parsing/generating", e);
    	}
    }
    
    private void doGeneration() throws Exception {
    	getLog().info("inputDirectory="+inputDirectory);
    	final File[] inputFiles=inputDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".neoform");
			}
    	});
    	
    	if(inputFiles==null || inputFiles.length==0)
    	    throw new MojoExecutionException("no .neoform-Files found in: "+inputDirectory);
    	

    	boolean qClosed=false;
        final BlockingQueue<NeoformModel> formQ=new ArrayBlockingQueue<NeoformModel>(16, true);

        final ResultHandler resultHandler=new ResultHandler() {
			public void finishedParsingNeoform(NeoformModel neoformModel) {
				formQ.add(neoformModel);
			}
        };
        
        final SAXParserFactory spf = SAXParserFactory.newInstance(); 
        getLog().info("input files: "+Arrays.asList(inputFiles));
        
       	final ClassLoader userClassLoader=createClassLoader();
    	for(final File f : inputFiles) {
    		SAXParser sp = spf.newSAXParser(); 
    		XMLReader xr = sp.getXMLReader(); 
    		ContentHandler neoformHandler=new NeoformDocumentHandler(resultHandler, userClassLoader);
    		xr.setContentHandler(neoformHandler);
    		// TODO: asynchron
    		xr.parse(f.getAbsolutePath()); 
    	}
    	qClosed=true;
    	//formQ.interrupt(); // formQ should be reengenered to Job/ThreadPool/Worker
    	
    	NeoformGeneratorContext generatorContext=new NeoformGeneratorContext();
    	generatorContext.setAppClassLoader(appClassLoader);
    	generatorContext.setSpringCallback(springCallback);
    	
    	final Map<String, NeoformModel> createdNeoformModels=new HashMap<String, NeoformModel>();
    	
    	generatorContext.setNeoformCallback(new NeoformModelLoader() {
            public NeoformModel getNeoformModel(String neoformName)
            {
                return createdNeoformModels.get(neoformName);
            }
            // TODO add wait/notify support, so that the above method does not
            // return null, but waits until the form is available.
    	    
    	});

       	NeoformModel neoformModel=null;
       	boolean breaked;
        while(!qClosed || formQ.size()>0) {
        	neoformModel=formQ.poll();
        	breaked=false;
        	
        	// check if all subforms are resolved. if not, push this
        	// form back on the top of the queue for later treatment.
        	// if there are no cyclic references, this should end at
        	// some point of time.
        	for(String subform : neoformModel.getSubforms())
        	    if(generatorContext.getNeoformCallback().getNeoformModel(subform)==null) {
        	        formQ.add(neoformModel); // pushback this form onto the formQ for later treatment
        	        // could make an endless loop :/
        	        breaked=true;
        	        break;
        	    }
        	
        	if(breaked)
        	    continue;
        	
        	// mark the form as resolved
        	createdNeoformModels.put(neoformModel.getFormName(), neoformModel);
        	
        	// now, that the subforms are resolved, add context fields for all subforms contexts
        	// to the NeoformModel's context.
        	for(String subform : neoformModel.getSubforms()) {
        	    NeoformModel subformModel=generatorContext.getNeoformCallback().getNeoformModel(subform);
        	    if(subformModel==null)
        	        throw new RuntimeException("unresolved subform... :/");
        	    neoformModel.addContextRef(
        	            subformModel.getContextClassnameTemplate().getGenClassname().getSimpleName(),
        	            subformModel.getContextClassnameTemplate().getSpringName(),
        	            subformModel.getContextClassnameTemplate().getGenClassname());
        	}
        	
        	// and add the factory of neoformModel to the contextFields of neoformModel
        	neoformModel.addFactory2ContextFields();

        	// TODO find Generator-Classes by neoformModel.getFormType()
        	// and/or Mojo parameters
        	
        	final NeoformGenerator[] generators=new NeoformGenerator[] {
        	        new SwtEditorSpringGenerator(),
        	        new NeoformFormGenerator(),
        	        new NeoformControllerGenerator(),
        	        new NeoformContextGenerator(),
        	        new NeoformComponentGenerator(),
        	        new NeoformIPageWrapperGenerator(),
        	        new NeoformLayoutGenerator(),
        	        new NeoformFactoryGenerator(),
        	        new NeoformBuilderGenerator()
        	};
        	
        	for(NeoformGenerator generator : generators) {
        	    // set the outputDir to the springOutputDir in the SwtEditoSpringGenerator
        	    if(generator instanceof SwtEditorSpringGenerator)
        	        generator.setOutputDir(springOutputDir);
        	    else
        	        generator.setOutputDir(outputDirectory);
        	    generator.setContext(generatorContext);
        	    generator.setModel(neoformModel);
        	    generator.generate();
        	    /* better do not refresh, does not work properly, and needs a lot of time!
        	    if(generator instanceof SwtEditorSpringGenerator)
        	        springCallback.refresh();
        	    */
        	}
        }
    }
    
    /** This creates the ClassLoader to load the model class(es).
     * @return ClassLoader to load referenced model classes from projects classpath
     * @throws MalformedURLException
     * @throws DependencyResolutionRequiredException
     */
    private ClassLoader createClassLoader() throws MalformedURLException, DependencyResolutionRequiredException {
    	// TODO: at least get classpath to search for the class, better compile them by starting 
    	// a sub compile... nasty :/
        
        final List<String> list=project.getCompileClasspathElements();
   	    final List<URL> urlList=new ArrayList<URL>();

    	getLog().info("Classpath for loading model classes:");
    	for(int i=0; i<list.size(); i++) {
    		final File f=new File(""+list.get(i));
    		final URL url=f.toURI().toURL();
    		urlList.add(url);
    	    getLog().info(""+url);
    	}

    	getLog().info("end Classpath");
    	appClassLoader=new URLClassLoader(urlList.toArray(new URL[0]), getClass().getClassLoader());
    	springCallback=createSpringCallback();
    	return appClassLoader;
    }
    
    private ClassLoader appClassLoader;
    
    private SpringCallback springCallback;
    
    private SpringCallback createSpringCallback() {
	return null;
/*
        return new SpringCallback() {

            final ClassPathXmlApplicationContext springApplicationContext=createSpringContext();

            public void refresh()
            {
                URL url;
                try {
                    url = springOutputDir.toURI().toURL();
                    URLClassLoader newLoader=new URLClassLoader(new URL[] { url }, appClassLoader);
                    appClassLoader=newLoader;
                    springApplicationContext.setClassLoader(newLoader);
                    springApplicationContext.refresh();
                } catch(MalformedURLException e) {
                    e.printStackTrace();
                }
            }
    
            public synchronized Class<?> getType(String springBean) {
                System.out.println("SpringContextThread.getType(): "+springBean);
                return springApplicationContext.getType(springBean);
            }
        };
*/
    }
    

/*
    private ClassPathXmlApplicationContext createSpringContext()
    {
                // Loading of applications spring ApplicationContext.
                // This is a pain in ass, because the standard initialization tries to
                // instantiate several beans...what end in errors because no osgiBundle
                // is available here at compile time.
                // So, some methods of ClassPathCmlApplicationContext are replaced here
                // by overriding them. Not sure why, but it seems to work.
        final ClassPathXmlApplicationContext springApplicationContext=
               new ClassPathXmlApplicationContext( new String[] {
                       "classpath*:META-INF/spring/**.xml",
                       "classpath*:META-INF/spring-override/**.xml",
                       "/META-INF/spring/**.xml"
                }, false) {
            protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
                if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
                        beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
                     beanFactory.setConversionService(
                                beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
                    }

                    // Stop using the temporary ClassLoader for type matching.
                    beanFactory.setTempClassLoader(null);

                    // Allow for caching all bean definition metadata, not expecting further changes.
                    beanFactory.freezeConfiguration();

                    getLog().info("dont finishBeanFactoryInitialisation(), beanFactory="+beanFactory);
                }
                protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
                    getLog().info("doing not invokeBeanFactroyPostProcessors(), beanFactory: "+beanFactory);
                }
                protected void finishRefresh() {
                    getLog().info("doing not finishRefresh()");
                }
            };
            springApplicationContext.setClassLoader(appClassLoader);
            springApplicationContext.refresh();
            return springApplicationContext;
        }
*/
}
