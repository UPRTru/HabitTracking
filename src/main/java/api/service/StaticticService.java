package api.service;

public interface StaticticService {

    String generateStatistics(long user_id, int days);

    String reportProgress(long user_id);
}
