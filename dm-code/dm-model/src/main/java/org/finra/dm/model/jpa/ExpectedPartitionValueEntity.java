/*
* Copyright 2015 herd contributors
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
package org.finra.dm.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Expected partition values entity.
 */
@XmlRootElement
@XmlType
@Table(name = ExpectedPartitionValueEntity.TABLE_NAME)
@Entity
public class ExpectedPartitionValueEntity extends AuditableEntity
{
    /**
     * The table name.
     */
    public static final String TABLE_NAME = "xpctd_prtn_value";

    @Id
    @Column(name = TABLE_NAME + "_id")
    @GeneratedValue(generator = TABLE_NAME + "_seq")
    @SequenceGenerator(name = TABLE_NAME + "_seq", sequenceName = TABLE_NAME + "_seq")
    private Integer id;

    /**
     * The partitionKeyGroup column.
     */
    @ManyToOne
    @JoinColumn(name = "prtn_key_group_tx", referencedColumnName = "prtn_key_group_tx", nullable = false)
    private PartitionKeyGroupEntity partitionKeyGroup;

    /**
     * The partitionValue column.
     */
    @Column(name = "prtn_value_tx", nullable = false)
    private String partitionValue;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public PartitionKeyGroupEntity getPartitionKeyGroup()
    {
        return partitionKeyGroup;
    }

    public void setPartitionKeyGroup(PartitionKeyGroupEntity partitionKeyGroup)
    {
        this.partitionKeyGroup = partitionKeyGroup;
    }

    public String getPartitionValue()
    {
        return partitionValue;
    }

    public void setPartitionValue(String partitionValue)
    {
        this.partitionValue = partitionValue;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        ExpectedPartitionValueEntity that = (ExpectedPartitionValueEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null)
        {
            return false;
        }
        if (partitionKeyGroup != null ? !partitionKeyGroup.equals(that.partitionKeyGroup) : that.partitionKeyGroup != null)
        {
            return false;
        }
        if (partitionValue != null ? !partitionValue.equals(that.partitionValue) : that.partitionValue != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (partitionKeyGroup != null ? partitionKeyGroup.hashCode() : 0);
        result = 31 * result + (partitionValue != null ? partitionValue.hashCode() : 0);
        return result;
    }
}
