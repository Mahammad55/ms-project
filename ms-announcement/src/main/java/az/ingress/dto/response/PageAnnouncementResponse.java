package az.ingress.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageAnnouncementResponse {
    private List<AnnouncementResponse> announcementResponseList;

    private Integer pageNumber;

    private Integer pageSize;

    private Integer lastPageNumber;

    private Long totalElements;

    private boolean hasNextPage;
}


