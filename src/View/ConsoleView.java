package View;

import Controller.Controller;

public class ConsoleView extends View {

    public ConsoleView(String title, Controller controller) {
        super(title);
    }

    @Override
    public void show(){
        System.out.println("==============" + this.getTitle() + "===================");


    }

    @Override
    public void onSelected() {

    }
}
