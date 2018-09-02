package ir.server.boutiqueburger.models;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double grandTotal;

    private List<OrderDetail> orderDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
            return id;
    }

    public void setId(Long id) {
            this.id = id;
    }

    public double getGrandTotal() {
            return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
            this.grandTotal = grandTotal;
    }

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    @Size(min = 2, max = 6)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 30)
    public List<OrderDetail> getOrderDetails() {
            return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
            this.orderDetails = orderDetails;
    }

    public void addOrderDetail(OrderDetail orderDetail) {
            orderDetails.add(orderDetail);
            orderDetail.setOrder(this);
    }

    public void removeOrderDetail(OrderDetail orderDetail) {
            orderDetails.remove(orderDetail);
            orderDetail.setOrder(null);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
