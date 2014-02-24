package net.neobp.neoform.widget;

import java.util.Collection;
import java.util.List;

import net.neobp.neoform.gui.AbstractNeoformComponent;
import net.neobp.neoform.value.ValueHolder;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;

public class NeoformTableViewer<C extends Collection<M>, M> extends AbstractNeoformComponent<C> {
    private final TableViewer tableViewer;
    private TableColDesc<M, ? extends ValueHolder<?>, ?>[] tableDesc;    // description of table
    private List<M> dataList;
    
    /** Description of a column in the table.
     */
    public interface TableColDesc<M, H extends ValueHolder<V>, V> {

        /** @return display string for table header */
        public String getHeader();

        /** Creates (and returns) the Control for a cell in the table.
         * @param parent the parent Composite
         * @return the newly created control
         */
        public H createControl(Composite parent);

        /** Should put the value from the model object model into the control created
         * by createControl().
         * @param model the model object
         */
        public void model2screen(M model, H controll);
    }

    public NeoformTableViewer(Composite parent) {
        tableViewer=new TableViewer(parent, 
                SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

        final Table table = tableViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true); 
        
        tableViewer.setContentProvider(ArrayContentProvider.getInstance());
        createColumns();
    }
    
    private void createColumns() {
        for(TableColDesc<M, ?, ?> col : tableDesc) {
            TableViewerColumn viewerCol=new TableViewerColumn(tableViewer, SWT.NONE);
            viewerCol.getColumn().setText(col.getHeader());
            
            CellLabelProvider labelProvider=new CellLabelProvider() {
                @Override
                public void update(ViewerCell cell) {
                    // sic..what to do here. Problem.
                }
                
            };
        }
    }

    @Override
    protected void value2widget(C value) {
        tableViewer.setInput(value);
        //dataList=new ArrayList<M>(value);
    }

    @Override
    public void setLayoutData(Object layoutData) {
        tableViewer.getTable().setLayoutData(layoutData);
    }

    @Override
    public Control getControl()
    {
        return tableViewer.getTable();
    }

}
