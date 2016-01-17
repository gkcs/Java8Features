package work;

public interface StuckCallProcessorMXBean {
    void sendTerminationRequest(String status,
                                String callerJid,
                                String legId,
                                Long endTime,
                                Long talkStart,
                                Double billedTime);
}
