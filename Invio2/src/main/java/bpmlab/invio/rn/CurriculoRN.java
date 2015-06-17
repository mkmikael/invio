/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.rn;

import bpmlab.invio.dao.CurriculoDAO;
import bpmlab.invio.dao.GenericDAO;
import bpmlab.invio.entidade.Area;
import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Livro;
import bpmlab.invio.entidade.Login;
import bpmlab.invio.entidade.Orientacao;
import bpmlab.invio.entidade.Periodico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dir de Armas Marinha
 */
public class CurriculoRN implements Serializable {

    private GenericDAO<Curriculo> dao = new GenericDAO<Curriculo>();
    private CurriculoDAO curriculoDAO = new CurriculoDAO();
    private GenericDAO<Area> daoArea = new GenericDAO<Area>();
    private GenericDAO<Login> daoLogin = new GenericDAO<Login>();

    public boolean salvar(Curriculo c) {
        boolean salvou = false;
        if ("Doutorado".equals(c.getTitulacao())) {
            c.setExtrato(30);
        } else if ("Mestrado".equals(c.getTitulacao())) {
            c.setExtrato(15);
        } else {
            c.setExtrato(0);
        }
        if (c.getId() == null || c.getId().equals(0)) {
            c.setStatus("Não Avaliado");
            c.setId(null);
            c.setFco(0);
            salvou = dao.criar(c);
        } else {
            if (dao.alterar(c)) {
                salvou = true;
            }
        }
        return salvou;
    }

    public boolean remover(Curriculo c) {
        return dao.excluir(c);
    }

    public Curriculo obter(Integer id) {
        return dao.obter(Curriculo.class, id);
    }

    public List<Curriculo> obterTodos() {
        return dao.obterTodos(Curriculo.class);
    }

    //Aqui se obtém uma lista de Curriculo usuário por Login (Se Master ou não)
    public List<Curriculo> obterCurriculoLogin(Login login) {
        return curriculoDAO.findCurriculoByUsuario(login);
    }

    public List<Curriculo> obterTodosOrdenado() {
        List<Curriculo> lista = new ArrayList<Curriculo>();
        for (Curriculo curriculo : curriculoDAO.obterTodosOrdenado()) {
            verificarStatus(curriculo);
            lista.add(curriculo);
        }
        return lista;
    }

    public List<Curriculo> obterCurriculosZerados() {
        return curriculoDAO.obterCurriculosZerados();
    }
    
    public Integer totalPontos(Curriculo curriculo) {
        int totalPontos = 0;
        if (curriculo != null
                && curriculo.getId() != null) {
            totalPontos += (curriculo.getExtrato() == null ? 0 : curriculo.getExtrato());

            List<Livro> livros = new LivroRN().obterLivrosAtuais(curriculo);
            List<Periodico> periodicos = new PeriodicoRN().obterPeriodicosAtuais(curriculo);
            List<Orientacao> orientacoes = new OrientacaoRN().obterOrientacoesAtuais(curriculo);

            if (livros != null) {
                for (Livro l : livros) {
                    totalPontos += l.getEstrato();
                }
            }

            if (periodicos != null) {
                for (Periodico p : periodicos) {
                    totalPontos += p.getEstrato();

                }
            }

            if (orientacoes != null) {
                for (Orientacao orientacaoTemp : orientacoes) {
                    totalPontos += orientacaoTemp.getEstrato();
                }
            }
        }
        return totalPontos;
    }

    public void verificarStatus(Curriculo c) {
        List<Livro> livros = new LivroRN().obterLivrosAtuais(c);
        List<Periodico> periodicos = new PeriodicoRN().obterPeriodicosAtuais(c);
        List<Orientacao> orientacoes = new OrientacaoRN().obterOrientacoesAtuais(c);
        int contador = 0;
        int total = 0;

        if (livros != null) {
            for (Livro l : livros) {
                if (l.getAvaliacao() != null) {
                    contador++;
                }
            }
            total += livros.size();
        }
        
        if (periodicos != null) {
            for (Periodico periodico : periodicos) {
                if (periodico.getAvaliacao() != null) {
                    contador++;
                }
            }
            total += periodicos.size();
        }
        
        if (orientacoes != null) {
            for (Orientacao orientacao : orientacoes) {
                if (orientacao.getAvaliacao() != null) {
                    contador++;
                }
            }
            total += orientacoes.size();
        }
        
        if (total == contador) {
            c.setStatus("Avaliado");
        } else {
            c.setStatus("Em Avaliação");
        }
        salvar(c);
    }

}
