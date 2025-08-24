package supportly.supportlybackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supportly.supportlybackend.Model.Client;
import supportly.supportlybackend.Service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/client")
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clientList = clientService.findAllClients();
        return new ResponseEntity<>(clientList, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Client>> findClientsByCompanyName(@RequestParam String companyName) {
        List<Client> clientList = clientService.findClientByCompanyNameContaining(companyName);
        return new ResponseEntity<>(clientList, HttpStatus.OK);
    }
}
