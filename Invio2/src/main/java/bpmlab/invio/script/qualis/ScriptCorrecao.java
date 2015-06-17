/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.script.qualis;

import bpmlab.invio.dao.JpaUtil;
import bpmlab.invio.entidade.Qualis;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author bpmlab
 */
public class ScriptCorrecao {
    public static void main(String[] args) {
        try {
            JpaUtil.getInstance().getEntityManagerFactory();
            EntityManager manager = JpaUtil.getInstance().getEntityManager();
            manager.getTransaction().begin();
            List<Qualis> lista = manager
                    .createQuery("select a from Qualis a where a.qualisPK.titulo like 'Revista Á%' order by a.qualisPK.titulo desc", Qualis.class)
                    .getResultList();
            for (Qualis q : lista) {
                manager.merge(q).getQualisPK().setTitulo(new String(
                        q.getQualisPK().getTitulo().getBytes()
                        , "utf8").replace("�", "").replace("Ãgua", "água").replace("Académica", "Acadêmica"));
                System.out.println(q.getQualisPK().getTitulo());
            }
            System.out.println(lista.size());
            manager.flush();
            manager.getTransaction().commit();
            manager.close();
            JpaUtil.getInstance().getEntityManagerFactory().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
