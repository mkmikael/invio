/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.conversor;

import bpmlab.invio.entidade.Livro;
import bpmlab.invio.rn.LivroRN;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Junior
 */
@FacesConverter(value = "livroConversor")
public class LivroConversor implements Converter {

    private LivroRN livroRN;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Livro resposta = null;
        if (value == null || "".equals(value)) {
            return resposta;
        } else {
            livroRN = new LivroRN();
            resposta = livroRN.obter(new Integer(value));
            return resposta;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof Livro) {
            Integer id = ((Livro) value).getId();
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