package de.lunatig.faibot.utils;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.lunatig.faibot.data.ExternalReference;
import de.lunatig.faibot.data.Placement;
import de.lunatig.faibot.data.Season;
import de.lunatig.faibot.data.TwitchUser;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class DatabaseManager {

    @Getter
    private final ConnectionSource connectionSource;

    public DatabaseManager(Dotenv env) throws SQLException {
        String url = env.get("DB_URL");
        String username = env.get("DB_USER");
        String password = env.get("DB_PASSWORD");

        JdbcPooledConnectionSource pooledSource = new JdbcPooledConnectionSource(url, username, password);

        pooledSource.setCheckConnectionsEveryMillis(1000 * 60);
        pooledSource.setMaxConnectionsFree(5);
        pooledSource.setMaxConnectionAgeMillis(1000 * 60 * 60);

        this.connectionSource = pooledSource;

        TableUtils.createTableIfNotExists(connectionSource, Placement.class);
        TableUtils.createTableIfNotExists(connectionSource, Season.class);
        TableUtils.createTableIfNotExists(connectionSource, TwitchUser.class);
        TableUtils.createTableIfNotExists(connectionSource, ExternalReference.class);
    }

    public void close() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (Exception e) {
                log.error("Error closing database connection: ", e);
            }
        }
    }
}