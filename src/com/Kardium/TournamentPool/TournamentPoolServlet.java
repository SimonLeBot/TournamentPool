package com.Kardium.TournamentPool;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class TournamentPoolServlet extends HttpServlet {
    public static final long serialVersionUID = 1;
    private static final String HOST_PORT = "http://k1web04.kardium.local:8080/";

    private static final String HOME = "TournamentPool";
    private static final String HOME_2 = "index";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String urlString = req.getRequestURL().toString().replace(HOST_PORT, "");

        switch (urlString) {
            case HOME:
            case HOME_2:
                // return the home page
                break;
            default: // return error page
                break;
        }
        outputString("\n" +
                //"<html><header><title>Euro 2020 -  Pool</title></header><body><img src=\"resources/images/UEFA_Euro_2020_Logo.svg.png\" alt=\"Euro 2020 Logo\"></body></html>\n", "text/html",resp);
                "<html><header><title>Euro 2020 -  Pool</title></header>" +
                "<body><p>Home of the Kardium pool for the Euro 2020 (Soccer)</p>" +
                "<img src=\"/images/UEFA_Euro_2020_Logo.png\" alt=\"Euro 2020 Logo\">" +
                "<p>urlString is " + getRequest(urlString )+ "</p>" +
                "<p>Location is " + new File(".").getAbsolutePath()+ "</p>" +
                "<p>&nbspCome have fun and help me... I wanted to add the Logo of the competition, but I could not!</body></html>",
                "text/html",resp);
    }

    private String getRequest(String urlString) {
        return urlString.substring(urlString.lastIndexOf("\\") + 1).replaceAll(".html", "");
    }

    private void outputString(String report, String format, HttpServletResponse resp) throws IOException {
        resp.setContentType(format);
        PrintStream ps = new PrintStream(resp.getOutputStream());
        ps.println(report);
        ps.flush();
    }

}
