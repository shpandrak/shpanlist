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


            if ("signIn".equals(what)){
                signIn(loggedInUser, request, response);
            }else if ("listListGroups".equals(what)){
                listListGroups(loggedInUser, request, response);
            }else if ("getListGroup".equals(what)){
                getListGroup(loggedInUser, request, response);
            }else if ("getListTemplateFull".equals(what)){
                getListTemplateFull(loggedInUser, request, response);
            }else if ("getListInstanceFull".equals(what)){
                getListInstanceFull(loggedInUser, request, response);
            }else if ("addListTemplateItem".equals(what)){
                addListTemplateItem(loggedInUser, request, response);
            }else if ("gotListInstanceItem".equals(what)){
                gotListInstanceItem(loggedInUser, request, response);
            }else if ("bringBackItem".equals(what)){
                bringBackItem(loggedInUser, request, response);
            }else if ("removeListTemplateItem".equals(what)){
                removeListTemplateItem(loggedInUser, request, response);
            }else if ("createListFromTemplate".equals(what)){
                createListFromTemplate(loggedInUser, request, response);
            }else if ("pushListTemplateItemDown".equals(what)){
                pushListTemplateItemDown(loggedInUser, request, response);
            }else if ("pushListTemplateItemUp".equals(what)){
                pushListTemplateItemUp(loggedInUser, request, response);
            }else if ("createUser".equals(what)){
                createUser(loggedInUser, request, response);
            }else if ("signOut".equals(what)){
                signOut(loggedInUser, request, response);
            }else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid action " + what);
            }

        } catch (PersistenceException e) {
            log("Failed " + what, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    private void signOut(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) {
        detachUserFromSession(request, loggedInUser);
    }

    private void createUser(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        ListUser user = ListUserService.createUser(userName, password, firstName, lastName, email);
        loggedInUser = ShpanlistAuthService.signIn(userName, password);
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

    private void removeListTemplateItem(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException {
        String listTemplateId = request.getParameter("listTemplateId");
        String listTemplateItemId = request.getParameter("listTemplateItemId");
        ListTemplateService.removeListTemplateItem(ListTemplateItem.DESCRIPTOR.idFieldDescriptor.fromString(listTemplateItemId));
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

    private void getListGroup(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        String listGroupId = request.getParameter("listGroupId");
        ListGroup listGroup = ListGroupService.getListGroup(ListGroup.DESCRIPTOR.idFieldDescriptor.fromString(listGroupId));
        response.getWriter().print(new EntityXMLConverter<ListGroup>(ListGroup.DESCRIPTOR).toXML(listGroup));
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

    private void listListGroups(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws PersistenceException, IOException {
        List<ListGroup> listGroups = ListGroupService.getListGroups(loggedInUser);
        response.getWriter().print(new EntityXMLConverter<ListGroup>(ListGroup.DESCRIPTOR).toXML(listGroups));
    }


    private void signIn(LoggedInUser loggedInUser, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        try {
            LoggedInUser user = ShpanlistAuthService.signIn(userName, password);
            if (user == null){
                response.getWriter().println("Boooo!");
            }else {
                attachUserToSession(request, user);
                response.getWriter().println("Yey!");
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
