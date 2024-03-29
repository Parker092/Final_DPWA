/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelos.cls_mascota;
import modelos.cls_registro;
import modelos.cls_usuario;
import modelosDAO.MascotaDAO;
import modelosDAO.RegistrosDAO;
import modelosDAO.usuarioDAO;
import utils.encript;

@WebServlet(name = "citas", urlPatterns = {"/citas", "/citas/edit", "/citas/delete", "/citas/update"})
public class citas extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet citas</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet citas at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String vista = "";
          String accion = request.getServletPath();

         if (accion.equals("/citas/edit")) {
                System.out.println(accion);
                String vista2 = "../vistas/citas/edit.jsp";
                int id = Integer.parseInt(request.getParameter("id"));
                RegistrosDAO mascotaDao = new RegistrosDAO();
                cls_registro mascota = mascotaDao.findRegistros(id);
                request.setAttribute("registro", mascota);
                request.getRequestDispatcher(vista2).forward(request, response);
            }else if(accion.equals("/citas/delete")){
                    System.out.println(accion);
                      String vista2 = "../vistas/citas/index.jsp";
                      int id = Integer.parseInt(request.getParameter("id"));
                      RegistrosDAO mascotaDao = new RegistrosDAO();
                  try {
                      boolean a = mascotaDao.delete(id);
                  } catch (ClassNotFoundException ex) {
                      Logger.getLogger(mascotas.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (SQLException ex) {
                      Logger.getLogger(mascotas.class.getName()).log(Level.SEVERE, null, ex);
                  }
                      List<cls_mascota> mascotas = mascotaDao.getRegistros(Integer.parseInt(session.getAttribute("id").toString()));
                      session.setAttribute("registros", mascotas);
                      request.getRequestDispatcher(vista2).forward(request, response);  
            }
            else if(session.getAttribute("idpermiso") == null){
            vista = "/";
        } else if ((int)session.getAttribute("idpermiso") == 1) {
            //redirect
            vista = "/users";
        } else if ((int)session.getAttribute("idpermiso") == 2 || (int)session.getAttribute("idpermiso") == 3) {
            vista= "vistas/citas/index.jsp";
             RegistrosDAO registrosdao = new RegistrosDAO();
                List<cls_registro> mascotas = registrosdao.getRegistros(Integer.parseInt(session.getAttribute("id").toString()));
                session.setAttribute("registros", mascotas);
            request.getRequestDispatcher(vista).forward(request, response);
        }

        response.sendRedirect(request.getContextPath() + vista);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getServletPath();
        if (accion.equals("/citas/update")) {
            updateRegistro(request, response);
        }else if(accion.equals("/mascotas/delete")){
            try {
                delete(request,response);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(mascotas.class.getName()).log(Level.SEVERE, null, ex);
            }}
        else {
            createCita(request, response);
        }
    }
    
     public void createCita(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String vista = "";
        
        String id = request.getParameter("idmascota");
        String fecha = request.getParameter("date");
        String sintoma = request.getParameter("sintomas");
        System.out.println(fecha);
        
             vista = "vistas/citas/index.jsp";
            

            cls_registro registro = new cls_registro();
            registro.setIdmascota(Integer.parseInt(id));
            registro.setFecha(Date.valueOf(fecha));
            registro.setSintomas(sintoma);
         

            RegistrosDAO registrosDAO2 = new RegistrosDAO();
            Boolean success = registrosDAO2.agregar(registro);
            if (success) {
                vista= "vistas/citas/index.jsp";
             RegistrosDAO registrosdao = new RegistrosDAO();
                List<cls_registro> mascotas = registrosdao.getRegistros(Integer.parseInt(session.getAttribute("id").toString()));
                session.setAttribute("registros", mascotas);
            request.getRequestDispatcher(vista).forward(request, response);
            } else {
                request.setAttribute("error", "No se pudo agregar la cita");
            }

            request.getRequestDispatcher(vista).forward(request, response);

        
    }
     
     public void updateRegistro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String vista = "";
        String id = request.getParameter("id");
        String idmascota = request.getParameter("idmascota");
        String fecha = request.getParameter("date");
        
        String sintomas = request.getParameter("sintomas");
        System.out.println(fecha);

        cls_registro usuario = new cls_registro();
        usuario.setId(Integer.parseInt(id));
        usuario.setIdmascota(Integer.parseInt(idmascota));
        usuario.setFecha(Date.valueOf(fecha));
        usuario.setSintomas(sintomas);
        RegistrosDAO registrosDAO = new RegistrosDAO();

        
                
                    Boolean success = registrosDAO.actualizar(usuario);
                    if (success) {

                        vista = "/citas/edit?id=" + id;
                        //success
                        response.sendRedirect(vista + "&success=Usuario actualizado correctamente");
                    } else {
                        vista = "/citas/edit?id=" + id;
                        //error
                        response.sendRedirect(vista + "&error=No se pudo actualizar el usuario");
                    }
                
            
       
        
    }
     
      public void delete(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException {
       HttpSession session = request.getSession();
        String vista = "/vistas/citas/index.jsp";
        String id = request.getParameter("id");
                RegistrosDAO mascotaDao = new RegistrosDAO();
              
        try {
            mascotaDao.delete(Integer.parseInt(id));
        } catch (SQLException ex) {
            Logger.getLogger(mascotas.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<cls_registro> usuarios = mascotaDao.getRegistros(Integer.parseInt(session.getAttribute("id").toString()));
        session.setAttribute("registros", usuarios);
        request.getRequestDispatcher(vista).forward(request, response);
     }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
