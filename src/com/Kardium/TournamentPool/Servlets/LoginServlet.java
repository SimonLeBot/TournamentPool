package com.Kardium.TournamentPool.Servlets;

import com.Kardium.TournamentPool.Application.LoginVerifier;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.Kardium.TournamentPool.Servlets.ServletUtils.outputString;

public class LoginServlet extends HttpServlet {
    public static final long serialVersionUID = 1;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        int password = req.getParameter("password").hashCode();
        LoginVerifier loginVerifier = new LoginVerifier(name, password);
        if (loginVerifier.isValidPassword()) {
            validPasswordPage(resp);
        } else {
            invalidPasswordPage(resp);
        }
    }

    private void validPasswordPage(HttpServletResponse resp) throws IOException {
        outputString("\n" +
                        "<html><header><title>Euro 2020 -  Pool</title></header>" +
                        "<body><p>Home of the Kardium pool for the Euro 2020 (Soccer)</p>" +
                        "<img src=\"/images/UEFA_Euro_2020_Logo.png\" alt=\"Euro 2020 Logo\">" +
                        "<p>The password is valid</p></body></html>",
                "text/html",resp);

    }
    private void invalidPasswordPage(HttpServletResponse resp) throws IOException {
        outputString("\n" +
                        "<html><header><title>Euro 2020 -  Pool</title></header>" +
                        "<body><p>Home of the Kardium pool for the Euro 2020 (Soccer)</p>" +
                        "<img src=\"/images/UEFA_Euro_2020_Logo.png\" alt=\"Euro 2020 Logo\">" +
                        "<p>The password is invalid</p></body></html>",
                "text/html",resp);

    }
}