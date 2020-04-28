package app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Null;
import java.sql.Timestamp;


@Component
public class ScheduledTask {
    @Autowired
    UserInfoDBRepository userInfoDBRepository;

    private int SESSION_TIME_ALIVE = 15 * 60 * 1000;
    private int OTP_TIME_ALIVE = 60 * 1000;

    @Scheduled(fixedRate = 2 * 60 * 1000)
    public void KillExpiredSessions() {

        for (UserInfoDB session : userInfoDBRepository.findAll()) {

            long sessStart = session.getSessionCreationTime();
            if (sessStart != 0) {
                long now = System.currentTimeMillis();

                if (now - sessStart > SESSION_TIME_ALIVE) {
                    session.setSessionToken(null);
                    session.setSessionCreationTime(0);

                    userInfoDBRepository.save(session);
                }
            }
        }
    }

    @Scheduled(fixedRate = 30 * 1000)
    public void KillExpiredOTP() {
        for (UserInfoDB otp : userInfoDBRepository.findAll()) {
            long otpCreationTime = otp.getOtpCreationTime();
            if (otpCreationTime != 0) {
                long now = System.currentTimeMillis();

                if (now - otpCreationTime > OTP_TIME_ALIVE){
                    otp.setOtpCreationTime(0);
                    otp.setOtp(null);

                    userInfoDBRepository.save(otp);
                }
            }
        }
    }
}
