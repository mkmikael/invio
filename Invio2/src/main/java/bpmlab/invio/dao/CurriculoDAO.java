package bpmlab.invio.dao;

import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Login;
import java.util.List;

/**
 *
 * @author Dir de Armas Marinha
 */
public class CurriculoDAO extends GenericDAO<Curriculo> {

    public List<Curriculo> findCurriculoByUsuario(Login loginLogado) {
        try {
            String consulta = "select o.curriculo from Login o where o.id=" + loginLogado.getId();
            return getEntityManager().createQuery(consulta).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }
    
    public List<Curriculo> obterTodosOrdenado() {
        try {
            String consulta = "select c from Curriculo c order by c.fco desc";
            return getEntityManager().createQuery(consulta).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }
     
}