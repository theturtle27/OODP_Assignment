import Controller.Controller;
import Controller.EntityManager.*;
import Controller.FunctionManager.ReportController;
import Controller.FunctionManager.ReservationController;
import Controller.FunctionManager.RoomServiceManagementController;
import Controller.FunctionManager.StayController;
import Controller.NavigationController;
import Persistence.Persistence;
import View.ConsoleView;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Scanner sc = new Scanner(System.in);
        Persistence persistence;
        try {
            persistence = new Persistence(new File("aotoid.cfg"));

            CreditCardController creditCardController = new CreditCardController(persistence);
            RoomTypeController roomTypeController = new RoomTypeController(persistence);
            RoomController roomController = new RoomController(persistence, roomTypeController);
            MenuController menuController = new MenuController(persistence);

            GuestController guestController = new GuestController(persistence, creditCardController);
            ReservationController reservationController = new ReservationController(persistence, creditCardController, roomController, roomTypeController, guestController);

            reservationController.createExpirationTimer();

            NavigationController hotelmanagementController = new NavigationController();
            hotelmanagementController.addView(new ConsoleView(roomController, "Manage Room", sc));
            hotelmanagementController.addView(new ConsoleView(roomTypeController, "Manage Room Type", sc));
            hotelmanagementController.addView(new ConsoleView(menuController, "Manage Menu Item", sc));


            ConsoleView hotelmanagementView = new ConsoleView(hotelmanagementController, "Hotel Management", sc);

            NavigationController guestmanagementController = new NavigationController();
            guestmanagementController.addView(new ConsoleView(guestController, "Manage Guest", sc));
            guestmanagementController.addView(new ConsoleView(creditCardController, "Manage Credit Card", sc));

            ConsoleView guestmanagementView = new ConsoleView(guestmanagementController, "Guest Management", sc);

            StayController stayController = new StayController(persistence,guestController,reservationController,roomController,roomTypeController);

            NavigationController frontDeskController = new NavigationController();
            frontDeskController.addView(new ConsoleView(stayController, "Check-In/Check-Out Management", sc));
            frontDeskController.addView(new ConsoleView(new RoomServiceManagementController(persistence,roomController,menuController,stayController), "Room Service Management", sc));

            ConsoleView frontDeskView = new ConsoleView(frontDeskController, "Front Desk", sc);


            ConsoleView creditCardView = new ConsoleView(creditCardController, "Credit Card Management", sc);

            NavigationController mainNav = new NavigationController();
            mainNav.addView(hotelmanagementView);
            mainNav.addView(guestmanagementView);
            mainNav.addView(new ConsoleView(reservationController, "Reservation System", sc));
            mainNav.addView(frontDeskView);
            mainNav.addView(new ConsoleView(new ReportController(persistence), "Report System", sc));

            ConsoleView mainView = new ConsoleView(mainNav, "Main View", sc);
            mainView.show();

            persistence.writeAllDataArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
