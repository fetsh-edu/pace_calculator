package me.fetsh.pacecalculator;

public interface Pace {

    static Pace withUnitSystem(UnitSystem us, int min, int sec) {
        switch (us) {
            case Imperial: return imperial(min, sec);
            case Metric:  return metric(min, sec);
            default: throw new IllegalArgumentException("Wrong unit system: " + us.name());
        }
    }

    static Pace metric(int min, int sec) {
        return new MetricPace(min, sec);
    }
    static Pace metric(int sec) {
        return new MetricPace(sec);
    }
    static Pace imperial(int min, int sec) {
        return new ImperialPace(min, sec);
    }
    static Pace imperial(int sec) {
        return new ImperialPace(sec);
    }

    static Pace time(int hours, int min, int sec) {
        return new MetricPace(hours*60*60 + min*60 + sec);
    }

    Pace multipliedBy(double distance);
    Pace dividedBy(double distance);

    default int getMinutes() {
        return getSeconds() / 60;
    }
    default int getHours() {
        return getSeconds() / 3600;
    }

    default int getMinutesPart() {
        return (getSeconds() % 3600) / 60;
    }

    default int getSecondsPart() {
        return getSeconds() % 60;
    }

    int getSeconds();

    UnitSystem getUnitSystem();

    abstract class AbstractPace implements Pace {
        private final int seconds;

        private AbstractPace(int min, int sec) {
            this(min * 60 + sec);
        }

        private AbstractPace(int seconds) {
            this.seconds = seconds;
        }

        public Pace multipliedBy(double multiplicand) {
            return null;
        }

        @Override
        public int getSeconds() {
            return seconds;
        }

        @Override
        public String toString() {
            int hours = getHours();
            int minutes = getMinutesPart();
            int lSeconds = getSecondsPart();
            if (hours > 0) {
                return String.format("%d:%02d:%02d", hours, minutes, lSeconds);
            } else {
                return String.format("%d:%02d", minutes, lSeconds);
            }
        }
    }

    class MetricPace extends AbstractPace {

        public MetricPace(int min, int sec) {
            super(min, sec);
        }

        public MetricPace(int seconds) {
            super(seconds);
        }

        public Pace multipliedBy(double multiplicand) {
            return new MetricPace((int) Math.ceil(getSeconds() * multiplicand));
        }

        @Override
        public Pace dividedBy(double distance) {
            return new MetricPace((int) Math.round(getSeconds() / distance) );
        }

        @Override
        public UnitSystem getUnitSystem() {
            return UnitSystem.Metric;
        }

    }
    class ImperialPace extends AbstractPace {

        private ImperialPace(int min, int sec) {
            super(min, sec);
        }

        private ImperialPace(int seconds) {
            super(seconds);
        }

        @Override
        public Pace multipliedBy(double multiplicand) {
            return new ImperialPace((int) Math.ceil(getSeconds() * multiplicand));
        }

        @Override
        public Pace dividedBy(double distance) {
            return new ImperialPace((int) Math.round(getSeconds() / distance) );
        }

        @Override
        public UnitSystem getUnitSystem() {
            return UnitSystem.Imperial;
        }

    }
}
