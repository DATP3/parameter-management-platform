package dk.nykredit.pmp.core.database.tests;

import dk.nykredit.pmp.core.ServiceConcept;
import dk.nykredit.pmp.core.database.setup.H2StartDatabase;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class PMPFullTest extends H2StartDatabase {

	private WeldContainer container;
	private ServiceConcept serviceConcept;

	@BeforeEach
	private void before() {
		Weld weld = new Weld();
		container = weld.initialize();
		serviceConcept = container.select(ServiceConcept.class).get();
	}

	@AfterEach
	private void after() {
		container.shutdown();
	}

	@Test
	// @Disabled("Transaction not working")
	void runMainTest() {
		serviceConcept.run();
	}

	@Test
	@Disabled("Transaction not working")
	void runErrorTest() {
		serviceConcept.runError();
	}
}
