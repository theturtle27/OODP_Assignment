package Model.Room;

public class Room {
    int roonID;
    String type;
    Double rate;
    String bedType;
    boolean wifiEnabled;
    String facing;
    boolean smoking;
    String status;

    public Room(int roonID, String type, Double rate, String bedType, boolean wifiEnabled, String facing, boolean smoking, String status) {
        this.roonID = roonID;
        this.type = type;
        this.rate = rate;
        this.bedType = bedType;
        this.wifiEnabled = wifiEnabled;
        this.facing = facing;
        this.smoking = smoking;
        this.status = status;
    }

    public int getRoonID() {
        return roonID;
    }

    public void setRoonID(int roonID) {
        this.roonID = roonID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public boolean isWifiEnabled() {
        return wifiEnabled;
    }

    public void setWifiEnabled(boolean wifiEnabled) {
        this.wifiEnabled = wifiEnabled;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
