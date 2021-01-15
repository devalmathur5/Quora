package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(method = RequestMethod.DELETE, value = "/admin/user/{userid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(final String userUuid,
                            @RequestHeader("authorization") final String adminAccessToken) throws AuthorizationFailedException, UserNotFoundException {
        UserEntity userDeleted = adminService.deleteUser(userUuid, adminAccessToken);
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse().id(userDeleted.getUuid()).status("DELETED");

        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }

}
