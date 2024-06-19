package studio.ecoprojects.ecozero.discordintergration.database;

import org.jdbi.v3.core.Handle;
import studio.ecoprojects.ecozero.EcoZero;
import studio.ecoprojects.ecozero.utils.DataBaseSetUp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VerifiedDB {

    public static boolean DataBaseHasTable() {
        try {
            ResultSet set = DataBaseSetUp.jdbi.open().getConnection().
                    getMetaData().getTables(null, null, null, new String[] {"TABLE"});
            while (set.next()) {
                String name = set.getString("TABLE_NAME");
                if (name.equalsIgnoreCase("verified")) return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public static void CreateVerifiedTable() {
        try (Handle handle = DataBaseSetUp.jdbi.open()) {
            handle.execute("CREATE TABLE verified (uuid varchar(255), discord_id varchar(255))");;
            EcoZero.logger.info("Created 'verified' table.");
        }
    }

    public static void AddVerified(String Uuid, String UserID) {
        try (Handle handle = DataBaseSetUp.jdbi.open()) {
            handle.execute("INSERT INTO verified (uuid, discord_id) VALUES ('" + Uuid + "', '" + UserID + "')", new Object[0]);
        }
    }

    public static void RemoveVerifiedByUUID(String Uuid) {
        try (Handle handle = DataBaseSetUp.jdbi.open()) {
            handle.execute("DELETE FROM verified WHERE uuid='" + Uuid + "'", new Object[0]);
        }
    }

    public static void RemoveVerifiedByUserID(String UserID) {
        try (Handle handle = DataBaseSetUp.jdbi.open()) {
            handle.execute("DELETE FROM verified WHERE discord_id='" + UserID + "'", new Object[0]);
        }
    }

    public static Optional<String> getUserID(String Uuid) {

        try (Handle handle = DataBaseSetUp.jdbi.open()) {
            return handle.createQuery("SELECT discord_id FROM verified WHERE uuid='" + Uuid + "'").mapTo(String.class).findOne();
        }
    }

    public static Optional<String> getUuid(String userID) {
        try (Handle handle = DataBaseSetUp.jdbi.open()) {
            return handle.createQuery("SELECT uuid FROM verified WHERE discord_id='" + userID + "'").mapTo(String.class).findOne();
        }
    }

    public static List<UUID> getVerifiedUUIDS() {
        try (Handle handle = DataBaseSetUp.jdbi.open()) {

            return handle.createQuery("SELECT uuid FROM verified").mapTo(UUID.class).collectIntoList();
        }
    }

}