package agency.vavien.ekolife.pojo_classes;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class DoganlarPojo {
    private int id, likeAdet;
    private String bitmapPhoto, name, horoscope, facebook, instagram, osi;

    public DoganlarPojo(int id, String bitmapPhoto, String name, String horoscope, String facebook, String instagram,String osi,int likeAdet) {
        this.id = id;
        this.bitmapPhoto = bitmapPhoto;
        this.name = name;
        this.horoscope = horoscope;
        this.facebook = facebook;
        this.instagram = instagram;
        this.osi = osi;
        this.likeAdet = likeAdet;
    }

    public int getId() {
        return id;
    }

    public String getBitmapPhoto() {
        return bitmapPhoto;
    }

    public String getName() {
        return name;
    }

    public String getHoroscope() {
        return horoscope;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public int getLikeAdet() {
        return likeAdet;
    }

    public String getOsi() {
        return osi;
    }
}

