package ru.otus.java_2018_08.student.hw12.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java_2018_08.student.hw12.morm.UserDataSet;
import ru.otus.java_2018_08.student.hw12.server.Admin;
import ru.otus.java_2018_08.student.hw12.server.Auth;
import ru.otus.java_2018_08.student.hw12.server.Login;
import ru.otus.java_2018_08.student.hw12.server.TemplateProcessor;
import ru.otus.java_2018_08.student.hw12.service.DBService;
import ru.otus.java_2018_08.student.hw12.service.DBServiceHibernateImpl;

public class Main {
    static final private Logger log = LoggerFactory.getLogger(Main.class);

    private final static int PORT = 8080;
    private final static String PUBLIC_HTML = "public_html";

    static public void main(String[] args) throws Exception {
        DBService serviceHibernate = new DBServiceHibernateImpl("maria_jpa");
        serviceHibernate.save(new UserDataSet("user"));

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        context.addServlet(new ServletHolder(new Auth(serviceHibernate)), "/auth");
        context.addServlet(new ServletHolder(new Login(templateProcessor)), "/login");
        context.addServlet(new ServletHolder(new Admin(templateProcessor, serviceHibernate)), "/admin");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();

        serviceHibernate.shutdown();
    }
}
