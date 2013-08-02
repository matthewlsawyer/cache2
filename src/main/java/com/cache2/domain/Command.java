package com.cache2.domain;

public interface Command {
	void execute(Object... args);
}
