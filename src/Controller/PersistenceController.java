package Controller;

import Persistence.Persistence;
import View.View;

import java.util.List;

public abstract class PersistenceController implements Controller {
    private final Persistence persistence;

    public PersistenceController(Persistence persistence) {
        this.persistence = persistence;
    }

    protected Persistence getPersistenceImpl() {
        return this.persistence;
    }

    protected abstract void safeOnOptionSelected(View view, int option) throws Exception;

    @Override
    public final void onOptionSelected(View view, int option) {
        try {
            this.safeOnOptionSelected(view, option);
        } catch(Exception e) {
            view.message("An error occurred while performing file operations, please try again later.");
            e.printStackTrace();
        }
    }

}