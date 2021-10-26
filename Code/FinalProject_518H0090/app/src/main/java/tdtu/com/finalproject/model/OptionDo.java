package tdtu.com.finalproject.model;

public class OptionDo {
    private String keyOptions;
    private String nameOptions;

    public OptionDo(String keyOptions, String nameOptions) {
        this.keyOptions = keyOptions;
        this.nameOptions = nameOptions;
    }

    public String getKeyOptions() {
        return keyOptions;
    }

    public void setKeyOptions(String keyOptions) {
        this.keyOptions = keyOptions;
    }

    public String getNameOptions() {
        return nameOptions;
    }

    public void setNameOptions(String nameOptions) {
        this.nameOptions = nameOptions;
    }
}
