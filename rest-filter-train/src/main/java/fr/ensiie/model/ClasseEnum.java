package fr.ensiie.model;

public enum ClasseEnum {


    STANDARD(0, "STANDARD"),
    FIRST(1, "FIRST"),
    BUSINESS(2, "BUSINESS");
    private int id;
    private String name;

    ClasseEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ClasseEnum getByName(String name) {
        for (ClasseEnum classeEnum : ClasseEnum.values()) {
            if (classeEnum.name.equalsIgnoreCase(name)) {
                return classeEnum;
            }
        }
        return null;
    }

    public int getId() {
        return this.id;
    }
}
