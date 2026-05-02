package model;

public class Product {

    private Integer id;
    private String code;
    private String name;
    private String spec;
    private String unit;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpec() { return spec; }
    public void setSpec(String spec) { this.spec = spec; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    @Override
    public String toString() {
        // 树上显示的内容
        if (spec != null) {
            return name + " (" + spec + ")";
        }
        return name;
    }
}