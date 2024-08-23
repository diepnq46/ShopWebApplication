package com.example.shopapp.mapper;

import com.example.shopapp.dto.OrderDetailDTO;
import com.example.shopapp.entity.OrderDetail;
import com.example.shopapp.response.OrderDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDetailMapper {
    OrderDetailMapper MAPPER = Mappers.getMapper(OrderDetailMapper.class);

    OrderDetail mapToOrderDetail(OrderDetailDTO orderDetailDTO);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail);
}
