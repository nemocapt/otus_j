package ru.otus.java_2018_08.student.hw14.server;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import ru.otus.java_2018_08.student.hw14.orm.UserDataSet;
import ru.otus.java_2018_08.student.hw14.orm.service.DBService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configurable
@AllArgsConstructor
@NoArgsConstructor
public class AuthServlet extends AbstractServlet {
    static final String URL_ADMIN = "admin";

    static final String PARAM_LOGIN = "login";
    static final String PARAM_AUTH = "auth";

    @Autowired
    private DBService service;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter(PARAM_LOGIN);
        UserDataSet loginName = service.readByName(login);

        if (loginName == null) {
            request.getSession().setAttribute(PARAM_AUTH, false);
            response.getWriter().print("unauthorized");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        request.getSession().setAttribute(PARAM_AUTH, true);
        response.sendRedirect(URL_ADMIN);
    }
}
