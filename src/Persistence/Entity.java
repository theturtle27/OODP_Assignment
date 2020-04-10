package Persistence;


public abstract class Entity {
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

}
