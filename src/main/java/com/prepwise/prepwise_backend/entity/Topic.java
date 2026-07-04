package com.prepwise.prepwise_backend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="topic")
public class Topic
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;
    


    @Column(name="topic_name", nullable = false)
    private String topicName;

    @Column(name="description")
    private String description;

    @Column(name="displayOrder")
     private Integer displayOrder;

    @Column(name="weightage")
    private Integer weightage;

     @ManyToOne
     @JoinColumn(name="chapter_id",nullable=false)
     private Chapter chapter;
     
     @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User user;

    

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}