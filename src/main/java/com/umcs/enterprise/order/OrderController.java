package com.umcs.enterprise.order;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RepositoryRestController
@ExposesResourceFor(Order.class)
@RequiredArgsConstructor
//@RequestMapping("/orders/{id}")
public class OrderController {

    private final  @NonNull OrderRepository orderRepository;
    private final  @NonNull OrderService orderService;


    @PatchMapping(path="/orders/{id}/pay")
    public ResponseEntity<?> pay(@PathVariable("id") Order order){
        if(order == null || order.getStatus() != null){
            return ResponseEntity.notFound().build();
        }

        order = orderService.pay(order);

        EntityModel<Order> model = EntityModel.of(order);

        model.add(entityLinks.linkToItemResource(Order.class, order.getId()));

        return  new ResponseEntity<>(model, HttpStatus.OK);
    }

    @NonNull private  final
    EntityLinks entityLinks;


    @PatchMapping(path="/orders/{id}/ship")
    public ResponseEntity<?> ship(@PathVariable("id") Order order){
        if(order == null || !order.getStatus() .equals(OrderStatus.PAID)){
            return ResponseEntity.notFound().build();
        }

        order = orderService.ship(order);

        EntityModel<Order> model = EntityModel.of(order);

        model.add(entityLinks.linkToItemResource(Order.class, order.getId()));

        return  new ResponseEntity<>(model,  HttpStatus.OK);
    }
}
