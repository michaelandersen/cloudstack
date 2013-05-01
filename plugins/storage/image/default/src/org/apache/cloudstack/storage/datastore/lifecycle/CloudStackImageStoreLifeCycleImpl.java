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
package org.apache.cloudstack.storage.datastore.lifecycle;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.cloudstack.engine.subsystem.api.storage.ClusterScope;
import org.apache.cloudstack.engine.subsystem.api.storage.DataStore;
import org.apache.cloudstack.engine.subsystem.api.storage.HostScope;
import org.apache.cloudstack.engine.subsystem.api.storage.ZoneScope;
import org.apache.cloudstack.storage.datastore.db.ImageStoreDao;
import org.apache.cloudstack.storage.datastore.db.ImageStoreVO;
import org.apache.cloudstack.storage.image.datastore.ImageStoreHelper;
import org.apache.cloudstack.storage.image.datastore.ImageStoreProviderManager;
import org.apache.cloudstack.storage.image.store.lifecycle.ImageStoreLifeCycle;
import org.apache.log4j.Logger;

import com.cloud.agent.api.StoragePoolInfo;
import com.cloud.exception.DiscoveryException;
import com.cloud.exception.InvalidParameterValueException;
import com.cloud.host.Host;
import com.cloud.host.HostVO;
import com.cloud.hypervisor.kvm.discoverer.KvmDummyResourceBase;
import com.cloud.resource.Discoverer;
import com.cloud.resource.ResourceListener;
import com.cloud.resource.ResourceManager;
import com.cloud.resource.ServerResource;
import com.cloud.storage.DataStoreRole;
import com.cloud.storage.ScopeType;
import com.cloud.utils.UriUtils;

public class CloudStackImageStoreLifeCycleImpl implements ImageStoreLifeCycle {

    private static final Logger s_logger = Logger
            .getLogger(CloudStackImageStoreLifeCycleImpl.class);
    @Inject
    protected ResourceManager _resourceMgr;
    @Inject
	protected ImageStoreDao imageStoreDao;
	@Inject
	ImageStoreHelper imageStoreHelper;
	@Inject
	ImageStoreProviderManager imageStoreMgr;

    protected List<? extends Discoverer> _discoverers;
    public List<? extends Discoverer> getDiscoverers() {
        return _discoverers;
    }
    public void setDiscoverers(List<? extends Discoverer> _discoverers) {
        this._discoverers = _discoverers;
    }

	public CloudStackImageStoreLifeCycleImpl() {
	}


    @Override
    public DataStore initialize(Map<String, Object> dsInfos) {

        Long dcId = (Long) dsInfos.get("zoneId");
        String url = (String) dsInfos.get("url");
        String providerName = (String)dsInfos.get("providerName");
        DataStoreRole role =(DataStoreRole) dsInfos.get("role");
        Map<String, String> details = (Map<String, String>)dsInfos.get("details");

        s_logger.info("Trying to add a new host at " + url + " in data center " + dcId);

        URI uri = null;
        try {
            uri = new URI(UriUtils.encodeURIComponent(url));
            if (uri.getScheme() == null) {
                throw new InvalidParameterValueException("uri.scheme is null "
                        + url + ", add nfs:// as a prefix");
            } else if (uri.getScheme().equalsIgnoreCase("nfs")) {
                if (uri.getHost() == null || uri.getHost().equalsIgnoreCase("")
                        || uri.getPath() == null
                        || uri.getPath().equalsIgnoreCase("")) {
                    throw new InvalidParameterValueException(
                            "Your host and/or path is wrong.  Make sure it's of the format nfs://hostname/path");
                }
            }
        } catch (URISyntaxException e) {
            throw new InvalidParameterValueException(url
                    + " is not a valid uri");
        }

        if ( dcId == null ){
            throw new InvalidParameterValueException("DataCenter id is null, and cloudstack default image storehas to be associated with a data center");
        }


        Map<String, Object> imageStoreParameters = new HashMap<String, Object>();
        imageStoreParameters.put("name", url);
        imageStoreParameters.put("zoneId", dcId);
        imageStoreParameters.put("url", url);
        imageStoreParameters.put("protocol", uri.getScheme().toLowerCase());
        imageStoreParameters.put("scope", ScopeType.ZONE);  // default cloudstack provider only supports zone-wide image store
        imageStoreParameters.put("providerName", providerName);
        imageStoreParameters.put("role", role);


        ImageStoreVO ids = imageStoreHelper.createImageStore(imageStoreParameters, details);
        return imageStoreMgr.getImageStore(ids.getId());
    }


    @Override
    public boolean attachCluster(DataStore store, ClusterScope scope) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean attachHost(DataStore store, HostScope scope,
            StoragePoolInfo existingInfo) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean attachZone(DataStore dataStore, ZoneScope scope) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean dettach() {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean unmanaged() {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean maintain(DataStore store) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean cancelMaintain(DataStore store) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean deleteDataStore(DataStore store) {
        // TODO Auto-generated method stub
        return false;
    }
}
