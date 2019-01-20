package agency.vavien.ekolife.pojo_classes;

/**
 * Created by SD on 18.12.2017.
 * dilmacsedat@gmail.com
 * :)
 */

public class DashHaberPojo {
    private String title, summary, newsDate;
    private int newsId;

    public DashHaberPojo(String title, String summary, int newsId, String newsDate) {
        this.title = title;
        this.summary = summary;
        this.newsId = newsId;
        this.newsDate = newsDate;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public int getNewsId() {
        return newsId;
    }

    public String getNewsDate() {
        return newsDate;
    }

}
