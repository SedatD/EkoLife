package agency.vavien.ekolife.pojo_classes;

/**
 * Created by SD on 19.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class HaberlerPojo {
    private String bitmapPhoto, text, date;

    public HaberlerPojo(String bitmapPhoto, String text, String date) {
        this.bitmapPhoto = bitmapPhoto;
        this.text = text;
        this.date = date;
    }

    public String getBitmapPhoto() {
        return bitmapPhoto;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
