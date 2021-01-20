package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerByUuidUserId(final String uuid, Integer userId){
        try{
            return entityManager.createNamedQuery("answerByUuidAndUserId", AnswerEntity.class).
                    setParameter("uuid", uuid).setParameter("userId", userId).
                    getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public AnswerEntity getAnswerByUuid(final String uuid){
        try{
            return entityManager.createNamedQuery("answerByAnsUuid", AnswerEntity.class).
                    setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public AnswerEntity editAnswer(final AnswerEntity answer){
        entityManager.merge(answer);
        return answer;
    }

    public AnswerEntity answerByUserId(final String uuid){
        try{
            return entityManager.createNamedQuery("answerByUserId", AnswerEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public AnswerEntity deleteAnswer(AnswerEntity answer){
        entityManager.remove(answer);
        return answer;
    }

    public List<AnswerEntity> getAllAnsForAQuestion(final Integer questionId){
        try{
            return entityManager.createNamedQuery("answersForAQuestion", AnswerEntity.class)
                    .setParameter("questionId", questionId).getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }
}
