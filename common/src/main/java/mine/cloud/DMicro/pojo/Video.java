package mine.cloud.DMicro.pojo;

import java.util.Date;

public class Video {
    private Integer videoId;

    private Integer usrId;

    private Long videoPlay;

    private Date videoPostime;

    private Long videoLike;

    private String videoUrl;

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getUsrId() {
        return usrId;
    }

    public void setUsrId(Integer usrId) {
        this.usrId = usrId;
    }

    public Long getVideoPlay() {
        return videoPlay;
    }

    public void setVideoPlay(Long videoPlay) {
        this.videoPlay = videoPlay;
    }

    public Date getVideoPostime() {
        return videoPostime;
    }

    public void setVideoPostime(Date videoPostime) {
        this.videoPostime = videoPostime;
    }

    public Long getVideoLike() {
        return videoLike;
    }

    public void setVideoLike(Long videoLike) {
        this.videoLike = videoLike;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl == null ? null : videoUrl.trim();
    }
}