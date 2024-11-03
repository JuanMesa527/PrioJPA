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
public class VoteDTO {

    private Long project;

    private Long userPrio;

    private Long voteDates;

    public VoteDTO() {

    }

    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public Long getUserPrio() {
        return userPrio;
    }

    public void setUserPrio(Long userPrio) {
        this.userPrio = userPrio;
    }

    public Long getVoteDates() {
        return voteDates;
    }

    public void setVoteDates(Long voteDates) {
        this.voteDates = voteDates;
    }

}
