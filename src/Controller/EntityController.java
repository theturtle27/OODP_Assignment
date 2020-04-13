package Controller;

import java.util.Arrays;
import java.util.List;
import Persistence.Persistence;
import View.View;

public abstract class EntityController<T> extends PersistenceController {

    public EntityController(Persistence persistence) {
        super(persistence);
    }

    @Override
    public List<String> getOptions() {
        return Arrays.asList(
                "Create " + this.getEntityName().toLowerCase(),
                "Retrieve/Search " + this.getEntityName().toLowerCase(),
                "Update " + this.getEntityName().toLowerCase(),
                "Delete " + this.getEntityName().toLowerCase(),
                "Show" + this.getEntityName().toLowerCase()
        );
    }

    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception{
        switch(option) {
            case 0:
                create(view);
                break;
            case 1:
                retrieve(view);
                break;
            case 2:
                update(view);
                break;
            case 3:
                delete(view);
                break;
            case 4:
                show(view);
                break;
        }
    }


    protected abstract String getEntityName();


    protected abstract void create(View view) throws Exception;


    protected abstract boolean retrieve(View view) throws Exception;


    protected abstract void update(View view) throws Exception;


    protected abstract void delete(View view) throws Exception;

    protected abstract void show(View view) throws Exception;


    public abstract T select(View view) throws Exception;

}