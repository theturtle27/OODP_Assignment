package Controller.FunctionManager;

import Controller.EntityManager.MenuController;
import Controller.EntityManager.RoomController;
import Controller.PersistenceController;
import Model.Menu.MenuItem;
import Model.Room.Room;
import Model.RoomServiceOrder.OrderItem;
import Model.RoomServiceOrder.RoomServiceOrder;
import Model.RoomServiceOrder.RoomServiceOrderStatus;
import Model.Stay.Stay;
import Persistence.Persistence;
import View.View;
import View.Options;
import Persistence.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomServiceManagementController extends PersistenceController {
    private final RoomController roomController;
    private final MenuController menuController;
    private final StayController stayController;

    public RoomServiceManagementController(Persistence persistence, RoomController roomController, MenuController menuController,StayController stayController) {
        super(persistence);
        this.roomController = roomController;
        this.menuController = menuController;
        this.stayController = stayController;
    }

    protected String getEntityName(){return "Room Service Order";}

    @Override
    public List<String> getOptions() {
        return Arrays.asList(
                "Add " + this.getEntityName().toLowerCase(),
                "Update " + this.getEntityName().toLowerCase() + " status",
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
                removeRoomServiceOrder(view);
                break;
        }
    }

    public void addRoomServiceOrder(View view) throws Exception{
        Room room = roomController.select(view);
        RoomServiceOrder roomServiceOrder = new RoomServiceOrder(room.getRoomNumber());
        Persistence persistence = getPersistenceImpl();
        ArrayList<Entity> items = persistence.retrieveAll(MenuItem.class);
        //display the options for the room service order
        view.display(items);

        MenuItem menuItem = menuController.select(view);

        OrderItem orderItem = new OrderItem(menuItem,"");
        roomServiceOrder.addOrderItem(orderItem);

        Stay stay = (Stay)stayController.getStayByRoomNumber(room.getRoomNumber());
        stay.addRoomSerciceOrder(roomServiceOrder);

        view.display("Order Successful");


    }

    public void updateRoomServiceOrderStatus(View view) throws Exception{
        Room room = roomController.select(view);
        Stay stay = (Stay)stayController.getStayByRoomNumber(room.getRoomNumber());

        ArrayList<RoomServiceOrder> roomServiceOrders = stay.getRoomServiceOrders();
        view.display(roomServiceOrders);

        view.message("Do you want to update all order status?");
        if(view.options(Arrays.asList(Options.Yes, Options.No)) == Options.Yes) {
            for (RoomServiceOrder roomServiceOrder :roomServiceOrders) {
                roomServiceOrder.setStatus(RoomServiceOrderStatus.DELIVERED);
            }
            view.display("Update Success");
        }
    }


    public void removeRoomServiceOrder(View view) throws Exception{
        Room room = roomController.select(view);
        Stay stay = (Stay)stayController.getStayByRoomNumber(room.getRoomNumber());

        ArrayList<RoomServiceOrder> roomServiceOrders = stay.getRoomServiceOrders();
        view.display(roomServiceOrders);

        view.message("Do you want to cancel all order status?");
        if(view.options(Arrays.asList(Options.Yes, Options.No)) == Options.Yes) {
            for (RoomServiceOrder roomServiceOrder :roomServiceOrders) {
                roomServiceOrder.setStatus(RoomServiceOrderStatus.CANCELED);
            }
            view.display("Update Success");
        }
    }

}
