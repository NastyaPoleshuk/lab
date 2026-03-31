package com.example.lab.controller;

import com.example.lab.Model;
import com.example.lab.Errors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;


@Controller
@SessionAttributes("gasModel")
public class LabController {

    @ModelAttribute("gasModel")
    public Model gasModel() {
        Model model = new Model();
        model.setTemperature(300.0);
        model.setMass(1.0);
        model.setMolarMass(1.0);
        model.setVolume(1.0);
        model.setPostVolume(2.0);
        return model;
    }
    @GetMapping("/")
    public String index(@ModelAttribute("gasModel") Model gasModel) {
        return "lab";
    }

    @PostMapping("/calculate")
    public String calculate(
            @RequestParam double Mass,
            @RequestParam double MolarMass,
            @RequestParam double Temperature,
            @RequestParam double PostVolume,
            @RequestParam double Volume,
            jakarta.servlet.http.HttpSession session,
            ModelMap modelMap) {
        Model gasModel = (Model) session.getAttribute("gasModel");

        if (gasModel == null) {
            gasModel = gasModel();
        }


        if (!gasModel.isSameExperiment(Mass, MolarMass, Temperature)) {
            gasModel.clearHistory();
        }

        gasModel.setMass(Mass);
        gasModel.setMolarMass(MolarMass);
        gasModel.setTemperature(Temperature);
        gasModel.setVolume(Volume);
        gasModel.setPostVolume(PostVolume);


        gasModel.isValidEnter();
        gasModel.checkProcessStatus();
        if (gasModel.IsValid) {
            gasModel.calculatedPressure();
            gasModel.calculatedPostPressure();
            gasModel.addToHistory(gasModel.getPostVolume(), gasModel.getPostPressure());
        }

        modelMap.addAttribute("gasModel", gasModel);
        return "lab";
    }
    @PostMapping("/calculateError")
    public String calculateError(@RequestParam String reliability,
                                 HttpSession session,
                                 ModelMap modelMap) {
        Model gasModel = (Model) session.getAttribute("gasModel");
        if (gasModel == null) {
            return "redirect:/";
        }
        if (gasModel != null) {
            Errors.Reliability selected = Errors.Reliability.valueOf(reliability);
            gasModel.getErrorCalculator().setReliability(selected);
            gasModel.calculateError();
        }
        modelMap.addAttribute("gasModel", gasModel);
        return "lab";
    }
}