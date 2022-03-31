package raf.si.bolnica.user.query;

import org.springframework.data.jpa.domain.Specification;
import raf.si.bolnica.user.models.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {

    private SearchCriteria criteria;

    public UserSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (root.get(criteria.getKey()).getJavaType() == String.class) {
            return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
        }
        else {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        }
    }
}
