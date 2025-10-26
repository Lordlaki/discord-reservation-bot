package sk.discordtranslatorbot.data;

public class Game {

    private final String name;

    // ---------------------------
    // CZ rezervácia
    // ---------------------------
    private String reservedByCz;
    private String reservedByIdCz;

    // ---------------------------
    // SK rezervácia
    // ---------------------------
    private String reservedBySk;
    private String reservedByIdSk;

    // ---------------------------
    // Spoločné dáta
    // ---------------------------
    private String steamLink;
    private String czechVersion;
    private String slovakVersion;
    private boolean completed;

    public Game(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    // ---------------------------
    // CZ
    // ---------------------------
    public String getReservedByCz() { return reservedByCz; }
    public void setReservedByCz(String reservedByCz) { this.reservedByCz = reservedByCz; }

    public String getReservedByIdCz() { return reservedByIdCz; }
    public void setReservedByIdCz(String reservedByIdCz) { this.reservedByIdCz = reservedByIdCz; }

    // ---------------------------
    // SK
    // ---------------------------
    public String getReservedBySk() { return reservedBySk; }
    public void setReservedBySk(String reservedBySk) { this.reservedBySk = reservedBySk; }

    public String getReservedByIdSk() { return reservedByIdSk; }
    public void setReservedByIdSk(String reservedByIdSk) { this.reservedByIdSk = reservedByIdSk; }

    // ---------------------------
    // Spoločné
    // ---------------------------
    public String getSteamLink() { return steamLink; }
    public void setSteamLink(String steamLink) { this.steamLink = steamLink; }

    public String getCzechVersion() { return czechVersion; }
    public void setCzechVersion(String czechVersion) { this.czechVersion = czechVersion; }

    public String getSlovakVersion() { return slovakVersion; }
    public void setSlovakVersion(String slovakVersion) { this.slovakVersion = slovakVersion; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    // ---------------------------
    // Logika rezervácie
    // ---------------------------

    public boolean isReserved(String lang) {
        switch (lang.toLowerCase()) {
            case "cz": return reservedByCz != null && !reservedByCz.isBlank();
            case "sk": return reservedBySk != null && !reservedBySk.isBlank();
            case "both":
                return (reservedByCz != null && !reservedByCz.isBlank())
                        || (reservedBySk != null && !reservedBySk.isBlank());
            default: return false;
        }
    }

    public boolean isReserved() {
        return isReserved("cz") || isReserved("sk");
    }

    @Override
    public String toString() {
        return String.format(
                "Game{name='%s', CZ='%s', SK='%s', completed=%s}",
                name,
                reservedByCz != null ? reservedByCz : "-",
                reservedBySk != null ? reservedBySk : "-",
                completed
        );
    }
}
