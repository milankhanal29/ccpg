package com.milan.codechangepresentationgenerator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Presentation {
    @Id
    private String id;
    private String userEmail;
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;
    private LocalDateTime createdAt;
}
