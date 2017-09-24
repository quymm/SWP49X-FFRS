package com.controller;

<<<<<<< HEAD
import com.entity.FieldOwnerEntity;
import com.services.FieldOwnerService;
=======
import com.dto.InputFieldOwnerDTO;
import com.entity.FieldOwnerEntity;
import com.services.FieldOwnerServices;
>>>>>>> master
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
<<<<<<< HEAD
 * Created by truonghuuthanh on 9/23/17.
 */
@RestController
@RequestMapping(value = "/fieldowner")
public class FieldOwnerController {

    @Autowired
    FieldOwnerService fieldOwnerService;


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/login")
    public ResponseEntity login (@RequestParam("username") String username, @RequestParam("password") String password){
        FieldOwnerEntity fieldOwnerEntity = fieldOwnerService.fieldOwnerLogin(username, password);
        return new ResponseEntity(fieldOwnerEntity, HttpStatus.OK);
    }


//    public ResponseEntity register(){
//
//    }

}

=======
 * Created by MinhQuy on 9/24/2017.
 */
@RestController
public class FieldOwnerController {
    @Autowired
    FieldOwnerServices fieldOwnerServices;

    @RequestMapping(value = "/fieldOwner/createNewFieldOwner", method = RequestMethod.POST)
    public ResponseEntity createNewFieldOwner(@RequestBody InputFieldOwnerDTO inputFieldOwnerDTO) {
        FieldOwnerEntity fieldOwnerEntity = fieldOwnerServices.createNewFieldOwner(inputFieldOwnerDTO);
        return new ResponseEntity(fieldOwnerEntity, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/fieldOwner/getByFieldOwnerId", method = RequestMethod.GET)
    public ResponseEntity getFieldOwnerById(@RequestParam("fieldOwnerId") int fieldOwnerId){
        FieldOwnerEntity fieldOwnerEntity = fieldOwnerServices.getFieldOwnerEntityByFieldOwnerId(fieldOwnerId);
        return new ResponseEntity(fieldOwnerEntity, HttpStatus.FOUND);
    }
}
>>>>>>> master
