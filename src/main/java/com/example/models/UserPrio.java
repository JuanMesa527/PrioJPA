/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.text.ParseException;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Mauricio
 */
@Entity
public class UserPrio implements Serializable {

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
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String cellphone;
    
    @NotNull
    private String email;
    
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @NotNull
    private String address;

    @NotNull
    @ManyToOne
    private RolePrio rolePrio;

    public UserPrio() {

    }

    public UserPrio(String nameN, String emailN, String cellphoneN, String addressN, Date birthdayN, String passwordN, RolePrio roleN) {
        name = nameN;
        email = emailN;
        cellphone = cellphoneN;
        address = addressN;
        birthday = birthdayN;
        password = passwordN;
        rolePrio=roleN;
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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PreUpdate
    private void updateTimestamp() {
        this.updatedAt = Calendar.getInstance();
    }

    @PrePersist
    private void creationTimestamp() {
        this.createdAt = this.updatedAt = Calendar.getInstance();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String x = formato.format(birthday);
        return x;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public RolePrio getRole() {
        return rolePrio;
    }

    public void setRole(RolePrio role) {
        this.rolePrio = role;
    }

    

}
