package com.umcs.enterprise.order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderProcessor implements RepresentationModelProcessor<EntityModel<Order>> {

	@Override
	public EntityModel<Order> process(EntityModel<Order> model) {
		Order order = model.getContent();

		return model
			.addIf(
				order.getStatus() == null,
				() -> linkTo(methodOn(OrderController.class).pay(order)).withRel("pay")
			)
			.addIf(
				order.getStatus().equals(OrderStatus.PAID),
				() -> linkTo(methodOn(OrderController.class).pay(order)).withRel("pay")
			);
	}
}
