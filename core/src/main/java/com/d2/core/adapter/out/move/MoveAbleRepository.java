package com.d2.core.adapter.out.move;

import java.util.List;

import com.d2.core.model.domain.MoveOrder;

public interface MoveAbleRepository {
	<T extends MoveAble> List<MoveOrder> move(List<MoveOrder> moveOrders, Class<T> type);
}
