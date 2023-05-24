package com.umcs.enterprise.hello;


import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RepositoryRestController
public class HelloController {






    @GetMapping("/hello")
    public  String helloRest(){
        return  "Hello World!";
    }
}
