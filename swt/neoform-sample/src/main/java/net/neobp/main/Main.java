package net.neobp.main;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executors;

import net.neobp.neo.nms.forms.orderHeader.CommonContext;
import net.neobp.neo.nmw.neoforms.OrderDocumentTableContext;
import net.neobp.neo.nmw.neoforms.OrderDocumentTableFactory;
import net.neobp.neo.nmw.neoforms.OrderHeaderDialogBuilder;
import net.neobp.neo.nmw.neoforms.OrderHeaderDialogContext;
import net.neobp.neo.nmw.neoforms.OrderHeaderDialogForm;
import net.neobp.neoform.exec.NeoformExec;
import net.neobp.neoform.gui.MessageSource;
import net.neobp.neoform.swt.exec.SwtNeoformExec;
import net.neobp.neoform.swt.gui.AbstractNeoform;
import net.neobp.sampleModels.OrderDocumentViewTransfer;
import net.neobp.sampleModels.OrderHeaderViewTransfer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/** Startups the sample application
 */
public class Main {
    private static Shell shell;
    private static Display display;
    
    private static NeoformExec neoformExec;
    
    public static void main(final String[] args) {
        System.out.println("main args: "+Arrays.asList(args));
        
        display = new Display();
        shell = new Shell(display);
        
        shell.setLayout(new GridLayout(2, false));
        shell.setLocation(new Point(80, 80));

        final Text text = new Text(shell, SWT.READ_ONLY);
        text.setText("Hello World");
        final GridData helloWorldGridData=new GridData();
        helloWorldGridData.grabExcessHorizontalSpace=false;
        helloWorldGridData.grabExcessVerticalSpace=false;
        text.setLayoutData(helloWorldGridData);
        
        final Composite composite=new Composite(shell, SWT.NONE);
        final GridData compositeGridData=new GridData();
        compositeGridData.grabExcessHorizontalSpace=true;
        compositeGridData.grabExcessVerticalSpace=true;
        compositeGridData.verticalAlignment=SWT.FILL;
        compositeGridData.horizontalAlignment=SWT.FILL;
        composite.setLayoutData(compositeGridData);
        
        addAppContent(composite);

        //shell.pack();
        shell.open();

        while (!shell.isDisposed()) {
                display.sleep();
        }
        display.dispose();
    }
    
    /** Adds the application GUI to the empty shell
     * @param parent an emtpy shell for the application
     */
    private static void addAppContent(final Composite parent) {
        // create the global NeoformExec
        neoformExec=new SwtNeoformExec(display, Executors.newCachedThreadPool());

        // Builder to create the form object
        OrderHeaderDialogBuilder builder=new OrderHeaderDialogBuilder();

        // External object references for the form
        OrderHeaderDialogContext context=new OrderHeaderDialogContext();
        initCommonContext(context);
        initOrderHeaderDialogContext(context);
        
        // create the form object
        AbstractNeoform<?> form=builder.createForm(context);
        
        // initialize the gui components of the form
        form.createContents(parent);
        
        try {
            addSampleData((OrderHeaderDialogForm)form);
        }catch(Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        
    }

   /** Initializes the OrderHeaderDialogContext spezific fields of the context.
     * Should go into some generated code or the Context-Bridging subframework.
     * @param context
     */
    private static void initOrderHeaderDialogContext(OrderHeaderDialogContext context) {
        final OrderDocumentTableContext subContext=new OrderDocumentTableContext();
        initCommonContext(subContext);
        initOrderDocumentTableContext(subContext);
        context.setOrderDocumentTableContext(subContext);
    }
    
    /** Initializes the OrderDocumentTableContext spezific fields of the context.
     * Should go into some generated code or the Context-Bridging subframework.
     * @param context
     */
    private static void initOrderDocumentTableContext(final OrderDocumentTableContext context) {
        context.setOrderDocumentTableFactory(new OrderDocumentTableFactory());
    }

    /** Sample initialization of the CommonContext-fields of a context. Should go into
     * some kind of Context-Bridging subframework.
     * @param context
     */
    private static void initCommonContext(final CommonContext context) {
        context.setFormToolkit(new FormToolkit(display));
        context.setMessageSource(new MessageSource() {
            @Override
            public String getMessage(final String key) {
                return key;
            }
        });
        context.setNeoformExec(neoformExec);
    }
    
    /** Adds some sample data object to a OrderHeaderDialogForm.
     * Should be done by application logic.
     * @param form
     * @throws ParseException 
     */
    private static void addSampleData(final OrderHeaderDialogForm form) throws ParseException {
        final DateFormat dateFormat=new SimpleDateFormat("YYYY-MM-DD");

        final OrderHeaderViewTransfer ohvt=new OrderHeaderViewTransfer();
        ohvt.setAddressName("Andrea Mustermann");
        ohvt.setAddressStreet("Kirchgasse");
        ohvt.setClosed(false);
        ohvt.setCode("4711");
        ohvt.setEndDate(dateFormat.parse("2014-12-31"));
        ohvt.setEquipment("123-321-848484848");
        ohvt.setGuid("0xABCDabcd12342323");
        ohvt.setOrderId("3290002348");
        ohvt.setStartDate(new Date());
        ohvt.setWorkTime(new BigDecimal("32.481"));
        
        final OrderDocumentViewTransfer odvt1=new OrderDocumentViewTransfer();
        odvt1.setCreateUser("test_user");
        odvt1.setDateModified(dateFormat.parse("2014-04-12"));
        odvt1.setDescription("Neoform spezification");
        odvt1.setDirPath("C:\\Users\\fendriss\\tmp");
        odvt1.setFileName("000000123.txt");
        odvt1.setGuid("0xFFFFAAAA1234abab");
        
        final OrderDocumentViewTransfer odvt2=new OrderDocumentViewTransfer();
        odvt2.setCreateUser("other_user");
        odvt2.setDateModified(dateFormat.parse("2014-04-17"));
        odvt2.setDescription("Neoform examples");
        odvt2.setDirPath("C:\\Users\\fendriss\\tmp");
        odvt2.setFileName("000000124.txt");
        odvt2.setGuid("0xFFFFAAAA1234cbcb");
        
        ohvt.addOrderDocument(odvt1);
        ohvt.addOrderDocument(odvt2);
        
        form.setModel(ohvt, false);
    }

 
}
