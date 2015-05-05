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
        if (estrato.equals("A1")) {
            return 50;
        } else if (estrato.equals("A2")) {
            return 45;
        } else if (estrato.equals("B1")) {
            return 40;
        } else if (estrato.equals("B2")) {
            return 30;
        } else if (estrato.equals("B3")) {
            return 25;
        } else if (estrato.equals("B4")) {
            return 20;
        } else if (estrato.equals("B5")) {
            return 10;
        } else if (estrato.equals("C")) {
            return 5;
        } else {
            return 0;
        }
    }

    public List<String> obterTodosTitulos(String palavra, int maxResultados) {
        return dao.obterTodosTitulos(palavra, maxResultados);
    }

    public List<String> obterPorArea(String area, String palavra) {
        return dao.obterPorArea(area, palavra, 20);
    }

    public List<String> obterTodosTitulosArea(String palavra) {
        return dao.obterTodosTitulosArea(palavra, 20);
    }

}
