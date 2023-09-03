package com.keyrus.proxemconnector;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class Hello {
    @GetMapping("/get")
    public String sayHello(){
        return "hello word";
    }
}
