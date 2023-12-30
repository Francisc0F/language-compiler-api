package francisco.languagecompiler.resource.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;


public class BaseResource {

    protected final String id;
    private final Date createdDate;
    private Date updatedDate;

    public BaseResource() {
        this.id = UUID.randomUUID().toString();
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    public BaseResource(String id) {
        this.id = UUID.fromString(id).toString();
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    // Override toString() method
    @Override
    public String toString() {
        return "BaseResource{" +
                "createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    // Override equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Build build = (Build) o;
        return Objects.equals(id, build.getId());
    }


    public String getId() {
        return id;
    }

}
