package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private SignupBusinessService signupBusinessService;

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest userRequest) throws SignUpRestrictedException {
        UserEntity userEntity = new UserEntity();

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());
        userEntity.setDob(userRequest.getDob());
        userEntity.setAboutMe(userRequest.getAboutMe());
        userEntity.setCountry(userRequest.getCountry());
        userEntity.setUserName(userRequest.getUserName());
        userEntity.setEmail(userRequest.getEmailAddress());
        userEntity.setContactNumber(userRequest.getContactNumber());
        userEntity.setPassword(userRequest.getPassword());
        userEntity.setRole("nonadmin");

        UserEntity createdUser = signupBusinessService.signup(userEntity);
        SignupUserResponse response = new SignupUserResponse();
        response.id(createdUser.getUuid()).status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupUserResponse>(response, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/user/signin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        //1. decode the authorization which is in the form of "Basic username:password"
        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        //2. extract the username & password from the decode which is in the form of "username:password"
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");
        //3. authorize the user: check if the username & password is a correct combination
        UserAuthEntity userAuthEntity = authenticationService.authenticateUser(decodedArray[0], decodedArray[1]);

        //4. create a user response
        SigninResponse signinResponse = new SigninResponse()
                .id(userAuthEntity.getUuid())
                .message("SIGNED IN SUCCESSFULLY");

        HttpHeaders header = new HttpHeaders();
        header.add("access-token", userAuthEntity.getAccessToken());

        ResponseEntity<SigninResponse> response = new ResponseEntity<>(signinResponse, header, HttpStatus.OK);
        return response;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("accesstoken") final String accesstoken) throws SignOutRestrictedException{
        //1. fetching the userAuthEntity object from the access token
        UserEntity userEntity = authenticationService.signoutService(accesstoken);
        //2. creating the signout response.
        SignoutResponse signoutResponse = new SignoutResponse().id(userEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        
        ResponseEntity<SignoutResponse> response = new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);
        return response;
    }
}
