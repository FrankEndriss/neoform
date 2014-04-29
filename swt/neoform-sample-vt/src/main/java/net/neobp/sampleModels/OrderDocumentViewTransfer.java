package net.neobp.sampleModels;

import java.util.Date;

/** Sample class with data fields which should be editable in a Neoform application.
 */
public class OrderDocumentViewTransfer {
    
    private String fileName;
    private String dirPath;
    private Date dateModified;
    private String guid;
    private String createUser;
    private String description;

    public String getDirPath()
    {
        return dirPath;
    }
    public void setDirPath(String dirPath)
    {
        this.dirPath = dirPath;
    }
    public Date getDateModified()
    {
        return dateModified;
    }
    public void setDateModified(Date dateModified)
    {
        this.dateModified = dateModified;
    }
    public String getGuid()
    {
        return guid;
    }
    public void setGuid(String guid)
    {
        this.guid = guid;
    }
    public String getCreateUser()
    {
        return createUser;
    }
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getFileName()
    {
        return fileName;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

}
