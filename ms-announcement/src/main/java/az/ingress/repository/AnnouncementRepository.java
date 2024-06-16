package az.ingress.repository;


import az.ingress.entity.Announcement;
import az.ingress.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long>, JpaSpecificationExecutor<Announcement> {
    @Query("SELECT a1 FROM Announcement a1 WHERE a1.viewCount=(SELECT MAX(a2.viewCount) FROM Announcement a2)")
    Page<Announcement> findMostViewedAnnouncements(Pageable pageable);

    @Query("SELECT a1 FROM Announcement a1 WHERE a1.viewCount=(SELECT MAX(a2.viewCount) FROM Announcement a2)" +
            " AND a1.user=:user")
    Optional<Announcement> findOwnMostViewedAnnouncement(User user);

    Page<Announcement> findAllByUser(User user, Pageable pageable);

    Optional<Announcement> findAnnouncementByIdAndUser(Long id, User user);
}
