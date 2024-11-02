/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.RoleDTO;
import com.example.models.RolePrio;
import com.example.models.UserPrio;
import com.example.models.UserDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Mauricio
 */
@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
public class RoleService {

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
        Query q = entityManager.createQuery("select u from RolePrio u order by u.code ASC");
        List<RolePrio> roles = q.getResultList();

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(roles).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRole(RoleDTO role) {
        JSONObject rta = new JSONObject();

        RolePrio roleTmp = new RolePrio(role.getDescription(), role.getResponsabilities());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(roleTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(roleTmp);
            rta.put("role_code", roleTmp.getCode());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            roleTmp = null;
        } finally {
            entityManager.clear();
            entityManager.close();
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(roleTmp).build();
    }

    @DELETE
    @Path("/delete/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRole(@PathParam("code") Long code) {
        try {
            RolePrio role = entityManager.find(RolePrio.class, code);
            if (role != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(role);
                entityManager.getTransaction().commit();
                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Role deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("Role not found").build();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error deleting Role").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @PUT
    @Path("/update/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRole(@PathParam("code") Long code, RoleDTO role) {
        try {
            RolePrio existingRole = entityManager.find(RolePrio.class, code);
            if (existingRole != null) {

                if (role.getDescription() != null) {
                    existingRole.setDescription(role.getDescription());
                }
                if (role.getResponsabilities() != null) {
                    existingRole.setResponsabilities(role.getResponsabilities());
                }
                entityManager.getTransaction().begin();
                entityManager.merge(existingRole);
                entityManager.getTransaction().commit();
                entityManager.refresh(existingRole);

                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(existingRole).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("Role not found").build();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error updating Role").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }
}
