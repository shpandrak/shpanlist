package com.shpandrak.shpanlist.web;

import com.shpandrak.persistence.PersistenceException;
import com.shpandrak.shpanlist.model.*;
import com.shpandrak.shpanlist.model.auth.LoggedInUser;
import com.shpandrak.shpanlist.services.*;
import com.shpandrak.xml.EntityXMLConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Created with love
 * User: shpandrak
 * Date: 5/6/13
 * Time: 20:26
 */
@WebServlet("/doIt")
public class DoItServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoggedInUser loggedInUser = (LoggedInUser)request.getSession().getAttribute("user");
        String what = request.getParameter("what");
        try {

            response.setContentType("application/xml; charset=UTF-8");
            ResponsePrinter responsePrinter = doItPlease(request, response, loggedInUser, what);


            PrintWriter writer = response.getWriter();
            writer.print("<shpanlistResponse status=\"0\" message=\"Cool\">");
            responsePrinter.printResponse(writer);
            writer.print("</shpanlistResponse>");

        } catch (UserMustSignInException e){
            response.getWriter().print("<shpanlistResponse status=\"1\" message=\"User Must Sign In\" />");
            
        } catch (Exception e) {
            response.getWriter().print("<shpanlistResponse status=\"2\" message=\"" + e.getMessage() + "\" />");
            log("Failed " + what, e);
        }

    }

    private ResponsePrinter doItPlease(HttpServletRequest request, HttpServletResponse response, LoggedInUser loggedInUser, String what) throws IOException, PersistenceException, UserMustSignInException {
        // First Actions supported with no logged in user
        if ("signIn".equals(what)){
            return signIn(loggedInUser, request);
        }else if ("createUser".equals(what)){
            return createUser(loggedInUser, request);
        }else if ("signOut".equals(what)){
            return signOut(loggedInUser, request);
        }else{

            // For those actions user must be signed in

            if (loggedInUser == null){
                throw new UserMustSignInException();
            }else if ("getListTemplateFull".equals(what)){
                return getListTemplateFull(loggedInUser, request);
            }else if ("listUserData".equals(what)){
                return listUserData(loggedInUser, request);
            }else if ("getListInstanceFull".equals(what)){
                return getListInstanceFull(loggedInUser, request);
            }else if ("addListTemplateItem".equals(what)){
                return addListTemplateItem(loggedInUser, request, response);
            }else if ("addListInstanceItem".equals(what)){
                return addListInstanceItem(loggedInUser, request, response);
            }else if ("gotListInstanceItem".equals(what)){
                return gotListInstanceItem(loggedInUser, request, response);
            }else if ("bringBackItem".equals(what)){
                return bringBackItem(loggedInUser, request, response);
            }else if ("removeListTemplateItem".equals(what)){
                return removeListTemplateItem(loggedInUser, request, response);
            }else if ("removeListInstanceItem".equals(what)){
                return removeListInstanceItem(loggedInUser, request, response);
            }else if ("createListFromTemplate".equals(what)){
                return createListFromTemplate(loggedInUser, request, response);
            }else if ("pushListTemplateItemDown".equals(what)){
                return pushListTemplateItemDown(loggedInUser, request, response);
            }else if ("pushListTemplateItemUp".equals(what)){
                return pushListTemplateItemUp(loggedInUser, request, response);
            }else if ("pushListInstanceItemDown".equals(what)){
                return pushListInstanceItemDown(loggedInUser, request, response);
            }else if ("pushListInstanceItemUp".equals(what)){
                return pushListInstanceItemUp(loggedInUser, request, response);
            }else if ("removeListInstance".equals(what)){
                return removeListInstance(loggedInUser, request, response);
            }else {
                throw new IllegalArgumentException("Invalid action " + what);
            }
        }
    }

    private ResponsePrinter listUserData(LoggedInUser loggedInUser, HttpServletRequest request) throws PersistenceException, IOException {
        List<ListTemplate> userListTemplates = ListTemplateService.getUserLists(loggedInUser.getUserId());
        List<ListInstance> userListInstances = ListInstanceService.getUserLists(loggedInUser.getUserId());
        return new ResponsePrinterWrapper("userData", Arrays.<ResponsePrinter>asList(
                new DefaultResponsePrinter<ListTemplate>(new EntityXMLConverter<ListTemplate>(ListTemplate.DESCRIPTOR), userListTemplates), 
                new DefaultResponsePrinter<ListInstance>(new EntityXMLConverter<ListInstance>(ListInstance.DESCRIPTOR), userListInstances) 
        ));
        
    }

    private ResponsePrinter removeListInstance(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        ListInstanceService.removeListInstance(ListInstance.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceId));
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter signOut(LoggedInUser loggedInUser, HttpServletRequest request) {
        detachUserFromSession(request, loggedInUser);
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter createUser(LoggedInUser loggedInUser, HttpServletRequest request) throws PersistenceException {
        String userName = request.getParameter("userName").toLowerCase();
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        if (userName == null || userName.isEmpty()) throw new IllegalArgumentException("userName cannot be empty");
        if (firstName== null || firstName.isEmpty()) throw new IllegalArgumentException("firstName cannot be empty");
        if (lastName == null || lastName.isEmpty()) throw new IllegalArgumentException("lastName cannot be empty");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("password cannot be empty");

        String email = request.getParameter("email");
        ListUser user = ListUserService.createUser(userName, password, firstName, lastName, email);
        loggedInUser = new LoggedInUser(user.getId(), user.getUserName());
        attachUserToSession(request, loggedInUser);
        return new EmptyResponsePrinter();

    }


    private ResponsePrinter createListFromTemplate(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listTemplateId");
        ListInstance listInstance = ListInstanceService.createFromTemplate(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId), loggedInUser.getUserId());
        return new DefaultResponsePrinter<ListInstance>(new EntityXMLConverter<ListInstance>(ListInstance.DESCRIPTOR), listInstance);
    }

    private ResponsePrinter addListTemplateItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemName = request.getParameter("listTemplateItemName");
        String listTemplateItemDescription = request.getParameter("listTemplateItemDescription");
        String listTemplateItemDefaultAmount = request.getParameter("listTemplateItemDefaultAmount");
        Integer defaultAmount = null;
        if (listTemplateItemDefaultAmount != null && !listTemplateItemDefaultAmount.isEmpty()){
            defaultAmount = Integer.valueOf(listTemplateItemDefaultAmount);
        }
        ListTemplateService.addListTemplateItem(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId), listTemplateItemName, listTemplateItemDescription, defaultAmount);
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter addListInstanceItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemName = request.getParameter("listInstanceItemName");
        String listInstanceItemDescription = request.getParameter("listInstanceItemDescription");
        String listInstanceItemDefaultAmount = request.getParameter("listInstanceItemAmount");
        Integer amount = null;
        if (listInstanceItemDefaultAmount != null && !listInstanceItemDefaultAmount.isEmpty()){
            amount = Integer.valueOf(listInstanceItemDefaultAmount);
        }
        ListInstanceService.addListInstanceItem(ListInstance.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceId), listInstanceItemName, listInstanceItemDescription, amount);
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter removeListTemplateItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.removeListTemplateItem(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter removeListInstanceItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.removeListInstanceItem(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId));
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter pushListTemplateItemDown(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.pushListTemplateItemDown(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter pushListTemplateItemUp(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.pushListTemplateItemUp(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter pushListInstanceItemDown(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.pushListInstanceItemDown(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId));
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter pushListInstanceItemUp(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.pushListInstanceItemUp(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId));
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter gotListInstanceItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.gotItem(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId));
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter bringBackItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.gotItem(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId), false);
        return new EmptyResponsePrinter();
    }

    private ResponsePrinter getListTemplateFull(LoggedInUser loggedInUser, HttpServletRequest request) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listTemplateId");
        ListTemplate listTemplate = ListTemplateService.getListTemplateFull(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId));
        return new DefaultResponsePrinter<ListTemplate>(new EntityXMLConverter<ListTemplate>(ListTemplate.DESCRIPTOR),listTemplate);
    }

    private ResponsePrinter getListInstanceFull(LoggedInUser loggedInUser, HttpServletRequest request) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listInstanceId");
        ListInstance listInstance = ListInstanceService.getListInstanceFull(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId));
        return new DefaultResponsePrinter<ListInstance>(new EntityXMLConverter<ListInstance>(ListInstance.DESCRIPTOR), listInstance);
    }

    private ResponsePrinter signIn(LoggedInUser loggedInUser, HttpServletRequest request) throws IOException, PersistenceException {
        String userName = request.getParameter("userName").toLowerCase();
        String password = request.getParameter("password");

        ListUser listUser = ShpanlistAuthService.signIn(userName, password);

        if (listUser == null){
            throw new IllegalArgumentException("Don't know who you are..");
        }else {
            LoggedInUser user = new LoggedInUser(listUser.getId(), listUser.getUserName());
            attachUserToSession(request, user);
            return new DefaultResponsePrinter<ListUser>(new EntityXMLConverter<ListUser>(ListUser.DESCRIPTOR),listUser);
        }
        
    }

    private void attachUserToSession(HttpServletRequest request, LoggedInUser user) {
        request.getSession().setAttribute("user", user);
    }
    private void detachUserFromSession(HttpServletRequest request, LoggedInUser user) {
        request.getSession().removeAttribute("user");
    }

}
