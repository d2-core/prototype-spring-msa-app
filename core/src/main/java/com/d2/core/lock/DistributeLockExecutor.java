package com.d2.core.lock;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.d2.core.error.ErrorCodeImpl;
import com.d2.core.exception.ApiExceptionImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DistributeLockExecutor {

	private final RedissonClient redissonClient;

	public <T> T execute(String lockName, long waitMilliSecond, long leaseMilliSecond, Callable<T> logic) {
		RLock lock = redissonClient.getLock(lockName);
		try {
			boolean isLocked = lock.tryLock(waitMilliSecond, leaseMilliSecond, TimeUnit.MILLISECONDS);
			if (!isLocked) {
				throw new IllegalStateException("[" + lockName + "] lock 획득 실패");
			}
			return logic.call();
		} catch (Exception ex) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	public void execute(String lockName, long waitMilliSecond, long leaseMilliSecond, Runnable logic) {
		RLock lock = redissonClient.getLock(lockName);
		try {
			boolean isLocked = lock.tryLock(waitMilliSecond, leaseMilliSecond, TimeUnit.MILLISECONDS);
			if (!isLocked) {
				throw new IllegalStateException("[" + lockName + "] lock 획득 실패");
			}
			logic.run();
		} catch (Exception ex) {
			throw new ApiExceptionImpl(ErrorCodeImpl.INTERNAL_SERVER_ERROR, ex);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
