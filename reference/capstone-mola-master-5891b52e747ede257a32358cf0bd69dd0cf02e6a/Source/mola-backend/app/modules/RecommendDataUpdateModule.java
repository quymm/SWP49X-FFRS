package modules;

import com.google.inject.AbstractModule;
import service.ScheduledRecommendDataUpdate;

/**
 * Created by rocks on 6/21/2017.
 */
public class RecommendDataUpdateModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScheduledRecommendDataUpdate.class).asEagerSingleton();
    }
}
