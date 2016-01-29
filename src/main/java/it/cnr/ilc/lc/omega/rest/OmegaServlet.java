package it.cnr.ilc.lc.omega.rest;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import sirius.kernel.Setup;
import sirius.kernel.Sirius;

/**
 *
 * @author oakgen
 */
@WebServlet(urlPatterns = "/core", loadOnStartup = 1)
public class OmegaServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Setup.createAndStartEnvironment(getClass().getClassLoader());
        System.out.println("SIRIUS STARTED");
    }

    @Override
    public void destroy() {
        Sirius.stop();
        System.out.println("SIRIUS STOPPED");
    }

    
}
