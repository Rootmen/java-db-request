package ru.iedt.database.request.demo;


import ru.iedt.database.request.servelt.XmlQueryRPCServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(value = "/api/v1/jax-rpc", loadOnStartup = 1)
public class Servlet extends XmlQueryRPCServlet {
}
