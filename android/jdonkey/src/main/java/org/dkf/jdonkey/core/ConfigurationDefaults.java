/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2016, FrostWire(R). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dkf.jdonkey.core;

import android.os.Environment;
import org.dkf.jed2k.exception.JED2KException;
import org.dkf.jed2k.protocol.server.ServerMet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author gubatron
 * @author aldenml
 */
final class ConfigurationDefaults {

    private final Map<String, Object> defaultValues;
    private final Map<String, Object> resetValues;

    ConfigurationDefaults() {
        defaultValues = new HashMap<>();
        resetValues = new HashMap<>();
        load();
    }

    Map<String, Object> getDefaultValues() {
        return Collections.unmodifiableMap(defaultValues);
    }

    Map<String, Object> getResetValues() {
        return Collections.unmodifiableMap(resetValues);
    }

    private void load() {
        defaultValues.put(Constants.PREF_KEY_STORAGE_PATH, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        defaultValues.put(Constants.PREF_KEY_CORE_UUID, uuidToByteArray(UUID.randomUUID()));
        defaultValues.put(Constants.PREF_KEY_CORE_LAST_SEEN_VERSION, "");//won't know until I see it.

        defaultValues.put(Constants.PREF_KEY_GUI_VIBRATE_ON_FINISHED_DOWNLOAD, true);
        defaultValues.put(Constants.PREF_KEY_GUI_LAST_MEDIA_TYPE_FILTER, Constants.FILE_TYPE_AUDIO);
        defaultValues.put(Constants.PREF_KEY_GUI_TOS_ACCEPTED, false);
        defaultValues.put(Constants.PREF_KEY_GUI_ALREADY_RATED_US_IN_MARKET, false);
        defaultValues.put(Constants.PREF_KEY_GUI_FINISHED_DOWNLOADS_BETWEEN_RATINGS_REMINDER, 10);
        defaultValues.put(Constants.PREF_KEY_GUI_INITIAL_SETTINGS_COMPLETE, false);
        defaultValues.put(Constants.PREF_KEY_GUI_ENABLE_PERMANENT_STATUS_NOTIFICATION, true);
        defaultValues.put(Constants.PREF_KEY_GUI_SHOW_TRANSFERS_ON_DOWNLOAD_START, true);
        defaultValues.put(Constants.PREF_KEY_GUI_SHOW_NEW_TRANSFER_DIALOG, true);

        defaultValues.put(Constants.PREF_KEY_SEARCH_COUNT_DOWNLOAD_FOR_TORRENT_DEEP_SCAN, 20);
        defaultValues.put(Constants.PREF_KEY_SEARCH_COUNT_ROUNDS_FOR_TORRENT_DEEP_SCAN, 10);
        defaultValues.put(Constants.PREF_KEY_SEARCH_INTERVAL_MS_FOR_TORRENT_DEEP_SCAN, 2000);
        defaultValues.put(Constants.PREF_KEY_SEARCH_MIN_SEEDS_FOR_TORRENT_DEEP_SCAN, 20); // this number must be bigger than PREF_KEY_SEARCH_MIN_SEEDS_FOR_TORRENT_RESULT to become relevant
        defaultValues.put(Constants.PREF_KEY_SEARCH_MIN_SEEDS_FOR_TORRENT_RESULT, 20);
        defaultValues.put(Constants.PREF_KEY_SEARCH_MAX_TORRENT_FILES_TO_INDEX, 100); // no ultra big torrents here
        defaultValues.put(Constants.PREF_KEY_SEARCH_FULLTEXT_SEARCH_RESULTS_LIMIT, 256);

        defaultValues.put(Constants.PREF_KEY_NETWORK_USE_MOBILE_DATA, true);
        defaultValues.put(Constants.PREF_KEY_NETWORK_MAX_CONCURRENT_UPLOADS, 3);

        defaultValues.put(Constants.PREF_KEY_STORAGE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath()); // /mnt/sdcard

        resetValue(Constants.PREF_KEY_SEARCH_COUNT_DOWNLOAD_FOR_TORRENT_DEEP_SCAN);
        resetValue(Constants.PREF_KEY_SEARCH_COUNT_ROUNDS_FOR_TORRENT_DEEP_SCAN);
        resetValue(Constants.PREF_KEY_SEARCH_INTERVAL_MS_FOR_TORRENT_DEEP_SCAN);
        resetValue(Constants.PREF_KEY_SEARCH_MIN_SEEDS_FOR_TORRENT_DEEP_SCAN);
        resetValue(Constants.PREF_KEY_SEARCH_MIN_SEEDS_FOR_TORRENT_RESULT);
        resetValue(Constants.PREF_KEY_SEARCH_MAX_TORRENT_FILES_TO_INDEX);
        resetValue(Constants.PREF_KEY_SEARCH_FULLTEXT_SEARCH_RESULTS_LIMIT);


        defaultValues.put(Constants.PREF_KEY_NICKNAME, "Nickname");
        defaultValues.put(Constants.PREF_KEY_LISTEN_PORT, 30000L);
        defaultValues.put(Constants.PREF_KEY_CONN_SERVER_ON_START, false);
        defaultValues.put(Constants.PREF_KEY_SHOW_SERVER_MSG, true);

        // servers section
        ServerMet sm = new ServerMet();
        try {
            sm.addServer(ServerMet.ServerMetEntry.create("91.200.42.46", (short) 1176, "eMule Security No1", "www.emule-security.org"));
            sm.addServer(ServerMet.ServerMetEntry.create("176.103.48.36", (short)4184, "TV Underground", "Operated by TVUnderground.org.ru"));
            sm.addServer(ServerMet.ServerMetEntry.create("91.200.42.47", (short)3883, "eMule Security No2", "www.emule-security.org"));
            sm.addServer(ServerMet.ServerMetEntry.create("91.200.42.119", (short)9939, "eMule Security No3", "www.emule-security.org"));
            sm.addServer(ServerMet.ServerMetEntry.create("77.120.115.66", (short)5041, "eMule Security No4", "www.emule-security.org"));
            sm.addServer(ServerMet.ServerMetEntry.create("85.204.50.116", (short)4232, "!! www.Sharing-Devils.to No.3 !!", "www.Sharing-Devils.to"));
            sm.addServer(ServerMet.ServerMetEntry.create("emule.is74.ru", (short)4661, "IS74", "IS 74 emule server"));
            defaultValues.put(Constants.PREF_KEY_SERVERS_LIST, sm);
        } catch(JED2KException e) {
            // wtf?
        }
    }

    private void resetValue(String key) {
        resetValues.put(key, defaultValues.get(key));
    }

    private static byte[] uuidToByteArray(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        byte[] buffer = new byte[16];

        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) (msb >>> 8 * (7 - i));
        }
        for (int i = 8; i < 16; i++) {
            buffer[i] = (byte) (lsb >>> 8 * (7 - i));
        }

        return buffer;
    }
}
