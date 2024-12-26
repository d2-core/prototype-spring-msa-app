package com.d2.core.adapter.out.move;

public interface MoveAble {
	Long getId();

	Long getOrders();

	void setOrders(Long order);

	default void move(Long newOrder) {
		setOrders(newOrder);
	}
}
