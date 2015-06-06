package bpmlab.invio.dao;

import bpmlab.invio.entidade.Qualis;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Dir de Armas Marinha
 */
public class QualisDAO extends GenericDAO<Qualis> {

    public String obterEstrato(String titulo, String area) {
        try {
            String query = "SELECT q.estrato FROM Qualis q WHERE "
                    + "q.qualisPK.titulo = :titulo AND "
                    + "q.qualisPK.areaAvaliacao = :area";
            Query q = getEntityManager().createQuery(query);
            q.setParameter("titulo", titulo);
            q.setParameter("area", area);
            String resultado = (String) q.getSingleResult();
            return resultado.trim();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }

    public List<String> obterTodosTitulos(String palavra, int maxResultados) {
        try {
            String query = "SELECT distinct q.titulo FROM qualis q "
                    + "WHERE q.titulo like '%" + palavra + "%'"; //Acrescentei o % antes da palavra
            Query q = getEntityManager().createNativeQuery(query);
            q.setMaxResults(maxResultados);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }

    public List<String> obterPorArea(String area) {
        try {
            String query = "select distinct q.qualisPK.titulo from Qualis q WHERE q.qualisPK.areaAvaliacao = :area";
            Query q = getEntityManager().createQuery(query);
            q.setParameter("area", area);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }

    public List<String> obterTodosTitulosArea(String palavra, int maxResultados) {
        try {
            String query = "SELECT distinct q.areaAvaliacao FROM qualis q "
                    + "WHERE q.areaAvaliacao like '%" + palavra + "%'";
            Query q = getEntityManager().createNativeQuery(query);
            q.setMaxResults(maxResultados);
            return q.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }
}
