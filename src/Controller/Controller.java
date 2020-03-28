package Controller;

import java.util.List;

public interface Controller {

    List<String> getOptions();

    void onSelected();
}
