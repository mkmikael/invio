/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.bean;

import bpmlab.invio.bean.util.BeanUtil;
import bpmlab.invio.bean.util.UsuarioUtil;
import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Login;
import bpmlab.invio.entidade.Orientacao;
import bpmlab.invio.rn.OrientacaoRN;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author fabio & Mikael Lima
 */
@ManagedBean
@RequestScoped
public class OrientacaoBean {

    private Orientacao orientacao = new Orientacao();
    private final OrientacaoRN orientacaoRN = new OrientacaoRN();

    public OrientacaoBean() {
    }

    public Orientacao getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(Orientacao orientacao) {
        this.orientacao = orientacao;
    }

    public List<Orientacao> getOrientacoesAtuais() {
        //TODO Orientações do curriculo indicado (login)
        return orientacaoRN.obterOrientacoesAtuais(UsuarioUtil.obterUsuarioLogado().getCurriculo());
    }

    public List<Orientacao> getOrientacoesPassadas() {
        //TODO Orientações do curriculo indicado (login)
        return orientacaoRN.obterOrientacoesPassadas(UsuarioUtil.obterUsuarioLogado().getCurriculo());
    }

    public int getTotal() {
        int total = 0;

        for (Orientacao o : getOrientacoesAtuais()) {
            total += o.getEstrato();
        }

        return total;
    }

    public String upload() {
        BeanUtil.colocarNaSessao("orientacaoUpload", orientacao);
        return "/usuario/cadastro/curriculo/orientacao/uploadOrientacao.xhtml";
    }

    public String salvarOrientacao() {
        //TODO Extrato é com X
        Login login = UsuarioUtil.obterUsuarioLogado();
        Curriculo curriculo = login.getCurriculo();

        orientacao.setEstrato(orientacaoRN.obterExtratoPorTipoOrientacao(orientacao.getTipoOrientacao()));

        if (curriculo == null) {
            BeanUtil.criarMensagemDeErro("Você ainda não possui Currículo",
                    "Por favor preencha seu currículo em 'Meu Currículo' -> 'Meu Perfil'");
        } else if (orientacao.getAluno() == null
                || orientacao.getAluno().trim().equals("")) {
            BeanUtil.criarMensagemDeErro("Erro ao salvar a Orientação.",
                    "Preencha o campo Nome Bolsista.");
            return null;
        } else if (orientacao.getPFinal() == null) {
            BeanUtil.criarMensagemDeErro("Erro ao salvar a Orientação.",
                    "Preencha o campo Período Final.");
            return null;
        } else if (orientacao.getTipoOrientacao() < 1 || orientacao.getTipoOrientacao() > 8) {
            BeanUtil.criarMensagemDeErro("Erro ao salvar a Orientação.",
                    "Selecione o campo Tipo de Orientação.");
        } else if (orientacao.getTipoBolsa() == null
                || orientacao.getTipoBolsa().trim().equals("")) {
            BeanUtil.criarMensagemDeErro("Erro ao salvar a Orientação.",
                    "Selecione o Tipo de Bolsa.");
        } else {
            orientacao.setCurriculo(curriculo);
            if (orientacaoRN.salvar(orientacao)) {
                BeanUtil.criarMensagemDeInformacao(
                        "Operação realizada com sucesso",
                        "A orientação do bolsista " + orientacao.getAluno() + " foi salva com sucesso.");
            } else {
                BeanUtil.criarMensagemDeErro("Erro ao salvar a orientação", "Orientação: " + orientacao.getAluno());
            }
        }
        orientacao = new Orientacao();
        return "/orientacao/list.xhtml";
    }

    public void excluirOrientacao() {
        if (orientacaoRN.remover(orientacao)) {
            BeanUtil.criarMensagemDeInformacao(
                    "Orientação excluída",
                    "Orientação: " + orientacao.getAluno());
        } else {
            BeanUtil.criarMensagemDeErro(
                    "Erro ao excluir Orientação",
                    "Orientação: " + orientacao.getAluno());
        }
        orientacao = new Orientacao();
    }

    public String obterTipo(Integer tipo) {
        return orientacaoRN.obterTipoOrientacao(tipo);
    }

}
