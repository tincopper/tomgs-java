/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomgs.ratis.filestore.cli;

import com.tomgs.ratis.common.SubCommandBase;

import java.util.ArrayList;
import java.util.List;

/**
 * This class enumerates all the commands enqueued by FileStore state machine.
 */
public final class FileStore {
  private FileStore() {
  }

  public static List<SubCommandBase> getSubCommands() {
    List<SubCommandBase> commands = new ArrayList<>();
    commands.add(new Server());
    commands.add(new LoadGen());
    commands.add(new DataStream());
    return commands;
  }
}
