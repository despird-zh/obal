package com.obal.test.wd;

import com.obal.core.hbase.ext.HashPrefixDistributor;

public class RowKeyDistributorByHashPrefix_IdentityHashTest extends RowKeyDistributorTestBase {
  public RowKeyDistributorByHashPrefix_IdentityHashTest() {
    super(new HashPrefixDistributor(new IdentityHash()));
  }

  public static class IdentityHash implements HashPrefixDistributor.Hasher {
    private static final byte[] EMPTY_PREFIX = new byte[0];

    @Override
    public byte[] getHashPrefix(byte[] originalKey) {
      return EMPTY_PREFIX;
    }

    @Override
    public byte[][] getAllPossiblePrefixes() {
      return new byte[][] {EMPTY_PREFIX};
    }

    @Override
    public int getPrefixLength(byte[] adjustedKey) {
      // the original key wasn't changed
      return 0;
    }

    @Override
    public String getParamsToStore() {
      return null;
    }

    @Override
    public void init(String storedParams) {
      // DO NOTHING
    }
  }
}
