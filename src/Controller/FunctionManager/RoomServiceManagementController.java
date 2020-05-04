package Controller.FunctionManager;

import Controller.EntityManager.MenuController;
import Controller.EntityManager.RoomController;
import Controller.PersistenceController;
import Model.Guest.Guest;
import Model.Menu.MenuItem;
import Model.Room.BedType;
import Model.Room.Room;
import Model.Room.RoomStatus;
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

    private static final String REGEX_NUMBERS = "[0-9]+";
    private static final String REGEX_BOOLEAN = "^(?:(0|1))$";
    private static final String REGEX_ONE_ALPHA_NUMERIC_CHARACTER = "^.*[a-zA-Z0-9]+.*$";

    private static final String ORDER_REMARK = "the order remark";
    private static final String ANOTHER_ITEM = "whether you want to add another item :\n1) Yes\n0) No\n\nPlease select an option";
    private static final String NUMBER_ROOM_SERVICE_ORDER = "the number of the room service order";
    private static final String ROOM_SERVICE_ORDER_STATUS = "room service order status";

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
        return Arrays.asList("Create a new room service order",
                "Update a room service order",
                "Search a room service order",
                "Remove a room service order",
                "Print all room service orders of a room");
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
                select(view);
                break;

            case 3:
                removeRoomServiceOrder(view);
                break;

            case 4:
                show(view);
                break;

        }
    }

    public void addRoomServiceOrder(View view) throws Exception{

        Room room = roomController.select(view);

        //check whether guest was found
        if (room == null) {
            return;
        }

        // check whether room is occupied
        if(room.getStatus() != RoomStatus.OCCUPIED)
        {
            view.displayText("The room is not occupied. No room service order can be ordered for a room that is not occupied.\n\n");

            return;
        }

        RoomServiceOrder roomServiceOrder = new RoomServiceOrder();

        boolean addAnotherItem = false;

        do {

            // print menu
            menuController.show(view);

            // select menu item
            MenuItem menuItem = menuController.select(view);

            if(menuItem == null)
            {
                return;
            }

            // get order remark
            String orderRemark = view.getInputRegex(ORDER_REMARK, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

            // break out of function
            if(orderRemark == null)
            {
                return;
            }

            // create new order item
            OrderItem orderItem = new OrderItem(menuItem, orderRemark);

            // add item to room service order
            roomServiceOrder.addOrderItem(orderItem);

            // print menu item
            view.displayText(orderItem.toString());

            view.displayText("\n\nThe order item has been added to the order.\n\n");

            // get enabled wifi
            String stringAddAnotherItem = view.getInputRegex(ANOTHER_ITEM, REGEX_BOOLEAN);

            // break out of function
            if (stringAddAnotherItem == null) {
                return;
            }

            // convert String to boolean
            addAnotherItem = "1".equals(stringAddAnotherItem);

        }while(addAnotherItem);

        // confirm room service order
        roomServiceOrder.confirmRoomServiceOrder();

        view.displayText("\nThe order has been placed.\n");


        view.displayText(roomServiceOrder.toString() + "\n\n");

        // get stay by room number
        Stay stay = (Stay)stayController.getStayByRoomNumber(room.getRoomNumber());

        // add room service order to stay
        stay.addRoomSerciceOrder(roomServiceOrder);


    }

    public void updateRoomServiceOrderStatus(View view) throws Exception{

        RoomServiceOrder roomServiceOrder = select(view);

        //check whether room was found
        if (roomServiceOrder == null) {
            return;
        }

        // get room service order status
        RoomServiceOrderStatus roomServiceOrderStatus = (RoomServiceOrderStatus) view.getInputEnum(RoomServiceOrderStatus.class, ROOM_SERVICE_ORDER_STATUS, REGEX_NUMBERS);

        // break out of method
        if (roomServiceOrderStatus == null) {
            return;
        }

        //
        roomServiceOrder.setStatus(roomServiceOrderStatus);

        // print room
        view.displayText(roomServiceOrder.toString());

        // print
        view.displayText("\n\nThe status of the room service order has been updated.\n\n");
    }


    public void removeRoomServiceOrder(View view) throws Exception{

        Room room = roomController.select(view);

        //check whether guest was found
        if (room == null) {
            return;
        }

        // check whether room is occupied
        if(room.getStatus() != RoomStatus.OCCUPIED)
        {
            view.displayText("The room is not occupied. No room service order can be searched for a room which is not occupied.\n\n");

            return;
        }

        Stay stay = (Stay)stayController.getStayByRoomNumber(room.getRoomNumber());

        ArrayList<RoomServiceOrder> roomServiceOrders = stay.getRoomServiceOrders();

        if(roomServiceOrders.size() == 0)
        {
            view.displayText("No room service order can be deleted since no room service order exists for this room.\n\n");

            return;
        }

        // select room service order
        RoomServiceOrder roomServiceOrder = view.getInputArray(roomServiceOrders, NUMBER_ROOM_SERVICE_ORDER, REGEX_NUMBERS);

        // break out of method
        if (roomServiceOrder == null) {
            return;
        }

        // check whether the room service order status is confirmed
        // If the room service order status is already preparing or delivered,
        // then the room service order cannot be removed
        if(roomServiceOrder.getStatus().equals(RoomServiceOrderStatus.CONFIRMED))
        {
            // remove room service order
            roomServiceOrders.remove(roomServiceOrder);

            view.displayText("\nThe room service order has been removed.\n\n");
        }
        else
        {
            // print message that the room service order cannot be removed
            view.displayText("\nThe room service order cannot be removed since the room service order status is already: "  + view.capitalizeFirstLetter(roomServiceOrder.getStatus().toString()) + ".\n\n");
        }

    }

    private RoomServiceOrder select(View view) throws Exception {

        Room room = roomController.select(view);

        //check whether guest was found
        if (room == null) {
            return null;
        }

        // check whether room is occupied
        if(room.getStatus() != RoomStatus.OCCUPIED)
        {
            view.displayText("The room is not occupied. No room service order can be searched for a room which is not occupied.\n\n");

            return null;
        }

        Stay stay = (Stay)stayController.getStayByRoomNumber(room.getRoomNumber());

        ArrayList<RoomServiceOrder> roomServiceOrders = stay.getRoomServiceOrders();

        if(roomServiceOrders.size() == 0)
        {
            view.displayText("No room service order exists for this room.\n\n");

            return null;
        }

        // select room service order
        RoomServiceOrder roomServiceOrder = view.getInputArray(roomServiceOrders, NUMBER_ROOM_SERVICE_ORDER, REGEX_NUMBERS);

        // break out of method
        if (roomServiceOrder == null) {
            return null;
        }

        // print room service order
        view.displayText(roomServiceOrder.toString() + "\n\n");

        return roomServiceOrder;

    }

    public void show(View view) throws Exception {

        Room room = roomController.select(view);

        //check whether room was found
        if (room == null) {
            return;
        }

        // check whether room is occupied
        if(room.getStatus() != RoomStatus.OCCUPIED)
        {
            view.displayText("The room is not occupied. No room service order can be searched for a room which is not occupied.\n\n");

            return;
        }

        Stay stay = (Stay)stayController.getStayByRoomNumber(room.getRoomNumber());

        ArrayList<RoomServiceOrder> roomServiceOrders = stay.getRoomServiceOrders();

        if(roomServiceOrders.size() == 0)
        {
            view.displayText("No room service order exists for this room.\n\n");

            return;
        }

        view.displayText("The following room service orders are on file for this room:\n");

        // iterate through all rooms
        for (RoomServiceOrder roomServiceOrder : roomServiceOrders) {

            //print room
            view.displayText(roomServiceOrder.toString() + "\n");

        }

        view.displayText("\n");

    }

}
