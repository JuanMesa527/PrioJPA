/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
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
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

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
        Query q = entityManager.createQuery("select u from UserPrio u order by u.id ASC");
        List<UserPrio> users = q.getResultList();

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(users).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserDTO user) {
        JSONObject rta = new JSONObject();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        Date birthday = null;
        try {
            birthday = formato.parse(user.getBirthday());
        } catch (ParseException ex) {
            System.out.println("fecha no valida");
        }

        Query q = entityManager.createQuery("SELECT r FROM RolePrio r WHERE r.code = '" + user.getRole() + "'");
        RolePrio role = (RolePrio) q.getSingleResult();

        UserPrio userTmp = new UserPrio(user.getName(), user.getEmail(), user.getCellphone(), user.getAddress(), birthday, user.getPassword(), role);
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(userTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(userTmp);
            rta.put("user_id", userTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            userTmp = null;
        } finally {
            entityManager.clear();
            entityManager.close();
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(userTmp).build();
    }
    
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            UserPrio user = entityManager.find(UserPrio.class, id);
            if (user != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(user);
                entityManager.getTransaction().commit();
                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("User deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("User not found").build();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error deleting user").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, UserDTO user) {
        try {
            UserPrio existingUser = entityManager.find(UserPrio.class, id);
            if (existingUser != null) {
                
                if (user.getName()!=null) {
                    existingUser.setName(user.getName());
                }
                if (user.getEmail()!=null) {
                    existingUser.setEmail(user.getEmail());
                }
                if (user.getCellphone()!=null) {
                    existingUser.setCellphone(user.getCellphone());
                }
                if (user.getAddress()!=null) {
                    existingUser.setAddress(user.getAddress());
                }
                if (user.getBirthday()!=null) {
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    Date birthday = formato.parse(user.getBirthday());
                    existingUser.setBirthday(birthday);
                }
                if (user.getPassword()!=null) {
                    existingUser.setPassword(user.getPassword());
                }
                
                if (user.getRole()!=0) {
                    Query q = entityManager.createQuery("SELECT r FROM RolePrio r WHERE r.code = '" + user.getRole() + "'");
                    RolePrio role = (RolePrio) q.getSingleResult();
                    existingUser.setRole(role);
                }

                entityManager.getTransaction().begin();
                entityManager.merge(existingUser);
                entityManager.getTransaction().commit();
                entityManager.refresh(existingUser);

                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(existingUser).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("User not found").build();
            }
        } catch (ParseException ex) {
            return Response.status(Response.Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity("Invalid date format").build();
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error updating user").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginCompetitor(UserDTO user) {
        Query q = entityManager.createQuery("select u from User u where u.email = " + "'" + user.getEmail() + "'" + " and u.password = " + "'" + user.getPassword() + "'");
        try {
            UserPrio userTmp = (UserPrio) q.getSingleResult();
            if (userTmp != null) {
                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(userTmp).build();
            }
        } catch (javax.persistence.NoResultException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Credenciales inválidas")
                    .build();
        }
        return null;
    }

}
