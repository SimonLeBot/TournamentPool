package com.Kardium.TournamentPool.Servlets;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;

class ServletUtils {
    static void outputString(String report, String format, HttpServletResponse resp) throws IOException {
        resp.setContentType(format);
        PrintStream ps = new PrintStream(resp.getOutputStream());
        ps.println(report);
        ps.flush();
    }

}
