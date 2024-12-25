package com.example.demo.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;

    private String issue;

    private String issueType;

    private String raisedBy;

    private ZonedDateTime raisedOn;  // Change to ZonedDateTime to handle time zones

    private String recordingVideo;

    private String recordingAudio;

    private String raisedStatus;

    private boolean taskDone;

    public Task() {}

    // Getters and Setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public ZonedDateTime getRaisedOn() {
        return raisedOn;
    }

    public void setRaisedOn(ZonedDateTime raisedOn) {
        this.raisedOn = raisedOn;
    }

    public String getRecordingVideo() {
        return recordingVideo;
    }

    public void setRecordingVideo(String recordingVideo) {
        this.recordingVideo = recordingVideo;
    }

    public String getRecordingAudio() {
        return recordingAudio;
    }

    public void setRecordingAudio(String recordingAudio) {
        this.recordingAudio = recordingAudio;
    }

    public String getRaisedStatus() {
        return raisedStatus;
    }

    public void setRaisedStatus(String raisedStatus) {
        this.raisedStatus = raisedStatus;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }
}
