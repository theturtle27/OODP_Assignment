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

        return    "\n------------Room Type--------------"
                + "\nRoom Type       : " + stringRoomTypeEnum
                + "\nRoom Rate       : " + roomRate;

    }

    // Method to convert the string
    public String capitalizeFirstLetter(String str) {
        StringBuffer s = new StringBuffer();

        // Declare a character of space
        // To identify that the next character is the starting
        // of a new word
        char ch = ' ';
        for (int i = 0; i < str.length(); i++) {

            // If previous character is space and current
            // character is not space then it shows that
            // current letter is the starting of the word
            if (ch == ' ' && str.charAt(i) != ' ')
            {
                ch = str.charAt(i);
                s.append(Character.toUpperCase(ch));
            }
            else if(str.charAt(i) == '_')
            {
                ch = ' ';
                s.append(ch);
            }
            else
            {
                ch = str.charAt(i);
                s.append(Character.toLowerCase(ch));
            }
        }

        // Return the string with trimming
        return s.toString().trim();
    }

}
