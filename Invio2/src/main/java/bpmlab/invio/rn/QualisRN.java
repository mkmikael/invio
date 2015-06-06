/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.rn;

import bpmlab.invio.dao.QualisDAO;
import java.util.List;

/**
 *
 * @author bpmlab
 */
public class QualisRN {

    private QualisDAO dao = new QualisDAO();

    public int obterEstrato(String titulo, String area) {
        String estrato = dao.obterEstrato(titulo, area);
        if (estrato == null) {
            return 0;
        }
        switch (estrato) {
            case "A1":
                return 50;
            case "A2":
                return 45;
            case "B1":
                return 40;
            case "B2":
                return 30;
            case "B3":
                return 25;
            case "B4":
                return 20;
            case "B5":
                return 10;
            case "C":
                return 5;
            default:
                return 0;
        }
    }

    public List<String> obterTodosTitulos(String palavra, int maxResultados) {
        return dao.obterTodosTitulos(palavra, maxResultados);
    }

    public List<String> obterPorArea(String area) {
        return dao.obterPorArea(area);
    }

    public List<String> obterTodosTitulosArea(String palavra) {
        return dao.obterTodosTitulosArea(palavra, 20);
    }

}
