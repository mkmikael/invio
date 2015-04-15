/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invioscript;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
            fileWriter = new FileWriter("/home/bpmlab/inserts.sql");
            bufferedWriter = new BufferedWriter(fileWriter);
            Map<String, Object> params = ConstruirQualis.construir();
            List<Qualis> qualis = (List<Qualis>) params.get("qualis");
            List<String> areas = new ArrayList<>((Set<String>) params.get("areas"));
            
            bufferedWriter.write("insert into area(nome) values");
            for (int i = 0; i < areas.size() - 1; i++) {
                bufferedWriter.write("('" + areas.get(i) + "'),");
                bufferedWriter.newLine();
            }
            bufferedWriter.write("('" + areas.get(areas.size() - 1) + "');");
            bufferedWriter.newLine();
            bufferedWriter.write("insert into qualis(issn, titulo, estrato, areaavaliacao, status) values");
            for (int i = 0; i < qualis.size() - 1; i++) {
                bufferedWriter.write("('" + qualis.get(i).getIssn() + "', '" + qualis.get(i).getTitulo()
                        + "', '" + qualis.get(i).getEstrato() + "', '" + qualis.get(i).getAreaDeAvaliacao() + "', '"
                        + qualis.get(i).getStatus() + "'),");
                bufferedWriter.newLine();
            }
            bufferedWriter.write("('" + qualis.get(areas.size() - 1) + "');");
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
