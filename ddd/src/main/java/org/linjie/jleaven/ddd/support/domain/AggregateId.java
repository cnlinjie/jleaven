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
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by linjie on 15-1-13.
 */
@SuppressWarnings("serial")
@Embeddable
public  abstract class AggregateId<PK> implements Serializable {

    @Column(name = "aggregateId")
    protected PK aggregateId;

    public AggregateId(PK aggregateId) {
        Validate.notNull(aggregateId);
        this.aggregateId = aggregateId;
    }

    public AggregateId(){

    }

    public abstract PK getId();

    protected abstract PK generate();


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
        AggregateId other = (AggregateId) obj;
        if (aggregateId == null) {
            if (other.aggregateId != null)
                return false;
        } else if (!aggregateId.equals(other.aggregateId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return aggregateId.toString();
    }

}
