package bpmlab.invio.dao;

import bpmlab.invio.entidade.Area;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;

public class GenericDAO<T> implements Serializable {

    protected static final Logger LOG = Logger.getLogger(GenericDAO.class.getName());

    public GenericDAO() {
    }

    public boolean salvar(T o) {
        if (getId(o) == null) {
            return criar(o);
        } else {
            return alterar(o);
        }
    }

    public boolean criar(T o) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(o);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ERRO NA CAMADA DAO", e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            closeEntityManager();
        }
    }

    public boolean excluir(T o) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.merge(o));
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ERRO NA CAMADA DAO", e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            closeEntityManager();
        }
    }

    public boolean alterar(T o) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(o);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ERRO NA CAMADA DAO", e);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        } finally {
            closeEntityManager();
        }
    }

    public T obter(Class<T> classe, Object id) {
        EntityManager em = getEntityManager();
        String query = classe.getSimpleName() + ".findById";
        final Query q = em.createNamedQuery(query);
        closeEntityManager();
        return (T) q.setParameter("id", id).getSingleResult();
    }

    public List<T> obterTodos(Class<T> classe) {
        EntityManager em = getEntityManager();
        String query = classe.getSimpleName() + ".findAll";
        Query q = em.createNamedQuery(query);
        closeEntityManager();
        return q.getResultList();
    }

    /**
     * @return the entityManager
     */
    protected EntityManager getEntityManager() {
        return JpaUtil.getInstance().getEntityManager();
    }

    protected void closeEntityManager() {
        JpaUtil.getInstance().closeEntityManager();
    }

    private static Object getId(Object o) {
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Id.class)) {
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                try {
                    return declaredField.get(o);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "ERRO DE REFLECÇÃO AO BUSCAR ID", e);
                    return null;
                }
            }
        }
        return null;
    }

}
