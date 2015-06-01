/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bpmlab.invio.config;

import bpmlab.invio.dao.JpaUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author bpmlab
 */
@WebListener
public class BootStrap implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(BootStrap.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        JpaUtil.getInstance().getEntityManagerFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
