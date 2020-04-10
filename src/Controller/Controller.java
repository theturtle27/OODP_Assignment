package Controller;

import View.View;

import java.util.List;

public interface Controller {

    public List<String> getOptions();

    public void onOptionSelected(View view, int option);
}
