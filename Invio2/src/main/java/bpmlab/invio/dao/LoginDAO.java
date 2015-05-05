package bpmlab.invio.dao;

import bpmlab.invio.entidade.Login;
import java.util.logging.Level;
import javax.persistence.Query;

/**
 *
 * @author Renan
 */
public class LoginDAO extends GenericDAO<Login> {

    public Login obter(String email) {
        try {
            String query = "SELECT l FROM Login l WHERE l.email = :email";
            Query q = super.getEntityManager().createQuery(query);
            q.setParameter("email", email);
            LOG.log(Level.INFO, "BUSCAR EMAIL: {0}", email);
            return (Login) q.getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "BUSCAR EMAIL: " + email, e);
            return null;
        } finally {
            closeEntityManager();
        }
    }

    public boolean existe(String email) {
        try {
            String query = "SELECT l FROM Login l WHERE l.email = :email";
            Query q = super.getEntityManager().createQuery(query);
            q.setParameter("email", email);
            return ((Login) q.getSingleResult()).getEmail().equalsIgnoreCase(email);
        } catch (Exception e) {
            return false;
        } finally {
            closeEntityManager();
        }

    }

}
