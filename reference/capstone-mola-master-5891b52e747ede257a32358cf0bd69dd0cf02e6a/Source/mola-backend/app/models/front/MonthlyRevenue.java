package models.front;

/**
 * Created by NGOCHIEU on 2017-08-15.
 */
public class MonthlyRevenue {
    private long revenue;
    private int month;

    public MonthlyRevenue() {
    }

    public MonthlyRevenue(long revenue, int month) {
        this.revenue = revenue;
        this.month = month;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
