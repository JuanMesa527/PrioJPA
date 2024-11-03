/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Project;
import com.example.models.UserPrio;
import com.example.models.Vote;
import com.example.models.VoteDTO;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author User
 */
@Path("/vote")
@Produces(MediaType.APPLICATION_JSON)
public class VoteService {

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
        Query q = entityManager.createQuery("select u from Vote u order by u.id ASC");
        List<Vote> vote = q.getResultList();

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(vote).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createVote(VoteDTO vote) {
        JSONObject rta = new JSONObject();

        Query q = entityManager.createQuery("SELECT r FROM VoteDates r WHERE r.id = '" + vote.getVoteDates() + "'");
        VoteDates voteDates = (VoteDates) q.getSingleResult();
        
        Query qUser = entityManager.createQuery("SELECT r FROM UserPrio r WHERE r.id = '" + vote.getUserPrio() + "'");
        UserPrio userPrio = (UserPrio) qUser.getSingleResult();
        
        Query qComp = entityManager.createQuery("SELECT COUNT(r) FROM Vote r WHERE r.userPrio.id = '" + userPrio.getId() + "' and r.voteDates.id= '" + voteDates.getId() + "'");
        Long voteComp = (Long) qComp.getSingleResult();
        
        if (voteDates.getStatus().equals("Activo") && voteComp == 0) {

            Query qProject = entityManager.createQuery("SELECT r FROM Project r WHERE r.id = '" + vote.getProject() + "'");
            Project project = (Project) qProject.getSingleResult();

            Vote voteTmp = new Vote(project, userPrio, voteDates);
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(voteTmp);
                entityManager.getTransaction().commit();
                entityManager.refresh(voteTmp);
                rta.put("vote_id", voteTmp.getId());
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
        return Response.status(Response.Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity("Not active dates").build();
    }
}
