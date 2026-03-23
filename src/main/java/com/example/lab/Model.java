package com.example.lab;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
    public double PostPressure;   public double Volume;
    public double Pressure;

    public void isValidEnter() {
        IsValid = Volume > 0 && MolarMass > 0 && Mass >= 0 && Temperature > 0;
    }

    public void checkProcessStatus() {
        if (IsValid) {
            ProcessInfo = "Изотермический процесс стабилен (T = const)";
        } else {
            ProcessInfo = "Ошибка: Некорректные параметры системы";
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


    public static void main(String[] args) {
        log.info("Lab simulation start");
        Model model = new Model();
    }
}
