package ru.gooamoko.roiClient.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "PETITIONS")
public class PetitionEntity {

    @Id
    @Column(name = "PET_ID", nullable = false)
    private Long id;

    @Column(name = "PET_TITLE", nullable = false, length = 255)
    private String title;

    @Column(name = "PET_LEVEL_ID", nullable = false)
    private int levelId;

    @Column(name = "PET_STATUS_ID", nullable = false)
    private int statusId;

    @Column(name = "PET_UPDATE_TIMESTAMP", nullable = false)
    private LocalDateTime updateTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public LocalDateTime getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(LocalDateTime updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}
