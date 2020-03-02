package com.Kardium.TournamentPool;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class TournamentPoolServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        outputString("\n" +
                //"<html><header><title>Euro 2020 -  Pool</title></header><body><img src=\"resources/images/UEFA_Euro_2020_Logo.svg.png\" alt=\"Euro 2020 Logo\"></body></html>\n", "text/html",resp);
                "<html><header><title>Euro 2020 -  Pool</title></header>" +
                "<body></body><p>Home of the Kardium pool for the Euro 2020 (Soccer)</p>" +
                "<p>&nbspCome have fun and help me... I wanted to add the Logo of the competition, but I could not!</html>",
                "text/html",resp);
    }

    private void outputString(String report, String format, HttpServletResponse resp) throws IOException {
        resp.setContentType(format);
        PrintStream ps = new PrintStream(resp.getOutputStream());
        ps.println(report);
        ps.flush();
    }

}
