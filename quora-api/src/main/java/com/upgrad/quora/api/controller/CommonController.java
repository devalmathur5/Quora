package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @RequestMapping(method = RequestMethod.GET, value = "/userprofile/{userid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("userid") final String uuid,
                                                       @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, UserNotFoundException {

        //String[] bearerToken = authorization.split("Bearer ");
        final UserEntity userEntity = commonService.getUser(uuid, authorization);

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().
                firstName(userEntity.getFirstName()).
                lastName(userEntity.getLastName()).
                userName(userEntity.getUserName()).
                emailAddress(userEntity.getEmail()).
                contactNumber(userEntity.getContactNumber()).
                aboutMe(userEntity.getAboutMe()).
                dob(userEntity.getDob()).
                country(userEntity.getCountry());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
