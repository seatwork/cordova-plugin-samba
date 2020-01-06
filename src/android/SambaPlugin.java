/**
 * Samba Plugin
 * Copyright (c) 2019, CLOUDSEAT Inc.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses>.
 *
 * @author AiChen
 * @copyright (c) 2019, CLOUDSEAT Inc.
 * @license https://www.gnu.org/licenses
 * @link https://www.cloudseat.net
 */

package net.cloudseat.cordova;

import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONException;

/**
 * Plugin Main Class
 */
public class SambaPlugin extends CordovaPlugin {

    private static final SambaAdapter samba = new SambaAdapter();

    /**
     * Plugin main method
     */
    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callback)
        throws JSONException {

        switch (action) {
            case "auth":
                String username = args.getString(0);
                String password = args.getString(1);
                samba.setPrincipal(username, password);
                callback.success();
                break;
            case "list": list(args, callback); break;
            case "read": read(args, callback); break;
            case "upload": upload(args, callback); break;
            case "mkfile": mkfile(args, callback); break;
            case "mkdir": mkdir(args, callback); break;
            case "delete": delete(args, callback); break;
            default:
                callback.error("Undefined method:" + action);
                return false;
        }
        return true;
    }

    private void list(CordovaArgs args, CallbackContext callback) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = args.getString(0);
                    callback.success(samba.list(path));
                } catch (Exception e) {
                    callback.error(e.getMessage());
                }
            }
        });
    }

    private void read(CordovaArgs args, CallbackContext callback) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = args.getString(0);
                    callback.success(samba.read(path));
                } catch (Exception e) {
                    callback.error(e.getMessage());
                }
            }
        });
    }

    private void mkfile(CordovaArgs args, CallbackContext callback) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = args.getString(0);
                    callback.success(samba.mkfile(path));
                } catch (Exception e) {
                    callback.error(e.getMessage());
                }
            }
        });
    }

    private void mkdir(CordovaArgs args, CallbackContext callback) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = args.getString(0);
                    callback.success(samba.mkdir(path));
                } catch (Exception e) {
                    callback.error(e.getMessage());
                }
            }
        });
    }

    private void delete(CordovaArgs args, CallbackContext callback) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = args.getString(0);
                    samba.delete(path);
                    callback.success();
                } catch (Exception e) {
                    callback.error(e.getMessage());
                }
            }
        });
    }

    private void upload(CordovaArgs args, CallbackContext callback) throws JSONException {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String localPath = args.getString(0);
                    String smbPath = args.getString(1);

                    Context context = cordova.getActivity().getApplicationContext();
                    String nativePath = FilePathUtil.getNativePath(context, localPath);

                    int index = nativePath.lastIndexOf("/");
                    String fileName = nativePath.substring(index + 1);

                    callback.success(samba.upload(nativePath, smbPath + fileName));
                } catch (Exception e) {
                    callback.error(e.getMessage());
                }
            }
        });
    }

}
