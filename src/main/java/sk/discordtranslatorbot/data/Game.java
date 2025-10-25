package sk.discordtranslatorbot.data;

public class Game {
    private final String name;
    private String reservedBy;
    private String reservedById;

    public Game(String name) {
        this.name = name;
        this.reservedBy = null;
        this.reservedById = null;
    }

    public String getName() {
        return name;
    }

    public boolean isReserved() {
        return reservedBy != null;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public String getReservedById() {
        return reservedById;
    }

    public void setReservedBy(String user) {
        this.reservedBy = user;
    }

    public void setReservedById(String id) {
        this.reservedById = id;
    }
}

