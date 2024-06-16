package az.ingress.controller;

import az.ingress.dto.request.AnnouncementRequest;
import az.ingress.dto.request.PageCriteria;
import az.ingress.dto.request.SearchCriteria;
import az.ingress.dto.response.AnnouncementResponse;
import az.ingress.dto.response.PageAnnouncementResponse;
import az.ingress.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @GetMapping("/all")
    public PageAnnouncementResponse getAllAnnouncement(SearchCriteria searchCriteria, PageCriteria pageCriteria) {
        return announcementService.getAllAnnouncement(searchCriteria, pageCriteria);
    }

    @GetMapping("/most")
    public PageAnnouncementResponse getMostViewedAnnouncement(PageCriteria pageCriteria) {
        return announcementService.getMostViewedAnnouncements(pageCriteria);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAnnouncement(@RequestBody AnnouncementRequest announcementRequest) {
        announcementService.createAnnouncement(announcementRequest);
    }

    @PutMapping("/{id}")
    public AnnouncementResponse updateAnnouncement(@PathVariable(value = "id") Long announcementId,
                                                   @RequestBody AnnouncementRequest announcementRequest) {
        return announcementService.updateAnnouncement(announcementId, announcementRequest);
    }

    @DeleteMapping("/{id}")
    public AnnouncementResponse deleteAnnouncement(@PathVariable(value = "id") Long announcementId) {
        return announcementService.deleteAnnouncement(announcementId);
    }

    @GetMapping("/{username}")
    public PageAnnouncementResponse getAllOwnAnnouncement(@PathVariable String username, PageCriteria pageCriteria) {
        return announcementService.getAllOwnAnnouncement(username, pageCriteria);
    }

    @GetMapping("/{username}/{id}")
    public AnnouncementResponse getOwnAnnouncementWithId(@PathVariable String username,
                                                         @PathVariable(value = "id") Long announcementId) {
        return announcementService.getOwnAnnouncementWithId(username, announcementId);
    }

    @GetMapping("/most/{username}")
    public AnnouncementResponse getOwnMostViewedAnnouncement(@PathVariable String username,Principal principal) {
        System.out.println(principal);
        return announcementService.getOwnMostViewedAnnouncement(username);
    }
}
