package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "question", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "getAllQuestions", query = "select q.uuid, q.content from QuestionEntity q"),
                @NamedQuery(name = "getQuestionById", query = "select q from QuestionEntity q where q.uuid = :uuid"),
                @NamedQuery(name = "getQuestionByUserId", query = "select q from QuestionEntity q where q.userId = :userid")
        }
)
public class QuestionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "content")
    @Size(max = 500)
    @NotNull
    private String content;

    @Column(name = "date")
    @NotNull
    private ZonedDateTime date;

    @Column(name = "user_id")
    @NotNull
    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "id")
    private Integer userId;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
