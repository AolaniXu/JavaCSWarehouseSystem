package model;

public class MenuItem {

    private Integer code;
    private String title;
    private Boolean isMenu;
    private String funcClassName;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(Boolean isMenu) {
        this.isMenu = isMenu;
    }

    public String getFuncClassName() {
        return funcClassName;
    }

    public void setFuncClassName(String funcClassName) {
        this.funcClassName = funcClassName;
    }
}