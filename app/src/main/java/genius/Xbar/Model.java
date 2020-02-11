package genius.Xbar;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Model {
    String name;
    Bitmap imaBitmap;
    String pac;
    String text;
    String ticker;
    Drawable icon;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPac() {
        return pac;
    }

    public void setPac(String pac) {
        this.pac = pac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return imaBitmap;
    }

    public void setImage(Bitmap imaBitmap) {
        this.imaBitmap = imaBitmap;
    }
}
