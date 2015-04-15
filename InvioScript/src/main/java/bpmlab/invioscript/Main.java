/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invioscript;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Dedo
 */
public class Main {

    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter("C:\\users\\Dedo\\Documents\\inserts.sql");
            bufferedWriter = new BufferedWriter(fileWriter);
            Map<String, Object> params = ConstruirQualis.construir();
            List<Qualis> qualis = (List<Qualis>) params.get("qualis");
            Set<String> areas = (Set<String>) params.get("areas");
            for (String area : areas) {
                bufferedWriter.write("insert into area(nome) values('" + area + "');");
                bufferedWriter.newLine();
            }
            for (Qualis q : qualis) {
                bufferedWriter.write("insert into qualis(issn, titulo, estrato, areaavaliacao, status) "
                        + "values('" + q.getIssn() + "', '" + q.getTitulo() + "', '" + q.getEstrato() + "', '" + q.getAreaDeAvaliacao() + "', '" + q.getStatus() + "');");
                bufferedWriter.newLine();
            }
        } catch (Exception e) {
            System.out.println("ERRO!");
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }
}
