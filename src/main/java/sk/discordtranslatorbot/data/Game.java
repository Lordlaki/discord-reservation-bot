package sk.discordtranslatorbot.data;

public class Game {

    private final String name;
    private String reservedBy;
    private String reservedById;
    private String steamLink;

    private String czechVersion;  // stĺpec E
    private String slovakVersion; // stĺpec F
    private boolean completed;    // stĺpec G

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

    public boolean isReserved() { return reservedBy != null && !reservedBy.isBlank(); }

    public String getCzechVersion() { return czechVersion; }
    public void setCzechVersion(String czechVersion) { this.czechVersion = czechVersion; }

    public String getSlovakVersion() { return slovakVersion; }
    public void setSlovakVersion(String slovakVersion) { this.slovakVersion = slovakVersion; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
