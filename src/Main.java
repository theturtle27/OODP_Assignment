import Controller.EntityManager.GuestController;
import Controller.EntityManager.MenuController;
import Controller.EntityManager.RoomController;
import Controller.FunctionManager.ReportController;
import Controller.FunctionManager.ReservationController;
import Controller.FunctionManager.RoomServiceManagementController;
import Controller.FunctionManager.StayController;
import Controller.NavigationController;
import Persistence.Persistence;
import View.ConsoleView;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Scanner sc = new Scanner(System.in);
        Persistence persistence;
        try {
            persistence = new Persistence(new File("aotoid.cfg"));

            NavigationController hotelmanagementController = new NavigationController();
            hotelmanagementController.addView(new ConsoleView(new RoomController(persistence), "Manage Room", sc));
            hotelmanagementController.addView(new ConsoleView(new MenuController(persistence), "Manage Menu Item", sc));



            ConsoleView hotelmanagementView = new ConsoleView(hotelmanagementController, "Hotel Management", sc);


            NavigationController frontDeskController = new NavigationController();
            frontDeskController.addView(new ConsoleView(new StayController(persistence), "Check-In/Check-Out Management", sc));
            frontDeskController.addView(new ConsoleView(new RoomServiceManagementController(persistence), "Room Service Management", sc));

            ConsoleView frontDeskView = new ConsoleView(frontDeskController, "Front Desk", sc);

            NavigationController mainNav = new NavigationController();
            mainNav.addView(hotelmanagementView);
            mainNav.addView(new ConsoleView(new GuestController(persistence), "Guest Management", sc));
            mainNav.addView(new ConsoleView(new ReservationController(persistence), "Reservation System", sc));
            mainNav.addView(frontDeskView);
            mainNav.addView(new ConsoleView(new ReportController(persistence), "Report System", sc));

            ConsoleView mainView = new ConsoleView(mainNav, "Main View", sc);
            mainView.show();

            persistence.writeAllData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
