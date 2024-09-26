package com.milan.codechangepresentationgenerator.shared.abstractentity;

import com.milan.codechangepresentationgenerator.shared.status.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private Status status=Status.ACTIVE;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
    private Date lastModifiedDate=createdDate;
    private String version="test";
    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }

}