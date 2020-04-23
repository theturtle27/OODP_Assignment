package View;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
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

    public abstract YearMonth getValidDate(String name, String patternDate, String regexDate);

    public abstract LocalDate getValidDate(String name, String patternDate, String printPatternDate, String regexDate, LocalDate dateInput);

    public abstract String getInputRegex(String name, String patternCondition);

    public abstract <E extends Enum<?>> Enum<?> getInputEnum(Class<E> e, String name, String patternEnum);

    public abstract <E> E getInputArray(ArrayList<E> e, String name, String patternEnum);

    public abstract boolean repeatEntry(String name, String operation);

    public abstract void displayText(String OutputMessage);

    public abstract String capitalizeFirstLetter(String str);


}
