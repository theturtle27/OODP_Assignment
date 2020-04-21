package Model.Payment;

public class Payment {
    // private final List<Reservation> reservations;
    private double totalPrice;
    private double tax;
    private double weekendSurcharge;
    private double discount;
    private DiscountType discountType;
	/*public Payment(List<Reservation> reservations) {
		this.reservations = reservations;
	}*/

    public Payment(double tax, double weekendSurcharge, double discount) {
        this.tax = tax;
        this.weekendSurcharge = weekendSurcharge;
        this.discount = discount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getWeekendSurcharge() {
        return weekendSurcharge;
    }

    public void setWeekendSurcharge(double weekendSurcharge) {
        this.weekendSurcharge = weekendSurcharge;
    }

    public void setDiscount(DiscountType discountType, double discount) {
        this.discount = discount;
        this.discountType = discountType;
    }

    public void calTotalPrice(Reservation reservation) {
        double totalPrice = 0;

        //for (Reservation reservation: reservations) {
        //long numOfDaysWD = reservation.getWeekDay();
        long numOfDaysWE = reservation.getWeekEnd();
        //totalPrice += reservation.getAssignedRoom().getType().getPrice() * numOfDaysWD;
        totalPrice += (reservation.getPrice() * (100 + weekendSurcharge)/100) * numOfDaysWE;

				/* for(RoomServiceOrder order: reservation.getOrderList()) {
					// Loop through all the service orders and add up all the service orders.
					if(order.getStatus() == OrderStatus.Delivered)
						totalPrice += order.getItem().getPrice();
		}
	}*/
        if(discountType != null) {
            if(discountType == DiscountType.FIXED)
                totalPrice -= discount;
            else
                totalPrice *= (100 - discount) / 100;
        }
        totalPrice *= (100 + tax)/100;
        this.totalPrice = totalPrice;
    }
