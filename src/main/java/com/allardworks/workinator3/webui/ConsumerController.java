package com.allardworks.workinator3.webui;

import com.allardworks.workinator3.core.Workinator;
import lombok.experimental.var;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsumerController {
    @Autowired
    private Workinator workinator;

    @GetMapping("/consumers")
    public String index(Model model) {
        val consumers = workinator.getConsumers();
        model.addAttribute("consumers", consumers);
        return "consumers";
    }
}
