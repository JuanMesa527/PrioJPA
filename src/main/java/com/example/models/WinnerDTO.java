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
public class WinnerDTO {
    private String locality;
    private Long projectId;
    private Long totalVotes;
    
    public WinnerDTO() {
    }
    
    public WinnerDTO(String locality, Long totalVotes) {
        this.locality = locality;
        this.totalVotes = totalVotes;
    }
    
    public WinnerDTO(String locality, Long projectId, Long totalVotes) {
        this.locality = locality;
        this.projectId = projectId;
        this.totalVotes = totalVotes;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }   
}
