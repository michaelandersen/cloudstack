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
package org.apache.cloudstack.api.command.user.vpc;

import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.network.vpc.VpcPeeringConnectionRequest;
import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ResponseObject.ResponseView;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.VpcResponse;
import org.apache.cloudstack.context.CallContext;

import org.apache.log4j.Logger;

@APICommand(name = "createVPCPeeringConnection", description = "Creates a VPC Peering Connection request", responseObject = VpcResponse.class, responseView = ResponseView.Restricted, entityType = {VpcPeeringConnectionRequest.class},
        requestHasSensitiveInfo = false, responseHasSensitiveInfo = false)
public class CreateVPCPeeringConnectionCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(CreateVPCPeeringConnectionCmd.class.getName());
    private static final String s_name = "createvpcpeeringconnectionresponse";

    // ///////////////////////////////////////////////////
    // ////////////// API parameters /////////////////////
    // ///////////////////////////////////////////////////


    @Parameter(name = ApiConstants.VPC_ID, type = CommandType.UUID, required = true, description = "the vpc id of the requester. ")
    private Long vpcId;

    @Parameter(name = ApiConstants.PEER_VPC_ID, type = CommandType.UUID, required = true, description = "the vpc id of the peer. ")
    private Long peerVpcId;

    // ///////////////////////////////////////////////////
    // ///////////////// Accessors ///////////////////////
    // ///////////////////////////////////////////////////

    public Long getVpcId() { return vpcId; }

    public Long getPeerVpcId() { return peerVpcId; }


    @Override
    public String getCommandName() { return s_name; }

    @Override
    public long getEntityOwnerId() {
        return CallContext.current().getCallingAccount().getId();
    }

    @Override
    public
    void execute() throws InsufficientCapacityException, ConcurrentOperationException, ResourceAllocationException {
        Boolean result = _vpcService.createVpcPeeringConnectionRequest();
        if (result != true) {
            s_logger.debug("VPC Peering Connection Request Created!!!");
//            VpcPeeringConnectionResponse response = _responseGenerator.createVpcPeeringConnectionResponse(ResponseView.Restricted, result);
//            response.setResponseName(getCommandName());
//            setResponseObject(response);
        } else {
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to create vpc peering connection request");
        }
    }
}
