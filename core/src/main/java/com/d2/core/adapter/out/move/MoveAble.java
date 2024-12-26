package com.d2.core.adapter.out.move;

public interface MoveAble {
	Long getId();

	Long getOrder();

	void setOrder(Long order);

	default void move(Long newOrder) {
		setOrder(newOrder);
	}
}
