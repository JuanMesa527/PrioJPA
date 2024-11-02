/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.ProjectType;
import com.example.models.ProjectTypeDTO;
import com.example.models.RoleDTO;
import com.example.models.RolePrio;
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
@Path("/projectTypes")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectTypeService {
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
        Query q = entityManager.createQuery("select u from ProjectType u order by u.id ASC");
        List<ProjectType> projectTypes = q.getResultList();

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(projectTypes).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProjectType(ProjectTypeDTO projectType) {
        JSONObject rta = new JSONObject();

        ProjectType projectTypeTmp = new ProjectType(projectType.getName(), projectType.getDescription());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(projectTypeTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(projectTypeTmp);
            rta.put("projectType_id", projectTypeTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            projectTypeTmp = null;
        } finally {
            entityManager.clear();
            entityManager.close();
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(projectTypeTmp).build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProjectType(@PathParam("id") Long id) {
        try {
            ProjectType projectType = entityManager.find(ProjectType.class, id);
            if (projectType != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(projectType);
                entityManager.getTransaction().commit();
                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Project type deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("Project type not found").build();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error deleting Project type").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRole(@PathParam("id") Long id, ProjectTypeDTO ProjectType) {
        try {
            ProjectType existingProjectType = entityManager.find(ProjectType.class, id);
            if (existingProjectType != null) {

                if (ProjectType.getDescription() != null) {
                    existingProjectType.setDescription(ProjectType.getDescription());
                }
                if (ProjectType.getName() != null) {
                    existingProjectType.setName(ProjectType.getName());
                }
                entityManager.getTransaction().begin();
                entityManager.merge(existingProjectType);
                entityManager.getTransaction().commit();
                entityManager.refresh(existingProjectType);

                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(existingProjectType).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("Project type not found").build();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error updating project Type").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }
}
