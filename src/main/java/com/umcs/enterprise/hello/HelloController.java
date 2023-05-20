package com.umcs.enterprise.hello;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {






    @GetMapping("/hello")
    public  String helloRest(){
        return  "Hello World!";
    }
}
