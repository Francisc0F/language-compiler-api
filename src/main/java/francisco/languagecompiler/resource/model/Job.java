package francisco.languagecompiler.resource.model;

import com.google.protobuf.FieldMask;
import francisco.languagecompiler.resource.util.ResponseMaker;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

import static francisco.languagecompiler.resource.util.FieldMaskMapper.validateFieldMask;

public class Job extends BaseResource implements ResponseMaker {

    @Getter
    @Setter
    private String name;

    public Job(String id, String name, String code) {
        super(id);
        this.name = name;
    }

    public Job(String id, String name) {
        super(id);
        this.name = name;
    }

    public Job() {
        super();
    }

    public Map<String, Object> toMap(FieldMask fm) {
        return validateFieldMask(this, fm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
