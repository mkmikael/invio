package bpmlab.invio.dao;

import bpmlab.invio.entidade.Area;
import bpmlab.invio.entidade.Instituicao;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Dir de Armas Marinha
 */
public class AreaDAO extends GenericDAO<Area> {

    public List<Area> obterAreaPorCriterio(String digitacao) {
        try {
            String consulta = "select o from Area o where o.nome like'%" + digitacao + "%'";
            return getEntityManager().createQuery(consulta).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }
    
    public List<Area> obterAreaOrdenada() {
        try {
            String consulta = "select a from Area a order by a.nome";
            return getEntityManager().createQuery(consulta).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }
    
    public List<Area> obterAreas(Instituicao instituicao) {
        try {
            String consulta = "select distinct p.area from Programa p "
                    + "where p.instituicao = :instituicao";
            Query query = getEntityManager().createQuery(consulta);
            query.setParameter("instituicao", instituicao);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }
    
}