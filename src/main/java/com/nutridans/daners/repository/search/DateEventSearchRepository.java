package com.nutridans.daners.repository.search;

import com.nutridans.daners.domain.DateEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the DateEvent entity.
 */
public interface DateEventSearchRepository extends ElasticsearchRepository<DateEvent, Long> {
}
