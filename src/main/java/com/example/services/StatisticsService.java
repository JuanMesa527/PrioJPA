/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.RolePrio;
import com.example.models.resultDTO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author User
 */
@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsService {

    @PersistenceContext(unitName = "PrioPU")
    EntityManager entityManager;

    @PostConstruct
    public void init() {
        try {
            entityManager = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/getWinner")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWinner() {
        Query q = entityManager.createQuery("SELECT p.locality, v.project.id, COUNT(v) as totalVotes\n"
                + "FROM Vote v\n"
                + "JOIN v.project p\n"
                + "GROUP BY p.locality, v.project.id\n"
                + "HAVING COUNT(v) = (\n"
                + "SELECT COUNT(v2)\n"
                + "FROM Vote v2\n"
                + "JOIN v2.project p2\n"
                + "WHERE p2.locality = p.locality\n"
                + "GROUP BY p2.id, p2.locality\n"
                + "HAVING COUNT(v2) >= ALL (\n"
                + "SELECT COUNT(v3)\n"
                + "FROM Vote v3\n"
                + "JOIN v3.project p3\n"
                + "WHERE p3.locality = p2.locality\n"
                + "GROUP BY p3.id, p3.locality\n"
                + ")\n"
                + ")");
        List<resultDTO> results = q.getResultList();

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(results).build();
    }

    @GET
    @Path("/getLocalityTotalVotes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocalityTotalVotes() {
        Query q = entityManager.createQuery("SELECT \n"
                + "p.locality, \n"
                + "COUNT(v) as totalVotes\n"
                + "FROM Vote v\n"
                + "JOIN v.project p\n"
                + "GROUP BY p.locality");
        List<resultDTO> results = q.getResultList();

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(results).build();
    }
}
