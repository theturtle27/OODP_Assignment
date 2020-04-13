import Controller.EntityManager.GuestController;
import Controller.EntityManager.MenuController;
import Controller.EntityManager.RoomController;
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

//            RoomTypeController rtController = new RoomTypeController(persistence);
//            GuestController gController = new GuestController(persistence);
//            ReservationController rController = new ReservationController(persistence, gController);
//            ServiceOrderController soController = new ServiceOrderController(persistence, new MenuItemController(persistence));

            NavigationController managementController = new NavigationController();
            managementController.addView(new ConsoleView(new GuestController(persistence), "Manage Guest", sc));
            managementController.addView(new ConsoleView(new MenuController(persistence), "Manage Menu Item", sc));
            managementController.addView(new ConsoleView(new RoomController(persistence), "Manage Room", sc));
//            managementController.addView(new ConsoleView(rtController, "Manage Room Type", sc));

            ConsoleView managementView = new ConsoleView(managementController, "Management View", sc);
//
//            NavigationController hrsController = new NavigationController();
//            hrsController.addView(new ConsoleView(rController, "Reservation System", sc));
//            hrsController.addView(new ConsoleView(new CheckInCheckOutController(persistence, gController, rController), "Check-in/Check-out", sc));
//
//
//            NavigationController hssController = new NavigationController();
//            hssController.addView(new ConsoleView(soController, "Manage Service Order", sc));
//
//            ConsoleView hrsView = new ConsoleView(hrsController, "Hotel Reservation System", sc);
//            ConsoleView hssView = new ConsoleView(hssController, "Hotel Service System", sc);
//            ConsoleView reportView = new ConsoleView(new ReportController(persistence), "Room occupancy report", sc);

            NavigationController mainNav = new NavigationController();
            mainNav.addView(managementView);
//            mainNav.addView(hrsView);
//            mainNav.addView(hssView);
//            mainNav.addView(reportView);

            ConsoleView mainView = new ConsoleView(mainNav, "Main View", sc);
            mainView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
