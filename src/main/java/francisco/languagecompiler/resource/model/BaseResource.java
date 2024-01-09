package francisco.languagecompiler.resource.model;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.util.ResponseMaker;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static francisco.languagecompiler.resource.util.FieldMaskMapper.validateFieldMask;


public abstract class BaseResource implements ResponseMaker {

    @Getter
    @Setter
    public String id;
    private Date createdDate;
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

    @Override
    public String toString() {
        return "BaseResource{" +
                "createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Build build = (Build) o;
        return Objects.equals(id, build.getId());
    }


    @Override
    public Map<String, Object> toMap(FieldMask fieldMask) {
        return validateFieldMask(this, fieldMask);
    }
}
