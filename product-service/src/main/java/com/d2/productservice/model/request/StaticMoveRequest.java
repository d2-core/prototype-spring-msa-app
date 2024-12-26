package com.d2.productservice.model.request;

import java.util.List;

import com.d2.core.model.domain.MoveOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StaticMoveRequest {
	private List<MoveOrder> moveOrders;
}
