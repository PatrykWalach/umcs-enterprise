package com.umcs.enterprise.executor;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
//import static org.springframework.boot.SpringApplication.logger;

import lombok.NonNull;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class RequestAttributesAsyncTaskExecutor implements AsyncTaskExecutor {

	private final AsyncTaskExecutor asyncTaskExecutor;

	public RequestAttributesAsyncTaskExecutor(AsyncTaskExecutor asyncTaskExecutor) {
		this.asyncTaskExecutor = asyncTaskExecutor;
	}

	@Override
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
		Optional<RequestAttributes> optRequestAttributes = getRequestAttributes();
		SecurityContext context = SecurityContextHolder.getContext();

		return () -> {
			SecurityContextHolder.setContext(context);
			setRequestAttributes(optRequestAttributes);
			try {
				delegate.run();
			} finally {
				resetRequestAttributes(optRequestAttributes);
				SecurityContextHolder.clearContext();
			}
		};
	}

	private <T> Callable<T> wrap(Callable<T> delegate) {
		Optional<RequestAttributes> optRequestAttributes = getRequestAttributes();
		SecurityContext context = SecurityContextHolder.getContext();
		return () -> {
			SecurityContextHolder.setContext(context);
			setRequestAttributes(optRequestAttributes);
			try {
				return delegate.call();
			} finally {
				resetRequestAttributes(optRequestAttributes);
				SecurityContextHolder.clearContext();
			}
		};
	}

	private Optional<RequestAttributes> getRequestAttributes() {
		try {
			return Optional.ofNullable(RequestContextHolder.getRequestAttributes());
		} catch (Exception e) {
			//            logger.warn(
			//                    "Unable to fetch the RequestAttributes based on the RequestContextHolder.getRequestAttributes method.",
			//                    e);
			return Optional.empty();
		}
	}

	private void setRequestAttributes(final Optional<RequestAttributes> optionalRequestAttributes) {
		try {
			optionalRequestAttributes.ifPresent(RequestContextHolder::setRequestAttributes);
		} catch (Exception e) {
			//            logger.warn("Unable to set the RequestAttributes.", e);
		}
	}

	private void resetRequestAttributes(final Optional<RequestAttributes> optionalRequestAttributes) {
		try {
			optionalRequestAttributes.ifPresent(requestAttributes ->
				RequestContextHolder.resetRequestAttributes()
			);
		} catch (Exception e) {
			//            logger.warn("Unable to reset the RequestAttributes.", e);
		}
	}
}
