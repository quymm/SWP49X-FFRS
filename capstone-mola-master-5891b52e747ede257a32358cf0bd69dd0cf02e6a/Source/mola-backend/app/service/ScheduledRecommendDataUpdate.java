package service;

import akka.actor.ActorSystem;
import javafx.util.Pair;
import play.cache.CacheApi;
import play.db.jpa.JPAApi;
import scala.concurrent.duration.Duration;
import utils.Const;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by rocks on 6/21/2017.
 */
public class ScheduledRecommendDataUpdate {

    @Inject
    private CacheApi cache;

    @Inject
    private JPAApi jpaApi;

    @Inject
    public ScheduledRecommendDataUpdate(final ActorSystem system) {
        system.scheduler().schedule(
                Duration.create(1, TimeUnit.MINUTES),
                Duration.create(1, TimeUnit.DAYS),
                () -> {
                    generateNewUserRecommendCache();
                    generateDataSet();
                },
                system.dispatcher()
        );
    }

    private void generateNewUserRecommendCache() {
        jpaApi.withTransaction(() -> {
            EntityManager em = jpaApi.em();
            for (String language : Const.LANGUAGE_LIST
                    ) {
                List<Pair> newUserRecommends = RecommendationService.getNewUserRecommendations(language, em);
                Map<String, Double> toCacheResult = new HashMap<>();
                for (Pair pair : newUserRecommends
                        ) {
                    toCacheResult.put(pair.getKey().toString(), Double.valueOf(pair.getValue().toString()));
                }
                cache.set(Const.NEW_USER_RECOMMEND_CACHE_KEY + language.toUpperCase(), toCacheResult);
            }
        });
    }

    private void generateDataSet() {
        jpaApi.withTransaction(() -> {
            EntityManager em = jpaApi.em();

            Map userBasedDataSet = RecommendationService.initDataSet(em);
            cache.set(Const.USER_BASED_DATA_SET_CACHE_KEY, userBasedDataSet);

            Map itemBasedDataSet = RecommendationService.flipDataSet(userBasedDataSet);
            cache.set(Const.ITEM_BASED_DATA_SET_CACHE_KEY, itemBasedDataSet);

            Map similarItemsDictionary =
                    RecommendationService.similarItemsDictionary(itemBasedDataSet, Const.DICTIONARY_ENTRY_AMOUNT);
            cache.set(Const.SIMILAR_ITEMS_DICTIONARY_CACHE_KEY, similarItemsDictionary);
        });
    }
}
