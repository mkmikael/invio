/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.rn;

import bpmlab.invio.dao.CurriculoDAO;
import bpmlab.invio.dao.GenericDAO;
import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Livro;
import bpmlab.invio.entidade.Orientacao;
import bpmlab.invio.entidade.Periodico;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Toshiaki & Mikael
 */
public class AvaliacaoRN implements Serializable {

    private final CurriculoRN rnCurriculo = new CurriculoRN();

    public List<Curriculo> autoCompleteCurriculo(String busca) {
        CurriculoDAO curriculoDAO = new CurriculoDAO();
        return curriculoDAO.obterCurriculoPorNome(busca);
    }

    public boolean possuiArquivo(String arquivo) {
        return !(arquivo == null || "".equals(arquivo));
    }

    public boolean confirmar(Curriculo curriculo, Object object) {
        String avaliacao = "Avaliado";
        if (object instanceof Livro) {
            ((Livro)object).setAvaliacao(avaliacao);
        } else if (object instanceof Orientacao) {
            ((Orientacao)object).setAvaliacao(avaliacao);
        } else if (object instanceof Periodico) {
            ((Periodico)object).setAvaliacao(avaliacao);
        } else {
            return false; //Raramente ocorrerá
        }
        rnCurriculo.verificarStatus(curriculo);
        new GenericDAO<Object>().alterar(object);
        curriculo.setFco(rnCurriculo.totalPontos(curriculo));
        return rnCurriculo.salvar(curriculo);
    }
    
    public boolean corrigir(Curriculo curriculo, Object object, Integer estrato) {
        String avaliacao = "Avaliado com diferenças";
        if (object instanceof Livro) {
            ((Livro)object).setAvaliacao(avaliacao);
            ((Livro)object).setEstrato(estrato);
        } else if (object instanceof Orientacao) {
            ((Orientacao)object).setAvaliacao(avaliacao);
            ((Orientacao)object).setEstrato(estrato);
        } else if (object instanceof Periodico) {
            ((Periodico)object).setAvaliacao(avaliacao);
            ((Periodico)object).setEstrato(estrato);
        } else {
            return false; //Raramente ocorrerá
        }
        rnCurriculo.verificarStatus(curriculo);
        new GenericDAO<Object>().alterar(object);
        curriculo.setFco(rnCurriculo.totalPontos(curriculo));
        return rnCurriculo.salvar(curriculo);
    }
    
    public boolean negar(Curriculo curriculo, Object object) {
        String avaliacao = "Negado pelo Comitê";
        int negado = 0;
        if (object instanceof Livro) {
            ((Livro)object).setAvaliacao(avaliacao);
            ((Livro)object).setEstrato(negado);
        } else if (object instanceof Orientacao) {
            ((Orientacao)object).setAvaliacao(avaliacao);
            ((Orientacao)object).setEstrato(negado);
        } else if (object instanceof Periodico) {
            ((Periodico)object).setAvaliacao(avaliacao);
            ((Periodico)object).setEstrato(negado);
        } else {
            return false; //Raramente ocorrerá
        }
        //AKI
        rnCurriculo.verificarStatus(curriculo);
        new GenericDAO<Object>().alterar(object);
        curriculo.setFco(rnCurriculo.totalPontos(curriculo));
        return rnCurriculo.salvar(curriculo);
    }

}
