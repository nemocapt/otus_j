package ru.otus.java_2018_08.student.hw12.server;

import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class LoginServlet extends HttpServlet {
    static final String LOGIN_DOC = "login.html";

    private TemplateProcessor processor;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().print(processor.getPage(LOGIN_DOC, null));
    }
}
