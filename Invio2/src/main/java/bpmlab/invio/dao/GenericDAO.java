package bpmlab.invio.dao;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class GenericDAO<T> implements InterfaceDAO<T>, Serializable {

    protected static final Logger LOG = Logger.getLogger(GenericDAO.class.getName());

    public GenericDAO() {
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public T obter(Class<T> classe, Object id) {
        EntityManager em = getEntityManager();
        if (id == null) {
            return null;
        }
        String query = classe.getSimpleName() + ".findById";
        final Query q = em.createNamedQuery(query);
        try {
            return (T) q.setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }

    @Override
    public List<T> obterTodos(Class<T> classe) {
        EntityManager em = getEntityManager();
        String query = classe.getSimpleName() + ".findAll";
        Query q = em.createNamedQuery(query);
        try {
            return (List<T>) q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
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
}
