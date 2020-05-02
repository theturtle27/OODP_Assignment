package Controller.EntityManager;

import Controller.EntityController;
import Model.Room.RoomType;
import Persistence.Persistence;
import Persistence.Entity;
import View.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomTypeController extends EntityController<RoomType> {

    private static final String REGEX_NUMBERS = "[0-9]+";
    private static final String REGEX_ROOM_RATE = "[0-9]+([,.][0-9]{1,2})?";

    private static final String ROOM_RATE = "the room rate in SGD";
    private static final String NUMBER_ROOM_TYPE = "number of the room type";

    public RoomTypeController(Persistence persistence) {super(persistence);}

    @Override
    protected String getEntityName() {
        return "Room Type";
    }

    @Override
    public List<String> getOptions() {

        return Arrays.asList("Update a room type",
                "Print all room types");

    }

    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception {
        switch (option) {
            case 0:
                update(view);
                break;
            case 1:
                show(view);
                break;
        }
    }

    protected void create(View view) throws Exception
    {

    }

    protected boolean retrieve(View view) throws Exception
    {
        return false;
    }

    protected void update(View view) throws Exception
    {
        // get room type enum
        RoomType roomType = select(view);

        // break out of method
        if(roomType == null)
        {
            return;
        }

        // get room rate
        String stringRoomRate = view.getInputRegex(ROOM_RATE, REGEX_ROOM_RATE);

        // break out of method
        if(stringRoomRate == null)
        {
            return;
        }

        // convert String to double
        double roomRate = Double.parseDouble(stringRoomRate);

        // set the room rate
        roomType.setRoomRate(roomRate);

        // print room type
        view.displayText(roomType.toString());

        // display text
        view.displayText("\n\nThe room type has been updated.\n\n");
    }

    protected void delete(View view) throws Exception
    {

    }

    protected void show(View view) throws Exception
    {
        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all rooms
            ArrayList<Entity> roomTypes = persistence.retrieveAll(RoomType.class);

            // check whether any room types exist
            if (roomTypes.size() == 0) {
                view.displayText("No room type exists. Error!\n\n");

                return;
            }

            view.displayText("The following room types exist:\n");

            // iterate through all guests
            for (Entity entity : roomTypes) {

                // cast to guest object
                RoomType roomType = (RoomType)entity;

                //print guest
                view.displayText(roomType.toString() + "\n");

            }

            view.displayText("\n");
        }
        catch(Exception e)
        {

        }
    }

    public RoomType select(View view)
    {
        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        // initialize guest
        RoomType roomType = null;

        try {

            // get all room types
            ArrayList<Entity> roomTypes = persistence.retrieveAll(RoomType.class);

            // check whether any room types exist
            if (roomTypes.size() == 0) {
                view.displayText("No room type exists. Error!\n\n");

                return null;
            }

            // flag to check whether the entry of the should be tried again
            boolean repeatEntry;

            //repeat
            do {
                // initialize repeat flag to false
                repeatEntry = false;

                // select roomType
                roomType = (RoomType) view.getInputArray(roomTypes, NUMBER_ROOM_TYPE, REGEX_NUMBERS);

                // break out of method
                if (roomType == null) {
                    return null;
                }

                // print room type details
                view.displayText(roomType.toString() + "\n\n");

            } while (roomType == null && repeatEntry);
        }
        catch(Exception e)
        {

        }

        return roomType;
    }

}
