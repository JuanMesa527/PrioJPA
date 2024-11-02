/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

/**
 *
 * @author User
 */
public class ProjectDTO {

    private String name;

    private double scope;

    private String periodTime;

    private int projectType;

    private String locality;

    private String budget;

    private int benefited;

    private String[] associatedAspects;
    
    ProjectDTO(){
        
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

    public int getProjectType() {
        return projectType;
    }

    public void setProjectType(int projectType) {
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
    
    
}
