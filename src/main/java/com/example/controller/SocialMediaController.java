package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
@RequestMapping
public class SocialMediaController {

   
    private final AccountService accountService;
 
    @Autowired
    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
    try {
        // Attempt to register the user
        Account createdAccount = accountService.registerAccount(account);
        return ResponseEntity.ok(createdAccount);
    } catch (IllegalStateException e) {
        // If username already exists, return 409 Conflict
        return ResponseEntity.status(409).body("Username already exists.");
    } catch (IllegalArgumentException e) {
        // If validation fails, return 400 Bad Request
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        // Handle unexpected errors
        return ResponseEntity.status(500).body("An unexpected error occurred.");
    }
}
   

}
