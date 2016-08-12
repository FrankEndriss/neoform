package net.neobp.sampleModels;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/** Sample domain class of an entity called "OrderHeader"
 */
public class OrderHeaderViewTransfer {
    private String guid;
    private String orderId;
    private String code;
    private Date startDate;
    private Date endDate;
    private BigDecimal workTime;

    private String addressName;
    private String addressStreet;
    private String equipment;
    private boolean closed;

    private Collection<OrderDocumentViewTransfer> orderDocuments=new ArrayList<OrderDocumentViewTransfer>();
    
    public String getGuid()
    {
        return guid;
    }
    public void setGuid(String guid)
    {
        this.guid = guid;
    }
    public String getOrderId()
    {
        return orderId;
    }
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public Date getStartDate()
    {
        return startDate;
    }
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }
    public Date getEndDate()
    {
        return endDate;
    }
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }
    public BigDecimal getWorkTime()
    {
        return workTime;
    }
    public void setWorkTime(BigDecimal workTime)
    {
        this.workTime = workTime;
    }
    public String getAddressName()
    {
        return addressName;
    }
    public void setAddressName(String addressName)
    {
        this.addressName = addressName;
    }
    public String getAddressStreet()
    {
        return addressStreet;
    }
    public void setAddressStreet(String addressStreet)
    {
        this.addressStreet = addressStreet;
    }
    public String getEquipment()
    {
        return equipment;
    }
    public void setEquipment(String equipment)
    {
        this.equipment = equipment;
    }
    public boolean isClosed()
    {
        return closed;
    }
    public void setClosed(boolean closed)
    {
        this.closed = closed;
    }
    public Collection<OrderDocumentViewTransfer> getOrderDocuments() {
        return orderDocuments;
    }
    
    public void addOrderDocument(final OrderDocumentViewTransfer orderDocumentViewTransfer) {
        orderDocuments.add(orderDocumentViewTransfer);
    }

}
