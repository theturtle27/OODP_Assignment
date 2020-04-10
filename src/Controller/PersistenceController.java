package Controller;

import Persistence.Persistence;

import java.util.List;

public abstract class PersistenceController implements Controller{

    Persistence persistence;

    public PersistenceController(Persistence persistence) {
        this.persistence = persistence;
    }

    public abstract void onOptionSelected();

    @Override
    public List<String> getOptions() {
        return null;
    }

    
}
