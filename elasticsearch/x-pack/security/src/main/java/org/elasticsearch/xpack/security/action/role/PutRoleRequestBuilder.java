/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.security.action.role;

import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.support.WriteRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.xpack.security.authz.RoleDescriptor;
import org.elasticsearch.xpack.security.authz.permission.FieldPermissions;

import java.util.Map;

/**
 * Builder for requests to add a role to the administrative index
 */
public class PutRoleRequestBuilder extends ActionRequestBuilder<PutRoleRequest, PutRoleResponse, PutRoleRequestBuilder>
        implements WriteRequestBuilder<PutRoleRequestBuilder> {

    public PutRoleRequestBuilder(ElasticsearchClient client) {
        this(client, PutRoleAction.INSTANCE);
    }

    public PutRoleRequestBuilder(ElasticsearchClient client, PutRoleAction action) {
        super(client, action, new PutRoleRequest());
    }

    public PutRoleRequestBuilder source(String name, BytesReference source) throws Exception {
        // we pass false as last parameter because we want to reject the request if field permissions
        // are given in 2.x syntax
        RoleDescriptor descriptor = RoleDescriptor.parse(name, source, false);
        assert name.equals(descriptor.getName());
        request.name(name);
        request.cluster(descriptor.getClusterPrivileges());
        request.addIndex(descriptor.getIndicesPrivileges());
        request.runAs(descriptor.getRunAs());
        request.metadata(descriptor.getMetadata());
        return this;
    }

    public PutRoleRequestBuilder name(String name) {
        request.name(name);
        return this;
    }

    public PutRoleRequestBuilder cluster(String... cluster) {
        request.cluster(cluster);
        return this;
    }

    public PutRoleRequestBuilder runAs(String... runAsUsers) {
        request.runAs(runAsUsers);
        return this;
    }

    public PutRoleRequestBuilder addIndices(String[] indices, String[] privileges,
                                            FieldPermissions fieldPermissions, @Nullable BytesReference query) {
        request.addIndex(indices, privileges, fieldPermissions, query);
        return this;
    }

    public PutRoleRequestBuilder metadata(Map<String, Object> metadata) {
        request.metadata(metadata);
        return this;
    }
}
