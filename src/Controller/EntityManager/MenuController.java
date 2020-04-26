package Controller.EntityManager;

import Controller.EntityController;
import Model.Guest.Guest;
import Model.Menu.MenuItem;
import Model.Room.RoomType;
import Persistence.Persistence;
import Persistence.Entity;
import View.View;

import java.util.*;

public class MenuController extends EntityController<MenuItem> {

    private static final String REGEX_NUMBERS = "[0-9]+";
    private static final String REGEX_ONE_ALPHA_NUMERIC_CHARACTER = "^.*[a-zA-Z0-9]+.*$";
    private static final String REGEX_PRICE = "[0-9]+([,.][0-9]{1,2})?";

    private static final String MENU_ITEM_NAME = "the name of the menu item";
    private static final String MENU_ITEM_DESCRIPTION = "the description of the menu item";
    private static final String MENU_ITEM_PRICE = "the price of the menu item";
    private static final String NUMBER_ROOM_TYPE = "number of the menu item";


    public final static String KEY_NAME = "name of the menu item";
    public final static String KEY_DESCRIPTION = "description of the menu item";
    public final static String KEY_PRICE = "price of the menu item";
    public final static String KEY_SEARCH = "name of the menu item to search for";
    public final static String KEY_ID = "ID of the menu item";

    public MenuController(Persistence persistence) {
        super(persistence);
    }


    @Override
    protected String getEntityName() {
        return "Menu Item";
    }

    @Override
    public List<String> getOptions() {
        return Arrays.asList("Create a new menu item",
                "Update the price of a menu item",
                "Remove a menu item",
                "Print the menu");
    }

    @Override
    protected void safeOnOptionSelected(View view, int option) throws Exception {
        switch (option) {
            case 0:
                create(view);
                break;
            case 1:
                update(view);
                break;
            case 2:
                delete(view);
                break;
            case 3:
                show(view);
                break;
        }
    }

    protected void create(View view) throws Exception {

        // get name of the menu item
        String menuItemName = view.getInputRegex(MENU_ITEM_NAME, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(menuItemName == null)
        {
            return;
        }

        // get description of the menu item
        String menuItemDescription = view.getInputRegex(MENU_ITEM_DESCRIPTION, REGEX_ONE_ALPHA_NUMERIC_CHARACTER);

        // break out of function
        if(menuItemDescription == null)
        {
            return;
        }

        // get price of menu item
        String stringMenuItemPrice = view.getInputRegex(MENU_ITEM_PRICE, REGEX_PRICE);

        // break out of method
        if(stringMenuItemPrice == null)
        {
            return;
        }

        // convert String to double
        double menuItemPrice = Double.parseDouble(stringMenuItemPrice);

        // create menu item
        MenuItem menuItem = new MenuItem(menuItemName, menuItemDescription, menuItemPrice);

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        // add menu item to ArrayList of menu items
        persistence.createCache(menuItem, MenuItem.class);

        // print menu item
        view.displayText(menuItem.toString());

        view.displayText("\n\nThe menu item has been added to the menu.\n\n");

    }

    protected void update(View view) throws Exception {

        // get room type enum
        MenuItem menuItem = select(view);

        // break out of method
        if(menuItem == null)
        {
            return;
        }

        // get price of menu item
        String stringMenuItemPrice = view.getInputRegex(MENU_ITEM_PRICE, REGEX_PRICE);

        // break out of method
        if(stringMenuItemPrice == null)
        {
            return;
        }

        // convert String to double
        double menuItemPrice = Double.parseDouble(stringMenuItemPrice);

        // set the room rate
        menuItem.setPrice(menuItemPrice);

        // print room type
        view.displayText(menuItem.toString());

        // display text
        view.displayText("\n\nThe price of the menu item has been updated.\n\n");


    }

    public MenuItem select(View view) throws Exception {

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        // initialize menu item
        MenuItem menuItem = null;

        try {

            // get all menu items
            ArrayList<Entity> menuItems = persistence.retrieveAll(MenuItem.class);

            // check whether any room types exist
            if (menuItems.size() == 0) {
                view.displayText("No menu items exist. Please create a menu item before searching for it\n\n");

                return null;
            }

            // flag to check whether the entry of the should be tried again
            boolean repeatEntry;

            //repeat
            do {
                // initialize repeat flag to false
                repeatEntry = false;

                // select roomType
                menuItem = (MenuItem) view.getInputArray(menuItems, NUMBER_ROOM_TYPE, REGEX_NUMBERS);

                // break out of method
                if (menuItem == null) {
                    return null;
                }

                // print room type details
                view.displayText(menuItem.toString() + "\n\n");

            } while (menuItem == null && repeatEntry);
        }
        catch(Exception e)
        {

        }

        return menuItem;

    }


    @Override
    protected boolean retrieve(View view) throws Exception {
        return false;
    }

    @Override
    protected void delete(View view) throws Exception
    {

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all menu items
            ArrayList<Entity> guests = persistence.retrieveAll(MenuItem.class);

            // search for menu item
            MenuItem menuItem = select(view);

            //check whether menu item was found
            if (menuItem == null) {
                return;
            }

            // remove guest
            guests.remove(menuItem);

            view.displayText("The menu item has been removed.\n\n");
        }
        catch(Exception e)
        {

        }

    }

    @Override
    public void show(View view) throws Exception{

        // get persistence
        Persistence persistence = this.getPersistenceImpl();

        try {

            // get all menu items
            ArrayList<Entity> menuItems = persistence.retrieveAll(MenuItem.class);

            // check whether any guests exist
            if (menuItems.size() == 0) {
                view.displayText("No menu item exists. Create a menu item before printing the menu items.\n\n");

                return;
            }

            view.displayText("===============MENU================");

            // iterate through all guests
            for (Entity entity : menuItems) {

                // cast to menu item object
                MenuItem menuItem = (MenuItem)entity;

                //print menu item
                view.displayText(menuItem.toString());

            }

            view.displayText("\n\n");
        }
        catch(Exception e)
        {

        }
    }

}
