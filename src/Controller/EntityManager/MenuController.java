package Controller.EntityManager;

import Controller.EntityController;
import Model.Guest.Guest;
import Model.Menu.MenuItem;
import Persistence.Persistence;
import View.View;

import java.util.*;
import java.util.regex.Pattern;

public class MenuController extends EntityController<MenuItem> {

    public final static String KEY_NAME = "menu item name";
    public final static String KEY_DESCRIPTION = "description";
    public final static String KEY_PRICE = "price";
    public final static String KEY_ID = "ID of guest or 'Search' to search for guest ID by name";

    public MenuController(Persistence persistence) {
        super(persistence);
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
    protected void update(View view) throws Exception {

        MenuItem menuItem = select(view);

        if(menuItem != null) {
            Persistence persistence = this.getPersistenceImpl();
            Map<String, String> inputMap = new LinkedHashMap<String, String>();
            inputMap.put(KEY_NAME, null);
            inputMap.put(KEY_GENDER, null);
            inputMap.put(KEY_CONTACT_NUMBER, null);
            inputMap.put(KEY_EMAIL_ADDRESS, null);

            boolean valid = false;
            do {
                // Retrieve user input for updateable fields
                view.input(inputMap);

                try {
                    guest.setName(inputMap.get(KEY_NAME));
                    guest.setGender(Character.toUpperCase(inputMap.get(KEY_GENDER).charAt(0)));
                    guest.setContactNo(inputMap.get(KEY_CONTACT_NUMBER));
                    guest.setEmailAddress(inputMap.get(KEY_EMAIL_ADDRESS));

                    // Validate all fields
                    List<String> invalids = new ArrayList<String>();
                    if(guest.getName().length() == 0)
                        invalids.add(KEY_NAME);
                    if(guest.getGender() != 'M' && guest.getGender() != 'F')
                        invalids.add(KEY_GENDER);
                    if(!Pattern.matches("\\+?(\\d|-|\\s|\\(|\\))+", guest.getContactNo()))
                        invalids.add(KEY_CONTACT_NUMBER);
                    if(!Pattern.matches("^.+@.+\\..+$", guest.getEmailAddress()))
                        invalids.add(KEY_EMAIL_ADDRESS);

                    // Attempts to update entity
                    if(invalids.size() == 0 && persistence.update(guest, Guest.class)) {
                        valid = true;
                        view.message("Guest profile successfully updated!");
                    }
                    else {
                        view.error(invalids);
                    }
                } catch(IndexOutOfBoundsException e) {
                    view.error(Arrays.asList(KEY_GENDER));
                }
            } while(!valid && !view.bailout());
        }
    }

    private void removeMenuItem(MenuView menuView)
    {
        menuView.printMenu(menu);
        int menuItemIndex = menuView.getInputInteger("Enter the number of the menu item: ");
        menu.removeMenuItem(menuItemIndex-1);
        menuView.displayText("\nThe menu item has been removed from the menu.\n");
        menuView.printMenu(menu);
        menuView.displayText("\n\n\n\n");
    }

    private void exitMenuEditor(MenuView menuView)
    {
        menuView.displayText("\nThe menu has been changed successfully.\n");
        menuView.printMenu(menu);
        menuView.displayText("\n\n\n\n");
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
//                        menuItem = persistence.retrieveByID(Long.parseLong(input), Guest.class);
//                        if(guest == null)
//                            view.error(Arrays.asList(KEY_ID));
                    }
                } catch(NumberFormatException e) {
                    view.error(Arrays.asList(KEY_ID));
                }
            } while(retry);
        } while(menuItem == null && !view.bailout());

        return menuItem;
    }

}
