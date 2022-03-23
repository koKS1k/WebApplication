package com.example.WebApplication.listeners;

import services.RESTmanager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;

@WebListener
public class StartAppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        /*
        Принято решение хранить атрибуты "employeeList", с полным списком сотрудников,
        и "departmentsList" с полным списком отделов в ApplicationScope.
        При запуске приложения эти аттрибуты заполняются.
        */


        RESTmanager rest= new RESTmanager();
        event.getServletContext().setAttribute("employeeList", rest.getAllEmployees());
        event.getServletContext().setAttribute("departmentsList", rest.getAllDepartments());


    }
}
