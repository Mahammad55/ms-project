package az.ingress.specification;


import az.ingress.dto.request.SearchCriteria;
import az.ingress.entity.Announcement;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class AnnouncementSpecification implements Specification<Announcement> {

    private SearchCriteria searchCriteria;

    @Override
    public Predicate toPredicate(Root<Announcement> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        Integer minViewCount = searchCriteria.getMinViewCount();
        Integer maxViewCount = searchCriteria.getMaxViewCount();

        if (minViewCount != null)
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("viewCount"), minViewCount));

        if (maxViewCount != null)
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("viewCount"), maxViewCount));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
