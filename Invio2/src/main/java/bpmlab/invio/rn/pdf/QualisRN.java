package bpmlab.invio.rn.pdf;

import bpmlab.invio.dao.QualisDAO;
import bpmlab.invio.entidade.Qualis;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityExistsException;

/**
 *
 * @author Junior
 */
public class QualisRN {
    
    private static final Logger LOG = Logger.getLogger(QualisRN.class.getName());
    QualisDAO dao = new QualisDAO();

    public boolean salvar(Qualis qualis) {
        if (qualis.getQualisPK() == null) {
            return dao.criar(qualis);
        } else {
            return dao.alterar(qualis);
        }
    }

    public boolean remover(Qualis qualis) {
        return dao.excluir(qualis);
    }

    public Qualis obter(Integer id) {
        return dao.obter(Qualis.class, id);
    }

    public List<Qualis> obterTodos() {
        return dao.obterTodos(Qualis.class);
    }

    public List<String> obterTodosTitulos(String query) {
        return dao.obterTodosTitulos(query, 20);
    }

    public List<String> obterTodosTitulosArea(String query) {
        return dao.obterTodosTitulosArea(query, 20);
    }

    public int obterEstrato(String titulo, String area) {
        LOG.log(Level.INFO, "Titulo: {0}; Area: {1}", new Object[]{titulo, area});
        String estrato = dao.obterEstrato(titulo, area);

        estrato = estrato.trim();
        System.out.println("estrato" + estrato);

        if (estrato.equalsIgnoreCase("A1")) {
            return 50;
        } else if (estrato.equalsIgnoreCase("A2")) {
            return 45;
        } else if (estrato.equalsIgnoreCase("B1")) {
            return 40;
        } else if (estrato.equalsIgnoreCase("B2")) {
            return 30;
        } else if (estrato.equalsIgnoreCase("B3")) {
            return 25;
        } else if (estrato.equalsIgnoreCase("B4")) {
            return 20;
        } else if (estrato.equalsIgnoreCase("B5")) {
            return 10;
        } else if (estrato.equalsIgnoreCase("C")) {
            return 5;
        }

        return 0;
    }
}
