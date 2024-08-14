package com.milan.codechangepresentationgenerator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Presentation {
    @Id
    private String id;
    @Lob
    private byte[] data;
    private Timestamp createdAt;
}
