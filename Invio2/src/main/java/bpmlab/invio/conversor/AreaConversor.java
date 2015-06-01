package bpmlab.invio.conversor;

import bpmlab.invio.dao.AreaDAO;
import bpmlab.invio.entidade.Area;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "areaConversor")
public class AreaConversor implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || "".equals(value)) {
            return null;
        } else {
            AreaDAO areaDAO = new AreaDAO();
            return areaDAO.obter(Area.class, new Integer(value));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof Area) {
            Integer id = ((Area) value).getId();
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