package siarhei.luskanau.iot.lamp.domain;

public class LampConfig {

    private String name;

    private boolean value;

    public LampConfig(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
