package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
@RequestMapping
public class SocialMediaController {

    private final MessageService messageService;
    private final AccountService accountService;
 
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
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
    
    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        Account authenticatedAccount = accountService.authenticate(account.getUsername(), account.getPassword());

        if (authenticatedAccount != null) {
            return ResponseEntity.ok(authenticatedAccount);
        } else {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }


    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createMessage(message);
            return ResponseEntity.ok(createdMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }


    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages); // ✅ Always returns 200 OK
    }


    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
        int rowsDeleted = messageService.deleteMessageById(messageId);
        
        // If rowsDeleted > 0, message was deleted, otherwise it did not exist
        return rowsDeleted > 0 
            ? ResponseEntity.ok(rowsDeleted) 
            : ResponseEntity.ok().build();  // Return 200 OK with empty body if message did not exist
    }


    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageText(@PathVariable Integer messageId, @RequestBody Map<String, String> updateData) {
    String newMessageText = updateData.get("messageText");

    // Validate new message text
    if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() > 255) {
        return ResponseEntity.badRequest().build(); // 400 Bad Request if invalid input
    }

    int rowsUpdated = messageService.updateMessageText(messageId, newMessageText);
    
    return rowsUpdated > 0 
        ? ResponseEntity.ok(rowsUpdated) // 200 OK with 1 if update successful
        : ResponseEntity.badRequest().build(); // 400 Bad Request if message does not exist
    }



    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        return messageService.getMessageById(messageId)
                .map(ResponseEntity::ok)  // Return 200 OK with the message if found
                .orElse(ResponseEntity.ok().build());  // Return 200 OK with an empty body if not found
    }


    @GetMapping("{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);  // Always returns 200 OK with an empty list if no messages
    }

    
   


    
    
        
   

}

