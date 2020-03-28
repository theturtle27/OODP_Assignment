package View;

public abstract class View {
    protected final String title;

    public View(String title) {this.title = title;}

    public String getTitle() {
        return title;
    }

    public abstract void show();

    public abstract void onSelected();
}
