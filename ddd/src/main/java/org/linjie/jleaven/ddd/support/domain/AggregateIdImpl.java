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
package org.linjie.jleaven.ddd.support.domain;

import org.apache.commons.lang3.Validate;

import javax.persistence.Column;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class AggregateIdImpl extends AggregateId<String> {


	@Column(name = "aggregateId")
	private String aggregateId;

	private static final String dtLong = "yyyyMMddHHmmss";
	private static final DateFormat df = new SimpleDateFormat(dtLong);

	public AggregateIdImpl(String aggregateId) {
		Validate.notNull(aggregateId);
		this.aggregateId = aggregateId;
	}

	protected AggregateIdImpl() {
	}
	

	public String getId() {
		return aggregateId;
	}

	@Override
	protected String generate() {
		return UUID.randomUUID().toString();
	}

	@Override
	public int hashCode() {
		return aggregateId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AggregateIdImpl other = (AggregateIdImpl) obj;
		if (aggregateId == null) {
			if (other.aggregateId != null)
				return false;
		} else if (!aggregateId.equals(other.aggregateId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return aggregateId;
	}
}
