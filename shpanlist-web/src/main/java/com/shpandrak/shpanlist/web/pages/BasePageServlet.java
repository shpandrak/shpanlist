package com.shpandrak.shpanlist.web.pages;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.web.HtmlResponsePrinter;
import com.shpandrak.shpanlist.web.UserMustSignInException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with love
 * User: shpandrak
 * Date: 7/13/13
 * Time: 23:04
 */
public abstract class BasePageServlet extends HttpServlet{

    public boolean requiresLogin(){
        return true;
    }

    public static String getHeaderConstants() {
        return "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "\t<link rel=\"stylesheet\" href=\"/include/jquery.mobile-1.3.1/jquery.mobile-1.3.1.min.css\" />\n" +
                "\t<script src=\"/include/jquery-1.9.1.min.js\"></script>\n" +
                "\t<script src=\"/include/jquery.mobile-1.3.1/jquery.mobile-1.3.1.min.js\"></script>\n" +
                "\t<script src=\"/ShpanlistController.js\"></script>\n" +
                "\t<script src=\"/homePageView.js\"></script>\n" +
                "\t<script src=\"/listTemplatePageView.js\"></script>\n" +
                "\t<script src=\"/listInstancePageView.js\"></script>\n" +
                "\t<script src=\"/listInstanceEditPageView.js\"></script>\n";
    }

    public abstract HtmlResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException;

    public abstract boolean drawPage(HttpServletRequest request, HttpServletResponse response, StringBuilder sb, LoggedInUser loggedInUser) throws IOException, PersistenceException;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        response.setContentType("text/html; charset=UTF-8");
        boolean successfullyProcessed = true;
        StringBuilder sb = new StringBuilder() ;
        try{
            LoggedInUser loggedInUser = (LoggedInUser)request.getSession().getAttribute("user");
            if (loggedInUser == null && requiresLogin()){
                response.sendRedirect("/signIn");
                return;
            }

            // Append Page Prefix
            appendPagePrefix(sb);

            // Draw the page
            boolean shouldContinue = drawPage(request, response, sb, loggedInUser);
            if (!shouldContinue){
                return;
            }

            // Append Page Suffix
            appendPageSuffix(sb);



        } catch (Exception ex) {
            successfullyProcessed = false;
            log("oops", ex);
            PrintWriter pw = response.getWriter();

            //todo: style error
            pw.append("<h2>").append(ex.getMessage()).append("</h2><br/>").append(
                    "Oops! I didn't see this ugly error coming.. oh well, try again sometime soon..");

        }finally {
            if (successfullyProcessed){
                response.getWriter().append(sb);
            }
        }
    }

    protected void appendPageSuffix(StringBuilder sb){
        sb.append("\n</html>");
    }


    private void appendPagePrefix(StringBuilder sb) {
        sb.append(
                "<!DOCTYPE html>\n" +
                "<html>\n");
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoggedInUser loggedInUser = (LoggedInUser)request.getSession().getAttribute("user");
        String what = request.getParameter("what");
        try {

            response.setContentType("application/json; charset=UTF-8");
            HtmlResponsePrinter responsePrinter = doItPlease(request, response, loggedInUser, what);

            PrintWriter writer = response.getWriter();
            writer.print("{\"status\":0, \"message\":\"Cool\"");
            responsePrinter.printResponse(writer);
            writer.print("}");

        } catch (UserMustSignInException e){
            response.getWriter().print("{\"status\":1, \"message\":\"User Must Sign In\"}");

        } catch (Exception e) {
            response.getWriter().print("{\"status\":1, \"message\":\"" + e.getMessage() + "\"}");
            log("Failed " + what, e);
        }
    }

    protected String encodeHtmlForJSONTransport(StringBuilder sb) {
        return sb.toString().replaceAll("\\\"", "\\\\\"").replaceAll("\n", "");
    }


}
