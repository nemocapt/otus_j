package ru.otus.java_2018_08.student.hw12.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.java_2018_08.student.hw12.morm.UserDataSet;
import ru.otus.java_2018_08.student.hw12.morm.service.DBService;
import ru.otus.java_2018_08.student.hw12.morm.service.DBServiceHibernateImpl;

public class MyServer {
    private final static int PORT = 8080;
    private final static String PUBLIC_HTML = "public_html";

    private int port;
    private String dir;

    private Server server;
    private DBService dbService;

    public MyServer() {
        port = PORT;
        dir = PUBLIC_HTML;
    }

    public MyServer(int port, String dir) {
        this.port = port;
        this.dir = dir;
    }

    public void start() throws Exception {
        dbService = new DBServiceHibernateImpl("maria_jpa");
        dbService.save(new UserDataSet("user"));

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(dir);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        context.addServlet(new ServletHolder(new AuthServlet(dbService)), "/auth");
        context.addServlet(new ServletHolder(new LoginServlet(templateProcessor)), "/login");
        context.addServlet(new ServletHolder(new AdminServlet(templateProcessor, dbService)), "/admin");

        server = new Server(port);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
        dbService.shutdown();
    }
}
