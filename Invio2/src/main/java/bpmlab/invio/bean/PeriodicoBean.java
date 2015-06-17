package bpmlab.invio.bean;

import bpmlab.invio.bean.util.BeanUtil;
import bpmlab.invio.bean.util.UsuarioUtil;
import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Login;
import bpmlab.invio.entidade.Periodico;
import bpmlab.invio.rn.CurriculoRN;
import bpmlab.invio.rn.PeriodicoRN;
import bpmlab.invio.rn.QualisRN;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author fabio & Mikael
 */
@ManagedBean
@RequestScoped
public class PeriodicoBean {

    private static final Logger LOG = Logger.getLogger(PeriodicoBean.class.getName());
    private Periodico periodico = new Periodico();
    private final PeriodicoRN periodicoRN = new PeriodicoRN();
    private List<Periodico> periodicosAtuais;

    public PeriodicoBean() {
    }

    public Periodico getPeriodico() {
        return periodico;
    }

    public void setPeriodico(Periodico periodico) {
        this.periodico = periodico;
    }

    public List<Periodico> getPeriodicosAtuais() {
        if (periodicosAtuais == null) {
            Curriculo curriculo = UsuarioUtil.obterUsuarioLogado().getCurriculo();
            periodicosAtuais = periodicoRN.obterPeriodicosAtuais(curriculo);
        }
        return periodicosAtuais;
    }

    public List<Periodico> getPeriodicosPassados() {
        Curriculo curriculo = UsuarioUtil.obterUsuarioLogado().getCurriculo();
        return periodicoRN.obterPeriodicosPassados(curriculo);
    }

    public String upload() {
        BeanUtil.colocarNaSessao("periodicoUpload", periodico);
        return "/usuario/cadastro/curriculo/periodico/uploadPeriodico.xhtml";
    }

    public int getTotal() {
        int total = 0;

        for (Periodico p : getPeriodicosAtuais()) {
            total += p.getEstrato();
        }
        return total;
    }

    public String salvarPeriodico() {
        Login login = UsuarioUtil.obterUsuarioLogado();
        Curriculo curriculo = login.getCurriculo();
        if (curriculo == null) {
            BeanUtil.criarMensagemDeErro("Você ainda não possui Curriculo",
                    "Por favor preencha seu curriculo em 'Meu Curriculo' -> 'Meu Perfil'");
        } else if (periodico.getTitulo() == null || periodico.getTitulo().trim().equals("")) {
            BeanUtil.criarMensagemDeErro(
                    "Erro ao salvar o Periódico.",
                    "Preencha o campo Título.");
        } else if (periodico.getAutores() == null || periodico.getAutores().trim().equals("")) {
            BeanUtil.criarMensagemDeErro(
                    "Erro ao salvar o Periódico.",
                    "Preencha o campo Autor.");
        } else if (periodico.getAno() == null || periodico.getAno().trim().equals("")) {
            BeanUtil.criarMensagemDeErro(
                    "Erro ao salvar o Periódico.",
                    "Preencha o campo Ano Publicação.");
        } else if (Integer.parseInt(periodico.getAno()) < Calendar.getInstance().getWeekYear() - 5) {
            BeanUtil.criarMensagemDeErro("O periódico deve ter sido publicado nos últimos 5 anos.", "");
        } else {
//            QualisRN rn = new QualisRN();
//            int estrato = rn.obterEstrato(periodico.getRevista(), curriculo.getArea().getNome());
//            if (estrato == 0) {
//                BeanUtil.criarMensagemDeErro("A revista esta incorreta.", "");
//                return null;
//            }
            LOG.info(periodico.getRevista());
            periodico.setCurriculo(curriculo);
            periodico.setArquivo("");

            if (periodicoRN.salvar(periodico)) {
                if (curriculo.getPeriodicoList() == null) {
                    curriculo.setPeriodicoList(new ArrayList<Periodico>());
                }
                if (curriculo.getFco() == null) {
                    curriculo.setFco(0);
                }
                curriculo.setFco(curriculo.getFco() + periodico.getEstrato());
                new CurriculoRN().salvar(curriculo);

                BeanUtil.criarMensagemDeInformacao(
                        "Operação realizada com sucesso",
                        "O periódico " + periodico.getTitulo() + " foi salvo com sucesso.");
            } else {
                BeanUtil.criarMensagemDeErro("Erro ao salvar o periódico", "Periódico: " + periodico.getTitulo());
            }
        }
        periodico = new Periodico();
        return "/periodico/list.xhtml";
    }

    public String excluirPeriodico() {
        LOG.log(Level.INFO, periodico.toString());
        if (periodicoRN.remover(periodico)) {
            BeanUtil.criarMensagemDeInformacao(
                    "Sucesso",
                    "Periódico excluído");
        } else {
            BeanUtil.criarMensagemDeErro(
                    "Erro",
                    "Não foi possível excluir o login");
        }
        periodico = new Periodico();
        Curriculo curriculo = UsuarioUtil.obterUsuarioLogado().getCurriculo();
        periodicosAtuais = periodicoRN.obterPeriodicosAtuais(curriculo);
        return "/periodico/list.xhtml";
    }

    public List<String> getRevistas() {
        QualisRN qualisRN = new QualisRN();
        String area = UsuarioUtil.obterUsuarioLogado().getCurriculo().getArea().getNome();
        return qualisRN.obterPorArea(area);
    }
    
}
