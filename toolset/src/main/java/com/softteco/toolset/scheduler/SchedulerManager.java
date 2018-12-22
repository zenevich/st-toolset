package com.softteco.toolset.scheduler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author serge
 */
@Singleton
public final class SchedulerManager {

    private static final int TWO_SECS = 2000;
    private static final int TEN_SECS = 10;
    private static final Logger LOGGER = LogManager.getLogger(SchedulerManager.class.getName());
    private Scheduler scheduler;

    @Inject
    public SchedulerManager(final Injector injector) {
        try {
            final StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(injector.getInstance(GuiceJobFactory.class));
//            scheduler.clear();
            scheduler.startDelayed(TEN_SECS);
        } catch (SchedulerException e) {
            LOGGER.error("Problem with scheduler", e);
        } catch (Exception e) {
            LOGGER.error("Problem with scheduler", e);
        }
    }

    public void addJob(final JobDetail jd) {
        final Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jd.getKey().getName()).
                startAt(new Date(System.currentTimeMillis() + TWO_SECS)).build();
        addJob(jd, trigger);
    }

    public void addJob(final JobDetail jd, final Trigger trigger) {
        try {
            LOGGER.error("addJob...");
            final Date date = scheduler.scheduleJob(jd, trigger);
            LOGGER.error("addJob: " + date);
        } catch (SchedulerException e) {
            LOGGER.error("Problem with scheduler", e);
        } catch (Exception e) {
            LOGGER.error("Problem with scheduler", e);
        }
    }
}
