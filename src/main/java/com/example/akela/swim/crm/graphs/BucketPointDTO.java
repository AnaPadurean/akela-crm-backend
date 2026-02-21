package com.example.akela.swim.crm.graphs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BucketPointDTO {
    private String bucket; // ex: "2026-01-29" / "2026-W05" / "2026-01"
    private double total;
}
