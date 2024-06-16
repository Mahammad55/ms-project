package az.ingress.service;


import az.ingress.dto.request.AnnouncementRequest;
import az.ingress.dto.request.PageCriteria;
import az.ingress.dto.request.SearchCriteria;
import az.ingress.dto.response.AnnouncementResponse;
import az.ingress.dto.response.PageAnnouncementResponse;

public interface AnnouncementService {
    PageAnnouncementResponse getAllAnnouncement(SearchCriteria searchCriteria, PageCriteria pageCriteria);

    PageAnnouncementResponse getMostViewedAnnouncements(PageCriteria pageCriteria);

    void createAnnouncement(AnnouncementRequest announcementRequest);

    AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest);

    AnnouncementResponse deleteAnnouncement(Long announcementId);

    PageAnnouncementResponse getAllOwnAnnouncement(String username, PageCriteria pageCriteria);

    AnnouncementResponse getOwnAnnouncementWithId(String username, Long announcementId);

    AnnouncementResponse getOwnMostViewedAnnouncement(String username);
}
