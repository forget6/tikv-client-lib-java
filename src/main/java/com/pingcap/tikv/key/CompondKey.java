/*
 * Copyright 2017 PingCAP, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pingcap.tikv.key;


import com.google.common.base.Joiner;
import com.pingcap.tikv.codec.CodecDataOutput;
import java.util.ArrayList;
import java.util.List;

public class CompondKey extends Key {

  private final List<Key> keys;

  protected CompondKey(List<Key> keys, byte[] value) {
    super(value);
    this.keys = keys;
  }

  public List<Key> getKeys() {
    return keys;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {
    private final List<Key> keys = new ArrayList<>();

    public void append(Key key) {
      keys.add(key);
    }

    public CompondKey build() {
      int totalLen = 0;
      for (Key key : keys) {
        totalLen += key.getBytes().length;
      }
      CodecDataOutput cdo = new CodecDataOutput(totalLen);
      for (Key key : keys) {
        cdo.write(key.getBytes());
      }
      return new CompondKey(keys, cdo.toBytes());
    }
  }

  @Override
  public String toString() {
    return String.format("[%s]", Joiner.on(",").useForNull("Null").join(keys));
  }
}