package ncm.task;

import ncm.monitor.NCMAllMonitor;
import ncm.monitor.NCMSongMonitor;
import ncm.monitor.NCMWeekMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wenxiangzhou214164 on 2017/8/3.
 */
@Configuration
@EnableScheduling
public class NCMTask {
    private NCMWeekMonitor ncmWeekMonitor;
    private NCMAllMonitor ncmAllMonitor;

    @Value(value = "${schedule.rate}")
    private int period;

    @Autowired
    public NCMTask(NCMWeekMonitor ncmWeekMonitor, NCMAllMonitor ncmAllMonitor, final NCMSongMonitor ncmSongMonitor) {
        this.ncmWeekMonitor = ncmWeekMonitor;
        this.ncmAllMonitor = ncmAllMonitor;
        Thread commentT = new Thread() {
            @Override
            public void run() {
                ncmSongMonitor.task(new Date());
            }
        };
        commentT.setDaemon(true);
        commentT.start();
    }

//    @Scheduled(cron = "${schedule.cronTab}")
    @Scheduled(fixedRate = 3 * 60 * 1000)
    public void task() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ncmWeekMonitor.task(new Date(new Date().getTime() / period * period));
            }
        });
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                ncmAllMonitor.task(new Date(new Date().getTime() / period * period));
            }
        });
    }
}
