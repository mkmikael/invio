/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.conversor;

import bpmlab.invio.entidade.Curriculo;
import bpmlab.invio.rn.CurriculoRN;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Junior
 */
@FacesConverter(value="curriculoConversor")
public class CurriculoConversor implements Converter{

    private CurriculoRN curriculoRN;
            
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
       Curriculo resposta = null;
        if (value == null || "".equals(value)) {
            return resposta;
        } else {
            curriculoRN = new CurriculoRN();
            resposta = curriculoRN.obter(new Integer(value));
            return resposta;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof Curriculo) {
            Integer id = ((Curriculo) value).getId();
            if (id != null) {
                return id.toString();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
    
}
