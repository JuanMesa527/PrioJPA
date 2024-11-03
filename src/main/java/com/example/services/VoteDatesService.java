/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Project;
import com.example.models.ProjectDTO;
import com.example.models.ProjectType;
import com.example.models.VoteDates;
import com.example.models.VoteDatesDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author User
 */
@Path("/voteDates")
@Produces(MediaType.APPLICATION_JSON)
public class VoteDatesService {

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
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        Query q = entityManager.createQuery("select u from VoteDates u order by u.id ASC");
        List<VoteDates> vote = q.getResultList();

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(vote).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVote(VoteDatesDTO vote) {
        JSONObject rta = new JSONObject();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        Date start = null;
        Date closing = null;
        try {
            start = formato.parse(vote.getStartDate());
            closing = formato.parse(vote.getClosingDate());
        } catch (ParseException ex) {
            System.out.println("fecha no valida");
        }

        Date hoy = new Date();
        String estado;
        if (hoy.before(start)) {
            estado = "Definido";
        } else if (hoy.after(closing)) {
            estado = "Cerrado";
        } else {
            estado = "Activo";
        }

        VoteDates voteTmp = new VoteDates(start, closing, estado);
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(voteTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(voteTmp);
            rta.put("voteDates_id", voteTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            voteTmp = null;
        } finally {
            entityManager.clear();
            entityManager.close();
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(voteTmp).build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteVote(@PathParam("id") Long id) {
        try {
            VoteDates vote = entityManager.find(VoteDates.class, id);
            if (vote != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(vote);
                entityManager.getTransaction().commit();
                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Vote deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("Vote not found").build();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error deleting vote").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateVote(@PathParam("id") Long id, VoteDatesDTO vote) {
        try {
            VoteDates existingVote = entityManager.find(VoteDates.class, id);
            if (existingVote != null) {
                
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                Date start = formato.parse(existingVote.getStartDate());
                Date closing= formato.parse(existingVote.getClosingDate());
                if (vote.getStartDate() != null) {
                    start = formato.parse(vote.getStartDate());
                    existingVote.setStartDate(start);
                }

                if (vote.getClosingDate() != null) {
                    closing = formato.parse(vote.getClosingDate());
                    existingVote.setClosingDate(closing);
                }

                Date hoy = new Date();
                String estado;
                if (hoy.before(start)) {
                    estado = "Definido";
                } else if (hoy.after(closing)) {
                    estado = "Cerrado";
                } else {
                    estado = "Activo";
                }
                existingVote.setStatus(estado);

                entityManager.getTransaction().begin();
                entityManager.merge(existingVote);
                entityManager.getTransaction().commit();
                entityManager.refresh(existingVote);

                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(existingVote).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("Vote not found").build();
            }
        } catch (ParseException ex) {
            return Response.status(Response.Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity("Invalid date format").build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error updating Vote").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

}
