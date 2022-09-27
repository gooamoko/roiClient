package ru.gooamoko.roiClient.model;

public class PetitionModel extends IdAndTitleModel {
    private GeoModel geo;
    private LevelModel level;
    private StatusModel status;

    public GeoModel getGeo() {
        return geo;
    }

    public void setGeo(GeoModel geo) {
        this.geo = geo;
    }

    public LevelModel getLevel() {
        return level;
    }

    public void setLevel(LevelModel level) {
        this.level = level;
    }

    public StatusModel getStatus() {
        return status;
    }

    public void setStatus(StatusModel status) {
        this.status = status;
    }
}
