package com.example.lab;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

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
    public enum Reliability {
        P05(0, 0.5),
        P06(1, 0.6),
        P07(2, 0.7),
        P08(3, 0.8),
        P09(4, 0.9),
        P095(5, 0.95),
        P099(6, 0.99);
        private final int index;
        private final double value;
        Reliability(int index, double value) {
            this.index = index;
            this.value = value;
        }
        public int getIndex() { return index; }
        public double getValue() { return value; }
    }
    private Reliability reliability = Reliability.P095;
    public double getCoefficient() {
        int n = history.size();
        if (n < 2) return 0.0;
        int row = Math.min(n, 10) - 2;
        int col = this.reliability.getIndex();

        return Constants.TABLE[row][col];
    }
    public void isValidEnter() {
        IsValid = Volume > 0 && MolarMass > 0 && Mass > 0 && Temperature > 0;
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

    private List<Result> history = new ArrayList<>();
    public boolean isSameExperiment(double newMass, double newMolarMass, double newTemp) {
        return Double.compare(this.Mass, newMass) == 0 &&
                Double.compare(this.MolarMass, newMolarMass) == 0 &&
                Double.compare(this.Temperature, newTemp) == 0;
    }
    public void addToHistory(double v, double p) {
        this.history.add(new Result(v, p));
    }
    public void clearHistory() {
        this.history.clear();
    }
    public void calculateError() {
        int n = history.size();
        if (n < 2) {
            this.finalVolumeError = 0.0;
            return;
        }
        double sumV = 0;
        for (Result res : history) {
            sumV += res.getFinalVolume();
        }
        double averageV = sumV / n;
        double sumSquaredDiff = 0;
        for (Result res : history) {
            double diff = res.getFinalVolume() - averageV;
            sumSquaredDiff += diff * diff;
        }
        double error = Math.sqrt(sumSquaredDiff / (n * (n - 1))) * getCoefficient();
        this.finalVolumeError = Math.round(error * 100.0) / 100.0;
    }
    private double finalVolumeError = 0.0;
}
