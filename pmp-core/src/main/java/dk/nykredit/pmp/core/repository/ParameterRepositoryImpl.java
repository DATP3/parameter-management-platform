package dk.nykredit.pmp.core.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import dk.nykredit.pmp.core.persistence.ParameterEntity;

@ApplicationScoped
@Transactional
public class ParameterRepositoryImpl implements ParameterRepository {

    @Inject
    private EntityManager em;

    @Override
    public ParameterEntity getValueByName(String name) {
        TypedQuery<ParameterEntity> query = em
                .createQuery("SELECT e FROM ParameterEntity e where e.name = :parameterName", ParameterEntity.class);
        query.setParameter("parameterName", name);

        List<ParameterEntity> parameterValue = query.getResultList();
        if (parameterValue.isEmpty()) {
            // This will cause a null pointer exception
            return null;
        }
        return parameterValue.get(0);
    }

    @Override
    public ParameterEntity persistParameterEntity(ParameterEntity entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }

    @Override
    public boolean checkIfParameterExists(String name) {
        Query query = em.createNativeQuery("SELECT EXISTS (SELECT 1 FROM PARAMETER_MANAGEMENT WHERE NAME = ?)");
        query.setParameter(1, name);
        return (Boolean) query.getSingleResult();
    }

}
