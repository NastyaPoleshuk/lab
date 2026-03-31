package com.example.lab;


import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Errors {
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
    private Errors.Reliability reliability = Errors.Reliability.P095;
    public double getCoefficient() {
        int n = history.size();
        if (n < 2) return 0.0;
        int row = Math.min(n, 10) - 2;
        int col = this.reliability.getIndex();
        return Constants.TABLE[row][col];
    }
    private List<Result> history = new ArrayList<>();
    public void addToHistory(double v, double p) {
        this.history.add(new Result(v, p));
    }
    public void clearHistory() {
        this.history.clear();
        this.finalVolumeError = 0.0;
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
        double tCoefficient = getCoefficient();
        this.finalVolumeError = tCoefficient * Math.sqrt(sumSquaredDiff / (1.0 * n * (n - 1)));
        this.finalVolumeError = Math.round(this.finalVolumeError * 100.0) / 100.0;
    }
    private double finalVolumeError = 0.0;
}
