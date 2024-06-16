package az.ingress.service.impl;

import az.ingress.dto.request.AnnouncementRequest;
import az.ingress.dto.request.PageCriteria;
import az.ingress.dto.request.SearchCriteria;
import az.ingress.dto.response.AnnouncementResponse;
import az.ingress.dto.response.PageAnnouncementResponse;
import az.ingress.entity.Announcement;
import az.ingress.entity.AnnouncementDetail;
import az.ingress.exception.NotFoundException;
import az.ingress.mapper.AnnouncementMapper;
import az.ingress.model.entity.User;
import az.ingress.repository.AnnouncementDetailRepository;
import az.ingress.repository.AnnouncementRepository;
import az.ingress.repository.UserRepository;
import az.ingress.service.AnnouncementService;
import az.ingress.specification.AnnouncementSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.ingress.enums.ExceptionMessage.ANNOUNCEMENT_DETAILS_NOT_FOUND;
import static az.ingress.enums.ExceptionMessage.ANNOUNCEMENT_NOT_FOUND;
import static az.ingress.enums.ExceptionMessage.USER_NOT_FOUND;
import static az.ingress.mapper.PageableMapper.mapAnnouncementResponsePageToCustomPageResponse;
import static az.ingress.util.AnnouncementUtil.getDefaultPageable;


@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    private final UserRepository userRepository;

    private final AnnouncementDetailRepository announcementDetailRepository;

    private final AnnouncementMapper announcementMapper;

    @Override
    public PageAnnouncementResponse getAllAnnouncement(SearchCriteria searchCriteria, PageCriteria pageCriteria) {
        Pageable pageable = getDefaultPageable(pageCriteria);
        Page<Announcement> announcementPage = announcementRepository.findAll(new AnnouncementSpecification(searchCriteria), pageable);

        if (announcementPage.getContent().isEmpty())
            throw new NotFoundException(ANNOUNCEMENT_NOT_FOUND.getMessage());

        List<AnnouncementResponse> announcementResponseList = announcementPage.getContent().stream().map(announcementMapper::entityToResponse).toList();
        Page<AnnouncementResponse> announcementResponsePage = new PageImpl<>(announcementResponseList);
        return mapAnnouncementResponsePageToCustomPageResponse(announcementResponsePage);
    }

    @Override
    public PageAnnouncementResponse getMostViewedAnnouncements(PageCriteria pageCriteria) {
        Pageable pageable = getDefaultPageable(pageCriteria);
        Page<Announcement> announcementPage = announcementRepository.findMostViewedAnnouncements(pageable);

        if (announcementPage.getContent().isEmpty())
            throw new NotFoundException(ANNOUNCEMENT_NOT_FOUND.getMessage());

        List<AnnouncementResponse> announcementResponseList = announcementPage.getContent().stream().map(announcementMapper::entityToResponse).toList();
        Page<AnnouncementResponse> announcementResponsePage = new PageImpl<>(announcementResponseList);
        return mapAnnouncementResponsePageToCustomPageResponse(announcementResponsePage);
    }

    @Override
    public void createAnnouncement(AnnouncementRequest announcementRequest) {
        String username = announcementRequest.getUsername();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage().formatted(username)));

        Long announcementDetailsId = announcementRequest.getAnnouncementDetailsId();
        AnnouncementDetail announcementDetail = announcementDetailRepository.findById(announcementDetailsId)
                .orElseThrow(() -> new NotFoundException(ANNOUNCEMENT_DETAILS_NOT_FOUND.getMessage().formatted(announcementDetailsId)));

        Announcement announcement = announcementMapper.requestToEntity(announcementRequest);
        announcement.setUser(user);
        announcement.setAnnouncementDetail(announcementDetail);
        announcementRepository.save(announcement);
    }

    @Override
    @CachePut(cacheNames = "announcement", key = "#announcementId")
    public AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest) {
        announcementRepository.findById(announcementId)
                .orElseThrow(() -> new NotFoundException(ANNOUNCEMENT_NOT_FOUND.getMessage().formatted(announcementId)));

        String username = announcementRequest.getUsername();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage().formatted(username)));

        Long announcementDetailsId = announcementRequest.getAnnouncementDetailsId();
        AnnouncementDetail announcementDetail = announcementDetailRepository.findById(announcementDetailsId)
                .orElseThrow(() -> new NotFoundException(ANNOUNCEMENT_DETAILS_NOT_FOUND.getMessage().formatted(announcementDetailsId)));

        Announcement announcement = announcementMapper.requestToEntity(announcementRequest);
        announcement.setId(announcementId);
        announcement.setUser(user);
        announcement.setAnnouncementDetail(announcementDetail);
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return announcementMapper.entityToResponse(savedAnnouncement);
    }

    @Override
    @CacheEvict(cacheNames = "announcement", key = "#announcementId")
    public AnnouncementResponse deleteAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new NotFoundException(ANNOUNCEMENT_NOT_FOUND.getMessage().formatted(announcementId)));

        announcementRepository.delete(announcement);
        return announcementMapper.entityToResponse(announcement);
    }

    @Override
    public PageAnnouncementResponse getAllOwnAnnouncement(String username, PageCriteria pageCriteria) {
        Pageable pageable = getDefaultPageable(pageCriteria);
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage().formatted(username)));

        Page<Announcement> announcementPage = announcementRepository.findAllByUser(user, pageable);

        if (announcementPage.getContent().isEmpty())
            throw new NotFoundException(ANNOUNCEMENT_NOT_FOUND.getMessage().formatted("all"));

        List<AnnouncementResponse> announcementResponseList = announcementPage.getContent().stream().map(announcementMapper::entityToResponse).toList();
        Page<AnnouncementResponse> announcementResponsePage = new PageImpl<>(announcementResponseList);
        return mapAnnouncementResponsePageToCustomPageResponse(announcementResponsePage);
    }

    @Override
    @Cacheable(cacheNames = "announcement", key = "#announcementId")
    public AnnouncementResponse getOwnAnnouncementWithId(String username, Long announcementId) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage().formatted(username)));

        Announcement announcement = announcementRepository.findAnnouncementByIdAndUser(announcementId, user)
                .orElseThrow(() -> new NotFoundException(ANNOUNCEMENT_NOT_FOUND.getMessage().formatted(username + " " + announcementId)));
        return announcementMapper.entityToResponse(announcement);
    }

    @Override
    public AnnouncementResponse getOwnMostViewedAnnouncement(String username) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage().formatted(username)));

        Announcement announcement = announcementRepository.findOwnMostViewedAnnouncement(user)
                .orElseThrow(() -> new NotFoundException(ANNOUNCEMENT_NOT_FOUND.getMessage().formatted(user)));
        return announcementMapper.entityToResponse(announcement);

    }
}
