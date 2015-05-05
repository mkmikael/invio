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
    private EntityManager manager;
    
    public EntityManager getEntityManager() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        if (manager == null || !manager.isOpen()) {
            manager = factory.createEntityManager();
        }
        return manager;
    }

    public void closeEntityManager() {
        if (manager != null && manager.isOpen()) {
            manager.close();
            manager = null;
        }
    }
    
    public static JpaUtil getInstance() {
        if (jpaUtil == null) {
            jpaUtil = new JpaUtil();
        }
        return jpaUtil;
    }

    public static void closeFactory() {
        if (factory != null || factory.isOpen()) {
            factory.close();
        }
    }
}
