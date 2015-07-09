/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 */
package org.linjie.jleaven.ddd.support.domain;

import org.linjie.jleaven.ddd.exceptions.DomainOperationException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Slawek
 * 
 */
@Component
@Scope("prototype")//created in domain factories, not in spring container, therefore we don't want eager creation
@MappedSuperclass
public abstract class BaseAggregateRoot<T extends AggregateId>  {
	public static enum AggregateStatus {
		ACTIVE, ARCHIVE
	}

	@EmbeddedId
	protected T aggregateId;

	@Version
	protected Long version;

	@Enumerated(EnumType.STRING)
	@Column(name = "del_status")
	private AggregateStatus aggregateStatus = AggregateStatus.ACTIVE;

	@Column(name = "create_time")
	protected Date createTime = new Date(System.currentTimeMillis());

	@Transient
	@Inject
	private DomainEventPublisher eventPublisher;

	@Transient
	private List<Serializable> events;

	protected void storageEvent(Serializable event) {
		if (events == null) {
			events = new ArrayList<Serializable>();
		}
		events.add(event);
	}

	protected void publishEvent(Serializable event) {
		if (eventPublisher == null) {
			storageEvent(event);
		} else {
			eventPublisher.publish(event);
		}
	}

	public void markAsRemoved() {
		aggregateStatus = AggregateStatus.ARCHIVE;
	}

	public void markAsRemoved(Serializable removeEvent) {
		aggregateStatus = AggregateStatus.ARCHIVE;
		storageEvent(removeEvent);
	}


	public T getAggregateId() {
		return aggregateId;
	}

	public boolean isRemoved() {
		return aggregateStatus == AggregateStatus.ARCHIVE;
	}
	
	protected void domainError(String message) {
		throw new DomainOperationException(aggregateId, message);
	}

	protected void assertNull(Object o , String message) {
		if (o != null) {
			domainError(message);
		}
	}

	protected void assertNotNull(Object o , String message) {
		if (o == null) {
			domainError(message);
		}
	}


	protected void assertEquals(Object o , Object o1,String message) {
		if (!o.equals(o1)) {
			domainError(message);
		}
	}


	protected void assertNotEquals(Object o , Object o1,String message) {
		if (o.equals(o1)) {
			domainError(message);
		}
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof BaseAggregateRoot) {
			BaseAggregateRoot other = (BaseAggregateRoot) obj;
			if (other.aggregateId == null)
				return false;
			return other.aggregateId.equals(aggregateId);
		}
		
		return false;
	}

	public synchronized void publishHasStoredEvents() {
		if (events != null) {
			for (Serializable event : events) {
				eventPublisher.publish(event);
			}
			events.clear();
		}
	}

	@Override
	public int hashCode() {	
		return aggregateId.hashCode();
	}
}
