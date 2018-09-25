package agency.vavien.ekolife.pojo_classes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class DoganlarPojo {

    private int InPersonelID, InLikeAdet;
    private String StFullName, StProfilePhoto, StOneSignalId, Horoscope, StFacebook, StInstagram, StLinkedin;
    private boolean BoIsLiked;

    public DoganlarPojo(JSONObject jsonObject) {
        try {
            InPersonelID = jsonObject.getInt("InPersonelID");
            StFullName = "Nice Yıllara " + jsonObject.getString("StFullName");
            StProfilePhoto = jsonObject.getString("StProfilePhoto");
            InLikeAdet = jsonObject.getInt("InLikeAdet");
            StOneSignalId = jsonObject.getString("StOneSignalId");
            BoIsLiked = jsonObject.getBoolean("BoIsLiked");
            Horoscope = jsonObject.getString("Horoscope");
            StFacebook = jsonObject.getString("StFacebook");
            StInstagram = jsonObject.getString("StInstagram");
            StLinkedin = jsonObject.getString("StLinkedin");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getInPersonelID() {
        return InPersonelID;
    }

    public int getInLikeAdet() {
        return InLikeAdet;
    }

    public String getStFullName() {
        return StFullName;
    }

    public String getStProfilePhoto() {
        return StProfilePhoto;
    }

    public String getStOneSignalId() {
        return StOneSignalId;
    }

    public String getHoroscope() {
        return Horoscope;
    }

    public String getStFacebook() {
        return StFacebook;
    }

    public String getStInstagram() {
        return StInstagram;
    }

    public String getStLinkedin() {
        return StLinkedin;
    }

    public boolean isBoIsLiked() {
        return BoIsLiked;
    }

}

