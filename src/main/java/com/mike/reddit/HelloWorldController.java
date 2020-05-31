package com.mike.reddit;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/")
    public String helloWorld() {
        String str = "{'title':'Hello world'}";
        return new JSONObject(str).getString("title");
    }
}
