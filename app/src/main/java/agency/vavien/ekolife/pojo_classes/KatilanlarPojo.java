package agency.vavien.ekolife.pojo_classes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */
public class KatilanlarPojo {

    private int InPersonelID, InLikeAdet;
    private String StFullName, stFrmeMail, StPhoneMobile, StProfilePhoto, StProjectName, DtWorkStart, StOneSignalId;
    private boolean BoIsLiked;

    public KatilanlarPojo(JSONObject jsonObject) {
        try {
            InPersonelID = jsonObject.getInt("InPersonelID");
            StFullName = jsonObject.getString("StFullName");
            stFrmeMail = jsonObject.getString("stFrmeMail");
            StPhoneMobile = jsonObject.getString("StPhoneMobile");
            StProfilePhoto = jsonObject.getString("StProfilePhoto");
            StProjectName = jsonObject.getString("StProjectName");
            DtWorkStart = jsonObject.getString("DtWorkStart");
            InLikeAdet = jsonObject.getInt("InLikeAdet");
            StOneSignalId = jsonObject.getString("StOneSignalId");
            BoIsLiked = jsonObject.getBoolean("BoIsLiked");
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

    public String getStFrmeMail() {
        return stFrmeMail;
    }

    public String getStPhoneMobile() {
        return StPhoneMobile;
    }

    public String getStProfilePhoto() {
        return StProfilePhoto;
    }

    public String getStProjectName() {
        return StProjectName;
    }

    public String getDtWorkStart() {
        return DtWorkStart;
    }

    public String getStOneSignalId() {
        return StOneSignalId;
    }

    public boolean isBoIsLiked() {
        return BoIsLiked;
    }

}
