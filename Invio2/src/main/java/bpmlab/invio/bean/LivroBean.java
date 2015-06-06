/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.bean;

import bpmlab.invio.bean.util.BeanUtil;
import bpmlab.invio.bean.util.UsuarioUtil;
import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.entidade.Livro;
import bpmlab.invio.entidade.Login;
import bpmlab.invio.rn.LivroRN;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author fabio & Mikael Lima
 */
@ManagedBean
@RequestScoped
public class LivroBean {

    Livro livro = new Livro();
    LivroRN livroRN = new LivroRN();

    public LivroBean() {
        livro.setTipoLivro(30);
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public List<Livro> getLivrosAtuais() {
        return livroRN.obterLivrosAtuais(UsuarioUtil.obterUsuarioLogado().getCurriculo());
    }

    public List<Livro> getLivrosPassados() {
        return livroRN.obterLivrosPassados(UsuarioUtil.obterUsuarioLogado().getCurriculo());
    }

    public int getTotal() {
        int total = 0;

        for (Livro l : getLivrosAtuais()) {
            total += l.getEstrato();
        }

        return total;
    }

    public String salvarLivro() {
        Login login = UsuarioUtil.obterUsuarioLogado();
        Curriculo curriculo = login.getCurriculo();

        if (curriculo == null) {
            BeanUtil.criarMensagemDeErro("Você ainda não possui Currículo",
                    "Por favor preencha seu currículo em 'Meu Currículo' -> 'Meu Perfil'");
        } else if (livro.getTitulo() == null
                || livro.getTitulo().trim().equals("")) {
            BeanUtil.criarMensagemDeErro("Erro ao salvar Livro.", "Preencha o campo Título.");
        } else if (livro.getTipoLivro() != 30 && livro.getTipoLivro() != 10) {
            BeanUtil.criarMensagemDeErro("Erro ao salvar Livro.", "Selecione o campo Autor ou Editor.");
        } else if (livro.getAutor() == null || livro.getAutor().trim().equals("")) {
            BeanUtil.criarMensagemDeErro("Erro ao salvar Livro.", "Preencha o campo Autor.");
        } else if (livro.getAno() == null || livro.getAno().trim().equals("")) {
            BeanUtil.criarMensagemDeErro("Erro ao salvar Livro.", "Preencha o campo Ano de Publicação.");
        } else {
            livro.setCurriculo(curriculo);
            if (livroRN.salvar(livro)) {
                BeanUtil.criarMensagemDeInformacao(
                        "Operação realizada com sucesso",
                        "O Livro " + livro.getTitulo() + " foi salvo com sucesso.");
            } else {
                BeanUtil.criarMensagemDeErro("Erro ao salvar o livro", "Livro: " + livro.getTitulo());
            }
        }
        livro = new Livro();
        livro.setTipoLivro(30);
        return "/livro/list.xhtml";
    }

    public String excluirLivro() {
        if (livroRN.remover(livro)) {
            BeanUtil.criarMensagemDeInformacao("Livro excluído", "Livro: " + livro.getTitulo());
        } else {
            BeanUtil.criarMensagemDeErro("Erro ao excluir Livro", "Livro: " + livro.getTitulo());
        }

        livro = new Livro();
        return "livros.xhtml";
    }

    public String voltar() {
        return "/usuario/cadastro/curriculo/livro/livros.xhtml";
    }

}
