package dk.nykredit.example.pmp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerProducer {
    @Produces
    @Named("parameterPersistenceUnit")
    public EntityManager produceEntityManager() {
        // Create an EntityManager for the persistence unit defined in
        // resources/META-INf/persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnitTest");
        return emf.createEntityManager();
    }
}
