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
        model.setPostVolume(1.0);
        return model;
    }
    @GetMapping("/")
    public String index(@ModelAttribute("gasModel") Model gasModel) {
        return "lab";
    }

    @PostMapping("/calculate")
    public String calculate(
            @RequestParam(required = false) String gasName,
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
        gasModel.setTemperature(Temperature);
        gasModel.setMolarMass(MolarMass);
        gasModel.setVolume(Volume);
        gasModel.setPostVolume(PostVolume);

        if (gasName != null && !gasName.isEmpty()) {
            gasModel.updateGasData(gasName);
        }

        gasModel.isValidEnter();
        if (gasModel.IsValid) {
            gasModel.calculatedPressure();
            gasModel.calculatedPostPressure();
            gasModel.checkProcessStatus();
            gasModel.addToHistory(gasModel.getPostVolume(), gasModel.getPostPressure());
            gasModel.getErrors().calculateError();
            double pError = gasModel.calculatePressureError();
            modelMap.addAttribute("pressureError", pError);
        }

        modelMap.addAttribute("gasModel", gasModel);
        return "lab";
    }
    @PostMapping("/clear")
    public String clear(@ModelAttribute("gasModel") Model gasModel) {
        gasModel.clearHistory();
        return "redirect:/";
    }
    @PostMapping("/updateReliability")
    public String updateReliability(@RequestParam String reliability, HttpSession session, ModelMap modelMap) {
        Model gasModel = (Model) session.getAttribute("gasModel");
        if (gasModel != null) {
            gasModel.getErrors().setReliability(Errors.Reliability.valueOf(reliability));
            gasModel.getErrors().calculateError();
            double pError = gasModel.calculatePressureError();
            gasModel.setPressureError(pError);
            modelMap.addAttribute("pressureError", pError);
            modelMap.addAttribute("gasModel", gasModel);
        }
        return "lab";
    }
}