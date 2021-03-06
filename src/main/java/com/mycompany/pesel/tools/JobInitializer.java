package com.mycompany.pesel.tools;

import com.mycompany.pesel.jobs.BreaksJob;
import com.mycompany.pesel.jobs.ResultsJob;
import com.mycompany.pesel.main.Person;
import org.eclipse.collections.impl.multimap.list.FastListMultimap;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class JobInitializer {

    public static void initializeJobs(Scheduler scheduler, FastListMultimap<String, Person> people) {
        try {
            scheduler.getContext().put("people", people);
            scheduler.start();

            JobDetail resultsJob = newJob(ResultsJob.class)
                    .withIdentity("results", "group1")
                    .build();

            CronTrigger resultsTrigger = newTrigger()
                    .withIdentity("resultsTrigger", "group1")
                    .startNow()
                    .withSchedule(cronSchedule("0/30 * * * * ? *"))
                    .build();

            JobDetail breaksJob = newJob(BreaksJob.class)
                    .withIdentity("breaks", "group1")
                    .build();

            CronTrigger breaksTrigger = newTrigger()
                    .withIdentity("breaksTrigger", "group1")
                    .startNow()
                    .withSchedule(cronSchedule("0 * * * * ?"))
                    .build();

            scheduler.scheduleJob(breaksJob, breaksTrigger);
            scheduler.scheduleJob(resultsJob, resultsTrigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }
}
