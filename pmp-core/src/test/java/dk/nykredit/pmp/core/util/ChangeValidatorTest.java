package dk.nykredit.pmp.core.util;

/* import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dk.nykredit.pmp.core.audit_log.AuditLog;
import dk.nykredit.pmp.core.commit.CommitRevert;
import dk.nykredit.pmp.core.commit.ParameterChange;
import dk.nykredit.pmp.core.commit.ParameterRevert;
import dk.nykredit.pmp.core.commit.Service;
 */
public class ChangeValidatorTest {

    /* private static ChangeValidator changeValidator;
    private static String pmpRoot;
    private static ServiceInfo serviceInfo;
    private WeldContainer container;
    private AuditLog auditLog;

    @BeforeAll
    public static void beforeAll() {
        serviceInfo = new ServiceInfo();
        serviceInfo.setPmpRoot(pmpRoot);

        changeValidator = new ChangeValidator(serviceInfo);
        
    }

    @BeforeEach
    public void before() {
        Weld weld = new Weld();
        container = weld.initialize();
        auditLog = container.select(AuditLog.class).get();
        changeValidator.getValidatedChanges().clear();
    }

    @AfterEach
    public void after() {
        container.shutdown();
        changeValidator.getValidatedChanges().clear();
    }

    @Test
    public void testVisitParameterChange() {
        ParameterChange change = createServiceFreeParamChange();

        change.setService(new Service("test", pmpRoot, "env"));
        
        change.visit(changeValidator);
        assertEquals(1, changeValidator.getValidatedChanges().size());
    }
    
    @Test
    public void testVisitParameterChangeWithWrongPmpRoot() {
        ParameterChange parameterChange = createServiceFreeParamChange();

        parameterChange.setService(new Service("test", "wrongPmpRoot", "env"));

        parameterChange.visit(changeValidator);
        assertEquals(0, changeValidator.getValidatedChanges().size());
    }

    @Test
    public void testVisitParameterRevert() {
        ParameterRevert parameterRevert = createServiceFreeParamRevert();

        parameterRevert.setService(new Service("test", pmpRoot, "env"));

        parameterRevert.visit(changeValidator);
        assertEquals(1, changeValidator.getValidatedChanges().size());
    }

    @Test
    public void testVisitParameterRevertWithWrongPmpRoot() {
        ParameterRevert parameterRevert = createServiceFreeParamRevert();

        parameterRevert.setService(new Service("test", "wrongPmpRoot", "env"));

        parameterRevert.visit(changeValidator);
        assertEquals(0, changeValidator.getValidatedChanges().size());
    }

    @Test
    public void testVisitCommitRevert() {

        // TODO: finish this test by adding a commit to the audit log and then create a commit revert with the commit hash.

        // assertEquals(1, changeValidator.getValidatedChanges().size());
    }

    @Test
    public void testVisitCommitRevertWithUnknownCommitHash() {
        CommitRevert commitRevert = new CommitRevert();

        commitRevert.visit(changeValidator);
        assertEquals(0, changeValidator.getValidatedChanges().size());
    }

    private ParameterChange createServiceFreeParamChange() {
        ParameterChange change = new ParameterChange();
        change.setName("name");
        change.setType("type");
        change.setOldValue("oldValue");
        change.setNewValue("newValue");
        change.setId("id");
        return change;
    }

    private ParameterRevert createServiceFreeParamRevert() {
        ParameterRevert revert = new ParameterRevert();
        revert.setCommitHash(10);
        revert.setParameterName("testParamName");
        revert.setRevertType("parameterRevert");
        return revert;
    } */
}
