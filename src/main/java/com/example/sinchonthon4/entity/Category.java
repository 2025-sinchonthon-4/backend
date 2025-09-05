package com.example.sinchonthon4.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private String subCategory;

    @OneToMany(mappedBy = "category",
            fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes;
}
