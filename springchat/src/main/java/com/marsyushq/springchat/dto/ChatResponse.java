package com.marsyushq.springchat.dto;

import java.time.Instant;
import java.util.List;

public class ChatResponse {
    private String id;
    private List<String> participantIds;
    private Instant createdAt; 
    private Instant updatedAt;

    public ChatResponse(String id, List<String> participantIds, Instant createdAt, Instant updatedAt){
        this.id = id;
        this.participantIds = participantIds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
    
}
