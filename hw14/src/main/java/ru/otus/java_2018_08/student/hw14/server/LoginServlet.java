package ru.otus.java_2018_08.student.hw14.server;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
public class LoginServlet extends AbstractServlet {
    static final String LOGIN_DOC = "login.html";

    private TemplateProcessor processor;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().print(processor.getPage(LOGIN_DOC, null));
    }

    @Override
    public void init() {
        super.init();

        if (processor == null) {
            processor = getProcessorByBean();
        }
    }
}
