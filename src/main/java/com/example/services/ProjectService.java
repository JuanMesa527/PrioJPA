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
import com.example.models.RolePrio;
import com.example.models.UserDTO;
import com.example.models.UserPrio;
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
@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectService {

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
        Query q = entityManager.createQuery("select u from Project u order by u.id ASC");
        List<Project> projects = q.getResultList();

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(projects).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProject(ProjectDTO project) {
        JSONObject rta = new JSONObject();

        Query q = entityManager.createQuery("SELECT r FROM ProjectType r WHERE r.id = '" + project.getProjectType() + "'");
        ProjectType projectType = (ProjectType) q.getSingleResult();

        Project projectTmp = new Project(project.getName(), project.getScope(), project.getPeriodTime(), projectType, project.getLocality(), project.getBudget(), project.getBenefited(), project.getAssociatedAspects());
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(projectTmp);
            entityManager.getTransaction().commit();
            entityManager.refresh(projectTmp);
            rta.put("project_id", projectTmp.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            projectTmp = null;
        } finally {
            entityManager.clear();
            entityManager.close();
        }

        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(projectTmp).build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProject(@PathParam("id") Long id) {
        try {
            Project project = entityManager.find(Project.class, id);
            if (project != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(project);
                entityManager.getTransaction().commit();
                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Project deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("Project not found").build();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error deleting project").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProject(@PathParam("id") Long id, ProjectDTO project) {
        try {
            Project existingProject = entityManager.find(Project.class, id);
            if (existingProject != null) {

                if (project.getName() != null) {
                    existingProject.setName(project.getName());
                }
                if (project.getScope() != 0.0) {
                    existingProject.setScope(project.getScope());
                }
                if (project.getPeriodTime() != null) {
                    existingProject.setPeriodTime(project.getPeriodTime());
                }
                if (project.getLocality() != null) {
                    existingProject.setLocality(project.getLocality());
                }
                if (project.getBudget() != null) {
                    existingProject.setBudget(project.getBudget());
                }
                if (project.getBenefited() != 0) {
                    existingProject.setBenefited(project.getBenefited());
                }
                if (project.getAssociatedAspects() != null) {
                    existingProject.setAssociatedAspects(project.getAssociatedAspects());
                }

                if (project.getProjectType() != 0) {
                    Query q = entityManager.createQuery("SELECT r FROM ProjectType r WHERE r.id = '" + project.getProjectType() + "'");
                    ProjectType projectType = (ProjectType) q.getSingleResult();
                    existingProject.setProjectType(projectType);
                }

                entityManager.getTransaction().begin();
                entityManager.merge(existingProject);
                entityManager.getTransaction().commit();
                entityManager.refresh(existingProject);

                return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(existingProject).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*").entity("Project not found").build();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Access-Control-Allow-Origin", "*").entity("Error updating project").build();
        } finally {
            entityManager.clear();
            entityManager.close();
        }
    }
}
