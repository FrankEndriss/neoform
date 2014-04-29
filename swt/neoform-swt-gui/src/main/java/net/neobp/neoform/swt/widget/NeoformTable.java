package net.neobp.neoform.swt.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.neobp.neoform.exec.NeoformExec;
import net.neobp.neoform.swt.gui.AbstractSwtNeoformComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/** Table implementation capable of using a Neoform for displaying the row data.
 * Implemented on top of SWT Table.
 * What makes it a bit tricky is that SWT Tables are organized per column, while
 * Neoforms are organized as a row. See the code, you will stumble over it ;)
 * TODO add support for adding/removing rows of the table data
 * @param C the Collection<M>
 * @param M the model object class (one such object is displayed in one row of the table)
 */
public class NeoformTable<C extends Collection<M>, M> extends AbstractSwtNeoformComponent<C> {
    private final Table table;
    private final List<TableColDesc<M>> tableDesc;    // description/handler of table
    private List<M> dataList;
    
    /** Description/Handler of a column in the table.
     */
    public interface TableColDesc<M> {

        /** @return display string for table header */
        public String getHeader();

        /** Creates the Control for a cell in the table.
         * @param parent the parent Composite
         * @return the newly created Control
         */
        public Control createControl(Composite parent, int idx, M model);

        /** Should put the value from the model object model into the control created
         * by createControl().
         * @param model the model object
         * @param idx the idx of the Control created by createControl()
         */
        public void model2screen(M model, int idx);
    }

    /** Creator of the NeoformTable
     * @param parent the parent Component where the table is shown.
     * @param tableDesc the description/handler/configuration of the table. (usually created by a generated Factory class)
     */
    public NeoformTable(Composite parent, List<TableColDesc<M>> tableDesc, NeoformExec neoformExec) {
        super(neoformExec);
        this.tableDesc=tableDesc;
        table=new Table(parent, SWT.VIRTUAL | SWT.BORDER);
        table.addListener(SWT.SetData, new Listener () {
            public void handleEvent (Event event) {
                TableItem item = (TableItem) event.item;
                int index = table.indexOf(item);
                try {
                    fillItem(item, index);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }); 
    }
    
    /** This puts the data of one row onto the screen.
     * @param item the TableItem created by the table
     * @param idx index of the item and the data
     */
    private void fillItem(TableItem item, int idx) {
        int i=0;
        for(TableColDesc<M> col : tableDesc) {
            // How to find if the editor for item was created in a previous call?
            // May be this method is called only once per item. Would be inefficient, though.
            final TableEditor editor=new TableEditor(table);
            final M model=dataList.get(idx);
            final Control control=col.createControl(table, idx, model);
            editor.setEditor(control, item, i++);
            //col.model2screen(model, idx);
        }
    }

    @Override
    protected void value2widget(C values) {
        table.clearAll();
        // put the data into List form
        // TODO: make this async
        dataList=new ArrayList<M>(values);
        table.setItemCount(values.size());
    }

    @Override
    protected void layout2widget(Object layoutData) {
        table.setLayoutData(layoutData);
    }

    @Override
    public Control getControl() {
        return table;
    }

}
