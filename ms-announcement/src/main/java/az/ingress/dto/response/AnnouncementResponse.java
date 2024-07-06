package az.ingress.dto.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private Integer viewCount;

    private String title;

    private String description;

    private BigDecimal price;
}
