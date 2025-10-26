# 🤖 Discord Reservation Bot

**Jednoduchý Discord bot pre prekladateľov hier (CZ/SK), ktorý umožňuje rezerváciu hier, správu stavu a synchronizáciu s Google Sheets.**

---

## ✨ Funkcie

- 🕹️ Rezervácia hier pre prekladateľov (CZ / SK)
- 📋 Zoznam všetkých hier a ich prekladateľov
- 🔗 Odkazy na Steam / projekty
- ☁️ Synchronizácia medzi **Google Sheets** a **lokálnym JSON súborom**
- 🔒 Bezpečné uloženie tokenov a prístupov
- 🔄 Automatické aktualizácie a záloha dát

---

## 🧰 Použité technológie

- **Java 17+**
- **Maven**
- **JDA 5 (Java Discord API)**
- **Google Sheets API**
- **Gson** (JSON serialization)
- **SLF4J** (logging)

---

## ⚙️ Inštalácia a spustenie

### 1️⃣ Klonovanie projektu

```bash
git clone https://github.com/<tvoj-username>/discord-reservation-bot.git
cd discord-reservation-bot
```

---

### 2️⃣ Nastavenie konfigurácie
🔑 Súbor config.properties

Umiestni do src/main/resources/config.properties

```properties
DISCORD_TOKEN=<tvoj_discord_bot_token>
SHEET_ID=<id_google_sheets_tabulky>
```
📄 Súbor credentials.json

Vygeneruj cez Google Cloud Console → Service Account → Keys → JSON
(pozor – nesdieľaj tento súbor verejne)

---

### 3️⃣ Build projektu
Pomocou Maven:

```bash
mvn clean install
```

---

### 4️⃣ Spustenie bota

```bash
java -jar target/discord-reservation-bot-1.0.0.jar
```

✅ Ak všetko prebehlo správne, v konzole sa zobrazí:

```bash
✅ Bot beží...
```

---

##🧠 Príkazy

| Príkaz | Popis |
|:--------|:------|
| `!pridaj <názov>` | Pridá novú hru do zoznamu |
| `!rezervuj <názov>` | Rezervuje hru pre používateľa |
| `!zrus <názov>` | Zruší tvoju rezerváciu |
| `!zoznam` | Zobrazí všetky hry |
| `!info <názov>` | Zobrazí detaily o hre |
| `!complet <názov>` | Označí hru ako dokončenú |
| `!commands` | Zobrazí všetky dostupné príkazy |

---

##🧩 Architektúra

```css
discord-reservation-bot/
│
├── src/
│   └── main/java/sk/discordtranslatorbot/
│       ├── Main.java
│       ├── bot/
│       │   └── ReservationBot.java
│       ├── commands/
│       │   ├── Command.java
│       │   ├── CommandRegistry.java
│       │   └── impl/
│       │       ├── AddGameCommand.java
│       │       ├── ListGamesCommand.java
│       │       ├── ReserveGameCommand.java
│       │       ├── CancelReservationCommand.java
│       │       └── InfoCommand.java
│       └── data/
│           ├── GameStorage.java
│           ├── GoogleSheetsStorage.java
│           ├── HybridStorage.java
│           └── Game.java
│
├── resources/
│   ├── config.properties
│   └── credentials.json
│
├── pom.xml
└── games.json
```

---

##☁️ Google Sheets prepojenie

Bot číta a zapisuje údaje do Google Sheets pomocou API.

Postup nastavenia:
1. Choď na [Google Cloud Console](https://console.cloud.google.com/)
2. Vytvor nový projekt
3. Aktivuj Google Sheets API
4. V časti APIs & Services → Credentials → Create credentials → Service Account
    -->Vygeneruj JSON kľúč (credentials.json)
5. V Google Sheets klikni na Zdieľať
    -->Pridaj e-mail z poľa client_email v credentials.json ako editor



##🧠 HybridStorage

Bot používa HybridStorage, ktorý kombinuje lokálny JSON a vzdialený Google Sheet:

| Operácia        | Lokálny JSON | Google Sheets          |
|:----------------|:------------:|:---------------------:|
| Pridanie hry    | ✅           | ✅                     |
| Čítanie zoznamu | ✅           | ✅                     |
| Rezervácia      | ✅           | ✅                     |
| Záloha dát      | ✅           | 🔁 Synchronizácia      |

---

##⚠️ Riešenie problémov

| Chyba | Možná príčina | Riešenie |
|:------------------------------|:------------------------|:---------------------------------------------|
| `package com.google.api.client... does not exist` | Chýba Google API knižnica | Spusti `mvn clean install -U` |
| Unresolved dependency | Maven cache | Vymaž `.m2/repository` alebo použi `-U` |
| Unauthorized 403 | Google Sheet nie je zdieľaný | Zdieľaj ho s `client_email` |
| DISCORD_TOKEN missing | Nenastavený token | Uprav `config.properties` alebo environment premennú |


---

##💬 Príklad použitia

```yaml
!pridaj Baldur’s Gate 3
✅ Hra "Baldur’s Gate 3" bola pridaná do zoznamu.

!rezervuj Baldur’s Gate 3
✅ Hra "Baldur’s Gate 3" bola rezervovaná pre @Haku (CZ).

!zoznam
🎮 Zoznam hier:
1️⃣ Baldur’s Gate 3 — CZ: Haku — SK: (voľné)
```

---

##🧾 POM závislosti

Najdôležitejšie knižnice v pom.xml:

```xml
<dependencies>
    <dependency>
        <groupId>net.dv8tion</groupId>
        <artifactId>JDA</artifactId>
        <version>5.0.0-beta.24</version>
    </dependency>

    <dependency>
        <groupId>com.google.api-client</groupId>
        <artifactId>google-api-client</artifactId>
        <version>2.2.0</version>
    </dependency>

    <dependency>
        <groupId>com.google.apis</groupId>
        <artifactId>google-api-services-sheets</artifactId>
        <version>v4-rev20230815-2.0.0</version>
    </dependency>

    <dependency>
        <groupId>com.google.auth</groupId>
        <artifactId>google-auth-library-oauth2-http</artifactId>
        <version>1.23.0</version>
    </dependency>

    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
</dependencies>
```

---

##📜 Licencia

MIT License © 2025 — Lukas M "Haku"

Tento projekt je open-source.
Použitie, úprava a distribúcia sú povolené s uvedením autora.

---

##🧑‍💻 Autor

Lukas M "Haku"
🎮 Discord Localization Tools Developer
🌐 [GitHub – LukasHaku](https://github.com/)

📅 2025
