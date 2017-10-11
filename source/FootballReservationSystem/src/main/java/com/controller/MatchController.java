package com.controller;

import com.services.MatchServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by MinhQuy on 9/30/2017.
 */
@RestController
public class MatchController {
    @Autowired
    MatchServices matchServices;
}
