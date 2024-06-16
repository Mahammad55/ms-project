package az.ingress.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageCriteria {
    private Integer pageSize;

    private Integer pageNumber;

    private String[] sort;
}
