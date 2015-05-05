package bpmlab.invio.dao;

import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Relatorio;
import java.util.List;
import javax.persistence.Query;

public class RelatorioDAO extends GenericDAO<Relatorio> {

    private final GenericDAO<Relatorio> genericDAO = new GenericDAO<Relatorio>();

    public List<Relatorio> obterRelatorios(Curriculo curriculo) {
        String consulta = "select o from Relatorio o "
                + "where o.curriculo = :curriculo ORDER BY o.mes desc";
        try {
            Query query = getEntityManager().createQuery(consulta);
            query.setParameter("curriculo", curriculo);
            List<Relatorio> relatorios = query.getResultList();
            return relatorios;
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }
}
