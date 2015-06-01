/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.bean;

import bpmlab.invio.bean.util.BeanUtil;
import bpmlab.invio.dao.AreaDAO;
import bpmlab.invio.entidade.Area;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class AreaBean {

    private AreaDAO areaDAO = new AreaDAO();
    private List<Area> areas;
    private Area area = new Area();

    /**
     * Creates a new instance of AreaBean
     */
        public AreaBean() {
    }

    public List<Area> getAreas() {
        if (areas == null) {
            areas = areaDAO.obterAreaOrdenada();
        }
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String salvar() {
        if (areaDAO.salvar(area)) {
            BeanUtil.criarMensagemDeInformacao(
                    "Operação realizada com sucesso",
                    "A área " + area.getNome() + " foi gravada com sucesso.");
        }else {
            BeanUtil.criarMensagemDeErro("Erro ao salvar a área", "Área: " + area.getNome());
        }
        return "listar.xhtml";
    }

    public String excluir() {
        if (areaDAO.excluir(area)) {
            BeanUtil.criarMensagemDeInformacao("Área excluída", "Área: " + area.getNome());
        } else {
            BeanUtil.criarMensagemDeErro("Erro ao excluir a área", "Área: " + area.getNome());
        }
        return "listar.xhtml";
    }
    
}
