package View;

import java.util.List;
import java.util.Map;

public abstract class View {
    protected final String title;

    public View(String title) {this.title = title;}

    public abstract void input(Map<String, String> input);

    public abstract void error(List<String> invalidFields);

    public abstract void display(Object object);

    public abstract void display(List objList);

    public abstract void message(String message);

    public abstract boolean bailout();

    public abstract <T> T options(List<T> options);

    public String getTitle() {
        return title;
    }

    public abstract void show();

    public abstract void onSelected();
}
