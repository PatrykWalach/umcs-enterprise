package com.umcs.enterprise.executor;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
//import static org.springframework.boot.SpringApplication.logger;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;

public class RequestAttributesAsyncTaskExecutor implements AsyncTaskExecutor {

	private final AsyncTaskExecutor asyncTaskExecutor;

	public RequestAttributesAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
		this.asyncTaskExecutor = asyncTaskExecutor;
	}

	@Override
	@Deprecated
	public void execute(Runnable task, long startTimeout) {
		asyncTaskExecutor.execute(wrap(task), startTimeout);
	}

	@Override
	@NonNull
	public Future<?> submit(@NonNull Runnable task) {
		return asyncTaskExecutor.submit(wrap(task));
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return asyncTaskExecutor.submit(wrap(task));
	}

	@Override
	public void execute(Runnable task) {
		asyncTaskExecutor.execute(wrap(task));
	}

	private Runnable wrap(Runnable delegate) {
		Callable<Exception> cleanup = wrap(() -> {
			delegate.run();
			return null;
		});

		return () -> {
			try {
				cleanup.call();
			} catch (Exception ignored) {}
		};
	}

	private <T> Callable<T> wrap(Callable<T> delegate) {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		SecurityContext context = SecurityContextHolder.getContext();
		return () -> {
			RequestContextHolder.setRequestAttributes(attrs);
			SecurityContextHolder.setContext(context);

			try {
				return delegate.call();
			} finally {
				RequestContextHolder.resetRequestAttributes();
				SecurityContextHolder.clearContext();
			}
		};
	}
}
