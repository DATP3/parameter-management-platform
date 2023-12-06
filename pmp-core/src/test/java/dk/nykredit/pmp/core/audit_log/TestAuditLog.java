package dk.nykredit.pmp.core.audit_log;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dk.nykredit.pmp.core.commit.Change;
import dk.nykredit.pmp.core.commit.Commit;
import dk.nykredit.pmp.core.commit.CommitDirector;
import dk.nykredit.pmp.core.commit.CommitRevert;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.database.setup.H2StartDatabase;
import dk.nykredit.pmp.core.service.ParameterService;

public class TestAuditLog extends H2StartDatabase {

    private WeldContainer container;
    private ParameterService parameterService;
    private AuditLog auditLog;
    private CommitDirector commitDirector;

    @BeforeEach
    public void before() {
        Weld weld = new Weld();
        container = weld.initialize();
        commitDirector = container.select(CommitDirector.class).get();
        parameterService = container.select(ParameterService.class).get();
        parameterService.persistParameter("test1", "data1");
        parameterService.persistParameter("test2", 5);

        auditLog = container.select(AuditLog.class).get();
    }

    @AfterEach
    public void after() {
        container.shutdown();
    }

    @Test
    void testLogCommit() {

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setMessage("test commit");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        Change c1 = new ParameterChange("test1", "String", "data1", "data2");
        Change c2 = new ParameterChange("test2", "Integer", "5", "10");

        List<Change> changes = new ArrayList<>();
        changes.add(c1);
        changes.add(c2);
        commit.setChanges(changes);

        commitDirector.apply(commit);

        Commit commit2 = auditLog.getCommit(commit.getCommitHash());

        assertEquals(commit, commit2);
    }

    @Test
    void testLogCommitRevert() {
        ParameterService parameterService = commitDirector.getParameterService();
        parameterService.getRepository().startTransaction();

        Commit commit = new Commit();
        commit.setUser("author");
        commit.setMessage("test commit");
        commit.setPushDate(LocalDateTime.now());
        commit.setAffectedServices(List.of("service1"));

        Change c1 = new ParameterChange("test1", "String", "data1", "data2");
        Change c2 = new ParameterChange("test2", "Integer", "5", "10");

        List<Change> changes = new ArrayList<>();
        changes.add(c1);
        changes.add(c2);
        commit.setChanges(changes);

        commitDirector.apply(commit);

        CommitRevert commitRevert = new CommitRevert();
        commitRevert.setCommitHash(commit.getCommitHash());

        List<Change> changes2 = new ArrayList<>();
        changes2.add(commitRevert);

        Commit commit2 = new Commit();
        commit2.setChanges(changes2);
        commit2.setPushDate(LocalDateTime.now());
        commit2.setUser("user 1");
        commit2.setMessage("revert commit 1");
        commit2.setAffectedServices(List.of("service1"));

        commitDirector.apply(commit2);

        Commit commit3 = auditLog.getCommit(commit2.getCommitHash());

        assertEquals(commit2.getAppliedChanges(), commit3.getChanges());

        parameterService.getRepository().endTransaction();
    }
}
