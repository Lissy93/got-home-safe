package net.as93.homesafe.menu;

public class MenuModel{

    private int icon;
    private String title;

    public MenuModel(String title) {
        this(-1,title,null);
    }
    public MenuModel(int icon, String title, String counter) {
        super();
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}