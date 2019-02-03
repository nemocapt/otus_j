package ru.otus.java_2018_08.student.hw14.server;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.java_2018_08.student.hw14.orm.service.DBService;

import javax.servlet.http.HttpServlet;

abstract public class AbstractServlet extends HttpServlet {
    protected WebApplicationContext context;

    @Override
    public void init() {
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

    protected DBService getServiceByBean() {
        return context.getBean("dbService", DBService.class);
    }

    protected TemplateProcessor getProcessorByBean() {
        return context.getBean("templateProcessor", TemplateProcessor.class);
    }
}
