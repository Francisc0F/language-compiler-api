package francisco.languagecompiler.resource.service;

import francisco.languagecompiler.resource.model.BaseResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class BaseService<T extends BaseResource> {
    protected List<T> items = new ArrayList<>();

    public Stream<T> getStream() {
        return items.stream();
    }

    public T get(String id) {
        return items.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }

    public void removeById(String id) {
        items.removeIf(item -> Objects.equals(item.getId(), id));
    }

    public T add(T item) {
        items.add(item);
        return item;
    }
}
