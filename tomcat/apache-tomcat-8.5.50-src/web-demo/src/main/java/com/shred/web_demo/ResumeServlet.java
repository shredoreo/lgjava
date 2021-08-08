package com.shred.web_demo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
//http://localhost:8080/web-demo/resume/add
//@WebServlet(name = "resumeServlet", value = "/resume/add")
public class ResumeServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World! /resume/add";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("===========>doGet");
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);

    }

    public void destroy() {
    }
}