package com.dockerapp.server.runtime.event;

import com.dockerapp.dao.entities.EventLogEntry;
import com.dockerapp.dao.repositories.EventLogEntryDao;
import com.dockerapp.server.api.EventSearchRequest;
import com.dockerapp.server.api.service.event.EventLogService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventLogServiceImpl implements EventLogService {

    @Autowired
    private EventLogEntryDao eventLogEntryDao;

    @Transactional(readOnly = true)
    @Override
    public Page<EventLogEntry> search(EventSearchRequest request) {
        return eventLogEntryDao.findAll(createSearchCriteria(request), request.getPageable());
    }

    private Specification<EventLogEntry> createSearchCriteria(final EventSearchRequest search) {
        return new Specification<EventLogEntry>() {
            @Override
            public Predicate toPredicate(Root<EventLogEntry> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!Strings.isNullOrEmpty(search.getUserId())) {
                    predicates.add(cb.or(
                            cb.equal(root.get("performedById"), search.getUserId()),
                            cb.equal(root.get("performedOnId"), search.getUserId()),
                            cb.equal(root.get("merchantUuid"), search.getUserId()),
                            cb.equal(root.get("giverMerchantUuid"), search.getUserId()),
                            cb.equal(root.get("takerMerchantUuid"), search.getUserId())));
                }
                if (!Strings.isNullOrEmpty(search.getStubId())) {
                    predicates.add(cb.equal(root.get("stubId"), search.getStubId()));
                }
                if (!Strings.isNullOrEmpty(search.getEmailAddress())) {
                    predicates.add(cb.or(
                            cb.equal(root.get("email"), search.getEmailAddress())));

                }
                if (Boolean.TRUE.equals(search.isExcludeCustomerEvents())) {
                    predicates.add(cb.isNull(root.get("customerId")));
                    predicates.add(cb.isNull(root.get("stubCustomerEventId")));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
