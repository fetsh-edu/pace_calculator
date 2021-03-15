package me.fetsh.pacecalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Distance extends Comparable<Distance> {

    static Distance getMetric(double distance) {
        return new MetricDistance(distance);
    }

    static List<Distance> all() {
        ArrayList<Distance> distances = new ArrayList<>();
        for (int i = 0; i <= 42; i++) {
            if (i == 5) {
                distances.add(new NamedDistance(i, "5k"));
            } else if (i == 10) {
                distances.add(new NamedDistance(i, "10k"));
            } else {
                distances.add(getMetric(i));
            }
        }
        distances.add(NamedDistance.getMarathon());
        distances.add(NamedDistance.getHalfMarathon());
        Collections.sort(distances);
        return distances;
    }

    double getDistance();

    class AbstractDistance implements Distance {

        private final double distance;

        public AbstractDistance(double distance) {
            this.distance = distance;
        }

        @Override
        public double getDistance() {
            return distance;
        }

        @Override
        public int compareTo(Distance o) {
            return Double.compare(this.distance, o.getDistance());
        }

        @Override
        public String toString() {
            if (distance % (int) distance == 0.0) {
                return Integer.toString((int) distance);
            } else {
                return Double.toString(distance);
            }
        }
    }

    class MetricDistance extends AbstractDistance {
        public MetricDistance(double distance) {
            super(distance);
        }
    }
    class NamedDistance extends AbstractDistance {
        private final String name;
        public static Distance getMarathon() {
            return new NamedDistance(42.195, "Marathon");
        }
        public static Distance getHalfMarathon() {
            return new NamedDistance(21.0975, "1/2 Marathon");
        }
        public NamedDistance(double distance, String name) {
            super(distance);
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
