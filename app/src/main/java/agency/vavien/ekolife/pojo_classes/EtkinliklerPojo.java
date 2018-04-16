package agency.vavien.ekolife.pojo_classes;

/**
 * Created by SD on 19.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class EtkinliklerPojo {
    private String title, text, yer, date;

    public EtkinliklerPojo(String title, String text, String yer, String date) {
        this.title = title;
        this.text = text;
        this.yer = yer;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getYer() {
        return yer;
    }

    public String getDate() {
        return date;
    }
}