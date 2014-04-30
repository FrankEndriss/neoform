package net.neobp.main;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/** Startups the sample application
 */
public class Main {
    public static void main(final String[] args) {
        System.out.println("main args: "+Arrays.asList(args));
        
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setLayout(new FillLayout());
        shell.setLocation(new Point(80, 80));
        Text text = new Text(shell, SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}
