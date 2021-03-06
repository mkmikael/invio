package bpmlab.invio.dao;

import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Livro;
import java.util.List;
import javax.persistence.Query;

public class LivroDAO extends GenericDAO<Livro> {


    public List<Livro> obterLivrosAtuais(Curriculo curriculo, String anoAtual, String anoLimite) {
        try {
            String consulta = "SELECT o FROM Livro o "
                    + "WHERE o.curriculo = :curriculo AND o.ano BETWEEN :anoLimite AND :anoAtual ORDER BY o.ano DESC";
            Query query = getEntityManager().createQuery(consulta);
            return query.setParameter("curriculo", curriculo).
                    setParameter("anoAtual", anoAtual).
                    setParameter("anoLimite", anoLimite).
                    getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }
    
    public List<Livro> obterLivrosPassados(Curriculo curriculo, String anoLimite) {
        try {
            String consulta = "SELECT o FROM Livro o "
                    + "where o.curriculo = :curriculo AND o.ano < :anoLimite ORDER BY o.ano desc";
            Query query = getEntityManager().createQuery(consulta);
            return query.setParameter("curriculo", curriculo).
                    setParameter("anoLimite", anoLimite).
                    getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }

    public List<Livro> obterLivros(Curriculo curriculo,
            boolean avaliado) {
        try {
            String consulta = "select o from Livro o "
                    + "where o.curriculo = :curriculo and "
                    + (avaliado ? "not(o.avaliacao is null) " : "o.avaliacao is null ")
                    + "ORDER BY o.ano desc";
            Query query = getEntityManager().createQuery(consulta);
            query.setParameter("curriculo", curriculo);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }

    public List<Livro> obterLivros2(Curriculo curriculo,
            boolean avaliado) {
        try {
            String consulta2 = "select o from Livro o where o.curriculo=" + curriculo.getId() + " ";
            if (avaliado == true) {
                consulta2 += "and NOT (o.avaliacao IS NULL)";
            } else {
                consulta2 += "and o.avaliacao IS NULL";
            }
            return getEntityManager().createQuery(consulta2).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            closeEntityManager();
        }
    }
}
