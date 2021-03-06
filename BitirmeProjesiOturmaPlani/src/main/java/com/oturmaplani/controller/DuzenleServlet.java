package com.oturmaplani.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import com.oturmaplani.dao.RehberDao;
@WebServlet("/duzenle")
public class DuzenleServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;
 
protected void doGet(HttpServletRequest request,
 HttpServletResponse response) throws ServletException, IOException {
 doPost(request, response);
 }
 
protected void doPost(HttpServletRequest request,
 HttpServletResponse response) throws ServletException, IOException {
 String id = request.getParameter("id");
 String adi = request.getParameter("adi");
 String soyadi = request.getParameter("soyadi");
 String mail = request.getParameter("mail");
 String tel = request.getParameter("tel");
 
 int durum = new RehberDao().kisiDuzenle(id, adi, soyadi, mail, tel);
 
 response.sendRedirect("duzenlesil.jsp");
 }
}