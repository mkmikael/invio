/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.bean;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author bpmlab
 */
@ApplicationScoped
@ManagedBean
public class ConfiguracaoBean implements Serializable {

    private boolean isPeriodoDeAvaliacao = false;

    public boolean isIsPeriodoDeAvaliacao() {
        return isPeriodoDeAvaliacao;
    }

    public void setIsPeriodoDeAvaliacao(boolean isPeriodoDeAvaliacao) {
        this.isPeriodoDeAvaliacao = isPeriodoDeAvaliacao;
    }
    
}
