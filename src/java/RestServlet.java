/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author AlexanderSteen
 */
@WebServlet(urlPatterns = {"/api/quote/*"})
public class RestServlet extends HttpServlet {

    private Map<Integer, String> quotes = new HashMap() {
        {
            put(1, "Friends are kisses blown to us by angels");
            put(2, "Do not take life too seriously. You will never get out of it alive");
            put(3, "Behind every great man, is a women rolling her eyes");
        }
    };
    private int nextId = 4;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
        response.setContentType("application/json");

        String[] parts = request.getRequestURI().split("/");
        String parameter = null;
        if(parts.length == 4){
            parameter = parts[3];
        }
        int id;
        if(parameter.toLowerCase().equals("random")){
            Random r = new Random();
            List<Integer> ids = new ArrayList();
            for (Map.Entry<Integer, String> entrySet : quotes.entrySet()) {
                ids.add(entrySet.getKey());
            }

            id = ids.get(r.nextInt(ids.size()));
        }else{
            id = Integer.parseInt(parameter);
        }
        JsonObject quote = new JsonObject();
        quote.addProperty("quote", quotes.get(id));
        String jsonResponse = new Gson().toJson(quote);
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(jsonResponse);
        }
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
        response.setContentType("application/json");

        Scanner jsonScanner = new Scanner(request.getInputStream());
        String json = "";
        while(jsonScanner.hasNext()){
            json += jsonScanner.nextLine();
        }
        
        JsonObject newQuote = new JsonParser().parse(json).getAsJsonObject();
        String quote = newQuote.get("quote").getAsString();
        int id = nextId++;
        quotes.put(id, quote);
        
        JsonObject jsonOut = new JsonObject();
        jsonOut.addProperty("id", id);
        jsonOut.addProperty("quote", quotes.get(id));
        String jsonResponse = new Gson().toJson(jsonOut);
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(jsonResponse);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String[] parts = req.getRequestURI().split("/");
        String parameter = null;
        if(parts.length == 4){
            parameter = parts[3];
        }
        
        int id = Integer.parseInt(parameter);
        
        Scanner jsonScanner = new Scanner(req.getInputStream());
        String json = "";
        while(jsonScanner.hasNext()){
            json += jsonScanner.nextLine();
        }
        
        JsonObject newQuote = new JsonParser().parse(json).getAsJsonObject();
        
        quotes.put(id, newQuote.get("quote").getAsString());
        
        JsonObject jsonOut = new JsonObject();
        jsonOut.addProperty("id", id);
        jsonOut.addProperty("quote", quotes.get(id));
        String jsonResponse = new Gson().toJson(jsonOut);
        
        try (PrintWriter out = resp.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(jsonResponse);
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String[] parts = req.getRequestURI().split("/");
        String parameter = null;
        if(parts.length == 4){
            parameter = parts[3];
        }
        
        int id = Integer.parseInt(parameter);
        
        String quote = quotes.get(id);
        quotes.remove(id);
        
        JsonObject jsonOut = new JsonObject();
        jsonOut.addProperty("quote", quote);
        String jsonResponse = new Gson().toJson(jsonOut);
        
        try (PrintWriter out = resp.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(jsonResponse);
        }
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
