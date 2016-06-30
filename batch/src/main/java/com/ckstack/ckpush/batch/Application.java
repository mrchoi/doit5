package com.ckstack.ckpush.batch;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Created by dhkim94 on 15. 9. 21..
 */
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    // 일 단위 가입자 카운트 통계용 데이터 추출
    private static final String BATCH_JOB_SUBSCRIBE_MEMBER_IN_DAY = "subscribe1";

    public static void main(String[] args) {
        if(args.length <= 0) {
            LOG.error("no batch job. It required job parameter.");
            return;
        }

        Application application = new Application();

        String jobName = StringUtils.trim(args[0]);
        MDC.put("logkey", jobName);

        // 일 단위 가입자 카운트 통계용 데이터 추출
        if(StringUtils.equals(jobName, Application.BATCH_JOB_SUBSCRIBE_MEMBER_IN_DAY))
            application.subscribeOfDay();

        // 정의 되지 않은 job은 에러 로그 출력
        else
            LOG.error("unknown batch job. job name [" + jobName + "]");
    }

    /**
     * 일 단위 가입자 카운트 통계용 데이터 추출
     */
    private void subscribeOfDay() {
        LOG.info("===== start count subscribe at one day =====");

        System.out.println("------ member of day 111");

        LOG.info("===== end count subscribe at one day =====");
    }
}
