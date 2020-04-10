package View;

import java.util.List;
import java.util.Map;

public abstract class View {
    protected final String title;

    public View(String title) {this.title = title;}

    public abstract void input(Map<String, String> input);

    /**
     * Warns user of the invalid fields.
     * @param invalidFields - A list of invalid fields that requires user's attention.
     */
    public abstract void error(List<String> invalidFields);

    /**
     * Displays the specified entity.
     * @param object - The object to be displayed.
     */
    public abstract void display(Object object);

    /**
     * Displays a list of entity.
     * @param objList - The list of objects to be displayed.
     */
    public abstract void display(List objList);

    /**
     * Display a message.
     * @param message - The message to be displayed
     */
    public abstract void message(String message);

    /**
     * Constructs a user bailout message and returns user's choice.
     * @return A flag indicating if user wants to bailout.
     */
    public abstract boolean bailout();

    /**
     * Displays a list of options for the user to select.
     * @param options - The list of options.
     * @return Selected option.
     */
    public abstract <T> T options(List<T> options);

    public String getTitle() {
        return title;
    }

    public abstract void show();

    public abstract void onSelected();
}
