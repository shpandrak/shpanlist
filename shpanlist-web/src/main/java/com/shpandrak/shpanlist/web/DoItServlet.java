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
            if ("signIn".equals(what)){
                signIn(loggedInUser, request, response);
            }else if ("listUserData".equals(what)){
                listUserData(loggedInUser, request, response);
            }else if ("getListTemplateFull".equals(what)){
                getListTemplateFull(loggedInUser, request, response);
            }else if ("getListInstanceFull".equals(what)){
                getListInstanceFull(loggedInUser, request, response);
            }else if ("addListTemplateItem".equals(what)){
                addListTemplateItem(loggedInUser, request, response);
            }else if ("addListInstanceItem".equals(what)){
                addListInstanceItem(loggedInUser, request, response);
            }else if ("gotListInstanceItem".equals(what)){
                gotListInstanceItem(loggedInUser, request, response);
            }else if ("bringBackItem".equals(what)){
                bringBackItem(loggedInUser, request, response);
            }else if ("removeListTemplateItem".equals(what)){
                removeListTemplateItem(loggedInUser, request, response);
            }else if ("removeListInstanceItem".equals(what)){
                removeListInstanceItem(loggedInUser, request, response);
            }else if ("createListFromTemplate".equals(what)){
                createListFromTemplate(loggedInUser, request, response);
            }else if ("pushListTemplateItemDown".equals(what)){
                pushListTemplateItemDown(loggedInUser, request, response);
            }else if ("pushListTemplateItemUp".equals(what)){
                pushListTemplateItemUp(loggedInUser, request, response);
            }else if ("pushListInstanceItemDown".equals(what)){
                pushListInstanceItemDown(loggedInUser, request, response);
            }else if ("pushListInstanceItemUp".equals(what)){
                pushListInstanceItemUp(loggedInUser, request, response);
            }else if ("createUser".equals(what)){
                createUser(loggedInUser, request, response);
            }else if ("signOut".equals(what)){
                signOut(loggedInUser, request, response);
            }else if ("removeListInstance".equals(what)){
                removeListInstance(loggedInUser, request, response);
            }else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid action " + what);
            }



        } catch (PersistenceException e) {
            log("Failed " + what, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private void listUserData(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        List<ListTemplate> userListTemplates = ListTemplateService.getUserLists(loggedInUser.getUserId());
        List<ListInstance> userListInstances = ListInstanceService.getUserLists(loggedInUser.getUserId());
        response.getWriter().print("<userData>");
        response.getWriter().print(new EntityXMLConverter<ListTemplate>(ListTemplate.DESCRIPTOR).toXML(userListTemplates));
        response.getWriter().print(new EntityXMLConverter<ListInstance>(ListInstance.DESCRIPTOR).toXML(userListInstances));
        response.getWriter().print("</userData>");
        
    }

    private void removeListInstance(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        ListInstanceService.removeListInstance(ListInstance.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceId));
    }

    private void signOut(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) {
        detachUserFromSession(request, loggedInUser);
    }

    private void createUser(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
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

    }


    private void createListFromTemplate(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listTemplateId");
        ListInstance listInstance = ListInstanceService.createFromTemplate(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId), loggedInUser.getUserId());
        response.getWriter().print(new EntityXMLConverter<ListInstance>(ListInstance.DESCRIPTOR).toXML(listInstance));
    }

    private void addListTemplateItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemName = request.getParameter("listTemplateItemName");
        String listTemplateItemDescription = request.getParameter("listTemplateItemDescription");
        String listTemplateItemDefaultAmount = request.getParameter("listTemplateItemDefaultAmount");
        Integer defaultAmount = null;
        if (listTemplateItemDefaultAmount != null && !listTemplateItemDefaultAmount.isEmpty()){
            defaultAmount = Integer.valueOf(listTemplateItemDefaultAmount);
        }
        ListTemplateService.addListTemplateItem(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId), listTemplateItemName, listTemplateItemDescription, defaultAmount);
    }

    private void addListInstanceItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemName = request.getParameter("listInstanceItemName");
        String listInstanceItemDescription = request.getParameter("listInstanceItemDescription");
        String listInstanceItemDefaultAmount = request.getParameter("listInstanceItemAmount");
        Integer amount = null;
        if (listInstanceItemDefaultAmount != null && !listInstanceItemDefaultAmount.isEmpty()){
            amount = Integer.valueOf(listInstanceItemDefaultAmount);
        }
        ListInstanceService.addListInstanceItem(ListInstance.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceId), listInstanceItemName, listInstanceItemDescription, amount);
    }

    private void removeListTemplateItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.removeListTemplateItem(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));
    }

    private void removeListInstanceItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.removeListInstanceItem(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId));
    }

    private void pushListTemplateItemDown(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.pushListTemplateItemDown(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));
    }

    private void pushListTemplateItemUp(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.pushListTemplateItemUp(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));
    }

    private void pushListInstanceItemDown(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.pushListInstanceItemDown(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId));
    }

    private void pushListInstanceItemUp(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.pushListInstanceItemUp(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId));
    }

    private void gotListInstanceItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.gotItem(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId));
    }

    private void bringBackItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listInstanceId = request.getParameter("listInstanceId");
        String listInstanceItemId = request.getParameter("listInstanceItemId");
        ListInstanceService.gotItem(ListInstanceItem.DESCRIPTOR.idFieldDescriptor.fromString(listInstanceItemId), false);
    }

    private void getListTemplateFull(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listTemplateId");
        ListTemplate listTemplate = ListTemplateService.getListTemplateFull(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId));
        response.getWriter().print(new EntityXMLConverter<ListTemplate>(ListTemplate.DESCRIPTOR).toXML(listTemplate));
    }

    private void getListInstanceFull(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listTemplateId = request.getParameter("listInstanceId");
        ListInstance listInstance = ListInstanceService.getListInstanceFull(ListTemplate.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateId));
        response.getWriter().print(new EntityXMLConverter<ListInstance>(ListInstance.DESCRIPTOR).toXML(listInstance));
    }

    private void signIn(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName").toLowerCase();
        String password = request.getParameter("password");

        try {
            ListUser listUser = ShpanlistAuthService.signIn(userName, password);

            if (listUser == null){
                response.getWriter().println("Boooo!");
            }else {
                LoggedInUser user = new LoggedInUser(listUser.getId(), listUser.getUserName());
                attachUserToSession(request, user);
                response.getWriter().print(new EntityXMLConverter<ListUser>(ListUser.DESCRIPTOR).toXML(listUser));
            }
        } catch (PersistenceException e) {
            response.getWriter().println("Baahhhh" + e.getMessage());
        }
    }

    private void attachUserToSession(HttpServletRequest request, LoggedInUser user) {
        request.getSession().setAttribute("user", user);
    }
    private void detachUserFromSession(HttpServletRequest request, LoggedInUser user) {
        request.getSession().removeAttribute("user");
    }

}
