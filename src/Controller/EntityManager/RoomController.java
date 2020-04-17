package Controller.EntityManager;

import Persistence.Entity;
import Controller.EntityController;
import Model.Room.Room;
import Persistence.Persistence;
import View.View;

import java.util.*;

public class RoomController extends EntityController<Room> {

    public final static String KEY_ROOM_NUMBER = "room number";
    public final static String KEY_VIEW = "view";
    public final static String KEY_PRICE = "price";
    public final static String KEY_SEARCH = "room number to search for";
    public final static String KEY_ID = "ID of guest or 'Search' to search for guest ID by name";

    public RoomController(Persistence persistence) {
        super(persistence);
    }


    @Override
    protected String getEntityName() {
        return "Room";
    }

    @Override
    public List<String> getOptions() {
        List<String> options = new ArrayList<String>(super.getOptions());

        return options;
    }

    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception {
        switch (option) {
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

    @Override
    protected void create(View view) throws Exception {
        Map<String, String> inputMap = new LinkedHashMap<String, String>();
        inputMap.put(KEY_ROOM_NUMBER, null);

        boolean valid = false;
        do {
            view.input(inputMap);

            try {
                Room room = new Room(inputMap.get(KEY_ROOM_NUMBER));

                // Validate all fields
                List<String> invalids = new ArrayList<String>();
                if(room.getNumber().length() == 0)
                    invalids.add(KEY_ROOM_NUMBER);

                if(invalids.size() == 0) {
                    // Ensure no duplicate guest record, with same identification and nationality
                    Persistence persistence = this.getPersistenceImpl();

                    persistence.create(room, Room.class);

                    view.message("\nThe menu item has been added to the menu.\n");
                    valid = true;

                }
                else {
                    view.error(invalids);
                }
            } catch(IndexOutOfBoundsException e) {
            }
        } while(!valid && !view.bailout());

    }

    @Override
    protected boolean retrieve(View view) throws Exception {
        Map<String, String> inputMap = new LinkedHashMap<String, String>();
        inputMap.put(KEY_SEARCH, null);

        view.input(inputMap);

        Persistence persistence = this.getPersistenceImpl();

        List entityList = new ArrayList();
        // Provide a predicate to search for matching items
        Iterable<Room> rooms = persistence.search(Room.class);

        // Loop through results and add it into the list
        for(Room entity: rooms) {
            if (entity.getNumber().equals(inputMap.get(KEY_SEARCH)))
                entityList.add(entity);
        }

        // Display guests
        view.display(entityList);

        return entityList.size() > 0;
    }

    @Override
    protected void update(View view) throws Exception {

        Room room = select(view);

        if(room != null) {
            Persistence persistence = this.getPersistenceImpl();
            Map<String, String> inputMap = new LinkedHashMap<String, String>();
            inputMap.put(KEY_VIEW, null);

            boolean valid = false;
            do {
                // Retrieve user input for updateable fields
                view.input(inputMap);

                try {
                    room.setView(inputMap.get(KEY_VIEW));

                    // Validate all fields
                    List<String> invalids = new ArrayList<String>();
                    if(room.getView().length() == 0)
                        invalids.add(KEY_VIEW);

                    // Attempts to update entity
                    if(invalids.size() == 0 && persistence.update(room, Room.class)) {
                        valid = true;
                        view.message("MenuItem successfully updated!");
                    }
                    else {
                        view.error(invalids);
                    }
                } catch(IndexOutOfBoundsException e) {
                }
            } while(!valid && !view.bailout());
        }
    }

    @Override
    protected void delete(View view) throws Exception
    {

        Room room = select(view);

        Persistence persistence = this.getPersistenceImpl();
        if(room != null && persistence.delete(room, Room.class))
            view.message("Room deleted successfully!");

    }


    @Override
    public Room select(View view) throws Exception {
        Room room = null;

        Map<String, String> inputMap = new LinkedHashMap<String, String>();
        inputMap.put(KEY_ID, null);

        Persistence persistence = this.getPersistenceImpl();
        do {
            boolean retry;
            do {
                retry = false;
                view.input(inputMap);

                try {
                    String input = inputMap.get(KEY_ID);
                    if(input.toLowerCase().equals("search")) {
                        retrieve(view);
                        retry = true;
                    }
                    else {
                        room = persistence.retrieveByID(Long.parseLong(input), Room.class);
                        if(room == null)
                            view.error(Arrays.asList(KEY_ID));
                    }
                } catch(NumberFormatException e) {
                    view.error(Arrays.asList(KEY_ID));
                }
            } while(retry);
        } while(room == null && !view.bailout());

        return room;
    }

    @Override
    public void show(View view) throws Exception{
        Persistence persistence = this.getPersistenceImpl();
        List entityList = new ArrayList();
        // Provide a predicate to search for matching items
        Iterable<Room> rooms = persistence.search(Room.class);

        // Loop through results and add it into the list
        for(Entity entity: rooms)
            entityList.add(entity);

        view.display(entityList);
    }

}
