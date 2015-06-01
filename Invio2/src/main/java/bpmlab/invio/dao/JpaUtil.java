/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author bpmlab
 */
public class JpaUtil {

    private static final String PERSISTENCE_UNIT = "bpmlab_Invio2_war_1.0-SNAPSHOTPU";
    private static EntityManagerFactory factory;
    private static JpaUtil jpaUtil;
    private EntityManager entityManager;

    private JpaUtil() {
    }
    
    public EntityManagerFactory getEntityManagerFactory() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return factory;
    }

    public EntityManager getEntityManager() {
        entityManager = factory.createEntityManager();
        return entityManager;
    }

    public void closeEntityManager() {
        if (entityManager != null || !entityManager.isOpen()) {
            entityManager.close();
        }
    }

    public static JpaUtil getInstance() {
        if (jpaUtil == null) {
            jpaUtil = new JpaUtil();
        }
        return jpaUtil;
    }
}
