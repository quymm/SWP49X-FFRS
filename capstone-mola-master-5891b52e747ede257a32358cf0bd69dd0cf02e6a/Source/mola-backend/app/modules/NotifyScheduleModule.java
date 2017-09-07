package modules;

import com.google.inject.AbstractModule;
import service.NotificationScheduleService;

/**
 * Created by stark on 18/07/2017.
 */
public class NotifyScheduleModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(NotificationScheduleService.class).asEagerSingleton();
    }
}
