package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity postAQuestion(QuestionEntity question){
        entityManager.persist(question);
        return question;
    }

    public List<QuestionEntity> getAllQuestions(){
        return entityManager.createQuery("select q from QuestionEntity q", QuestionEntity.class).getResultList();
    }

    public QuestionEntity getQuestionById(final String uuid){
        try{
            return entityManager.createNamedQuery("getQuestionById", QuestionEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<QuestionEntity> getQuestionByUserId(final Integer userId){
        try{
            return entityManager.createNamedQuery("getQuestionByUserId", QuestionEntity.class)
                    .setParameter("userid", userId).getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public QuestionEntity editQuestion(QuestionEntity question){
        return entityManager.merge(question);
    }

    public QuestionEntity deleteQuestion(QuestionEntity question){
        entityManager.remove(question);
        return question;
    }
}
