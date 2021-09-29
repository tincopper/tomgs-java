/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tomgs.registry.jraft.rhea;

import com.tomgs.registry.jraft.rhea.cmd.store.*;
import com.tomgs.registry.jraft.rhea.metadata.RegionEpoch;

/**
 * Request processing service on the KV server side.
 * <p>
 * A {@link StoreEngine} contains many {@link com.alipay.sofa.jraft.rhea.RegionKVService}s,
 * each {@link com.alipay.sofa.jraft.rhea.RegionKVService} corresponds to a region, and it
 * only processes request keys within its own region.
 *
 * @author jiachun.fjc
 */
public interface RegionKVService {

    long getRegionId();

    RegionEpoch getRegionEpoch();

    /**
     * {@link BaseRequest#PUT}
     */
    void handlePutRequest(final PutRequest request, final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#BATCH_PUT}
     */
    void handleBatchPutRequest(final BatchPutRequest request,
                               final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#PUT_IF_ABSENT}
     */
    void handlePutIfAbsentRequest(final PutIfAbsentRequest request,
                                  final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#GET_PUT}
     */
    void handleGetAndPutRequest(final GetAndPutRequest request,
                                final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#COMPARE_PUT}
     */
    void handleCompareAndPutRequest(final CompareAndPutRequest request,
                                    final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#DELETE}
     */
    void handleDeleteRequest(final DeleteRequest request,
                             final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#DELETE_RANGE}
     */
    void handleDeleteRangeRequest(final DeleteRangeRequest request,
                                  final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#BATCH_DELETE}
     */
    void handleBatchDeleteRequest(final BatchDeleteRequest request,
                                  final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#MERGE}
     */
    void handleMergeRequest(final MergeRequest request,
                            final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#GET}
     */
    void handleGetRequest(final GetRequest request, final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#MULTI_GET}
     */
    void handleMultiGetRequest(final MultiGetRequest request,
                               final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#CONTAINS_KEY}
     */
    void handleContainsKeyRequest(final ContainsKeyRequest request,
                                  final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#SCAN}
     */
    void handleScanRequest(final ScanRequest request, final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#GET_SEQUENCE}
     */
    void handleGetSequence(final GetSequenceRequest request,
                           final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#RESET_SEQUENCE}
     */
    void handleResetSequence(final ResetSequenceRequest request,
                             final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#KEY_LOCK}
     */
    void handleKeyLockRequest(final KeyLockRequest request,
                              final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#KEY_UNLOCK}
     */
    void handleKeyUnlockRequest(final KeyUnlockRequest request,
                                final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#NODE_EXECUTE}
     */
    void handleNodeExecuteRequest(final NodeExecuteRequest request,
                                  final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#RANGE_SPLIT}
     */
    void handleRangeSplitRequest(final RangeSplitRequest request,
                                 final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);

    /**
     * {@link BaseRequest#COMPARE_PUT_ALL}
     */
    void handleCompareAndPutAll(final CASAllRequest request,
                                final RequestProcessClosure<BaseRequest, BaseResponse<?>> closure);
}
