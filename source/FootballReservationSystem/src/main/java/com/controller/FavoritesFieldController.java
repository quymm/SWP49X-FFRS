package com.controller;

import com.dto.Wrapper;
import com.services.FavoritesFieldServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FavoritesFieldController {
    @Autowired
    FavoritesFieldServices favoritesFieldServices;

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/favorites/managed-favorites", method = RequestMethod.POST)
    public ResponseEntity addNewFavoritesField(@RequestParam("user-id") int userId, @RequestParam("field-owner-id") int fieldOwnerId){
        Wrapper wrapper = new Wrapper(favoritesFieldServices.createNewFavoritesField(userId, fieldOwnerId), HttpStatus.CREATED.value(), HttpStatus.CREATED.name());
        return new ResponseEntity(wrapper, HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/favorites/managed-favorites", method = RequestMethod.GET)
    public ResponseEntity findFavoritesByUserId(@RequestParam("user-id") int userId){
        Wrapper wrapper = new Wrapper(favoritesFieldServices.findFavoritesFieldByUserId(userId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/favorites/managed-favorites", method = RequestMethod.DELETE)
    public ResponseEntity deleteFavorites(@RequestParam("id") int id){
        Wrapper wrapper = new Wrapper(favoritesFieldServices.removeFavoritesEntity(id), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/swp49x-ffrs/favorites/user-and-field", method = RequestMethod.GET)
    public ResponseEntity findFavoritesByUserIdAndFieldOwnerId(@RequestParam("user-id") int userId, @RequestParam("field-owner-id") int fieldOwnerId){
        Wrapper wrapper = new Wrapper(favoritesFieldServices.findFavoritesFieldByUserIdAndFieldOwnerId(userId, fieldOwnerId), HttpStatus.OK.value(), HttpStatus.OK.name());
        return new ResponseEntity(wrapper, HttpStatus.OK);
    }
}
