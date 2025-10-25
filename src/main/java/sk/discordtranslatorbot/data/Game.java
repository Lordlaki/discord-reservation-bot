package sk.discordtranslatorbot.data;

public class Game {

    private final String name;
    private String reservedBy;
    private String reservedById;
    private String steamLink;

    public Game(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public String getReservedBy() { return reservedBy; }
    public void setReservedBy(String reservedBy) { this.reservedBy = reservedBy; }

    public String getReservedById() { return reservedById; }
    public void setReservedById(String reservedById) { this.reservedById = reservedById; }

    public String getSteamLink() { return steamLink; }
    public void setSteamLink(String steamLink) { this.steamLink = steamLink; }

    // Nová metóda: či je hra rezervovaná
    public boolean isReserved() {
        return reservedBy != null && !reservedBy.isBlank();
    }
}
