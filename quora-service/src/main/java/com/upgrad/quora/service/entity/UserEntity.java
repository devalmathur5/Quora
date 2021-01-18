package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USERS",schema = "public")
@NamedQueries({
        @NamedQuery(name = "userByUsername", query = "select u from UserEntity u where u.userName = :username"),
        @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email = :email"),
        @NamedQuery(name = "passwordByUsername", query = "select u.password from UserEntity u where u.userName = :username"),
        @NamedQuery(name = "userById", query = "select u from UserEntity u where u.id = :id"),
        @NamedQuery(name = "userByUuid", query = "select u from UserEntity u where u.uuid = :uuid")
})
public class UserEntity {

    //nbNB+=
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "FIRSTNAME")
    @Size(max = 30)
    @NotNull
    private String firstName;

    @Column(name = "LASTNAME")
    @Size(max = 30)
    @NotNull
    private String lastName;

    @Column(name = "USERNAME")
    @Size(max = 30)
    @NotNull
    private String userName;

    @Column(name = "EMAIL")
    @Size(max = 50)
    @NotNull
    private String email;

    @Column(name = "PASSWORD")
    @Size(max = 255)
    @NotNull
    private String password;

    @Column(name = "SALT")
    @Size(max = 200)
    @NotNull
    private String salt;

    @Column(name = "COUNTRY")
    @Size(max = 30)
    private String country;

    @Column(name = "ABOUTME")
    @Size(max = 50)
    private String aboutMe;

    @Column(name = "DOB")
    @Size(max = 30)
    private String dob;

    @Column(name = "ROLE")
    @Size(max = 30)
    private String role;

    @Column(name = "CONTACTNUMBER")
    @Size(max = 30)
    private String contactNumber;

    //@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    //private List<QuestionEntity> questions = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /*
    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
    }*/

    //Why are below these here?
    // equals() hashcode() & toString()???

}
