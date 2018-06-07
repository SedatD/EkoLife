package agency.vavien.ekolife.pojo_classes;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */
public class KatilanlarPojo {
    private int id,likeAdet;
    private String departman, name, mail, bitmapPhoto,osi;

    public KatilanlarPojo(int id, String bitmapPhoto, String departman, String name, String mail,String osi,int likeAdet) {
        this.id = id;
        this.bitmapPhoto = bitmapPhoto;
        this.departman = departman;
        this.name = name;
        this.mail = mail;
        this.osi = osi;
        this.likeAdet = likeAdet;
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

    public int getLikeAdet() {
        return likeAdet;
    }

    public String getOsi() {
        return osi;
    }
}
