package bpmlab.invio.bean;

import bpmlab.invio.bean.util.BeanUtil;
import bpmlab.invio.bean.util.UsuarioUtil;
import bpmlab.invio.entidade.Area;
import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Instituicao;
import bpmlab.invio.entidade.Livro;
import bpmlab.invio.entidade.Login;
import bpmlab.invio.entidade.Orientacao;
import bpmlab.invio.entidade.Periodico;
import bpmlab.invio.entidade.Plano;
import bpmlab.invio.entidade.Programa;
import bpmlab.invio.rn.AreaRN;
import bpmlab.invio.rn.CurriculoRN;
import bpmlab.invio.rn.LivroRN;
import bpmlab.invio.rn.LoginRN;
import bpmlab.invio.rn.OrientacaoRN;
import bpmlab.invio.rn.PeriodicoRN;
import bpmlab.invio.rn.PlanoRN;
import bpmlab.invio.rn.pdf.QualisRN;
import bpmlab.invio.util.UploadArquivo;
import invio.util.relatorio.Relatorio;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class CurriculoBean {

    private CurriculoRN curriculoRN = new CurriculoRN();
    private LoginRN loginRN = new LoginRN();
    private PeriodicoRN periodicoRN = new PeriodicoRN();
    private LivroRN livroRN = new LivroRN();
    private AreaRN areaRN = new AreaRN();
    private OrientacaoRN orientacaoRN = new OrientacaoRN();
    private PlanoRN planoRN = new PlanoRN();
    private List<Curriculo> curriculos;
    private List<Curriculo> curriculosDesc;
    private Curriculo curriculo;
    private Login login;
    private boolean skip;
    private Plano plano = new Plano();
    private UploadArquivo fileUpload = new UploadArquivo();
    private Programa programa = new Programa();
    private Area areaOutra = new Area();
    private boolean exibirOutroArea;
    private Integer totalPontos;
    private Orientacao orientacao;
    private UploadedFile upload;

    public CurriculoBean(List<Curriculo> curriculos) {
        this.curriculos = curriculos;
    }

    public CurriculoBean() {
    }

    public List<Curriculo> getCurriculos() {
        if (UsuarioUtil.isUsuarioLogadoAdministrador()) {
            curriculos = curriculoRN.obterTodos();
        } else {
            curriculos = curriculoRN.obterCurriculoLogin(UsuarioUtil.obterUsuarioLogado());
        }
        return curriculos;
    }

    public UploadedFile getUpload() {
        return upload;
    }

    public void setUpload(UploadedFile upload) {
        System.out.println("Toshiaki");
        this.upload = upload;
    }

    public List<Curriculo> getCurriculosDesc() {
        curriculos = curriculoRN.obterTodosDesc();
        return curriculosDesc;
    }

    public void setCurriculosDesc(List<Curriculo> curriculosDesc) {
        this.curriculosDesc = curriculosDesc;
    }

    public Plano getPlano() {
        return plano;
    }

    public Orientacao getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(Orientacao orientacao) {
        this.orientacao = orientacao;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Integer getIdLogin() {

        return login.getId();
    }

    public void setCurriculos(List<Curriculo> curriculos) {
        this.curriculos = curriculos;
    }

    public Curriculo getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(Curriculo curriculo) {
        this.curriculo = curriculo;
    }

    public String voltarListaOrientacao() {
        return "/usuario/cadastro/curriculo/orientacao/orientacoes.xhtml";
    }

    public String salvarCurriculo() {
        if (curriculo.getArea() == null) {
            BeanUtil.criarMensagemDeErro(
                    "Não foi selecionada nenhuma área de atuação.",
                    "Por favor preencher uma área.");
            return null;
        } else {
            if ("Doutorado".equals(curriculo.getTitulacao())) {
                curriculo.setExtrato(30);
            } else if ("Mestrado".equals(curriculo.getTitulacao())) {
                curriculo.setExtrato(15);
            } else {
                curriculo.setExtrato(0);
            }

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
        System.out.println("Curriculo " + getCurriculo());
        if (curriculoRN.remover(getCurriculo())) {
            BeanUtil.criarMensagemDeInformacao("Curriculo excluído", "Curriculo: " + getCurriculo().getNome());
        } else {
            BeanUtil.criarMensagemDeErro("Erro ao excluir o curriculo", "Curriculo: " + getCurriculo().getNome());
        }
        return "listarUsuarios.xhtml";
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public String onFlowProcess(FlowEvent event) {

        if (event.getOldStep()
                .equals("areaCurriculo")
                && curriculo.getArea() != null) {
            return "confirm";
        } else if (event.getOldStep()
                .equals("areaOutroCurriculo")
                && curriculo.getArea() != null) {
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    public List<Area> getAreas() {
        Instituicao UFRA = new Instituicao(1); //TODO CORRIGIR POG
        return areaRN.obterAreas(UFRA);
    }

    public String irListarCurriculos() {
        setCurriculo(null);
        return "/usuario/cadastro/curriculo/listar.xhtml";
    }

    public String irListarUsuarios() {
        setCurriculo(null);
        return "/administracao/usuarios/listarUsuarios.xhtml";
    }

    public String meuCurriculo() {
        if (curriculo == null) {
            curriculo = new Curriculo();
        } else {
            Login usuarioLogado = UsuarioUtil.obterUsuarioLogado();
            curriculo = usuarioLogado.getCurriculo();
        }
        return "/usuario/cadastro/curriculo/wizard.xhtml";
    }

    public String totalFCO() {
        Login usuarioLogado = UsuarioUtil.obterUsuarioLogado();
        curriculo = usuarioLogado.getCurriculo();

        if (curriculo != null
                && curriculo.getId() != null) {
            totalPontos = 0;
            totalPontos += (curriculo.getExtrato() == null ? 0 : curriculo.getExtrato());

            List<Livro> livros = curriculo.getLivroList();
            List<Periodico> periodicos = curriculo.getPeriodicoList();
            List<Orientacao> orientacoes = curriculo.getOrientacaoList();

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
        curriculo.setFco(totalPontos);

        Login loginLogado = UsuarioUtil.obterUsuarioLogado();
        loginLogado.setCurriculo(curriculo);

        loginRN.salvar(loginLogado);
        curriculoRN.salvar(curriculo);
        return "/usuario/cadastro/curriculo/fco.xhtml";
    }

    public Integer getTotalPontos() {
        Login usuarioLogado = UsuarioUtil.obterUsuarioLogado();
        curriculo = usuarioLogado.getCurriculo();

        if (curriculo != null
                && curriculo.getId() != null) {
            totalPontos = 0;
            totalPontos += (curriculo.getExtrato() == null ? 0 : curriculo.getExtrato());

            List<Livro> livros = curriculo.getLivroList();
            List<Periodico> periodicos = curriculo.getPeriodicoList();
            List<Orientacao> orientacoes = curriculo.getOrientacaoList();

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
        curriculo.setFco(totalPontos);

        Login loginLogado = UsuarioUtil.obterUsuarioLogado();
        loginLogado.setCurriculo(curriculo);

        loginRN.salvar(loginLogado);
        curriculoRN.salvar(curriculo);
        return totalPontos;
    }

    public void setTotalPontos(Integer totalPontos) {
        this.totalPontos = totalPontos;
    }

    public void uploadActionPlano(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        InputStream stream = null;
        try {
            stream = file.getInputstream();
            String tipo = file.getContentType();
            if (tipo.equals("application/pdf")) {
                tipo = "pdf";
            } else if (tipo.equals("application/jpg")) {
                tipo = "jpg";
            }
            String nomeDoArquivo = this.fileUpload.uploadPlano(getCurriculo(), plano, tipo, stream);
            this.plano.setTitulo(nomeDoArquivo);
            planoRN.salvar(plano);
            //Inicializa
            this.plano = new Plano();
            this.fileUpload = new UploadArquivo();
            BeanUtil.criarMensagemDeInformacao("O Arquivo foi salvo com sucesso. ", "Arquivo: " + nomeDoArquivo);
        } catch (IOException ex) {
        }
    }

    public void uploadActionOrientacao(FileUploadEvent event) {
        System.out.println("EVENTO: " + event.getFile().getFileName());
        UploadedFile file = event.getFile();
        InputStream stream = null;
        try {
            stream = file.getInputstream();
            String tipo = file.getContentType();
            if (tipo.equals("application/pdf")) {
                tipo = "pdf";
            } else if (tipo.equals("application/jpg")) {
                tipo = "jpg";
            }
            String nomeDoArquivo = this.fileUpload.uploadOrientacao(getCurriculo(), orientacao, tipo, stream);
            orientacaoRN.salvar(orientacao);
            //Inicializa
            this.orientacao = new Orientacao();
            this.fileUpload = new UploadArquivo();
            BeanUtil.criarMensagemDeInformacao("O Arquivo foi salvo com sucesso. ", "Arquivo: " + nomeDoArquivo);
        } catch (IOException ex) {
        }
    }

    public String salvarFCO() {

        List<Periodico> periodicos = getCurriculo().getPeriodicoList();
        List<Livro> livros = getCurriculo().getLivroList();
        List<Orientacao> orientacoes = getCurriculo().getOrientacaoList();

        for (Periodico periodicoAtual : periodicos) {
            periodicoRN.salvar(periodicoAtual);
        }

        for (Livro livroAtual : livros) {
            livroRN.salvar(livroAtual);
        }

        for (Orientacao orientacaoAtual : orientacoes) {
            orientacaoRN.salvar(orientacaoAtual);
        }
        curriculo.setFco(totalPontos);

        Login loginLogado = UsuarioUtil.obterUsuarioLogado();
        loginLogado.setCurriculo(curriculo);

        loginRN.salvar(loginLogado);
        curriculoRN.salvar(curriculo);
        return "/administracao/listarCurriculoAv.xhtml";
    }

    //DESTA LINHA PARA BAIXO ENCONTRA-SE O UPLOAD DE ORIENTACAO
    //DESTA LINHA PARA BAIXO ENCONTRA-SE O UPLOAD DE ORIENTACAO
//    public void uploadActionOrientacao(FileUploadEvent event) {
//        UploadedFile file = event.getFile();
//        InputStream stream = null;
//        try {
//            stream = file.getInputstream();
//            String tipo = file.getContentType();
//            if (tipo.equals("application/pdf")) {
//                tipo = "pdf";
//            } else if (tipo.equals("application/jpg")) {
//                tipo = "jpg";
//            }
//
//
//            String nomeDoArquivo = this.fileUpload.uploadOrientacao(getCurriculo(), orientacao, tipo, stream);
//            this.orientacao.setArquivo(nomeDoArquivo);
//            orientacaoRN.salvar(orientacao);
//            //Inicializa
//            this.orientacao = new Orientacao();
//            this.fileUpload = new UploadArquivo();
//            BeanUtil.criarMensagemDeInformacao("O Arquivo foi salvo com sucesso. ", "Arquivo: " + nomeDoArquivo);
//        } catch (IOException ex) {
//        }
//    }
    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public void salvarProgramaCurriculo() {
        getCurriculo().getArea().getProgramaList().add(programa);
        curriculoRN.salvar(getCurriculo());
    }

    //Bolsa de Produtividade do CNPq
//    public boolean isBolsaP() {
//        return BolsaP;
//    }
//
//    public void setBolsaP(boolean BolsaP) {
//        if (BolsaP == true) {
//            curriculo.setBolsaProdutividade(30);
//        }
//        this.BolsaP = BolsaP;
//    }
    //Bolsa de Produtividade do CNPq
    /**
     * @return the curriculo
     */
    public void exibirTabOutroArea() {
        if (curriculo != null) {
            if (curriculo.getArea().equals(areaOutra)) {
                setExibirOutroArea(true);
            } else {
                setExibirOutroArea(false);
            }
        }
    }

    /**
     * @return the exibirOutroArea
     */
    public boolean isExibirOutroArea() {
        return exibirOutroArea;
    }

    /**
     * @param exibirOutroArea the exibirOutroArea to set
     */
    public void setExibirOutroArea(boolean exibirOutroArea) {
        this.exibirOutroArea = exibirOutroArea;
    }

    /**
     * @return the areaOutra
     */
    public Area getAreaOutra() {
        return areaOutra;
    }

    /**
     * @param areaOutra the areaOutra to set
     */
    public void setAreaOutra(Area areaOutra) {
        this.areaOutra = areaOutra;
    }

    public List<String> complete(String query) {
        QualisRN qualisRN = new QualisRN();
        return qualisRN.obterTodosTitulos(query);
    }

    public List<Area> completeArea(String query) {
        return areaRN.completeArea(query);
    }

    public void gerarListaDocentes() {

        String path = "/usuario/core/report/crachaCredenciado.jasper";
        List<Curriculo> dataSource = new ArrayList<Curriculo>();
        Curriculo curriculoR = new Curriculo();
        dataSource.add(curriculoR);
    }

    public String gerarFCO() {
        Login usuario = UsuarioUtil.obterUsuarioLogado();
        if (usuario.getCurriculo() == null) {
            FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção!", "Não há FCO preenchido para exibição.");
            FacesContext.getCurrentInstance().addMessage(null, fm);
            return "/publico/indexHome.xhtml";
        } else {
            String path = "/usuario/core/report/fco.jasper";
            Curriculo curriculoR = UsuarioUtil.obterUsuarioLogado().getCurriculo();
            String area = curriculoR.getArea().getNome();
            int id_do_usuario_logado = curriculoR.getId();

            Relatorio.geraRelatorio(path, "Curriculo - " + curriculoR.getNome(), id_do_usuario_logado, area);
            return null;
        }
    }

    public void gerarcrachaCredenciado() {

        String path = "/usuario/core/report/crachaCredenciado.jasper";
        List dataSource = new ArrayList();
        Curriculo curriculoR = UsuarioUtil.obterUsuarioLogado().getCurriculo();
        List datasourceLivro = new ArrayList(UsuarioUtil.obterUsuarioLogado().getCurriculo().getLivroList());
        List datasourcePeriodico = new ArrayList(UsuarioUtil.obterUsuarioLogado().getCurriculo().getPeriodicoList());
        List datasourceOrientacao = new ArrayList(UsuarioUtil.obterUsuarioLogado().getCurriculo().getOrientacaoList());
        dataSource.addAll(datasourceLivro);
        dataSource.addAll(datasourcePeriodico);
        dataSource.addAll(datasourceOrientacao);
        dataSource.add(curriculoR);
        String area = curriculoR.getArea().getNome();
        int id_do_usuario_logado = curriculoR.getId();

        Relatorio.ImprimirRelatorio(path, dataSource,
                "relatorio" + id_do_usuario_logado + area, id_do_usuario_logado, area);
    }

    public void gerarListaLivros() {
        //TODO
        String path = "/usuario/core/report/listaLivro.jasper";
        List<Livro> dataSource = new ArrayList<Livro>(UsuarioUtil.obterUsuarioLogado().getCurriculo().getLivroList());
        Object params = UsuarioUtil.obterUsuarioLogado().getCurriculo().getId();
        String nome = UsuarioUtil.obterUsuarioLogado().getCurriculo().getNome();
        Relatorio.geraRelatorio(path, nome, params, null);
    }

    public void gerarListaPerioticos() {
        //TODO
        String path = "/usuario/core/report/listaPeriodico.jasper";
        List<Periodico> dataSource = new ArrayList<Periodico>(UsuarioUtil.obterUsuarioLogado().getCurriculo().getPeriodicoList());
        Object params = UsuarioUtil.obterUsuarioLogado().getCurriculo().getId();
        Curriculo curriculoR = new Curriculo();
        Relatorio.geraRelatorio(path, "Curriculo - " + curriculoR.getNome(), null, null);
    }

    public void gerarListaOrientacoes() {
        //TODO
        String path = "/usuario/core/report/listaOrientacao.jasper";
        List<Orientacao> dataSource = new ArrayList<Orientacao>(UsuarioUtil.obterUsuarioLogado().getCurriculo().getOrientacaoList());
        Curriculo curriculoR = new Curriculo();
        Object params = UsuarioUtil.obterUsuarioLogado().getCurriculo().getId();
    }
}