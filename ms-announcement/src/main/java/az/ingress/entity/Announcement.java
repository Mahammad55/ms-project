package az.ingress.entity;

import az.ingress.model.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "announcements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer viewCount;

    @OneToOne
    @JsonIgnore
    private AnnouncementDetail announcementDetail;

    @ManyToOne
    @JsonIgnore
    private User user;
}
