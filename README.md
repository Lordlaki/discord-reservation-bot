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

### 3️⃣ Build projektu
Pomocou Maven:

```bash
mvn clean install
```

### 4️⃣ Spustenie bota

```bash
java -jar target/discord-reservation-bot-1.0.0.jar
```

