package com.controller;

import com.dto.InputCommentDTO;
import com.service.AndroidServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Minh Quy on 3/6/2017.
 */
@RestController(value = "/android")
public class AndroidController {
    @Autowired
    AndroidServices androidServices;

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public Map<String, Object> getCategory() {
        Map<String, Object> map = new HashMap();
        map.put("result", androidServices.getAllCategory());
        return map;
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public Map<String, Object> getProductByCategoryCode(@RequestParam(name = "categoryCode") String categoryCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", androidServices.getProductByCategoryCode(categoryCode));
        return map;
    }

    @RequestMapping(value = "/product", method = RequestMethod.PUT)
    public Map<String, Object> likeOrUnlikeProduct(@RequestParam(name = "productCode") String productCode,
                                                   @RequestParam(name = "like") Boolean like) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", androidServices.likeOrUnlikeProduct(productCode, like));
        return map;
    }

    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    public Map<String, Object> getCommentByProductCode(@RequestParam(name = "productCode") String productCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", androidServices.getCommentByProduct(productCode));
        return map;
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Map<String, Object> insertNewComment(@RequestBody InputCommentDTO comment) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", androidServices.createNewCommentForProduct(comment));
        return map;
    }

}
