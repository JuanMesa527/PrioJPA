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
public class ResultFilteredDTO {
    private String range;
    private Long totalVotes;

    public ResultFilteredDTO() {
    }

    public ResultFilteredDTO(String range, Long totalVotes) {
        this.range = range;
        this.totalVotes = totalVotes;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }   
}