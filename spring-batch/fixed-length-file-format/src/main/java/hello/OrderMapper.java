package hello;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements FieldSetMapper<Order> {
    public Order mapFieldSet(FieldSet fs) {

        if (fs == null) {
            return null;
        }

        Order order = new Order(
                fs.readString("isin"),
                fs.readString("quantity"),
                fs.readString("price"),
                fs.readString("customer")
        );

        return order;
    }
}