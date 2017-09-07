package models;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by NGOCHIEU on 2017-07-24.
 */
@Entity
@Table(name = "Payment", schema = "mola", catalog = "")
public class PaymentEntity {
    private int id;
    private Integer sessionId;
    private Timestamp timePaid;
    private String paypalId;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Session_ID")
    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    @Basic
    @Column(name = "TimePaid")
    public Timestamp getTimePaid() {
        return timePaid;
    }

    public void setTimePaid(Timestamp timePaid) {
        this.timePaid = timePaid;
    }

    @Basic
    @Column(name = "Paypal_ID")
    public String getPaypalId() {
        return paypalId;
    }

    public void setPaypalId(String paypalId) {
        this.paypalId = paypalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentEntity that = (PaymentEntity) o;

        if (id != that.id) return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (timePaid != null ? !timePaid.equals(that.timePaid) : that.timePaid != null) return false;
        if (paypalId != null ? !paypalId.equals(that.paypalId) : that.paypalId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        result = 31 * result + (timePaid != null ? timePaid.hashCode() : 0);
        result = 31 * result + (paypalId != null ? paypalId.hashCode() : 0);
        return result;
    }
}
