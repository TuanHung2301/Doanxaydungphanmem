package service;

import dao.StatsDAO;
import java.util.List;

public class StatsService {

    private StatsDAO statsDAO = new StatsDAO();

    public List<Object[]> getEmployeePerformance() {
        return statsDAO.getEmployeePerformance();
    }

    public List<Object[]> getStatsByTime() {
        return statsDAO.getStatsByTime();
    }

    public List<Object[]> getStatsByType() {
        return statsDAO.getStatsByType();
    }
}
