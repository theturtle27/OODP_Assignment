package Controller.EntityManager;

import Controller.EntityController;
import Model.Menu.MenuItem;
import Persistence.Persistence;
import Persistence.Entity;
import View.View;

import java.util.*;

public class MenuController extends EntityController<MenuItem> {

    public final static String KEY_NAME = "menu item name";
    public final static String KEY_DESCRIPTION = "description";
    public final static String KEY_PRICE = "price";
    public final static String KEY_SEARCH = "name of the menuitem to search for";
    public final static String KEY_ID = "ID of guest or 'Search' to search for guest ID by name";

    public MenuController(Persistence persistence) {
        super(persistence);
    }


    @Override
    protected String getEntityName() {
        return "MenuItem Controller";
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
        }
    }

    @Override
    protected void create(View view) throws Exception {
        Map<String, String> inputMap = new LinkedHashMap<String, String>();
        inputMap.put(KEY_NAME, null);
        inputMap.put(KEY_DESCRIPTION, null);
        inputMap.put(KEY_PRICE, null);

        boolean valid = false;
        do {
            view.input(inputMap);

            try {
                MenuItem menuItem = new MenuItem(inputMap.get(KEY_NAME), inputMap.get(KEY_DESCRIPTION), Double.parseDouble(inputMap.get(KEY_PRICE)));

                // Validate all fields
                List<String> invalids = new ArrayList<String>();
                if(menuItem.getName().length() == 0)
                    invalids.add(KEY_NAME);
                if(menuItem.getDescription().length() == 0)
                    invalids.add(KEY_DESCRIPTION);
                if(menuItem.getPrice() <= 0)
                    invalids.add(KEY_PRICE);

                if(invalids.size() == 0) {
                    // Ensure no duplicate guest record, with same identification and nationality
                    Persistence persistence = this.getPersistenceImpl();

                    persistence.create(menuItem, MenuItem.class);

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
        Iterable<MenuItem> menuItems = persistence.search(MenuItem.class);

        // Loop through results and add it into the list
        for(Entity entity: menuItems)
            entityList.add(entity);

        // Display guests
        view.display(entityList);

        return entityList.size() > 0;
    }

    @Override
    protected void update(View view) throws Exception {

        MenuItem menuItem = select(view);

        if(menuItem != null) {
            Persistence persistence = this.getPersistenceImpl();
            Map<String, String> inputMap = new LinkedHashMap<String, String>();
            inputMap.put(KEY_PRICE, null);

            boolean valid = false;
            do {
                // Retrieve user input for updateable fields
                view.input(inputMap);

                try {
                    menuItem.setPrice(Double.parseDouble(inputMap.get(KEY_PRICE)));

                    // Validate all fields
                    List<String> invalids = new ArrayList<String>();
                    if(menuItem.getPrice() <= 0)
                        invalids.add(KEY_PRICE);

                    // Attempts to update entity
                    if(invalids.size() == 0 && persistence.update(menuItem, MenuItem.class)) {
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

        MenuItem menuItem = select(view);

        Persistence persistence = this.getPersistenceImpl();
        if(menuItem != null && persistence.delete(menuItem, MenuItem.class))
            view.message("MenuItem deleted successfully!");

    }


    @Override
    public MenuItem select(View view) throws Exception {
        MenuItem menuItem = null;

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
                        menuItem = persistence.retrieveByID(Long.parseLong(input), MenuItem.class);
                        if(menuItem == null)
                            view.error(Arrays.asList(KEY_ID));
                    }
                } catch(NumberFormatException e) {
                    view.error(Arrays.asList(KEY_ID));
                }
            } while(retry);
        } while(menuItem == null && !view.bailout());

        return menuItem;
    }

}