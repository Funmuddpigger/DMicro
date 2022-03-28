package mine.cloud.DMicro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Video {
    private Integer videoId;

    private Integer usrId;

    private Long videoPlay;

    private Date videoPostime;

    private Long videoLike;

    private String videoUrl;

    private String videoType;

    private String videoType2;

    private String videoTitle;

    private String videoImg;

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl == null ? null : videoUrl.trim();
    }
}