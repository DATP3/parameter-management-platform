package dk.nykredit.example.pmp;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class DatabaseInitializer {

    private static final int TCP_PORT = 7050;
    private static final int WEB_PORT = 7051;

    private static final Logger LOGGER = Log.getLogger(DatabaseInitializer.class);

    private Server tcp;
    private Server ws;

    @PostConstruct
    public void startDatabase() {
        try {
            tcp = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-ifNotExists", "-tcpPort", TCP_PORT + "");
            ws = Server.createWebServer("-webPort", WEB_PORT + "", "-webAllowOthers");

            LOGGER.info("Starting H2 TCP at port " + TCP_PORT);
            tcp.start();
            LOGGER.info("Starting H2 web at port " + WEB_PORT);
            ws.start();

            final DataSource ds = getDataSource();
            final Connection con = ds.getConnection();

            Database db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(con));
            db.setDatabaseChangeLogTableName("PMP_CHANGE_LOG");
            db.setDatabaseChangeLogLockTableName("PMP_CHANGE_LOG_LOCK");

            // Make sure that the database has the tables required for PMP
            final Liquibase liquibase = new Liquibase("liquibase/pmpChangelog.yml", new ClassLoaderResourceAccessor(), db);
            liquibase.update(new Contexts("default"));
        }
        catch (SQLException | LiquibaseException e) {
            throw new Error(e);
        }
    }

    private DataSource getDataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:tcp://localhost:" + TCP_PORT + "/mem:pmptest");
        ds.setUser("sa");
        ds.setPassword("sa");
        return ds;
    }


    public void stopDatabase() {
        LOGGER.info("Stopping H2 TCP");
        tcp.stop();
        LOGGER.info("Stopping H2 web");
        ws.stop();
    }
}
