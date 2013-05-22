package invio.bean;

import invio.bean.util.BeanUtil;
import invio.entidade.Area;
import invio.entidade.Curriculo;
import invio.entidade.Instituicao;
import invio.entidade.Livro;
import invio.entidade.Login;
import invio.entidade.Orientacao;
import invio.entidade.Periodico;
import invio.entidade.Plano;
import invio.entidade.Programa;
import invio.rn.AreaRN;
import invio.rn.CurriculoRN;
import invio.rn.LivroRN;
import invio.rn.LoginRN;
import invio.rn.OrientacaoRN;
import invio.rn.PeriodicoRN;
import invio.rn.PlanoRN;
import invio.rn.pdf.QualisRN;
import invio.util.UploadArquivo;
import invio.bean.util.UsuarioUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
    private QualisRN qualisRN = new QualisRN();
    private OrientacaoRN orientacaoRN = new OrientacaoRN();
    private PlanoRN planoRN = new PlanoRN();
    private List<Curriculo> curriculos;
    private List<Curriculo> curriculosDesc;
    private Curriculo curriculo;
    private Login login;
    private boolean skip;
    private Livro livro = new Livro();
    private Plano plano = new Plano();
    private UploadArquivo fileUpload = new UploadArquivo();
    private Programa programa = new Programa();
    private Area areaOutra = new Area();
    private boolean exibirOutroArea;
    private Integer totalPontos;

    public CurriculoBean(List<Curriculo> curriculos) {
        this.curriculos = curriculos;
    }

    public CurriculoBean() {
    }

    public List<Curriculo> getCurriculos() {
        if (UsuarioUtil.isUsuarioLogadoMaster()) {
            curriculos = curriculoRN.obterTodos();
        } else {
            curriculos = curriculoRN.obterCurriculoLogin(UsuarioUtil.obterUsuarioLogado());
        }
        return curriculos;
    }

    public List<Curriculo> getCurriculosDesc() {
        //TODO Para que isso?!!!

        curriculos = curriculoRN.obterTodosDesc();
        return curriculosDesc;
    }

    public void setCurriculosDesc(List<Curriculo> curriculosDesc) {
        this.curriculosDesc = curriculosDesc;
    }

    public Plano getPlano() {
        return plano;
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

    public void setCurriculo(Curriculo curriculo) {
        if (BeanUtil.lerDaSessao("curriculo") == null) {
            BeanUtil.colocarNaSessao("curriculo", curriculo);
        }
        this.curriculo = curriculo;
    }

    public String salvarCurriculo() {
        if (curriculo.getArea() == null) {
            BeanUtil.criarMensagemDeErro(
                    "Não foi selecionada nenhuma área de atuação.",
                    "Por favor preencher uma área.");
            return null;
        } else {
            if ("Doutorado".equals(curriculo.getTitulacao())) {
                curriculo.setExtrato(50);
            } else if ("Mestrado".equals(curriculo.getTitulacao())) {
                curriculo.setExtrato(30);
            }

            Login loginLogado = UsuarioUtil.obterUsuarioLogado();
            loginLogado.setCurriculo(curriculo);

            if (loginRN.salvar(loginLogado)
                    && curriculoRN.salvar(curriculo)) {

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
        if (event.getOldStep().equals("areaCurriculo")
                && curriculo.getArea() != null) {
            return "confirm";
        } else if (event.getOldStep().equals("areaOutroCurriculo")
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
        return "/cadastro/curriculo/listar.xhtml";
    }

    public String irListarUsuarios() {
        setCurriculo(null);
        return "/admin/usuarios/listarUsuarios.xhtml";
    }

    public String meuCurriculo() {
        Login usuarioLogado = UsuarioUtil.obterUsuarioLogado();
        Curriculo curriculo = usuarioLogado.getCurriculo();
        if (curriculo != null) {
            setCurriculo(curriculo);
        } else {
            setCurriculo(new Curriculo());
        }
        return "/cadastro/curriculo/wizard.xhtml";
    }

    public String altualizarPontos() {
        if (curriculo != null) {

            totalPontos = 0;

            List<Livro> livros = curriculo.getLivroList();
            List<Periodico> periodicos = curriculo.getPeriodicoList();
            List<Orientacao> orientacoes = curriculo.getOrientacaoList();

            for (Livro livroTemp : livros) {
                totalPontos = totalPontos + livroTemp.getEstrato();
            }

            for (Periodico periodicoTemp : periodicos) {
                totalPontos = totalPontos + periodicoTemp.getEstrato();

            }

            for (Orientacao orientacaoTemp : orientacoes) {

                totalPontos = totalPontos + orientacaoTemp.getEstrato();
            }

        }
        return "/admin/listarProducao.xhtml";

    }

    public Integer getTotalPontos() {
        if (curriculo != null) {

            totalPontos = 0;

            List<Livro> livros = curriculo.getLivroList();
            List<Periodico> periodicos = curriculo.getPeriodicoList();
            List<Orientacao> orientacoes = curriculo.getOrientacaoList();

            for (Livro livroTemp : livros) {
                totalPontos = totalPontos + livroTemp.getEstrato();
            }

            for (Periodico periodicoTemp : periodicos) {
                totalPontos = totalPontos + periodicoTemp.getEstrato();

            }

            for (Orientacao orientacaoTemp : orientacoes) {

                totalPontos = totalPontos + orientacaoTemp.getEstrato();
            }
        }
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

   

    //CONTROLE DE LIVRO APARTIR DESTA LINHA
    //CONTROLE DE LIVRO APARTIR DESTA LINHA
    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public String salvarLivro() {
        if (livro.getTipoLivro() == 30 && livro.getCapitulo().equals("")) {
            livro.setEstrato(30);
        } else if (livro.getTipoLivro() == 30 && livro.getCapitulo() != null) {
            livro.setEstrato(20);
        } else if (livro.getTipoLivro() == 10) {
            livro.setEstrato(10);
        }

        livro.setCurriculo(getCurriculo());
        getCurriculo().getLivroList().add(livro);

        if (livroRN.salvar(livro)) {
            curriculoRN.salvar(getCurriculo());
            BeanUtil.criarMensagemDeInformacao(
                    "Operação realizada com sucesso",
                    "O Livro " + livro.getTitulo() + " foi salvo com sucesso.");
        } else {
            BeanUtil.criarMensagemDeErro("Erro ao salvar o livro", "Livro: " + livro.getTitulo());
        }
        livro = new Livro();
        return null;
    }

    public String salvarEditarLivro(Livro livroTemp) {

        if (livroRN.salvar(livroTemp)) {
            BeanUtil.criarMensagemDeInformacao(
                    "Operação realizada com sucesso",
                    "O Livro " + livroTemp.getTitulo() + " foi salvo com sucesso.");
        } else {
            BeanUtil.criarMensagemDeErro("Erro ao salvar o livro", "Livro: " + livroTemp.getTitulo());
        }
        livro = new Livro();

        return null;
    }

    public String excluirLivro() {
        System.out.println("Livro: " + livro);
        if (livroRN.remover(livro)) {
            BeanUtil.criarMensagemDeInformacao("Livro excluído", "Livro: " + livro.getTitulo());
        } else {
            BeanUtil.criarMensagemDeErro("Erro ao excluir Livro", "Livro: " + livro.getTitulo());
        }

        livro = new Livro();
        return "livros.xhtml";
    }

    public void uploadActionLivro(FileUploadEvent event) {
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
            String nomeDoArquivo = this.fileUpload.uploadLivro(getCurriculo(), livro, tipo, stream);
            this.livro.setArquivo(nomeDoArquivo);
            livroRN.salvar(livro);
            //Inicializa
            this.livro = new Livro();
            this.fileUpload = new UploadArquivo();
            BeanUtil.criarMensagemDeInformacao("O Arquivo foi salvo com sucesso. ", "Arquivo: " + nomeDoArquivo);
        } catch (IOException ex) {
        }

    }

    public String voltarListaLivro() {

        livro = new Livro();
        return "/cadastro/curriculo/producao/livros.xhtml";
    }

    public String salvarFCO() {

        List<Periodico> periodicos = getCurriculo().getPeriodicoList();
        List<Livro> livros = getCurriculo().getLivroList();
        List<Orientacao> orientacoes = getCurriculo().getOrientacaoList();

        for (Periodico periodicoAtual : periodicos) {
            periodicoRN.salvarPAtual(periodicoAtual);
        }

        for (Livro livroAtual : livros) {
            livroRN.salvar(livroAtual);
        }

        for (Orientacao orientacaoAtual : orientacoes) {
            orientacaoRN.salvar(orientacaoAtual);
        }

        return "/admin/listarCurriculoAv.xhtml";
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

    /**
     * @return the curriculo
     */
    public Curriculo getCurriculo() {
        return curriculo;
    }

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
}