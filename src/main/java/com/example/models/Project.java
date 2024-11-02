/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mauricio
 */
@Entity
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.DATE)
    private Calendar createdAt;

    @NotNull
    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Calendar updatedAt;
    
    @NotNull
    private String name;
    
    @NotNull
    private double scope;
    
    @NotNull
    private String periodTime;
    
    @NotNull
    @ManyToOne
    private ProjectType projectType;
    
    @NotNull
    private String locality;
    
    @NotNull
    private String budget;
    
    @NotNull
    private int benefited;
    
    private String[] associatedAspects;

    public Project() {

    }

    public Project(String name, double scope, String periodTime, ProjectType proyectType, String locality, String budget, int benefited, String[] associatedAspects) {
        this.name = name;
        this.scope = scope;
        this.periodTime = periodTime;
        this.projectType = proyectType;
        this.locality = locality;
        this.budget = budget;
        this.benefited = benefited;
        this.associatedAspects = associatedAspects;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScope() {
        return scope;
    }

    public void setScope(double scope) {
        this.scope = scope;
    }

    public String getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(String periodTime) {
        this.periodTime = periodTime;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public int getBenefited() {
        return benefited;
    }

    public void setBenefited(int benefited) {
        this.benefited = benefited;
    }

    public String[] getAssociatedAspects() {
        return associatedAspects;
    }

    public void setAssociatedAspects(String[] associatedAspects) {
        this.associatedAspects = associatedAspects;
    }

    @PreUpdate
    private void updateTimestamp() {
        this.updatedAt = Calendar.getInstance();
    }

    @PrePersist
    private void creationTimestamp() {
        this.createdAt = this.updatedAt = Calendar.getInstance();
    }

}
