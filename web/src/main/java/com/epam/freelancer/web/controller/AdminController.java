package com.epam.freelancer.web.controller;

import com.epam.freelancer.business.context.ApplicationContext;
import com.epam.freelancer.business.manager.UserManager;
import com.epam.freelancer.business.service.AdminCandidateService;
import com.epam.freelancer.business.service.CustomerService;
import com.epam.freelancer.business.service.DeveloperService;
import com.epam.freelancer.business.util.SendMessageToEmail;
import com.epam.freelancer.database.model.AdminCandidate;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rynik on 04.02.2016.
 */
public class AdminController extends HttpServlet implements Responsable {
    public static final Logger LOG = Logger.getLogger(CustomerController.class);
    private static final long serialVersionUID = -2356506023594947745L;
    private ObjectMapper mapper;
    private UserManager userManager;
    private AdminCandidateService adminCandidateService;
    private DeveloperService developerService;
    private CustomerService customerService;


    public AdminController() {
        mapper = new ObjectMapper();
        userManager = (UserManager) ApplicationContext.getInstance().getBean("userManager");
        adminCandidateService = (AdminCandidateService) ApplicationContext.getInstance().getBean("adminCandidateService");
        developerService = (DeveloperService) ApplicationContext.getInstance().getBean("developerService");
        customerService = (CustomerService) ApplicationContext.getInstance().getBean("customerService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            System.out.println("asdasdasd");
            String path = FrontController.getPath(request);

            switch (path) {
                case "/admin/statistics":
                    sendDevAndCustAmount(request, response);
                    break;
                default:

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
                    sendLinkToCandidateMail(request, response);
                    break;
                case "admin/check/uuid":
                    checkAvailableUUID(request, response);
                    break;
                case "admin/remove/uuid":
                    removeUUID(request, response);
                    break;
                default:

            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.fatal(getClass().getSimpleName() + " - " + "doPost");
        }
    }


    private void sendLinkToCandidateMail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String accessUUID = String.valueOf(UUID.randomUUID());
        Map<String, String[]> map = new HashMap<>();

        while (!userManager.isUUIDAvailable(accessUUID)) {
            accessUUID = String.valueOf(UUID.randomUUID());
        }

        String arrEmail[] = {email};
        String arrAccessUUID[] = {accessUUID};
        map.put("email", arrEmail);
        map.put("access_key", arrAccessUUID);
        AdminCandidate adminCandidate = adminCandidateService.create(map);
        if (adminCandidate != null) {
            startCountdownExpireTime(adminCandidate, 20);
        }

        String accessLink = request.getLocalAddr() + ":" + request.getLocalPort() + "/#/signup?uuid=" + accessUUID;
        SendMessageToEmail.sendFromGMail("onlineshopjava@gmail.com", "ForTestOnly", arrEmail, "Freelancer -  Admin Registration ", getAdminCreatingMessage() + accessLink);
    }

    private void startCountdownExpireTime(AdminCandidate candidate, int secDelay) {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(() ->  adminCandidateService.remove(candidate)
        , secDelay, TimeUnit.HOURS);
    }

    private String getAdminCreatingMessage() {
        return "Hey, you have permissions to sign up as administrator in Freelancer." +
                " Verifying this address will let you do this right now." +
                "If you was not notified about this message by persons of Freelancer, ignore this email, and contact us." + "\n\n" +
                " This offer will be available only 1 hour. If you wish to continue, please follow the link below: " + "\n";
    }

    private void checkAvailableUUID(HttpServletRequest request, HttpServletResponse response) {
        String uuid = request.getParameter("uuid");
        if (adminCandidateService.getAdminCandidateByKey(uuid) != null) {
            sendResponse(response, true, mapper);
        } else {
            sendResponse(response, false, mapper);
        }
    }

    private void removeUUID(HttpServletRequest request, HttpServletResponse response) {
        String uuid = request.getParameter("uuid");
        adminCandidateService.remove(adminCandidateService.getAdminCandidateByKey(uuid));
    }

    private void sendDevAndCustAmount(HttpServletRequest request,HttpServletResponse response) throws IOException{
        System.out.println("getstatistics");
        Map<String,Integer> map = new HashMap<>();
        map.put("devAmount",developerService.getAllWorkers().size());
        map.put("custAmount",customerService.findAll().size());

        sendResponse(response,map,mapper);
    }
}
