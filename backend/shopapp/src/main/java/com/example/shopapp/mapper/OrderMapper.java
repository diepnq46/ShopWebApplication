package com.example.shopapp.mapper;

import com.example.shopapp.dto.OrderDTO;
import com.example.shopapp.entity.Order;
import com.example.shopapp.response.OrderResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    Order mapToOrder(OrderDTO orderDTO);

    @Mapping(source = "user.id", target = "userId")
    OrderResponse mapToOrderResponse(Order order);
}
