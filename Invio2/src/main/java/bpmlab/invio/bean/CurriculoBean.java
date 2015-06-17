package bpmlab.invio.bean;

import bpmlab.invio.bean.util.BeanUtil;
import bpmlab.invio.bean.util.UsuarioUtil;
import bpmlab.invio.dao.AreaDAO;
import bpmlab.invio.entidade.Area;
import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Instituicao;
import bpmlab.invio.entidade.Livro;
import bpmlab.invio.entidade.Login;
import bpmlab.invio.entidade.Orientacao;
import bpmlab.invio.rn.CurriculoRN;
import bpmlab.invio.rn.LivroRN;
import bpmlab.invio.rn.LoginRN;
import bpmlab.invio.rn.OrientacaoRN;
import bpmlab.invio.rn.PeriodicoRN;
import bpmlab.invio.rn.QualisRN;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CurriculoBean {

    private CurriculoRN curriculoRN = new CurriculoRN();
    private LoginRN loginRN = new LoginRN();
    private PeriodicoRN periodicoRN = new PeriodicoRN();
    private LivroRN livroRN = new LivroRN();
    private AreaDAO areaDAO = new AreaDAO();
    private OrientacaoRN orientacaoRN = new OrientacaoRN();
    private List<Curriculo> curriculos;
    private List<Curriculo> curriculosZerados;
    private Curriculo curriculo;
    private Integer totalPontos;
    private Orientacao orientacao;

    public CurriculoBean(List<Curriculo> curriculos) {
        this.curriculos = curriculos;
    }

    public CurriculoBean() {
    }

    public List<Curriculo> getCurriculos() {
        curriculos = curriculoRN.obterTodos();
        return curriculos;
    }

    public Orientacao getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(Orientacao orientacao) {
        this.orientacao = orientacao;
    }

    public Curriculo getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(Curriculo curriculo) {
        System.out.println(curriculo);
        this.curriculo = curriculo;
    }

    public String salvarCurriculo() {
        if (curriculo.getArea() == null) {
            BeanUtil.criarMensagemDeErro(
                    "Não foi selecionada nenhuma Área de atuação.",
                    "Por favor preencher uma Área.");
            return null;
        } else {
            curriculoRN.salvar(curriculo);

            Login loginLogado = UsuarioUtil.obterUsuarioLogado();
            loginLogado.setCurriculo(curriculo);

            if (loginRN.salvar(loginLogado)) {
                BeanUtil.criarMensagemDeInformacao(
                        "Operação realizada com sucesso",
                        "O curriculo " + getCurriculo().getNome() + " foi gravado com sucesso.");
                return "/publico/indexHome.xhtml";
            } else {
                BeanUtil.criarMensagemDeErro(
                        "Erro ao salvar o curriculo",
                        "Curriculo: " + getCurriculo().getNome());
                return null;
            }
        }
    }

    public String excluirCurriculo() {
        if (curriculoRN.remover(getCurriculo())) {
            BeanUtil.criarMensagemDeInformacao("Curriculo excluído", "Curriculo: " + getCurriculo().getNome());
        } else {
            BeanUtil.criarMensagemDeErro("Erro ao excluir o curriculo", "Curriculo: " + getCurriculo().getNome());
        }
        return "listarUsuarios.xhtml";
    }

    public List<Area> getAreas() {
        Instituicao UFRA = new Instituicao(1); //TODO CORRIGIR POG
        return areaDAO.obterAreas(UFRA);
    }

    public String meuCurriculo() {
        Login usuarioLogado = UsuarioUtil.obterUsuarioLogado();
        if (usuarioLogado.getCurriculo() == null) {
            curriculo = new Curriculo();
        } else {
            curriculo = usuarioLogado.getCurriculo();
        }
        return "/curriculo/create.xhtml";
    }

    public Integer getTotalPontos() {
        Login usuarioLogado = UsuarioUtil.obterUsuarioLogado();
        curriculo = usuarioLogado.getCurriculo();
        if (curriculo != null) {
            Integer ponto = curriculoRN.totalPontos(curriculo);
            curriculo.setFco(ponto);
            curriculoRN.salvar(curriculo);
            totalPontos = ponto;
        }
        return totalPontos;
    }

    public void setTotalPontos(Integer totalPontos) {
        this.totalPontos = totalPontos;
    }

    public List<Livro> getLivrosAtuais() {
        System.out.println(curriculo);
        List<Livro> obterLivrosAtuais = livroRN.obterLivrosAtuais(curriculo);
        System.out.println(obterLivrosAtuais);
        return obterLivrosAtuais;
    }
    
    public List<Curriculo> getCurriculosZerados() {
        curriculosZerados = curriculoRN.obterCurriculosZerados();
        return curriculosZerados;
    }

    public List<String> complete(String query) {
        QualisRN qualisRN = new QualisRN();
        return qualisRN.obterTodosTitulosArea(query);
    }

    public List<Area> completeArea(String query) {
        return areaDAO.obterAreaPorCriterio(query);
    }
    
    public String irParaProducao(String producao) {
        Login loginLogado = UsuarioUtil.obterUsuarioLogado();
        if (loginLogado.getCurriculo() == null || loginLogado.getCurriculo().getNome().isEmpty()) {
            BeanUtil.criarMensagemDeAviso("Aviso Você deve registrar seus dados de perfil para poder registrar uma produção, clique na opção do menu 'Meu Perfil' e preencha os dados", "");
            return null;
        } else {
            return producao;
        }
    }

}
