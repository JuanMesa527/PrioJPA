/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class Vote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne
    private Project project;
    
    @NotNull
    @ManyToOne
    private UserPrio userPrio;
    
    @NotNull
    @ManyToOne
    private VoteDates voteDates;

    public Vote() {

    }

    public Vote(Project project, UserPrio userPrio, VoteDates voteDates) {
        this.project = project;
        this.userPrio = userPrio;
        this.voteDates = voteDates;
    }

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public UserPrio getUserPrio() {
        return userPrio;
    }

    public void setUserPrio(UserPrio userPrio) {
        this.userPrio = userPrio;
    }

    public VoteDates getVoteDates() {
        return voteDates;
    }

    public void setVoteDates(VoteDates voteDates) {
        this.voteDates = voteDates;
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
