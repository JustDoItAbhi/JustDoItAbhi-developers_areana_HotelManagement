package com.locationservice.locationservice.locations.util.query;


import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Component;

@Component
public class HibernateStatisticsService {

    private final Statistics statistics;

    public HibernateStatisticsService(EntityManagerFactory entityManagerFactory) {

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        this.statistics = sessionFactory.getStatistics();
        this.statistics.setStatisticsEnabled(true);
    }

    public void clear() {
        statistics.clear();
    }

    public long getQueryCount() {
        return statistics.getPrepareStatementCount();
    }

    public long getEntityLoadCount() {
        return statistics.getEntityLoadCount();
    }

    public long getEntityFetchCount() {
        return statistics.getEntityFetchCount();
    }

    public long getCollectionFetchCount() {
        return statistics.getCollectionFetchCount();
    }

    public long getSecondLevelCacheHitCount() {
        return statistics.getSecondLevelCacheHitCount();
    }
}