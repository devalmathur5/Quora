package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.POST, value = "/question/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(final QuestionRequest questionRequest,
                                                           @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        QuestionEntity question = new QuestionEntity();

        UserAuthEntity userAuthEntity = questionService.userAuthCheck(authorization);

        question.setUuid(UUID.randomUUID().toString());
        question.setContent(questionRequest.getContent());
        question.setDate(ZonedDateTime.now());
        question.setUserId(userAuthEntity.getUserId());

        questionService.postQuestion(question);

        QuestionResponse response = new QuestionResponse().id(question.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(response, HttpStatus.OK);
    }

    // Not Working
    /*
    @RequestMapping(method = RequestMethod.GET, value = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = questionService.userAuthCheck(authorization);
        List<QuestionEntity> allQuestions = questionService.getAllQuestions();

        List<QuestionDetailsResponse> questionResponse = new ArrayList<>(allQuestions.size());

        for(int i=0; i<allQuestions.size(); i++){
            questionResponse.add(i, new QuestionDetailsResponse().id(allQuestions.get(i).getUuid())
                                                .content(allQuestions.get(i).getContent()));
        }

        return new ResponseEntity<QuestionDetailsResponse>(questionResponse, HttpStatus.OK);
    }*/

    @RequestMapping(method = RequestMethod.PUT, value = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestionContent(final QuestionEditRequest questionRequest,
                                                             @PathVariable("questionId") final String questionUuid,
                                                             @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        // check whether the user has access to edit the question.
        // Only the question owner can change the question content.
        // The auth token passed in the request header is compared with the signed in user.
        // If the auth token matches, user can change content of the question.
        UserAuthEntity userAuthEntity = questionService.userAuthCheck(authorization);
        // 1. check whether question uuid passed in the request as pathVariable exist in the Question Entity
        // 2. If the userId in UserAuth entity for corresponding UserAuthToken passed matches with the user_id
        // field in Question table.
        // 3. Fetch the question which is to be edited.
        QuestionEntity questionToBeEdited = questionService.questionCheck(userAuthEntity.getUserId(), questionUuid);

        // set the question content.
        // along with other values.
        /** Why i have to set other values when updating only one field? All the other fields should be intact.
         Is there something to do with entityManager.merge() that i gotta provide all the fields? */
        QuestionEntity q = new QuestionEntity();
        q.setContent(questionRequest.getContent());
        q.setUuid(questionToBeEdited.getUuid());
        q.setUserId(questionToBeEdited.getUserId());
        q.setDate(ZonedDateTime.now());
        q.setId(questionToBeEdited.getId());
        QuestionEditResponse question = new QuestionEditResponse().id(q.getUuid()).status("QUESTION EDITED");

        questionService.editQuestion(q);
        return new ResponseEntity<QuestionEditResponse>(question, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionId,
                                                                 @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = questionService.userAuthCheck(authorization);
        QuestionEntity questionToBeDeleted = questionService.deleteQuestion(userAuthEntity.getUserId().toString(), questionId);

        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(questionToBeDeleted.getUuid()).status("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse> (questionDeleteResponse, HttpStatus.OK);
    }

    /*
    @RequestMapping(method = RequestMethod.GET, value = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsnsByUser(@PathVariable("userId") final String userUuid,
                                                                    @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = questionService.userAuthCheck(authorization);
        UserEntity userEntity = null;
        try {
            userEntity = userDao.getUserByUuid(userUuid);
        }catch (NoResultException nre){
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }

        List<QuestionEntity> questions = questionService.getAllQuestionsById(userEntity.getId());

        List<QuestionDetailsResponse> questionDetails = new ArrayList<>(questions.size());

        for(int i=0; i<questions.size(); i++){

       }

        ResponseEntity<List<QuestionDetailsResponse>> response = new ResponseEntity<>(questionDetails, HttpStatus.OK);
        return response;
    }*/
}
