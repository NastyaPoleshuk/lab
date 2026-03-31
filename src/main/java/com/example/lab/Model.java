package com.example.lab;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
        IsValid = Volume > 0 && MolarMass > 0 && Mass > 0 && Temperature > 0;
    }
    private Errors errorCalculator = new Errors();
    public void addToHistory(double v, double p) {
        errorCalculator.addToHistory(v, p);
    }
    public void clearHistory() {
        errorCalculator.clearHistory();
    }
    public boolean isSameExperiment(double m, double mm, double t) {
        return Double.compare(this.Mass, m) == 0 &&
                Double.compare(this.MolarMass, mm) == 0 &&
                Double.compare(this.Temperature, t) == 0;
    }
    public void calculateError() {
        errorCalculator.calculateError();
    }
    public void checkProcessStatus() {
        if (IsValid) {
            ProcessInfo = "Изотермический процесс стабилен (T = const)";
        } else {
            ProcessInfo = "Некорректные параметры";
        }
    }

    public void calculatedPressure() {
        double MolarGasConstant = Constants.MOLAR_GAS_CONSTANT;
        if (IsValid) {
            this.Pressure = (this.Mass * MolarGasConstant * this.Temperature) / (this.MolarMass * this.Volume);
            this.Pressure = Math.round(this.Pressure * 100.0) / 100.0;
        }
    }

    public void calculatedPostPressure() {
        double MolarGasConstant = Constants.MOLAR_GAS_CONSTANT;
        if (IsValid) {
            this.PostPressure = (this.Mass * MolarGasConstant * this.Temperature) / (this.MolarMass * this.PostVolume);
            this.PostPressure = Math.round(this.PostPressure * 100.0) / 100.0;
        }
    }
}
