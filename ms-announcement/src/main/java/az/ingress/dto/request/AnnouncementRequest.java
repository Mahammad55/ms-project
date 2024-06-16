package az.ingress.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequest {
    private String username;

    private Integer viewCount;

    private Long announcementDetailsId;
}
