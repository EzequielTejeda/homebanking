package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.configurations.WebAuthentication;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;

    @GetMapping("/api/clients")
    public List<ClientDTO> getClients(){
        return clientService.getAllClients().stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("/api/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
       Client client = clientService.getByEmail(authentication.getName());
       ClientDTO clientDTO = new ClientDTO(client);
       return clientDTO;
    }
    @PostMapping(path = "/api/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName,
                                           @RequestParam String email, @RequestParam String password){
        if(firstName.isEmpty()||lastName.isEmpty()||email.isEmpty()||password.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(clientService.getByEmail(email) != null){
            return new ResponseEntity<>("Mail already in use", HttpStatus.FORBIDDEN);
        }
        clientService.saveClient(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
