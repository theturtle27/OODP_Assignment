package Model.Room;

import Persistence.Entity;

public class RoomType extends Entity{

    private RoomTypeEnum roomTypeEnum;
    private double roomRate;


    public RoomType(RoomTypeEnum roomTypeEnum, double roomRate)
    {
        this.roomTypeEnum = roomTypeEnum;
        this.roomRate = roomRate;
    }

    public RoomTypeEnum getRoomTypeEnum()
    {
        return roomTypeEnum;
    }

    public double getRoomRate()
    {
        return roomRate;
    }

    public void setRoomRate(double roomRate)
    {
        this.roomRate = roomRate;
    }

    public String toString()
    {

        // convert room type enum to String
        String stringRoomTypeEnum = capitalizeFirstLetter(roomTypeEnum.toString());

        // format room rate
        String stringRoomRate = String.format("%.2f",roomRate);

        return    "\n------------Room Type--------------"
                + "\nRoom Type        : " + stringRoomTypeEnum
                + "\nRoom Rate        : " + stringRoomRate;

    }

}
