/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.script.qualis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author bpmlab
 */
public class LerXls {
    
    public static List<Qualis> listaDeQualis(File file) {
        List<Qualis> lista = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);
            int[] pages = {60001, 48850};
            for (int i = 0; i < 2; i++) {
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                HSSFRow row;
                for (int j = 1; j < pages[i]; j++) {
                    row = sheet.getRow(j);
                    String issn = row.getCell(0).getStringCellValue().trim();
                    String titulo = row.getCell(1).getStringCellValue().trim();
                    String estrato = row.getCell(2).getStringCellValue().trim();
                    String area = row.getCell(3).getStringCellValue().trim();
                    String status = row.getCell(4).getStringCellValue().trim();
                    if (issn != null && !issn.isEmpty()) {
                        Qualis qualis = new Qualis(issn, titulo, estrato, area, status);
                        lista.add(qualis);
                    }
                }
            }
            return lista;
        } catch (IOException e) {
            return null;
        }
    } 
    
    public static List<String> listaDeArea(File file) {
        Set<String> lista = new HashSet<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);
            int[] pages = {60001, 48850};
            for (int i = 0; i < 2; i++) {
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                HSSFRow row;
                for (int j = 1; j < pages[i]; j++) {
                    row = sheet.getRow(j);
                    String area = row.getCell(3).getStringCellValue().trim();
                    lista.add(area);
                }
            }
            return new ArrayList<>(lista);
        } catch (IOException e) {
            return null;
        }
    }
}
