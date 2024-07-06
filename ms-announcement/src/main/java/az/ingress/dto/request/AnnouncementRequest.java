package az.ingress.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequest {
    private String username;

    private Integer viewCount;

    private Long announcementDetailsId;
}
