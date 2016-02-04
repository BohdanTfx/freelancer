package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.util.SendMessageToEmail;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Rynik on 04.02.2016.
 */
public class AdminController extends HttpServlet implements Responsable {
    public static final Logger LOG = Logger.getLogger(CustomerController.class);
    private static final long serialVersionUID = -2356506023594947745L;
    private ObjectMapper mapper;
    private UserManager userManager;


    public AdminController() {
        mapper = new ObjectMapper();
        userManager = (UserManager) ApplicationContext.getInstance().getBean("userManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String path = FrontController.getPath(request);

            switch (path) {
//                case "admin/create/:
//                    createNewAdmin(request, response);
//                    break;
//                default:

            }

        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doGet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            switch (FrontController.getPath(request)) {
                case "admin/create/new/admin":
                    createNewAdmin(request, response);
                    break;
                case "admin/check/uuid":
                    checkAvailableUUID(request, response);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }


    private void createNewAdmin (HttpServletRequest request, HttpServletResponse response) throws IOException{
        String email = request.getParameter("email");

        String accessUUID = String.valueOf(UUID.randomUUID());
        while(!userManager.isUUIDAvailable(accessUUID)){
           accessUUID = String.valueOf(UUID.randomUUID());
        }
        String[] receivers = {email};
        String accessLink = request.getLocalAddr()+":"+request.getLocalPort()+"/#/signup?uuid="+accessUUID;
        SendMessageToEmail.sendFromGMail("onlineshopjava@gmail.com","ForTestOnly",receivers,"Freelancer -  Admin Registration ",getAdminCreatingMessage()+accessLink);

    }

    private String getAdminCreatingMessage(){
       return "Hey, you have permissions to sign up as administrator in Freelancer." +
                " Verifying this address will let you do this right now." +
                "If you was not notified about this message by persons of Freelancer, ignore this email, and contact us." +"\n\n"+
                " This offer will be available only 1 hour. If you wish to continue, please follow the link below: "+"\n";
    }

    private void checkAvailableUUID(HttpServletRequest request, HttpServletResponse response){
        String uuid = request.getParameter("uuid");

        sendResponse(response, true, mapper);
    }


}
