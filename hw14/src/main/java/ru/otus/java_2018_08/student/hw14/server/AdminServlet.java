package ru.otus.java_2018_08.student.hw14.server;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.java_2018_08.student.hw14.orm.UserDataSet;
import ru.otus.java_2018_08.student.hw14.orm.service.DBService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configurable
@NoArgsConstructor
public class AdminServlet extends AbstractServlet {
    static final String LOGIN_DOC = "admin.html";
    static final String PARAM_ADD = "add";
    static final String PARAM_ID = "id";
    static final String PARAM_MAX = "max";

    static final String PARAM_AUTH = "auth";

    @Autowired
    private TemplateProcessor processor;
    @Autowired
    private DBService service;

    public AdminServlet(TemplateProcessor processor, DBService service) {
        this.processor = processor;
        this.service = service;
        WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!(boolean)request.getSession().getAttribute(PARAM_AUTH)) {
            response.getWriter().print("unauthorized");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Map<String, Object> params = new HashMap<>();

        String add = request.getParameter(PARAM_ADD);
        if (add != null) {
            UserDataSet user = new UserDataSet(add);
            service.save(user);
            params.put(PARAM_ADD, user.toString());
        }

        String id = request.getParameter(PARAM_ID);
        if (id != null) {
            UserDataSet user = service.read(Long.parseLong(id));
            params.put(PARAM_ID, user == null ? "empty" : user.toString());
        }

        String max = request.getParameter(PARAM_MAX);
        if (max != null) {
            params.put(PARAM_MAX, service.readAll().size());
        }

        response.getWriter().print(processor.getPage(LOGIN_DOC, params));
    }
}
