/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.script.qualis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author Dedo
 */
public class Main {

    public static void main(String[] args) throws IOException {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        escreverArquivo(LerXls.listaDeQualis(chooser.getSelectedFile()),
                LerXls.listaDeArea(chooser.getSelectedFile()));
    }

    public static void escreverArquivo(List<Qualis> listaQualis, List<String> listaAreas) throws IOException {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter("/home/bpmlab/inserts.sql");
            bufferedWriter = new BufferedWriter(fileWriter);
            List<Qualis> qualis = listaQualis;
            List<String> areas = listaAreas;

            bufferedWriter.write("insert into area(nome) values");
            for (int i = 0; i < areas.size() - 1; i++) {
                bufferedWriter.write("('" + areas.get(i) + "'),");
                bufferedWriter.newLine();
            }
            bufferedWriter.write("('" + areas.get(areas.size() - 1) + "');");
            bufferedWriter.newLine();
            for (int i = 0; i < qualis.size() - 1; i++) {
                bufferedWriter.write("insert into qualis(issn, titulo, estrato, areaavaliacao, status) values");
                bufferedWriter.write("('" + qualis.get(i).getIssn() + "', '" + qualis.get(i).getTitulo().replace("'", "''")
                        + "', '" + qualis.get(i).getEstrato() + "', '" + qualis.get(i).getAreaDeAvaliacao() + "', '"
                        + qualis.get(i).getStatus() + "');");
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
