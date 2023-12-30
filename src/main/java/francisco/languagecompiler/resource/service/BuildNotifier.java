package francisco.languagecompiler.resource.service;



import francisco.languagecompiler.resource.model.Build;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BuildNotifier {
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addObserver(PropertyChangeListener l) {
        pcs.addPropertyChangeListener("run", l);
    }

    public void notify(String val) {
        pcs.firePropertyChange("run", null, val);
    }

    public void addObserverComplete(PropertyChangeListener l) {
        pcs.addPropertyChangeListener("complete", l);
    }

    public void notifyComplete(Build val) {
        pcs.firePropertyChange("complete", null, val);
    }
}
