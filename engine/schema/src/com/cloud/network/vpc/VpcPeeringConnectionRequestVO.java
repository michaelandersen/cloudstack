// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.cloud.network.vpc;

import com.cloud.utils.db.GenericDao;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "vpc_peering_request")
public class VpcPeeringConnectionRequestVO implements VpcPeeringConnectionRequest {

    @Id
    @Column(name = "id")
    long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "zone_id")
    long zoneId;

    @Column(name = "domain_id")
    Long domainId = null;

    @Column(name = "account_id")
    Long accountId;

    @Column(name = "vpc_id")
    Long vpcId = null;

    @Column(name = "peer_vpc_id")
    Long peerVpcId = null;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    Status status;

    @Column(name = "expiration")
    Date expiration;

    @Column(name = GenericDao.REMOVED_COLUMN)
    Date removed;

    @Column(name = GenericDao.CREATED_COLUMN)
    Date created;


    public VpcPeeringConnectionRequestVO() {
    }

    protected VpcPeeringConnectionRequestVO(Long vpcId, Long peerVpcId) {
        this.uuid = UUID.randomUUID().toString();
        this.vpcId = vpcId;
        this.peerVpcId = peerVpcId;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getUuid() { return uuid; }

    @Override
    public long getZoneId() {
        return zoneId;
    }

    @Override
    public long getDomainId() {
        return domainId;
    }

    @Override
    public long getAccountId() {
        return accountId;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getPeeringConnectionId() { return uuid; }

    @Override
    public Long getVpcId() { return vpcId; }

    @Override
    public Long getPeerVpcId() { return peerVpcId; }

    public void setStatus(final Status status) {
        this.status = status;
    }

    @Override
    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(final Date expiration) {
        this.expiration = expiration;
    }

    public Date getRemoved() {
        return removed;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder("[VPC [");
        return buf.append(vpcId).append("-").append(peerVpcId).append("]").toString();
    }

}
