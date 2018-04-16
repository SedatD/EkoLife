package agency.vavien.ekolife.pojo_classes;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */
public class KatilanlarPojo {
    private int id;
    private String departman, name, mail, bitmapPhoto;

    public KatilanlarPojo(int id, String bitmapPhoto, String departman, String name, String mail) {
        this.id = id;
        this.bitmapPhoto = bitmapPhoto;
        this.departman = departman;
        this.name = name;
        this.mail = mail;
    }

    public String getBitmapPhoto() {
        return bitmapPhoto;
    }

    public String getDepartman() {
        return departman;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public int getId() {
        return id;
    }

}
