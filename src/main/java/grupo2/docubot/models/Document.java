package grupo2.docubot.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastModified;

    private String title;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name="document_use_case",
            joinColumns = @JoinColumn(name="document_id"),
            inverseJoinColumns = @JoinColumn(name="use_case_id")
    )
    private List<UseCase> content = new ArrayList<>();

    @Column(nullable = false)
    private Boolean published;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.published = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModified = LocalDateTime.now();
    }
}
