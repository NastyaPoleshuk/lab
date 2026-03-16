package com.example.lab.controller;

import com.example.lab.Model;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LabController {

    @GetMapping("/")
    public String index(ModelMap modelMap) {
        Model gasModel = new Model();
        // Устанавливаем дефолтные значения через ваши сеттеры
        gasModel.setTemperature(300.0);
        gasModel.setMass(0.028);
        gasModel.setMolarMass(0.028);
        gasModel.setVolume(0.1);
        gasModel.setPostVolume(0.2);
        modelMap.addAttribute("gasModel", gasModel);
        return "lab"; // Должно совпадать с именем lab.html в templates
    }

    @PostMapping("/calculate")
    public String calculate(@ModelAttribute("gasModel") Model gasModel, ModelMap modelMap) {
        gasModel.isValidEnter();
        if (gasModel.IsValid) {
            gasModel.calculatedPressure();
            gasModel.calculatedPostPressure();
        }

        gasModel.checkProcessStatus();

        modelMap.addAttribute("gasModel", gasModel);
        return "lab";
    }
}