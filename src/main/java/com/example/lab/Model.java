package com.example.lab;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.abs;

@Slf4j
@Setter
@Getter
public class Model {
    public double Temperature;
    public double Mass;
    public double MolarMass;
    public boolean IsValid;
    public String ProcessInfo;
    public double PostVolume;
    public double PostPressure;
    public double Volume;
    public double Pressure;

    public void isValidEnter() {
        IsValid = Volume > 0 && MolarMass > 0 && Mass >= 0 && Temperature > 0 && PostVolume > 0;
    }

    public void checkProcessStatus() {
        if (IsValid) {
            ProcessInfo = "Изотермический процесс стабилен (T = const)";
        } else {
            ProcessInfo = "Ошибка: Некорректные параметры системы";
        }
    }

    public void setGasType(String gasName) {
        switch (gasName) {
            case "Кислород" -> this.MolarMass = 0.032;
            case "Азот" -> this.MolarMass = 0.028;
            case "Воздух" -> this.MolarMass = 0.029;
            case "Гелий" -> this.MolarMass = 0.004;
            default -> this.MolarMass = 1.0;
        }
    }

    public void updateGasData(String gasName) {
        setGasType(gasName);
        if (this.Mass > 0 && this.Temperature > 0) {
            double P1 = 101325.0;
            this.Volume = (this.Mass * Constants.MOLAR_GAS_CONSTANT * this.Temperature) / (this.MolarMass * P1);
            this.Volume = Math.round(this.Volume * 100.0) / 100.0;
            if (this.PostVolume == 0 || this.PostVolume == 1.0) {
                this.PostVolume = this.Volume;
            }
        }
    }

    public void calculatedPressure() {
        double MolarGasConstant = Constants.MOLAR_GAS_CONSTANT;
        if (IsValid && this.Volume > 0) {
            this.Pressure = (this.Mass * MolarGasConstant * this.Temperature) / (this.MolarMass * this.Volume);
            this.Pressure = Math.round(this.Pressure * 100.0) / 100.0;
        }
    }

    public void calculatedPostPressure() {
        double MolarGasConstant = Constants.MOLAR_GAS_CONSTANT;
        if (IsValid && this.PostVolume > 0) {
            this.PostPressure = (this.Mass * MolarGasConstant * this.Temperature) / (this.MolarMass * this.PostVolume);
            this.PostPressure = Math.round(this.PostPressure * 100.0) / 100.0;
        }
    }

    private Errors errors = new Errors();
    private List<Result> history = new ArrayList<>();

    public boolean isSameExperiment(double newMass, double newMolarMass, double newTemp) {
        return Double.compare(this.Mass, newMass) == 0 &&
                Double.compare(this.MolarMass, newMolarMass) == 0 &&
                Double.compare(this.Temperature, newTemp) == 0;
    }
    public void addToHistory(double v, double p) {
        this.history.add(new Result(v, p));
        if (this.errors != null) {
            this.errors.addToHistory(v, p);
        }
    }
    public void clearHistory() {
        this.history.clear();
        this.errors.clearHistory();
    }
}
