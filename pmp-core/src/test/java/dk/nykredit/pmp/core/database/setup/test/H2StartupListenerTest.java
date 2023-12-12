package dk.nykredit.pmp.core.database.setup.test;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import dk.nykredit.pmp.core.database.setup.H2StartupListenerSetup;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

/**
 * Test of {@link H2StartupListenerSetup}
 */
class H2StartupListenerTest {

    private H2StartupListenerSetup startupListener = new H2StartupListenerSetup();

    private final TestLogger logger = TestLoggerFactory.getTestLogger(H2StartupListenerSetup.class);

    @Test
    @Disabled
    void tcpStartup() throws Exception {
        System.setProperty("dk.nykredit.pmp.h2.startservers", "false");
        startupListener.contextInitialized();
        Assertions.assertTrue(logger.getAllLoggingEvents().isEmpty());

        System.setProperty("dk.nykredit.pmp.h2.startservers", "true");
        System.setProperty("dk.nykredit.pmp.h2.tcpport", "7050");
        System.setProperty("dk.nykredit.pmp.h2.webport", "7051");

        startupListener.contextInitialized();

        DataSource ds = getDataSource();
        Connection con = ds.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select count(*) from information_schema.tables");

        rs.next();
        long tableCount = rs.getLong(1);
        //Assertions.assertEquals(38, tableCount);


        runAndVerifyLiquibase(con, st);

        con.close();


    }

    private void runAndVerifyLiquibase(Connection con, Statement st) throws LiquibaseException, SQLException {
        Database db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(con));
        db.setDatabaseChangeLogTableName("PMP_CHANGE_LOG");
        db.setDatabaseChangeLogLockTableName("PMP_CHANGE_LOG_LOCK");
        Liquibase liquibase = new Liquibase("liquibase/pmpChangelog.yml", new ClassLoaderResourceAccessor(), db);
        liquibase.update(new Contexts("default"));


        ResultSet stmt = st.executeQuery("SHOW TABLES");
        List<String> tableNames = new ArrayList<>();
        while (stmt.next()) {
            tableNames.add(stmt.getString(1));
        }
        Assertions.assertTrue(tableNames.stream().anyMatch(table -> table.equals("PARAMETER_MANAGEMENT")));
    }

    private static DataSource getDataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:tcp://localhost:7050/mem:pmptest");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }

}
