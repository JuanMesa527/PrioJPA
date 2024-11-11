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
public class LocalityTotalVotesDTO {
    private String locality;
    private Long totalVotes;
    
    public LocalityTotalVotesDTO() {
    }
    
    public LocalityTotalVotesDTO(String locality, Long totalVotes) {
        this.locality = locality;
        this.totalVotes = totalVotes;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }   
}
