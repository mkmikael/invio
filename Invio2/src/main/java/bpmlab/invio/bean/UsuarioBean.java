package bpmlab.invio.bean;

import bpmlab.invio.bean.util.BeanUtil;
import bpmlab.invio.bean.util.UsuarioUtil;
import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Login;
import bpmlab.invio.rn.LoginRN;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class UsuarioBean {

    private LoginRN loginRN = new LoginRN();
    private List<Login> logins;
    private Login login = new Login();
    private Curriculo curriculo = new Curriculo();
    private Login usuarioLogado = new Login();
    private String codigoDeConfirmacao;
    private String cpfLoginTemp = "";
    private String cpfLogin = "";
    private String usuario;

    public UsuarioBean() {
    }

    public List<Login> getLogins() {
        if (UsuarioUtil.isUsuarioLogadoAdministrador()) {
            logins = loginRN.obterTodos();
        } else {
            logins = null;
            BeanUtil.criarMensagemDeInformacao("Página não autorizada para este Usuário.", "");
        }
        return logins;
    }

    public String getCodigoDeConfirmacao() {
        return codigoDeConfirmacao;
    }

    public void setCodigoDeConfirmacao(String codigoDeConfirmacaoTemp) {
        this.codigoDeConfirmacao = codigoDeConfirmacaoTemp;
    }

    public Curriculo getCurriculo() {
        return curriculo;
    }

    public void setCurriculo(Curriculo curriculo) {
        this.curriculo = curriculo;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getUsuario() {
        return usuario;
    }

    public String irRecuperarSenha() {
        return "/publico/login/recuperarSenha.xhtml";
    }

    boolean entrar = false;

    public void setEntrar(boolean entrar) {
        this.entrar = entrar;
    }

    public String getCpfLoginTemp() {
        return cpfLoginTemp;
    }

    public void setCpfLoginTemp(String cpfLoginTemp) {
        this.cpfLoginTemp = cpfLoginTemp;
    }

    public String getCpfLogin() {
        return cpfLogin;
    }

    public void setCpfLogin(String cpfLogin) {
        this.cpfLogin = cpfLogin;
    }

    public String salvarCurriculo() {
        return "/publico/indexHome.xhtml";
    }

    public String validar() {
        Login usuarioAtual = UsuarioUtil.obterUsuarioLogado();
        if (usuarioAtual.getCodigoConfirmacao().equals(codigoDeConfirmacao)) {
            usuarioAtual.setCodigoConfirmacao(null);
            loginRN.salvar(usuarioAtual);
            return "/publico/indexHome.xhtml";
        } else {
            FacesMessage fm = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Erro!",
                    "Código de confirmação inválido!");
            FacesContext.getCurrentInstance().addMessage(null, fm);
            return null;
        }
    }
    
    public Login getUsuarioLogado() {
        usuarioLogado = UsuarioUtil.obterUsuarioLogado();
        return usuarioLogado;
    }

    public void setUsuarioLogado(Login usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public boolean isAdministrador() {
        return UsuarioUtil.isUsuarioLogadoAdministrador();
    }

    public boolean isSecretaria() {
        return UsuarioUtil.isUsuarioLogadoSecretaria();
    }
    
    public boolean isUsuarioSessao() {
        return UsuarioUtil.isUsuarioLogadoUsuario();
    }
}
