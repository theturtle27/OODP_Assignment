package Persistence;


import java.io.Serializable;

public abstract class Entity implements Serializable {
    private final long _id;


    public Entity() {
        this._id = Long.MIN_VALUE;
    }


    public long getIdentifier() {
        return this._id;
    }


    public boolean isManaged() {
        return this._id != Long.MIN_VALUE;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = false;

        if(obj != null && obj instanceof Entity) {
            Entity entity = (Entity) obj;
            if(entity.getClass().equals(this.getClass()) &&
                    entity.isManaged() == this.isManaged()) {
                if(entity.isManaged())
                    equals = entity.getIdentifier() == this.getIdentifier();
                else
                    equals = entity == this;
            }
        }

        return equals;
    }

    @Override
    public int hashCode() {
        int hash = 1;

        // Generate a unique hash function for Entity classes to support the Java Collections Framework.
        int seed = 31;
        hash = (hash * seed + this.getClass().hashCode()) % Integer.MAX_VALUE;
        hash = (hash * seed + (int)(this._id ^ (this._id >>> 32))) % Integer.MAX_VALUE;

        return hash;
    }

    @Override
    public String toString() {
        return "------------ ID " + this._id + " ------------\n";
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
