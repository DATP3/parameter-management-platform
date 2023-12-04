package dk.nykredit.pmp.core.database.setup;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class H2StartDatabase {

    private static H2StartupListenerSetup startupListener;

    @BeforeAll
    static void setupDatabase() throws LiquibaseException, SQLException {
        startupListener = new H2StartupListenerSetup();

        System.setProperty("dk.nykredit.pmp.h2.startservers", "true");
        System.setProperty("dk.nykredit.pmp.h2.tcpport", "7050");
        System.setProperty("dk.nykredit.pmp.h2.webport", "7051");
        startupListener.contextInitialized();

        DataSource ds = getDataSource();
        Connection con = ds.getConnection();

        Database db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(con));
        db.setDatabaseChangeLogTableName("PMP_CHANGE_LOG");
        db.setDatabaseChangeLogLockTableName("PMP_CHANGE_LOG_LOCK");
        Liquibase liquibase = new Liquibase("liquibase/pmpChangelog.yml", new ClassLoaderResourceAccessor(), db);
        liquibase.update(new Contexts("default"));
    }


    protected static DataSource getDataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:tcp://localhost:7050/mem:pmptest");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }

    @AfterAll
    static void closeDatabase() {
        startupListener.contextDestroyed();
    }
}
