/**
 *    Copyright (c) [2021] [JDsen]
 *    [Software Name] is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *             http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.ss.pojo;

/**
 * @author JDsen99
 * @description
 * @createDate 2021/9/17-11:10
 */
public class Client {
    private int id;
    private String serverIP;
    private String serverName;
    private int port;
    private String username;
    private String password;
    private byte status;
    private int tps;
    private int thread;

    public Client() {
    }

    public Client(int id, String serverIP, String serverName, int port, String username, String password, byte status, int tps, int thread) {
        this.id = id;
        this.serverIP = serverIP;
        this.serverName = serverName;
        this.port = port;
        this.username = username;
        this.password = password;
        this.status = status;
        this.tps = tps;
        this.thread = thread;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public int getTps() {
        return tps;
    }

    public void setTps(int tps) {
        this.tps = tps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", serverIP='" + serverIP + '\'' +
                ", serverName='" + serverName + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", tps=" + tps +
                ", thread=" + thread +
                '}';
    }
}
