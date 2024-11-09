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
public class resultDTO {
    private String locality;
    private Long projectId;
    private Long totalVotes;
    
    public resultDTO() {
    }

    public String getLocality() {
        return "Locality: " + locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getProjectId() {
        return "Project:" + projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getTotalVotes() {
        return "Total votes:" + totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }   
}
