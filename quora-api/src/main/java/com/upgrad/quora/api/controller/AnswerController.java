package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
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
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(method = RequestMethod.POST, value = "/question/{questionId}/answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest,
                                                       @PathVariable("questionId") final String questionId,
                                                       @RequestHeader("authorization") final String authorization) throws InvalidQuestionException, AuthorizationFailedException {
        QuestionEntity questionEntity = answerService.checkQuestion(questionId);
        UserAuthEntity userAuthEntity = answerService.checkUser(authorization);

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAnswer(answerRequest.getAnswer());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setQuestionId(questionEntity.getId());
        answerEntity.setUserId(userAuthEntity.getUserId());

        answerService.saveAnswer(answerEntity);

        AnswerResponse response = new AnswerResponse();
        response.id(answerEntity.getUuid()).status("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/answer/edit/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswerContent(final AnswerEditRequest answerEditRequest,
                                                                @PathVariable("answerId") final String answerId,
                                                                @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = answerService.checkUser(authorization);
        answerService.ifAnswerExist(answerId);
        AnswerEntity answerEntity = answerService.checkAnswerOwner(answerId, userAuthEntity.getUserId());

        AnswerEntity answerToBeEdited = new AnswerEntity();
        answerToBeEdited.setUuid(answerEntity.getUuid());
        answerToBeEdited.setUserId(answerEntity.getUserId());
        answerToBeEdited.setQuestionId(answerEntity.getQuestionId());
        answerToBeEdited.setAnswer(answerEditRequest.getContent());
        answerToBeEdited.setDate(ZonedDateTime.now());
        answerToBeEdited.setId(answerEntity.getId());

        answerService.editAnswer(answerToBeEdited);

        AnswerEditResponse response = new AnswerEditResponse().id(answerToBeEdited.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(response, HttpStatus.OK);
    }
}
