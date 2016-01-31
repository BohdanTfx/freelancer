package com.epam.freelancer.business.util;

/**
 * Created by Bohdan on 20.01.2016.
 */

import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class SmsSender {

    private static final Logger LOG = Logger.getLogger(SmsSender.class);
    private final String LOGIN;
    private final String PASSWORD;
    private final String CHARSET;
    private final boolean FLAG_HTTPS;
    private final boolean FLAG_DEBUG;
    private final boolean FLAG_POST;


    public SmsSender() {
        LOGIN = "e-freelance";
        PASSWORD = "ForTestOnly";
        CHARSET = "utf-8";
        FLAG_HTTPS = false;
        FLAG_DEBUG = false;
        FLAG_POST = false;
    }

    public SmsSender(String login, String password) {
        LOGIN = login;
        PASSWORD = password;
        CHARSET = "utf-8";
        FLAG_HTTPS = false;
        FLAG_POST = false;
        FLAG_DEBUG = false;
    }

    public SmsSender(String login, String password, String charset) {
        LOGIN = login;
        PASSWORD = password;
        CHARSET = charset;
        FLAG_HTTPS = false;
        FLAG_POST = false;
        FLAG_DEBUG = false;
    }

    public SmsSender(String login, String password, String charset, boolean flagHttps) {
        LOGIN = login;
        PASSWORD = password;
        CHARSET = charset;
        FLAG_HTTPS = flagHttps;
        FLAG_POST = false;
        FLAG_DEBUG = false;

    }

    public SmsSender(String login, String password, String charset, boolean flagHttps, boolean flagPost) {
        LOGIN = login;
        PASSWORD = password;
        CHARSET = charset;
        FLAG_HTTPS = flagHttps;
        FLAG_POST = flagPost;
        FLAG_DEBUG = false;
    }

    public SmsSender(String login, String password, String charset, boolean flagHttps, boolean flagPost, boolean flagDebug) {
        LOGIN = login;
        PASSWORD = password;
        CHARSET = charset;
        FLAG_HTTPS = flagHttps;
        FLAG_POST = flagPost;
        FLAG_DEBUG = flagDebug;
    }

    private static String implode(String[] array, String delim) {
        String out = "";

        for (int i = 0; i < array.length; i++) {
            if (i != 0)
                out += delim;
            out += array[i];
        }

        return out;
    }

    public String[] sendSms(String phone, String message, String sender) {
        return send(phone, message, 0, "", "", 0, sender, "");
    }

    public String[] sendGroupSms(List<String> phones, String message, String sender) {

        String phone = String.join(",", phones);
        return send(phone, message, 0, "", "", 0, sender, "");
    }

    public String[] getSmsCost(String phones, String message, int translit, int format, String sender, String query) {
        String[] formats = {"", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1"};
        String[] infoAboutMessage = {};

        try {
            infoAboutMessage = smsSendCmd("send", "cost=1&phones=" + URLEncoder.encode(phones, CHARSET)
                    + "&mes=" + URLEncoder.encode(message, CHARSET)
                    + "&translit=" + translit + (format > 0 ? "&" + formats[format] : "")
                    + (sender == "" ? "" : "&sender=" + URLEncoder.encode(sender, CHARSET))
                    + (query == "" ? "" : "&" + query));
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Some problem with SMS, when get SMS Cost" + e.getMessage());
        }

        if (FLAG_DEBUG) {
            if (Integer.parseInt(infoAboutMessage[1]) > 0) {
                LOG.info("Price: " + infoAboutMessage[0] + "Number of sent SMS " + infoAboutMessage[1]);
            } else {
                LOG.info("Error #" + Math.abs(Integer.parseInt(infoAboutMessage[1])));
            }
        }

        return infoAboutMessage;
    }

    public String[] getStatus(int id, String phone, int all) {
        String[] infoAboutMessage = {};
        String temporary;

        try {
            infoAboutMessage = smsSendCmd("status", "phone=" + URLEncoder.encode(phone, CHARSET) + "&id=" + id + "&all=" + all);

            if (FLAG_DEBUG) {
                if (infoAboutMessage[1] != "" && Integer.parseInt(infoAboutMessage[1]) >= 0) {
                    LOG.info("Status SMS = " + infoAboutMessage[0]);
                } else
                    LOG.info("Error #" + Math.abs(Integer.parseInt(infoAboutMessage[1])));
            }

            if (all == 1 && infoAboutMessage.length > 9 && (infoAboutMessage.length < 14 || infoAboutMessage[14] != "HLR")) {
                temporary = implode(infoAboutMessage, ",");
                infoAboutMessage = temporary.split(",", 9);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.warn("UnsupprotedEncodingException with get status SMS " + e.getMessage());
        }

        return infoAboutMessage;
    }

    public String getBalance() {
        String[] infoAboutMessage = {};

        infoAboutMessage = smsSendCmd("balance", "");

        if (FLAG_DEBUG) {
            if (infoAboutMessage.length == 1)
                LOG.info("The amount im the account: " + infoAboutMessage[0]);
            else
                LOG.info("Error #" + Math.abs(Integer.parseInt(infoAboutMessage[1])));
        }

        return infoAboutMessage.length == 2 ? "" : infoAboutMessage[0];
    }

    private String[] send(String phones, String message, int translit, String time, String id, int format, String sender, String query) {

        String[] formats = {"", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1"};
        String[] infoAboutMessage = {};

        try {
            infoAboutMessage = smsSendCmd("send", "cost=3&phones=" + URLEncoder.encode(phones, CHARSET)
                    + "&mes=" + URLEncoder.encode(message, CHARSET)
                    + "&translit=" + translit + "&id=" + id + (format > 0 ? "&" + formats[format] : "")
                    + (sender == "" ? "" : "&sender=" + URLEncoder.encode(sender, CHARSET))
                    + (time == "" ? "" : "&time=" + URLEncoder.encode(time, CHARSET))
                    + (query == "" ? "" : "&" + query));
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Some problem with sending SMS " + e.getMessage());
        }

        if (FLAG_DEBUG) {
            if (Integer.parseInt(infoAboutMessage[1]) > 0) {
                LOG.info("The message sent. ID: " + infoAboutMessage[0] + ", all SMS: " + infoAboutMessage[1] + ", cost: " + infoAboutMessage[2] + ", balance: " + infoAboutMessage[3]);
            } else {
                LOG.info("Error #" + Math.abs(Integer.parseInt(infoAboutMessage[1])));
                LOG.info(Integer.parseInt(infoAboutMessage[0]) > 0 ? (", ID: " + infoAboutMessage[0]) : "");
            }
        }

        return infoAboutMessage;
    }

    private String[] smsSendCmd(String cmd, String arg) {
        String ret = ",";

        try {
            String url = (FLAG_HTTPS ? "https" : "http") + "://smsc.ru/sys/" + cmd + ".php?login=" + URLEncoder.encode(LOGIN, CHARSET)
                    + "&psw=" + URLEncoder.encode(PASSWORD, CHARSET)
                    + "&fmt=1&charset=" + CHARSET + "&" + arg;

            int i = 0;
            do {
                if (i > 0)
                    Thread.sleep(2000 + 1000 * i);

                if (i == 2)
                    url = url.replace("://smsc.ru/", "://www2.smsc.ru/");

                ret = readUrl(url);
            }
            while (ret == "" && ++i < 4);
        } catch (UnsupportedEncodingException e) {
            LOG.warn("Some problem with encoding SMS " + e.getMessage());
        } catch (InterruptedException e) {
            LOG.warn("Some problem with sending SMS " + e.getMessage());
        }

        return ret.split(",");
    }

    private String readUrl(String url) {

        String line = "";
        String realUrl = url;
        String[] param = {};
        boolean inputStreamPost = (FLAG_POST || url.length() > 2000);

        if (inputStreamPost) {
            param = url.split("\\?", 2);
            realUrl = param[0];
        }

        try {
            URL newUrl = new URL(realUrl);
            InputStream inputStream;

            if (inputStreamPost) {
                URLConnection conn = newUrl.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream(), CHARSET);
                outputStream.write(param[1]);
                outputStream.flush();
                outputStream.close();
                LOG.info("POST sending SMS");
                inputStream = conn.getInputStream();
            } else {
                inputStream = newUrl.openStream();
            }

            InputStreamReader reader = new InputStreamReader(inputStream, CHARSET);

            int character;
            while ((character = reader.read()) != -1) {
                line += (char) character;
            }

            reader.close();
        } catch (MalformedURLException e) {
            LOG.warn("URL is wrong for sending SMS");
        } catch (IOException e) {
            LOG.warn("Some problem with sending SMS " + e.getMessage());
        }

        return line;
    }
}