package az.ingress.service.impl;

import az.ingress.dto.request.AnnouncementRequest;
import az.ingress.dto.request.PageCriteria;
import az.ingress.dto.request.SearchCriteria;
import az.ingress.dto.response.AnnouncementResponse;
import az.ingress.dto.response.PageAnnouncementResponse;
import az.ingress.entity.Announcement;
import az.ingress.entity.AnnouncementDetail;
import az.ingress.mapper.AnnouncementMapper;
import az.ingress.mapper.PageableMapper;
import az.ingress.model.entity.User;
import az.ingress.repository.AnnouncementDetailRepository;
import az.ingress.repository.AnnouncementRepository;
import az.ingress.repository.UserRepository;
import az.ingress.specification.AnnouncementSpecification;
import az.ingress.util.AnnouncementUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnnouncementServiceImplTest {
    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private AnnouncementDetailRepository announcementDetailRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnnouncementMapper announcementMapper;

    @Mock
    private Announcement announcement;

    @Mock
    private AnnouncementDetail announcementDetail;

    @Mock
    private AnnouncementResponse announcementResponse;

    @Mock
    private AnnouncementRequest announcementRequest;

    @Mock
    private User user;

    @Mock
    private SearchCriteria searchCriteria;

    @Mock
    private PageCriteria pageCriteria;

    @Mock
    private PageAnnouncementResponse pageAnnouncementResponse;

    @Mock
    private Page<Announcement> announcementPage;

    @Mock
    private Page<AnnouncementResponse> announcementResponsePage;

    @Mock
    private Pageable pageable;

    @BeforeEach
    public void setup() {
        announcementDetail = new AnnouncementDetail();
        announcementDetail.setId(1L);
        announcementDetail.setTitle("Java");
        announcementDetail.setDescription("High level language");
        announcementDetail.setPrice(BigDecimal.TEN);

        user = new User();
        user.setUsername("ilyazovmehemmed@gmail.com");

        announcement = new Announcement();
        announcement.setId(1L);
        announcement.setAnnouncementDetail(announcementDetail);
        announcement.setUser(user);

        announcementResponse = new AnnouncementResponse();
        announcementResponse.setTitle("Java");
        announcementResponse.setDescription("High level language");
        announcementResponse.setPrice(BigDecimal.TEN);

        announcementRequest = new AnnouncementRequest();
        announcementRequest.setUsername("Mahammad");
        announcementRequest.setAnnouncementDetailsId(1L);
        announcementRequest.setViewCount(1000);

        searchCriteria = new SearchCriteria();
        searchCriteria.setMinViewCount(100);
        searchCriteria.setMaxViewCount(1000);

        pageCriteria.setPageNumber(0);
        pageCriteria.setPageSize(1);

        announcementPage = new PageImpl<>(List.of(announcement));

        announcementResponsePage = new PageImpl<>(List.of(announcementResponse));

        pageable = AnnouncementUtil.getDefaultPageable(pageCriteria);
    }

    @Test
    public void givenSearchCriteriaAndPageCriteria_whenGetAllAnnouncements_thenShouldReturnSuccess() {
        // arrange
        when(announcementRepository.findAll(new AnnouncementSpecification(searchCriteria), pageable)).thenReturn(announcementPage);
        when(announcementMapper.entityToResponse(announcement)).thenReturn(announcementResponse);
        when(PageableMapper.mapAnnouncementResponsePageToCustomPageResponse(announcementResponsePage)).thenReturn(pageAnnouncementResponse);

        // act
        var actual = announcementService.getAllAnnouncement(searchCriteria, pageCriteria);

        // assert
        assertThat(actual).isNotNull();
        assertThat(actual.getAnnouncementResponseList()).hasSize(1);
        assertThat(actual.getAnnouncementResponseList().get(0).getTitle()).isEqualTo("Java");
    }

    @Test
    public void givenPageCriteria_whenGetMostViewedAnnouncements_thenShouldReturnSuccess() {
        // arrange
        when(announcementRepository.findMostViewedAnnouncements(pageable)).thenReturn(announcementPage);
        when(announcementMapper.entityToResponse(announcement)).thenReturn(announcementResponse);
        when(PageableMapper.mapAnnouncementResponsePageToCustomPageResponse(announcementResponsePage)).thenReturn(pageAnnouncementResponse);

        // act
        var actual = announcementService.getMostViewedAnnouncements(pageCriteria);

        // assert
        assertThat(actual).isNotNull();
        assertThat(actual.getAnnouncementResponseList()).hasSize(1);
        assertThat(actual.getAnnouncementResponseList().get(0).getTitle()).isEqualTo("Java");
    }

    @Test
    public void givenValidAnnouncementRequest_whenCreateAnnouncement_thenShouldReturnSuccess() {
        // arrange
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(announcementDetailRepository.findById(anyLong())).thenReturn(Optional.of(announcementDetail));
        when(announcementMapper.requestToEntity(announcementRequest)).thenReturn(announcement);

        // act
        announcementService.createAnnouncement(announcementRequest);

        // assert
        verify(userRepository, times(1)).findUserByUsername("Mahammad");
        verify(announcementDetailRepository, times(1)).findById(1L);
        verify(announcementRepository, times(1)).save(announcement);
    }

    @Test
    public void givenAnnouncementIdAndValidAnnouncementRequest_whenUpdateAnnouncement_thenShouldReturnSuccess() {
        // arrange
        when(announcementRepository.findById(anyLong())).thenReturn(Optional.of(announcement));
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(announcementDetailRepository.findById(anyLong())).thenReturn(Optional.of(announcementDetail));
        when(announcementMapper.requestToEntity(announcementRequest)).thenReturn(announcement);

        // act
        var actual = announcementService.updateAnnouncement(1L, announcementRequest);

        // assert
        assertThat(actual).isEqualTo(announcementResponse);

    }

    @Test
    public void givenValidAnnouncementId_whenDeleteAnnouncement_thenShouldReturnSuccess() {
        // arrange
        when(announcementRepository.findById(anyLong())).thenReturn(Optional.of(announcement));
        when(announcementMapper.entityToResponse(announcement)).thenReturn(announcementResponse);

        // act
        var actual = announcementService.deleteAnnouncement(1L);

        // assert
        assertThat(actual).isEqualTo(announcementResponse);
    }

    @Test
    public void givenUsernameAndPageCriteria_whenGetAllOwnAnnouncements_thenShouldReturnSuccess() {
        // arrange
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(announcementRepository.findAllByUser(user, pageable)).thenReturn(announcementPage);
        when(announcementMapper.entityToResponse(announcement)).thenReturn(announcementResponse);
        when(PageableMapper.mapAnnouncementResponsePageToCustomPageResponse(announcementResponsePage)).thenReturn(pageAnnouncementResponse);

        // act
        var actual = announcementService.getAllOwnAnnouncement("Mahammad", pageCriteria);

        // assert
        assertThat(actual).isNotNull();
        assertThat(actual.getAnnouncementResponseList()).hasSize(1);
        assertThat(actual.getAnnouncementResponseList().get(0).getTitle()).isEqualTo("Java");

        verify(userRepository).findUserByUsername("Mahammad");
        verify(announcementRepository).findAllByUser(user, pageable);
    }

    @Test
    public void givenUsernameAndAnnouncementId_whenGetOwnAnnouncementWithId_thenShouldReturnSuccess() {
        // arrange
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(announcementRepository.findAnnouncementByIdAndUser(anyLong(), user)).thenReturn(Optional.of(announcement));
        when(announcementMapper.entityToResponse(announcement)).thenReturn(announcementResponse);

        // act
        var actual = announcementService.getOwnAnnouncementWithId("Mahammad", anyLong());

        // assert
        assertThat(actual).isEqualTo(announcementResponse);
    }

    @Test
    public void givenUsername_whenGetOwnMostViewedAnnouncement_thenShouldReturnSuccess() {
        // arrange
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));
        when(announcementRepository.findOwnMostViewedAnnouncement(user)).thenReturn(Optional.of(announcement));
        when(announcementMapper.entityToResponse(announcement)).thenReturn(announcementResponse);

        // act
        var actual = announcementService.getOwnMostViewedAnnouncement("Mahammad");

        // assert
        assertThat(actual).isEqualTo(announcementResponse);
    }
}