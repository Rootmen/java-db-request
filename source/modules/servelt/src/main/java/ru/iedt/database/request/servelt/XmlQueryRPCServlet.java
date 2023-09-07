package ru.iedt.database.request.servelt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.iedt.database.request.database.controller.query.execution.type.ExecutionServlet;
import ru.iedt.database.request.database.executorservice.QueryExecutorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class XmlQueryRPCServlet extends HttpServlet {
    static Logger LOGGER = LoggerFactory.getLogger(XmlQueryRPCServlet.class);
    QueryExecutorService executorService = new QueryExecutorService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.queryRun(new ByteArrayInputStream(request.getParameter("xmlQuery").getBytes()), request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.queryRun(request.getInputStream(), request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
    }

    protected void queryRun(InputStream xmlQuery, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(200);
        try (PrintWriter output = response.getWriter()) {
            String callback = request.getParameter("callback");
            if (callback != null) {
                output.write(callback + "(");
            }
            executorService.startQuery(xmlQuery, output, ExecutionServlet.class, ExecutionServlet.ExecutionServletReturn.class);
            if (callback != null) {
                output.write(")");
            }
        } catch (Exception e) {
            LOGGER.error("Ошибка", e);
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        QueryExecutorService.shutdown();
    }
}
