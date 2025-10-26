package sk.discordtranslatorbot.data;

public class Game {

    private final String name;
    private String reservedBy;
    private String reservedById;
    private String steamLink;

    // 🔹 nové polia pre complet
    private String czechVersion;
    private boolean completed;

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

    public boolean isReserved() {
        return reservedBy != null && !reservedBy.isBlank();
    }

    // 🔹 verzia češtiny
    public String getCzechVersion() { return czechVersion; }
    public void setCzechVersion(String czechVersion) { this.czechVersion = czechVersion; }

    // 🔹 stav dokončenia
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
