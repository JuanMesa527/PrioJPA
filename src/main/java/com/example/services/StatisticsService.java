/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.LocalityTotalVotesDTO;
import com.example.models.RolePrio;
import com.example.models.UserPrio;
import com.example.models.WinnerDTO;
import com.example.models.ResultFilteredDTO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    @Path("/getWinner/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWinner(@PathParam("id") Long id) {
        UserPrio user = entityManager.find(UserPrio.class, id);
        RolePrio role = (RolePrio) user.getRole();
        if (user != null) {
            if (role.getCode() == 3) {
                Query q = entityManager.createQuery("SELECT NEW com.example.models.WinnerDTO(p.locality, v.project.id, COUNT(v))\n"
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
                List<WinnerDTO> results = q.getResultList();
                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(results).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").entity("Protected content").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("User not found").build();
        }
    }

    @GET
    @Path("/getLocalityTotalVotes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocalityTotalVotes(@PathParam("id") Long id) {
        UserPrio user = entityManager.find(UserPrio.class, id);
        RolePrio role = (RolePrio) user.getRole();
        if (user != null) {
            if (role.getCode() == 3) {
                Query q = entityManager.createQuery("SELECT NEW com.example.models.LocalityTotalVotesDTO(p.locality, COUNT(v)) \n" 
                        + "FROM Vote v\n"
                        + "JOIN v.project p\n"
                        + "GROUP BY p.locality");
                List<LocalityTotalVotesDTO> results = q.getResultList();
                
                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(results).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").entity("Protected content").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("User not found").build();
        }
    }

    @GET
    @Path("/getVotesByAge/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVotesByAge(@PathParam("id") Long id) {
        UserPrio user = entityManager.find(UserPrio.class, id);
        RolePrio role = (RolePrio) user.getRole();
        if (user != null) {
            if (role.getCode() == 3) {
                Query q = entityManager.createQuery("SELECT NEW com.example.models.ResultFilteredDTO( \n"
                        + "CASE\n"
                        + "WHEN u.birthday >= '2006-01-01' THEN 'Menor de 18'\n"
                        + "WHEN u.birthday >= '1999-01-01' AND u.birthday < '2006-01-01' THEN '18-25'\n"
                        + "WHEN u.birthday >= '1989-01-01' AND u.birthday < '1999-01-01' THEN '26-35'\n"
                        + "WHEN u.birthday >= '1974-01-01' AND u.birthday < '1989-01-01' THEN '36-50'\n"
                        + "WHEN u.birthday >= '1959-01-01' AND u.birthday < '1974-01-01' THEN '51-65'\n"
                        + "ELSE 'Mayor de 65'\n"
                        + "END,\n"
                        + "COUNT(v.id))\n"
                        + "FROM Vote v\n"
                        + "JOIN v.userPrio u\n"
                        + "GROUP BY \n"
                        + "CASE\n"
                        + "WHEN u.birthday >= '2006-01-01' THEN 'Menor de 18'\n"
                        + "WHEN u.birthday >= '1999-01-01' AND u.birthday < '2006-01-01' THEN '18-25'\n"
                        + "WHEN u.birthday >= '1989-01-01' AND u.birthday < '1999-01-01' THEN '26-35'\n"
                        + "WHEN u.birthday >= '1974-01-01' AND u.birthday < '1989-01-01' THEN '36-50'\n"
                        + "WHEN u.birthday >= '1959-01-01' AND u.birthday < '1974-01-01' THEN '51-65'\n"
                        + "ELSE 'Mayor de 65'\n"
                        + "END\n");
                List<ResultFilteredDTO> results = q.getResultList();
                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(results).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).header("Access-Control-Allow-Origin", "*").entity("Protected content").build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("User not found").build();
        }
    }

}
