/*
 * Copyright (c) 2014. Tony Dw. Xia All rights reserved.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package debra.Web;

import debra.Framework.DBUtil.DBUtil;
import debra.Framework.Interface.DebraAction;
import debra.Framework.Login.LoginAction;
import debra.Framework.Transfer.DebraProtocolEnum;
import debra.Framework.Transfer.DebraTransfer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author user
 */
public class DebraMainServlet extends HttpServlet {
    private String DBDriver;
    private String url;
    private String dbUserName;
    private String dbPassword;
    private Connection conn;
    private String loginID;
    private String actionURL;
    private String responseCLS = "";
    private LoginAction loginWidgetAction;
    private debra.Framework.CheckPurview.CheckPurviewAction CheckPurviewAction;
    private DBUtil XMLHelper, JSONHelper;
    private DBUtil dbUtil;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DebraTransfer debraTransfer = null;
        DebraAction debraAction = null;
        DebraProtocolEnum protocal = DebraProtocolEnum.valueOf(req.getHeader("PROTOCAL"));
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "PROTOCAL = " + protocal);
        if (protocal.equals(DebraProtocolEnum.XML)) {
            debraTransfer = new debra.Framework.Transfer.XML.DebraServerTransfer(req.getInputStream(), resp.getOutputStream());
            dbUtil = XMLHelper.clone();
        } else {
            debraTransfer = new debra.Framework.Transfer.JSON.DebraServerTransfer(req.getInputStream(), resp.getOutputStream());
            dbUtil = JSONHelper.clone();
        }
        dbUtil.setDebraTransfer(debraTransfer);
        debraTransfer.receive();
        loginID = (String) debraTransfer.getReceiveAttribute("LoginID");
        actionURL = (String) debraTransfer.getReceiveAttribute("ActionURL");
        responseCLS = (String) debraTransfer.getReceiveAttribute("ResponseCLS");
        try {
            if (responseCLS.equals(LoginAction.class.getName())) {
                debraAction = loginWidgetAction.clone();
            } else if (responseCLS.equals(debra.Framework.CheckPurview.CheckPurviewAction.class.getName())) {
                debraAction = CheckPurviewAction.clone();
            } else {
                URL cmdURL = new URL(actionURL);
                URLClassLoader debraCL = URLClassLoader.newInstance(new URL[]{cmdURL}, Thread.currentThread().getContextClassLoader());
                Class debraActionClass = debraCL.loadClass(responseCLS);
                debraAction = (DebraAction) (debraActionClass.newInstance());
            }
            debraAction.setDebraTransfer(debraTransfer);
            debraAction.setUserID(loginID);
            debraAction.setDBUtil(dbUtil);
            debraAction.analyseParameter();
            debraAction.doAction(conn);
            debraTransfer.send();
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "sql failed rollback", ex);
            debraTransfer.setThrow();
            debraTransfer.send();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void init(ServletConfig config) throws ServletException {
        try {
            DBDriver = config.getInitParameter("DBDriver");
            url = config.getInitParameter("URL");
            System.out.println(url);
            dbUserName = config.getInitParameter("UserName");
            dbPassword = config.getInitParameter("Password");
            Class.forName(DBDriver);
            conn = DriverManager.getConnection(url, dbUserName, dbPassword);
            if (!conn.isClosed()) {
                conn.setAutoCommit(false);
                System.out.println("Succeeded connecting to the Database!");
            }
            CheckPurviewAction = new debra.Framework.CheckPurview.CheckPurviewAction();
            loginWidgetAction = new LoginAction();
            XMLHelper = new debra.Framework.DBUtil.XML.DBHelper();
            XMLHelper.setConnection(conn);
            JSONHelper = new debra.Framework.DBUtil.JSON.DBHelper();
            JSONHelper.setConnection(conn);
        } catch (SQLException ex) {
            Logger.getLogger(DebraMainServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DebraMainServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
