package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity checkQuestion(final String questionUuid) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionById(questionUuid);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }
        return questionEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity checkUser(final String userAuthToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(userAuthToken);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an Answer");
        }
        return userAuthEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity saveAnswer(final AnswerEntity answer){
        return answerDao.createAnswer(answer);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(final AnswerEntity answer){
        return answerDao.editAnswer(answer);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity checkAnswerOwner(final String answerUuid, final Integer userId) throws AuthorizationFailedException {
        AnswerEntity answerEntity = answerDao.getAnswerByUuidUserId(answerUuid, userId);
        if(answerEntity == null){
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        return answerEntity;
    }

    public AnswerEntity ifAnswerExist(final String uuid) throws AnswerNotFoundException {
        AnswerEntity answerEntity = answerDao.getAnswerByUuid(uuid);
        if(answerEntity == null){
            throw new AnswerNotFoundException("ASR-001", "Entered answer uuid does not exist");
        }
        return answerEntity;
    }
}
