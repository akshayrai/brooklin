/**
 *  Copyright 2026 LinkedIn Corporation. All rights reserved.
 *  Licensed under the BSD 2-Clause License. See the LICENSE file in the project root for license information.
 *  See the NOTICE file in the project root for additional information regarding copyright ownership.
 */
package com.linkedin.datastream.server.diagnostics;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.linkedin.datastream.common.zk.ZkClient;

import static org.mockito.Mockito.mock;


/**
 * Tests for {@link ServerComponentHealthAggregator}
 */
public class TestServerComponentHealthAggregator {
  private static final String CLUSTER = "testCluster";

  @Test
  public void testDefaultPeerTimeout() {
    ServerComponentHealthAggregator aggregator =
        new ServerComponentHealthAggregator(mock(ZkClient.class), CLUSTER, 2428, "diag");
    Assert.assertEquals(aggregator.getPeerTimeoutMs(), ServerComponentHealthAggregator.DEFAULT_PEER_TIMEOUT_MS);
    Assert.assertEquals(ServerComponentHealthAggregator.DEFAULT_PEER_TIMEOUT_MS, 10000);
  }

  @Test
  public void testCustomPeerTimeout() {
    ServerComponentHealthAggregator aggregator =
        new ServerComponentHealthAggregator(mock(ZkClient.class), CLUSTER, 2428, "diag", 2000);
    Assert.assertEquals(aggregator.getPeerTimeoutMs(), 2000);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNonPositivePeerTimeoutIsRejected() {
    new ServerComponentHealthAggregator(mock(ZkClient.class), CLUSTER, 2428, "diag", 0);
  }
}
