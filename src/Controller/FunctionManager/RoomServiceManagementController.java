package Controller.FunctionManager;

import Controller.PersistenceController;
import Persistence.Persistence;
import View.View;

import java.util.Arrays;
import java.util.List;

public class RoomServiceManagementController extends PersistenceController {
    public RoomServiceManagementController(Persistence persistence) {
        super(persistence);
    }

    protected String getEntityName(){return "Room Service Order";}

    @Override
    public List<String> getOptions() {
        return Arrays.asList(
                "Add " + this.getEntityName().toLowerCase(),
                "Update " + this.getEntityName().toLowerCase() + " status",
                "Update " + this.getEntityName().toLowerCase(),
                "Delete " + this.getEntityName().toLowerCase()
        );
    }


    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception {
        switch (option) {
            case 0:
                addRoomServiceOrder(view);
                break;
            case 1:
                updateRoomServiceOrderStatus(view);
                break;
            case 2:
                updateRoomServiceOrder(view);
                break;
            case 3:
                deleteRoomServiceOrder(view);
                break;
        }
    }

    public void addRoomServiceOrder(View view){

    }

    public void updateRoomServiceOrderStatus(View view){

    }

    public void updateRoomServiceOrder(View view){

    }

    public void deleteRoomServiceOrder(View view){

    }
}
